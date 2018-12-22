package Latte.Frontend;

import Latte.Absyn.*;
import Latte.Definitions.CallableDeclaration;

import static java.lang.Math.max;

public class LocalVariablesCounter {
    private static class VariablesCount {
        public int numberOfLocalVariables;
        public int numberOfBlockVariables;

        public VariablesCount(int numberOfLocalVariables, int numberOfBlockVariables) {
            this.numberOfLocalVariables = numberOfLocalVariables;
            this.numberOfBlockVariables = numberOfBlockVariables;
        }
    }

    public static int callableVariablesCounter(CallableDeclaration callable) {
        int maximumNumberOfLocalVariables = 0;

        maximumNumberOfLocalVariables += callable.getArgumentList().size();

        Integer res = callable.getMethodBody().match(
                (emptyMBody) -> 0,
                (mBody) -> countVarsMBody(mBody)
        );

        return maximumNumberOfLocalVariables + res;
    }

    private static Integer countVarsMBody(MBody mBody) {
        return mBody.block_.match((blockS) -> countVarsBlockS(blockS)).numberOfBlockVariables;
    }

    private static VariablesCount countVarsBlockS(BlockS blockS) {
        int biggestBlock = 0;
        int totalNumberOfVars = 0;

        for (Stmt stmt : blockS.liststmt_) {
            VariablesCount vc = countVarsStmt(stmt);

            totalNumberOfVars += vc.numberOfLocalVariables;
            biggestBlock = max(biggestBlock, vc.numberOfBlockVariables);
        }

        return new VariablesCount(0, totalNumberOfVars + biggestBlock);
    }

    private static VariablesCount countVarsStmt(Stmt stmt) {
        return stmt.match(
                (empty) -> new VariablesCount(0, 0),
                LocalVariablesCounter::countVarsBStmt,
                LocalVariablesCounter::countVarsDecl,
                (ignored) -> new VariablesCount(0, 0),
                (ignored) -> new VariablesCount(0, 0),
                (ignored) -> new VariablesCount(0, 0),
                (ignored) -> new VariablesCount(0, 0),
                (ignored) -> new VariablesCount(0, 0),
                LocalVariablesCounter::countVarsCond,
                LocalVariablesCounter::countVarsCondElse,
                LocalVariablesCounter::countVarsSWhile,
                (ignored) -> new VariablesCount(0, 0),
                LocalVariablesCounter::countVarsForArr
        );
    }

    private static VariablesCount countVarsBStmt(BStmt bStmt) {
        return bStmt.block_.match(LocalVariablesCounter::countVarsBlockS);
    }

    private static VariablesCount countVarsDecl(Decl decl) {
        return new VariablesCount(decl.listitem_.size(), 0);
    }

    private static VariablesCount countVarsCond(Cond cond) {
        VariablesCount stmtVc = countVarsStmt(cond.stmt_);
        // either local or block are 0, we treat them always as the block variables
        return new VariablesCount(0, stmtVc.numberOfBlockVariables + stmtVc.numberOfLocalVariables);
    }

    private static VariablesCount countVarsCondElse(CondElse cond) {
        VariablesCount stmt1Vc = countVarsStmt(cond.stmt_1);
        VariablesCount stmt2Vc = countVarsStmt(cond.stmt_2);
        // either local or block are 0, we treat them always as the block variables
        int stmt1Num = stmt1Vc.numberOfBlockVariables + stmt1Vc.numberOfLocalVariables;
        int stmt2Num = stmt2Vc.numberOfBlockVariables + stmt2Vc.numberOfLocalVariables;
        return new VariablesCount(0, max(stmt1Num, stmt2Num));
    }

    private static VariablesCount countVarsSWhile(While sWhile) {
        VariablesCount stmtVc = countVarsStmt(sWhile.stmt_);
        // either local or block are 0, we treat them always as the block variables
        return new VariablesCount(0, stmtVc.numberOfBlockVariables + stmtVc.numberOfLocalVariables);
    }

    private static VariablesCount countVarsForArr(ForArr forArr) {
        VariablesCount stmtVc = countVarsStmt(forArr.stmt_);
        // either local or block are 0, we treat them always as the block variables, +1 for iterator
        return new VariablesCount(0, stmtVc.numberOfBlockVariables + stmtVc.numberOfLocalVariables + 1);
    }

}
