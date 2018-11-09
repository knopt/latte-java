package Latte.Absyn; // Java Package generated by the BNF Converter.

import java.util.function.Function;

public class NoInit extends Item {
    public final String ident_;
    public int line_num, col_num, offset;

    public NoInit(String p1) {
        ident_ = p1;
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof NoInit) {
            NoInit x = (NoInit) o;
            return this.ident_.equals(x.ident_);
        }
        return false;
    }

    public int hashCode() {
        return this.ident_.hashCode();
    }


    public <T> T match(Function<NoInit, T> noInit,
                       Function<Init, T> init) {
        return noInit.apply(this);
    }

}
