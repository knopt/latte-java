package src.Backend.Instructions;

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
