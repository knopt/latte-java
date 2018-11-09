package Absyn; // Java Package generated by the BNF Converter.

import java.util.function.Function;

public class ERel extends Expr {
    public final Expr expr_1, expr_2;
    public final RelOp relop_;
    public int line_num, col_num, offset;

    public ERel(Expr p1, RelOp p2, Expr p3) {
        expr_1 = p1;
        relop_ = p2;
        expr_2 = p3;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof ERel) {
            ERel x = (ERel) o;
            return this.expr_1.equals(x.expr_1) && this.relop_.equals(x.relop_) && this.expr_2.equals(x.expr_2);
        }
        return false;
    }

    public int hashCode() {
        return 37 * (37 * (this.expr_1.hashCode()) + this.relop_.hashCode()) + this.expr_2.hashCode();
    }

    public <T> T match(Function<EVar, T> eVar,
                       Function<ELitInt, T> eLitInt,
                       Function<ELitTrue, T> eLitTrue,
                       Function<ELitFalse, T> eLitFalse,
                       Function<EThis, T> eThis,
                       Function<ENull, T> eNull,
                       Function<EApp, T> eApp,
                       Function<EString, T> eString,
                       Function<EConstr, T> eConstr,
                       Function<EArrConstr, T> eArrConstr,
                       Function<ENDArrAcc, T> eNDArrAcc,
                       Function<FieldAcc, T> fieldAcc,
                       Function<MethodCall, T> methodCall,
                       Function<Neg, T> neg,
                       Function<Not, T> not,
                       Function<EMul, T> eMul,
                       Function<EAdd, T> eAdd,
                       Function<ERel, T> eRel,
                       Function<EAnd, T> eAnd,
                       Function<EOr, T> eOr) {
        return eRel.apply(this);
    }
}
