package Latte.Absyn; // Java Package generated by the BNF Converter.

import java.util.function.Function;

public class Minus extends AddOp {
    public int line_num, col_num, offset;

    public Minus() {
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Minus) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return 37;
    }

    public <T> T match(Function<Plus, T> plus,
                       Function<Minus, T> minus) {
        return minus.apply(this);
    }
}
