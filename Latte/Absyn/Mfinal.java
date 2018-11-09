package Latte.Absyn; // Java Package generated by the BNF Converter.

import java.util.function.Function;

public class Mfinal extends Modifier {
    public int line_num, col_num, offset;

    public Mfinal() {
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Mfinal) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return 37;
    }

    public <T> T match(Function<Mfinal, T> mFinal,
                       Function<Mpublic, T> mPublic,
                       Function<Mprivate, T> mPrivate,
                       Function<Mstatic, T> mStatic,
                       Function<MEmpty, T> mEmpty) {
        return mFinal.apply(this);
    }
}
