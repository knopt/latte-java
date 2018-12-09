package Latte.Backend;

import Latte.Absyn.BlockS;
import Latte.Absyn.MBody;
import Latte.Absyn.Stmt;
import Latte.Backend.Definitions.BackendScope;
import Latte.Backend.Definitions.Register;
import Latte.Backend.Instructions.*;
import Latte.Definitions.FunctionDeclaration;
import Latte.Definitions.VariableDefinition;
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
        List<AssemblyInstruction> instructions = new ArrayList<>();

        instructions.add(new CustomInstruction(ConstantUtils.TAB + "section .text"));
        instructions.add(new CustomInstruction(ConstantUtils.TAB + "align " + ConstantUtils.WORD_SIZE));

        for (FunctionDeclaration func : env.declaredFunctions.values()) {
            instructions.add(new CustomInstruction(ConstantUtils.TAB + "global " + func.getName()));
        }

        return instructions;
    }


    public void generate() {
        addInstructions(generateEntryInstructions());

        for (FunctionDeclaration func : env.declaredFunctions.values()) {
            if (!func.isExternal()) {
                generateFunction(func);
            }
        }
    }

    public void generateFunction(FunctionDeclaration func) {
        BackendScope scope = new BackendScope(this.env);

        addInstructions(new Comment("code for function " + func.getName()));
        addInstructions(new Label(func.getName()));
        addInstructions(generateProlog());

        generateEntryArguments(func, scope);

        BackendScope scopeWithArgs = new BackendScope(scope);

        func.getMethodBody().match(
                null,
                (body) -> generateBody(body, scopeWithArgs)
        );
    }

    public void generateEntryArguments(FunctionDeclaration func, BackendScope scope) {
        List<AssemblyInstruction> instructions = new ArrayList<>();

        List<VariableDefinition> args = func.getArgumentList();

        if (args.size() > 0) {
            instructions.add(new PushInstruction(Register.RDI));
            scope.declareVariable(args.get(0).getVariableName(), args.get(0).getType());
        }

        if (args.size() > 1) {
            instructions.add(new PushInstruction(Register.RSI));
            scope.declareVariable(args.get(1).getVariableName(), args.get(1).getType());
        }

        if (args.size() > 2) {
            instructions.add(new PushInstruction(Register.RDX));
            scope.declareVariable(args.get(2).getVariableName(), args.get(2).getType());
        }

        if (args.size() > 3) {
            instructions.add(new PushInstruction(Register.RCX));
            scope.declareVariable(args.get(3).getVariableName(), args.get(3).getType());
        }

        if (args.size() > 4) {
            instructions.add(new PushInstruction(Register.R8));
            scope.declareVariable(args.get(4).getVariableName(), args.get(4).getType());
        }

        if (args.size() > 5) {
            instructions.add(new PushInstruction(Register.R9));
            scope.declareVariable(args.get(5).getVariableName(), args.get(5).getType());
        }

        for (int i = 6; i < args.size(); i++) {
            int offset = i - 4; // below rbp and return address
            scope.declareVariable(args.get(i).getVariableName(), args.get(i).getType(), offset, false);
        }

        addInstructions(instructions);
    }

    public List<AssemblyInstruction> generateProlog() {
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
