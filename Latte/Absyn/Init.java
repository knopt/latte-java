package Latte.Absyn; // Java Package generated by the BNF Converter.

import java.util.function.Function;

public class Init extends Latte.Absyn.Item {
    public final String ident_;
    public final Latte.Absyn.Expr expr_;

    public Init(String p1, Latte.Absyn.Expr p2) {
        ident_ = p1;
        expr_ = p2;
    }

    public <R, A> R accept(Latte.Absyn.Item.Visitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Latte.Absyn.Init) {
            Latte.Absyn.Init x = (Latte.Absyn.Init) o;
            return this.ident_.equals(x.ident_) && this.expr_.equals(x.expr_);
        }
        return false;
    }

    public int hashCode() {
        return 37 * (this.ident_.hashCode()) + this.expr_.hashCode();
    }


    public <T> T match(Function<Latte.Absyn.NoInit, T> noInit,
                       Function<Latte.Absyn.Init, T> init) {
        init.apply(this);
    }
}
