package src.Backend;

import org.apache.commons.lang3.StringEscapeUtils;
import src.Absyn.*;
import src.Backend.Definitions.BackendScope;
import src.Backend.Definitions.ExternalFunctions;
import src.Backend.Definitions.Instructions;
import src.Backend.Definitions.Register;
import src.Backend.Instructions.*;
import src.Definitions.*;
import src.Exceptions.CompilerException;

import static src.Backend.CompileExpression.callUnsafeRegs;
import static src.Backend.CompileExpression.generateExpr;
import static src.Backend.Instructions.ConstantUtils.THIS_KEYWORD;
import static src.Backend.Instructions.ConstantUtils.WORD_SIZE;
import static src.Frontend.TypeUtils.getType;
import static src.PrettyPrinter.print;

public class CompileStatement {

    public static Instructions generateStmt(Stmt stmt, BackendScope scope) {
        Instructions instructions = new Instructions();

        instructions.add(new Comment("Statement " + StringEscapeUtils.escapeJava(print(stmt))));

        Instructions stmtInstr = stmt.match(
                (empty) -> new Instructions(),
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
        );
        instructions.addAll(stmtInstr);

        instructions.add(new Comment("end of Statement " + StringEscapeUtils.escapeJava(print(stmt))));

        instructions.addRegisters(stmtInstr.usedRegisters);

        return instructions;
    }

    public static Instructions generateDecr(Decr decr, BackendScope scope) {
        return decr.lhs_.match(
                (var) -> generateVarDecr(var, scope),
                (arr) -> generateArrDecr(arr, scope),
                (field) -> generateFieldDecr(field, scope)
        );
    }

    public static Instructions generateIncr(Incr incr, BackendScope scope) {
        return incr.lhs_.match(
                (var) -> generateVarIncr(var, scope),
                (arr) -> generateArrIncr(arr, scope),
                (field) -> generateFieldIncr(field, scope)
        );
    }

    public static Instructions generateVarIncr(VariableRawLhs var, BackendScope scope) {
        Instructions instructions = new Instructions();

        if (var.binding.isField()) {
            return generateFieldIncrFromVar(var.ident_, var.binding.getBindedClass(), scope);
        }

        int offset = scope.getVariable(var.ident_).getOffset() * WORD_SIZE;

        instructions.add(new MovInstruction(Register.RAX, MemoryReference.getWithOffset(Register.RBP, offset)));
        instructions.add(new AddInstruction(Register.RAX, YieldUtils.number(1)));
        instructions.add(new MovInstruction(MemoryReference.getWithOffset(Register.RBP, offset), Register.RAX));

        return instructions;
    }

    public static Instructions generateArrIncr(ArrElemLhs arrElem, BackendScope scope) {
        Instructions instructions = new Instructions();

        Instructions exprInstr2 = generateExpr(arrElem.expr_2, Register.RAX, Register.RAX, scope);

        instructions.addAll(exprInstr2);

        instructions.add(new AddInstruction(Register.RAX, YieldUtils.number(1)));
        instructions.add(new MulInstruction(Register.RAX, YieldUtils.number(WORD_SIZE)));
        instructions.add(new MovInstruction(Register.RCX, Register.RAX));

        Instructions exprInstr1 = generateExpr(arrElem.expr_1, Register.RAX, Register.RAX, scope);

        if (exprInstr1.usedRegisters.contains(Register.RCX)) {
            instructions.add(new PushInstruction(Register.RCX, scope));
        }

        instructions.addAll(exprInstr1);

        if (exprInstr1.usedRegisters.contains(Register.RCX)) {
            instructions.add(new PopInstruction(Register.RCX, scope));
        }

        instructions.add(new AddInstruction(Register.RAX, Register.RCX));

        instructions.add(new MovInstruction(Register.RCX, MemoryReference.getRaw(Register.RAX)));
        instructions.add(new AddInstruction(Register.RCX, YieldUtils.number(1)));
        instructions.add(new MovInstruction(MemoryReference.getRaw(Register.RAX), Register.RCX));

        instructions.addRegisters(exprInstr1.usedRegisters);
        instructions.addRegisters(exprInstr2.usedRegisters);
        instructions.addRegister(Register.RCX);

        return instructions;
    }

