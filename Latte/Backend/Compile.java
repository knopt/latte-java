package Latte.Backend;

import Latte.Absyn.BlockS;
import Latte.Absyn.MBody;
import Latte.Absyn.Stmt;
import Latte.Backend.Instructions.AssemblyInstruction;
import Latte.Backend.Instructions.ConstantUtils;
import Latte.Backend.Instructions.CustomInstruction;
import Latte.Backend.Instructions.Label;
import Latte.Definitions.FunctionDeclaration;
import Latte.Frontend.Environment;

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
        addInstructions(new CustomInstruction("; code for function " + func.getName()));
        addInstructions(new Label(func.getName()));
        func.getMethodBody().match(
                null,
                (body) -> generateBody(body)
        );
    }

    public Boolean generateBody(MBody body) {
        BlockS block = body.block_.match((blockS) -> blockS);

        for (Stmt stmt : block.liststmt_) {
            addInstructions(CompileStatement.generateStmt(stmt));
        }

        return true;
    }

    public void print() {
        for (AssemblyInstruction instr : this.code) {
            System.out.println(instr.yield());
        }
    }


}
