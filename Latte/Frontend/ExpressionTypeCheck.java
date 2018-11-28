package Latte.Frontend;

import Latte.Absyn.*;
import Latte.Definitions.*;
import Latte.Exceptions.IllegalTypeException;
import Latte.Exceptions.TypeCheckException;

import static Latte.Frontend.StatementTypeCheck.validateTypes;

public class ExpressionTypeCheck {

    public static TypeDefinition typeCheckExpr(Expr expr, boolean thisAllowed, Scope scope, CallableDeclaration callableDeclaration) {

        return expr.match(
                (var) -> typeCheckEVar(var, scope),
                (elitInt) -> typeCheckELitInt(),
                (elitTrue) -> typeCheckELitTrue(),
                (elitFalse) -> typeCheckELitFalse(),
                (eThis) -> typeCheckThis(eThis, thisAllowed, callableDeclaration),
                ExpressionTypeCheck::typeCheckNull,
                ExpressionTypeCheck::typeCheckEApp,
                ExpressionTypeCheck::typeCheckString,
                (constr) -> typeCheckConstr(constr, scope),
                (arrConstr) -> typeCheckArrConstr(arrConstr, thisAllowed, scope, callableDeclaration),
                (arrAcc) -> typeCheckENDArrayAcc(arrAcc, thisAllowed, scope, callableDeclaration),
                (neg) -> typeCheckNeg(neg, thisAllowed, scope, callableDeclaration),
                (not) -> typeCheckNot(not, thisAllowed, scope, callableDeclaration),
                (mul) -> typeCheckMul(mul, thisAllowed, scope, callableDeclaration),
                (add) -> typeCheckAdd(add, thisAllowed, scope, callableDeclaration),
                (rel) -> typeCheckRel(rel, thisAllowed, scope, callableDeclaration),
                (and) -> typeCheckAnd(and, thisAllowed, scope, callableDeclaration),
                (eOr) -> typeCheckOr(eOr, thisAllowed, scope, callableDeclaration),
                (objAcc) -> typeCheckObjAcc(objAcc, thisAllowed, scope, callableDeclaration)
        );
    }