    public static Instructions generateVarDecr(VariableRawLhs var, BackendScope scope) {
        Instructions instructions = new Instructions();

        if (var.binding.isField()) {
            return generateFieldDecrFromVar(var.ident_, var.binding.getBindedClass(), scope);
        }

        int offset = scope.getVariable(var.ident_).getOffset() * WORD_SIZE;

        instructions.add(new MovInstruction(Register.RAX, MemoryReference.getWithOffset(Register.RBP, offset)));
        instructions.add(new SubInstruction(Register.RAX, YieldUtils.number(1)));
        instructions.add(new MovInstruction(MemoryReference.getWithOffset(Register.RBP, offset), Register.RAX));

        return instructions;
    }

    public static Instructions generateArrDecr(ArrElemLhs arrElem, BackendScope scope) {
        Instructions instructions = new Instructions();

        Instructions exprInstr2 = generateExpr(arrElem.expr_2, Register.RAX, Register.RAX, scope);
        instructions.addAll(exprInstr2);

        instructions.add(new AddInstruction(Register.RAX, YieldUtils.number(1)));
        instructions.add(new MulInstruction(Register.RAX, YieldUtils.number(WORD_SIZE)));
        instructions.add(new MovInstruction(Register.RCX, Register.RAX));

        Instructions exprInstr1 = generateExpr(arrElem.expr_1, Register.RAX, Register.RAX, scope);

        if (exprInstr1.usedRegisters.contains(Register.RCX)) {
            instructions.add(new PushInstruction(Register.RCX, scope));
        }

        instructions.addAll(exprInstr1);

        if (exprInstr1.usedRegisters.contains(Register.RCX)) {
            instructions.add(new PopInstruction(Register.RCX, scope));
        }

        instructions.add(new AddInstruction(Register.RAX, Register.RCX));

        instructions.add(new MovInstruction(Register.RCX, MemoryReference.getRaw(Register.RAX)));
        instructions.add(new SubInstruction(Register.RCX, YieldUtils.number(1)));
        instructions.add(new MovInstruction(MemoryReference.getRaw(Register.RAX), Register.RCX));

        instructions.addRegister(Register.RCX);
        instructions.addRegisters(exprInstr1.usedRegisters);
        instructions.addRegisters(exprInstr2.usedRegisters);

        return instructions;
    }

    public static Instructions generateFieldDecr(FieldLhs fieldLhs, BackendScope scope) {
        Instructions instructions = new Instructions();
        int offset = fieldLhs.expr_.type.getClassDefinition().getFieldOffset(fieldLhs.ident_);

        Instructions exprInstructions = generateExpr(fieldLhs.expr_, Register.RAX, Register.RAX, scope);
        instructions.addAll(exprInstructions);

        instructions.add(new MovInstruction(Register.RCX, MemoryReference.getWithOffset(Register.RAX, offset)));
        instructions.add(new SubInstruction(Register.RCX, YieldUtils.number(1)));
        instructions.add(new MovInstruction(MemoryReference.getWithOffset(Register.RAX, offset), Register.RCX));

        instructions.addRegisters(exprInstructions.usedRegisters);
        instructions.addRegister(Register.RCX);

        return instructions;
    }

    public static Instructions generateFieldIncr(FieldLhs fieldLhs, BackendScope scope) {
        Instructions instructions = new Instructions();
        int offset = fieldLhs.expr_.type.getClassDefinition().getFieldOffset(fieldLhs.ident_);

        Instructions exprInstructions = generateExpr(fieldLhs.expr_, Register.RAX, Register.RAX, scope);

        instructions.addAll(exprInstructions);

        instructions.add(new MovInstruction(Register.RCX, MemoryReference.getWithOffset(Register.RAX, offset)));
        instructions.add(new AddInstruction(Register.RCX, YieldUtils.number(1)));
        instructions.add(new MovInstruction(MemoryReference.getWithOffset(Register.RAX, offset), Register.RCX));

        instructions.addRegisters(exprInstructions.usedRegisters);
        instructions.addRegister(Register.RCX);

        return instructions;
    }

