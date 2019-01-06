package src.Backend;

import src.Absyn.*;
import src.Backend.Definitions.*;
import src.Backend.Instructions.*;
import src.Definitions.BasicTypeDefinition;
import src.Definitions.TypeDefinition;
import src.Exceptions.CompilerException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static src.Backend.Instructions.ConstantUtils.THIS_KEYWORD;
import static src.Backend.Instructions.ConstantUtils.WORD_SIZE;
import static src.Backend.LabelsGenerator.getNonceLabel;
import static src.Frontend.TypeUtils.getType;
import static java.lang.Math.max;

public class CompileExpression {

    public static List<AssemblyInstruction> generateExpr(Expr expr, String destRegister, String sourceRegister, BackendScope scope) {
        return expr.match(
                (var) -> generateVar(var, destRegister, scope),
                (eInt) -> generateInt(eInt, destRegister),
                (eTrue) -> generateTrue(destRegister),
                (eFalse) -> generateFalse(destRegister),
                (eThis) -> generateThis(destRegister, scope),
                (ignored) -> generateNull(destRegister),
                (eApp) -> generateApp(eApp, destRegister, scope),
                (str) -> generateStr(str, destRegister, scope),
                (constr) -> generateEConstr(constr, destRegister, scope),
                (arrConstr) -> generateArrConstr(arrConstr, destRegister, scope),
                (arrAcc) -> generateArrAcc(arrAcc, destRegister, scope),
                (neg) -> generateNeg(neg, destRegister, sourceRegister, scope),
                (not) -> generateNot(not, destRegister, sourceRegister, scope),
                (mul) -> generateMul(mul, destRegister, sourceRegister, scope),
                (add) -> generateAdd(add, destRegister, sourceRegister, scope),
                (rel) -> generateRel(rel, destRegister, sourceRegister, scope),
                (and) -> generateLazyAnd(and, destRegister, sourceRegister, scope),
                (eOr) -> generateLazyOr(eOr, destRegister, sourceRegister, scope),
                (objAcc) -> generateObjAcc(objAcc, destRegister, scope),
                (cast) -> generateCast(cast, destRegister, sourceRegister, scope)
        );
    }

    private static List<AssemblyInstruction> generatePushBeforeFunction() {
        List<AssemblyInstruction> instructions = new ArrayList<>();

        instructions.add(new PushInstruction(Register.RDI));
        instructions.add(new PushInstruction(Register.RSI));
        instructions.add(new PushInstruction(Register.RDX));
        instructions.add(new PushInstruction(Register.RCX));
        instructions.add(new PushInstruction(Register.R8));
        instructions.add(new PushInstruction(Register.R9));

        return instructions;
    }

    private static List<AssemblyInstruction> generatePopAfterFunction() {
        List<AssemblyInstruction> instructions = new ArrayList<>();

        instructions.add(new PopInstruction(Register.R9));
        instructions.add(new PopInstruction(Register.R8));
        instructions.add(new PopInstruction(Register.RCX));
        instructions.add(new PopInstruction(Register.RDX));
        instructions.add(new PopInstruction(Register.RSI));
        instructions.add(new PopInstruction(Register.RDI));

        return instructions;
    }
    
    public static List<AssemblyInstruction> generateFunctionApp(EApp eApp, String destRegister, BackendScope scope) {
        List<AssemblyInstruction> instructions = new ArrayList<>();

        String funcName = eApp.ident_;

        instructions.addAll(generatePushBeforeFunction());

        int numberOfArgsPassedOnStack = max(0, (eApp.listexpr_.size() - 6));
        int numberOfTempsOnStack = scope.getNumberOfTempsOnStack();

        boolean hasToAlignStack = (numberOfArgsPassedOnStack + numberOfTempsOnStack) % 2 != 0;

        if (hasToAlignStack) {
            instructions.add(new Comment("aligning stack"));
            instructions.add(new SubInstruction(Register.RSP, YieldUtils.number(WORD_SIZE)));
        }

        instructions.addAll(generateFunctionArguments(eApp.listexpr_, scope));
        instructions.add(new CallInstruction(funcName));

        int offset = max(0, (eApp.listexpr_.size() - 6)) * WORD_SIZE;

        if (offset > 0) {
            instructions.add(new AddInstruction(Register.RSP, YieldUtils.number(offset)));
            scope.changeTempsOnStack(-offset);
        }

        if (hasToAlignStack) {
            instructions.add(new AddInstruction(Register.RSP, YieldUtils.number(WORD_SIZE)));
        }

        instructions.addAll(generatePopAfterFunction());

        return instructions;
    }

