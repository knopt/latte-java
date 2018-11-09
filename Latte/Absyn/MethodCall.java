package Absyn; // Java Package generated by the BNF Converter.

import java.util.function.Function;

public class MethodCall extends Expr {
    public final Expr expr_;
    public final String ident_;
    public final ListExpr listexpr_;
    public int line_num, col_num, offset;

    public MethodCall(Expr p1, String p2, ListExpr p3) {
        expr_ = p1;
        ident_ = p2;
        listexpr_ = p3;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof MethodCall) {
            MethodCall x = (MethodCall) o;
            return this.expr_.equals(x.expr_) && this.ident_.equals(x.ident_) && this.listexpr_.equals(x.listexpr_);
        }
        return false;
    }

    public int hashCode() {
        return 37 * (37 * (this.expr_.hashCode()) + this.ident_.hashCode()) + this.listexpr_.hashCode();
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
        return methodCall.apply(this);
    }
}