    public static Instructions generateFieldChangeFromVar(String fieldName, TypeDefinition lhsType, BackendScope scope, boolean incr) {
        Instructions instructions = new Instructions();

        int thisOffset = scope.getVariable(THIS_KEYWORD).getOffset() * WORD_SIZE;
        int fieldOffset = lhsType.getClassDefinition().getFieldOffset(fieldName);

        instructions.add(new MovInstruction(Register.RCX, MemoryReference.getWithOffset(Register.RBP, thisOffset)));
        instructions.add(new AddInstruction(Register.RCX, YieldUtils.number(fieldOffset)));
        instructions.add(new MovInstruction(Register.RAX, MemoryReference.getRaw(Register.RCX)));

        if (incr) {
            instructions.add(new AddInstruction(Register.RAX, YieldUtils.number(1)));
        } else {
            instructions.add(new SubInstruction(Register.RAX, YieldUtils.number(1)));
        }

        instructions.add(new MovInstruction(MemoryReference.getRaw(Register.RCX), Register.RAX));

        instructions.addRegister(Register.RCX);

        return instructions;
    }

    public static Instructions generateFieldIncrFromVar(String fieldName, TypeDefinition lhsType, BackendScope scope) {
        return generateFieldChangeFromVar(fieldName, lhsType, scope, true);
    }

    public static Instructions generateFieldDecrFromVar(String fieldName, TypeDefinition lhsType, BackendScope scope) {
        return generateFieldChangeFromVar(fieldName, lhsType, scope, false);
    }

    public static Instructions generateDecl(Decl decl, BackendScope scope) {
        Instructions instructions = new Instructions();

        instructions.add(new Comment(""));
        instructions.add(new Comment(" declaration"));

        TypeDefinition type = decl.type_.match(
                (arrayType) -> getType(arrayType, scope.getGlobalEnvironment()),
                (typeNameS) -> getType(typeNameS, scope.getGlobalEnvironment())
        );


        for (Item item : decl.listitem_) {
            Instructions declInstructions = item.match(
                    (noInit) -> generateNoInit(noInit, type, scope),
                    (init) -> generateInit(init, type, scope)
            );

            instructions.addAll(declInstructions);
            instructions.addRegisters(declInstructions.usedRegisters);
        }

        return instructions;
    }

    public static Instructions generateStmtExpr(SExp exp, BackendScope scope) {
        return generateExpr(exp.expr_, Register.RAX, Register.RAX, scope);
    }

    public static Instructions generateWhile(While sWhile, BackendScope scope) {
        Instructions instructions = new Instructions();

        Label start = LabelsGenerator.getNonceLabel("_start_while");
        Label end = LabelsGenerator.getNonceLabel("_end_start");

        instructions.add(start);

        Instructions exprInstr = generateExpr(sWhile.expr_, Register.RAX, Register.RAX, scope);
        instructions.addAll(exprInstr);

        instructions.add(new CompareInstruction(Register.RAX, YieldUtils.number(1)));
        instructions.add(new JumpInstruction(end, JumpInstruction.Type.NE));

        Instructions stmtInstr = generateStmt(sWhile.stmt_, scope);
        instructions.addAll(stmtInstr);
        instructions.add(new JumpInstruction(start, JumpInstruction.Type.ALWAYS));
        instructions.add(end);

        instructions.addRegisters(exprInstr.usedRegisters);
        instructions.addRegisters(stmtInstr.usedRegisters);

        return instructions;
    }

    public static Instructions generateCond(Cond cond, BackendScope scope) {
        Instructions instructions = generateExpr(cond.expr_, Register.RAX, Register.RAX, scope);

        Label afterLabel = LabelsGenerator.getNonceLabel("_end_if");

        instructions.add(new CompareInstruction(Register.RAX, YieldUtils.number(1)));
        instructions.add(new JumpInstruction(afterLabel, JumpInstruction.Type.NE));

        Instructions stmtInstructions = generateStmt(cond.stmt_, scope);
        instructions.addAll(stmtInstructions);
        instructions.add(afterLabel);

        instructions.addRegisters(stmtInstructions.usedRegisters);

        return instructions;
    }

