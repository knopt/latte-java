package Latte.Absyn; // Java Package generated by the BNF Converter.

public class Mpublic extends Modifier {
    public Mpublic() {
    }

    public <R, A> R accept(Latte.Absyn.Modifier.Visitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Latte.Absyn.Mpublic) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return 37;
    }


}
