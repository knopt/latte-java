package Latte.Frontend;

import Latte.Absyn.*;
import Latte.Definitions.*;
import Latte.Exceptions.IllegalTypeException;
import Latte.Exceptions.TypeCheckException;

import java.util.Arrays;
import java.util.List;

import static Latte.Frontend.StatementTypeCheck.validateTypes;
import static Latte.Frontend.TypeCheck.getType;

public class ExpressionTypeCheck {

    public static TypeDefinition typeCheckExpr(Expr expr, Scope scope, CallableDeclaration callableDeclaration) {

        return expr.match(
                (var) -> typeCheckEVar(var, scope, callableDeclaration),
                (elitInt) -> typeCheckELitInt(),
                (elitTrue) -> typeCheckELitTrue(),
                (elitFalse) -> typeCheckELitFalse(),
                (eThis) -> typeCheckThis(eThis, callableDeclaration),
                ExpressionTypeCheck::typeCheckNull,
                (eApp) -> typeCheckEApp(eApp, scope, callableDeclaration),
                ExpressionTypeCheck::typeCheckString,
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
    }

    public static TypeDefinition getVariableType(String varName, Scope scope, CallableDeclaration callableDeclaration, int lineNum, int colNum) {
        TypeCheckException e;

        try {
            return scope.getVariable(varName, lineNum, colNum).getType();
        } catch (TypeCheckException exception) {
            e = exception;
        }

        if (callableDeclaration.isMethod() && callableDeclaration.getCallerType().isClassType()) {
            ClassTypeDefinition classTypeDefinition = callableDeclaration.getCallerType().getClassDefinition();
            if (classTypeDefinition.fields.containsKey(varName)) {
                return classTypeDefinition.fields.get(varName).getType();
            }
        }

        throw e;
    }

    public static TypeDefinition typeCheckEVar(EVar var, Scope scope, CallableDeclaration callableDeclaration) {
        return getVariableType(var.ident_, scope, callableDeclaration, var.line_num, var.col_num);
    }

    public static TypeDefinition typeCheckELitInt() {
        return new BasicTypeDefinition(BasicTypeName.INT);
    }

    public static TypeDefinition typeCheckELitTrue() {
        return new BasicTypeDefinition(BasicTypeName.BOOLEAN);
    }

    public static TypeDefinition typeCheckELitFalse() {
        return new BasicTypeDefinition(BasicTypeName.BOOLEAN);
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

    public static TypeDefinition typeCheckEApp(EApp eApp, Scope scope, CallableDeclaration callableDeclaration) {
        FunctionDeclaration func = scope.globalEnvironment.getFunction(eApp.ident_, eApp.line_num, eApp.line_num);

        validateCallablesArgumentsMatch(func, eApp.listexpr_, scope, callableDeclaration, eApp.line_num, eApp.col_num);

        return func.returnType;
    }

    public static TypeDefinition typeCheckString(EString str) {
        return new BasicTypeDefinition(BasicTypeName.STRING);
    }

    public static TypeDefinition typeCheckConstr(EConstr constr, Scope scope) {
        TypeDefinition typeDefinition = scope.getType(constr.ident_, constr.line_num, constr.col_num);

        if (!typeDefinition.isClassType()) {
            throw new TypeCheckException("Only instance of class type can be created. " + typeDefinition + " is not a class type",
                    constr.line_num, constr.col_num);
        }

        return typeDefinition;

    }

    public static TypeDefinition typeCheckArrConstr(EArrConstr arrConstr, Scope scope, CallableDeclaration callableDeclaration) {
        String elemTypeName = arrConstr.typename_.match(
                TypeCheck::getTypeName,
                TypeCheck::getTypeName
        );

        if (!scope.globalEnvironment.declaredTypes.containsKey(elemTypeName)) {
            throw new IllegalTypeException(elemTypeName, arrConstr.line_num, arrConstr.col_num);
        }

        TypeDefinition innerArrayTypeDefinition = scope.globalEnvironment.declaredTypes.get(elemTypeName);
        ArrayTypeDefinition arrayType = new ArrayTypeDefinition(innerArrayTypeDefinition);

        TypeDefinition indexType = typeCheckExpr(arrConstr.expr_, scope, callableDeclaration);

        validateTypes(new BasicTypeDefinition(BasicTypeName.INT), indexType, arrConstr.line_num, arrConstr.line_num);

        return arrayType;
    }

    public static TypeDefinition typeCheckENDArrayAcc(ENDArrAcc arrAcc, Scope scope, CallableDeclaration callableDeclaration) {
        TypeDefinition arrayType = typeCheckExpr(arrAcc.expr_1, scope, callableDeclaration);

        if (!arrayType.isArrayType()) {
            throw new TypeCheckException("Expected array type for bracket operator, got " + arrayType, arrAcc.line_num, arrAcc.col_num);
        }

        TypeDefinition indexType = typeCheckExpr(arrAcc.expr_2, scope, callableDeclaration);

        validateTypes(new BasicTypeDefinition(BasicTypeName.INT), indexType, arrAcc.line_num, arrAcc.line_num);

        return arrayType.getArrayTypeDefinition().getInnerTypeDefinition();
    }

    public static TypeDefinition typeCheckNeg(Neg neg, Scope scope, CallableDeclaration callableDeclaration) {
        TypeDefinition exprType = typeCheckExpr(neg.expr_, scope, callableDeclaration);

        validateTypes(new BasicTypeDefinition(BasicTypeName.INT), exprType, neg.line_num, neg.col_num);

        return new BasicTypeDefinition(BasicTypeName.INT);
    }

    public static TypeDefinition typeCheckNot(Not not, Scope scope, CallableDeclaration callableDeclaration) {
        TypeDefinition exprType = typeCheckExpr(not.expr_, scope, callableDeclaration);

        validateTypes(new BasicTypeDefinition(BasicTypeName.BOOLEAN), exprType, not.line_num, not.col_num);

        return new BasicTypeDefinition(BasicTypeName.BOOLEAN);
    }

    public static TypeDefinition typeCheckMul(EMul mul, Scope scope, CallableDeclaration callableDeclaration) {
        TypeDefinition exprType1 = typeCheckExpr(mul.expr_1, scope, callableDeclaration);
        TypeDefinition exprType2 = typeCheckExpr(mul.expr_2, scope, callableDeclaration);

        validateTypes(new BasicTypeDefinition(BasicTypeName.INT), exprType1, mul.line_num, mul.col_num);
        validateTypes(new BasicTypeDefinition(BasicTypeName.INT), exprType2, mul.line_num, mul.col_num);


        return new BasicTypeDefinition(BasicTypeName.INT);
    }

    public static TypeDefinition typeCheckAdd(EAdd add, Scope scope, CallableDeclaration callableDeclaration) {
        TypeDefinition exprType1 = typeCheckExpr(add.expr_1, scope, callableDeclaration);
        TypeDefinition exprType2 = typeCheckExpr(add.expr_2, scope, callableDeclaration);

        // check they're equal
        validateTypes(exprType1, exprType2, add.line_num, add.col_num);
        // check they can be added
        validateTypes(
                Arrays.asList(
                    new BasicTypeDefinition(BasicTypeName.INT),
                    new BasicTypeDefinition(BasicTypeName.STRING)
                ),
                exprType1, add.line_num, add.col_num);

        return exprType1;
    }


    public static TypeDefinition typeCheckRel(ERel rel, Scope scope, CallableDeclaration callableDeclaration) {
        TypeDefinition exprType1 = typeCheckExpr(rel.expr_1, scope, callableDeclaration);
        TypeDefinition exprType2 = typeCheckExpr(rel.expr_2, scope, callableDeclaration);

        validateTypes(exprType1, exprType2, rel.line_num, rel.col_num);

        List<BasicTypeDefinition> eqTypes = Arrays.asList(
                new BasicTypeDefinition(BasicTypeName.STRING),
                new BasicTypeDefinition(BasicTypeName.INT),
                new BasicTypeDefinition(BasicTypeName.BOOLEAN)
        );

        rel.relop_.match(
                (ignored) -> validateTypes(new BasicTypeDefinition(BasicTypeName.INT), exprType1, rel.line_num, rel.col_num),
                (ignored) -> validateTypes(new BasicTypeDefinition(BasicTypeName.INT), exprType1, rel.line_num, rel.col_num),
                (ignored) -> validateTypes(new BasicTypeDefinition(BasicTypeName.INT), exprType1, rel.line_num, rel.col_num),
                (ignored) -> validateTypes(new BasicTypeDefinition(BasicTypeName.INT), exprType1, rel.line_num, rel.col_num),
                (ignored) -> validateTypes(eqTypes, exprType1, rel.line_num, rel.col_num),
                (ignored) -> validateTypes(eqTypes, exprType1, rel.line_num, rel.col_num)
        );

        return new BasicTypeDefinition(BasicTypeName.BOOLEAN);
    }

    public static TypeDefinition typeCheckAnd(EAnd and, Scope scope, CallableDeclaration callableDeclaration) {
        TypeDefinition exprType1 = typeCheckExpr(and.expr_1, scope, callableDeclaration);
        TypeDefinition exprType2 = typeCheckExpr(and.expr_2, scope, callableDeclaration);

        validateTypes(new BasicTypeDefinition(BasicTypeName.BOOLEAN), exprType1, and.line_num, and.col_num);
        validateTypes(new BasicTypeDefinition(BasicTypeName.BOOLEAN), exprType2, and.line_num, and.col_num);


        return new BasicTypeDefinition(BasicTypeName.BOOLEAN);
    }

    public static TypeDefinition typeCheckOr(EOr eOr, Scope scope, CallableDeclaration callableDeclaration) {
        TypeDefinition exprType1 = typeCheckExpr(eOr.expr_1, scope, callableDeclaration);
        TypeDefinition exprType2 = typeCheckExpr(eOr.expr_2, scope, callableDeclaration);

        validateTypes(new BasicTypeDefinition(BasicTypeName.BOOLEAN), exprType1, eOr.line_num, eOr.col_num);
        validateTypes(new BasicTypeDefinition(BasicTypeName.BOOLEAN), exprType2, eOr.line_num, eOr.col_num);


        return new BasicTypeDefinition(BasicTypeName.BOOLEAN);
    }

    public static TypeDefinition typeCheckObjAcc(EObjAcc objAcc, Scope scope, CallableDeclaration callableDeclaration) {
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

    public static TypeDefinition typeCheckObjMthAcc(ObjMethAcc mthAcc, TypeDefinition objTypeDefinition, Scope scope, CallableDeclaration callableDeclaration) {
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

    public static TypeDefinition typeCheckCast(ECast cast, Scope scope, CallableDeclaration callableDeclaration) {
        TypeDefinition castedType = typeCheckExpr(cast.expr_, scope, callableDeclaration);

        TypeDefinition castedToType = getType(cast.typename_, cast.line_num, cast.col_num);

        if (castedToType.isClassType()) {
            validateTypes(new NullTypeDefinition(), castedType, cast.line_num, cast.col_num);
            return castedToType;
        }

        if (new BasicTypeDefinition(BasicTypeName.VOID).equals(castedToType.isBasicType())) {
            return castedToType;
        }

        throw new TypeCheckException(castedType + " cannot be casted to " + castedToType, cast.line_num, cast.col_num);
    }

    private static void validateCallablesArgumentsMatch(CallableDeclaration callable, ListExpr listExpr, Scope scope, CallableDeclaration callableDeclaration, int lineNumber, int colNumber) {
        if (callable.getArgumentList().size() != listExpr.size()) {
            throw new TypeCheckException("Number of method " + callable.getName() + " arguments doesn't match. Expected " +  callable.getArgumentList().size() + ", got " + listExpr.size());
        }

        for (int i = 0; i < callable.getArgumentList().size(); i++) {
            TypeDefinition exprType = typeCheckExpr(listExpr.get(i), scope, callableDeclaration);
            TypeDefinition definiedType = callable.getArgumentList().get(i).type;

            validateTypes(definiedType, exprType, lineNumber, colNumber);
        }
    }




}
