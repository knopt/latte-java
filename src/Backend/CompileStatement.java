package src.Backend;

import src.Absyn.*;
import src.Backend.Definitions.BackendScope;
import src.Backend.Definitions.ExternalFunctions;
import src.Backend.Definitions.Register;
import src.Backend.Instructions.*;
import src.Definitions.BasicTypeDefinition;
import src.Definitions.TypeDefinition;
import src.Exceptions.CompilerException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static src.Backend.CompileExpression.generateExpr;
import static src.Backend.Instructions.ConstantUtils.THIS_KEYWORD;
import static src.Backend.Instructions.ConstantUtils.WORD_SIZE;
import static src.Frontend.TypeUtils.getType;
import static src.PrettyPrinter.print;

public class CompileStatement {

    public static List<AssemblyInstruction> generateStmt(Stmt stmt, BackendScope scope) {
        List<AssemblyInstruction> instructions = new ArrayList<>();
        instructions.addAll(stmt.match(
                (empty) -> new ArrayList<>(),
                (bStmt) -> generateBStmt(bStmt, scope),
                (decl) -> generateDecl(decl, scope),
                (ass) -> generateAss(ass, scope),
                (incr) -> generateIncr(incr, scope),
                (decr) -> generateDecr(decr, scope),
                (ret) -> generateReturn(ret, scope),
                CompileStatement::generateVReturn,
                (cond) -> generateCond(cond, scope),
                (condElse) -> generateCondElse(condElse, scope),
                (sWhile) -> generateWhile(sWhile, scope),
                (sExp) -> generateStmtExpr(sExp, scope),
                (forArr) -> generateForArr(forArr, scope)
        ));

        return instructions;
    }

    public static List<AssemblyInstruction> generateDecr(Decr decr, BackendScope scope) {
        return decr.lhs_.match(
                (var) -> generateVarDecr(var, scope),
                (arr) -> generateArrDecr(arr, scope),
                (field) -> generateFieldDecr(field, scope)
        );
    }

    public static List<AssemblyInstruction> generateIncr(Incr incr, BackendScope scope) {
        return incr.lhs_.match(
                (var) -> generateVarIncr(var, scope),
                (arr) -> generateArrIncr(arr, scope),
                (field) -> generateFieldIncr(field, scope)
        );
    }

    public static List<AssemblyInstruction> generateVarIncr(VariableRawLhs var, BackendScope scope) {
        List<AssemblyInstruction> instructions = new ArrayList<>();

        if (var.binding.isField()) {
            return generateFieldIncrFromVar(var.ident_, var.binding.getBindedClass(), scope);
        }

        int offset = scope.getVariable(var.ident_).getOffset() * WORD_SIZE;

        instructions.add(new MovInstruction(Register.RAX, MemoryReference.getWithOffset(Register.RBP, offset)));
        instructions.add(new AddInstruction(Register.RAX, YieldUtils.number(1)));
        instructions.add(new MovInstruction(MemoryReference.getWithOffset(Register.RBP, offset), Register.RAX));

        return instructions;
    }

    public static List<AssemblyInstruction> generateArrIncr(ArrElemLhs arrElem, BackendScope scope) {
        List<AssemblyInstruction> instructions = new ArrayList<>();

        instructions.add(new PushInstruction(Register.RCX, scope));

        instructions.addAll(generateExpr(arrElem.expr_2, Register.RAX, Register.RAX, scope));
        instructions.add(new AddInstruction(Register.RAX, YieldUtils.number(1)));
        instructions.add(new MulInstruction(Register.RAX, YieldUtils.number(WORD_SIZE)));
        instructions.add(new MovInstruction(Register.RCX, Register.RAX));
        instructions.addAll(generateExpr(arrElem.expr_1, Register.RAX, Register.RAX, scope));

        instructions.add(new AddInstruction(Register.RAX, Register.RCX));

        instructions.add(new MovInstruction(Register.RCX, MemoryReference.getRaw(Register.RAX)));
        instructions.add(new AddInstruction(Register.RCX, YieldUtils.number(1)));
        instructions.add(new MovInstruction(MemoryReference.getRaw(Register.RAX), Register.RCX));

        instructions.add(new PopInstruction(Register.RCX, scope));

        return instructions;
    }

