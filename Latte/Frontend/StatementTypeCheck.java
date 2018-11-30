package Latte.Frontend;

import Latte.Absyn.*;
import Latte.Definitions.*;
import Latte.Exceptions.IllegalTypeException;
import Latte.Exceptions.InternalStateException;
import Latte.Exceptions.TypeCheckException;
import Latte.Exceptions.TypeMismatchException;

import java.util.List;

import static Latte.Frontend.ExpressionTypeCheck.typeCheckExpr;


public class StatementTypeCheck {
    public static void typeCheckStatement(Stmt stmt, boolean thisAllowed, Scope scope, CallableDeclaration callableDeclaration) {
        stmt.match(
                StatementTypeCheck::typeCheckEmpty,
                (bStmt) -> typeCheckBStmt(bStmt, thisAllowed, scope, callableDeclaration),
                (decl) -> typeCheckDecl(decl, thisAllowed, scope, callableDeclaration),
                (ass) -> typeCheckAss(ass, thisAllowed, scope, callableDeclaration),
                (incr) -> typeCheckIncr(incr, thisAllowed, scope, callableDeclaration),
                (decr) -> typeCheckDecr(decr, thisAllowed, scope, callableDeclaration),
                (ret) -> typeCheckRet(ret, thisAllowed, scope, callableDeclaration),
                (vret) -> typeCheckVRet(vret, callableDeclaration),
                (cond) -> typeCheckCond(cond, thisAllowed, scope, callableDeclaration),
                (condElse) -> typeCheckCondElse(condElse, thisAllowed, scope, callableDeclaration),
                (sWhile) -> typeCheckWhile(sWhile, thisAllowed, scope, callableDeclaration),
                (expr) -> typeCheckExpression(expr, thisAllowed, scope, callableDeclaration),
                (forArr) -> typeCheckForArray(forArr, thisAllowed, scope, callableDeclaration)
        );
    }

    public static Boolean typeCheckEmpty(Empty empty) {
        return true;
    }

    public static Boolean typeCheckBStmt(BStmt bStmt, boolean thisAllowed, Scope oldScope, CallableDeclaration callableDeclaration) {
        Scope scope = new Scope(oldScope);

        bStmt.block_.match(
                (block) -> typeCheckBlockS(block, thisAllowed, scope, callableDeclaration)
        );

        return true;
    }

    public static Boolean typeCheckBlockS(BlockS blockS, boolean thisAllowed, Scope scope, CallableDeclaration callableDeclaration) {
        for (Stmt stmt : blockS.liststmt_) {
            typeCheckStatement(stmt, thisAllowed, scope, callableDeclaration);
        }

        return true;
    }

    public static Boolean typeCheckDecl(Decl decl, boolean thisAllowed, Scope scope, CallableDeclaration callableDeclaration) {
        TypeDefinition type = decl.type_.match(
                TypeCheck::getType,
                TypeCheck::getType
        );

        for (Item item : decl.listitem_) {
            item.match(
                    (noInit) -> typeCheckNoInit(noInit, type, scope),
                    (init) -> typeCheckInit(init, type, thisAllowed, scope, callableDeclaration)
            );
        }

        return true;
    }

    public static Boolean typeCheckInit(Init init, TypeDefinition itemType, boolean thisAllowed, Scope scope, CallableDeclaration callableDeclaration) {
        TypeDefinition valueType = typeCheckExpr(init.expr_, thisAllowed, scope, callableDeclaration);

        validateTypes(itemType, valueType, init.line_num, init.col_num);

        scope.declareVariable(init.ident_, itemType, init.line_num, init.col_num);

        return true;
    }

    public static Boolean typeCheckNoInit(NoInit noInit, TypeDefinition itemType, Scope scope) {
        scope.declareVariable(noInit.ident_, itemType, noInit.line_num, noInit.col_num);

        return true;
    }

    public static Boolean typeCheckAss(Ass ass, boolean thisAllowed, Scope scope, CallableDeclaration callableDeclaration) {
        TypeDefinition lhs = getLhsType(ass.lhs_, thisAllowed, scope, callableDeclaration);

        if (new BasicTypeDefinition(BasicTypeName.VOID).equals(lhs)) {
            throw new TypeCheckException("Void type is not assignable", ass.line_num, ass.col_num);
        }

        TypeDefinition exprType = typeCheckExpr(ass.expr_, thisAllowed, scope, callableDeclaration);

        validateTypes(lhs, exprType, ass.line_num, ass.col_num);

        return true;
    }

