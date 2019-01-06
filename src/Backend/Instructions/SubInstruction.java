package src.Backend.Instructions;

public class SubInstruction implements AssemblyInstruction {
    private String destRegister;
    private String sourceRegister;

    public SubInstruction(String destRegister, String sourceRegister) {
        this.destRegister = destRegister;
        this.sourceRegister = sourceRegister;
    }

    @Override
    public String yield() {
        return ConstantUtils.TAB + "sub " + destRegister + ", " + sourceRegister;
    }
}
