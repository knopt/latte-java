package Latte.Absyn; // Java Package generated by the BNF Converter.

import java.util.function.Function;

public class ClassDecl extends TopDef implements Positioned {

    @Override
    public String getPosition() {
        return "(" + line_num + ", " + col_num + ")";
    }


    public final ClassHeader classheader_;
    public final ListFieldDeclaration listfielddeclaration_;
    public int line_num, col_num, offset;

    public ClassDecl(ClassHeader p1, ListFieldDeclaration p2) {
        classheader_ = p1;
        listfielddeclaration_ = p2;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof ClassDecl) {
            ClassDecl x = (ClassDecl) o;
            return this.classheader_.equals(x.classheader_) && this.listfielddeclaration_.equals(x.listfielddeclaration_);
        }
        return false;
    }

    public int hashCode() {
        return 37 * (this.classheader_.hashCode()) + this.listfielddeclaration_.hashCode();
    }

    public <T> T match(Function<FnDef, T> fnDef,
                       Function<ClassDecl, T> classDecl) {
        return classDecl.apply(this);
    }
}
