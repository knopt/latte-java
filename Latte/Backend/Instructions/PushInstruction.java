package Latte.Backend.Instructions;

public class PushInstruction implements AssemblyInstruction {
    private String register;

    public PushInstruction(String register) {
        this.register = register;
    }

    @Override
    public String yield() {
        return ConstantUtils.TAB + "push " + register;
    }
}