    private static List<AssemblyInstruction> generateCallableArguments(List<Expr> exprs, BackendScope scope, boolean isMethod) {
        List<AssemblyInstruction> instructions = new ArrayList<>();

        int argsOffset = 0; // used for 'this' argument in methods

        if (isMethod) {
            argsOffset = -1;
        }

        if (!isMethod && exprs.size() > 0) {
            instructions.addAll(generateExpr(exprs.get(0), Register.RAX, Register.RAX, scope));
            instructions.add(new MovInstruction(Register.RDI, Register.RAX));
        }

        if (exprs.size() > 1 + argsOffset) {
            instructions.addAll(generateExpr(exprs.get(1 + argsOffset), Register.RAX, Register.RAX, scope));
            instructions.add(new MovInstruction(Register.RSI, Register.RAX));
        }

        if (exprs.size() > 2 + argsOffset) {
            instructions.addAll(generateExpr(exprs.get(2 + argsOffset), Register.RAX, Register.RAX, scope));
            instructions.add(new MovInstruction(Register.RDX, Register.RAX));
        }

        if (exprs.size() > 3 + argsOffset) {
            instructions.addAll(generateExpr(exprs.get(3 + argsOffset), Register.RAX, Register.RAX, scope));
            instructions.add(new MovInstruction(Register.RCX, Register.RAX));
        }

        if (exprs.size() > 4 + argsOffset) {
            instructions.addAll(generateExpr(exprs.get(4 + argsOffset), Register.RAX, Register.RAX, scope));
            instructions.add(new MovInstruction(Register.R8, Register.RAX));
        }

        if (exprs.size() > 5 + argsOffset) {
            instructions.addAll(generateExpr(exprs.get(5 + argsOffset), Register.RAX, Register.RAX, scope));
            instructions.add(new MovInstruction(Register.R9, Register.RAX));
        }

        for (int i = exprs.size(); i > (6 + argsOffset); i--) {
            instructions.addAll(generateExpr(exprs.get(i-1), Register.RAX, Register.RAX, scope));
            instructions.add(new PushInstruction(Register.RAX, scope));
        }

        return instructions;
    }

    private static List<AssemblyInstruction> generateFunctionArguments(List<Expr> exprs, BackendScope scope) {
        return generateCallableArguments(exprs, scope, false);
    }

    private static List<AssemblyInstruction> generateMethodArguments(List<Expr> exprs, BackendScope scope) {
        return generateCallableArguments(exprs, scope, true);
    }

    public static List<AssemblyInstruction> generateMethodApp(EApp eApp, String destRegister, BackendScope scope) {
        List<AssemblyInstruction> instructions = new ArrayList<>();

        instructions.addAll(generatePushBeforeFunction());

        int numberOfArgsPassedOnStack = max(0, (eApp.listexpr_.size() - 5)); // - 6 + 1 - dodajemy "this" jako pierwszy argument
        int numberOfTempsOnStack = scope.getNumberOfTempsOnStack();

        boolean hasToAlignStack = (numberOfArgsPassedOnStack + numberOfTempsOnStack) % 2 != 0;

        if (hasToAlignStack) {
            instructions.add(new Comment("aligning stack"));
            instructions.add(new SubInstruction(Register.RSP, YieldUtils.number(WORD_SIZE)));
        }

        VariableCompilerInfo varInfo = scope.getVariable(THIS_KEYWORD);
        int thisOffset = varInfo.getOffset() * WORD_SIZE;
        instructions.add(new MovInstruction(Register.RDI, MemoryReference.getWithOffset(Register.RBP, thisOffset)));

        instructions.addAll(generateMethodArguments(eApp.listexpr_, scope));


        // TODO: handle interfaces
        instructions.add(new CallInstruction(eApp.binding.getBindedClass().getClassDefinition().methods.get(eApp.ident_).label.getLabelName()));

        // function args pushed on stack?
        int offset = max(0, (eApp.listexpr_.size() - 5)) * WORD_SIZE;

        if (offset > 0) {
            instructions.add(new AddInstruction(Register.RSP, YieldUtils.number(offset)));
            scope.changeTempsOnStack(-offset);
        }

        if (hasToAlignStack) {
            instructions.add(new AddInstruction(Register.RSP, YieldUtils.number(WORD_SIZE)));
        }

        instructions.addAll(generatePopAfterFunction());

        return instructions;
    }

