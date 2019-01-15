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
        return ConstantUtils.TAB + stringLabel + " db " + "\"" + escapeBackslashes(stringLiteral) + "\", 0";
    }

    public static String escapeBackslashes(String str) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);

            switch (c) {
                case '\n':
                    builder.append("\", 0x0A, \"");
                    break;
                case '\t':
                    builder.append("\", 0x09, \"");
                    break;
                case '\b':
                    builder.append("\", 0x08, \"");
                    break;
                case '\f':
                    builder.append("\", 0x0C, \"");
                    break;
                case '\r':
                    builder.append("\", 0x0D, \"");
                    break;
                case '\'':
                    builder.append("'");
                    break;
                case '\"':
                    builder.append("\", 34, \"");
                    break;
                case '\\':
                    builder.append("\\");
                    break;
                default:
                    builder.append(c);
            }
        }

        return builder.toString();
    }
}
