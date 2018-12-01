package Latte.Backend.Instructions;

public class Label implements AssemblyInstruction {
    private String labelName;

    public Label(String labelName) {
        this.labelName = labelName;
    }


    @Override
    public String yield() {
        return labelName + ":";
    }
}
