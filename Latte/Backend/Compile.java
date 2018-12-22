package Latte.Backend;

import Latte.Absyn.BlockS;
import Latte.Absyn.MBody;
import Latte.Absyn.Stmt;
import Latte.Backend.Definitions.BackendScope;
import Latte.Backend.Definitions.ExternalFunctions;
import Latte.Backend.Definitions.Register;
import Latte.Backend.Instructions.*;
import Latte.Definitions.FunctionDeclaration;
import Latte.Definitions.VariableDefinition;
import Latte.Frontend.Environment;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static Latte.Backend.Instructions.ConstantUtils.WORD_SIZE;

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

    public List<AssemblyInstruction> generateSectionTextEntryInstructions() {
        List<AssemblyInstruction> instructions = new ArrayList<>();

        instructions.add(new CustomInstruction("section .text"));
        instructions.add(new CustomInstruction(ConstantUtils.TAB + "align " + 16));

        for (FunctionDeclaration func : env.declaredFunctions.values()) {
            if (func.isExternal()) {
                instructions.add(new CustomInstruction(ConstantUtils.TAB + "extern " + func.getName()));
            } else {
                instructions.add(new CustomInstruction(ConstantUtils.TAB + "global " + func.getName()));
            }
        }

        instructions.add(new CustomInstruction(ConstantUtils.TAB + "extern " + ExternalFunctions.ADD_STRINGS));
        instructions.add(new CustomInstruction(ConstantUtils.TAB + "extern " + ExternalFunctions.MALLOC_ARRAY));

        return instructions;
    }

    public List<AssemblyInstruction> generateStringSection() {
        assignLabelsToStrings(env);

        List<AssemblyInstruction> instructions = new ArrayList<>();

        instructions.add(new CustomInstruction("section .rodata"));

        for (String k : env.stringLiterals.keySet()) {
            instructions.add(new StringInstruction(env.getStringLabel(k).getLabelName(), k));
        }

        return instructions;
    }


    public void generate() {
        addInstructions(generateStringSection());
        addInstructions(generateSectionTextEntryInstructions());

        for (FunctionDeclaration func : env.declaredFunctions.values()) {
            if (!func.isExternal()) {
                generateFunction(func);
            }
        }
    }

    public void assignLabelsToStrings(Environment env) {
        Set<String> keys = env.stringLiterals.keySet();

        for (String k : keys) {
            Label l = LabelsGenerator.getNonceLabel("str");
            env.declareStringLiteral(k, l);
        }

        return;

    }

    public void generateFunction(FunctionDeclaration func) {
        BackendScope scope = new BackendScope(this.env);

        addInstructions(new Comment("code for function " + func.getName()));
        addInstructions(new Label(func.getName()));
        addInstructions(generateProlog(func.getNumberOfVariables()));

        generatePushEntryArgumentsToStack(func, scope);

        BackendScope scopeWithArgs = new BackendScope(scope);

        func.getMethodBody().match(
                null,
                (body) -> generateBody(body, scopeWithArgs)
        );
    }

    public void generatePushEntryArgumentsToStack(FunctionDeclaration func, BackendScope scope) {
        List<AssemblyInstruction> instructions = new ArrayList<>();

        List<VariableDefinition> args = func.getArgumentList();

        if (args.size() > 0) {
            int pos = 0;
            String register = Register.RDI;
            scope.declareVariable(args.get(pos).getVariableName(), args.get(pos).getType());
            int offset = scope.getVariable(args.get(pos).getVariableName()).getOffset() * WORD_SIZE;
            instructions.add(new MovInstruction(MemoryReference.getWithOffset(Register.RBP, offset), register));
        }

        if (args.size() > 1) {
            int pos = 1;
            String register = Register.RSI;
            scope.declareVariable(args.get(pos).getVariableName(), args.get(pos).getType());
            int offset = scope.getVariable(args.get(pos).getVariableName()).getOffset() * WORD_SIZE;
            instructions.add(new MovInstruction(MemoryReference.getWithOffset(Register.RBP, offset), register));
        }

        if (args.size() > 2) {
            int pos = 2;
            String register = Register.RDX;
            scope.declareVariable(args.get(pos).getVariableName(), args.get(pos).getType());
            int offset = scope.getVariable(args.get(pos).getVariableName()).getOffset() * WORD_SIZE;
            instructions.add(new MovInstruction(MemoryReference.getWithOffset(Register.RBP, offset), register));
        }

        if (args.size() > 3) {
            int pos = 3;
            String register = Register.RCX;
            scope.declareVariable(args.get(pos).getVariableName(), args.get(pos).getType());
            int offset = scope.getVariable(args.get(pos).getVariableName()).getOffset() * WORD_SIZE;
            instructions.add(new MovInstruction(MemoryReference.getWithOffset(Register.RBP, offset), register));
        }

        if (args.size() > 4) {
            int pos = 4;
            String register = Register.R8;
            scope.declareVariable(args.get(pos).getVariableName(), args.get(pos).getType());
            int offset = scope.getVariable(args.get(pos).getVariableName()).getOffset() * WORD_SIZE;
            instructions.add(new MovInstruction(MemoryReference.getWithOffset(Register.RBP, offset), register));
        }

        if (args.size() > 5) {
            int pos = 5;
            String register = Register.R9;
            scope.declareVariable(args.get(pos).getVariableName(), args.get(pos).getType());
            int offset = scope.getVariable(args.get(pos).getVariableName()).getOffset() * WORD_SIZE;
            instructions.add(new MovInstruction(MemoryReference.getWithOffset(Register.RBP, offset), register));
        }

        for (int i = 6; i < args.size(); i++) {
            int offset = i - 4; // below rbp and return address
            scope.declareVariable(args.get(i).getVariableName(), args.get(i).getType(), offset, false);
        }

        addInstructions(instructions);
    }

    public List<AssemblyInstruction> generateProlog(int numberOfVariables) {
        List<AssemblyInstruction> instructions = new ArrayList<>();

        instructions.add(new PushInstruction(Register.RBP));
        instructions.add(new MovInstruction(Register.RBP, Register.RSP));

        int rspOffset = (numberOfVariables % 2 == 0) ? numberOfVariables : numberOfVariables + 1; // align to 16 bytes

        instructions.add(new SubInstruction(Register.RSP, YieldUtils.number(rspOffset * WORD_SIZE)));

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