    public static TypeDefinition getLhsType(Lhs lhs, boolean thisAllowed, Scope scope, CallableDeclaration callableDeclaration) {
        return lhs.match(
                (rLhs) -> scope.getVariable(rLhs.ident_, rLhs.line_num, rLhs.col_num).getType(),
                (aLhs) -> getALhsType(aLhs, thisAllowed, scope, callableDeclaration)
        );
    }

    public static TypeDefinition getALhsType(ArrElemLhs elem, boolean thisAllowed, Scope scope, CallableDeclaration callableDeclaration) {
        TypeDefinition typeDefinition = scope.getVariable(elem.ident_, elem.line_num, elem.col_num).getType();

        if (!typeDefinition.isArrayType()) {
            throw new InternalStateException("Expected array type in lhs. Should have been caught earlier");
        }

        TypeDefinition indexExprType = typeCheckExpr(elem.expr_, thisAllowed, scope, callableDeclaration);

        validateTypes(new BasicTypeDefinition(BasicTypeName.INT), indexExprType, elem.line_num, elem.col_num);

        return typeDefinition.getArrayTypeDefinition().getInnerTypeDefinition();
    }

    public static Boolean typeCheckIncr(Incr incr, boolean thisAllowed, Scope scope, CallableDeclaration callableDeclaration) {
        TypeDefinition lhsType = getLhsType(incr.lhs_, thisAllowed, scope, callableDeclaration);

        validateTypes(new BasicTypeDefinition(BasicTypeName.INT), lhsType, incr.line_num, incr.col_num);

        return true;
    }

    public static Boolean typeCheckDecr(Decr decr, boolean thisAllowed, Scope scope, CallableDeclaration callableDeclaration) {
        TypeDefinition lhsType = getLhsType(decr.lhs_, thisAllowed, scope, callableDeclaration);

        validateTypes(new BasicTypeDefinition(BasicTypeName.INT), lhsType, decr.line_num, decr.col_num);

        return true;
    }

    public static Boolean typeCheckRet(Ret ret, boolean thisAllowed, Scope scope, CallableDeclaration callableDeclaration) {
        TypeDefinition retType = typeCheckExpr(ret.expr_, thisAllowed, scope, callableDeclaration);

        validateTypes(retType, callableDeclaration.getReturnType(), ret.line_num, ret.col_num);

        return true;
    }

    public static Boolean typeCheckVRet(VRet vret, CallableDeclaration callableDeclaration) {
        validateTypes(new BasicTypeDefinition(BasicTypeName.VOID), callableDeclaration.getReturnType(), vret.line_num, vret.col_num);

        return true;
    }

    public static Boolean typeCheckCond(Cond cond, boolean thisAllowed, Scope scope, CallableDeclaration callableDeclaration) {
        TypeDefinition condType = typeCheckExpr(cond.expr_, thisAllowed, scope, callableDeclaration);

        validateTypes(new BasicTypeDefinition(BasicTypeName.BOOLEAN), condType, cond.line_num, cond.col_num);

        typeCheckStatement(cond.stmt_, thisAllowed, scope, callableDeclaration);

        return true;
    }

    public static Boolean typeCheckCondElse(CondElse condElse, boolean thisAllowed, Scope scope, CallableDeclaration callableDeclaration) {
        TypeDefinition condType = typeCheckExpr(condElse.expr_, thisAllowed, scope, callableDeclaration);

        validateTypes(new BasicTypeDefinition(BasicTypeName.BOOLEAN), condType, condElse.line_num, condElse.col_num);

        typeCheckStatement(condElse.stmt_1, thisAllowed, scope, callableDeclaration);
        typeCheckStatement(condElse.stmt_2, thisAllowed, scope, callableDeclaration);

        return true;
    }

    public static Boolean typeCheckWhile(While sWhile, boolean thisAllowed, Scope scope, CallableDeclaration callableDeclaration) {
        TypeDefinition condType = typeCheckExpr(sWhile.expr_, thisAllowed, scope, callableDeclaration);

        validateTypes(new BasicTypeDefinition(BasicTypeName.BOOLEAN), condType, sWhile.line_num, sWhile.col_num);

        typeCheckStatement(sWhile.stmt_, thisAllowed, scope, callableDeclaration);

        return true;
    }

    public static Boolean typeCheckExpression(SExp expr, boolean thisAllowed, Scope scope, CallableDeclaration callableDeclaration) {
        typeCheckExpr(expr.expr_, thisAllowed, scope, callableDeclaration);

        return true;
    }

