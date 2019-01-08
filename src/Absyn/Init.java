package src.Absyn; // Java Package generated by the BNF Converter.

import java.util.function.Function;

public class Init extends Item implements Positioned {

    @Override
    public String getPosition() {
        return "(" + line_num + ", " + offset + ")";
    }


    public final String ident_;
    public final Expr expr_;
    public int line_num, col_num, offset;

    public Init(String p1, Expr p2) {
        ident_ = p1;
        expr_ = p2;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Init) {
            Init x = (Init) o;
            return this.ident_.equals(x.ident_) && this.expr_.equals(x.expr_);
        }
        return false;
    }

    public int hashCode() {
        return 37 * (this.ident_.hashCode()) + this.expr_.hashCode();
    }


    public <T> T match(Function<NoInit, T> noInit,
                       Function<Init, T> init) {
        return init.apply(this);
    }
}