    public static Instructions generateCondElse(CondElse cond, BackendScope scope) {
        Instructions instructions = generateExpr(cond.expr_, Register.RAX, Register.RAX, scope);

        Label afterLabel = LabelsGenerator.getNonceLabel("_end_if");
        Label elseLabel = LabelsGenerator.getNonceLabel("_else");

        instructions.add(new CompareInstruction(Register.RAX, YieldUtils.number(1)));
        instructions.add(new JumpInstruction(elseLabel, JumpInstruction.Type.NE));

        Instructions stmtInstructions1 = generateStmt(cond.stmt_1, scope);
        instructions.addAll(stmtInstructions1);
        instructions.add(new JumpInstruction(afterLabel, JumpInstruction.Type.ALWAYS));

        instructions.add(elseLabel);

        Instructions stmtInstructions2 = generateStmt(cond.stmt_2, scope);
        instructions.addAll(stmtInstructions2);
        instructions.add(afterLabel);

        instructions.addRegisters(stmtInstructions1.usedRegisters);
        instructions.addRegisters(stmtInstructions2.usedRegisters);

        return instructions;
    }


    public static Instructions generateBStmt(BStmt bStmt, BackendScope oldScope) {
        BackendScope scope = new BackendScope(oldScope);

        Instructions instructions = new Instructions();

        instructions.add(new Comment(""));
        instructions.add(new Comment("block"));

        Instructions blockInstructions = bStmt.block_.match(
                (blockS) -> generateBlockS(blockS, scope)
        );

        instructions.addAll(blockInstructions);
        instructions.addRegisters(blockInstructions.usedRegisters);

        instructions.add(new Comment(""));
        instructions.add(new Comment("end block"));

        return instructions;

    }

    public static Instructions generateBlockS(BlockS block, BackendScope scope) {
        Instructions instructions = new Instructions();

        for (Stmt stmt : block.liststmt_) {
            Instructions genStmtInstr = generateStmt(stmt, scope);
            instructions.addAll(genStmtInstr);
            instructions.addRegisters(genStmtInstr.usedRegisters);
        }

        return instructions;
    }

    public static Instructions generateAss(Ass ass, BackendScope scope) {
        Instructions instructions = new Instructions();

        instructions.add(new Comment(""));
        instructions.add(new Comment("assignment " + print(ass)));

        Instructions exprInstr = generateExpr(ass.expr_, Register.RAX, Register.RAX, scope);
        instructions.addAll(exprInstr);

        Instructions lhsInstructions = ass.lhs_.match(
                (var) -> generateVariableAss(var, ass.expr_.type, Register.RAX, scope),
                (arr) -> generateVariableArrayAss(arr, ass.expr_.type, Register.RAX, scope),
                (field) -> generateFieldAss(field, ass.expr_.type, Register.RAX, scope)
        );

        instructions.addAll(lhsInstructions);
        instructions.addRegisters(exprInstr.usedRegisters);
        instructions.addRegisters(lhsInstructions.usedRegisters);

        return instructions;
    }

    public static Instructions generateVariableAss(VariableRawLhs var, TypeDefinition rhsType, String sourceRegister, BackendScope scope) {
        if (var.binding.isField()) {
            return generateFieldAssFromVar(var, rhsType, sourceRegister, scope);
        }

        int varOffset = scope.getVariable(var.ident_).getOffset() * WORD_SIZE;

        Instructions instructions = new Instructions();

        if (scope.getVariable(var.ident_).getType().isInterfaceType()) {
            Instructions genInterfaceInstr = generateInterfaceAss(scope.getVariable(var.ident_).getType().getInterfaceDefinition(), rhsType, scope);

            if (!sourceRegister.equals(Register.RAX)) {
                instructions.add(new MovInstruction(Register.RAX, sourceRegister));
            }

            instructions.add(new MovInstruction(Register.RCX, MemoryReference.getWithOffset(Register.RBP, varOffset)));
            instructions.addAll(genInterfaceInstr);
            instructions.addRegisters(genInterfaceInstr.usedRegisters);
            instructions.addRegister(Register.RCX);
        } else {
            instructions.add(new MovInstruction(MemoryReference.getWithOffset(Register.RBP, varOffset), sourceRegister));
        }

        return instructions;
    }

