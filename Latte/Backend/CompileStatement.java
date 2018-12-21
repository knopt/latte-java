package Latte.Backend;

import Latte.Absyn.*;
import Latte.Backend.Definitions.BackendScope;
import Latte.Backend.Definitions.Register;
import Latte.Backend.Instructions.*;
import Latte.Definitions.TypeDefinition;
import Latte.Exceptions.CompilerException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static Latte.Backend.CompileExpression.generateExpr;
import static Latte.Backend.Instructions.ConstantUtils.WORD_SIZE;
import static Latte.Frontend.TypeUtils.getType;
import static Latte.PrettyPrinter.print;

public class CompileStatement {

    public static List<AssemblyInstruction> generateStmt(Stmt stmt, BackendScope scope) {
        return stmt.match(
                (empty) -> new ArrayList<>(),
                (bStmt) -> generateBStmt(bStmt, scope),
                (decl) -> generateDecl(decl, scope),
                (ass) -> generateAss(ass, scope),
                (incr) -> generateIncr(incr, scope),
                (decr) -> generateDecr(decr, scope),
                (ret) -> generateReturn(ret, scope),
                (vret) -> generateVReturn(vret),
                (cond) -> generateCond(cond, scope),
                (condElse) -> generateCondElse(condElse, scope),
                (sWhile) -> generateWhile(sWhile, scope),
                (sExp) -> generateStmtExpr(sExp, scope),
                (x) -> notImplemented(x)
        );
    }

    public static List<AssemblyInstruction> generateDecr(Decr decr, BackendScope scope) {
        return decr.lhs_.match(
                (var) -> generateVarDecr(var, scope),
                (arr) -> generateArrDecr(arr, scope)
        );
    }

    public static List<AssemblyInstruction> generateIncr(Incr incr, BackendScope scope) {
        return incr.lhs_.match(
                (var) -> generateVarIncr(var, scope),
                (arr) -> generateArrIncr(arr, scope)
        );
    }

    public static List<AssemblyInstruction> generateVarIncr(VariableRawLhs var, BackendScope scope) {
        List<AssemblyInstruction> instructions = new ArrayList<>();

        int offset = scope.getVariable(var.ident_).getOffset() * WORD_SIZE;

        instructions.add(new MovInstruction(Register.RAX, MemoryReference.getWithOffset(Register.RBP, offset)));
        instructions.add(new AddInstruction(Register.RAX, YieldUtils.number(1)));
        instructions.add(new MovInstruction(MemoryReference.getWithOffset(Register.RBP, offset), Register.RAX));

        return instructions;
    }

    public static List<AssemblyInstruction> generateArrIncr(ArrElemLhs arrElem, BackendScope scope) {
        return notImplemented(null);
    }

    public static List<AssemblyInstruction> generateVarDecr(VariableRawLhs var, BackendScope scope) {
        List<AssemblyInstruction> instructions = new ArrayList<>();

        int offset = scope.getVariable(var.ident_).getOffset() * WORD_SIZE;

        instructions.add(new MovInstruction(Register.RAX, MemoryReference.getWithOffset(Register.RBP, offset)));
        instructions.add(new SubInstruction(Register.RAX, YieldUtils.number(1)));
        instructions.add(new MovInstruction(MemoryReference.getWithOffset(Register.RBP, offset), Register.RAX));

        return instructions;
    }

    public static List<AssemblyInstruction> generateArrDecr(ArrElemLhs arrElem, BackendScope scope) {
        return notImplemented(null);
    }

