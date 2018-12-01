package Latte.Backend.Instructions;

public class CompareUnsignedInstruction implements AssemblyInstruction {
    private String register;

    public CompareUnsignedInstruction(String register) {
        this.register = register;
    }

    @Override
    public String yield() {
        return ConstantUtils.TAB + "cmpl " + register + ", " + YieldUtils.number(0);
    }
}
