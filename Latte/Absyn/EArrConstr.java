package Latte.Absyn; // Java Package generated by the BNF Converter.

import java.util.function.Function;

public class EArrConstr extends Expr implements Positioned {

    @Override
    public String getPosition() {
        return "(" + line_num + ", " + col_num + ")";
    }


    public final String ident_;
    public final ListSizeBracket listsizebracket_;
    public int line_num, col_num, offset;

    public EArrConstr(String p1, ListSizeBracket p2) {
        ident_ = p1;
        listsizebracket_ = p2;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof EArrConstr) {
            EArrConstr x = (EArrConstr) o;
            return this.ident_.equals(x.ident_) && this.listsizebracket_.equals(x.listsizebracket_);
        }
        return false;
    }

    public int hashCode() {
        return 37 * (this.ident_.hashCode()) + this.listsizebracket_.hashCode();
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
        return eArrConstr.apply(this);
    }
}
