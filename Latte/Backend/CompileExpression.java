package Latte.Backend;

import Latte.Absyn.*;
import Latte.Backend.Instructions.*;
import Latte.Exceptions.CompilerException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CompileExpression {

    public static List<AssemblyInstruction> generateExpr(Expr expr, String destRegister, String sourceRegister) {
        return expr.match(
                (e) -> notImplemented(e),
                (eInt) -> generateInt(eInt, destRegister),
                (e) -> notImplemented(e),
                (e) -> notImplemented(e),
                (e) -> notImplemented(e),
                (e) -> notImplemented(e),
                (e) -> notImplemented(e),
                (e) -> notImplemented(e),
                (e) -> notImplemented(e),
                (e) -> notImplemented(e),
                (e) -> notImplemented(e),
                (neg) -> generateNeg(neg, destRegister, sourceRegister),
                (not) -> generateNot(not, destRegister, sourceRegister),
                (mul) -> generateMul(mul, destRegister, sourceRegister),
                (add) -> generateAdd(add, destRegister, sourceRegister),
                (e) -> notImplemented(e),
                (e) -> notImplemented(e),
                (e) -> notImplemented(e),
                (e) -> notImplemented(e),
                (e) -> notImplemented(e)
        );
    }

    public static List<AssemblyInstruction> generateInt(ELitInt eInt, String destRegister) {
        MovInstruction mov = new MovInstruction(destRegister, YieldUtils.number(eInt.integer_));
        return new ArrayList<>(Collections.singletonList(mov));
    }

    public static List<AssemblyInstruction> generateNeg(Neg neg, String destRegister, String sourceRegister) {
        List<AssemblyInstruction> assemblyInstructions = generateExpr(neg.expr_, destRegister, sourceRegister);
        NegInstruction negInstr = new NegInstruction(destRegister);

        assemblyInstructions.add(negInstr);

        return assemblyInstructions;
    }

    public static List<AssemblyInstruction> generateNot(Not not, String destRegister, String sourceRegister) {
        List<AssemblyInstruction> assemblyInstructions = generateExpr(not.expr_, destRegister, sourceRegister);

        CompareUnsignedInstruction cmpInstr = new CompareUnsignedInstruction(sourceRegister);
        MovInstruction movInstruction = new MovInstruction(sourceRegister, YieldUtils.number(0));
        SetIfEqualInstruction setIfEqualInstruction = new SetIfEqualInstruction("al");

        assemblyInstructions.addAll(new ArrayList<>(Arrays.asList(cmpInstr, movInstruction, setIfEqualInstruction)));

        return assemblyInstructions;
    }

    public static List<AssemblyInstruction> generateAdd(EAdd add, String destRegister, String sourceRegister) {
        List<AssemblyInstruction> instructions = generateExpr(add.expr_2, destRegister, sourceRegister);
        instructions.add(new PushInstruction(Register.RAX));

        instructions.addAll(generateExpr(add.expr_1, destRegister, sourceRegister));
        instructions.add(new PopInstruction(Register.RCX));

        List<AssemblyInstruction> oppInstr = add.addop_.match(
                (ignored) -> getPlusInstruction(Register.RAX, Register.RCX),
                (ignored) -> getMinusInstruction(Register.RAX, Register.RCX)
        );

        instructions.addAll(oppInstr);

        return instructions;
    }

    public static List<AssemblyInstruction> getMinusInstruction(String destRegister, String sourceRegister) {
        return new ArrayList<>(Collections.singletonList(new SubInstruction(destRegister, sourceRegister)));
    }

    public static List<AssemblyInstruction> getPlusInstruction(String destRegister, String sourceRegister) {
        return new ArrayList<>(Collections.singletonList(new AddInstruction(destRegister, sourceRegister)));

    }

    public static List<AssemblyInstruction> generateMul(EMul mul, String destRegister, String sourceRegister) {
        List<AssemblyInstruction> instructions = new ArrayList<>();

        List<AssemblyInstruction> instructions1 = generateExpr(mul.expr_1, destRegister, sourceRegister);
        List<AssemblyInstruction> instructions2 = generateExpr(mul.expr_2, destRegister, sourceRegister);

        instructions.addAll(instructions2);
        instructions.add(new PushInstruction(Register.RAX));

        instructions.addAll(instructions1);
        instructions.add(new PopInstruction(Register.RCX));

        List<AssemblyInstruction> oppInstr = mul.mulop_.match(
                (ignored) -> generateMultiplyInstructions(Register.RAX, Register.RCX),
                (ignored) -> generateDivideInstruction(Register.RAX, Register.RCX),
                (ignored) -> generateModInstruction(Register.RAX, Register.RCX)
        );

        instructions.addAll(oppInstr);

        return instructions;
    }

    public static List<AssemblyInstruction> generateMultiplyInstructions(String destRegister, String sourceRegister) {
        return new ArrayList<>(Collections.singletonList(new MulInstruction(destRegister, sourceRegister)));
    }

    public static List<AssemblyInstruction> generateDivideInstruction(String destRegsister, String sourceRegister) {
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

    public static List<AssemblyInstruction> generateModInstruction(String destRegister, String sourceRegister) {
        List<AssemblyInstruction> instructions = generateDivideInstruction(destRegister, sourceRegister);
        instructions.add(
                new MovInstruction(Register.RAX, Register.RDX)
        );

        return instructions;
    }


    public static List<AssemblyInstruction> notImplemented(Expr expr) {
        throw new CompilerException("Compiling expression " + expr.getClass() + " not implemented yet");
    }

}
