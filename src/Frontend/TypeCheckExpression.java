package src.Frontend;

import src.Absyn.*;
import src.Backend.Definitions.Binding;
import src.Definitions.*;
import src.Exceptions.IllegalTypeException;
import src.Exceptions.TypeCheckException;

import java.util.Arrays;
import java.util.List;

import static src.Frontend.TypeUtils.getType;
import static src.Frontend.TypeUtils.validateTypes;

public class TypeCheckExpression {

    public static TypeDefinition typeCheckExpr(Expr expr, FrontendScope scope, CallableDeclaration callableDeclaration) {

        TypeDefinition typeDefinition = expr.match(
                (var) -> typeCheckEVar(var, scope, callableDeclaration),
                (elitInt) -> typeCheckELitInt(),
                (elitTrue) -> typeCheckELitTrue(),
                (elitFalse) -> typeCheckELitFalse(),
                (eThis) -> typeCheckThis(eThis, callableDeclaration),
                TypeCheckExpression::typeCheckNull,
                (eApp) -> typeCheckEApp(eApp, scope, callableDeclaration),
                (str) -> typeCheckString(str, scope),
                (constr) -> typeCheckConstr(constr, scope),
                (arrConstr) -> typeCheckArrConstr(arrConstr, scope, callableDeclaration),
                (arrAcc) -> typeCheckENDArrayAcc(arrAcc, scope, callableDeclaration),
                (neg) -> typeCheckNeg(neg, scope, callableDeclaration),
                (not) -> typeCheckNot(not, scope, callableDeclaration),
                (mul) -> typeCheckMul(mul, scope, callableDeclaration),
                (add) -> typeCheckAdd(add, scope, callableDeclaration),
                (rel) -> typeCheckRel(rel, scope, callableDeclaration),
                (and) -> typeCheckAnd(and, scope, callableDeclaration),
                (eOr) -> typeCheckOr(eOr, scope, callableDeclaration),
                (objAcc) -> typeCheckObjAcc(objAcc, scope, callableDeclaration),
                (cast) -> typeCheckCast(cast, scope, callableDeclaration)
        );

        expr.type = typeDefinition;
        return typeDefinition;
    }

    public static TypeDefinition typeCheckEVar(EVar var, FrontendScope scope, CallableDeclaration callableDeclaration) {
        return TypeUtils.getVariableType(var, var.ident_, scope, callableDeclaration, var.line_num, var.col_num);
    }

    public static TypeDefinition typeCheckELitInt() {
        return BasicTypeDefinition.INT;
    }

    public static TypeDefinition typeCheckELitTrue() {
        return BasicTypeDefinition.BOOLEAN;
    }

    public static TypeDefinition typeCheckELitFalse() {
        return BasicTypeDefinition.BOOLEAN;
    }

    public static TypeDefinition typeCheckThis(EThis eThis, CallableDeclaration callableDeclaration) {
        if (!callableDeclaration.isMethod()) {
            throw new TypeCheckException("This keyword can't be used outside of class method body", eThis.line_num, eThis.col_num);
        }

        return callableDeclaration.getCallerType();
    }

    public static TypeDefinition typeCheckNull(ENull eNull) {
        return new NullTypeDefinition();
    }

    public static TypeDefinition typeCheckEApp(EApp eApp, FrontendScope scope, CallableDeclaration callableDeclaration) {
        TypeCheckException e;

        eApp.binding = Binding.FUNCTION_BINDING;

        try {
            FunctionDeclaration func = scope.globalEnvironment.getFunction(eApp.ident_, eApp.line_num, eApp.line_num);
            validateCallablesArgumentsMatch(func, eApp.listexpr_, scope, callableDeclaration, eApp.line_num, eApp.col_num);
            return func.getReturnType();
        } catch (TypeCheckException ex) {
            e = ex;
        }

        if (callableDeclaration.isMethod()) {
            CallableDeclaration callable;
            eApp.binding = Binding.getMethodBinding(callableDeclaration.getCallerType());
            if (callableDeclaration.getCallerType().isClassType()) {
                callable = callableDeclaration.getCallerType().getClassDefinition().getCallableDeclaration(eApp.ident_, eApp.line_num, eApp.col_num);
            } else {
                callable = callableDeclaration.getCallerType().getInterfaceDefinition().getCallableDeclaration(eApp.ident_, eApp.line_num, eApp.col_num);
            }

            validateCallablesArgumentsMatch(callable, eApp.listexpr_, scope, callableDeclaration, eApp.line_num, eApp.col_num);

            return callable.getReturnType();
        }


        throw e;
    }

