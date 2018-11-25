export CLASSPATH=$CLASSPATH:bnfc/testing/data/java-cup-11b-runtime.jar:.


JAVAC=javac
JAVAC_FLAGS=-sourcepath .
JAVA=java
JAVA_FLAGS=
PARSER=${JAVA} ${JAVA_FLAGS} -jar java-cup-11b.jar
PARSER_FLAGS=-locations -expect 200
LEXER=${JAVA} ${JAVA_FLAGS} JLex.Main
LEXER_FLAGS=
all: test

test: absyn Latte/Yylex.class Latte/PrettyPrinter.class Latte/Test.class Latte/parser.class Latte/sym.class Latte/Test.class

.PHONY: absyn

%.class: %.java
	${JAVAC} ${JAVAC_FLAGS} $^

absyn: Latte/Absyn/Program.java Latte/Absyn/ProgramTD.java Latte/Absyn/TopDef.java Latte/Absyn/FnDef.java Latte/Absyn/ClassDecl.java Latte/Absyn/ListTopDef.java Latte/Absyn/Arg.java Latte/Absyn/ArgTI.java Latte/Absyn/ListArg.java Latte/Absyn/ClassHeader.java Latte/Absyn/ClassDec.java Latte/Absyn/InterDec.java Latte/Absyn/FieldDeclaration.java Latte/Absyn/Dvar.java Latte/Absyn/Dmth.java Latte/Absyn/ListFieldDeclaration.java Latte/Absyn/MethodBody.java Latte/Absyn/EmptyMBody.java Latte/Absyn/MBody.java Latte/Absyn/Modifier.java Latte/Absyn/Mfinal.java Latte/Absyn/Mpublic.java Latte/Absyn/Mprivate.java Latte/Absyn/Mstatic.java Latte/Absyn/MEmpty.java Latte/Absyn/ListModifier.java Latte/Absyn/Block.java Latte/Absyn/BlockS.java Latte/Absyn/ListStmt.java Latte/Absyn/Stmt.java Latte/Absyn/Empty.java Latte/Absyn/BStmt.java Latte/Absyn/Decl.java Latte/Absyn/Ass.java Latte/Absyn/Incr.java Latte/Absyn/Decr.java Latte/Absyn/Ret.java Latte/Absyn/VRet.java Latte/Absyn/Cond.java Latte/Absyn/CondElse.java Latte/Absyn/While.java Latte/Absyn/ForArr.java Latte/Absyn/SExp.java Latte/Absyn/Item.java Latte/Absyn/NoInit.java Latte/Absyn/Init.java Latte/Absyn/ListItem.java Latte/Absyn/Lhs.java Latte/Absyn/VariableRawLhs.java Latte/Absyn/ArrElemLhs.java Latte/Absyn/BasicType.java Latte/Absyn/Int.java Latte/Absyn/Str.java Latte/Absyn/Bool.java Latte/Absyn/Void.java Latte/Absyn/TypeName.java Latte/Absyn/BuiltIn.java Latte/Absyn/ClassName.java Latte/Absyn/Type.java Latte/Absyn/ArrayType.java Latte/Absyn/TypeNameS.java Latte/Absyn/ListType.java Latte/Absyn/Expr.java Latte/Absyn/EVar.java Latte/Absyn/ELitInt.java Latte/Absyn/ELitTrue.java Latte/Absyn/ELitFalse.java Latte/Absyn/EThis.java Latte/Absyn/ENull.java Latte/Absyn/EApp.java Latte/Absyn/EString.java Latte/Absyn/EConstr.java Latte/Absyn/EArrConstr.java Latte/Absyn/ENDArrAcc.java Latte/Absyn/EObjAcc.java Latte/Absyn/Neg.java Latte/Absyn/Not.java Latte/Absyn/EMul.java Latte/Absyn/EAdd.java Latte/Absyn/ERel.java Latte/Absyn/EAnd.java Latte/Absyn/EOr.java Latte/Absyn/ListExpr.java Latte/Absyn/ObjAcc.java Latte/Absyn/ObjFieldAcc.java Latte/Absyn/ObjMethAcc.java Latte/Absyn/AddOp.java Latte/Absyn/Plus.java Latte/Absyn/Minus.java Latte/Absyn/MulOp.java Latte/Absyn/Times.java Latte/Absyn/Div.java Latte/Absyn/Mod.java Latte/Absyn/RelOp.java Latte/Absyn/LTH.java Latte/Absyn/LE.java Latte/Absyn/GTH.java Latte/Absyn/GE.java Latte/Absyn/EQU.java Latte/Absyn/NE.java
	${JAVAC} ${JAVAC_FLAGS} $^

