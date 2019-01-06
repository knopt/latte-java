package src.Backend.Instructions;

public class SetIfEqualInstruction implements AssemblyInstruction {
    public String register;

    public SetIfEqualInstruction(String register) {
        this.register = register;
    }

    @Override
    public String yield() {
        return ConstantUtils.TAB + "sete " + register;
    }
}