    public static TypeDefinition typeCheckString(EString str, FrontendScope scope) {
        scope.globalEnvironment.declareStringLiteral(str.string_);
        return BasicTypeDefinition.STRING;
    }

    public static TypeDefinition typeCheckConstr(EConstr constr, FrontendScope scope) {
        TypeDefinition typeDefinition = scope.getType(constr.ident_, constr.line_num, constr.col_num);

        if (!typeDefinition.isClassType()) {
            throw new TypeCheckException("Only instance of class type can be created. " + typeDefinition + " is not a class type",
                    constr.line_num, constr.col_num);
        }

        return typeDefinition;

    }

    public static TypeDefinition typeCheckArrConstr(EArrConstr arrConstr, FrontendScope scope, CallableDeclaration callableDeclaration) {
        String elemTypeName = arrConstr.typename_.match(
                TypeUtils::getTypeName,
                TypeUtils::getTypeName
        );

        if (!scope.globalEnvironment.declaredTypes.containsKey(elemTypeName)) {
            throw new IllegalTypeException(elemTypeName, arrConstr.line_num, arrConstr.col_num);
        }

        TypeDefinition innerArrayTypeDefinition = scope.globalEnvironment.declaredTypes.get(elemTypeName);
        ArrayTypeDefinition arrayType = new ArrayTypeDefinition(innerArrayTypeDefinition);

        TypeDefinition indexType = typeCheckExpr(arrConstr.expr_, scope, callableDeclaration);

        validateTypes(BasicTypeDefinition.INT, indexType, arrConstr.line_num, arrConstr.line_num);

        return arrayType;
    }

    public static TypeDefinition typeCheckENDArrayAcc(ENDArrAcc arrAcc, FrontendScope scope, CallableDeclaration callableDeclaration) {
        TypeDefinition arrayType = typeCheckExpr(arrAcc.expr_1, scope, callableDeclaration);

        if (!arrayType.isArrayType()) {
            throw new TypeCheckException("Expected array type for bracket operator, got " + arrayType, arrAcc.line_num, arrAcc.col_num);
        }

        TypeDefinition indexType = typeCheckExpr(arrAcc.expr_2, scope, callableDeclaration);

        validateTypes(BasicTypeDefinition.INT, indexType, arrAcc.line_num, arrAcc.line_num);

        return arrayType.getArrayTypeDefinition().getInnerTypeDefinition();
    }

    public static TypeDefinition typeCheckNeg(Neg neg, FrontendScope scope, CallableDeclaration callableDeclaration) {
        TypeDefinition exprType = typeCheckExpr(neg.expr_, scope, callableDeclaration);

        validateTypes(BasicTypeDefinition.INT, exprType, neg.line_num, neg.col_num);

        return BasicTypeDefinition.INT;
    }

    public static TypeDefinition typeCheckNot(Not not, FrontendScope scope, CallableDeclaration callableDeclaration) {
        TypeDefinition exprType = typeCheckExpr(not.expr_, scope, callableDeclaration);

        validateTypes(BasicTypeDefinition.BOOLEAN, exprType, not.line_num, not.col_num);

        return BasicTypeDefinition.BOOLEAN;
    }

    public static TypeDefinition typeCheckMul(EMul mul, FrontendScope scope, CallableDeclaration callableDeclaration) {
        TypeDefinition exprType1 = typeCheckExpr(mul.expr_1, scope, callableDeclaration);
        TypeDefinition exprType2 = typeCheckExpr(mul.expr_2, scope, callableDeclaration);

        validateTypes(BasicTypeDefinition.INT, exprType1, mul.line_num, mul.col_num);
        validateTypes(BasicTypeDefinition.INT, exprType2, mul.line_num, mul.col_num);


        return BasicTypeDefinition.INT;
    }

