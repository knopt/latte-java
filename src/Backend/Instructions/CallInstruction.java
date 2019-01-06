package src.Backend.Instructions;

public class CallInstruction implements AssemblyInstruction {
    private String functionLabel;

    public CallInstruction(String functionLabel) {
        this.functionLabel = functionLabel;
    }

    @Override
    public String yield() {
        return ConstantUtils.TAB + "call " + functionLabel;
    }
}
