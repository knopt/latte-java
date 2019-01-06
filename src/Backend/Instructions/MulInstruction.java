package src.Backend.Instructions;

public class MulInstruction implements AssemblyInstruction {
    private String destRegister;
    private String sourceRegister;

    public MulInstruction(String destRegister, String sourceRegister) {
        this.destRegister = destRegister;
        this.sourceRegister = sourceRegister;
    }

    @Override
    public String yield() {
        return ConstantUtils.TAB + "imul " + destRegister + ", " + sourceRegister;
    }


}