    public static TypeDefinition typeCheckAdd(EAdd add, FrontendScope scope, CallableDeclaration callableDeclaration) {
        TypeDefinition exprType1 = typeCheckExpr(add.expr_1, scope, callableDeclaration);
        TypeDefinition exprType2 = typeCheckExpr(add.expr_2, scope, callableDeclaration);

        // check they're equal
        validateTypes(exprType1, exprType2, add.line_num, add.col_num);
        // check they can be added

        add.addop_.match(
                (plus) -> validateTypes(Arrays.asList(BasicTypeDefinition.INT, BasicTypeDefinition.STRING), exprType1, add.line_num, add.col_num),
                (minus) -> validateTypes(BasicTypeDefinition.INT, exprType1, add.line_num, add.col_num)
        );


        add.type = exprType1;

        return exprType1;
    }

    public static TypeDefinition typeCheckRel(ERel rel, FrontendScope scope, CallableDeclaration callableDeclaration) {
        TypeDefinition exprType1 = typeCheckExpr(rel.expr_1, scope, callableDeclaration);
        TypeDefinition exprType2 = typeCheckExpr(rel.expr_2, scope, callableDeclaration);

        validateTypes(exprType1, exprType2, rel.line_num, rel.col_num);

        List<BasicTypeDefinition> types = Arrays.asList(BasicTypeDefinition.INT, BasicTypeDefinition.BOOLEAN);


        rel.relop_.match(
                (ignored) -> validateTypes(BasicTypeDefinition.INT, exprType1, rel.line_num, rel.col_num),
                (ignored) -> validateTypes(BasicTypeDefinition.INT, exprType1, rel.line_num, rel.col_num),
                (ignored) -> validateTypes(BasicTypeDefinition.INT, exprType1, rel.line_num, rel.col_num),
                (ignored) -> validateTypes(BasicTypeDefinition.INT, exprType1, rel.line_num, rel.col_num),
                (ignored) -> validateTypes(types, exprType1, rel.line_num, rel.col_num),
                (ignored) -> validateTypes(types, exprType1, rel.line_num, rel.col_num)
        );

        return BasicTypeDefinition.BOOLEAN;
    }

    public static TypeDefinition typeCheckAnd(EAnd and, FrontendScope scope, CallableDeclaration callableDeclaration) {
        TypeDefinition exprType1 = typeCheckExpr(and.expr_1, scope, callableDeclaration);
        TypeDefinition exprType2 = typeCheckExpr(and.expr_2, scope, callableDeclaration);

        validateTypes(BasicTypeDefinition.BOOLEAN, exprType1, and.line_num, and.col_num);
        validateTypes(BasicTypeDefinition.BOOLEAN, exprType2, and.line_num, and.col_num);


        return BasicTypeDefinition.BOOLEAN;
    }

    public static TypeDefinition typeCheckOr(EOr eOr, FrontendScope scope, CallableDeclaration callableDeclaration) {
        TypeDefinition exprType1 = typeCheckExpr(eOr.expr_1, scope, callableDeclaration);
        TypeDefinition exprType2 = typeCheckExpr(eOr.expr_2, scope, callableDeclaration);

        validateTypes(BasicTypeDefinition.BOOLEAN, exprType1, eOr.line_num, eOr.col_num);
        validateTypes(BasicTypeDefinition.BOOLEAN, exprType2, eOr.line_num, eOr.col_num);


        return BasicTypeDefinition.BOOLEAN;
    }

    public static TypeDefinition typeCheckObjAcc(EObjAcc objAcc, FrontendScope scope, CallableDeclaration callableDeclaration) {
        TypeDefinition typeDefinition = typeCheckExpr(objAcc.expr_, scope, callableDeclaration);

        return objAcc.objacc_.match(
                (fieldAcc) -> typeCheckObjFieldAcc(fieldAcc, typeDefinition),
                (mthAcc) -> typeCheckObjMthAcc(mthAcc, typeDefinition, scope, callableDeclaration)
        );
    }

