package Absyn; // Java Package generated by the BNF Converter.

import java.util.function.Function;

public class Dmth extends FieldDeclaration {
    public final Modifier modifier_;
    public final Type type_;
    public final String ident_;
    public final ListArg listarg_;
    public final MethodBody methodbody_;
    public int line_num, col_num, offset;

    public Dmth(Modifier p1, Type p2, String p3, ListArg p4, MethodBody p5) {
        modifier_ = p1;
        type_ = p2;
        ident_ = p3;
        listarg_ = p4;
        methodbody_ = p5;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Dmth) {
            Dmth x = (Dmth) o;
            return this.modifier_.equals(x.modifier_) && this.type_.equals(x.type_) && this.ident_.equals(x.ident_) && this.listarg_.equals(x.listarg_) && this.methodbody_.equals(x.methodbody_);
        }
        return false;
    }

    public int hashCode() {
        return 37 * (37 * (37 * (37 * (this.modifier_.hashCode()) + this.type_.hashCode()) + this.ident_.hashCode()) + this.listarg_.hashCode()) + this.methodbody_.hashCode();
    }


    public <T> T match(Function<Dvar, T> dVar,
                       Function<Dmth, T> dMth) {
        return dMth.apply(this);
    }
}