    public static List<AssemblyInstruction> generateVarDecr(VariableRawLhs var, BackendScope scope) {
        List<AssemblyInstruction> instructions = new ArrayList<>();

        if (var.binding.isField()) {
            return generateFieldDecrFromVar(var.ident_, var.binding.getBindedClass(), scope);
        }

        int offset = scope.getVariable(var.ident_).getOffset() * WORD_SIZE;

        instructions.add(new MovInstruction(Register.RAX, MemoryReference.getWithOffset(Register.RBP, offset)));
        instructions.add(new SubInstruction(Register.RAX, YieldUtils.number(1)));
        instructions.add(new MovInstruction(MemoryReference.getWithOffset(Register.RBP, offset), Register.RAX));

        return instructions;
    }

    public static List<AssemblyInstruction> generateArrDecr(ArrElemLhs arrElem, BackendScope scope) {
        List<AssemblyInstruction> instructions = new ArrayList<>();

        instructions.add(new PushInstruction(Register.RCX, scope));

        instructions.addAll(generateExpr(arrElem.expr_2, Register.RAX, Register.RAX, scope));
        instructions.add(new AddInstruction(Register.RAX, YieldUtils.number(1)));
        instructions.add(new MulInstruction(Register.RAX, YieldUtils.number(WORD_SIZE)));
        instructions.add(new MovInstruction(Register.RCX, Register.RAX));
        instructions.addAll(generateExpr(arrElem.expr_1, Register.RAX, Register.RAX, scope));

        instructions.add(new AddInstruction(Register.RAX, Register.RCX));

        instructions.add(new MovInstruction(Register.RCX, MemoryReference.getRaw(Register.RAX)));
        instructions.add(new SubInstruction(Register.RCX, YieldUtils.number(1)));
        instructions.add(new MovInstruction(MemoryReference.getRaw(Register.RAX), Register.RCX));

        instructions.add(new PopInstruction(Register.RCX, scope));

        return instructions;
    }

    public static List<AssemblyInstruction> generateFieldDecr(FieldLhs fieldLhs, BackendScope scope) {
        List<AssemblyInstruction> instructions = new ArrayList<>();
        int offset = fieldLhs.expr_.type.getClassDefinition().getFieldOffset(fieldLhs.ident_);

        instructions.add(new PushInstruction(Register.RCX, scope));

        instructions.addAll(generateExpr(fieldLhs.expr_, Register.RAX, Register.RAX, scope));
        instructions.add(new MovInstruction(Register.RCX, MemoryReference.getWithOffset(Register.RAX, offset)));
        instructions.add(new SubInstruction(Register.RCX, YieldUtils.number(1)));
        instructions.add(new MovInstruction(MemoryReference.getWithOffset(Register.RAX, offset), Register.RCX));

        instructions.add(new PopInstruction(Register.RCX, scope));

        return instructions;
    }

    public static List<AssemblyInstruction> generateFieldIncr(FieldLhs fieldLhs, BackendScope scope) {
        List<AssemblyInstruction> instructions = new ArrayList<>();
        int offset = fieldLhs.expr_.type.getClassDefinition().getFieldOffset(fieldLhs.ident_);

        instructions.add(new PushInstruction(Register.RCX, scope));

        instructions.addAll(generateExpr(fieldLhs.expr_, Register.RAX, Register.RAX, scope));
        instructions.add(new MovInstruction(Register.RCX, MemoryReference.getWithOffset(Register.RAX, offset)));
        instructions.add(new AddInstruction(Register.RCX, YieldUtils.number(1)));
        instructions.add(new MovInstruction(MemoryReference.getWithOffset(Register.RAX, offset), Register.RCX));

        instructions.add(new PopInstruction(Register.RCX, scope));

        return instructions;
    }