    public static List<AssemblyInstruction> generateApp(EApp eApp, String destRegister, BackendScope scope) {
        if (Binding.FUNCTION_BINDING.equals(eApp.binding)) {
            return generateFunctionApp(eApp, destRegister, scope);
        }

        return generateMethodApp(eApp, destRegister, scope);
    }

    public static List<AssemblyInstruction> generateVar(EVar var, String destRegister, BackendScope scope) {
        if (!Binding.VARIABLE_BINDING.equals(var.binding)) {
            return generateFieldFromVar(var, destRegister, scope);
        }

        VariableCompilerInfo varInfo = scope.getVariable(var.ident_);

        int offset = varInfo.getOffset() * WORD_SIZE;

        return new ArrayList<>(Collections.singletonList(new MovInstruction(destRegister, MemoryReference.getWithOffset(Register.RBP, offset))));
    }

    public static List<AssemblyInstruction> generateFieldFromVar(EVar var, String destRegister, BackendScope scope) {
        List<AssemblyInstruction> instructions = new ArrayList<>();

        int thisOffset = scope.getVariable(THIS_KEYWORD).getOffset() * WORD_SIZE;
        int fieldOffset = var.binding.getBindedClass().getClassDefinition().getFieldOffset(var.ident_);

        instructions.add(new MovInstruction(Register.RAX, MemoryReference.getWithOffset(Register.RBP, thisOffset)));
        instructions.add(new AddInstruction(Register.RAX, YieldUtils.number(fieldOffset)));
        instructions.add(new MovInstruction(Register.RAX, MemoryReference.getRaw(Register.RAX)));

        return instructions;
    }

    public static List<AssemblyInstruction> generateInt(ELitInt eInt, String destRegister) {
        MovInstruction mov = new MovInstruction(destRegister, YieldUtils.number(eInt.integer_));
        return new ArrayList<>(Collections.singletonList(mov));
    }

    public static List<AssemblyInstruction> generateTrue(String destRegister) {
        MovInstruction mov = new MovInstruction(destRegister, YieldUtils.number(1));
        return new ArrayList<>(Collections.singletonList(mov));
    }

    public static List<AssemblyInstruction> generateFalse(String destRegister) {
        MovInstruction mov = new MovInstruction(destRegister, YieldUtils.number(0));
        return new ArrayList<>(Collections.singletonList(mov));
    }

    public static List<AssemblyInstruction> generateNeg(Neg neg, String destRegister, String sourceRegister, BackendScope scope) {
        List<AssemblyInstruction> assemblyInstructions = generateExpr(neg.expr_, destRegister, sourceRegister, scope);
        NegInstruction negInstr = new NegInstruction(destRegister);

        assemblyInstructions.add(negInstr);

        return assemblyInstructions;
    }

    public static List<AssemblyInstruction> generateNot(Not not, String destRegister, String sourceRegister, BackendScope scope) {
        List<AssemblyInstruction> assemblyInstructions = generateExpr(not.expr_, destRegister, sourceRegister, scope);

        CompareInstruction cmpInstr = new CompareInstruction(sourceRegister, YieldUtils.number(0));
        MovInstruction movInstruction = new MovInstruction(sourceRegister, YieldUtils.number(0));
        SetIfEqualInstruction setIfEqualInstruction = new SetIfEqualInstruction("al");

        assemblyInstructions.addAll(new ArrayList<>(Arrays.asList(cmpInstr, movInstruction, setIfEqualInstruction)));

        return assemblyInstructions;
    }

