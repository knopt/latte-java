package src.Backend.Instructions;

public class CompareInstruction implements AssemblyInstruction {
    private String sourceRegister;
    private String destRegister;

    public CompareInstruction(String destRegister, String sourceRegister) {
        this.sourceRegister = sourceRegister;
        this.destRegister = destRegister;
    }

    @Override
    public String yield() {
        return ConstantUtils.TAB + "cmp " + destRegister + ", " + sourceRegister;
    }
}
