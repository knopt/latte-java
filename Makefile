export CLASSPATH=.:lib/java-cup-11b-runtime.jar:.:lib/commons-lang3-3.8.1.jar

JAVAC=javac
JAVAC_FLAGS=-sourcepath .
JAVA=java
JAVA_FLAGS=
PARSER=${JAVA} ${JAVA_FLAGS} -jar lib/java-cup-11b.jar
PARSER_FLAGS=-locations -expect 200
LEXER=${JAVA} ${JAVA_FLAGS} lib.JLex.Main
LEXER_FLAGS=
all: test

test: absyn own src/Yylex.class src/PrettyPrinter.class src/Test.class src/parser.class src/sym.class src/Test.class

.PHONY: absyn

%.class: %.java
	${JAVAC} ${JAVAC_FLAGS} $^

own:  src/Exceptions/*.java src/Definitions/*.java src/Frontend/*.java src/Generate.java src/Backend/**/*.java
	${JAVAC} ${JAVAC_FLAGS} $^

absyn: src/Absyn/Program.java src/Absyn/ProgramTD.java src/Absyn/TopDef.java src/Absyn/FnDef.java src/Absyn/ClassDecl.java src/Absyn/ListTopDef.java src/Absyn/Arg.java src/Absyn/ArgTI.java src/Absyn/ListArg.java src/Absyn/ClassHeader.java src/Absyn/ClassDec.java src/Absyn/InterDec.java src/Absyn/Implements.java src/Absyn/InterImpl.java src/Absyn/EImpl.java src/Absyn/FieldDeclaration.java src/Absyn/Dvar.java src/Absyn/Dmth.java src/Absyn/ListFieldDeclaration.java src/Absyn/MethodBody.java src/Absyn/EmptyMBody.java src/Absyn/MBody.java src/Absyn/Modifier.java src/Absyn/Mfinal.java src/Absyn/Mpublic.java src/Absyn/Mprivate.java src/Absyn/Mstatic.java src/Absyn/MEmpty.java src/Absyn/ListModifier.java src/Absyn/Block.java src/Absyn/BlockS.java src/Absyn/ListStmt.java src/Absyn/Stmt.java src/Absyn/Empty.java src/Absyn/BStmt.java src/Absyn/Decl.java src/Absyn/Ass.java src/Absyn/Incr.java src/Absyn/Decr.java src/Absyn/Ret.java src/Absyn/VRet.java src/Absyn/Cond.java src/Absyn/CondElse.java src/Absyn/While.java src/Absyn/ForArr.java src/Absyn/SExp.java src/Absyn/Item.java src/Absyn/NoInit.java src/Absyn/Init.java src/Absyn/ListItem.java src/Absyn/Lhs.java src/Absyn/VariableRawLhs.java src/Absyn/ArrElemLhs.java src/Absyn/BasicType.java src/Absyn/Int.java src/Absyn/Str.java src/Absyn/Bool.java src/Absyn/Void.java src/Absyn/TypeName.java src/Absyn/BuiltIn.java src/Absyn/ClassName.java src/Absyn/Type.java src/Absyn/ArrayType.java src/Absyn/TypeNameS.java src/Absyn/ListType.java src/Absyn/Expr.java src/Absyn/EVar.java src/Absyn/ELitInt.java src/Absyn/ELitTrue.java src/Absyn/ELitFalse.java src/Absyn/EThis.java src/Absyn/ENull.java src/Absyn/EApp.java src/Absyn/EString.java src/Absyn/EConstr.java src/Absyn/EArrConstr.java src/Absyn/ENDArrAcc.java src/Absyn/EObjAcc.java src/Absyn/Neg.java src/Absyn/Not.java src/Absyn/EMul.java src/Absyn/EAdd.java src/Absyn/ERel.java src/Absyn/EAnd.java src/Absyn/EOr.java src/Absyn/ListExpr.java src/Absyn/ObjAcc.java src/Absyn/ObjFieldAcc.java src/Absyn/ObjMethAcc.java src/Absyn/AddOp.java src/Absyn/Plus.java src/Absyn/Minus.java src/Absyn/MulOp.java src/Absyn/Times.java src/Absyn/Div.java src/Absyn/Mod.java src/Absyn/RelOp.java src/Absyn/LTH.java src/Absyn/LE.java src/Absyn/GTH.java src/Absyn/GE.java src/Absyn/EQU.java src/Absyn/NE.java
	${JAVAC} ${JAVAC_FLAGS} $^