    // this w RAX
    public static Instructions generateFieldAss(FieldLhs fieldLhs, TypeDefinition rhsType, String sourceRegister, BackendScope scope) {
        Instructions instructions = new Instructions();

        int offset = fieldLhs.expr_.type.getClassDefinition().getFieldOffset(fieldLhs.ident_);
        TypeDefinition fieldType = fieldLhs.expr_.type.getClassDefinition().getFieldDeclaration(fieldLhs.ident_, -1, -1).getType();

        // this do RCX
        instructions.add(new MovInstruction(Register.RCX, Register.RAX));

        Instructions exprInstr = generateExpr(fieldLhs.expr_, Register.RAX, Register.RAX, scope);

        if (exprInstr.usedRegisters.contains(Register.RCX)) {
            instructions.add(new PushInstruction(Register.RCX, scope));
        }
        // wartosc do zapisania w RAX
        instructions.addAll(exprInstr);

        if (exprInstr.usedRegisters.contains(Register.RCX)) {
            instructions.add(new PopInstruction(Register.RCX, scope));
        }


        if (fieldType.isInterfaceType()) {
            Instructions generateInterfaceInstr = generateInterfaceAss(fieldType.getInterfaceDefinition(), rhsType, scope);
            instructions.add(new AddInstruction(Register.RCX, YieldUtils.number(offset)));
            instructions.addAll(generateInterfaceInstr);
            instructions.addRegisters(generateInterfaceInstr.usedRegisters);

        } else {
            instructions.add(new MovInstruction(MemoryReference.getWithOffset(Register.RAX, offset), Register.RCX));
        }



        instructions.addRegisters(exprInstr.usedRegisters);
        instructions.addRegister(Register.RCX);

        return instructions;
    }

    public static Instructions generateFieldAssFromVar(VariableRawLhs lhs, TypeDefinition rhsType, String sourceRegister, BackendScope scope) {
        Instructions instructions = new Instructions();

        // wartość do zapisania jest w RAX
        int thisOffset = scope.getVariable(THIS_KEYWORD).getOffset() * WORD_SIZE;
        int fieldOffset = lhs.binding.getBindedClass().getClassDefinition().getFieldOffset(lhs.ident_);
        TypeDefinition lhsType = lhs.binding.getBindedClass().getClassDefinition().fields.get(lhs.ident_).type;


        instructions.add(new MovInstruction(Register.RCX, MemoryReference.getWithOffset(Register.RBP, thisOffset)));
        instructions.add(new AddInstruction(Register.RCX, YieldUtils.number(fieldOffset)));

        if (lhsType.isInterfaceType()) {
            Instructions interfaceAssInstr = generateInterfaceAss(lhsType.getInterfaceDefinition(), rhsType, scope);
            instructions.add(new PushInstruction(Register.RCX, scope));
            instructions.addAll(interfaceAssInstr);
            instructions.add(new PopInstruction(Register.RCX, scope));
            instructions.addRegisters(interfaceAssInstr.usedRegisters);
        } else {
            instructions.add(new MovInstruction(MemoryReference.getRaw(Register.RCX), Register.RAX));
        }

        instructions.addRegister(Register.RCX);

        return instructions;
    }

