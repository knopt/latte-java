package Latte.Absyn; // Java Package generated by the BNF Converter.

import java.util.function.Function;

public class EImpl extends Implements {
    public int line_num, col_num, offset;

    public EImpl() {
    }

    public <T> T match(Function<InterImpl, T> internImpl,
                       Function<EImpl, T> emptyImpl) {
        return emptyImpl.apply(this);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Latte.Absyn.EImpl) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return 37;
    }


}
