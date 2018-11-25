package Latte.Absyn; // Java Package generated by the BNF Converter.

public abstract class Implements implements java.io.Serializable {
  public abstract <R,A> R accept(Implements.Visitor<R,A> v, A arg);
  public interface Visitor <R,A> {
    public R visit(Latte.Absyn.InterImpl p, A arg);
    public R visit(Latte.Absyn.EImpl p, A arg);

  }

}