    public static List<AssemblyInstruction> generateAdd(EAdd add, String destRegister, String sourceRegister, BackendScope scope) {
        if (BasicTypeDefinition.STRING.equals(add.type)) {
            return generateStringAdd(add, destRegister, scope);
        }

        List<AssemblyInstruction> instructions = new ArrayList<>();

        instructions.add(new PushInstruction(Register.RCX, scope));

        instructions.addAll(generateExpr(add.expr_2, destRegister, sourceRegister, scope));
        instructions.add(new PushInstruction(Register.RAX, scope));

        instructions.addAll(generateExpr(add.expr_1, destRegister, sourceRegister, scope));
        instructions.add(new PopInstruction(Register.RCX, scope));

        List<AssemblyInstruction> oppInstr = add.addop_.match(
                (ignored) -> getPlusInstruction(Register.RAX, Register.RCX, scope),
                (ignored) -> getMinusInstruction(Register.RAX, Register.RCX, scope)
        );

        instructions.addAll(oppInstr);

        instructions.add(new PopInstruction(Register.RCX, scope));

        return instructions;
    }

    public static List<AssemblyInstruction> generateStringAdd(EAdd add, String destRegister, BackendScope scope) {
        // here it has to be string + as there's no - in strings
        List<AssemblyInstruction> instructions = new ArrayList<>();

        instructions.add(new PushInstruction(Register.RDI, scope));
        instructions.add(new PushInstruction(Register.RSI, scope));

        instructions.addAll(generateExpr(add.expr_1, Register.RAX, Register.RAX, scope));
        instructions.add(new MovInstruction(Register.RDI, Register.RAX));

        instructions.addAll(generateExpr(add.expr_2, Register.RAX, Register.RAX, scope));
        instructions.add(new MovInstruction(Register.RSI, Register.RAX));

        instructions.add(new CallInstruction(ExternalFunctions.ADD_STRINGS));

        instructions.add(new PopInstruction(Register.RSI, scope));
        instructions.add(new PopInstruction(Register.RDI, scope));

        if (!destRegister.equals(Register.RAX)) {
            instructions.add(new MovInstruction(destRegister, Register.RAX));
        }

        return instructions;

    }

    public static List<AssemblyInstruction> getMinusInstruction(String destRegister, String sourceRegister, BackendScope scope) {
        return new ArrayList<>(Collections.singletonList(new SubInstruction(destRegister, sourceRegister)));
    }

    public static List<AssemblyInstruction> getPlusInstruction(String destRegister, String sourceRegister, BackendScope scope) {
        return new ArrayList<>(Collections.singletonList(new AddInstruction(destRegister, sourceRegister)));

    }

    public static List<AssemblyInstruction> generateMul(EMul mul, String destRegister, String sourceRegister, BackendScope scope) {
        List<AssemblyInstruction> instructions = new ArrayList<>();

        instructions.add(new PushInstruction(Register.RCX, scope));

        List<AssemblyInstruction> instructions1 = generateExpr(mul.expr_1, destRegister, sourceRegister, scope);
        List<AssemblyInstruction> instructions2 = generateExpr(mul.expr_2, destRegister, sourceRegister, scope);

        instructions.addAll(instructions2);
        instructions.add(new PushInstruction(Register.RAX, scope));

        instructions.addAll(instructions1);
        instructions.add(new PopInstruction(Register.RCX, scope));

        List<AssemblyInstruction> oppInstr = mul.mulop_.match(
                (ignored) -> generateMultiplyInstructions(Register.RAX, Register.RCX, scope),
                (ignored) -> generateDivideInstructions(Register.RAX, Register.RCX, scope),
                (ignored) -> generateModInstruction(Register.RAX, Register.RCX, scope)
        );

        instructions.addAll(oppInstr);

        instructions.add(new PopInstruction(Register.RCX, scope));

        return instructions;
    }

    public static List<AssemblyInstruction> generateMultiplyInstructions(String destRegister, String sourceRegister, BackendScope scope) {
        return new ArrayList<>(Collections.singletonList(new MulInstruction(destRegister, sourceRegister)));
    }

    public static List<AssemblyInstruction> generateDivideInstructions(String destRegister, String sourceRegister, BackendScope scope) {
        List<AssemblyInstruction> instructions = new ArrayList<>();


        instructions.add(new PushInstruction(Register.RDX, scope));

        if (!destRegister.equals(Register.RAX)) {
            instructions.add(new MovInstruction(Register.RAX, destRegister));
        }

        instructions.add(new XorInstruction(Register.RDX, Register.RDX));
        instructions.add(new DivInstruction(sourceRegister));

        if (!destRegister.equals(Register.RAX)) {
            instructions.add(new MovInstruction(destRegister, Register.RAX));
        }

        instructions.add(new PopInstruction(Register.RDX, scope));

        return instructions;
    }

