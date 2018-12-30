package Latte.Frontend;

import Latte.Absyn.*;
import Latte.Backend.Definitions.Binding;
import Latte.Definitions.ArrayTypeDefinition;
import Latte.Definitions.BasicTypeDefinition;
import Latte.Definitions.CallableDeclaration;
import Latte.Definitions.TypeDefinition;
import Latte.Exceptions.IllegalTypeException;
import Latte.Exceptions.InternalStateException;
import Latte.Exceptions.TypeCheckException;

import static Latte.Frontend.TypeCheckExpression.typeCheckExpr;
import static Latte.Frontend.TypeUtils.getType;
import static Latte.Frontend.TypeUtils.getVariableType;


public class TypeCheckStatement {
    public static void typeCheckStatement(Stmt stmt, FrontendScope scope, CallableDeclaration callableDeclaration) {
        stmt.match(
                TypeCheckStatement::typeCheckEmpty,
                (bStmt) -> typeCheckBStmt(bStmt, scope, callableDeclaration),
                (decl) -> typeCheckDecl(decl, scope, callableDeclaration),
                (ass) -> typeCheckAss(ass, scope, callableDeclaration),
                (incr) -> typeCheckIncr(incr, scope, callableDeclaration),
                (decr) -> typeCheckDecr(decr, scope, callableDeclaration),
                (ret) -> typeCheckRet(ret, scope, callableDeclaration),
                (vret) -> typeCheckVRet(vret, callableDeclaration),
                (cond) -> typeCheckCond(cond, scope, callableDeclaration),
                (condElse) -> typeCheckCondElse(condElse, scope, callableDeclaration),
                (sWhile) -> typeCheckWhile(sWhile, scope, callableDeclaration),
                (expr) -> typeCheckExpression(expr, scope, callableDeclaration),
                (forArr) -> typeCheckForArray(forArr, scope, callableDeclaration)
        );
    }

    public static Boolean typeCheckEmpty(Empty empty) {
        return true;
    }

    public static Boolean typeCheckBStmt(BStmt bStmt, FrontendScope oldScope, CallableDeclaration callableDeclaration) {
        FrontendScope scope = new FrontendScope(oldScope);

        bStmt.block_.match(
                (block) -> typeCheckBlockS(block, scope, callableDeclaration)
        );

        return true;
    }

    public static Boolean typeCheckBlockS(BlockS blockS, FrontendScope scope, CallableDeclaration callableDeclaration) {
        for (Stmt stmt : blockS.liststmt_) {
            typeCheckStatement(stmt, scope, callableDeclaration);
        }

        return true;
    }

    public static Boolean typeCheckDecl(Decl decl, FrontendScope scope, CallableDeclaration callableDeclaration) {
        TypeDefinition type = decl.type_.match(
                (arrayType) -> getType(arrayType, scope.globalEnvironment),
                (typeNameS) -> getType(typeNameS, scope.globalEnvironment)
        );

        for (Item item : decl.listitem_) {
            item.match(
                    (noInit) -> typeCheckNoInit(noInit, type, scope),
                    (init) -> typeCheckInit(init, type, scope, callableDeclaration)
            );
        }

        return true;
    }

    public static Boolean typeCheckInit(Init init, TypeDefinition itemType, FrontendScope scope, CallableDeclaration callableDeclaration) {
        TypeDefinition valueType = typeCheckExpr(init.expr_, scope, callableDeclaration);

        TypeUtils.validateTypes(itemType, valueType, init.line_num, init.col_num);

        scope.declareVariable(init.ident_, itemType, init.line_num, init.col_num);

        return true;
    }

    public static Boolean typeCheckNoInit(NoInit noInit, TypeDefinition itemType, FrontendScope scope) {
        scope.declareVariable(noInit.ident_, itemType, noInit.line_num, noInit.col_num);

        return true;
    }

    public static Boolean typeCheckAss(Ass ass, FrontendScope scope, CallableDeclaration callableDeclaration) {
        TypeDefinition lhs = getLhsType(ass.lhs_, scope, callableDeclaration);

        if (BasicTypeDefinition.VOID.equals(lhs)) {
            throw new TypeCheckException("Void type is not assignable", ass.line_num, ass.col_num);
        }

        TypeDefinition exprType = typeCheckExpr(ass.expr_, scope, callableDeclaration);

        TypeUtils.validateTypes(lhs, exprType, ass.line_num, ass.col_num);

        return true;
    }

