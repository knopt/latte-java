package Latte.Absyn; // Java Package generated by the BNF Converter.

import java.util.function.Function;

public abstract class BasicType implements java.io.Serializable {
    public abstract <R, A> R accept(BasicType.Visitor<R, A> v, A arg);

    public interface Visitor<R, A> {
        public R visit(Latte.Absyn.Int p, A arg);

        public R visit(Latte.Absyn.Str p, A arg);

        public R visit(Latte.Absyn.Bool p, A arg);

        public R visit(Latte.Absyn.Void p, A arg);

    }

    public abstract <T> T match(Function<Latte.Absyn.Int, T> intT,
                                Function<Latte.Absyn.Str, T> strT,
                                Function<Latte.Absyn.Bool, T> boolT,
                                Function<Latte.Absyn.Void, T> voidT);

}
