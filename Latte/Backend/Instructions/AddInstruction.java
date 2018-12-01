package Latte.Backend.Instructions;

public class AddInstruction implements AssemblyInstruction {
    private String destRegister;
    private String sourceRegister;

    public AddInstruction(String destRegister, String sourceRegister) {
        this.destRegister = destRegister;
        this.sourceRegister = sourceRegister;
    }

    @Override
    public String yield() {
        return ConstantUtils.TAB + "add " + destRegister + ", " + sourceRegister;
    }
}
