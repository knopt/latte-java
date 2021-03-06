package src.Absyn; // Java Package generated by the BNF Converter.

import java.util.function.Function;

public abstract class BasicType implements java.io.Serializable {

    public abstract <T> T match(Function<Int, T> intT,
                                Function<Str, T> strT,
                                Function<Bool, T> boolT,
                                Function<Void, T> voidT);

}
