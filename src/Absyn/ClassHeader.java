package src.Absyn; // Java Package generated by the BNF Converter.

import java.util.function.Function;

public abstract class ClassHeader implements java.io.Serializable {

    public abstract <T> T match(Function<ClassDec, T> classDec,
                                Function<InterDec, T> interDec);

}
