package Latte.Absyn; // Java Package generated by the BNF Converter.

import java.util.function.Function;

public class Ass extends Stmt implements Positioned {

    @Override
    public String getPosition() {
        return "(" + line_num + ", " + col_num + ")";
    }

    public final Lhs lhs_;
    public final Expr expr_;
    public int line_num, col_num, offset;

    public Ass(Lhs p1, Expr p2) {
        lhs_ = p1;
        expr_ = p2;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Latte.Absyn.Ass) {
            Latte.Absyn.Ass x = (Latte.Absyn.Ass) o;
            return this.lhs_.equals(x.lhs_) && this.expr_.equals(x.expr_);
        }
        return false;
    }

    public int hashCode() {
        return 37 * (this.lhs_.hashCode()) + this.expr_.hashCode();
    }

    public <T> T match(Function<Empty, T> empty,
                       Function<BStmt, T> bStmt,
                       Function<Decl, T> decl,
                       Function<Ass, T> ass,
                       Function<Incr, T> incr,
                       Function<Decr, T> decr,
                       Function<Ret, T> ret,
                       Function<VRet, T> vRet,
                       Function<Cond, T> cond,
                       Function<CondElse, T> condElse,
                       Function<While, T> sWhile,
                       Function<SExp, T> sExp,
                       Function<ForArr, T> forArr) {
        return ass.apply(this);
    }
}