    public static Instructions generateVariableArrayAss(ArrElemLhs arrElem, TypeDefinition rhsType, String registerOfValue, BackendScope scope) {
        Instructions instructions = new Instructions();

        // RCX bedzie trzymalo wartość którą należy zapisać do elementu tablicy
        if (!registerOfValue.equals(Register.RCX)) {
            instructions.add(new MovInstruction(Register.RCX, registerOfValue));
        }

        Instructions exprInstr2 = generateExpr(arrElem.expr_2, Register.RAX, Register.RAX, scope);

        if (exprInstr2.usedRegisters.contains(Register.RCX)) {
            instructions.add(new PushInstruction(Register.RCX, scope));
        }
        instructions.addAll(exprInstr2);
        if (exprInstr2.usedRegisters.contains(Register.RCX)) {
            instructions.add(new PopInstruction(Register.RCX, scope));
        }

        // Liczymy index tablicy w RDX
        instructions.add(new MovInstruction(Register.RDX, Register.RAX));
        instructions.add(new AddInstruction(Register.RDX, YieldUtils.number(1)));
        instructions.add(new MulInstruction(Register.RDX, YieldUtils.number(WORD_SIZE)));

        Instructions exprInstr1 = generateExpr(arrElem.expr_1, Register.RAX, Register.RAX, scope);

        if (exprInstr1.usedRegisters.contains(Register.RCX)) {
            instructions.add(new PushInstruction(Register.RCX, scope));
        }
        if (exprInstr1.usedRegisters.contains(Register.RDX)) {
            instructions.add(new PushInstruction(Register.RDX, scope));
        }
        // początek tablicy w RAX
        instructions.addAll(exprInstr1);
        if (exprInstr1.usedRegisters.contains(Register.RDX)) {
            instructions.add(new PopInstruction(Register.RDX, scope));
        }
        if (exprInstr1.usedRegisters.contains(Register.RCX)) {
            instructions.add(new PopInstruction(Register.RCX, scope));
        }

        // adres elementu tablicy w RAX
        instructions.add(new AddInstruction(Register.RAX, Register.RDX));

        TypeDefinition innerArrayType = arrElem.expr_1.type.getArrayTypeDefinition().getInnerTypeDefinition();

        if (innerArrayType.isInterfaceType()) {
            instructions.add(new PushInstruction(Register.RAX, scope));

            // rhs byla w RCX, interface w RAX, zamieniamy je miejscami
            instructions.add(new MovInstruction(Register.RDX, Register.RCX));
            instructions.add(new MovInstruction(Register.RCX, Register.RAX));
            instructions.add(new MovInstruction(Register.RAX, Register.RDX));

            Instructions interfaceAssInstr = generateInterfaceAss(innerArrayType.getInterfaceDefinition(), rhsType, scope);
            instructions.addAll(interfaceAssInstr);
            instructions.add(new MovInstruction(Register.RCX, Register.RAX));
            instructions.add(new PopInstruction(Register.RAX, scope));

            instructions.addRegisters(interfaceAssInstr.usedRegisters);
        } else {
            instructions.add(new MovInstruction(MemoryReference.getRaw(Register.RAX), Register.RCX));
        }


        instructions.addRegisters(exprInstr1.usedRegisters);
        instructions.addRegisters(exprInstr2.usedRegisters);
        instructions.addRegister(Register.RCX);
        instructions.addRegister(Register.RDX);

        return instructions;
    }

    public static Instructions generateNoInit(NoInit noInit, TypeDefinition type, BackendScope scope) {
        Instructions instructions = new Instructions();

        scope.declareVariable(noInit.ident_, type);
        int offset = scope.getVariable(noInit.ident_).getOffset() * WORD_SIZE;

        if (BasicTypeDefinition.STRING.equals(type)) {
            instructions.add(new CallInstruction(ExternalFunctions.EMPTY_STRING));
            instructions.addRegisters(callUnsafeRegs());
        } else if (type.isInterfaceType()) {
            instructions.add(new MovInstruction(Register.RDI, type.getInterfaceDefinition().methodsOffsetTable.size() + 1));
            // +1 bo w interfacie trzymamy jeszcze referencje to rzeczywistego obiektu
            instructions.add(new CallInstruction(ExternalFunctions.MALLOC_SIZE));
            instructions.addRegisters(callUnsafeRegs());
            instructions.addRegister(Register.RDI);
        } else {
            instructions.add(new MovInstruction(Register.RAX, YieldUtils.number(0)));
        }
        instructions.add(new MovInstruction(MemoryReference.getWithOffset(Register.RBP, offset), Register.RAX));

        return instructions;
    }

