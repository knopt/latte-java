package Latte.Absyn; // Java Package generated by the BNF Converter.

import java.util.function.Function;

public class ENDArrAcc extends Expr implements Positioned {

    @Override
    public String getPosition() {
        return "(" + line_num + ", " + col_num + ")";
    }

    public final Expr expr_1, expr_2;
    public int line_num, col_num, offset;
    public ENDArrAcc(Expr p1, Expr p2) { expr_1 = p1; expr_2 = p2; }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Latte.Absyn.ENDArrAcc) {
            Latte.Absyn.ENDArrAcc x = (Latte.Absyn.ENDArrAcc)o;
            return this.expr_1.equals(x.expr_1) && this.expr_2.equals(x.expr_2);
        }
        return false;
    }

    public int hashCode() {
        return 37*(this.expr_1.hashCode())+this.expr_2.hashCode();
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
                       Function<Neg, T> neg,
                       Function<Not, T> not,
                       Function<EMul, T> eMul,
                       Function<EAdd, T> eAdd,
                       Function<ERel, T> eRel,
                       Function<EAnd, T> eAnd,
                       Function<EOr, T> eOr,
                       Function<EObjAcc, T> eObjAcc) {
        return eNDArrAcc.apply(this);
    }
}
