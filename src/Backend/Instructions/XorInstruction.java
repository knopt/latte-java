package src.Backend.Instructions;

public class XorInstruction implements AssemblyInstruction {
    private String destRegister;
    private String sourceRegister;

    public XorInstruction(String destRegister, String sourceRegister) {
        this.destRegister = destRegister;
        this.sourceRegister = sourceRegister;
    }

    @Override
    public String yield() {
        return ConstantUtils.TAB + "xor " + destRegister + ", " + sourceRegister;
    }
}
