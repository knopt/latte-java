package Latte;
import Latte.Absyn.*;

public class PrettyPrinter
{
  //For certain applications increasing the initial size of the buffer may improve performance.
  private static final int INITIAL_BUFFER_SIZE = 128;
  private static final int INDENT_WIDTH = 2;
  //You may wish to change the parentheses used in precedence.
  private static final String _L_PAREN = new String("(");
  private static final String _R_PAREN = new String(")");
  //You may wish to change render
  private static void render(String s)
  {
    if (s.equals("{"))
    {
       buf_.append("\n");
       indent();
       buf_.append(s);
       _n_ = _n_ + INDENT_WIDTH;
       buf_.append("\n");
       indent();
    }
    else if (s.equals("(") || s.equals("["))
       buf_.append(s);
    else if (s.equals(")") || s.equals("]"))
    {
       backup();
       buf_.append(s);
       buf_.append(" ");
    }
    else if (s.equals("}"))
    {
       int t;
       _n_ = _n_ - INDENT_WIDTH;
       for(t=0; t<INDENT_WIDTH; t++) {
         backup();
       }
       buf_.append(s);
       buf_.append("\n");
       indent();
    }
    else if (s.equals(","))
    {
       backup();
       buf_.append(s);
       buf_.append(" ");
    }
    else if (s.equals(";"))
    {
       backup();
       buf_.append(s);
       buf_.append("\n");
       indent();
    }
    else if (s.equals("")) return;
    else
    {
       buf_.append(s);
       buf_.append(" ");
    }
  }


