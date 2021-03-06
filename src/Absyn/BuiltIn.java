package src.Absyn; // Java Package generated by the BNF Converter.

import java.util.function.Function;

public class BuiltIn extends TypeName implements Positioned {

    @Override
    public String getPosition() {
        return "(" + line_num + ", " + col_num + ")";
    }


    public final BasicType basictype_;
    public int line_num, col_num, offset;

    public BuiltIn(BasicType p1) {
        basictype_ = p1;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof BuiltIn) {
            BuiltIn x = (BuiltIn) o;
            return this.basictype_.equals(x.basictype_);
        }
        return false;
    }

    public int hashCode() {
        return this.basictype_.hashCode();
    }

    public <T> T match(Function<BuiltIn, T> builtIn,
                       Function<ClassName, T> className) {
        return builtIn.apply(this);
    }
}
