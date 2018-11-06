package Latte.Absyn; // Java Package generated by the BNF Converter.

import java.util.function.Function;

public class Not extends Latte.Absyn.Expr {
    public final Latte.Absyn.Expr expr_;

    public Not(Latte.Absyn.Expr p1) {
        expr_ = p1;
    }

    public <R, A> R accept(Latte.Absyn.Expr.Visitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Latte.Absyn.Not) {
            Latte.Absyn.Not x = (Latte.Absyn.Not) o;
            return this.expr_.equals(x.expr_);
        }
        return false;
    }

    public int hashCode() {
        return this.expr_.hashCode();
    }

    public <T> T match(Function<Latte.Absyn.EVar, T> eVar,
                       Function<Latte.Absyn.ELitInt, T> eLitInt,
                       Function<Latte.Absyn.ELitTrue, T> eLitTrue,
                       Function<Latte.Absyn.ELitFalse, T> eLitFalse,
                       Function<Latte.Absyn.EThis, T> eThis,
                       Function<Latte.Absyn.ENull, T> eNull,
                       Function<Latte.Absyn.EApp, T> eApp,
                       Function<Latte.Absyn.EString, T> eString,
                       Function<Latte.Absyn.EConstr, T> eConstr,
                       Function<Latte.Absyn.EArrConstr, T> eArrConstr,
                       Function<Latte.Absyn.ENDArrAcc, T> eNDArrAcc,
                       Function<Latte.Absyn.FieldAcc, T> fieldAcc,
                       Function<Latte.Absyn.MethodCall, T> methodCall,
                       Function<Latte.Absyn.Neg, T> neg,
                       Function<Latte.Absyn.Not, T> not,
                       Function<Latte.Absyn.EMul, T> eMul,
                       Function<Latte.Absyn.EAdd, T> eAdd,
                       Function<Latte.Absyn.ERel, T> eRel,
                       Function<Latte.Absyn.EAnd, T> eAnd,
                       Function<Latte.Absyn.EOr, T> eOr) {
        return not.apply(this);
    }
}
