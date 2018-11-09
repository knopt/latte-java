package Latte.Absyn; // Java Package generated by the BNF Converter.

import java.util.function.Function;

public class ClassName extends TypeName {
    public final String ident_;
    public int line_num, col_num, offset;

    public ClassName(String p1) {
        ident_ = p1;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof ClassName) {
            ClassName x = (ClassName) o;
            return this.ident_.equals(x.ident_);
        }
        return false;
    }

    public int hashCode() {
        return this.ident_.hashCode();
    }

    public <T> T match(Function<BuiltIn, T> builtIn,
                       Function<ClassName, T> className) {
        return className.apply(this);
    }
}