    public static TypeDefinition getLhsType(Lhs lhs, FrontendScope scope, CallableDeclaration callableDeclaration) {
        return lhs.match(
                (rLhs) -> getVariableType(rLhs, rLhs.ident_, scope, callableDeclaration, rLhs.line_num, rLhs.col_num),
                (aLhs) -> getALhsType(aLhs, scope, callableDeclaration),
                (fLhs) -> getFLhsType(fLhs, scope, callableDeclaration)
        );
    }

    public static TypeDefinition getALhsType(ArrElemLhs elem, FrontendScope scope, CallableDeclaration callableDeclaration) {
        TypeDefinition typeDefinition = scope.getVariable(elem.ident_, elem.line_num, elem.col_num).getType();

        if (!typeDefinition.isArrayType()) {
            throw new InternalStateException("Expected array type in lhs. Should have been caught earlier");
        }

        TypeDefinition indexExprType = typeCheckExpr(elem.expr_, scope, callableDeclaration);

        TypeUtils.validateTypes(BasicTypeDefinition.INT, indexExprType, elem.line_num, elem.col_num);

        return typeDefinition.getArrayTypeDefinition().getInnerTypeDefinition();
    }

    public static TypeDefinition getFLhsType(FieldLhs lhs, FrontendScope scope, CallableDeclaration callable) {
        TypeDefinition type = typeCheckExpr(lhs.expr_, scope, callable);

        if (!type.isClassType()) {
            throw new TypeCheckException("Type " + type + " doesn't have any assignable fields", lhs.line_num, lhs.col_num);
        }

        lhs.binding = Binding.getFieldBinding(type);

        return type.getClassDefinition().getFieldDeclaration(lhs.ident_, lhs.line_num, lhs.line_num).getType();
    }

    public static Boolean typeCheckIncr(Incr incr, FrontendScope scope, CallableDeclaration callableDeclaration) {
        TypeDefinition lhsType = getLhsType(incr.lhs_, scope, callableDeclaration);

        TypeUtils.validateTypes(BasicTypeDefinition.INT, lhsType, incr.line_num, incr.col_num);

        return true;
    }

    public static Boolean typeCheckDecr(Decr decr, FrontendScope scope, CallableDeclaration callableDeclaration) {
        TypeDefinition lhsType = getLhsType(decr.lhs_, scope, callableDeclaration);

        TypeUtils.validateTypes(BasicTypeDefinition.INT, lhsType, decr.line_num, decr.col_num);

        return true;
    }

    public static Boolean typeCheckRet(Ret ret, FrontendScope scope, CallableDeclaration callableDeclaration) {
        TypeDefinition retType = typeCheckExpr(ret.expr_, scope, callableDeclaration);

        TypeUtils.validateTypes(retType, callableDeclaration.getReturnType(), ret.line_num, ret.col_num);

        return true;
    }

    public static Boolean typeCheckVRet(VRet vret, CallableDeclaration callableDeclaration) {
        TypeUtils.validateTypes(BasicTypeDefinition.VOID, callableDeclaration.getReturnType(), vret.line_num, vret.col_num);

        return true;
    }

    public static Boolean typeCheckCond(Cond cond, FrontendScope scope, CallableDeclaration callableDeclaration) {
        TypeDefinition condType = typeCheckExpr(cond.expr_, scope, callableDeclaration);

        TypeUtils.validateTypes(BasicTypeDefinition.BOOLEAN, condType, cond.line_num, cond.col_num);

        typeCheckStatement(cond.stmt_, scope, callableDeclaration);

        return true;
    }

    public static Boolean typeCheckCondElse(CondElse condElse, FrontendScope scope, CallableDeclaration callableDeclaration) {
        TypeDefinition condType = typeCheckExpr(condElse.expr_, scope, callableDeclaration);

        TypeUtils.validateTypes(BasicTypeDefinition.BOOLEAN, condType, condElse.line_num, condElse.col_num);

        typeCheckStatement(condElse.stmt_1, scope, callableDeclaration);
        typeCheckStatement(condElse.stmt_2, scope, callableDeclaration);

        return true;
    }

