package src.Absyn; // Java Package generated by the BNF Converter.

import java.util.function.Function;

public class InterDec extends ClassHeader implements Positioned {

    @Override
    public String getPosition() {
        return "(" + line_num + ", " + col_num + ")";
    }


    public final String ident_;
    public int line_num, col_num, offset;

    public InterDec(String p1) {
        ident_ = p1;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof InterDec) {
            InterDec x = (InterDec) o;
            return this.ident_.equals(x.ident_);
        }
        return false;
    }

    public int hashCode() {
        return this.ident_.hashCode();
    }

    public <T> T match(Function<ClassDec, T> classDec,
                       Function<InterDec, T> interDec) {
        return interDec.apply(this);
    }
}
