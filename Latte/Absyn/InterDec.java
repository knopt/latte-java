package Latte.Absyn; // Java Package generated by the BNF Converter.

import java.util.function.Function;

public class InterDec extends Latte.Absyn.ClassHeader {
    public final String ident_;

    public InterDec(String p1) {
        ident_ = p1;
    }

    public <R, A> R accept(Latte.Absyn.ClassHeader.Visitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Latte.Absyn.InterDec) {
            Latte.Absyn.InterDec x = (Latte.Absyn.InterDec) o;
            return this.ident_.equals(x.ident_);
        }
        return false;
    }

    public int hashCode() {
        return this.ident_.hashCode();
    }


    public <T> T match(Function<Latte.Absyn.ClassDec, T> classDec,
                       Function<Latte.Absyn.InterDec, T> interDec) {
        return interDec.apply(this);
    }


}
