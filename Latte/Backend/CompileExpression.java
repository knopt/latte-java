package Latte.Backend;

import Latte.Absyn.*;
import Latte.Backend.Definitions.BackendScope;
import Latte.Backend.Definitions.Register;
import Latte.Backend.Definitions.VariableCompilerInfo;
import Latte.Backend.Instructions.*;
import Latte.Definitions.BasicTypeDefinition;
import Latte.Definitions.TypeDefinition;
import Latte.Exceptions.CompilerException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static Latte.Backend.Instructions.ConstantUtils.WORD_SIZE;
import static Latte.Backend.LabelsGenerator.getNonceLabel;
import static Latte.Frontend.TypeUtils.getType;

public class CompileExpression {

    public static List<AssemblyInstruction> generateExpr(Expr expr, String destRegister, String sourceRegister, BackendScope scope) {
        return expr.match(
                (var) -> generateVar(var, destRegister, scope),
                (eInt) -> generateInt(eInt, destRegister),
                (e) -> generateTrue(destRegister),
                (e) -> generateFalse(destRegister),
                (e) -> notImplemented(e),
                (e) -> notImplemented(e),
                (e) -> notImplemented(e),
                (e) -> notImplemented(e),
                (e) -> notImplemented(e),
                (e) -> notImplemented(e),
                (e) -> notImplemented(e),
                (neg) -> generateNeg(neg, destRegister, sourceRegister, scope),
                (not) -> generateNot(not, destRegister, sourceRegister, scope),
                (mul) -> generateMul(mul, destRegister, sourceRegister, scope),
                (add) -> generateAdd(add, destRegister, sourceRegister, scope),
                (rel) -> generateRel(rel, destRegister, sourceRegister, scope),
                (and) -> generateLazyAnd(and, destRegister, sourceRegister, scope),
                (eOr) -> generateLazyOr(eOr, destRegister, sourceRegister, scope),
                (e) -> notImplemented(e),
                (cast) -> generateCast(cast, destRegister, sourceRegister, scope)
        );
    }

    public static List<AssemblyInstruction> generateVar(EVar var, String destRegister, BackendScope scope) {
        VariableCompilerInfo varInfo = scope.getVariable(var.ident_);

        int offset = varInfo.getOffset() * WORD_SIZE * -1;

        return new ArrayList<>(Collections.singletonList(new MovInstruction(destRegister, MemoryReference.getWithOffset(Register.RBP, offset))));
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
        List<AssemblyInstruction> instructions = generateExpr(add.expr_2, destRegister, sourceRegister, scope);
        instructions.add(new PushInstruction(Register.RAX));

        instructions.addAll(generateExpr(add.expr_1, destRegister, sourceRegister, scope));
        instructions.add(new PopInstruction(Register.RCX));

        List<AssemblyInstruction> oppInstr = add.addop_.match(
                (ignored) -> getPlusInstruction(Register.RAX, Register.RCX, scope),
                (ignored) -> getMinusInstruction(Register.RAX, Register.RCX, scope)
        );

        instructions.addAll(oppInstr);

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

        List<AssemblyInstruction> instructions1 = generateExpr(mul.expr_1, destRegister, sourceRegister, scope);
        List<AssemblyInstruction> instructions2 = generateExpr(mul.expr_2, destRegister, sourceRegister, scope);

        instructions.addAll(instructions2);
        instructions.add(new PushInstruction(Register.RAX));

        instructions.addAll(instructions1);
        instructions.add(new PopInstruction(Register.RCX));

        List<AssemblyInstruction> oppInstr = mul.mulop_.match(
                (ignored) -> generateMultiplyInstructions(Register.RAX, Register.RCX, scope),
                (ignored) -> generateDivideInstructions(Register.RAX, Register.RCX, scope),
                (ignored) -> generateModInstruction(Register.RAX, Register.RCX, scope)
        );

        instructions.addAll(oppInstr);

        return instructions;
    }

    public static List<AssemblyInstruction> generateMultiplyInstructions(String destRegister, String sourceRegister, BackendScope scope) {
        return new ArrayList<>(Collections.singletonList(new MulInstruction(destRegister, sourceRegister)));
    }

    public static List<AssemblyInstruction> generateDivideInstructions(String destRegsister, String sourceRegister, BackendScope scope) {
        List<AssemblyInstruction> instructions = new ArrayList<>();

        if (!destRegsister.equals(Register.RAX)) {
            instructions.add(new MovInstruction(Register.RAX, destRegsister));
        }

        instructions.add(new XorInstruction(Register.RDX, Register.RDX));
        instructions.add(new DivInstruction(sourceRegister));

        if (!destRegsister.equals(Register.RAX)) {
            instructions.add(new MovInstruction(destRegsister, Register.RAX));
        }

        return instructions;
    }

    public static List<AssemblyInstruction> generateModInstruction(String destRegister, String sourceRegister, BackendScope scope) {
        List<AssemblyInstruction> instructions = generateDivideInstructions(destRegister, sourceRegister, scope);
        instructions.add(
                new MovInstruction(Register.RAX, Register.RDX)
        );

        return instructions;
    }

    public static List<AssemblyInstruction> generateLazyOr(EOr eOr, String destRegister, String sourceRegister, BackendScope scope) {
        List<AssemblyInstruction> expr1Instructions = generateExpr(eOr.expr_1, destRegister, sourceRegister, scope);

        Label endLabel = getNonceLabel("or_end");

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

        Label endLabel = getNonceLabel("and_end");

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
        List<AssemblyInstruction> instructions = generateExpr(rel.expr_2, destRegister, sourceRegister, scope);
        instructions.add(new PushInstruction(Register.RAX));

        instructions.addAll(generateExpr(rel.expr_1, destRegister, sourceRegister, scope));
        instructions.add(new PopInstruction(Register.RCX));

        instructions.add(new CompareInstruction(Register.RAX, Register.RCX));
        instructions.add(new MovInstruction(destRegister, YieldUtils.number(1)));

        Label endLabel = LabelsGenerator.getNonceLabel("rel_end");

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


    public static List<AssemblyInstruction> notImplemented(Expr expr) {
        throw new CompilerException("Compiling expression " + expr.getClass() + " not implemented yet");
    }

}
