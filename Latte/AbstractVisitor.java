package Latte;
import Latte.Absyn.*;
/** BNFC-Generated Abstract Visitor */
public class AbstractVisitor<R,A> implements AllVisitor<R,A> {
/* Program */
    public R visit(Latte.Absyn.ProgramTD p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(Latte.Absyn.Program p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* TopDef */
    public R visit(Latte.Absyn.FnDef p, A arg) { return visitDefault(p, arg); }
    public R visit(Latte.Absyn.ClassDecl p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(Latte.Absyn.TopDef p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* Arg */
    public R visit(Latte.Absyn.ArgTI p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(Latte.Absyn.Arg p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* ClassHeader */
    public R visit(Latte.Absyn.ClassDec p, A arg) { return visitDefault(p, arg); }
    public R visit(Latte.Absyn.InterDec p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(Latte.Absyn.ClassHeader p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* FieldDeclaration */
    public R visit(Latte.Absyn.Dvar p, A arg) { return visitDefault(p, arg); }
    public R visit(Latte.Absyn.Dmth p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(Latte.Absyn.FieldDeclaration p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* MethodBody */
    public R visit(Latte.Absyn.EmptyMBody p, A arg) { return visitDefault(p, arg); }
    public R visit(Latte.Absyn.MBody p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(Latte.Absyn.MethodBody p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* Modifier */
    public R visit(Latte.Absyn.Mfinal p, A arg) { return visitDefault(p, arg); }
    public R visit(Latte.Absyn.Mpublic p, A arg) { return visitDefault(p, arg); }
    public R visit(Latte.Absyn.Mprivate p, A arg) { return visitDefault(p, arg); }
    public R visit(Latte.Absyn.Mstatic p, A arg) { return visitDefault(p, arg); }
    public R visit(Latte.Absyn.MEmpty p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(Latte.Absyn.Modifier p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* Block */
    public R visit(Latte.Absyn.BlockS p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(Latte.Absyn.Block p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* Stmt */
    public R visit(Latte.Absyn.Empty p, A arg) { return visitDefault(p, arg); }
    public R visit(Latte.Absyn.BStmt p, A arg) { return visitDefault(p, arg); }
    public R visit(Latte.Absyn.Decl p, A arg) { return visitDefault(p, arg); }
    public R visit(Latte.Absyn.Ass p, A arg) { return visitDefault(p, arg); }
    public R visit(Latte.Absyn.Incr p, A arg) { return visitDefault(p, arg); }
    public R visit(Latte.Absyn.Decr p, A arg) { return visitDefault(p, arg); }
    public R visit(Latte.Absyn.Ret p, A arg) { return visitDefault(p, arg); }
    public R visit(Latte.Absyn.VRet p, A arg) { return visitDefault(p, arg); }
    public R visit(Latte.Absyn.Cond p, A arg) { return visitDefault(p, arg); }
    public R visit(Latte.Absyn.CondElse p, A arg) { return visitDefault(p, arg); }
    public R visit(Latte.Absyn.While p, A arg) { return visitDefault(p, arg); }
    public R visit(Latte.Absyn.SExp p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(Latte.Absyn.Stmt p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* Item */
    public R visit(Latte.Absyn.NoInit p, A arg) { return visitDefault(p, arg); }
    public R visit(Latte.Absyn.Init p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(Latte.Absyn.Item p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* BasicType */
    public R visit(Latte.Absyn.Int p, A arg) { return visitDefault(p, arg); }
    public R visit(Latte.Absyn.Str p, A arg) { return visitDefault(p, arg); }
    public R visit(Latte.Absyn.Bool p, A arg) { return visitDefault(p, arg); }
    public R visit(Latte.Absyn.Void p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(Latte.Absyn.BasicType p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* TypeName */
    public R visit(Latte.Absyn.BuiltIn p, A arg) { return visitDefault(p, arg); }
    public R visit(Latte.Absyn.ClassName p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(Latte.Absyn.TypeName p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* Type */
    public R visit(Latte.Absyn.ArrayType p, A arg) { return visitDefault(p, arg); }
    public R visit(Latte.Absyn.TypeNameS p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(Latte.Absyn.Type p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* EmptyBracket */
    public R visit(Latte.Absyn.EBracket p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(Latte.Absyn.EmptyBracket p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* SizeBracket */
    public R visit(Latte.Absyn.SBracket p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(Latte.Absyn.SizeBracket p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* Expr */
    public R visit(Latte.Absyn.EVar p, A arg) { return visitDefault(p, arg); }
    public R visit(Latte.Absyn.ELitInt p, A arg) { return visitDefault(p, arg); }
    public R visit(Latte.Absyn.ELitTrue p, A arg) { return visitDefault(p, arg); }
    public R visit(Latte.Absyn.ELitFalse p, A arg) { return visitDefault(p, arg); }
    public R visit(Latte.Absyn.EThis p, A arg) { return visitDefault(p, arg); }
    public R visit(Latte.Absyn.ENull p, A arg) { return visitDefault(p, arg); }
    public R visit(Latte.Absyn.EApp p, A arg) { return visitDefault(p, arg); }
    public R visit(Latte.Absyn.EString p, A arg) { return visitDefault(p, arg); }
    public R visit(Latte.Absyn.EConstr p, A arg) { return visitDefault(p, arg); }

    public R visit(Latte.Absyn.EArrConstr p, A arg) { return visitDefault(p, arg); }
    public R visit(Latte.Absyn.ENDArrAcc p, A arg) { return visitDefault(p, arg); }

    public R visit(Latte.Absyn.FieldAcc p, A arg) { return visitDefault(p, arg); }
    public R visit(Latte.Absyn.MethodCall p, A arg) { return visitDefault(p, arg); }
    public R visit(Latte.Absyn.Neg p, A arg) { return visitDefault(p, arg); }
    public R visit(Latte.Absyn.Not p, A arg) { return visitDefault(p, arg); }

    public R visit(Latte.Absyn.EMul p, A arg) { return visitDefault(p, arg); }

    public R visit(Latte.Absyn.EAdd p, A arg) { return visitDefault(p, arg); }

    public R visit(Latte.Absyn.ERel p, A arg) { return visitDefault(p, arg); }

    public R visit(Latte.Absyn.EAnd p, A arg) { return visitDefault(p, arg); }

    public R visit(Latte.Absyn.EOr p, A arg) { return visitDefault(p, arg); }

    public R visitDefault(Latte.Absyn.Expr p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* AddOp */
    public R visit(Latte.Absyn.Plus p, A arg) { return visitDefault(p, arg); }
    public R visit(Latte.Absyn.Minus p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(Latte.Absyn.AddOp p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* MulOp */
    public R visit(Latte.Absyn.Times p, A arg) { return visitDefault(p, arg); }
    public R visit(Latte.Absyn.Div p, A arg) { return visitDefault(p, arg); }
    public R visit(Latte.Absyn.Mod p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(Latte.Absyn.MulOp p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* RelOp */
    public R visit(Latte.Absyn.LTH p, A arg) { return visitDefault(p, arg); }
    public R visit(Latte.Absyn.LE p, A arg) { return visitDefault(p, arg); }
    public R visit(Latte.Absyn.GTH p, A arg) { return visitDefault(p, arg); }
    public R visit(Latte.Absyn.GE p, A arg) { return visitDefault(p, arg); }
    public R visit(Latte.Absyn.EQU p, A arg) { return visitDefault(p, arg); }
    public R visit(Latte.Absyn.NE p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(Latte.Absyn.RelOp p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }

}
