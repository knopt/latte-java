package Latte.Absyn; // Java Package generated by the BNF Converter.

import java.util.function.Function;

public class LTH extends Latte.Absyn.RelOp {
    public LTH() {
    }

    public <R, A> R accept(Latte.Absyn.RelOp.Visitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Latte.Absyn.LTH) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return 37;
    }

    public <T> T match(Function<Latte.Absyn.LTH, T> lth,
                        Function<Latte.Absyn.LE, T> le,
                        Function<Latte.Absyn.GTH, T> gth,
                        Function<Latte.Absyn.GE, T> ge,
                        Function<Latte.Absyn.EQU, T> equ,
                        Function<Latte.Absyn.NE, T> ne) {
        return lth.apply(this);
    }
}
