package Absyn; // Java Package generated by the BNF Converter.

import java.util.function.Function;

public class Dvar extends FieldDeclaration {
    public final Modifier modifier_;
    public final Type type_;
    public final ListItem listitem_;
    public int line_num, col_num, offset;

    public Dvar(Modifier p1, Type p2, ListItem p3) {
        modifier_ = p1;
        type_ = p2;
        listitem_ = p3;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Dvar) {
            Dvar x = (Dvar) o;
            return this.modifier_.equals(x.modifier_) && this.type_.equals(x.type_) && this.listitem_.equals(x.listitem_);
        }
        return false;
    }

    public int hashCode() {
        return 37 * (37 * (this.modifier_.hashCode()) + this.type_.hashCode()) + this.listitem_.hashCode();
    }

    public <T> T match(Function<Dvar, T> dVar,
                       Function<Dmth, T> dMth) {
        return dVar.apply(this);
    }

}