    public static TypeDefinition typeCheckObjFieldAcc(ObjFieldAcc fieldAcc, TypeDefinition objTypeDefinition) {
        if (objTypeDefinition.isArrayType()) {
            return objTypeDefinition.getArrayTypeDefinition().getFieldDeclaration(fieldAcc.ident_, fieldAcc.line_num, fieldAcc.line_num).getType();
        }

        if (objTypeDefinition.isClassType()) {
            return objTypeDefinition.getClassDefinition().getFieldDeclaration(fieldAcc.ident_, fieldAcc.line_num, fieldAcc.line_num).getType();
        }

        throw new TypeCheckException("Type " + objTypeDefinition + " is not allowed to have any fields", fieldAcc.line_num, fieldAcc.col_num);
    }

    public static TypeDefinition typeCheckObjMthAcc(ObjMethAcc mthAcc, TypeDefinition objTypeDefinition, FrontendScope scope, CallableDeclaration callableDeclaration) {
        CallableDeclaration callable;

        if (!objTypeDefinition.isClassType() && !objTypeDefinition.isInterfaceType()) {
            throw new TypeCheckException("Type " + objTypeDefinition + " is not allowed to have any methods", mthAcc.line_num, mthAcc.col_num);
        }

        if (objTypeDefinition.isClassType()) {
            callable = objTypeDefinition.getClassDefinition().getCallableDeclaration(mthAcc.ident_, mthAcc.line_num, mthAcc.line_num);
        } else {
            callable = objTypeDefinition.getInterfaceDefinition().getCallableDeclaration(mthAcc.ident_, mthAcc.line_num, mthAcc.line_num);
        }

        validateCallablesArgumentsMatch(callable, mthAcc.listexpr_, scope, callableDeclaration, mthAcc.line_num, mthAcc.col_num);

        return callable.getReturnType();
    }

    public static TypeDefinition typeCheckCast(ECast cast, FrontendScope scope, CallableDeclaration callableDeclaration) {
        TypeDefinition castedType = typeCheckExpr(cast.expr_, scope, callableDeclaration);

        TypeDefinition castedToType = getType(cast.typename_, scope.globalEnvironment, cast.line_num, cast.col_num);

        if (castedToType.equals(castedType)) {
            return castedToType;
        }

        if (castedToType.isClassType()) {
            validateTypes(new NullTypeDefinition(), castedType, cast.line_num, cast.col_num);
            return castedToType;
        }

        if (castedToType.equals(BasicTypeDefinition.INT) && castedType.equals(BasicTypeDefinition.BOOLEAN)) {
            return castedToType;
        }

        if (BasicTypeDefinition.VOID.equals(castedToType.isBasicType())) {
            return castedToType;
        }

        throw new TypeCheckException(castedType + " cannot be casted to " + castedToType, cast.line_num, cast.col_num);
    }

    private static void validateCallablesArgumentsMatch(CallableDeclaration callable, ListExpr listExpr, FrontendScope scope, CallableDeclaration callableDeclaration, int lineNumber, int colNumber) {
        if (callable.getArgumentList().size() != listExpr.size()) {
            throw new TypeCheckException("Number of method " + callable.getName() + " arguments doesn't match. Expected " + callable.getArgumentList().size() + ", got " + listExpr.size());
        }

        for (int i = 0; i < callable.getArgumentList().size(); i++) {
            TypeDefinition exprType = typeCheckExpr(listExpr.get(i), scope, callableDeclaration);
            TypeDefinition definiedType = callable.getArgumentList().get(i).type;

            validateTypes(definiedType, exprType, lineNumber, colNumber);
        }
    }


    static Boolean exprTriviallyTrue(Expr expr) {
        return expr.match(
                (x) -> false, (x) -> false, (x) -> true, (x) -> false, (x) -> false, (x) -> false, (x) -> false,
                (x) -> false, (x) -> false, (x) -> false, (x) -> false, (x) -> false, (x) -> false, (x) -> false,
                (x) -> false, (x) -> false, (x) -> false, (x) -> false, (x) -> false, (x) -> false
        );
    }

    static Boolean exprTriviallyFalse(Expr expr) {
        return expr.match(
                (x) -> false, (x) -> false, (x) -> false, (x) -> true, (x) -> false, (x) -> false, (x) -> false,
                (x) -> false, (x) -> false, (x) -> false, (x) -> false, (x) -> false, (x) -> false, (x) -> false,
                (x) -> false, (x) -> false, (x) -> false, (x) -> false, (x) -> false, (x) -> false
        );
    }
}
