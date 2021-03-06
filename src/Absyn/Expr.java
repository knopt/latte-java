package src.Absyn; // Java Package generated by the BNF Converter.

import src.Definitions.TypeDefinition;

import java.util.function.Function;

public abstract class Expr implements java.io.Serializable {

    public TypeDefinition type;

    public abstract <T> T match(Function<EVar, T> eVar,
                                Function<ELitInt, T> eLitInt,
                                Function<ELitTrue, T> eLitTrue,
                                Function<ELitFalse, T> eLitFalse,
                                Function<EThis, T> eThis,
                                Function<ENull, T> eNull,
                                Function<EApp, T> eApp,
                                Function<EString, T> eString,
                                Function<EConstr, T> eConstr,
                                Function<EArrConstr, T> eArrConstr,
                                Function<ENDArrAcc, T> eNDArrAcc,
                                Function<Neg, T> neg,
                                Function<Not, T> not,
                                Function<EMul, T> eMul,
                                Function<EAdd, T> eAdd,
                                Function<ERel, T> eRel,
                                Function<EAnd, T> eAnd,
                                Function<EOr, T> eOr,
                                Function<EObjAcc, T> eObjAcc,
                                Function<ECast, T> eCast);

}
