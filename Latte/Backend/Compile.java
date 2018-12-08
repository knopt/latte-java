package Latte.Backend;

import Latte.Absyn.BlockS;
import Latte.Absyn.MBody;
import Latte.Absyn.Stmt;
import Latte.Backend.Definitions.BackendScope;
import Latte.Backend.Definitions.Register;
import Latte.Backend.Instructions.*;
import Latte.Definitions.FunctionDeclaration;
import Latte.Frontend.Environment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Compile {
    Environment env;

    public List<AssemblyInstruction> code;

    public Compile(Environment env) {
        this.env = env;
        this.code = new LinkedList<>();
    }

    private void addInstructions(List<AssemblyInstruction> instructions) {
        this.code.addAll(instructions);
    }

    private void addInstructions(AssemblyInstruction instruction) {
        this.code.add(instruction);
    }

    public List<AssemblyInstruction> generateEntryInstructions() {
        return Arrays.asList(
                new CustomInstruction(ConstantUtils.TAB + "section .text"),
                new CustomInstruction(ConstantUtils.TAB + "align " + ConstantUtils.WORD_SIZE),
                new CustomInstruction(ConstantUtils.TAB + "global main")
        );
    }


    public void generate() {
        addInstructions(generateEntryInstructions());

        FunctionDeclaration func = env.declaredFunctions.get("main");

        generateFunction(func);
    }

    public void generateFunction(FunctionDeclaration func) {
        BackendScope scope = new BackendScope(this.env);

        addInstructions(new Comment("code for function " + func.getName()));
        addInstructions(new Label(func.getName()));
        addInstructions(generateEpilog());
        func.getMethodBody().match(
                null,
                (body) -> generateBody(body, scope)
        );
    }

    public List<AssemblyInstruction> generateEpilog() {
        List<AssemblyInstruction> instructions = new ArrayList<>();

        instructions.add(new PushInstruction(Register.RBP));
        instructions.add(new MovInstruction(Register.RBP, Register.RSP));

        return instructions;
    }

    public Boolean generateBody(MBody body, BackendScope scope) {
        BlockS block = body.block_.match((blockS) -> blockS);

        for (Stmt stmt : block.liststmt_) {
            addInstructions(CompileStatement.generateStmt(stmt, scope));
        }

        return true;
    }

    public void print() {
        for (AssemblyInstruction instr : this.code) {
            System.out.println(instr.yield());
        }
    }


}
