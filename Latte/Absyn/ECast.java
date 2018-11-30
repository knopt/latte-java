package Latte.Absyn; // Java Package generated by the BNF Converter.

import java.util.Objects;
import java.util.function.Function;

public class ECast extends Expr {
    public final TypeName typename_;
    public final Expr expr_;
    public int line_num, col_num, offset;

    public ECast(TypeName p1, Expr p2) {
        typename_ = p1;
        expr_ = p2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ECast eCast = (ECast) o;
        return line_num == eCast.line_num &&
                col_num == eCast.col_num &&
                offset == eCast.offset &&
                Objects.equals(typename_, eCast.typename_) &&
                Objects.equals(expr_, eCast.expr_);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typename_, expr_, line_num, col_num, offset);
    }

    @Override
    public <T> T match(Function<EVar, T> eVar, Function<ELitInt, T> eLitInt, Function<ELitTrue, T> eLitTrue, Function<ELitFalse, T> eLitFalse, Function<EThis, T> eThis, Function<ENull, T> eNull, Function<EApp, T> eApp, Function<EString, T> eString, Function<EConstr, T> eConstr, Function<EArrConstr, T> eArrConstr, Function<ENDArrAcc, T> eNDArrAcc, Function<Neg, T> neg, Function<Not, T> not, Function<EMul, T> eMul, Function<EAdd, T> eAdd, Function<ERel, T> eRel, Function<EAnd, T> eAnd, Function<EOr, T> eOr, Function<EObjAcc, T> eObjAcc, Function<ECast, T> eCast) {
        return eCast.apply(this);
    }
}
