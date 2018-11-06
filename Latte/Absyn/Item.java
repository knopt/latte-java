package Latte.Absyn; // Java Package generated by the BNF Converter.

import java.util.function.Function;

public abstract class Item implements java.io.Serializable {
    public abstract <R, A> R accept(Item.Visitor<R, A> v, A arg);

    public interface Visitor<R, A> {
        public R visit(Latte.Absyn.NoInit p, A arg);

        public R visit(Latte.Absyn.Init p, A arg);

    }

    public abstract <T> T match(Function<Latte.Absyn.NoInit, T> noInit,
                                Function<Latte.Absyn.Init, T> init);

}
