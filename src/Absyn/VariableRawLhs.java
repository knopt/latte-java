package src.Absyn; // Java Package generated by the BNF Converter.

import java.util.function.Function;

public class VariableRawLhs extends Lhs implements Positioned {


    @Override
    public String getPosition() {
        return "(" + line_num + ", " + col_num + ")";
    }

    public final String ident_;
    public int line_num, col_num, offset;

    public VariableRawLhs(String p1) {
        ident_ = p1;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof src.Absyn.VariableRawLhs) {
            src.Absyn.VariableRawLhs x = (src.Absyn.VariableRawLhs) o;
            return this.ident_.equals(x.ident_);
        }
        return false;
    }

    public int hashCode() {
        return this.ident_.hashCode();
    }

    public <T> T match(Function<VariableRawLhs, T> variableRawLhs,
                       Function<ArrElemLhs, T> arrElemLhs,
                       Function<FieldLhs, T> fieldLhs) {
        return variableRawLhs.apply(this);
    }

}
