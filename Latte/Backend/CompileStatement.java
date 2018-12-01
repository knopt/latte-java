package Latte.Backend;

import Latte.Absyn.Ret;
import Latte.Absyn.Stmt;
import Latte.Backend.Instructions.AssemblyInstruction;
import Latte.Backend.Instructions.Return;
import Latte.Exceptions.CompilerException;

import java.util.ArrayList;
import java.util.List;

import static Latte.Backend.CompileExpression.generateExpr;

public class CompileStatement {

    public static List<AssemblyInstruction> generateStmt(Stmt stmt) {
        return stmt.match(
                (x) -> notImplemented(x),
                (x) -> notImplemented(x),
                (x) -> notImplemented(x),
                (x) -> notImplemented(x),
                (x) -> notImplemented(x),
                (x) -> notImplemented(x),
                (ret) -> generateReturn(ret),
                (x) -> notImplemented(x),
                (x) -> notImplemented(x),
                (x) -> notImplemented(x),
                (x) -> notImplemented(x),
                (x) -> notImplemented(x),
                (x) -> notImplemented(x)
        );
    }

    public static List<AssemblyInstruction> generateReturn(Ret ret) {
        List<AssemblyInstruction> retExpr = new ArrayList<>(generateExpr(ret.expr_, Register.RAX, Register.RAX));
        retExpr.add(new Return());

        return retExpr;
    }

    public static List<AssemblyInstruction> notImplemented(Stmt stmt) {
        throw new CompilerException("Compiling statement " + stmt.getClass() + " not implemented yet");
    }

}