    public static List<AssemblyInstruction> generateModInstruction(String destRegister, String sourceRegister, BackendScope scope) {
        List<AssemblyInstruction> instructions = new ArrayList<>();


        instructions.add(new PushInstruction(Register.RDX, scope));

        if (!destRegister.equals(Register.RAX)) {
            instructions.add(new MovInstruction(Register.RAX, destRegister));
        }

        instructions.add(new XorInstruction(Register.RDX, Register.RDX));
        instructions.add(new DivInstruction(sourceRegister));

        instructions.add(new MovInstruction(destRegister, Register.RDX));

        instructions.add(new PopInstruction(Register.RDX, scope));

        return instructions;
    }

    public static List<AssemblyInstruction> generateLazyOr(EOr eOr, String destRegister, String sourceRegister, BackendScope scope) {
        List<AssemblyInstruction> expr1Instructions = generateExpr(eOr.expr_1, destRegister, sourceRegister, scope);

        Label endLabel = getNonceLabel("_or_end");

        expr1Instructions.add(new CompareInstruction(destRegister, YieldUtils.number(1)));
        // if jumps, in RAX there is 1, so the result is correct
        expr1Instructions.add(new JumpInstruction(endLabel, JumpInstruction.Type.EQU));

        // sets to RAX rhs of or, so OR value is the same as value of expr_2
        List<AssemblyInstruction> expr2Instructions = generateExpr(eOr.expr_2, destRegister, sourceRegister, scope);

        expr1Instructions.addAll(expr2Instructions);
        expr1Instructions.add(endLabel);


        return expr1Instructions;
    }

    public static List<AssemblyInstruction> generateLazyAnd(EAnd eAnd, String destRegister, String sourceRegister, BackendScope scope) {
        List<AssemblyInstruction> expr1Instructions = generateExpr(eAnd.expr_1, destRegister, sourceRegister, scope);

        Label endLabel = getNonceLabel("_and_end");

        expr1Instructions.add(new CompareInstruction(destRegister, YieldUtils.number(0)));
        // if jumps, in RAX there is 0, so the result of AND is 0, so we can end
        expr1Instructions.add(new JumpInstruction(endLabel, JumpInstruction.Type.EQU));

        // lhs was 1, so setting RAX value to rhs value is enough to have correct overall AND result
        List<AssemblyInstruction> expr2Instructions = generateExpr(eAnd.expr_2, destRegister, sourceRegister, scope);

        expr1Instructions.addAll(expr2Instructions);
        expr1Instructions.add(endLabel);

        return expr1Instructions;
    }

    public static List<AssemblyInstruction> generateRel(ERel rel, String destRegister, String sourceRegister, BackendScope scope) {
        List<AssemblyInstruction> instructions = new ArrayList<>();

        instructions.add(new PushInstruction(Register.RCX, scope));

        instructions.addAll(generateExpr(rel.expr_2, destRegister, sourceRegister, scope));
        instructions.add(new PushInstruction(Register.RAX, scope));

        instructions.addAll(generateExpr(rel.expr_1, destRegister, sourceRegister, scope));
        instructions.add(new PopInstruction(Register.RCX, scope));

        instructions.add(new CompareInstruction(Register.RAX, Register.RCX));
        instructions.add(new MovInstruction(destRegister, YieldUtils.number(1)));

        Label endLabel = LabelsGenerator.getNonceLabel("_rel_end");

        instructions.add(rel.relop_.match(
                (lth) -> new JumpInstruction(endLabel, JumpInstruction.Type.LTH),
                (le) -> new JumpInstruction(endLabel, JumpInstruction.Type.LE),
                (gth) -> new JumpInstruction(endLabel, JumpInstruction.Type.GTH),
                (ge) -> new JumpInstruction(endLabel, JumpInstruction.Type.GE),
                (equ) -> new JumpInstruction(endLabel, JumpInstruction.Type.EQU),
                (ne) -> new JumpInstruction(endLabel, JumpInstruction.Type.NE)
        ));

        instructions.add(new MovInstruction(destRegister, YieldUtils.number(0)));
        instructions.add(endLabel);

        instructions.add(new PopInstruction(Register.RCX, scope));

        return instructions;
    }

