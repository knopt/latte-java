package src.Absyn; // Java Package generated by the BNF Converter.

import java.util.function.Function;

public class FnDef extends TopDef implements Positioned {

    @Override
    public String getPosition() {
        return "(" + line_num + ", " + col_num + ")";
    }


    public final Type type_;
    public final String ident_;
    public final ListArg listarg_;
    public final Block block_;
    public int line_num, col_num, offset;

    public FnDef(Type p1, String p2, ListArg p3, Block p4) {
        type_ = p1;
        ident_ = p2;
        listarg_ = p3;
        block_ = p4;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof FnDef) {
            FnDef x = (FnDef) o;
            return this.type_.equals(x.type_) && this.ident_.equals(x.ident_) && this.listarg_.equals(x.listarg_) && this.block_.equals(x.block_);
        }
        return false;
    }

    public int hashCode() {
        return 37 * (37 * (37 * (this.type_.hashCode()) + this.ident_.hashCode()) + this.listarg_.hashCode()) + this.block_.hashCode();
    }


    public <T> T match(Function<FnDef, T> fnDef,
                       Function<ClassDecl, T> classDecl) {
        return fnDef.apply(this);
    }

}