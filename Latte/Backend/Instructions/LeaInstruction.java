package Latte.Backend.Instructions;

import static Latte.Backend.Instructions.ConstantUtils.WORD_SIZE;
import static java.lang.Math.abs;

public class LeaInstruction implements AssemblyInstruction {
    private String destRegister;
    private String source;

    public LeaInstruction(String destRegister, String source) {
        this.destRegister = destRegister;
        this.source = source;
    }

    @Override
    public String yield() {
        return ConstantUtils.TAB + "lea " + destRegister + ", " + source;
    }
}
