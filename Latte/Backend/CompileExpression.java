package Latte.Backend;

import Latte.Absyn.*;
import Latte.Backend.Instructions.*;
import Latte.Exceptions.CompilerException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CompileExpression {

    public static List<AssemblyInstruction> generateExpr(Expr expr, String sourceRegister, String destRegister) {
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
                (neg) -> generateNeg(neg, sourceRegister, destRegister),
                (not) -> generateNot(not, sourceRegister, destRegister),
                (e) -> notImplemented(e),
                (add) -> generateAdd(add, sourceRegister, destRegister),
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

    public static List<AssemblyInstruction> generateNeg(Neg neg, String sourceRegister, String destRegister) {
        List<AssemblyInstruction> assemblyInstructions = generateExpr(neg.expr_, sourceRegister, destRegister);
        NegInstruction negInstr = new NegInstruction(destRegister);

        assemblyInstructions.add(negInstr);

        return assemblyInstructions;
    }

    public static List<AssemblyInstruction> generateNot(Not not, String sourceRegister, String destRegister) {
        List<AssemblyInstruction> assemblyInstructions = generateExpr(not.expr_, sourceRegister, destRegister);

        CompareUnsignedInstruction cmpInstr = new CompareUnsignedInstruction(sourceRegister);
        MovInstruction movInstruction = new MovInstruction(sourceRegister, YieldUtils.number(0));
        SetIfEqualInstruction setIfEqualInstruction = new SetIfEqualInstruction("al");

        assemblyInstructions.addAll(new ArrayList<>(Arrays.asList(cmpInstr, movInstruction, setIfEqualInstruction)));

        return assemblyInstructions;
    }

    public static List<AssemblyInstruction> generateAdd(EAdd add, String sourceRegister, String destRegister) {
        List<AssemblyInstruction> instructions = generateExpr(add.expr_2, sourceRegister, destRegister);
        instructions.add(new PushInstruction(Register.RAX));

        instructions.addAll(generateExpr(add.expr_1, sourceRegister, destRegister));
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


    public static List<AssemblyInstruction> notImplemented(Expr expr) {
        throw new CompilerException("Compiling expression " + expr.getClass() + " not implemented yet");
    }

}
