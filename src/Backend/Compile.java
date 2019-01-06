package src.Backend;

import src.Absyn.BlockS;
import src.Absyn.MBody;
import src.Absyn.Stmt;
import src.Backend.Definitions.BackendScope;
import src.Backend.Definitions.ExternalFunctions;
import src.Backend.Definitions.Register;
import src.Backend.Instructions.*;
import src.Definitions.*;
import src.Frontend.Environment;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static src.Backend.Instructions.ConstantUtils.THIS_KEYWORD;
import static src.Backend.Instructions.ConstantUtils.WORD_SIZE;

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
        instructions.add(new CustomInstruction(ConstantUtils.TAB + "extern " + ExternalFunctions.MALLOC_SIZE));

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
        for (TypeDefinition type : env.declaredTypes.values()) {
            if (type.isClassType()) {
                type.getClassDefinition().createOffSetTable();

                for (MethodDeclaration mth : type.getClassDefinition().methods.values()) {
                    mth.assignLabel();
                }
            }

            if (type.isInterfaceType()) {
                type.getInterfaceDefinition().createOffSetTable();
            }
        }

        addInstructions(generateStringSection());
        addInstructions(generateSectionTextEntryInstructions());

        for (TypeDefinition type : env.declaredTypes.values()) {
            if (type.isClassType()) {
                for (MethodDeclaration mth : type.getClassDefinition().methods.values()) {
                    generateMethod(mth);
                }
            }
        }

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

        addInstructions(generatePushFunctionEntryArgumentsToStack(func, scope));

        BackendScope scopeWithArgs = new BackendScope(scope);

        func.getMethodBody().match(
                null,
                (body) -> generateBody(body, scopeWithArgs)
        );
    }

    public void generateMethod(MethodDeclaration mth) {
        BackendScope scope = new BackendScope(this.env);

        addInstructions(new Comment("code for method" + mth.getName()));
        addInstructions(mth.label);
        addInstructions(generateProlog(mth.getNumberOfVariables() + 1));

        addInstructions(generatePushMethodEntryArgumentsToStack(mth, scope));

        BackendScope scopeWithArgs = new BackendScope(scope);

        mth.getMethodBody().match(
                null,
                (body) -> generateBody(body, scopeWithArgs)
        );
    }

    public List<AssemblyInstruction> generatePushFunctionEntryArgumentsToStack(FunctionDeclaration func, BackendScope scope) {
        return generatePushCallableEntryArgumentsToStack(func.getArgumentList(), scope, null);
    }

    public List<AssemblyInstruction> generatePushMethodEntryArgumentsToStack(MethodDeclaration mth, BackendScope scope) {
        List<AssemblyInstruction> instructions = new ArrayList<>();

        String register = Register.RDI;
        scope.declareVariable(THIS_KEYWORD, mth.getCallerType());
        int offset = scope.getVariable(THIS_KEYWORD).getOffset() * WORD_SIZE;
        instructions.add(new MovInstruction(MemoryReference.getWithOffset(Register.RBP, offset), register));

        instructions.addAll(generatePushCallableEntryArgumentsToStack(mth.getArgumentList(), scope, mth.getCallerType()));

        return instructions;
    }

    public List<AssemblyInstruction> generatePushCallableEntryArgumentsToStack(List<VariableDefinition> args, BackendScope scope, TypeDefinition methodsCallerType) {
        List<AssemblyInstruction> instructions = new ArrayList<>();

        int argsOffset = 0;

        boolean isMethod = methodsCallerType != null;

        if (isMethod) {
            argsOffset = -1;
            scope.declareVariable(THIS_KEYWORD, methodsCallerType);
            int offset = scope.getVariable(THIS_KEYWORD).getOffset() * WORD_SIZE;
            instructions.add(new MovInstruction(MemoryReference.getWithOffset(Register.RBP, offset), Register.RDI));
        }

        if (!isMethod && args.size() > 0) {
            int pos = 0;
            String register = Register.RDI;
            scope.declareVariable(args.get(pos).getVariableName(), args.get(pos).getType());
            int offset = scope.getVariable(args.get(pos).getVariableName()).getOffset() * WORD_SIZE;
            instructions.add(new MovInstruction(MemoryReference.getWithOffset(Register.RBP, offset), register));
        }

        if (args.size() > 1 + argsOffset) {
            int pos = 1 + argsOffset;
            String register = Register.RSI;
            scope.declareVariable(args.get(pos).getVariableName(), args.get(pos).getType());
            int offset = scope.getVariable(args.get(pos).getVariableName()).getOffset() * WORD_SIZE;
            instructions.add(new MovInstruction(MemoryReference.getWithOffset(Register.RBP, offset), register));
        }

        if (args.size() > 2 + argsOffset) {
            int pos = 2 + argsOffset;
            String register = Register.RDX;
            scope.declareVariable(args.get(pos).getVariableName(), args.get(pos).getType());
            int offset = scope.getVariable(args.get(pos).getVariableName()).getOffset() * WORD_SIZE;
            instructions.add(new MovInstruction(MemoryReference.getWithOffset(Register.RBP, offset), register));
        }

        if (args.size() > 3 + argsOffset) {
            int pos = 3 + argsOffset;
            String register = Register.RCX;
            scope.declareVariable(args.get(pos).getVariableName(), args.get(pos).getType());
            int offset = scope.getVariable(args.get(pos).getVariableName()).getOffset() * WORD_SIZE;
            instructions.add(new MovInstruction(MemoryReference.getWithOffset(Register.RBP, offset), register));
        }

        if (args.size() > 4 + argsOffset) {
            int pos = 4 + argsOffset;
            String register = Register.R8;
            scope.declareVariable(args.get(pos).getVariableName(), args.get(pos).getType());
            int offset = scope.getVariable(args.get(pos).getVariableName()).getOffset() * WORD_SIZE;
            instructions.add(new MovInstruction(MemoryReference.getWithOffset(Register.RBP, offset), register));
        }

        if (args.size() > 5 + argsOffset) {
            int pos = 5 + argsOffset;
            String register = Register.R9;
            scope.declareVariable(args.get(pos).getVariableName(), args.get(pos).getType());
            int offset = scope.getVariable(args.get(pos).getVariableName()).getOffset() * WORD_SIZE;
            instructions.add(new MovInstruction(MemoryReference.getWithOffset(Register.RBP, offset), register));
        }

        for (int i = 6 + argsOffset; i < args.size(); i++) {
            int offset = i - 4; // below rbp and return address
            scope.declareVariable(args.get(i).getVariableName(), args.get(i).getType(), offset, false);
        }

        return instructions;
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