    public static List<AssemblyInstruction> generateFieldChangeFromVar(String fieldName, TypeDefinition lhsType, BackendScope scope, boolean incr) {
        List<AssemblyInstruction> instructions = new ArrayList<>();

        int thisOffset = scope.getVariable(THIS_KEYWORD).getOffset() * WORD_SIZE;
        int fieldOffset = lhsType.getClassDefinition().getFieldOffset(fieldName);

        instructions.add(new PushInstruction(Register.RCX, scope));

        instructions.add(new MovInstruction(Register.RCX, MemoryReference.getWithOffset(Register.RBP, thisOffset)));
        instructions.add(new AddInstruction(Register.RCX, YieldUtils.number(fieldOffset)));
        instructions.add(new MovInstruction(Register.RAX, MemoryReference.getRaw(Register.RCX)));

        if (incr) {
            instructions.add(new AddInstruction(Register.RAX, YieldUtils.number(1)));
        } else {
            instructions.add(new SubInstruction(Register.RAX, YieldUtils.number(1)));
        }

        instructions.add(new MovInstruction(MemoryReference.getRaw(Register.RCX), Register.RAX));
        instructions.add(new PopInstruction(Register.RCX, scope));

        return instructions;
    }

    public static List<AssemblyInstruction> generateFieldIncrFromVar(String fieldName, TypeDefinition lhsType, BackendScope scope) {
        return generateFieldChangeFromVar(fieldName, lhsType, scope, true);
    }

    public static List<AssemblyInstruction> generateFieldDecrFromVar(String fieldName, TypeDefinition lhsType, BackendScope scope) {
        return generateFieldChangeFromVar(fieldName, lhsType, scope, false);
    }

    public static List<AssemblyInstruction> generateDecl(Decl decl, BackendScope scope) {
        List<AssemblyInstruction> instructions = new ArrayList<>();

        instructions.add(new Comment(""));
        instructions.add(new Comment(" declaration " + print(decl)));

        TypeDefinition type = decl.type_.match(
                (arrayType) -> getType(arrayType, scope.getGlobalEnvironment()),
                (typeNameS) -> getType(typeNameS, scope.getGlobalEnvironment())
        );


        for (Item item : decl.listitem_) {
            instructions.addAll(item.match(
                    (noInit) -> generateNoInit(noInit, type, scope),
                    (init) -> generateInit(init, type, scope)
            ));
        }

        return instructions;
    }

    public static List<AssemblyInstruction> generateStmtExpr(SExp exp, BackendScope scope) {
        return generateExpr(exp.expr_, Register.RAX, Register.RAX, scope);
    }

    public static List<AssemblyInstruction> generateWhile(While sWhile, BackendScope scope) {
        List<AssemblyInstruction> instructions = new ArrayList<>();

        Label start = LabelsGenerator.getNonceLabel("_start_while");
        Label end = LabelsGenerator.getNonceLabel("_end_start");

        instructions.add(start);
        instructions.addAll(generateExpr(sWhile.expr_, Register.RAX, Register.RAX, scope));
        instructions.add(new CompareInstruction(Register.RAX, YieldUtils.number(1)));
        instructions.add(new JumpInstruction(end, JumpInstruction.Type.NE));

        instructions.addAll(generateStmt(sWhile.stmt_, scope));
        instructions.add(new JumpInstruction(start, JumpInstruction.Type.ALWAYS));
        instructions.add(end);

        return instructions;
    }

    public static List<AssemblyInstruction> generateCond(Cond cond, BackendScope scope) {
        List<AssemblyInstruction> instructions = generateExpr(cond.expr_, Register.RAX, Register.RAX, scope);

        Label afterLabel = LabelsGenerator.getNonceLabel("_end_if");

        instructions.add(new CompareInstruction(Register.RAX, YieldUtils.number(1)));
        instructions.add(new JumpInstruction(afterLabel, JumpInstruction.Type.NE));

        instructions.addAll(generateStmt(cond.stmt_, scope));
        instructions.add(afterLabel);

        return instructions;
    }

