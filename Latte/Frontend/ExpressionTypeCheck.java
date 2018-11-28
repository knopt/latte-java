package Latte.Frontend;

import Latte.Absyn.*;
import Latte.Definitions.*;
import Latte.Exceptions.TypeCheckException;

import static Latte.Frontend.StatementTypeCheck.validateTypes;

public class ExpressionTypeCheck {

    public static TypeDefinition typeCheckExpr(Expr expr, boolean thisAllowed, Scope scope, CallableDeclaration callableDeclaration) {
        return null;
        //        return expr.match(
//                (var) -> typeCheckEVar(var, scope),
//                (elitInt) -> typeCheckELitInt(),
//                (elitTrue) -> typeCheckELitTrue(),
//                (elitFalse) -> typeCheckELitFalse(),
//                (eThis) -> typeCheckThis(eThis, thisAllowed, callableDeclaration),
//                ExpressionTypeCheck::typeCheckNull,
//                ExpressionTypeCheck::typeCheckEApp,
//                ExpressionTypeCheck::typeCheckString,
//                (constr) -> typeCheckConstr(constr, scope),
//                (arrConstr) -> typeCheckArrConstr(arrConstr, thisAllowed, scope, callableDeclaration),
//                (arrAcc) -> typeCheckENDArrayAcc(arrAcc, thisAllowed, scope, callableDeclaration),
//
//        );
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
        TypeDefinition innerArrayTypeDefinition = scope.getType(arrConstr.ident_, arrConstr.line_num, arrConstr.col_num);
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


}
