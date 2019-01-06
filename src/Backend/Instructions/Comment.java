package src.Backend.Instructions;

public class Comment implements AssemblyInstruction {
    private String comment;

    public Comment(String comment) {
        this.comment = comment;
    }


    @Override
    public String yield() {
        return "; " + comment;
    }
}