    public static List<AssemblyInstruction> generateCondElse(CondElse cond, BackendScope scope) {
        List<AssemblyInstruction> instructions = generateExpr(cond.expr_, Register.RAX, Register.RAX, scope);

        Label afterLabel = LabelsGenerator.getNonceLabel("_end_if");
        Label elseLabel = LabelsGenerator.getNonceLabel("_else");

        instructions.add(new CompareInstruction(Register.RAX, YieldUtils.number(1)));
        instructions.add(new JumpInstruction(elseLabel, JumpInstruction.Type.NE));

        instructions.addAll(generateStmt(cond.stmt_1, scope));
        instructions.add(new JumpInstruction(afterLabel, JumpInstruction.Type.ALWAYS));

        instructions.add(elseLabel);
        instructions.addAll(generateStmt(cond.stmt_2, scope));
        instructions.add(afterLabel);

        return instructions;
    }


    public static List<AssemblyInstruction> generateBStmt(BStmt bStmt, BackendScope oldScope) {
        BackendScope scope = new BackendScope(oldScope);

        List<AssemblyInstruction> instructions = new ArrayList<>();

        instructions.add(new Comment(""));
        instructions.add(new Comment("block"));

        instructions.addAll(bStmt.block_.match(
                (blockS) -> generateBlockS(blockS, scope)
        ));

        instructions.add(new Comment(""));
        instructions.add(new Comment("end block"));

        return instructions;

    }

    public static List<AssemblyInstruction> generateBlockS(BlockS block, BackendScope scope) {
        List<AssemblyInstruction> instructions = new ArrayList<>();

        for (Stmt stmt : block.liststmt_) {
            instructions.addAll(generateStmt(stmt, scope));
        }

        return instructions;
    }

    public static List<AssemblyInstruction> generateAss(Ass ass, BackendScope scope) {
        List<AssemblyInstruction> instructions = new ArrayList<>();

        instructions.add(new Comment(""));
        instructions.add(new Comment("assignment " + print(ass)));

        instructions.addAll(generateExpr(ass.expr_, Register.RAX, Register.RAX, scope));

        instructions.addAll(ass.lhs_.match(
                (var) -> generateVariableAss(var, ass.expr_.type, Register.RAX, scope),
                (arr) -> generateVariableArrayAss(arr, ass.expr_.type, Register.RAX, scope),
                (field) -> generateFieldAss(field, ass.expr_.type, Register.RAX, scope)
        ));

        return instructions;
    }

    public static List<AssemblyInstruction> generateVariableAss(VariableRawLhs var, TypeDefinition rhsType, String sourceRegister, BackendScope scope) {
        if (var.binding.isField()) {
            return generateFieldAssFromVar(var, sourceRegister, scope);
        }

        int varOffset = scope.getVariable(var.ident_).getOffset() * WORD_SIZE;

        List<AssemblyInstruction> instructions = new ArrayList<>();

        if (scope.getVariable(var.ident_).getType().isInterfaceType() && rhsType.isClassType()) {
            // TODO: implement interfaces assignment
        }



        AssemblyInstruction instruction = new MovInstruction(MemoryReference.getWithOffset(Register.RBP, varOffset), sourceRegister);

        return new ArrayList<>(Collections.singletonList(instruction));
    }

    public static List<AssemblyInstruction> generateFieldAss(FieldLhs fieldLhs, TypeDefinition rhsType, String sourceRegister, BackendScope scope) {
        List<AssemblyInstruction> instructions = new ArrayList<>();
        int offset = fieldLhs.expr_.type.getClassDefinition().getFieldOffset(fieldLhs.ident_);

        TypeDefinition fieldType = fieldLhs.expr_.type.getClassDefinition().getFieldDeclaration(fieldLhs.ident_, -1, -1).getType();

        if (fieldType.isInterfaceType() && rhsType.isClassType()) {
            // TODO: imeplemnt interfaces from class assignment
        }

        instructions.add(new PushInstruction(Register.RCX, scope));

        instructions.add(new MovInstruction(Register.RCX, Register.RAX));
        instructions.addAll(generateExpr(fieldLhs.expr_, Register.RAX, Register.RAX, scope));
        instructions.add(new MovInstruction(MemoryReference.getWithOffset(Register.RAX, offset), Register.RCX));

        instructions.add(new PopInstruction(Register.RCX, scope));

        return instructions;
    }

