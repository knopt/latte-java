package src.Backend.Instructions;


import org.apache.commons.lang3.StringEscapeUtils;

public class StringInstruction implements AssemblyInstruction {
    private String stringLabel;
    private String stringLiteral;

    public StringInstruction(String stringLabel, String stringLiteral) {
        this.stringLiteral = stringLiteral;
        this.stringLabel = stringLabel;
    }

    @Override
    public String yield() {
        return ConstantUtils.TAB + stringLabel + " db " + "\"" + StringEscapeUtils.escapeJava(stringLiteral) + "\", 0";
    }
}
