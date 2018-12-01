package Latte.Backend.Instructions;

public class MovInsrtuction implements AssemblyInstruction {
    private Object what;
    private Object where;

    public MovInsrtuction(Object what, Object where) {
        this.what = what;
        this.where = where;
    }

    public String yield() {
        return "mov " + where + ", " + what;
    }
}
