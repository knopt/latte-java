package Latte.Absyn; // Java Package generated by the BNF Converter.

import java.util.function.Function;

public class Void extends BasicType {
    public int line_num, col_num, offset;

    public Void() {
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Void) {
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
        return voidT.apply(this);
    }
}