    public static List<AssemblyInstruction> generateCast(ECast cast, String destRegister, String sourceRegister, BackendScope scope) {
        List<AssemblyInstruction> exprInstructions = generateExpr(cast.expr_, destRegister, sourceRegister, scope);

        TypeDefinition castedToType = getType(cast.typename_, scope.getGlobalEnvironment(), cast.line_num, cast.col_num);

        if (castedToType.equals(BasicTypeDefinition.INT)) {
            // do nothing, treat register value as an int
            return exprInstructions;
        }

        return notImplemented(cast);
    }

    public static List<AssemblyInstruction> generateStr(EString str, String destRegister, BackendScope scope) {
        List<AssemblyInstruction> instructions = new ArrayList<>();
        Label l = scope.getGlobalEnvironment().getStringLabel(str.string_);

        instructions.add(new MovInstruction(destRegister, l.getLabelName()));

        return instructions;
    }

    public static List<AssemblyInstruction> generateNull(String destRegister) {
        List<AssemblyInstruction> instructions = new ArrayList<>();

        instructions.add(new XorInstruction(destRegister, destRegister));

        return instructions;
    }

    public static List<AssemblyInstruction> generateArrConstr(EArrConstr constr, String destRegister, BackendScope scope) {
        List<AssemblyInstruction> instructions = new ArrayList<>();

        instructions.addAll(generateExpr(constr.expr_, Register.RAX, Register.RAX, scope));
        instructions.add(new MovInstruction(Register.RDI, Register.RAX));

        instructions.add(new PushInstruction(Register.RDI, scope));
        instructions.add(new CallInstruction(ExternalFunctions.MALLOC_ARRAY));
        instructions.add(new PopInstruction(Register.RDI, scope));

        if (!destRegister.equals(Register.RAX)) {
            instructions.add(new MovInstruction(destRegister, Register.RAX));
        }

        return  instructions;

    }

    public static List<AssemblyInstruction> generateArrAcc(ENDArrAcc acc, String destRegister, BackendScope scope) {
        List<AssemblyInstruction> instructions = new ArrayList<>();

        instructions.add(new PushInstruction(Register.RCX, scope));

        instructions.addAll(generateExpr(acc.expr_1, Register.RAX, Register.RAX, scope));
        instructions.add(new PushInstruction(Register.RAX, scope));
        instructions.addAll(generateExpr(acc.expr_2, Register.RAX, Register.RAX, scope));

        instructions.add(new PopInstruction(Register.RCX, scope));

        instructions.add(new MovInstruction(Register.RAX, MemoryReference.getWithConstOffset(Register.RCX,Register.RAX, 8 , WORD_SIZE)));

        instructions.add(new PopInstruction(Register.RCX, scope));

        if (!destRegister.equals(Register.RAX)) {
            instructions.add(new MovInstruction(destRegister, Register.RAX));
        }

        return instructions;
    }

    public static List<AssemblyInstruction> generateObjAcc(EObjAcc objAcc, String destRegister, BackendScope scope) {
        List<AssemblyInstruction> instructions = new ArrayList<>();

        instructions.addAll(generateExpr(objAcc.expr_, Register.RAX, Register.RAX, scope));

        instructions.addAll(objAcc.objacc_.match(
                (fieldAcc) -> generateFieldAcc(fieldAcc, objAcc.expr_.type, Register.RAX, scope),
                (methAcc) -> generateMthAcc(methAcc, objAcc.expr_.type, Register.RAX, scope)
        ));

        return instructions;
    }

    public static List<AssemblyInstruction> generateFieldAcc(ObjFieldAcc acc, TypeDefinition type, String destRegister, BackendScope scope) {
        // object in RAX, just take the field

        if (type.isArrayType()) {
            return generateArrayFieldAcc(acc, type, destRegister, scope);
        }

        if (type.isClassType()) {
            return generateClassFieldAcc(acc, type, destRegister, scope);
        }

        return new ArrayList<>();
    }

