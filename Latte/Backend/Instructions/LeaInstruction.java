package Latte.Backend.Instructions;

import static Latte.Backend.Instructions.ConstantUtils.WORD_SIZE;
import static java.lang.Math.abs;

public class LeaInstruction implements AssemblyInstruction {
    private String destRegister;
    private String baseRegister;
    private String offsetRegister;
    private int constOffset;
    private int multiplier;

    private String getWithSign(int n) {
        if (n > 0) {
            return "+ " + n;
        }

        if (n < 0) {
            return "- " + abs(n);
        }

        return "";
    }

    private String formatBaseRegister(String base) {
        if (base != null) {
            return baseRegister + ", ";
        }
        return "";
    }

    public LeaInstruction(String destRegister, String baseRegister, String offsetRegister, int multipier, int constOffset) {
        this.destRegister = destRegister;
        this.baseRegister = baseRegister;
        this.offsetRegister = offsetRegister;
        this.constOffset = constOffset;
        this.multiplier = multipier;
    }

    public LeaInstruction(String destRegister, String baseRegister, String offsetRegister, int constOffset) {
        this.destRegister = destRegister;
        this.baseRegister = baseRegister;
        this.offsetRegister = offsetRegister;
        this.constOffset = constOffset;
        this.multiplier = WORD_SIZE;
    }

    @Override
    public String yield() {
        return ConstantUtils.TAB + "lea " + destRegister +
                ", [" + baseRegister + ", " + getWithSign(multiplier) + " * " + offsetRegister +
                " " + getWithSign(constOffset);
    }
}