src/Yylex.java: src/Yylex
	${LEXER} ${LEXER_FLAGS} src/Yylex

src/parser.java src/sym.java: src/_cup.cup
	${PARSER} ${PARSER_FLAGS} src/_cup.cup
	mv parser.java sym.java src/

src/Yylex.class: src/Yylex.java src/parser.java src/sym.java

src/sym.class: src/sym.java

src/parser.class: src/parser.java src/sym.java

src/PrettyPrinter.class: src/PrettyPrinter.java

clean:
	rm -f src/Absyn/*.class src/*.class src/Exceptions/*.class src/Definitions/*.class src/Frontend/*.class src/Backend/*.class src/Backend/Instructions/*.class src/Check.class src/Generate.class

distclean: vclean

vclean:
	 rm -f src/Absyn/Program.java src/Absyn/ProgramTD.java src/Absyn/TopDef.java src/Absyn/FnDef.java src/Absyn/ClassDecl.java src/Absyn/ListTopDef.java src/Absyn/Arg.java src/Absyn/ArgTI.java src/Absyn/ListArg.java src/Absyn/ClassHeader.java src/Absyn/ClassDec.java src/Absyn/InterDec.java src/Absyn/Implements.java src/Absyn/InterImpl.java src/Absyn/EImpl.java src/Absyn/FieldDeclaration.java src/Absyn/Dvar.java src/Absyn/Dmth.java src/Absyn/ListFieldDeclaration.java src/Absyn/MethodBody.java src/Absyn/EmptyMBody.java src/Absyn/MBody.java src/Absyn/Modifier.java src/Absyn/Mfinal.java src/Absyn/Mpublic.java src/Absyn/Mprivate.java src/Absyn/Mstatic.java src/Absyn/MEmpty.java src/Absyn/ListModifier.java src/Absyn/Block.java src/Absyn/BlockS.java src/Absyn/ListStmt.java src/Absyn/Stmt.java src/Absyn/Empty.java src/Absyn/BStmt.java src/Absyn/Decl.java src/Absyn/Ass.java src/Absyn/Incr.java src/Absyn/Decr.java src/Absyn/Ret.java src/Absyn/VRet.java src/Absyn/Cond.java src/Absyn/CondElse.java src/Absyn/While.java src/Absyn/ForArr.java src/Absyn/SExp.java src/Absyn/Item.java src/Absyn/NoInit.java src/Absyn/Init.java src/Absyn/ListItem.java src/Absyn/Lhs.java src/Absyn/VariableRawLhs.java src/Absyn/ArrElemLhs.java src/Absyn/BasicType.java src/Absyn/Int.java src/Absyn/Str.java src/Absyn/Bool.java src/Absyn/Void.java src/Absyn/TypeName.java src/Absyn/BuiltIn.java src/Absyn/ClassName.java src/Absyn/Type.java src/Absyn/ArrayType.java src/Absyn/TypeNameS.java src/Absyn/ListType.java src/Absyn/Expr.java src/Absyn/EVar.java src/Absyn/ELitInt.java src/Absyn/ELitTrue.java src/Absyn/ELitFalse.java src/Absyn/EThis.java src/Absyn/ENull.java src/Absyn/EApp.java src/Absyn/EString.java src/Absyn/EConstr.java src/Absyn/EArrConstr.java src/Absyn/ENDArrAcc.java src/Absyn/EObjAcc.java src/Absyn/Neg.java src/Absyn/Not.java src/Absyn/EMul.java src/Absyn/EAdd.java src/Absyn/ERel.java src/Absyn/EAnd.java src/Absyn/EOr.java src/Absyn/ListExpr.java src/Absyn/ObjAcc.java src/Absyn/ObjFieldAcc.java src/Absyn/ObjMethAcc.java src/Absyn/AddOp.java src/Absyn/Plus.java src/Absyn/Minus.java src/Absyn/MulOp.java src/Absyn/Times.java src/Absyn/Div.java src/Absyn/Mod.java src/Absyn/RelOp.java src/Absyn/LTH.java src/Absyn/LE.java src/Absyn/GTH.java src/Absyn/GE.java src/Absyn/EQU.java src/Absyn/NE.java src/Absyn/Program.class src/Absyn/ProgramTD.class src/Absyn/TopDef.class src/Absyn/FnDef.class src/Absyn/ClassDecl.class src/Absyn/ListTopDef.class src/Absyn/Arg.class src/Absyn/ArgTI.class src/Absyn/ListArg.class src/Absyn/ClassHeader.class src/Absyn/ClassDec.class src/Absyn/InterDec.class src/Absyn/Implements.class src/Absyn/InterImpl.class src/Absyn/EImpl.class src/Absyn/FieldDeclaration.class src/Absyn/Dvar.class src/Absyn/Dmth.class src/Absyn/ListFieldDeclaration.class src/Absyn/MethodBody.class src/Absyn/EmptyMBody.class src/Absyn/MBody.class src/Absyn/Modifier.class src/Absyn/Mfinal.class src/Absyn/Mpublic.class src/Absyn/Mprivate.class src/Absyn/Mstatic.class src/Absyn/MEmpty.class src/Absyn/ListModifier.class src/Absyn/Block.class src/Absyn/BlockS.class src/Absyn/ListStmt.class src/Absyn/Stmt.class src/Absyn/Empty.class src/Absyn/BStmt.class src/Absyn/Decl.class src/Absyn/Ass.class src/Absyn/Incr.class src/Absyn/Decr.class src/Absyn/Ret.class src/Absyn/VRet.class src/Absyn/Cond.class src/Absyn/CondElse.class src/Absyn/While.class src/Absyn/ForArr.class src/Absyn/SExp.class src/Absyn/Item.class src/Absyn/NoInit.class src/Absyn/Init.class src/Absyn/ListItem.class src/Absyn/Lhs.class src/Absyn/VariableRawLhs.class src/Absyn/ArrElemLhs.class src/Absyn/BasicType.class src/Absyn/Int.class src/Absyn/Str.class src/Absyn/Bool.class src/Absyn/Void.class src/Absyn/TypeName.class src/Absyn/BuiltIn.class src/Absyn/ClassName.class src/Absyn/Type.class src/Absyn/ArrayType.class src/Absyn/TypeNameS.class src/Absyn/ListType.class src/Absyn/Expr.class src/Absyn/EVar.class src/Absyn/ELitInt.class src/Absyn/ELitTrue.class src/Absyn/ELitFalse.class src/Absyn/EThis.class src/Absyn/ENull.class src/Absyn/EApp.class src/Absyn/EString.class src/Absyn/EConstr.class src/Absyn/EArrConstr.class src/Absyn/ENDArrAcc.class src/Absyn/EObjAcc.class src/Absyn/Neg.class src/Absyn/Not.class src/Absyn/EMul.class src/Absyn/EAdd.class src/Absyn/ERel.class src/Absyn/EAnd.class src/Absyn/EOr.class src/Absyn/ListExpr.class src/Absyn/ObjAcc.class src/Absyn/ObjFieldAcc.class src/Absyn/ObjMethAcc.class src/Absyn/AddOp.class src/Absyn/Plus.class src/Absyn/Minus.class src/Absyn/MulOp.class src/Absyn/Times.class src/Absyn/Div.class src/Absyn/Mod.class src/Absyn/RelOp.class src/Absyn/LTH.class src/Absyn/LE.class src/Absyn/GTH.class src/Absyn/GE.class src/Absyn/EQU.class src/Absyn/NE.class
	 rm -f src/Absyn/*.class
	 rmdir src/Absyn/
	 rm -f src/Yylex src/_cup.cup src/Yylex.java src/VisitSkel.java src/ComposVisitor.java src/AbstractVisitor.java src/FoldVisitor.java src/AllVisitor.java src/PrettyPrinter.java src/Skeleton.java src/Test.java src/parser.java src/sym.java src/*.class
	 rm -f Makefile
	 rmdir -p src/