    public static TypeDefinition typeCheckEVar(EVar var, Scope scope) {
        return scope.getVariable(var.ident_, var.line_num, var.col_num).getType();
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

    public static TypeDefinition typeCheckThis(EThis eThis, boolean thisAllowed, CallableDeclaration callableDeclaration) {
        if (!thisAllowed) {
            throw new TypeCheckException("This keyword can't be used outside of class method body", eThis.line_num, eThis.col_num);
        }

        return callableDeclaration.getCallerType();
    }

    public static TypeDefinition typeCheckNull(ENull eNull) {
        return new NullTypeDefinition();
    }

    public static TypeDefinition typeCheckEApp(EApp eApp) {
        throw new TypeCheckException("Function application not supported yet", eApp.line_num, eApp.col_num);
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

    public static TypeDefinition typeCheckArrConstr(EArrConstr arrConstr, boolean thisAllowed, Scope scope, CallableDeclaration callableDeclaration) {
        String elemTypeName = arrConstr.typename_.match(
                TypeCheck::getTypeName,
                TypeCheck::getTypeName
        );

        if (!scope.globalEnvironment.declaredTypes.containsKey(elemTypeName)) {
            throw new IllegalTypeException(elemTypeName, arrConstr.line_num, arrConstr.col_num);
        }

        TypeDefinition innerArrayTypeDefinition = scope.globalEnvironment.declaredTypes.get(elemTypeName);
        ArrayTypeDefinition arrayType = new ArrayTypeDefinition(innerArrayTypeDefinition);

        TypeDefinition indexType = typeCheckExpr(arrConstr.expr_, thisAllowed, scope, callableDeclaration);

        validateTypes(new BasicTypeDefinition(BasicTypeName.INT), indexType, arrConstr.line_num, arrConstr.line_num);

        return arrayType;
    }

    public static TypeDefinition typeCheckENDArrayAcc(ENDArrAcc arrAcc, boolean thisAllowed, Scope scope, CallableDeclaration callableDeclaration) {
        TypeDefinition arrayType = typeCheckExpr(arrAcc.expr_1, thisAllowed, scope, callableDeclaration);

        if (!arrayType.isArrayType()) {
            throw new TypeCheckException("Expected array type for bracket operator, got " + arrayType, arrAcc.line_num, arrAcc.col_num);
        }

        TypeDefinition indexType = typeCheckExpr(arrAcc.expr_2, thisAllowed, scope, callableDeclaration);

        validateTypes(new BasicTypeDefinition(BasicTypeName.INT), indexType, arrAcc.line_num, arrAcc.line_num);

        return arrayType;
    }

    public static TypeDefinition typeCheckNeg(Neg neg, boolean thisAllowed, Scope scope, CallableDeclaration callableDeclaration) {
        TypeDefinition exprType = typeCheckExpr(neg.expr_, thisAllowed, scope, callableDeclaration);

        validateTypes(new BasicTypeDefinition(BasicTypeName.INT), exprType, neg.line_num, neg.col_num);

        return new BasicTypeDefinition(BasicTypeName.INT);
    }

    public static TypeDefinition typeCheckNot(Not not, boolean thisAllowed, Scope scope, CallableDeclaration callableDeclaration) {
        TypeDefinition exprType = typeCheckExpr(not.expr_, thisAllowed, scope, callableDeclaration);

        validateTypes(new BasicTypeDefinition(BasicTypeName.BOOLEAN), exprType, not.line_num, not.col_num);

        return new BasicTypeDefinition(BasicTypeName.BOOLEAN);
    }

    public static TypeDefinition typeCheckMul(EMul mul, boolean thisAllowed, Scope scope, CallableDeclaration callableDeclaration) {
        TypeDefinition exprType1 = typeCheckExpr(mul.expr_1, thisAllowed, scope, callableDeclaration);
        TypeDefinition exprType2 = typeCheckExpr(mul.expr_2, thisAllowed, scope, callableDeclaration);

        validateTypes(new BasicTypeDefinition(BasicTypeName.INT), exprType1, mul.line_num, mul.col_num);
        validateTypes(new BasicTypeDefinition(BasicTypeName.INT), exprType2, mul.line_num, mul.col_num);


        return new BasicTypeDefinition(BasicTypeName.INT);
    }

    public static TypeDefinition typeCheckAdd(EAdd add, boolean thisAllowed, Scope scope, CallableDeclaration callableDeclaration) {
        TypeDefinition exprType1 = typeCheckExpr(add.expr_1, thisAllowed, scope, callableDeclaration);
        TypeDefinition exprType2 = typeCheckExpr(add.expr_2, thisAllowed, scope, callableDeclaration);

        validateTypes(new BasicTypeDefinition(BasicTypeName.INT), exprType1, add.line_num, add.col_num);
        validateTypes(new BasicTypeDefinition(BasicTypeName.INT), exprType2, add.line_num, add.col_num);


        return new BasicTypeDefinition(BasicTypeName.INT);
    }


    public static TypeDefinition typeCheckRel(ERel rel, boolean thisAllowed, Scope scope, CallableDeclaration callableDeclaration) {
        TypeDefinition exprType1 = typeCheckExpr(rel.expr_1, thisAllowed, scope, callableDeclaration);
        TypeDefinition exprType2 = typeCheckExpr(rel.expr_2, thisAllowed, scope, callableDeclaration);

        validateTypes(new BasicTypeDefinition(BasicTypeName.BOOLEAN), exprType1, rel.line_num, rel.col_num);
        validateTypes(new BasicTypeDefinition(BasicTypeName.INT), exprType2, rel.line_num, rel.col_num);


        return new BasicTypeDefinition(BasicTypeName.BOOLEAN);
    }

    public static TypeDefinition typeCheckAnd(EAnd and, boolean thisAllowed, Scope scope, CallableDeclaration callableDeclaration) {
        TypeDefinition exprType1 = typeCheckExpr(and.expr_1, thisAllowed, scope, callableDeclaration);
        TypeDefinition exprType2 = typeCheckExpr(and.expr_2, thisAllowed, scope, callableDeclaration);

        validateTypes(new BasicTypeDefinition(BasicTypeName.BOOLEAN), exprType1, and.line_num, and.col_num);
        validateTypes(new BasicTypeDefinition(BasicTypeName.INT), exprType2, and.line_num, and.col_num);


        return new BasicTypeDefinition(BasicTypeName.BOOLEAN);
    }

    public static TypeDefinition typeCheckOr(EOr eOr, boolean thisAllowed, Scope scope, CallableDeclaration callableDeclaration) {
        TypeDefinition exprType1 = typeCheckExpr(eOr.expr_1, thisAllowed, scope, callableDeclaration);
        TypeDefinition exprType2 = typeCheckExpr(eOr.expr_2, thisAllowed, scope, callableDeclaration);

        validateTypes(new BasicTypeDefinition(BasicTypeName.BOOLEAN), exprType1, eOr.line_num, eOr.col_num);
        validateTypes(new BasicTypeDefinition(BasicTypeName.INT), exprType2, eOr.line_num, eOr.col_num);


        return new BasicTypeDefinition(BasicTypeName.BOOLEAN);
    }

    public static TypeDefinition typeCheckObjAcc(EObjAcc objAcc, boolean thisAllowed, Scope scope, CallableDeclaration callableDeclaration) {
        TypeDefinition typeDefinition = typeCheckExpr(objAcc.expr_, thisAllowed, scope, callableDeclaration);

        return objAcc.objacc_.match(
                (fieldAcc) -> typeCheckObjFieldAcc(fieldAcc, typeDefinition),
                (mthAcc) -> typeCheckObjMthAcc(mthAcc, typeDefinition, thisAllowed, scope, callableDeclaration)
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

    public static TypeDefinition typeCheckObjMthAcc(ObjMethAcc mthAcc, TypeDefinition objTypeDefinition, boolean thisAllowed, Scope scope, CallableDeclaration callableDeclaration) {
        CallableDeclaration callable;

        if (!objTypeDefinition.isClassType() || !objTypeDefinition.isInterfaceType()) {
            throw new TypeCheckException("Type " + objTypeDefinition + " is not allowed to have any methods", mthAcc.line_num, mthAcc.col_num);
        }

        if (objTypeDefinition.isClassType()) {
            callable = objTypeDefinition.getClassDefinition().getCallableDeclaration(mthAcc.ident_, mthAcc.line_num, mthAcc.line_num);
        } else {
            callable = objTypeDefinition.getInterfaceDefinition().getCallableDeclaration(mthAcc.ident_, mthAcc.line_num, mthAcc.line_num);
        }

        if (callable.getArgumentList().size() != mthAcc.listexpr_.size()) {
            throw new TypeCheckException("Number of method " + callable.getName() + " arguments doesn't match. Expected " +  callable.getArgumentList().size() + ", got " + mthAcc.listexpr_.size());
        }

        for (int i = 0; i < callable.getArgumentList().size(); i++) {
            TypeDefinition exprType = typeCheckExpr(mthAcc.listexpr_.get(i), thisAllowed, scope, callableDeclaration);
            TypeDefinition definiedType = callable.getArgumentList().get(i).type;

            validateTypes(definiedType, exprType, mthAcc.line_num, mthAcc.col_num);
        }

        return callable.getReturnType();
    }




}