    public static List<AssemblyInstruction> generateDecl(Decl decl, BackendScope scope) {
        List<AssemblyInstruction> instructions = new ArrayList<>();

        instructions.add(new Comment(""));
        instructions.add(new Comment(" declaration " + print(decl)));

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

    public static List<AssemblyInstruction> generateStmtExpr(SExp exp, BackendScope scope) {
        return generateExpr(exp.expr_, Register.RAX, Register.RAX, scope);
    }

    public static List<AssemblyInstruction> generateWhile(While sWhile, BackendScope scope) {
        List<AssemblyInstruction> instructions = new ArrayList<>();

        Label start = LabelsGenerator.getNonceLabel("_start_while");
        Label end = LabelsGenerator.getNonceLabel("_end_start");

        instructions.add(start);
        instructions.addAll(generateExpr(sWhile.expr_, Register.RAX, Register.RAX, scope));
        instructions.add(new CompareInstruction(Register.RAX, YieldUtils.number(1)));
        instructions.add(new JumpInstruction(end, JumpInstruction.Type.NE));

        instructions.addAll(generateStmt(sWhile.stmt_, scope));
        instructions.add(new JumpInstruction(start, JumpInstruction.Type.ALWAYS));
        instructions.add(end);

        return instructions;
    }

    public static List<AssemblyInstruction> generateCond(Cond cond, BackendScope scope) {
        List<AssemblyInstruction> instructions = generateExpr(cond.expr_, Register.RAX, Register.RAX, scope);

        Label afterLabel = LabelsGenerator.getNonceLabel("_end_if");

        instructions.add(new CompareInstruction(Register.RAX, YieldUtils.number(1)));
        instructions.add(new JumpInstruction(afterLabel, JumpInstruction.Type.NE));

        instructions.addAll(generateStmt(cond.stmt_, scope));
        instructions.add(afterLabel);

        return instructions;
    }

    public static List<AssemblyInstruction> generateCondElse(CondElse cond, BackendScope scope) {
        List<AssemblyInstruction> instructions = generateExpr(cond.expr_, Register.RAX, Register.RAX, scope);

        Label afterLabel = LabelsGenerator.getNonceLabel("_end_if");
        Label elseLabel = LabelsGenerator.getNonceLabel("_else");

        instructions.add(new CompareInstruction(Register.RAX, YieldUtils.number(1)));
        instructions.add(new JumpInstruction(elseLabel, JumpInstruction.Type.NE));

        instructions.addAll(generateStmt(cond.stmt_1, scope));
        instructions.add(new JumpInstruction(afterLabel, JumpInstruction.Type.ALWAYS));

        instructions.add(elseLabel);
        instructions.addAll(generateStmt(cond.stmt_2, scope));
        instructions.add(afterLabel);

        return instructions;
    }


    public static List<AssemblyInstruction> generateBStmt(BStmt bStmt, BackendScope oldScope) {
        BackendScope scope = new BackendScope(oldScope);

        List<AssemblyInstruction> instructions = new ArrayList<>();

        instructions.add(new Comment(""));
        instructions.add(new Comment("block"));

        instructions.addAll(bStmt.block_.match(
                (blockS) -> generateBlockS(blockS, scope)
        ));

        int offset = oldScope.getNumberOfVariablesOnStack() * WORD_SIZE;

        instructions.add(new MovInstruction(Register.RSP, Register.RBP));
        instructions.add(new SubInstruction(Register.RSP, YieldUtils.number(offset)));

        instructions.add(new Comment(""));
        instructions.add(new Comment("end block"));

        return instructions;

    }

    public static List<AssemblyInstruction> generateBlockS(BlockS block, BackendScope scope) {
        List<AssemblyInstruction> instructions = new ArrayList<>();

        for (Stmt stmt : block.liststmt_) {
            instructions.addAll(generateStmt(stmt, scope));
        }

        return instructions;
    }

    public static List<AssemblyInstruction> generateAss(Ass ass, BackendScope scope) {
        List<AssemblyInstruction> instructions = new ArrayList<>();

        instructions.add(new Comment(""));
        instructions.add(new Comment("assignment " + print(ass)));

        instructions.addAll(generateExpr(ass.expr_, Register.RAX, Register.RAX, scope));

        instructions.addAll(ass.lhs_.match(
                (var) -> generateVariableAss(var, Register.RAX, scope),
                (arr) -> generateVariableArrayAss(arr, Register.RAX, scope)
        ));

        return instructions;
    }

    public static List<AssemblyInstruction> generateVariableAss(VariableRawLhs var, String sourceRegister, BackendScope scope) {
        int varOffset = scope.getVariable(var.ident_).getOffset() * WORD_SIZE;

        AssemblyInstruction instruction = new MovInstruction(MemoryReference.getWithOffset(Register.RBP, varOffset), sourceRegister);

        return new ArrayList<>(Collections.singletonList(instruction));
    }

    public static List<AssemblyInstruction> generateVariableArrayAss(ArrElemLhs arrElem, String registerOfValue, BackendScope scope) {
        int varOffset = scope.getVariable(arrElem.ident_).getOffset() * WORD_SIZE;
        List<AssemblyInstruction> instructions = new ArrayList<>();

        instructions.add(new PushInstruction(Register.RCX));

        if (!registerOfValue.equals(Register.RCX)) {
            instructions.add(new MovInstruction(Register.RCX, registerOfValue));
        }

        instructions.addAll(generateExpr(arrElem.expr_, Register.RAX, Register.RAX, scope));
        instructions.add(new AddInstruction(Register.RAX, YieldUtils.number(1)));
        instructions.add(new MulInstruction(Register.RAX, YieldUtils.number(WORD_SIZE)));
        instructions.add(new AddInstruction(Register.RAX, MemoryReference.getWithOffset(Register.RBP, varOffset)));

        instructions.add(new MovInstruction(MemoryReference.getRaw(Register.RAX), Register.RCX));

        instructions.add(new PopInstruction(Register.RCX));

        return instructions;
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

    private static List<AssemblyInstruction> generateEpilog() {
        List<AssemblyInstruction> instructions = new ArrayList<>();

        instructions.add(new MovInstruction(Register.RSP, Register.RBP));
        instructions.add(new PopInstruction(Register.RBP));

        instructions.add(new Return());

        return instructions;
    }

    public static List<AssemblyInstruction> generateReturn(Ret ret, BackendScope scope) {
        List<AssemblyInstruction> instructions = new ArrayList<>(Arrays.asList(new Comment(""), new Comment("return from function")));
        instructions.addAll(generateExpr(ret.expr_, Register.RAX, Register.RAX, scope));

        instructions.addAll(generateEpilog());

        return instructions;
    }

    public static List<AssemblyInstruction> generateVReturn(VRet ret) {
        List<AssemblyInstruction> instructions = new ArrayList<>(Arrays.asList(new Comment(""), new Comment("void return from function")));

        instructions.addAll(generateEpilog());

        return instructions;
    }

    public static List<AssemblyInstruction> notImplemented(Stmt stmt) {
        throw new CompilerException("Compiling statement " + stmt.getClass() + " not implemented yet");
    }

}