Latte/Yylex.java: Latte/Yylex
	${LEXER} ${LEXER_FLAGS} Latte/Yylex

Latte/parser.java Latte/sym.java: Latte/_cup.cup
	${PARSER} ${PARSER_FLAGS} Latte/_cup.cup
	mv parser.java sym.java Latte/

Latte/Yylex.class: Latte/Yylex.java Latte/parser.java Latte/sym.java

Latte/sym.class: Latte/sym.java

Latte/parser.class: Latte/parser.java Latte/sym.java

Latte/PrettyPrinter.class: Latte/PrettyPrinter.java

clean:
	rm -f Latte/Absyn/*.class Latte/*.class

distclean: vclean

vclean:
	 rm -f Latte/Absyn/Program.java Latte/Absyn/ProgramTD.java Latte/Absyn/TopDef.java Latte/Absyn/FnDef.java Latte/Absyn/ClassDecl.java Latte/Absyn/ListTopDef.java Latte/Absyn/Arg.java Latte/Absyn/ArgTI.java Latte/Absyn/ListArg.java Latte/Absyn/ClassHeader.java Latte/Absyn/ClassDec.java Latte/Absyn/InterDec.java Latte/Absyn/FieldDeclaration.java Latte/Absyn/Dvar.java Latte/Absyn/Dmth.java Latte/Absyn/ListFieldDeclaration.java Latte/Absyn/MethodBody.java Latte/Absyn/EmptyMBody.java Latte/Absyn/MBody.java Latte/Absyn/Modifier.java Latte/Absyn/Mfinal.java Latte/Absyn/Mpublic.java Latte/Absyn/Mprivate.java Latte/Absyn/Mstatic.java Latte/Absyn/MEmpty.java Latte/Absyn/ListModifier.java Latte/Absyn/Block.java Latte/Absyn/BlockS.java Latte/Absyn/ListStmt.java Latte/Absyn/Stmt.java Latte/Absyn/Empty.java Latte/Absyn/BStmt.java Latte/Absyn/Decl.java Latte/Absyn/Ass.java Latte/Absyn/Incr.java Latte/Absyn/Decr.java Latte/Absyn/Ret.java Latte/Absyn/VRet.java Latte/Absyn/Cond.java Latte/Absyn/CondElse.java Latte/Absyn/While.java Latte/Absyn/SExp.java Latte/Absyn/Item.java Latte/Absyn/NoInit.java Latte/Absyn/Init.java Latte/Absyn/ListItem.java Latte/Absyn/Lhs.java Latte/Absyn/VariableRawLhs.java Latte/Absyn/ArrElemLhs.java Latte/Absyn/BasicType.java Latte/Absyn/Int.java Latte/Absyn/Str.java Latte/Absyn/Bool.java Latte/Absyn/Void.java Latte/Absyn/TypeName.java Latte/Absyn/BuiltIn.java Latte/Absyn/ClassName.java Latte/Absyn/Type.java Latte/Absyn/ArrayType.java Latte/Absyn/TypeNameS.java Latte/Absyn/ListType.java Latte/Absyn/Expr.java Latte/Absyn/EVar.java Latte/Absyn/ELitInt.java Latte/Absyn/ELitTrue.java Latte/Absyn/ELitFalse.java Latte/Absyn/EThis.java Latte/Absyn/ENull.java Latte/Absyn/EApp.java Latte/Absyn/EString.java Latte/Absyn/EConstr.java Latte/Absyn/EArrConstr.java Latte/Absyn/ENDArrAcc.java Latte/Absyn/EObjAcc.java Latte/Absyn/Neg.java Latte/Absyn/Not.java Latte/Absyn/EMul.java Latte/Absyn/EAdd.java Latte/Absyn/ERel.java Latte/Absyn/EAnd.java Latte/Absyn/EOr.java Latte/Absyn/ListExpr.java Latte/Absyn/ObjAcc.java Latte/Absyn/ObjFieldAcc.java Latte/Absyn/ObjMethAcc.java Latte/Absyn/AddOp.java Latte/Absyn/Plus.java Latte/Absyn/Minus.java Latte/Absyn/MulOp.java Latte/Absyn/Times.java Latte/Absyn/Div.java Latte/Absyn/Mod.java Latte/Absyn/RelOp.java Latte/Absyn/LTH.java Latte/Absyn/LE.java Latte/Absyn/GTH.java Latte/Absyn/GE.java Latte/Absyn/EQU.java Latte/Absyn/NE.java Latte/Absyn/Program.class Latte/Absyn/ProgramTD.class Latte/Absyn/TopDef.class Latte/Absyn/FnDef.class Latte/Absyn/ClassDecl.class Latte/Absyn/ListTopDef.class Latte/Absyn/Arg.class Latte/Absyn/ArgTI.class Latte/Absyn/ListArg.class Latte/Absyn/ClassHeader.class Latte/Absyn/ClassDec.class Latte/Absyn/InterDec.class Latte/Absyn/FieldDeclaration.class Latte/Absyn/Dvar.class Latte/Absyn/Dmth.class Latte/Absyn/ListFieldDeclaration.class Latte/Absyn/MethodBody.class Latte/Absyn/EmptyMBody.class Latte/Absyn/MBody.class Latte/Absyn/Modifier.class Latte/Absyn/Mfinal.class Latte/Absyn/Mpublic.class Latte/Absyn/Mprivate.class Latte/Absyn/Mstatic.class Latte/Absyn/MEmpty.class Latte/Absyn/ListModifier.class Latte/Absyn/Block.class Latte/Absyn/BlockS.class Latte/Absyn/ListStmt.class Latte/Absyn/Stmt.class Latte/Absyn/Empty.class Latte/Absyn/BStmt.class Latte/Absyn/Decl.class Latte/Absyn/Ass.class Latte/Absyn/Incr.class Latte/Absyn/Decr.class Latte/Absyn/Ret.class Latte/Absyn/VRet.class Latte/Absyn/Cond.class Latte/Absyn/CondElse.class Latte/Absyn/While.class Latte/Absyn/SExp.class Latte/Absyn/Item.class Latte/Absyn/NoInit.class Latte/Absyn/Init.class Latte/Absyn/ListItem.class Latte/Absyn/Lhs.class Latte/Absyn/VariableRawLhs.class Latte/Absyn/ArrElemLhs.class Latte/Absyn/BasicType.class Latte/Absyn/Int.class Latte/Absyn/Str.class Latte/Absyn/Bool.class Latte/Absyn/Void.class Latte/Absyn/TypeName.class Latte/Absyn/BuiltIn.class Latte/Absyn/ClassName.class Latte/Absyn/Type.class Latte/Absyn/ArrayType.class Latte/Absyn/TypeNameS.class Latte/Absyn/ListType.class Latte/Absyn/Expr.class Latte/Absyn/EVar.class Latte/Absyn/ELitInt.class Latte/Absyn/ELitTrue.class Latte/Absyn/ELitFalse.class Latte/Absyn/EThis.class Latte/Absyn/ENull.class Latte/Absyn/EApp.class Latte/Absyn/EString.class Latte/Absyn/EConstr.class Latte/Absyn/EArrConstr.class Latte/Absyn/ENDArrAcc.class Latte/Absyn/EObjAcc.class Latte/Absyn/Neg.class Latte/Absyn/Not.class Latte/Absyn/EMul.class Latte/Absyn/EAdd.class Latte/Absyn/ERel.class Latte/Absyn/EAnd.class Latte/Absyn/EOr.class Latte/Absyn/ListExpr.class Latte/Absyn/ObjAcc.class Latte/Absyn/ObjFieldAcc.class Latte/Absyn/ObjMethAcc.class Latte/Absyn/AddOp.class Latte/Absyn/Plus.class Latte/Absyn/Minus.class Latte/Absyn/MulOp.class Latte/Absyn/Times.class Latte/Absyn/Div.class Latte/Absyn/Mod.class Latte/Absyn/RelOp.class Latte/Absyn/LTH.class Latte/Absyn/LE.class Latte/Absyn/GTH.class Latte/Absyn/GE.class Latte/Absyn/EQU.class Latte/Absyn/NE.class
	 rm -f Latte/Absyn/*.class
	 rmdir Latte/Absyn/
	 rm -f Latte/Yylex Latte/_cup.cup Latte/Yylex.java Latte/VisitSkel.java Latte/ComposVisitor.java Latte/AbstractVisitor.java Latte/FoldVisitor.java Latte/AllVisitor.java Latte/PrettyPrinter.java Latte/Skeleton.java Latte/Test.java Latte/parser.java Latte/sym.java Latte/*.class
	 rm -f Makefile
	 rmdir -p Latte/
