package src.Absyn; // Java Package generated by the BNF Converter.

import java.util.function.Function;

public abstract class Stmt implements java.io.Serializable {

    public abstract <T> T match(Function<Empty, T> empty,
                                Function<BStmt, T> bStmt,
                                Function<Decl, T> decl,
                                Function<Ass, T> ass,
                                Function<Incr, T> incr,
                                Function<Decr, T> decr,
                                Function<Ret, T> ret,
                                Function<VRet, T> vRet,
                                Function<Cond, T> cond,
                                Function<CondElse, T> condElse,
                                Function<While, T> sWhile,
                                Function<SExp, T> sExp,
                                Function<ForArr, T> forArr);

}