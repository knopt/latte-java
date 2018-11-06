package Latte.Absyn; // Java Package generated by the BNF Converter.

public class Div extends MulOp {
    public Div() {
    }

    public <R, A> R accept(Latte.Absyn.MulOp.Visitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Latte.Absyn.Div) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return 37;
    }


}
