package Latte.Backend.Instructions;

public class PopInstruction implements AssemblyInstruction {
    private String register;

    public PopInstruction(String register) {
        this.register = register;
    }

    @Override
    public String yield() {
        return ConstantUtils.TAB + "pop " + register;
    }
}
