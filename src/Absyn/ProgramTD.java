package src.Absyn; // Java Package generated by the BNF Converter.

import java.util.function.Function;

public class ProgramTD extends Program {
    public final ListTopDef listtopdef_;
    public int line_num, col_num, offset;

    public ProgramTD(ListTopDef p1) {
        listtopdef_ = p1;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof ProgramTD) {
            ProgramTD x = (ProgramTD) o;
            return this.listtopdef_.equals(x.listtopdef_);
        }
        return false;
    }

    public int hashCode() {
        return this.listtopdef_.hashCode();
    }


    public <T> T match(Function<ProgramTD, T> td) {

        return td.apply(this);
    }
}