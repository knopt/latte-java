package Latte.Backend;

import Latte.Absyn.*;
import Latte.Backend.Definitions.BackendScope;
import Latte.Backend.Definitions.Register;
import Latte.Backend.Instructions.*;
import Latte.Definitions.TypeDefinition;
import Latte.Exceptions.CompilerException;
import Latte.Frontend.TypeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static Latte.Backend.CompileExpression.generateExpr;
import static Latte.Frontend.TypeUtils.getType;

public class CompileStatement {

    public static List<AssemblyInstruction> generateStmt(Stmt stmt, BackendScope scope) {
        return stmt.match(
                (x) -> notImplemented(x),
                (x) -> notImplemented(x),
                (decl) -> generateDecl(decl, scope),
                (ass) -> generateAss(ass, scope),
                (x) -> notImplemented(x),
                (x) -> notImplemented(x),
                (ret) -> generateReturn(ret, scope),
                (x) -> notImplemented(x),
                (x) -> notImplemented(x),
                (x) -> notImplemented(x),
                (x) -> notImplemented(x),
                (x) -> notImplemented(x),
                (x) -> notImplemented(x)
        );
    }

    public static List<AssemblyInstruction> generateDecl(Decl decl, BackendScope scope) {
        List<AssemblyInstruction> instructions = new ArrayList<>();

        instructions.add(new Comment(""));
        instructions.add(new Comment(" declaration"));

        TypeDefinition type = decl.type_.match(
                (arrayType) -> getType(arrayType, scope.getGlobalEnvironment()),
                (typeNameS) -> getType(typeNameS, scope.getGlobalEnvironment())
        );

        for (Item item : decl.listitem_) {
            instructions.addAll(item.match(
                    (noInit) -> generateNoInit(noInit, type, scope),
                    (init) -> generateInit(init, type, scope)
            ));
        }

        return instructions;
    }

    public static List<AssemblyInstruction> generateAss(Ass ass, BackendScope scope) {
        List<AssemblyInstruction> instructions = new ArrayList<>();

        instructions.add(new Comment(""));
        instructions.add(new Comment("assignment"));

        instructions.addAll(generateExpr(ass.expr_, Register.RAX, Register.RAX, scope));

        instructions.addAll(ass.lhs_.match(
                (var) -> generateVariableAss(var, Register.RAX, scope),
                (arr) -> generateVariableArrayAss(arr, Register.RAX, scope)
        ));

        return instructions;
    }

    public static List<AssemblyInstruction> generateVariableAss(VariableRawLhs var, String sourceRegister, BackendScope scope) {
        int varOffset = scope.getVariable(var.ident_).getOffset();

        AssemblyInstruction instruction = new MovInstruction(MemoryReference.getWithOffset(Register.RBP, varOffset), sourceRegister);

        return new ArrayList<>(Collections.singletonList(instruction));
    }

    public static List<AssemblyInstruction> generateVariableArrayAss(ArrElemLhs arrElem, String sourceRegister, BackendScope scope) {
        throw new CompilerException("ArrElemAss not implemented!");
    }

    public static List<AssemblyInstruction> generateNoInit(NoInit noInit, TypeDefinition type, BackendScope scope) {
        List<AssemblyInstruction> instructions = new ArrayList<>();

        instructions.add(new MovInstruction(Register.RAX, YieldUtils.number(0)));
        instructions.add(new PushInstruction(Register.RAX));

        scope.declareVariable(noInit.ident_, type);

        return instructions;
    }

    public static List<AssemblyInstruction> generateInit(Init init, TypeDefinition type, BackendScope scope) {
        List<AssemblyInstruction> instructions = generateExpr(init.expr_, Register.RAX, Register.RAX, scope);

        instructions.add(new PushInstruction(Register.RAX));

        scope.declareVariable(init.ident_, type);

        return instructions;
    }

    public static List<AssemblyInstruction> generateReturn(Ret ret, BackendScope scope) {
        List<AssemblyInstruction> instructions = new ArrayList<>(Arrays.asList(new Comment(""), new Comment("return from function")));
        instructions.addAll(generateExpr(ret.expr_, Register.RAX, Register.RAX, scope));

        instructions.add(new MovInstruction(Register.RSP, Register.RBP));
        instructions.add(new PopInstruction(Register.RBP));

        instructions.add(new Return());

        return instructions;
    }

    public static List<AssemblyInstruction> notImplemented(Stmt stmt) {
        throw new CompilerException("Compiling statement " + stmt.getClass() + " not implemented yet");
    }

}
