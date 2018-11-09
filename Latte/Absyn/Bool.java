package Absyn; // Java Package generated by the BNF Converter.

import java.util.function.Function;

public class Bool extends BasicType {
    public int line_num, col_num, offset;

    public Bool() {
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Bool) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return 37;
    }

    public <T> T match(Function<Int, T> intT,
                       Function<Str, T> strT,
                       Function<Bool, T> boolT,
                       Function<Void, T> voidT) {
        return boolT.apply(this);
    }
}
