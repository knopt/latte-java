package Latte.Absyn; // Java Package generated by the BNF Converter.

import java.util.function.Function;

public abstract class Item implements java.io.Serializable {

    public abstract <T> T match(Function<NoInit, T> noInit,
                                Function<Init, T> init);

}