    public static List<AssemblyInstruction> generateFieldAssFromVar(VariableRawLhs lhs, String sourceRegister, BackendScope scope) {
        List<AssemblyInstruction> instructions = new ArrayList<>();
        // TODO: add 1 to max stack size to fit 'this'
        // wartość do zapisania jest w RAX
        int thisOffset = scope.getVariable(THIS_KEYWORD).getOffset() * WORD_SIZE;
        int fieldOffset = lhs.binding.getBindedClass().getClassDefinition().getFieldOffset(lhs.ident_);

        instructions.add(new PushInstruction(Register.RCX, scope));

        instructions.add(new MovInstruction(Register.RCX, MemoryReference.getWithOffset(Register.RBP, thisOffset)));
        instructions.add(new AddInstruction(Register.RCX, YieldUtils.number(fieldOffset)));
        instructions.add(new MovInstruction(MemoryReference.getRaw(Register.RCX), Register.RAX));

        instructions.add(new PopInstruction(Register.RCX, scope));

        return instructions;
    }

    public static List<AssemblyInstruction> generateVariableArrayAss(ArrElemLhs arrElem, TypeDefinition rhsType, String registerOfValue, BackendScope scope) {
        List<AssemblyInstruction> instructions = new ArrayList<>();

        instructions.add(new PushInstruction(Register.RCX, scope));
        instructions.add(new PushInstruction(Register.RDX, scope));

        if (!registerOfValue.equals(Register.RCX)) {
            instructions.add(new MovInstruction(Register.RCX, registerOfValue));
        }

        instructions.addAll(generateExpr(arrElem.expr_2, Register.RAX, Register.RAX, scope));
        instructions.add(new MovInstruction(Register.RDX, Register.RAX));
        instructions.add(new AddInstruction(Register.RDX, YieldUtils.number(1)));
        instructions.add(new MulInstruction(Register.RDX, YieldUtils.number(WORD_SIZE)));
        instructions.addAll(generateExpr(arrElem.expr_1, Register.RAX, Register.RAX, scope));
        instructions.add(new AddInstruction(Register.RAX, Register.RDX));

        instructions.add(new PopInstruction(Register.RDX, scope));

        TypeDefinition innerArrayType = arrElem.expr_1.type.getArrayTypeDefinition().getInnerTypeDefinition();

        if (innerArrayType.isInterfaceType() && arrElem.expr_2.type.isClassType()) {
            //TODO: implement interface assignment from class
        }

        instructions.add(new MovInstruction(MemoryReference.getRaw(Register.RAX), Register.RCX));

        instructions.add(new PopInstruction(Register.RCX, scope));

        return instructions;
    }

    public static List<AssemblyInstruction> generateNoInit(NoInit noInit, TypeDefinition type, BackendScope scope) {
        List<AssemblyInstruction> instructions = new ArrayList<>();

        scope.declareVariable(noInit.ident_, type);
        int offset = scope.getVariable(noInit.ident_).getOffset() * WORD_SIZE;

        if (BasicTypeDefinition.STRING.equals(type)) {
            instructions.add(new CallInstruction(ExternalFunctions.EMPTY_STRING));
        } else {
            instructions.add(new MovInstruction(Register.RAX, YieldUtils.number(0)));
        }
        instructions.add(new MovInstruction(MemoryReference.getWithOffset(Register.RBP, offset), Register.RAX));

        return instructions;
    }