    public static Boolean typeCheckWhile(While sWhile, FrontendScope scope, CallableDeclaration callableDeclaration) {
        TypeDefinition condType = typeCheckExpr(sWhile.expr_, scope, callableDeclaration);

        TypeUtils.validateTypes(BasicTypeDefinition.BOOLEAN, condType, sWhile.line_num, sWhile.col_num);

        typeCheckStatement(sWhile.stmt_, scope, callableDeclaration);

        return true;
    }

    public static Boolean typeCheckExpression(SExp expr, FrontendScope scope, CallableDeclaration callableDeclaration) {
        typeCheckExpr(expr.expr_, scope, callableDeclaration);

        return true;
    }

    public static Boolean typeCheckForArray(ForArr forArr, FrontendScope scope, CallableDeclaration callableDeclaration) {
        TypeDefinition exprType = typeCheckExpr(forArr.expr_, scope, callableDeclaration);

        if (!exprType.isArrayType()) {
            throw new TypeCheckException("Expected array type, got " + exprType, forArr.line_num, forArr.col_num);
        }

        ArrayTypeDefinition arrayType = exprType.getArrayTypeDefinition();

        String elemTypeName = forArr.typename_.match(
                TypeUtils::getTypeName,
                TypeUtils::getTypeName
        );

        if (!scope.globalEnvironment.declaredTypes.containsKey(elemTypeName)) {
            throw new IllegalTypeException(elemTypeName, forArr.line_num, forArr.col_num);
        }

        TypeDefinition elemType = scope.globalEnvironment.declaredTypes.get(elemTypeName);

        TypeUtils.validateTypes(elemType, arrayType.getInnerTypeDefinition(), forArr.line_num, forArr.col_num);

        FrontendScope withElemScope = new FrontendScope(scope);
        withElemScope.declareVariable(forArr.ident_, elemType, forArr.line_num, forArr.col_num);

        typeCheckStatement(forArr.stmt_, withElemScope, callableDeclaration);

        return true;
    }

    public static Boolean typesMatch(TypeDefinition type1, TypeDefinition type2) {
        if (type1.equals(type2)) {
            return true;
        }

        if ((type1.isInterfaceType() || type1.isClassType()) && type2.isNullType()) {
            return true;
        }

        if (type1.isInterfaceType() && type2.isClassType() &&
                type1.getInterfaceDefinition().isImplementedBy(type2)) {
            return true;
        }

        return false;
    }

    private static Boolean stmtReturns(Stmt stmt) {
        return stmt.match(
                (x) -> false, TypeCheckStatement::stmtReturns, (x) -> false, (x) -> false, (x) -> false, (x) -> false,
                (x) -> true, (x) -> true,
                TypeCheckStatement::stmtReturns, TypeCheckStatement::stmtReturns, TypeCheckStatement::stmtReturns,
                (x) -> false, (x) -> false
        );
    }

    private static Boolean stmtReturns(BStmt bStmt) {
        return bStmt.block_.match((block) -> block.liststmt_.stream().anyMatch(TypeCheckStatement::stmtReturns));
    }

    private static Boolean stmtReturns(Cond cond) {
        Boolean exprTrue = TypeCheckExpression.exprTriviallyTrue(cond.expr_);

        return exprTrue && stmtReturns(cond.stmt_);
    }

    private static Boolean stmtReturns(While sWhile) {
        return TypeCheckExpression.exprTriviallyTrue(sWhile.expr_) && stmtReturns(sWhile.stmt_);
    }

    private static Boolean stmtReturns(CondElse cond) {
        Boolean exprTrue = TypeCheckExpression.exprTriviallyTrue(cond.expr_);
        Boolean exprFalse = TypeCheckExpression.exprTriviallyFalse(cond.expr_);
        Boolean ifReturns = stmtReturns(cond.stmt_1);
        Boolean elseReturns = stmtReturns(cond.stmt_2);

        return exprTrue && ifReturns ||
                exprFalse && elseReturns ||
                ifReturns && elseReturns;
    }

    public static Boolean checkCallableReturns(BlockS block, CallableDeclaration callableDeclaration) {
//        if (BasicTypeDefinition.VOID.equals(callableDeclaration.getReturnType())) {
//            return true;
//        }

        if (block.liststmt_.stream().anyMatch(TypeCheckStatement::stmtReturns)) {
            return true;
        }

        throw new TypeCheckException("Missing return statement", block.line_num, block.col_num);

    }
}
