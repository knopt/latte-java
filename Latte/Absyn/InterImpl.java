package Latte.Absyn; // Java Package generated by the BNF Converter.

public class InterImpl  extends Implements {
  public final String ident_;
  public int line_num, col_num, offset;
  public InterImpl(String p1) { ident_ = p1; }

  public <R,A> R accept(Latte.Absyn.Implements.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o instanceof Latte.Absyn.InterImpl) {
      Latte.Absyn.InterImpl x = (Latte.Absyn.InterImpl)o;
      return this.ident_.equals(x.ident_);
    }
    return false;
  }

  public int hashCode() {
    return this.ident_.hashCode();
  }


}