    public static Boolean typeCheckForArray(ForArr forArr, boolean thisAllowed, Scope scope, CallableDeclaration callableDeclaration) {
        TypeDefinition exprType = typeCheckExpr(forArr.expr_, thisAllowed, scope, callableDeclaration);

        if (!exprType.isArrayType()) {
            throw new TypeCheckException("Expected array type, got " + exprType, forArr.line_num, forArr.col_num);
        }

        ArrayTypeDefinition arrayType = exprType.getArrayTypeDefinition();

        String elemTypeName = forArr.typename_.match(
                TypeCheck::getTypeName,
                TypeCheck::getTypeName
        );

        if (!scope.globalEnvironment.declaredTypes.containsKey(elemTypeName)) {
            throw new IllegalTypeException(elemTypeName, forArr.line_num, forArr.col_num);
        }

        TypeDefinition elemType = scope.globalEnvironment.declaredTypes.get(elemTypeName);

        validateTypes(elemType, arrayType.getInnerTypeDefinition(), forArr.line_num, forArr.col_num);

        Scope withElemScope = new Scope(scope);
        withElemScope.declareVariable(forArr.ident_, elemType, forArr.line_num, forArr.col_num);

        typeCheckStatement(forArr.stmt_, thisAllowed, withElemScope, callableDeclaration);

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

    public static TypeDefinition validateTypes(TypeDefinition type1, TypeDefinition type2, int lineNum, int colNum) {
        if (typesMatch(type1, type2)) {
            return type1;
        }

        throw new TypeMismatchException(type1, type2, lineNum, colNum);
    }

    public static TypeDefinition validateTypes(List<BasicTypeDefinition> types, TypeDefinition type1, int lineNum, int colNum) {
        if (types.stream().anyMatch((type) -> typesMatch(type1, type))) {
            return type1;
        }

        throw new TypeMismatchException(types, type1, lineNum, colNum);
    }

    private static Boolean stmtReturns(Stmt stmt) {
        return stmt.match(
                (x) -> false, StatementTypeCheck::stmtReturns,  (x) -> false, (x) -> false, (x) -> false, (x) -> false,
                (x) -> true, (x) -> true,
                StatementTypeCheck::stmtReturns, StatementTypeCheck::stmtReturns, StatementTypeCheck::stmtReturns,
                (x) -> false, (x) -> false
        );
    }

    private static Boolean stmtReturns(BStmt bStmt) {
        return bStmt.block_.match((block) -> block.liststmt_.stream().anyMatch(StatementTypeCheck::stmtReturns));
    }

    private static Boolean stmtReturns(Cond cond) {
        Boolean exprTrue = exprTriviallyTrue(cond.expr_);

        return exprTrue && stmtReturns(cond.stmt_);
    }

    private static Boolean stmtReturns(While sWhile) {
        return exprTriviallyTrue(sWhile.expr_) && stmtReturns(sWhile.stmt_);
    }

    private static Boolean stmtReturns(CondElse cond) {
        Boolean exprTrue = exprTriviallyTrue(cond.expr_);
        Boolean exprFalse = exprTriviallyFalse(cond.expr_);
        Boolean ifReturns = stmtReturns(cond.stmt_1);
        Boolean elseReturns = stmtReturns(cond.stmt_2);

        return exprTrue && ifReturns ||
                exprFalse && elseReturns ||
                ifReturns && elseReturns;
    }

    private static Boolean exprTriviallyTrue(Expr expr) {
        return expr.match(
                (x) -> false, (x) -> false, (x) -> true, (x) -> false, (x) -> false, (x) -> false, (x) -> false,
                (x) -> false, (x) -> false, (x) -> false, (x) -> false, (x) -> false, (x) -> false, (x) -> false,
                (x) -> false, (x) -> false, (x) -> false, (x) -> false, (x) -> false
        );
    }

    private static Boolean exprTriviallyFalse(Expr expr) {
        return expr.match(
                (x) -> false, (x) -> false, (x) -> false, (x) -> true, (x) -> false, (x) -> false, (x) -> false,
                (x) -> false, (x) -> false, (x) -> false, (x) -> false, (x) -> false, (x) -> false, (x) -> false,
                (x) -> false, (x) -> false, (x) -> false, (x) -> false, (x) -> false
        );
    }

    public static Boolean checkCallableReturns(BlockS block, CallableDeclaration callableDeclaration) {
        if (new BasicTypeDefinition(BasicTypeName.VOID).equals(callableDeclaration.getReturnType())) {
            return true;
        }

        if (block.liststmt_.stream().anyMatch(StatementTypeCheck::stmtReturns)) {
            return true;
        }

        throw new TypeCheckException("Missing return statement", block.line_num, block.col_num);

    }
}
