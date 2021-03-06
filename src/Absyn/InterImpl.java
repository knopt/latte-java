package src.Absyn; // Java Package generated by the BNF Converter.

import java.util.function.Function;

public class InterImpl extends Implements {
    public final String ident_;
    public int line_num, col_num, offset;

    public InterImpl(String p1) {
        ident_ = p1;
    }


    public <T> T match(Function<InterImpl, T> internImpl,
                       Function<EImpl, T> emptyImpl) {
        return internImpl.apply(this);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof src.Absyn.InterImpl) {
            src.Absyn.InterImpl x = (src.Absyn.InterImpl) o;
            return this.ident_.equals(x.ident_);
        }
        return false;
    }

    public int hashCode() {
        return this.ident_.hashCode();
    }


}
