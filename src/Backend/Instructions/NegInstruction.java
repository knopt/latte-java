package src.Backend.Instructions;

public class NegInstruction implements AssemblyInstruction {
    private String register;

    public NegInstruction(String register) {
        this.register = register;
    }


    @Override
    public String yield() {
        return ConstantUtils.TAB + "neg " + register;
    }
}
