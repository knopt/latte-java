package Latte.Absyn; // Java Package generated by the BNF Converter.

import java.util.function.Function;

public class SBracket extends Latte.Absyn.SizeBracket {
    public final Latte.Absyn.Expr expr_;

    public SBracket(Latte.Absyn.Expr p1) {
        expr_ = p1;
    }

    public <R, A> R accept(Latte.Absyn.SizeBracket.Visitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Latte.Absyn.SBracket) {
            Latte.Absyn.SBracket x = (Latte.Absyn.SBracket) o;
            return this.expr_.equals(x.expr_);
        }
        return false;
    }

    public int hashCode() {
        return this.expr_.hashCode();
    }


    public <T> T match(Function<Latte.Absyn.SBracket, T> sBracket) {
        return sBracket.apply(this);
    }
}
