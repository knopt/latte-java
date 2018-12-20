package Latte.Backend.Instructions;

public class StringInstruction implements AssemblyInstruction {
    private String stringLiteral;

    public StringInstruction(String stringLiteral) {
        this.stringLiteral = stringLiteral;
    }

    @Override
    public String yield() {
        return ConstantUtils.TAB + ".string " + "\"" + stringLiteral + "\"\n" +
                ConstantUtils.TAB + ".string \"\"" ;
    }
}