    public static List<AssemblyInstruction> generateInit(Init init, TypeDefinition type, BackendScope scope) {
        List<AssemblyInstruction> instructions = generateExpr(init.expr_, Register.RAX, Register.RAX, scope);

        if (type.isInterfaceType() && init.expr_.type.isClassType()) {
            // TODO: generate interface assignment from class type
        }

        scope.declareVariable(init.ident_, type);
        int offset = scope.getVariable(init.ident_).getOffset() * WORD_SIZE;
        instructions.add(new MovInstruction(MemoryReference.getWithOffset(Register.RBP, offset), Register.RAX));


        return instructions;
    }

    private static List<AssemblyInstruction> generateEpilog() {
        List<AssemblyInstruction> instructions = new ArrayList<>();

        instructions.add(new MovInstruction(Register.RSP, Register.RBP));
        instructions.add(new PopInstruction(Register.RBP));

        instructions.add(new Return());

        return instructions;
    }

    public static List<AssemblyInstruction> generateReturn(Ret ret, BackendScope scope) {
        List<AssemblyInstruction> instructions = new ArrayList<>(Arrays.asList(new Comment(""), new Comment("return from function")));
        instructions.addAll(generateExpr(ret.expr_, Register.RAX, Register.RAX, scope));

        instructions.addAll(generateEpilog());

        return instructions;
    }

    public static List<AssemblyInstruction> generateVReturn(VRet ret) {
        List<AssemblyInstruction> instructions = new ArrayList<>(Arrays.asList(new Comment(""), new Comment("void return from function")));

        instructions.addAll(generateEpilog());

        return instructions;
    }

    public static List<AssemblyInstruction> generateForArr(ForArr forArr, BackendScope oldScope) {
        BackendScope scope = new BackendScope(oldScope);

        List<AssemblyInstruction> instructions = new ArrayList<>();

        Label start = LabelsGenerator.getNonceLabel("_start_for_arr");
        Label end = LabelsGenerator.getNonceLabel("_end_for_arr");

        scope.declareVariable(forArr.ident_, getType(forArr.typename_, scope.getGlobalEnvironment(), -1, -1));
        int offset = scope.getVariable(forArr.ident_).getOffset() * WORD_SIZE;

        instructions.add(new PushInstruction(Register.RDX, scope));
        instructions.add(new PushInstruction(Register.RCX, scope));

        instructions.addAll(generateExpr(forArr.expr_, Register.RAX, Register.RAX, scope)); // get array beginning
        instructions.add(new MovInstruction(Register.RCX, Register.RAX));
        instructions.add(new MovInstruction(Register.RDX, MemoryReference.getRaw(Register.RCX)));

        instructions.add(start);
        instructions.add(new CompareInstruction(Register.RDX, YieldUtils.number(0)));
        instructions.add(new JumpInstruction(end, JumpInstruction.Type.EQU));

        instructions.add(new MovInstruction(Register.RAX, Register.RDX));
        instructions.add(new MulInstruction(Register.RAX, YieldUtils.number(WORD_SIZE)));
        instructions.add(new AddInstruction(Register.RAX, Register.RCX)); // add address of begining of an array
        instructions.add(new MovInstruction(Register.RAX, MemoryReference.getRaw(Register.RAX)));
        instructions.add(new MovInstruction(MemoryReference.getWithOffset(Register.RBP, offset), Register.RAX));

        instructions.addAll(generateStmt(forArr.stmt_, scope));

        instructions.add(new SubInstruction(Register.RDX, YieldUtils.number(1)));

        instructions.add(new JumpInstruction(start, JumpInstruction.Type.ALWAYS));
        instructions.add(end);

        instructions.add(new PopInstruction(Register.RCX, scope));
        instructions.add(new PopInstruction(Register.RDX, scope));

        return instructions;
    }

    public static List<AssemblyInstruction> notImplemented(Stmt stmt) {
        throw new CompilerException("Compiling statement " + stmt.getClass() + " not implemented yet");
    }


}