    // rhs Value to assign is in RAX, returned interface reference in RAX
    // Interface register in RCX
    public static Instructions generateInterfaceAss(InterfaceTypeDefinition interfaceType, TypeDefinition rhsType, BackendScope scope) {
        Instructions instructions = new Instructions();

        MethodType methodType;
        if (rhsType.isClassType()) {
            methodType = rhsType.getClassDefinition();
        } else {
            methodType = rhsType.getInterfaceDefinition();
        }

        // referencja do interfacu w RDX
        instructions.add(new MovInstruction(Register.RDX, Register.RCX));

        // referencja do klasy/rhs-interfacu w RCX
        instructions.add(new MovInstruction(Register.RCX, Register.RAX));

        if (rhsType.isInterfaceType()) {
            instructions.add(new MovInstruction(Register.RAX, MemoryReference.getRaw(Register.RCX)));
            instructions.add(new MovInstruction(MemoryReference.getRaw(Register.RDX), Register.RAX));
        } else {
            // zapisz faktyczna wartosc klasy w interfacie
            instructions.add(new MovInstruction(MemoryReference.getRaw(Register.RDX), Register.RCX));
        }

        for (String methodName : interfaceType.methodsOffsetTable.keySet()) {
            int rhsMethodOffset = methodType.getMethodOffset(methodName);
            int interfaceMethodOffset = interfaceType.getMethodOffset(methodName) + WORD_SIZE;

            if (((TypeDefinition) methodType).isInterfaceType()) {
                rhsMethodOffset += WORD_SIZE;
            }

            instructions.add(new MovInstruction(Register.RDI, Register.RCX));
            instructions.add(new AddInstruction(Register.RDI, YieldUtils.number(rhsMethodOffset)));
            instructions.add(new MovInstruction(Register.RAX, MemoryReference.getRaw(Register.RDI)));
            instructions.add(new MovInstruction(Register.RDI, Register.RDX));
            instructions.add(new AddInstruction(Register.RDI, YieldUtils.number(interfaceMethodOffset)));
            instructions.add(new MovInstruction(MemoryReference.getRaw(Register.RDI), Register.RAX));
        }

        instructions.add(new MovInstruction(Register.RAX, Register.RDX));

        instructions.addRegisters(callUnsafeRegs());
        instructions.addRegister(Register.RDI);
        instructions.addRegister(Register.RCX);
        instructions.addRegister(Register.RDX);

        return instructions;
    }

    public static Instructions generateInit(Init init, TypeDefinition type, BackendScope scope) {
        Instructions instructions = generateExpr(init.expr_, Register.RAX, Register.RAX, scope);

        scope.declareVariable(init.ident_, type);
        int offset = scope.getVariable(init.ident_).getOffset() * WORD_SIZE;

        TypeDefinition rhsType = init.expr_.type;

        if (type.isInterfaceType()) {
            InterfaceTypeDefinition interfaceType = type.getInterfaceDefinition();

            instructions.add(new PushInstruction(Register.RAX, scope));
            instructions.add(new MovInstruction(Register.RDI, interfaceType.methodsOffsetTable.size() + 1));
            // +1 bo w interfacie trzymamy jeszcze referencje to rzeczywistego obiektu
            instructions.add(new CallInstruction(ExternalFunctions.MALLOC_SIZE));
            instructions.add(new MovInstruction(Register.RCX, Register.RAX));
            // rhs w RAX
            instructions.add(new PopInstruction(Register.RAX, scope));

            Instructions interfaceAssInsr = generateInterfaceAss(interfaceType, rhsType, scope);

            instructions.addAll(interfaceAssInsr);
            instructions.addRegisters(interfaceAssInsr.usedRegisters);
        }

        instructions.add(new MovInstruction(MemoryReference.getWithOffset(Register.RBP, offset), Register.RAX));


        return instructions;
    }

    private static Instructions generateEpilog() {
        Instructions instructions = new Instructions();

        instructions.add(new MovInstruction(Register.RSP, Register.RBP));
        instructions.add(new PopInstruction(Register.RBP));

        instructions.add(new Return());

        return instructions;
    }

