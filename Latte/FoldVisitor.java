//package Latte;
//
//import Latte.Absyn.*;
//
///** BNFC-Generated Fold Visitor */
//public abstract class FoldVisitor<R,A> implements Program.Visitor<R, A>, TopDef.Visitor<R, A>, Arg.Visitor<R, A>, ClassHeader.Visitor<R, A>, FieldDeclaration.Visitor<R, A>, MethodBody.Visitor<R, A>, Modifier.Visitor<R, A>, Block.Visitor<R, A>, Stmt.Visitor<R, A>, Item.Visitor<R, A>, BasicType.Visitor<R, A>, TypeName.Visitor<R, A>, Type.Visitor<R, A>, EmptyBracket.Visitor<R, A>, SizeBracket.Visitor<R, A>, Expr.Visitor<R, A>, AddOp.Visitor<R, A>, MulOp.Visitor<R, A>, RelOp.Visitor<R, A> {
//    public abstract R leaf(A arg);
//    public abstract R combine(R x, R y, A arg);
//
///* Program */
//    public R visit(Latte.Absyn.ProgramTD p, A arg) {
//      R r = leaf(arg);
//      for (TopDef x : p.listtopdef_)
//      {
//        r = combine(x.accept(this, arg), r, arg);
//      }
//      return r;
//    }
//
///* TopDef */
//    public R visit(Latte.Absyn.FnDef p, A arg) {
//      R r = leaf(arg);
//      r = combine(p.type_.accept(this, arg), r, arg);
//      for (Arg x : p.listarg_)
//      {
//        r = combine(x.accept(this, arg), r, arg);
//      }
//      r = combine(p.block_.accept(this, arg), r, arg);
//      return r;
//    }
//    public R visit(Latte.Absyn.ClassDecl p, A arg) {
//      R r = leaf(arg);
//      r = combine(p.classheader_.accept(this, arg), r, arg);
//      for (FieldDeclaration x : p.listfielddeclaration_)
//      {
//        r = combine(x.accept(this, arg), r, arg);
//      }
//      return r;
//    }
//
///* Arg */
//    public R visit(Latte.Absyn.ArgTI p, A arg) {
//      R r = leaf(arg);
//      r = combine(p.type_.accept(this, arg), r, arg);
//      return r;
//    }
//
///* ClassHeader */
//    public R visit(Latte.Absyn.ClassDec p, A arg) {
//      R r = leaf(arg);
//      return r;
//    }
//    public R visit(Latte.Absyn.InterDec p, A arg) {
//      R r = leaf(arg);
//      return r;
//    }
//
///* FieldDeclaration */
//    public R visit(Latte.Absyn.Dvar p, A arg) {
//      R r = leaf(arg);
//      r = combine(p.modifier_.accept(this, arg), r, arg);
//      r = combine(p.type_.accept(this, arg), r, arg);
//      for (Item x : p.listitem_)
//      {
//        r = combine(x.accept(this, arg), r, arg);
//      }
//      return r;
//    }
//    public R visit(Latte.Absyn.Dmth p, A arg) {
//      R r = leaf(arg);
//      r = combine(p.modifier_.accept(this, arg), r, arg);
//      r = combine(p.type_.accept(this, arg), r, arg);
//      for (Arg x : p.listarg_)
//      {
//        r = combine(x.accept(this, arg), r, arg);
//      }
//      r = combine(p.methodbody_.accept(this, arg), r, arg);
//      return r;
//    }
//
///* MethodBody */
//    public R visit(Latte.Absyn.EmptyMBody p, A arg) {
//      R r = leaf(arg);
//      return r;
//    }
//    public R visit(Latte.Absyn.MBody p, A arg) {
//      R r = leaf(arg);
//      r = combine(p.block_.accept(this, arg), r, arg);
//      return r;
//    }
//
///* Modifier */
//    public R visit(Latte.Absyn.Mfinal p, A arg) {
//      R r = leaf(arg);
//      return r;
//    }
//    public R visit(Latte.Absyn.Mpublic p, A arg) {
//      R r = leaf(arg);
//      return r;
//    }
//    public R visit(Latte.Absyn.Mprivate p, A arg) {
//      R r = leaf(arg);
//      return r;
//    }
//    public R visit(Latte.Absyn.Mstatic p, A arg) {
//      R r = leaf(arg);
//      return r;
//    }
//    public R visit(Latte.Absyn.MEmpty p, A arg) {
//      R r = leaf(arg);
//      return r;
//    }
//
///* Block */
//    public R visit(Latte.Absyn.BlockS p, A arg) {
//      R r = leaf(arg);
//      for (Stmt x : p.liststmt_)
//      {
//        r = combine(x.accept(this, arg), r, arg);
//      }
//      return r;
//    }
//
///* Stmt */
//    public R visit(Latte.Absyn.Empty p, A arg) {
//      R r = leaf(arg);
//      return r;
//    }
//    public R visit(Latte.Absyn.BStmt p, A arg) {
//      R r = leaf(arg);
//      r = combine(p.block_.accept(this, arg), r, arg);
//      return r;
//    }
//    public R visit(Latte.Absyn.Decl p, A arg) {
//      R r = leaf(arg);
//      r = combine(p.type_.accept(this, arg), r, arg);
//      for (Item x : p.listitem_)
//      {
//        r = combine(x.accept(this, arg), r, arg);
//      }
//      return r;
//    }
//    public R visit(Latte.Absyn.Ass p, A arg) {
//      R r = leaf(arg);
//      r = combine(p.expr_.accept(this, arg), r, arg);
//      return r;
//    }
//    public R visit(Latte.Absyn.Incr p, A arg) {
//      R r = leaf(arg);
//      return r;
//    }
//    public R visit(Latte.Absyn.Decr p, A arg) {
//      R r = leaf(arg);
//      return r;
//    }
//    public R visit(Latte.Absyn.Ret p, A arg) {
//      R r = leaf(arg);
//      r = combine(p.expr_.accept(this, arg), r, arg);
//      return r;
//    }
//    public R visit(Latte.Absyn.VRet p, A arg) {
//      R r = leaf(arg);
//      return r;
//    }
//    public R visit(Latte.Absyn.Cond p, A arg) {
//      R r = leaf(arg);
//      r = combine(p.expr_.accept(this, arg), r, arg);
//      r = combine(p.stmt_.accept(this, arg), r, arg);
//      return r;
//    }
//    public R visit(Latte.Absyn.CondElse p, A arg) {
//      R r = leaf(arg);
//      r = combine(p.expr_.accept(this, arg), r, arg);
//      r = combine(p.stmt_1.accept(this, arg), r, arg);
//      r = combine(p.stmt_2.accept(this, arg), r, arg);
//      return r;
//    }
//    public R visit(Latte.Absyn.While p, A arg) {
//      R r = leaf(arg);
//      r = combine(p.expr_.accept(this, arg), r, arg);
//      r = combine(p.stmt_.accept(this, arg), r, arg);
//      return r;
//    }
//    public R visit(Latte.Absyn.SExp p, A arg) {
//      R r = leaf(arg);
//      r = combine(p.expr_.accept(this, arg), r, arg);
//      return r;
//    }
//
///* Item */
//    public R visit(Latte.Absyn.NoInit p, A arg) {
//      R r = leaf(arg);
//      return r;
//    }
//    public R visit(Latte.Absyn.Init p, A arg) {
//      R r = leaf(arg);
//      r = combine(p.expr_.accept(this, arg), r, arg);
//      return r;
//    }
//
///* BasicType */
//    public R visit(Latte.Absyn.Int p, A arg) {
//      R r = leaf(arg);
//      return r;
//    }
//    public R visit(Latte.Absyn.Str p, A arg) {
//      R r = leaf(arg);
//      return r;
//    }
//    public R visit(Latte.Absyn.Bool p, A arg) {
//      R r = leaf(arg);
//      return r;
//    }
//    public R visit(Latte.Absyn.Void p, A arg) {
//      R r = leaf(arg);
//      return r;
//    }
//
///* TypeName */
//    public R visit(Latte.Absyn.BuiltIn p, A arg) {
//      R r = leaf(arg);
//      r = combine(p.basictype_.accept(this, arg), r, arg);
//      return r;
//    }
//    public R visit(Latte.Absyn.ClassName p, A arg) {
//      R r = leaf(arg);
//      return r;
//    }
//
///* Type */
//    public R visit(Latte.Absyn.ArrayType p, A arg) {
//      R r = leaf(arg);
//      r = combine(p.typename_.accept(this, arg), r, arg);
//      for (EmptyBracket x : p.listemptybracket_)
//      {
//        r = combine(x.accept(this, arg), r, arg);
//      }
//      return r;
//    }
//    public R visit(Latte.Absyn.TypeNameS p, A arg) {
//      R r = leaf(arg);
//      r = combine(p.typename_.accept(this, arg), r, arg);
//      return r;
//    }
//
///* EmptyBracket */
//    public R visit(Latte.Absyn.EBracket p, A arg) {
//      R r = leaf(arg);
//      return r;
//    }
//
///* SizeBracket */
//    public R visit(Latte.Absyn.SBracket p, A arg) {
//      R r = leaf(arg);
//      r = combine(p.expr_.accept(this, arg), r, arg);
//      return r;
//    }
//
///* Expr */
//    public R visit(Latte.Absyn.EVar p, A arg) {
//      R r = leaf(arg);
//      return r;
//    }
//    public R visit(Latte.Absyn.ELitInt p, A arg) {
//      R r = leaf(arg);
//      return r;
//    }
//    public R visit(Latte.Absyn.ELitTrue p, A arg) {
//      R r = leaf(arg);
//      return r;
//    }
//    public R visit(Latte.Absyn.ELitFalse p, A arg) {
//      R r = leaf(arg);
//      return r;
//    }
//    public R visit(Latte.Absyn.EThis p, A arg) {
//      R r = leaf(arg);
//      return r;
//    }
//    public R visit(Latte.Absyn.ENull p, A arg) {
//      R r = leaf(arg);
//      return r;
//    }
//    public R visit(Latte.Absyn.EApp p, A arg) {
//      R r = leaf(arg);
//      for (Expr x : p.listexpr_)
//      {
//        r = combine(x.accept(this, arg), r, arg);
//      }
//      return r;
//    }
//    public R visit(Latte.Absyn.EString p, A arg) {
//      R r = leaf(arg);
//      return r;
//    }
//    public R visit(Latte.Absyn.EConstr p, A arg) {
//      R r = leaf(arg);
//      return r;
//    }
//    public R visit(Latte.Absyn.EArrConstr p, A arg) {
//      R r = leaf(arg);
//      for (SizeBracket x : p.listsizebracket_)
//      {
//        r = combine(x.accept(this, arg), r, arg);
//      }
//      return r;
//    }
//    public R visit(Latte.Absyn.ENDArrAcc p, A arg) {
//      R r = leaf(arg);
//      r = combine(p.expr_.accept(this, arg), r, arg);
//      for (SizeBracket x : p.listsizebracket_)
//      {
//        r = combine(x.accept(this, arg), r, arg);
//      }
//      return r;
//    }
//    public R visit(Latte.Absyn.FieldAcc p, A arg) {
//      R r = leaf(arg);
//      r = combine(p.expr_.accept(this, arg), r, arg);
//      return r;
//    }
//    public R visit(Latte.Absyn.MethodCall p, A arg) {
//      R r = leaf(arg);
//      r = combine(p.expr_.accept(this, arg), r, arg);
//      for (Expr x : p.listexpr_)
//      {
//        r = combine(x.accept(this, arg), r, arg);
//      }
//      return r;
//    }
//    public R visit(Latte.Absyn.Neg p, A arg) {
//      R r = leaf(arg);
//      r = combine(p.expr_.accept(this, arg), r, arg);
//      return r;
//    }
//    public R visit(Latte.Absyn.Not p, A arg) {
//      R r = leaf(arg);
//      r = combine(p.expr_.accept(this, arg), r, arg);
//      return r;
//    }
//    public R visit(Latte.Absyn.EMul p, A arg) {
//      R r = leaf(arg);
//      r = combine(p.expr_1.accept(this, arg), r, arg);
//      r = combine(p.mulop_.accept(this, arg), r, arg);
//      r = combine(p.expr_2.accept(this, arg), r, arg);
//      return r;
//    }
//    public R visit(Latte.Absyn.EAdd p, A arg) {
//      R r = leaf(arg);
//      r = combine(p.expr_1.accept(this, arg), r, arg);
//      r = combine(p.addop_.accept(this, arg), r, arg);
//      r = combine(p.expr_2.accept(this, arg), r, arg);
//      return r;
//    }
//    public R visit(Latte.Absyn.ERel p, A arg) {
//      R r = leaf(arg);
//      r = combine(p.expr_1.accept(this, arg), r, arg);
//      r = combine(p.relop_.accept(this, arg), r, arg);
//      r = combine(p.expr_2.accept(this, arg), r, arg);
//      return r;
//    }
//    public R visit(Latte.Absyn.EAnd p, A arg) {
//      R r = leaf(arg);
//      r = combine(p.expr_1.accept(this, arg), r, arg);
//      r = combine(p.expr_2.accept(this, arg), r, arg);
//      return r;
//    }
//    public R visit(Latte.Absyn.EOr p, A arg) {
//      R r = leaf(arg);
//      r = combine(p.expr_1.accept(this, arg), r, arg);
//      r = combine(p.expr_2.accept(this, arg), r, arg);
//      return r;
//    }
//
///* AddOp */
//    public R visit(Latte.Absyn.Plus p, A arg) {
//      R r = leaf(arg);
//      return r;
//    }
//    public R visit(Latte.Absyn.Minus p, A arg) {
//      R r = leaf(arg);
//      return r;
//    }
//
///* MulOp */
//    public R visit(Latte.Absyn.Times p, A arg) {
//      R r = leaf(arg);
//      return r;
//    }
//    public R visit(Latte.Absyn.Div p, A arg) {
//      R r = leaf(arg);
//      return r;
//    }
//    public R visit(Latte.Absyn.Mod p, A arg) {
//      R r = leaf(arg);
//      return r;
//    }
//
///* RelOp */
//    public R visit(Latte.Absyn.LTH p, A arg) {
//      R r = leaf(arg);
//      return r;
//    }
//    public R visit(Latte.Absyn.LE p, A arg) {
//      R r = leaf(arg);
//      return r;
//    }
//    public R visit(Latte.Absyn.GTH p, A arg) {
//      R r = leaf(arg);
//      return r;
//    }
//    public R visit(Latte.Absyn.GE p, A arg) {
//      R r = leaf(arg);
//      return r;
//    }
//    public R visit(Latte.Absyn.EQU p, A arg) {
//      R r = leaf(arg);
//      return r;
//    }
//    public R visit(Latte.Absyn.NE p, A arg) {
//      R r = leaf(arg);
//      return r;
//    }
//
//
//}