    public static List<AssemblyInstruction> generateClassFieldAcc(ObjFieldAcc acc, TypeDefinition type, String destRegister, BackendScope scope) {
        // base object's address already in RAX
        return generateClassFieldAcc(acc.ident_, type, destRegister, scope);
    }

    public static List<AssemblyInstruction> generateClassFieldAcc(String fieldName, TypeDefinition type, String destRegister, BackendScope scope) {
        List<AssemblyInstruction> instructions = new ArrayList<>();
        int offset = type.getClassDefinition().getFieldOffset(fieldName);

        instructions.add(new MovInstruction(Register.RAX, MemoryReference.getWithOffset(Register.RAX, offset)));

        return instructions;
    }

    public static List<AssemblyInstruction> generateArrayFieldAcc(ObjFieldAcc acc, TypeDefinition type, String destRegister, BackendScope scope) {
        // expression result in RAX
        // it has to be length field acc, checked in typecheck

        List<AssemblyInstruction> instructions = new ArrayList<>();

        // length of an array is stored in first 8 bytes
        instructions.add(new MovInstruction(destRegister, MemoryReference.getRaw(Register.RAX)));

        return instructions;
    }

    public static List<AssemblyInstruction> generateEConstr(EConstr constr, String destRegister, BackendScope scope) {
        int size = scope.getType(constr.ident_).getClassDefinition().getClassSize();

        List<AssemblyInstruction> instructions = new ArrayList<>();
        instructions.add(new PushInstruction(Register.RDI, scope));
        instructions.add(new MovInstruction(Register.RDI, YieldUtils.number(size * WORD_SIZE)));
        instructions.add(new CallInstruction(ExternalFunctions.MALLOC_SIZE));

        if (!Register.RAX.equals(destRegister)) {
            instructions.add(new MovInstruction(destRegister, Register.RAX));
        }

        instructions.add(new PopInstruction(Register.RDI, scope));

        return instructions;
    }

    public static List<AssemblyInstruction> generateMthAcc(ObjMethAcc acc, TypeDefinition type, String destRegister, BackendScope scope) {
        List<AssemblyInstruction> instructions = new ArrayList<>();

        instructions.addAll(generatePushBeforeFunction());

        int numberOfArgsPassedOnStack = max(0, (acc.listexpr_.size() - 5)); // - 6 + 1 - dodajemy "this" jako pierwszy argument
        int numberOfTempsOnStack = scope.getNumberOfTempsOnStack();

        boolean hasToAlignStack = (numberOfArgsPassedOnStack + numberOfTempsOnStack) % 2 != 0;

        if (hasToAlignStack) {
            instructions.add(new Comment("aligning stack"));
            instructions.add(new SubInstruction(Register.RSP, YieldUtils.number(WORD_SIZE)));
        }

        // object which method is called upon is in rax
        instructions.add(new MovInstruction(Register.RDI, Register.RAX));

        if (type.isInterfaceType()) {
            // TODO: put to rdi runtime class of object, not the whole interface
        }

        instructions.addAll(generateMethodArguments(acc.listexpr_, scope));

        //TODO: change to support interfaces, take offset no from typedef, but from runtime object
        instructions.add(new CallInstruction(type.getClassDefinition().methods.get(acc.ident_).label.getLabelName()));

        // function args pushed on stack?
        int offset = max(0, (acc.listexpr_.size() - 5)) * WORD_SIZE;

        if (offset > 0) {
            instructions.add(new AddInstruction(Register.RSP, YieldUtils.number(offset)));
            scope.changeTempsOnStack(-offset);
        }

        if (hasToAlignStack) {
            instructions.add(new AddInstruction(Register.RSP, YieldUtils.number(WORD_SIZE)));
        }

        instructions.addAll(generatePopAfterFunction());

        return instructions;
    }

    public static List<AssemblyInstruction> generateThis(String destRegister, BackendScope scope) {
        VariableCompilerInfo varInfo = scope.getVariable(THIS_KEYWORD);

        int offset = varInfo.getOffset() * WORD_SIZE;

        return new ArrayList<>(Collections.singletonList(new MovInstruction(destRegister, MemoryReference.getWithOffset(Register.RBP, offset))));
    }


    public static List<AssemblyInstruction> notImplemented(Expr expr) {
        throw new CompilerException("Compiling expression " + expr.getClass() + " not implemented yet");
    }

}
