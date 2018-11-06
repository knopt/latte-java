package Latte;
import Latte.Absyn.*;
/** BNFC-Generated Composition Visitor
*/

public class ComposVisitor<A> implements
  Latte.Absyn.Program.Visitor<Latte.Absyn.Program,A>,
  Latte.Absyn.TopDef.Visitor<Latte.Absyn.TopDef,A>,
  Latte.Absyn.Arg.Visitor<Latte.Absyn.Arg,A>,
  Latte.Absyn.ClassHeader.Visitor<Latte.Absyn.ClassHeader,A>,
  Latte.Absyn.FieldDeclaration.Visitor<Latte.Absyn.FieldDeclaration,A>,
  Latte.Absyn.MethodBody.Visitor<Latte.Absyn.MethodBody,A>,
  Latte.Absyn.Modifier.Visitor<Latte.Absyn.Modifier,A>,
  Latte.Absyn.Block.Visitor<Latte.Absyn.Block,A>,
  Latte.Absyn.Stmt.Visitor<Latte.Absyn.Stmt,A>,
  Latte.Absyn.Item.Visitor<Latte.Absyn.Item,A>,
  Latte.Absyn.BasicType.Visitor<Latte.Absyn.BasicType,A>,
  Latte.Absyn.TypeName.Visitor<Latte.Absyn.TypeName,A>,
  Latte.Absyn.Type.Visitor<Latte.Absyn.Type,A>,
  Latte.Absyn.EmptyBracket.Visitor<Latte.Absyn.EmptyBracket,A>,
  Latte.Absyn.SizeBracket.Visitor<Latte.Absyn.SizeBracket,A>,
  Latte.Absyn.Expr.Visitor<Latte.Absyn.Expr,A>,
  Latte.Absyn.AddOp.Visitor<Latte.Absyn.AddOp,A>,
  Latte.Absyn.MulOp.Visitor<Latte.Absyn.MulOp,A>,
  Latte.Absyn.RelOp.Visitor<Latte.Absyn.RelOp,A>
{
/* Program */
    public Program visit(Latte.Absyn.ProgramTD p, A arg)
    {
      ListTopDef listtopdef_ = new ListTopDef();
      for (TopDef x : p.listtopdef_)
      {
        listtopdef_.add(x.accept(this,arg));
      }
      return new Latte.Absyn.ProgramTD(listtopdef_);
    }
/* TopDef */
    public TopDef visit(Latte.Absyn.FnDef p, A arg)
    {
      Type type_ = p.type_.accept(this, arg);
      String ident_ = p.ident_;
      ListArg listarg_ = new ListArg();
      for (Arg x : p.listarg_)
      {
        listarg_.add(x.accept(this,arg));
      }
      Block block_ = p.block_.accept(this, arg);
      return new Latte.Absyn.FnDef(type_, ident_, listarg_, block_);
    }    public TopDef visit(Latte.Absyn.ClassDecl p, A arg)
    {
      ClassHeader classheader_ = p.classheader_.accept(this, arg);
      ListFieldDeclaration listfielddeclaration_ = new ListFieldDeclaration();
      for (FieldDeclaration x : p.listfielddeclaration_)
      {
        listfielddeclaration_.add(x.accept(this,arg));
      }
      return new Latte.Absyn.ClassDecl(classheader_, listfielddeclaration_);
    }
/* Arg */
    public Arg visit(Latte.Absyn.ArgTI p, A arg)
    {
      Type type_ = p.type_.accept(this, arg);
      String ident_ = p.ident_;
      return new Latte.Absyn.ArgTI(type_, ident_);
    }
/* ClassHeader */
    public ClassHeader visit(Latte.Absyn.ClassDec p, A arg)
    {
      String ident_ = p.ident_;
      return new Latte.Absyn.ClassDec(ident_);
    }    public ClassHeader visit(Latte.Absyn.InterDec p, A arg)
    {
      String ident_ = p.ident_;
      return new Latte.Absyn.InterDec(ident_);
    }
/* FieldDeclaration */
    public FieldDeclaration visit(Latte.Absyn.Dvar p, A arg)
    {
      Modifier modifier_ = p.modifier_.accept(this, arg);
      Type type_ = p.type_.accept(this, arg);
      ListItem listitem_ = new ListItem();
      for (Item x : p.listitem_)
      {
        listitem_.add(x.accept(this,arg));
      }
      return new Latte.Absyn.Dvar(modifier_, type_, listitem_);
    }    public FieldDeclaration visit(Latte.Absyn.Dmth p, A arg)
    {
      Modifier modifier_ = p.modifier_.accept(this, arg);
      Type type_ = p.type_.accept(this, arg);
      String ident_ = p.ident_;
      ListArg listarg_ = new ListArg();
      for (Arg x : p.listarg_)
      {
        listarg_.add(x.accept(this,arg));
      }
      MethodBody methodbody_ = p.methodbody_.accept(this, arg);
      return new Latte.Absyn.Dmth(modifier_, type_, ident_, listarg_, methodbody_);
    }
/* MethodBody */
    public MethodBody visit(Latte.Absyn.EmptyMBody p, A arg)
    {
      return new Latte.Absyn.EmptyMBody();
    }    public MethodBody visit(Latte.Absyn.MBody p, A arg)
    {
      Block block_ = p.block_.accept(this, arg);
      return new Latte.Absyn.MBody(block_);
    }
/* Modifier */
    public Modifier visit(Latte.Absyn.Mfinal p, A arg)
    {
      return new Latte.Absyn.Mfinal();
    }    public Modifier visit(Latte.Absyn.Mpublic p, A arg)
    {
      return new Latte.Absyn.Mpublic();
    }    public Modifier visit(Latte.Absyn.Mprivate p, A arg)
    {
      return new Latte.Absyn.Mprivate();
    }    public Modifier visit(Latte.Absyn.Mstatic p, A arg)
    {
      return new Latte.Absyn.Mstatic();
    }    public Modifier visit(Latte.Absyn.MEmpty p, A arg)
    {
      return new Latte.Absyn.MEmpty();
    }
/* Block */
    public Block visit(Latte.Absyn.BlockS p, A arg)
    {
      ListStmt liststmt_ = new ListStmt();
      for (Stmt x : p.liststmt_)
      {
        liststmt_.add(x.accept(this,arg));
      }
      return new Latte.Absyn.BlockS(liststmt_);
    }
/* Stmt */
    public Stmt visit(Latte.Absyn.Empty p, A arg)
    {
      return new Latte.Absyn.Empty();
    }    public Stmt visit(Latte.Absyn.BStmt p, A arg)
    {
      Block block_ = p.block_.accept(this, arg);
      return new Latte.Absyn.BStmt(block_);
    }    public Stmt visit(Latte.Absyn.Decl p, A arg)
    {
      Type type_ = p.type_.accept(this, arg);
      ListItem listitem_ = new ListItem();
      for (Item x : p.listitem_)
      {
        listitem_.add(x.accept(this,arg));
      }
      return new Latte.Absyn.Decl(type_, listitem_);
    }    public Stmt visit(Latte.Absyn.Ass p, A arg)
    {
      String ident_ = p.ident_;
      Expr expr_ = p.expr_.accept(this, arg);
      return new Latte.Absyn.Ass(ident_, expr_);
    }    public Stmt visit(Latte.Absyn.Incr p, A arg)
    {
      String ident_ = p.ident_;
      return new Latte.Absyn.Incr(ident_);
    }    public Stmt visit(Latte.Absyn.Decr p, A arg)
    {
      String ident_ = p.ident_;
      return new Latte.Absyn.Decr(ident_);
    }    public Stmt visit(Latte.Absyn.Ret p, A arg)
    {
      Expr expr_ = p.expr_.accept(this, arg);
      return new Latte.Absyn.Ret(expr_);
    }    public Stmt visit(Latte.Absyn.VRet p, A arg)
    {
      return new Latte.Absyn.VRet();
    }    public Stmt visit(Latte.Absyn.Cond p, A arg)
    {
      Expr expr_ = p.expr_.accept(this, arg);
      Stmt stmt_ = p.stmt_.accept(this, arg);
      return new Latte.Absyn.Cond(expr_, stmt_);
    }    public Stmt visit(Latte.Absyn.CondElse p, A arg)
    {
      Expr expr_ = p.expr_.accept(this, arg);
      Stmt stmt_1 = p.stmt_1.accept(this, arg);
      Stmt stmt_2 = p.stmt_2.accept(this, arg);
      return new Latte.Absyn.CondElse(expr_, stmt_1, stmt_2);
    }    public Stmt visit(Latte.Absyn.While p, A arg)
    {
      Expr expr_ = p.expr_.accept(this, arg);
      Stmt stmt_ = p.stmt_.accept(this, arg);
      return new Latte.Absyn.While(expr_, stmt_);
    }    public Stmt visit(Latte.Absyn.SExp p, A arg)
    {
      Expr expr_ = p.expr_.accept(this, arg);
      return new Latte.Absyn.SExp(expr_);
    }
/* Item */
    public Item visit(Latte.Absyn.NoInit p, A arg)
    {
      String ident_ = p.ident_;
      return new Latte.Absyn.NoInit(ident_);
    }    public Item visit(Latte.Absyn.Init p, A arg)
    {
      String ident_ = p.ident_;
      Expr expr_ = p.expr_.accept(this, arg);
      return new Latte.Absyn.Init(ident_, expr_);
    }
/* BasicType */
    public BasicType visit(Latte.Absyn.Int p, A arg)
    {
      return new Latte.Absyn.Int();
    }    public BasicType visit(Latte.Absyn.Str p, A arg)
    {
      return new Latte.Absyn.Str();
    }    public BasicType visit(Latte.Absyn.Bool p, A arg)
    {
      return new Latte.Absyn.Bool();
    }    public BasicType visit(Latte.Absyn.Void p, A arg)
    {
      return new Latte.Absyn.Void();
    }
/* TypeName */
    public TypeName visit(Latte.Absyn.BuiltIn p, A arg)
    {
      BasicType basictype_ = p.basictype_.accept(this, arg);
      return new Latte.Absyn.BuiltIn(basictype_);
    }    public TypeName visit(Latte.Absyn.ClassName p, A arg)
    {
      String ident_ = p.ident_;
      return new Latte.Absyn.ClassName(ident_);
    }
/* Type */
    public Type visit(Latte.Absyn.ArrayType p, A arg)
    {
      TypeName typename_ = p.typename_.accept(this, arg);
      ListEmptyBracket listemptybracket_ = new ListEmptyBracket();
      for (EmptyBracket x : p.listemptybracket_)
      {
        listemptybracket_.add(x.accept(this,arg));
      }
      return new Latte.Absyn.ArrayType(typename_, listemptybracket_);
    }    public Type visit(Latte.Absyn.TypeNameS p, A arg)
    {
      TypeName typename_ = p.typename_.accept(this, arg);
      return new Latte.Absyn.TypeNameS(typename_);
    }
/* EmptyBracket */
    public EmptyBracket visit(Latte.Absyn.EBracket p, A arg)
    {
      return new Latte.Absyn.EBracket();
    }
/* SizeBracket */
    public SizeBracket visit(Latte.Absyn.SBracket p, A arg)
    {
      Expr expr_ = p.expr_.accept(this, arg);
      return new Latte.Absyn.SBracket(expr_);
    }
/* Expr */
    public Expr visit(Latte.Absyn.EVar p, A arg)
    {
      String ident_ = p.ident_;
      return new Latte.Absyn.EVar(ident_);
    }    public Expr visit(Latte.Absyn.ELitInt p, A arg)
    {
      Integer integer_ = p.integer_;
      return new Latte.Absyn.ELitInt(integer_);
    }    public Expr visit(Latte.Absyn.ELitTrue p, A arg)
    {
      return new Latte.Absyn.ELitTrue();
    }    public Expr visit(Latte.Absyn.ELitFalse p, A arg)
    {
      return new Latte.Absyn.ELitFalse();
    }    public Expr visit(Latte.Absyn.EThis p, A arg)
    {
      return new Latte.Absyn.EThis();
    }    public Expr visit(Latte.Absyn.ENull p, A arg)
    {
      return new Latte.Absyn.ENull();
    }    public Expr visit(Latte.Absyn.EApp p, A arg)
    {
      String ident_ = p.ident_;
      ListExpr listexpr_ = new ListExpr();
      for (Expr x : p.listexpr_)
      {
        listexpr_.add(x.accept(this,arg));
      }
      return new Latte.Absyn.EApp(ident_, listexpr_);
    }    public Expr visit(Latte.Absyn.EString p, A arg)
    {
      String string_ = p.string_;
      return new Latte.Absyn.EString(string_);
    }    public Expr visit(Latte.Absyn.EConstr p, A arg)
    {
      String ident_ = p.ident_;
      return new Latte.Absyn.EConstr(ident_);
    }    public Expr visit(Latte.Absyn.EArrConstr p, A arg)
    {
      String ident_ = p.ident_;
      ListSizeBracket listsizebracket_ = new ListSizeBracket();
      for (SizeBracket x : p.listsizebracket_)
      {
        listsizebracket_.add(x.accept(this,arg));
      }
      return new Latte.Absyn.EArrConstr(ident_, listsizebracket_);
    }    public Expr visit(Latte.Absyn.ENDArrAcc p, A arg)
    {
      Expr expr_ = p.expr_.accept(this, arg);
      ListSizeBracket listsizebracket_ = new ListSizeBracket();
      for (SizeBracket x : p.listsizebracket_)
      {
        listsizebracket_.add(x.accept(this,arg));
      }
      return new Latte.Absyn.ENDArrAcc(expr_, listsizebracket_);
    }    public Expr visit(Latte.Absyn.FieldAcc p, A arg)
    {
      Expr expr_ = p.expr_.accept(this, arg);
      String ident_ = p.ident_;
      return new Latte.Absyn.FieldAcc(expr_, ident_);
    }    public Expr visit(Latte.Absyn.MethodCall p, A arg)
    {
      Expr expr_ = p.expr_.accept(this, arg);
      String ident_ = p.ident_;
      ListExpr listexpr_ = new ListExpr();
      for (Expr x : p.listexpr_)
      {
        listexpr_.add(x.accept(this,arg));
      }
      return new Latte.Absyn.MethodCall(expr_, ident_, listexpr_);
    }    public Expr visit(Latte.Absyn.Neg p, A arg)
    {
      Expr expr_ = p.expr_.accept(this, arg);
      return new Latte.Absyn.Neg(expr_);
    }    public Expr visit(Latte.Absyn.Not p, A arg)
    {
      Expr expr_ = p.expr_.accept(this, arg);
      return new Latte.Absyn.Not(expr_);
    }    public Expr visit(Latte.Absyn.EMul p, A arg)
    {
      Expr expr_1 = p.expr_1.accept(this, arg);
      MulOp mulop_ = p.mulop_.accept(this, arg);
      Expr expr_2 = p.expr_2.accept(this, arg);
      return new Latte.Absyn.EMul(expr_1, mulop_, expr_2);
    }    public Expr visit(Latte.Absyn.EAdd p, A arg)
    {
      Expr expr_1 = p.expr_1.accept(this, arg);
      AddOp addop_ = p.addop_.accept(this, arg);
      Expr expr_2 = p.expr_2.accept(this, arg);
      return new Latte.Absyn.EAdd(expr_1, addop_, expr_2);
    }    public Expr visit(Latte.Absyn.ERel p, A arg)
    {
      Expr expr_1 = p.expr_1.accept(this, arg);
      RelOp relop_ = p.relop_.accept(this, arg);
      Expr expr_2 = p.expr_2.accept(this, arg);
      return new Latte.Absyn.ERel(expr_1, relop_, expr_2);
    }    public Expr visit(Latte.Absyn.EAnd p, A arg)
    {
      Expr expr_1 = p.expr_1.accept(this, arg);
      Expr expr_2 = p.expr_2.accept(this, arg);
      return new Latte.Absyn.EAnd(expr_1, expr_2);
    }    public Expr visit(Latte.Absyn.EOr p, A arg)
    {
      Expr expr_1 = p.expr_1.accept(this, arg);
      Expr expr_2 = p.expr_2.accept(this, arg);
      return new Latte.Absyn.EOr(expr_1, expr_2);
    }
/* AddOp */
    public AddOp visit(Latte.Absyn.Plus p, A arg)
    {
      return new Latte.Absyn.Plus();
    }    public AddOp visit(Latte.Absyn.Minus p, A arg)
    {
      return new Latte.Absyn.Minus();
    }
/* MulOp */
    public MulOp visit(Latte.Absyn.Times p, A arg)
    {
      return new Latte.Absyn.Times();
    }    public MulOp visit(Latte.Absyn.Div p, A arg)
    {
      return new Latte.Absyn.Div();
    }    public MulOp visit(Latte.Absyn.Mod p, A arg)
    {
      return new Latte.Absyn.Mod();
    }
/* RelOp */
    public RelOp visit(Latte.Absyn.LTH p, A arg)
    {
      return new Latte.Absyn.LTH();
    }    public RelOp visit(Latte.Absyn.LE p, A arg)
    {
      return new Latte.Absyn.LE();
    }    public RelOp visit(Latte.Absyn.GTH p, A arg)
    {
      return new Latte.Absyn.GTH();
    }    public RelOp visit(Latte.Absyn.GE p, A arg)
    {
      return new Latte.Absyn.GE();
    }    public RelOp visit(Latte.Absyn.EQU p, A arg)
    {
      return new Latte.Absyn.EQU();
    }    public RelOp visit(Latte.Absyn.NE p, A arg)
    {
      return new Latte.Absyn.NE();
    }
}