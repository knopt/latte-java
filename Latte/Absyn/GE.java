package Latte.Absyn; // Java Package generated by the BNF Converter.

import java.util.function.Function;

public class GE extends RelOp {
    public int line_num, col_num, offset;

    public GE() {
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof GE) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return 37;
    }

    public <T> T match(Function<LTH, T> lth,
                       Function<LE, T> le,
                       Function<GTH, T> gth,
                       Function<GE, T> ge,
                       Function<EQU, T> equ,
                       Function<NE, T> ne) {
        return ge.apply(this);
    }
}
