package Latte.Backend.Instructions;

public class MovInstruction implements AssemblyInstruction {
    private Object what;
    private Object where;

    public MovInstruction(Object where, Object what) {
        this.what = what;
        this.where = where;
    }

    public String yield() {
        return ConstantUtils.TAB + "mov " + where + ", " + what;
    }

}