  //  print and show methods are defined for each category.
  public static String print(Latte.Absyn.Program foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(Latte.Absyn.Program foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(Latte.Absyn.TopDef foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(Latte.Absyn.TopDef foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(Latte.Absyn.ListTopDef foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(Latte.Absyn.ListTopDef foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(Latte.Absyn.Arg foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(Latte.Absyn.Arg foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(Latte.Absyn.ListArg foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(Latte.Absyn.ListArg foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(Latte.Absyn.ClassHeader foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(Latte.Absyn.ClassHeader foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(Latte.Absyn.Implements foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(Latte.Absyn.Implements foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(Latte.Absyn.FieldDeclaration foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(Latte.Absyn.FieldDeclaration foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(Latte.Absyn.ListFieldDeclaration foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(Latte.Absyn.ListFieldDeclaration foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(Latte.Absyn.MethodBody foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(Latte.Absyn.MethodBody foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(Latte.Absyn.Modifier foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(Latte.Absyn.Modifier foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(Latte.Absyn.ListModifier foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(Latte.Absyn.ListModifier foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(Latte.Absyn.Block foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(Latte.Absyn.Block foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(Latte.Absyn.ListStmt foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(Latte.Absyn.ListStmt foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(Latte.Absyn.Stmt foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(Latte.Absyn.Stmt foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(Latte.Absyn.Item foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(Latte.Absyn.Item foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(Latte.Absyn.ListItem foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(Latte.Absyn.ListItem foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(Latte.Absyn.Lhs foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(Latte.Absyn.Lhs foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(Latte.Absyn.BasicType foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(Latte.Absyn.BasicType foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(Latte.Absyn.TypeName foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(Latte.Absyn.TypeName foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(Latte.Absyn.Type foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(Latte.Absyn.Type foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(Latte.Absyn.ListType foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(Latte.Absyn.ListType foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(Latte.Absyn.Expr foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(Latte.Absyn.Expr foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(Latte.Absyn.ListExpr foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(Latte.Absyn.ListExpr foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(Latte.Absyn.ObjAcc foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(Latte.Absyn.ObjAcc foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(Latte.Absyn.AddOp foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(Latte.Absyn.AddOp foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(Latte.Absyn.MulOp foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(Latte.Absyn.MulOp foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(Latte.Absyn.RelOp foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(Latte.Absyn.RelOp foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  /***   You shouldn't need to change anything beyond this point.   ***/

  private static void pp(Latte.Absyn.Program foo, int _i_)
  {
    if (foo instanceof Latte.Absyn.ProgramTD)
    {
       Latte.Absyn.ProgramTD _programtd = (Latte.Absyn.ProgramTD) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_programtd.listtopdef_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(Latte.Absyn.TopDef foo, int _i_)
  {
    if (foo instanceof Latte.Absyn.FnDef)
    {
       Latte.Absyn.FnDef _fndef = (Latte.Absyn.FnDef) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_fndef.type_, 0);
       pp(_fndef.ident_, 0);
       render("(");
       pp(_fndef.listarg_, 0);
       render(")");
       pp(_fndef.block_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.ClassDecl)
    {
       Latte.Absyn.ClassDecl _classdecl = (Latte.Absyn.ClassDecl) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_classdecl.classheader_, 0);
       render("{");
       pp(_classdecl.listfielddeclaration_, 0);
       render("}");
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(Latte.Absyn.ListTopDef foo, int _i_)
  {
     for (java.util.Iterator<TopDef> it = foo.iterator(); it.hasNext();)
     {
       pp(it.next(), _i_);
       if (it.hasNext()) {
         render("");
       } else {
         render("");
       }
     }  }

  private static void pp(Latte.Absyn.Arg foo, int _i_)
  {
    if (foo instanceof Latte.Absyn.ArgTI)
    {
       Latte.Absyn.ArgTI _argti = (Latte.Absyn.ArgTI) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_argti.type_, 0);
       pp(_argti.ident_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(Latte.Absyn.ListArg foo, int _i_)
  {
     for (java.util.Iterator<Arg> it = foo.iterator(); it.hasNext();)
     {
       pp(it.next(), _i_);
       if (it.hasNext()) {
         render(",");
       } else {
         render("");
       }
     }  }

  private static void pp(Latte.Absyn.ClassHeader foo, int _i_)
  {
    if (foo instanceof Latte.Absyn.ClassDec)
    {
       Latte.Absyn.ClassDec _classdec = (Latte.Absyn.ClassDec) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("class");
       pp(_classdec.ident_, 0);
       pp(_classdec.implements_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.InterDec)
    {
       Latte.Absyn.InterDec _interdec = (Latte.Absyn.InterDec) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("interface");
       pp(_interdec.ident_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(Latte.Absyn.Implements foo, int _i_)
  {
    if (foo instanceof Latte.Absyn.InterImpl)
    {
       Latte.Absyn.InterImpl _interimpl = (Latte.Absyn.InterImpl) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("implements");
       pp(_interimpl.ident_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.EImpl)
    {
       Latte.Absyn.EImpl _eimpl = (Latte.Absyn.EImpl) foo;
       if (_i_ > 0) render(_L_PAREN);
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(Latte.Absyn.FieldDeclaration foo, int _i_)
  {
    if (foo instanceof Latte.Absyn.Dvar)
    {
       Latte.Absyn.Dvar _dvar = (Latte.Absyn.Dvar) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_dvar.modifier_, 0);
       pp(_dvar.type_, 0);
       pp(_dvar.listitem_, 0);
       render(";");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.Dmth)
    {
       Latte.Absyn.Dmth _dmth = (Latte.Absyn.Dmth) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_dmth.modifier_, 0);
       pp(_dmth.type_, 0);
       pp(_dmth.ident_, 0);
       render("(");
       pp(_dmth.listarg_, 0);
       render(")");
       pp(_dmth.methodbody_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(Latte.Absyn.ListFieldDeclaration foo, int _i_)
  {
     for (java.util.Iterator<FieldDeclaration> it = foo.iterator(); it.hasNext();)
     {
       pp(it.next(), _i_);
       if (it.hasNext()) {
         render("");
       } else {
         render("");
       }
     }  }

  private static void pp(Latte.Absyn.MethodBody foo, int _i_)
  {
    if (foo instanceof Latte.Absyn.EmptyMBody)
    {
       Latte.Absyn.EmptyMBody _emptymbody = (Latte.Absyn.EmptyMBody) foo;
       if (_i_ > 0) render(_L_PAREN);
       render(";");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.MBody)
    {
       Latte.Absyn.MBody _mbody = (Latte.Absyn.MBody) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_mbody.block_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(Latte.Absyn.Modifier foo, int _i_)
  {
    if (foo instanceof Latte.Absyn.Mfinal)
    {
       Latte.Absyn.Mfinal _mfinal = (Latte.Absyn.Mfinal) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("final");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.Mpublic)
    {
       Latte.Absyn.Mpublic _mpublic = (Latte.Absyn.Mpublic) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("public");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.Mprivate)
    {
       Latte.Absyn.Mprivate _mprivate = (Latte.Absyn.Mprivate) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("private");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.Mstatic)
    {
       Latte.Absyn.Mstatic _mstatic = (Latte.Absyn.Mstatic) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("static");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.MEmpty)
    {
       Latte.Absyn.MEmpty _mempty = (Latte.Absyn.MEmpty) foo;
       if (_i_ > 0) render(_L_PAREN);
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(Latte.Absyn.ListModifier foo, int _i_)
  {
     for (java.util.Iterator<Modifier> it = foo.iterator(); it.hasNext();)
     {
       pp(it.next(), _i_);
       if (it.hasNext()) {
         render("");
       } else {
         render("");
       }
     }  }

  private static void pp(Latte.Absyn.Block foo, int _i_)
  {
    if (foo instanceof Latte.Absyn.BlockS)
    {
       Latte.Absyn.BlockS _blocks = (Latte.Absyn.BlockS) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("{");
       pp(_blocks.liststmt_, 0);
       render("}");
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(Latte.Absyn.ListStmt foo, int _i_)
  {
     for (java.util.Iterator<Stmt> it = foo.iterator(); it.hasNext();)
     {
       pp(it.next(), _i_);
       if (it.hasNext()) {
         render("");
       } else {
         render("");
       }
     }  }

  private static void pp(Latte.Absyn.Stmt foo, int _i_)
  {
    if (foo instanceof Latte.Absyn.Empty)
    {
       Latte.Absyn.Empty _empty = (Latte.Absyn.Empty) foo;
       if (_i_ > 0) render(_L_PAREN);
       render(";");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.BStmt)
    {
       Latte.Absyn.BStmt _bstmt = (Latte.Absyn.BStmt) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_bstmt.block_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.Decl)
    {
       Latte.Absyn.Decl _decl = (Latte.Absyn.Decl) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_decl.type_, 0);
       pp(_decl.listitem_, 0);
       render(";");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.Ass)
    {
       Latte.Absyn.Ass _ass = (Latte.Absyn.Ass) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_ass.lhs_, 0);
       render("=");
       pp(_ass.expr_, 0);
       render(";");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.Incr)
    {
       Latte.Absyn.Incr _incr = (Latte.Absyn.Incr) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_incr.lhs_, 0);
       render("++");
       render(";");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.Decr)
    {
       Latte.Absyn.Decr _decr = (Latte.Absyn.Decr) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_decr.lhs_, 0);
       render("--");
       render(";");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.Ret)
    {
       Latte.Absyn.Ret _ret = (Latte.Absyn.Ret) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("return");
       pp(_ret.expr_, 0);
       render(";");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.VRet)
    {
       Latte.Absyn.VRet _vret = (Latte.Absyn.VRet) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("return");
       render(";");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.Cond)
    {
       Latte.Absyn.Cond _cond = (Latte.Absyn.Cond) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("if");
       render("(");
       pp(_cond.expr_, 0);
       render(")");
       pp(_cond.stmt_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.CondElse)
    {
       Latte.Absyn.CondElse _condelse = (Latte.Absyn.CondElse) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("if");
       render("(");
       pp(_condelse.expr_, 0);
       render(")");
       pp(_condelse.stmt_1, 0);
       render("else");
       pp(_condelse.stmt_2, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.While)
    {
       Latte.Absyn.While _while = (Latte.Absyn.While) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("while");
       render("(");
       pp(_while.expr_, 0);
       render(")");
       pp(_while.stmt_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.ForArr)
    {
       Latte.Absyn.ForArr _forarr = (Latte.Absyn.ForArr) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("for");
       render("(");
       pp(_forarr.basictype_, 0);
       pp(_forarr.ident_, 0);
       render(":");
       pp(_forarr.expr_, 0);
       render(")");
       pp(_forarr.stmt_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.SExp)
    {
       Latte.Absyn.SExp _sexp = (Latte.Absyn.SExp) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_sexp.expr_, 0);
       render(";");
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(Latte.Absyn.Item foo, int _i_)
  {
    if (foo instanceof Latte.Absyn.NoInit)
    {
       Latte.Absyn.NoInit _noinit = (Latte.Absyn.NoInit) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_noinit.ident_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.Init)
    {
       Latte.Absyn.Init _init = (Latte.Absyn.Init) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_init.ident_, 0);
       render("=");
       pp(_init.expr_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(Latte.Absyn.ListItem foo, int _i_)
  {
     for (java.util.Iterator<Item> it = foo.iterator(); it.hasNext();)
     {
       pp(it.next(), _i_);
       if (it.hasNext()) {
         render(",");
       } else {
         render("");
       }
     }  }

  private static void pp(Latte.Absyn.Lhs foo, int _i_)
  {
    if (foo instanceof Latte.Absyn.VariableRawLhs)
    {
       Latte.Absyn.VariableRawLhs _variablerawlhs = (Latte.Absyn.VariableRawLhs) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_variablerawlhs.ident_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.ArrElemLhs)
    {
       Latte.Absyn.ArrElemLhs _arrelemlhs = (Latte.Absyn.ArrElemLhs) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_arrelemlhs.ident_, 0);
       render("[");
       pp(_arrelemlhs.expr_, 0);
       render("]");
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(Latte.Absyn.BasicType foo, int _i_)
  {
    if (foo instanceof Latte.Absyn.Int)
    {
       Latte.Absyn.Int _int = (Latte.Absyn.Int) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("int");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.Str)
    {
       Latte.Absyn.Str _str = (Latte.Absyn.Str) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("string");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.Bool)
    {
       Latte.Absyn.Bool _bool = (Latte.Absyn.Bool) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("boolean");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.Void)
    {
       Latte.Absyn.Void _void = (Latte.Absyn.Void) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("void");
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(Latte.Absyn.TypeName foo, int _i_)
  {
    if (foo instanceof Latte.Absyn.BuiltIn)
    {
       Latte.Absyn.BuiltIn _builtin = (Latte.Absyn.BuiltIn) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_builtin.basictype_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.ClassName)
    {
       Latte.Absyn.ClassName _classname = (Latte.Absyn.ClassName) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_classname.ident_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(Latte.Absyn.Type foo, int _i_)
  {
    if (foo instanceof Latte.Absyn.ArrayType)
    {
       Latte.Absyn.ArrayType _arraytype = (Latte.Absyn.ArrayType) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_arraytype.typename_, 0);
       render("[]");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.TypeNameS)
    {
       Latte.Absyn.TypeNameS _typenames = (Latte.Absyn.TypeNameS) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_typenames.typename_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(Latte.Absyn.ListType foo, int _i_)
  {
     for (java.util.Iterator<Type> it = foo.iterator(); it.hasNext();)
     {
       pp(it.next(), _i_);
       if (it.hasNext()) {
         render(",");
       } else {
         render("");
       }
     }  }

  private static void pp(Latte.Absyn.Expr foo, int _i_)
  {
    if (foo instanceof Latte.Absyn.EVar)
    {
       Latte.Absyn.EVar _evar = (Latte.Absyn.EVar) foo;
       if (_i_ > 7) render(_L_PAREN);
       pp(_evar.ident_, 0);
       if (_i_ > 7) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.ELitInt)
    {
       Latte.Absyn.ELitInt _elitint = (Latte.Absyn.ELitInt) foo;
       if (_i_ > 7) render(_L_PAREN);
       pp(_elitint.integer_, 0);
       if (_i_ > 7) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.ELitTrue)
    {
       Latte.Absyn.ELitTrue _elittrue = (Latte.Absyn.ELitTrue) foo;
       if (_i_ > 7) render(_L_PAREN);
       render("true");
       if (_i_ > 7) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.ELitFalse)
    {
       Latte.Absyn.ELitFalse _elitfalse = (Latte.Absyn.ELitFalse) foo;
       if (_i_ > 7) render(_L_PAREN);
       render("false");
       if (_i_ > 7) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.EThis)
    {
       Latte.Absyn.EThis _ethis = (Latte.Absyn.EThis) foo;
       if (_i_ > 7) render(_L_PAREN);
       render("this");
       if (_i_ > 7) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.ENull)
    {
       Latte.Absyn.ENull _enull = (Latte.Absyn.ENull) foo;
       if (_i_ > 7) render(_L_PAREN);
       render("null");
       if (_i_ > 7) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.EApp)
    {
       Latte.Absyn.EApp _eapp = (Latte.Absyn.EApp) foo;
       if (_i_ > 7) render(_L_PAREN);
       pp(_eapp.ident_, 0);
       render("(");
       pp(_eapp.listexpr_, 0);
       render(")");
       if (_i_ > 7) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.EString)
    {
       Latte.Absyn.EString _estring = (Latte.Absyn.EString) foo;
       if (_i_ > 7) render(_L_PAREN);
       printQuoted(_estring.string_);
       if (_i_ > 7) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.EConstr)
    {
       Latte.Absyn.EConstr _econstr = (Latte.Absyn.EConstr) foo;
       if (_i_ > 7) render(_L_PAREN);
       render("new");
       pp(_econstr.ident_, 0);
       if (_i_ > 7) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.EArrConstr)
    {
       Latte.Absyn.EArrConstr _earrconstr = (Latte.Absyn.EArrConstr) foo;
       if (_i_ > 6) render(_L_PAREN);
       render("new");
       pp(_earrconstr.ident_, 0);
       render("[");
       pp(_earrconstr.expr_, 0);
       render("]");
       if (_i_ > 6) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.ENDArrAcc)
    {
       Latte.Absyn.ENDArrAcc _endarracc = (Latte.Absyn.ENDArrAcc) foo;
       if (_i_ > 6) render(_L_PAREN);
       pp(_endarracc.expr_1, 0);
       render("[");
       pp(_endarracc.expr_2, 0);
       render("]");
       if (_i_ > 6) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.EObjAcc)
    {
       Latte.Absyn.EObjAcc _eobjacc = (Latte.Absyn.EObjAcc) foo;
       if (_i_ > 5) render(_L_PAREN);
       pp(_eobjacc.expr_, 0);
       pp(_eobjacc.objacc_, 0);
       if (_i_ > 5) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.Neg)
    {
       Latte.Absyn.Neg _neg = (Latte.Absyn.Neg) foo;
       if (_i_ > 5) render(_L_PAREN);
       render("-");
       pp(_neg.expr_, 6);
       if (_i_ > 5) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.Not)
    {
       Latte.Absyn.Not _not = (Latte.Absyn.Not) foo;
       if (_i_ > 5) render(_L_PAREN);
       render("!");
       pp(_not.expr_, 6);
       if (_i_ > 5) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.EMul)
    {
       Latte.Absyn.EMul _emul = (Latte.Absyn.EMul) foo;
       if (_i_ > 4) render(_L_PAREN);
       pp(_emul.expr_1, 4);
       pp(_emul.mulop_, 0);
       pp(_emul.expr_2, 5);
       if (_i_ > 4) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.EAdd)
    {
       Latte.Absyn.EAdd _eadd = (Latte.Absyn.EAdd) foo;
       if (_i_ > 3) render(_L_PAREN);
       pp(_eadd.expr_1, 3);
       pp(_eadd.addop_, 0);
       pp(_eadd.expr_2, 4);
       if (_i_ > 3) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.ERel)
    {
       Latte.Absyn.ERel _erel = (Latte.Absyn.ERel) foo;
       if (_i_ > 2) render(_L_PAREN);
       pp(_erel.expr_1, 2);
       pp(_erel.relop_, 0);
       pp(_erel.expr_2, 3);
       if (_i_ > 2) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.EAnd)
    {
       Latte.Absyn.EAnd _eand = (Latte.Absyn.EAnd) foo;
       if (_i_ > 1) render(_L_PAREN);
       pp(_eand.expr_1, 2);
       render("&&");
       pp(_eand.expr_2, 1);
       if (_i_ > 1) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.EOr)
    {
       Latte.Absyn.EOr _eor = (Latte.Absyn.EOr) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_eor.expr_1, 1);
       render("||");
       pp(_eor.expr_2, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(Latte.Absyn.ListExpr foo, int _i_)
  {
     for (java.util.Iterator<Expr> it = foo.iterator(); it.hasNext();)
     {
       pp(it.next(), _i_);
       if (it.hasNext()) {
         render(",");
       } else {
         render("");
       }
     }  }

  private static void pp(Latte.Absyn.ObjAcc foo, int _i_)
  {
    if (foo instanceof Latte.Absyn.ObjFieldAcc)
    {
       Latte.Absyn.ObjFieldAcc _objfieldacc = (Latte.Absyn.ObjFieldAcc) foo;
       if (_i_ > 0) render(_L_PAREN);
       render(".");
       pp(_objfieldacc.ident_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.ObjMethAcc)
    {
       Latte.Absyn.ObjMethAcc _objmethacc = (Latte.Absyn.ObjMethAcc) foo;
       if (_i_ > 0) render(_L_PAREN);
       render(".");
       pp(_objmethacc.ident_, 0);
       render("(");
       pp(_objmethacc.listexpr_, 0);
       render(")");
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(Latte.Absyn.AddOp foo, int _i_)
  {
    if (foo instanceof Latte.Absyn.Plus)
    {
       Latte.Absyn.Plus _plus = (Latte.Absyn.Plus) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("+");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.Minus)
    {
       Latte.Absyn.Minus _minus = (Latte.Absyn.Minus) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("-");
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(Latte.Absyn.MulOp foo, int _i_)
  {
    if (foo instanceof Latte.Absyn.Times)
    {
       Latte.Absyn.Times _times = (Latte.Absyn.Times) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("*");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.Div)
    {
       Latte.Absyn.Div _div = (Latte.Absyn.Div) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("/");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.Mod)
    {
       Latte.Absyn.Mod _mod = (Latte.Absyn.Mod) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("%");
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(Latte.Absyn.RelOp foo, int _i_)
  {
    if (foo instanceof Latte.Absyn.LTH)
    {
       Latte.Absyn.LTH _lth = (Latte.Absyn.LTH) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("<");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.LE)
    {
       Latte.Absyn.LE _le = (Latte.Absyn.LE) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("<=");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.GTH)
    {
       Latte.Absyn.GTH _gth = (Latte.Absyn.GTH) foo;
       if (_i_ > 0) render(_L_PAREN);
       render(">");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.GE)
    {
       Latte.Absyn.GE _ge = (Latte.Absyn.GE) foo;
       if (_i_ > 0) render(_L_PAREN);
       render(">=");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.EQU)
    {
       Latte.Absyn.EQU _equ = (Latte.Absyn.EQU) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("==");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Latte.Absyn.NE)
    {
       Latte.Absyn.NE _ne = (Latte.Absyn.NE) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("!=");
       if (_i_ > 0) render(_R_PAREN);
    }
  }


  private static void sh(Latte.Absyn.Program foo)
  {
    if (foo instanceof Latte.Absyn.ProgramTD)
    {
       Latte.Absyn.ProgramTD _programtd = (Latte.Absyn.ProgramTD) foo;
       render("(");
       render("ProgramTD");
       render("[");
       sh(_programtd.listtopdef_);
       render("]");
       render(")");
    }
  }

  private static void sh(Latte.Absyn.TopDef foo)
  {
    if (foo instanceof Latte.Absyn.FnDef)
    {
       Latte.Absyn.FnDef _fndef = (Latte.Absyn.FnDef) foo;
       render("(");
       render("FnDef");
       sh(_fndef.type_);
       sh(_fndef.ident_);
       render("[");
       sh(_fndef.listarg_);
       render("]");
       sh(_fndef.block_);
       render(")");
    }
    if (foo instanceof Latte.Absyn.ClassDecl)
    {
       Latte.Absyn.ClassDecl _classdecl = (Latte.Absyn.ClassDecl) foo;
       render("(");
       render("ClassDecl");
       sh(_classdecl.classheader_);
       render("[");
       sh(_classdecl.listfielddeclaration_);
       render("]");
       render(")");
    }
  }

  private static void sh(Latte.Absyn.ListTopDef foo)
  {
     for (java.util.Iterator<TopDef> it = foo.iterator(); it.hasNext();)
     {
       sh(it.next());
       if (it.hasNext())
         render(",");
     }
  }

  private static void sh(Latte.Absyn.Arg foo)
  {
    if (foo instanceof Latte.Absyn.ArgTI)
    {
       Latte.Absyn.ArgTI _argti = (Latte.Absyn.ArgTI) foo;
       render("(");
       render("ArgTI");
       sh(_argti.type_);
       sh(_argti.ident_);
       render(")");
    }
  }

  private static void sh(Latte.Absyn.ListArg foo)
  {
     for (java.util.Iterator<Arg> it = foo.iterator(); it.hasNext();)
     {
       sh(it.next());
       if (it.hasNext())
         render(",");
     }
  }

  private static void sh(Latte.Absyn.ClassHeader foo)
  {
    if (foo instanceof Latte.Absyn.ClassDec)
    {
       Latte.Absyn.ClassDec _classdec = (Latte.Absyn.ClassDec) foo;
       render("(");
       render("ClassDec");
       sh(_classdec.ident_);
       sh(_classdec.implements_);
       render(")");
    }
    if (foo instanceof Latte.Absyn.InterDec)
    {
       Latte.Absyn.InterDec _interdec = (Latte.Absyn.InterDec) foo;
       render("(");
       render("InterDec");
       sh(_interdec.ident_);
       render(")");
    }
  }

  private static void sh(Latte.Absyn.Implements foo)
  {
    if (foo instanceof Latte.Absyn.InterImpl)
    {
       Latte.Absyn.InterImpl _interimpl = (Latte.Absyn.InterImpl) foo;
       render("(");
       render("InterImpl");
       sh(_interimpl.ident_);
       render(")");
    }
    if (foo instanceof Latte.Absyn.EImpl)
    {
       Latte.Absyn.EImpl _eimpl = (Latte.Absyn.EImpl) foo;
       render("EImpl");
    }
  }

  private static void sh(Latte.Absyn.FieldDeclaration foo)
  {
    if (foo instanceof Latte.Absyn.Dvar)
    {
       Latte.Absyn.Dvar _dvar = (Latte.Absyn.Dvar) foo;
       render("(");
       render("Dvar");
       sh(_dvar.modifier_);
       sh(_dvar.type_);
       render("[");
       sh(_dvar.listitem_);
       render("]");
       render(")");
    }
    if (foo instanceof Latte.Absyn.Dmth)
    {
       Latte.Absyn.Dmth _dmth = (Latte.Absyn.Dmth) foo;
       render("(");
       render("Dmth");
       sh(_dmth.modifier_);
       sh(_dmth.type_);
       sh(_dmth.ident_);
       render("[");
       sh(_dmth.listarg_);
       render("]");
       sh(_dmth.methodbody_);
       render(")");
    }
  }

  private static void sh(Latte.Absyn.ListFieldDeclaration foo)
  {
     for (java.util.Iterator<FieldDeclaration> it = foo.iterator(); it.hasNext();)
     {
       sh(it.next());
       if (it.hasNext())
         render(",");
     }
  }

  private static void sh(Latte.Absyn.MethodBody foo)
  {
    if (foo instanceof Latte.Absyn.EmptyMBody)
    {
       Latte.Absyn.EmptyMBody _emptymbody = (Latte.Absyn.EmptyMBody) foo;
       render("EmptyMBody");
    }
    if (foo instanceof Latte.Absyn.MBody)
    {
       Latte.Absyn.MBody _mbody = (Latte.Absyn.MBody) foo;
       render("(");
       render("MBody");
       sh(_mbody.block_);
       render(")");
    }
  }

  private static void sh(Latte.Absyn.Modifier foo)
  {
    if (foo instanceof Latte.Absyn.Mfinal)
    {
       Latte.Absyn.Mfinal _mfinal = (Latte.Absyn.Mfinal) foo;
       render("Mfinal");
    }
    if (foo instanceof Latte.Absyn.Mpublic)
    {
       Latte.Absyn.Mpublic _mpublic = (Latte.Absyn.Mpublic) foo;
       render("Mpublic");
    }
    if (foo instanceof Latte.Absyn.Mprivate)
    {
       Latte.Absyn.Mprivate _mprivate = (Latte.Absyn.Mprivate) foo;
       render("Mprivate");
    }
    if (foo instanceof Latte.Absyn.Mstatic)
    {
       Latte.Absyn.Mstatic _mstatic = (Latte.Absyn.Mstatic) foo;
       render("Mstatic");
    }
    if (foo instanceof Latte.Absyn.MEmpty)
    {
       Latte.Absyn.MEmpty _mempty = (Latte.Absyn.MEmpty) foo;
       render("MEmpty");
    }
  }

  private static void sh(Latte.Absyn.ListModifier foo)
  {
     for (java.util.Iterator<Modifier> it = foo.iterator(); it.hasNext();)
     {
       sh(it.next());
       if (it.hasNext())
         render(",");
     }
  }

  private static void sh(Latte.Absyn.Block foo)
  {
    if (foo instanceof Latte.Absyn.BlockS)
    {
       Latte.Absyn.BlockS _blocks = (Latte.Absyn.BlockS) foo;
       render("(");
       render("BlockS");
       render("[");
       sh(_blocks.liststmt_);
       render("]");
       render(")");
    }
  }

  private static void sh(Latte.Absyn.ListStmt foo)
  {
     for (java.util.Iterator<Stmt> it = foo.iterator(); it.hasNext();)
     {
       sh(it.next());
       if (it.hasNext())
         render(",");
     }
  }

  private static void sh(Latte.Absyn.Stmt foo)
  {
    if (foo instanceof Latte.Absyn.Empty)
    {
       Latte.Absyn.Empty _empty = (Latte.Absyn.Empty) foo;
       render("Empty");
    }
    if (foo instanceof Latte.Absyn.BStmt)
    {
       Latte.Absyn.BStmt _bstmt = (Latte.Absyn.BStmt) foo;
       render("(");
       render("BStmt");
       sh(_bstmt.block_);
       render(")");
    }
    if (foo instanceof Latte.Absyn.Decl)
    {
       Latte.Absyn.Decl _decl = (Latte.Absyn.Decl) foo;
       render("(");
       render("Decl");
       sh(_decl.type_);
       render("[");
       sh(_decl.listitem_);
       render("]");
       render(")");
    }
    if (foo instanceof Latte.Absyn.Ass)
    {
       Latte.Absyn.Ass _ass = (Latte.Absyn.Ass) foo;
       render("(");
       render("Ass");
       sh(_ass.lhs_);
       sh(_ass.expr_);
       render(")");
    }
    if (foo instanceof Latte.Absyn.Incr)
    {
       Latte.Absyn.Incr _incr = (Latte.Absyn.Incr) foo;
       render("(");
       render("Incr");
       sh(_incr.lhs_);
       render(")");
    }
    if (foo instanceof Latte.Absyn.Decr)
    {
       Latte.Absyn.Decr _decr = (Latte.Absyn.Decr) foo;
       render("(");
       render("Decr");
       sh(_decr.lhs_);
       render(")");
    }
    if (foo instanceof Latte.Absyn.Ret)
    {
       Latte.Absyn.Ret _ret = (Latte.Absyn.Ret) foo;
       render("(");
       render("Ret");
       sh(_ret.expr_);
       render(")");
    }
    if (foo instanceof Latte.Absyn.VRet)
    {
       Latte.Absyn.VRet _vret = (Latte.Absyn.VRet) foo;
       render("VRet");
    }
    if (foo instanceof Latte.Absyn.Cond)
    {
       Latte.Absyn.Cond _cond = (Latte.Absyn.Cond) foo;
       render("(");
       render("Cond");
       sh(_cond.expr_);
       sh(_cond.stmt_);
       render(")");
    }
    if (foo instanceof Latte.Absyn.CondElse)
    {
       Latte.Absyn.CondElse _condelse = (Latte.Absyn.CondElse) foo;
       render("(");
       render("CondElse");
       sh(_condelse.expr_);
       sh(_condelse.stmt_1);
       sh(_condelse.stmt_2);
       render(")");
    }
    if (foo instanceof Latte.Absyn.While)
    {
       Latte.Absyn.While _while = (Latte.Absyn.While) foo;
       render("(");
       render("While");
       sh(_while.expr_);
       sh(_while.stmt_);
       render(")");
    }
    if (foo instanceof Latte.Absyn.ForArr)
    {
       Latte.Absyn.ForArr _forarr = (Latte.Absyn.ForArr) foo;
       render("(");
       render("ForArr");
       sh(_forarr.basictype_);
       sh(_forarr.ident_);
       sh(_forarr.expr_);
       sh(_forarr.stmt_);
       render(")");
    }
    if (foo instanceof Latte.Absyn.SExp)
    {
       Latte.Absyn.SExp _sexp = (Latte.Absyn.SExp) foo;
       render("(");
       render("SExp");
       sh(_sexp.expr_);
       render(")");
    }
  }

  private static void sh(Latte.Absyn.Item foo)
  {
    if (foo instanceof Latte.Absyn.NoInit)
    {
       Latte.Absyn.NoInit _noinit = (Latte.Absyn.NoInit) foo;
       render("(");
       render("NoInit");
       sh(_noinit.ident_);
       render(")");
    }
    if (foo instanceof Latte.Absyn.Init)
    {
       Latte.Absyn.Init _init = (Latte.Absyn.Init) foo;
       render("(");
       render("Init");
       sh(_init.ident_);
       sh(_init.expr_);
       render(")");
    }
  }

  private static void sh(Latte.Absyn.ListItem foo)
  {
     for (java.util.Iterator<Item> it = foo.iterator(); it.hasNext();)
     {
       sh(it.next());
       if (it.hasNext())
         render(",");
     }
  }

  private static void sh(Latte.Absyn.Lhs foo)
  {
    if (foo instanceof Latte.Absyn.VariableRawLhs)
    {
       Latte.Absyn.VariableRawLhs _variablerawlhs = (Latte.Absyn.VariableRawLhs) foo;
       render("(");
       render("VariableRawLhs");
       sh(_variablerawlhs.ident_);
       render(")");
    }
    if (foo instanceof Latte.Absyn.ArrElemLhs)
    {
       Latte.Absyn.ArrElemLhs _arrelemlhs = (Latte.Absyn.ArrElemLhs) foo;
       render("(");
       render("ArrElemLhs");
       sh(_arrelemlhs.ident_);
       sh(_arrelemlhs.expr_);
       render(")");
    }
  }

  private static void sh(Latte.Absyn.BasicType foo)
  {
    if (foo instanceof Latte.Absyn.Int)
    {
       Latte.Absyn.Int _int = (Latte.Absyn.Int) foo;
       render("Int");
    }
    if (foo instanceof Latte.Absyn.Str)
    {
       Latte.Absyn.Str _str = (Latte.Absyn.Str) foo;
       render("Str");
    }
    if (foo instanceof Latte.Absyn.Bool)
    {
       Latte.Absyn.Bool _bool = (Latte.Absyn.Bool) foo;
       render("Bool");
    }
    if (foo instanceof Latte.Absyn.Void)
    {
       Latte.Absyn.Void _void = (Latte.Absyn.Void) foo;
       render("Void");
    }
  }

  private static void sh(Latte.Absyn.TypeName foo)
  {
    if (foo instanceof Latte.Absyn.BuiltIn)
    {
       Latte.Absyn.BuiltIn _builtin = (Latte.Absyn.BuiltIn) foo;
       render("(");
       render("BuiltIn");
       sh(_builtin.basictype_);
       render(")");
    }
    if (foo instanceof Latte.Absyn.ClassName)
    {
       Latte.Absyn.ClassName _classname = (Latte.Absyn.ClassName) foo;
       render("(");
       render("ClassName");
       sh(_classname.ident_);
       render(")");
    }
  }

  private static void sh(Latte.Absyn.Type foo)
  {
    if (foo instanceof Latte.Absyn.ArrayType)
    {
       Latte.Absyn.ArrayType _arraytype = (Latte.Absyn.ArrayType) foo;
       render("(");
       render("ArrayType");
       sh(_arraytype.typename_);
       render(")");
    }
    if (foo instanceof Latte.Absyn.TypeNameS)
    {
       Latte.Absyn.TypeNameS _typenames = (Latte.Absyn.TypeNameS) foo;
       render("(");
       render("TypeNameS");
       sh(_typenames.typename_);
       render(")");
    }
  }

  private static void sh(Latte.Absyn.ListType foo)
  {
     for (java.util.Iterator<Type> it = foo.iterator(); it.hasNext();)
     {
       sh(it.next());
       if (it.hasNext())
         render(",");
     }
  }

  private static void sh(Latte.Absyn.Expr foo)
  {
    if (foo instanceof Latte.Absyn.EVar)
    {
       Latte.Absyn.EVar _evar = (Latte.Absyn.EVar) foo;
       render("(");
       render("EVar");
       sh(_evar.ident_);
       render(")");
    }
    if (foo instanceof Latte.Absyn.ELitInt)
    {
       Latte.Absyn.ELitInt _elitint = (Latte.Absyn.ELitInt) foo;
       render("(");
       render("ELitInt");
       sh(_elitint.integer_);
       render(")");
    }
    if (foo instanceof Latte.Absyn.ELitTrue)
    {
       Latte.Absyn.ELitTrue _elittrue = (Latte.Absyn.ELitTrue) foo;
       render("ELitTrue");
    }
    if (foo instanceof Latte.Absyn.ELitFalse)
    {
       Latte.Absyn.ELitFalse _elitfalse = (Latte.Absyn.ELitFalse) foo;
       render("ELitFalse");
    }
    if (foo instanceof Latte.Absyn.EThis)
    {
       Latte.Absyn.EThis _ethis = (Latte.Absyn.EThis) foo;
       render("EThis");
    }
    if (foo instanceof Latte.Absyn.ENull)
    {
       Latte.Absyn.ENull _enull = (Latte.Absyn.ENull) foo;
       render("ENull");
    }
    if (foo instanceof Latte.Absyn.EApp)
    {
       Latte.Absyn.EApp _eapp = (Latte.Absyn.EApp) foo;
       render("(");
       render("EApp");
       sh(_eapp.ident_);
       render("[");
       sh(_eapp.listexpr_);
       render("]");
       render(")");
    }
    if (foo instanceof Latte.Absyn.EString)
    {
       Latte.Absyn.EString _estring = (Latte.Absyn.EString) foo;
       render("(");
       render("EString");
       sh(_estring.string_);
       render(")");
    }
    if (foo instanceof Latte.Absyn.EConstr)
    {
       Latte.Absyn.EConstr _econstr = (Latte.Absyn.EConstr) foo;
       render("(");
       render("EConstr");
       sh(_econstr.ident_);
       render(")");
    }
    if (foo instanceof Latte.Absyn.EArrConstr)
    {
       Latte.Absyn.EArrConstr _earrconstr = (Latte.Absyn.EArrConstr) foo;
       render("(");
       render("EArrConstr");
       sh(_earrconstr.ident_);
       sh(_earrconstr.expr_);
       render(")");
    }
    if (foo instanceof Latte.Absyn.ENDArrAcc)
    {
       Latte.Absyn.ENDArrAcc _endarracc = (Latte.Absyn.ENDArrAcc) foo;
       render("(");
       render("ENDArrAcc");
       sh(_endarracc.expr_1);
       sh(_endarracc.expr_2);
       render(")");
    }
    if (foo instanceof Latte.Absyn.EObjAcc)
    {
       Latte.Absyn.EObjAcc _eobjacc = (Latte.Absyn.EObjAcc) foo;
       render("(");
       render("EObjAcc");
       sh(_eobjacc.expr_);
       sh(_eobjacc.objacc_);
       render(")");
    }
    if (foo instanceof Latte.Absyn.Neg)
    {
       Latte.Absyn.Neg _neg = (Latte.Absyn.Neg) foo;
       render("(");
       render("Neg");
       sh(_neg.expr_);
       render(")");
    }
    if (foo instanceof Latte.Absyn.Not)
    {
       Latte.Absyn.Not _not = (Latte.Absyn.Not) foo;
       render("(");
       render("Not");
       sh(_not.expr_);
       render(")");
    }
    if (foo instanceof Latte.Absyn.EMul)
    {
       Latte.Absyn.EMul _emul = (Latte.Absyn.EMul) foo;
       render("(");
       render("EMul");
       sh(_emul.expr_1);
       sh(_emul.mulop_);
       sh(_emul.expr_2);
       render(")");
    }
    if (foo instanceof Latte.Absyn.EAdd)
    {
       Latte.Absyn.EAdd _eadd = (Latte.Absyn.EAdd) foo;
       render("(");
       render("EAdd");
       sh(_eadd.expr_1);
       sh(_eadd.addop_);
       sh(_eadd.expr_2);
       render(")");
    }
    if (foo instanceof Latte.Absyn.ERel)
    {
       Latte.Absyn.ERel _erel = (Latte.Absyn.ERel) foo;
       render("(");
       render("ERel");
       sh(_erel.expr_1);
       sh(_erel.relop_);
       sh(_erel.expr_2);
       render(")");
    }
    if (foo instanceof Latte.Absyn.EAnd)
    {
       Latte.Absyn.EAnd _eand = (Latte.Absyn.EAnd) foo;
       render("(");
       render("EAnd");
       sh(_eand.expr_1);
       sh(_eand.expr_2);
       render(")");
    }
    if (foo instanceof Latte.Absyn.EOr)
    {
       Latte.Absyn.EOr _eor = (Latte.Absyn.EOr) foo;
       render("(");
       render("EOr");
       sh(_eor.expr_1);
       sh(_eor.expr_2);
       render(")");
    }
  }

  private static void sh(Latte.Absyn.ListExpr foo)
  {
     for (java.util.Iterator<Expr> it = foo.iterator(); it.hasNext();)
     {
       sh(it.next());
       if (it.hasNext())
         render(",");
     }
  }

  private static void sh(Latte.Absyn.ObjAcc foo)
  {
    if (foo instanceof Latte.Absyn.ObjFieldAcc)
    {
       Latte.Absyn.ObjFieldAcc _objfieldacc = (Latte.Absyn.ObjFieldAcc) foo;
       render("(");
       render("ObjFieldAcc");
       sh(_objfieldacc.ident_);
       render(")");
    }
    if (foo instanceof Latte.Absyn.ObjMethAcc)
    {
       Latte.Absyn.ObjMethAcc _objmethacc = (Latte.Absyn.ObjMethAcc) foo;
       render("(");
       render("ObjMethAcc");
       sh(_objmethacc.ident_);
       render("[");
       sh(_objmethacc.listexpr_);
       render("]");
       render(")");
    }
  }

  private static void sh(Latte.Absyn.AddOp foo)
  {
    if (foo instanceof Latte.Absyn.Plus)
    {
       Latte.Absyn.Plus _plus = (Latte.Absyn.Plus) foo;
       render("Plus");
    }
    if (foo instanceof Latte.Absyn.Minus)
    {
       Latte.Absyn.Minus _minus = (Latte.Absyn.Minus) foo;
       render("Minus");
    }
  }

  private static void sh(Latte.Absyn.MulOp foo)
  {
    if (foo instanceof Latte.Absyn.Times)
    {
       Latte.Absyn.Times _times = (Latte.Absyn.Times) foo;
       render("Times");
    }
    if (foo instanceof Latte.Absyn.Div)
    {
       Latte.Absyn.Div _div = (Latte.Absyn.Div) foo;
       render("Div");
    }
    if (foo instanceof Latte.Absyn.Mod)
    {
       Latte.Absyn.Mod _mod = (Latte.Absyn.Mod) foo;
       render("Mod");
    }
  }

  private static void sh(Latte.Absyn.RelOp foo)
  {
    if (foo instanceof Latte.Absyn.LTH)
    {
       Latte.Absyn.LTH _lth = (Latte.Absyn.LTH) foo;
       render("LTH");
    }
    if (foo instanceof Latte.Absyn.LE)
    {
       Latte.Absyn.LE _le = (Latte.Absyn.LE) foo;
       render("LE");
    }
    if (foo instanceof Latte.Absyn.GTH)
    {
       Latte.Absyn.GTH _gth = (Latte.Absyn.GTH) foo;
       render("GTH");
    }
    if (foo instanceof Latte.Absyn.GE)
    {
       Latte.Absyn.GE _ge = (Latte.Absyn.GE) foo;
       render("GE");
    }
    if (foo instanceof Latte.Absyn.EQU)
    {
       Latte.Absyn.EQU _equ = (Latte.Absyn.EQU) foo;
       render("EQU");
    }
    if (foo instanceof Latte.Absyn.NE)
    {
       Latte.Absyn.NE _ne = (Latte.Absyn.NE) foo;
       render("NE");
    }
  }


  private static void pp(Integer n, int _i_) { buf_.append(n); buf_.append(" "); }
  private static void pp(Double d, int _i_) { buf_.append(d); buf_.append(" "); }
  private static void pp(String s, int _i_) { buf_.append(s); buf_.append(" "); }
  private static void pp(Character c, int _i_) { buf_.append("'" + c.toString() + "'"); buf_.append(" "); }
  private static void sh(Integer n) { render(n.toString()); }
  private static void sh(Double d) { render(d.toString()); }
  private static void sh(Character c) { render(c.toString()); }
  private static void sh(String s) { printQuoted(s); }
  private static void printQuoted(String s) { render("\"" + s + "\""); }
  private static void indent()
  {
    int n = _n_;
    while (n > 0)
    {
      buf_.append(" ");
      n--;
    }
  }
  private static void backup()
  {
     if (buf_.charAt(buf_.length() - 1) == ' ') {
      buf_.setLength(buf_.length() - 1);
    }
  }
  private static void trim()
  {
     while (buf_.length() > 0 && buf_.charAt(0) == ' ')
        buf_.deleteCharAt(0); 
    while (buf_.length() > 0 && buf_.charAt(buf_.length()-1) == ' ')
        buf_.deleteCharAt(buf_.length()-1);
  }
  private static int _n_ = 0;
  private static StringBuilder buf_ = new StringBuilder(INITIAL_BUFFER_SIZE);
}