    public static Instructions generateReturn(Ret ret, BackendScope scope) {
        Instructions instructions = new Instructions();
        instructions.add(new Comment(""));
        instructions.add(new Comment("return from function"));

        Instructions exprInstr = generateExpr(ret.expr_, Register.RAX, Register.RAX, scope);
        instructions.addAll(exprInstr);
        instructions.addRegisters(exprInstr.usedRegisters);

        instructions.addAll(generateEpilog());

        return instructions;
    }

    public static Instructions generateVReturn(VRet ret) {
        Instructions instructions = new Instructions();
        instructions.add(new Comment(""));
        instructions.add(new Comment("void return from function"));

        instructions.addAll(generateEpilog());

        return instructions;
    }

    public static Instructions generateForArr(ForArr forArr, BackendScope oldScope) {
        BackendScope scope = new BackendScope(oldScope);

        Instructions instructions = new Instructions();

        Label start = LabelsGenerator.getNonceLabel("_start_for_arr");
        Label end = LabelsGenerator.getNonceLabel("_end_for_arr");

        scope.declareVariable(forArr.ident_, getType(forArr.typename_, scope.getGlobalEnvironment(), -1, -1));
        int offset = scope.getVariable(forArr.ident_).getOffset() * WORD_SIZE;

        instructions.addRegister(Register.RDX);
        instructions.addRegister(Register.RCX);
        instructions.addRegister(Register.RDI);

        Instructions exprInstr = generateExpr(forArr.expr_, Register.RAX, Register.RAX, scope);

        instructions.addAll(exprInstr); // get array beginning
        instructions.add(new MovInstruction(Register.RCX, Register.RAX));
        instructions.add(new MovInstruction(Register.RDX, MemoryReference.getRaw(Register.RCX)));
        instructions.add(new MovInstruction(Register.RDI, YieldUtils.number(1)));

        instructions.add(start);
        instructions.add(new CompareInstruction(Register.RDI, Register.RDX));
        instructions.add(new JumpInstruction(end, JumpInstruction.Type.GTH));

        instructions.add(new MovInstruction(Register.RAX, Register.RDI));
        instructions.add(new MulInstruction(Register.RAX, YieldUtils.number(WORD_SIZE)));
        instructions.add(new AddInstruction(Register.RAX, Register.RCX)); // add address of begining of an array
        instructions.add(new MovInstruction(Register.RAX, MemoryReference.getRaw(Register.RAX)));
        instructions.add(new MovInstruction(MemoryReference.getWithOffset(Register.RBP, offset), Register.RAX));

        Instructions stmtInstr = generateStmt(forArr.stmt_, scope);

        if (stmtInstr.usedRegisters.contains(Register.RCX)) {
            instructions.add(new PushInstruction(Register.RCX, scope));
        }
        if (stmtInstr.usedRegisters.contains(Register.RDX)) {
            instructions.add(new PushInstruction(Register.RDX, scope));
        }
        if (stmtInstr.usedRegisters.contains(Register.RDI)) {
            instructions.add(new PushInstruction(Register.RDI));
        }

        instructions.addAll(stmtInstr);

        if (stmtInstr.usedRegisters.contains(Register.RDI)) {
            instructions.add(new PopInstruction(Register.RDI));
        }
        if (stmtInstr.usedRegisters.contains(Register.RDX)) {
            instructions.add(new PopInstruction(Register.RDX, scope));
        }
        if (stmtInstr.usedRegisters.contains(Register.RCX)) {
            instructions.add(new PopInstruction(Register.RCX, scope));
        }

        instructions.add(new AddInstruction(Register.RDI, YieldUtils.number(1)));

        instructions.add(new JumpInstruction(start, JumpInstruction.Type.ALWAYS));
        instructions.add(end);


        return instructions;
    }

    public static Instructions notImplemented(Stmt stmt) {
        throw new CompilerException("Compiling statement " + stmt.getClass() + " not implemented yet");
    }


}
