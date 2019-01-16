package src.Backend;

import src.Absyn.*;
import src.Backend.Definitions.*;
import src.Backend.Instructions.*;
import src.Definitions.BasicTypeDefinition;
import src.Definitions.TypeDefinition;
import src.Exceptions.CompilerException;

import java.util.*;

import static java.lang.Math.max;
import static src.Backend.Instructions.ConstantUtils.THIS_KEYWORD;
import static src.Backend.Instructions.ConstantUtils.WORD_SIZE;
import static src.Backend.LabelsGenerator.getNonceLabel;
import static src.Frontend.TypeUtils.getType;
import static src.PrettyPrinter.print;

public class CompileExpression {

    public static Instructions generateExpr(Expr expr, String destRegister, String sourceRegister, BackendScope scope) {
        return expr.match(
                (var) -> generateVar(var, destRegister, scope),
                (eInt) -> generateInt(eInt, destRegister),
                (eTrue) -> generateTrue(destRegister),
                (eFalse) -> generateFalse(destRegister),
                (eThis) -> generateThis(destRegister, scope),
                (ignored) -> generateNull(destRegister),
                (eApp) -> generateApp(eApp, destRegister, scope),
                (str) -> generateStr(str, destRegister, scope),
                (constr) -> generateEConstr(constr, destRegister, scope),
                (arrConstr) -> generateArrConstr(arrConstr, destRegister, scope),
                (arrAcc) -> generateArrAcc(arrAcc, destRegister, scope),
                (neg) -> generateNeg(neg, destRegister, sourceRegister, scope),
                (not) -> generateNot(not, destRegister, sourceRegister, scope),
                (mul) -> generateMul(mul, destRegister, sourceRegister, scope),
                (add) -> generateAdd(add, destRegister, sourceRegister, scope),
                (rel) -> generateRel(rel, destRegister, sourceRegister, scope),
                (and) -> generateLazyAnd(and, destRegister, sourceRegister, scope),
                (eOr) -> generateLazyOr(eOr, destRegister, sourceRegister, scope),
                (objAcc) -> generateObjAcc(objAcc, destRegister, scope),
                (cast) -> generateCast(cast, destRegister, sourceRegister, scope)
        );
    }

    public static Set<String> callUnsafeRegs() {
        Set<String> regs = new HashSet<>();
        regs.add(Register.RDI);
        regs.add(Register.RSI);
        regs.add(Register.RDX);
        regs.add(Register.RCX);
        regs.add(Register.R8);
        regs.add(Register.R9);

        return regs;
    }

    private static Instructions generatePushBeforeFunction(BackendScope scope) {
        Instructions instructions = new Instructions();

        instructions.add(new PushInstruction(Register.RDI, scope));
        instructions.add(new PushInstruction(Register.RSI, scope));
        instructions.add(new PushInstruction(Register.RDX, scope));
        instructions.add(new PushInstruction(Register.RCX, scope));
        instructions.add(new PushInstruction(Register.R8, scope));
        instructions.add(new PushInstruction(Register.R9, scope));

        return instructions;
    }

    private static Instructions generatePopAfterFunction(BackendScope scope) {
        Instructions instructions = new Instructions();

        instructions.add(new PopInstruction(Register.R9, scope));
        instructions.add(new PopInstruction(Register.R8, scope));
        instructions.add(new PopInstruction(Register.RCX, scope));
        instructions.add(new PopInstruction(Register.RDX, scope));
        instructions.add(new PopInstruction(Register.RSI, scope));
        instructions.add(new PopInstruction(Register.RDI, scope));

        return instructions;
    }
    
    public static Instructions generateFunctionApp(EApp eApp, String destRegister, BackendScope scope) {
        Instructions instructions = new Instructions();

        String funcName = eApp.ident_;

        int numberOfArgsPassedOnStack = max(0, (eApp.listexpr_.size() - 6));
        int numberOfTempsOnStack = scope.getNumberOfTempsOnStack();

        boolean hasToAlignStack = (numberOfArgsPassedOnStack + numberOfTempsOnStack) % 2 != 0;

        if (hasToAlignStack) {
            instructions.add(new Comment("aligning stack"));
            instructions.add(new SubInstruction(Register.RSP, YieldUtils.number(WORD_SIZE)));
        }

        Instructions genFunArgInsr = generateFunctionArguments(eApp.listexpr_, scope);
        instructions.addAll(genFunArgInsr);
        instructions.add(new CallInstruction(funcName));

        int offset = max(0, (eApp.listexpr_.size() - 6)) * WORD_SIZE;

        if (offset > 0) {
            instructions.add(new AddInstruction(Register.RSP, YieldUtils.number(offset)));
            scope.changeTempsOnStack(-offset);
        }

        if (hasToAlignStack) {
            instructions.add(new AddInstruction(Register.RSP, YieldUtils.number(WORD_SIZE)));
        }

        instructions.addRegisters(callUnsafeRegs());
        instructions.addRegisters(genFunArgInsr.usedRegisters);

        return instructions;
    }

    private static Instructions generateCallableArguments(List<Expr> exprs, BackendScope scope, boolean isMethod) {
        Instructions instructions = new Instructions();
        Instructions exprInstructions0 = new Instructions();
        Instructions exprInstructions1 = new Instructions();
        Instructions exprInstructions2 = new Instructions();
        Instructions exprInstructions3 = new Instructions();
        Instructions exprInstructions4 = new Instructions();
        Instructions exprInstructions5 = new Instructions();

        boolean rdiDestroyed = false;
        boolean rsiDestroyed = false;
        boolean rdxDestroyed = false;
        boolean rcxDestroyed = false;
        boolean r8Destroyed = false;

        int argsOffset = 0; // used for 'this' argument in methods

        if (isMethod) {
            argsOffset = -1;
        }

        if (!isMethod && exprs.size() > 0) {
            exprInstructions0 = generateExpr(exprs.get(0), Register.RAX, Register.RAX, scope);
        }

        if (exprs.size() > 1 + argsOffset) {
            exprInstructions1 = generateExpr(exprs.get(1 + argsOffset), Register.RAX, Register.RAX, scope);
        }

        if (exprs.size() > 2 + argsOffset) {
            exprInstructions2 = generateExpr(exprs.get(2 + argsOffset), Register.RAX, Register.RAX, scope);
        }

        if (exprs.size() > 3 + argsOffset) {
            exprInstructions3 = generateExpr(exprs.get(3 + argsOffset), Register.RAX, Register.RAX, scope);
        }

        if (exprs.size() > 4 + argsOffset) {
            exprInstructions4 = generateExpr(exprs.get(4 + argsOffset), Register.RAX, Register.RAX, scope);
        }

        if (exprs.size() > 5 + argsOffset) {
            exprInstructions5 = generateExpr(exprs.get(5 + argsOffset), Register.RAX, Register.RAX, scope);
        }


        for (int i = exprs.size(); i > (6 + argsOffset); i--) {
            instructions.addAll(generateExpr(exprs.get(i-1), Register.RAX, Register.RAX, scope));
            instructions.add(new PushInstruction(Register.RAX, scope));
        }

        Set<String> incrementalUsedRegistersSet = new HashSet<>();


        if (exprs.size() > 5 + argsOffset) {
            incrementalUsedRegistersSet.addAll(exprInstructions5.usedRegisters);
        }

        if (exprs.size() > 4 + argsOffset && incrementalUsedRegistersSet.contains(Register.R8)) {
            r8Destroyed = true;
            incrementalUsedRegistersSet.addAll(exprInstructions4.usedRegisters);
        }

        if (exprs.size() > 3 + argsOffset && incrementalUsedRegistersSet.contains(Register.RCX)) {
            rcxDestroyed = true;
            incrementalUsedRegistersSet.addAll(exprInstructions3.usedRegisters);
        }

        if (exprs.size() > 2 + argsOffset && incrementalUsedRegistersSet.contains(Register.RDX)) {
            rdxDestroyed = true;
            incrementalUsedRegistersSet.addAll(exprInstructions2.usedRegisters);
        }

        if (exprs.size() > 1 + argsOffset && incrementalUsedRegistersSet.contains(Register.RSI)) {
            rsiDestroyed = true;
            incrementalUsedRegistersSet.addAll(exprInstructions1.usedRegisters);
        }

        if (!isMethod && exprs.size() > 0 && incrementalUsedRegistersSet.contains(Register.RDI)) {
            rdiDestroyed = true;
            incrementalUsedRegistersSet.addAll(exprInstructions0.usedRegisters);
        }


        if (!isMethod && exprs.size() > 0) {
            instructions.addAll(exprInstructions0);

            if (rdiDestroyed) {
                instructions.add(new PushInstruction(Register.RDI));
            } else {
                instructions.add(new MovInstruction(Register.RDI, Register.RAX));
            }
        }

        if (exprs.size() > 1 + argsOffset) {
            instructions.addAll(exprInstructions1);

            if (rsiDestroyed) {
                instructions.add(new PushInstruction(Register.RSI));
            } else {
                instructions.add(new MovInstruction(Register.RSI, Register.RAX));
            }
        }

        if (exprs.size() > 2 + argsOffset) {
            instructions.addAll(generateExpr(exprs.get(2 + argsOffset), Register.RAX, Register.RAX, scope));
            if (rdxDestroyed) {
                instructions.add(new PushInstruction(Register.RDX));
            } else {
                instructions.add(new MovInstruction(Register.RDX, Register.RAX));
            }
        }

        if (exprs.size() > 3 + argsOffset) {
            instructions.addAll(generateExpr(exprs.get(3 + argsOffset), Register.RAX, Register.RAX, scope));
            if (rcxDestroyed) {
                instructions.add(new PushInstruction(Register.RCX));
            } else {
                instructions.add(new MovInstruction(Register.RCX, Register.RAX));
            }
        }

        if (exprs.size() > 4 + argsOffset) {
            instructions.addAll(generateExpr(exprs.get(4 + argsOffset), Register.RAX, Register.RAX, scope));
            if (r8Destroyed) {
                instructions.add(new PushInstruction(Register.R8));
            } else {
                instructions.add(new MovInstruction(Register.R8, Register.RAX));
            }
        }

        if (exprs.size() > 5 + argsOffset) {
            instructions.addAll(generateExpr(exprs.get(5 + argsOffset), Register.RAX, Register.RAX, scope));
            // r9 never gets destroyed as it's the last expr to be generated
            instructions.add(new MovInstruction(Register.R9, Register.RAX));
        }

        if (r8Destroyed) { instructions.add(new PopInstruction(Register.R8)); }
        if (rcxDestroyed) { instructions.add(new PopInstruction(Register.RCX)); }
        if (rdxDestroyed) { instructions.add(new PopInstruction(Register.RDX)); }
        if (rsiDestroyed) { instructions.add(new PopInstruction(Register.RSI)); }
        if (rdiDestroyed) { instructions.add(new PopInstruction(Register.RDI)); }

        instructions.addRegisters(incrementalUsedRegistersSet);

        return instructions;
    }

    private static Instructions generateFunctionArguments(List<Expr> exprs, BackendScope scope) {
        return generateCallableArguments(exprs, scope, false);
    }

    private static Instructions generateMethodArguments(List<Expr> exprs, BackendScope scope) {
        return generateCallableArguments(exprs, scope, true);
    }

    public static Instructions generateMethodApp(EApp eApp, String destRegister, BackendScope scope) {
        Instructions instructions = new Instructions();


        int numberOfArgsPassedOnStack = max(0, (eApp.listexpr_.size() - 5)); // - 6 + 1 - dodajemy "this" jako pierwszy argument
        int numberOfTempsOnStack = scope.getNumberOfTempsOnStack();

        boolean hasToAlignStack = (numberOfArgsPassedOnStack + numberOfTempsOnStack) % 2 != 0;

        if (hasToAlignStack) {
            instructions.add(new Comment("aligning stack"));
            instructions.add(new SubInstruction(Register.RSP, YieldUtils.number(WORD_SIZE)));
        }

        VariableCompilerInfo varInfo = scope.getVariable(THIS_KEYWORD);
        int thisOffset = varInfo.getOffset() * WORD_SIZE;
        instructions.add(new MovInstruction(Register.RDI, MemoryReference.getWithOffset(Register.RBP, thisOffset)));

        Instructions genMethAppInstr = generateMethodArguments(eApp.listexpr_, scope);
        instructions.addAll(genMethAppInstr);


        // TODO: handle interfaces
        instructions.add(new CallInstruction(eApp.binding.getBindedClass().getClassDefinition().methods.get(eApp.ident_).label.getLabelName()));

        // function args pushed on stack?
        int offset = max(0, (eApp.listexpr_.size() - 5)) * WORD_SIZE;

        if (offset > 0) {
            instructions.add(new AddInstruction(Register.RSP, YieldUtils.number(offset)));
            scope.changeTempsOnStack(-offset);
        }

        if (hasToAlignStack) {
            instructions.add(new AddInstruction(Register.RSP, YieldUtils.number(WORD_SIZE)));
        }

        instructions.addRegisters(genMethAppInstr.usedRegisters);
        instructions.addRegisters(callUnsafeRegs());
        instructions.addRegister(Register.RDI);

        return instructions;
    }

    public static Instructions generateApp(EApp eApp, String destRegister, BackendScope scope) {
        if (Binding.FUNCTION_BINDING.equals(eApp.binding)) {
            return generateFunctionApp(eApp, destRegister, scope);
        }

        return generateMethodApp(eApp, destRegister, scope);
    }

    public static Instructions generateVar(EVar var, String destRegister, BackendScope scope) {
        if (!Binding.VARIABLE_BINDING.equals(var.binding)) {
            return generateFieldFromVar(var, destRegister, scope);
        }

        VariableCompilerInfo varInfo = scope.getVariable(var.ident_);

        int offset = varInfo.getOffset() * WORD_SIZE;

        Instructions instructions = new Instructions();
        instructions.add(new MovInstruction(destRegister, MemoryReference.getWithOffset(Register.RBP, offset)));

        return instructions;
    }

    public static Instructions generateFieldFromVar(EVar var, String destRegister, BackendScope scope) {
        Instructions instructions = new Instructions();

        int thisOffset = scope.getVariable(THIS_KEYWORD).getOffset() * WORD_SIZE;
        int fieldOffset = var.binding.getBindedClass().getClassDefinition().getFieldOffset(var.ident_);

        instructions.add(new MovInstruction(Register.RAX, MemoryReference.getWithOffset(Register.RBP, thisOffset)));
        instructions.add(new AddInstruction(Register.RAX, YieldUtils.number(fieldOffset)));
        instructions.add(new MovInstruction(Register.RAX, MemoryReference.getRaw(Register.RAX)));

        return instructions;
    }

    public static Instructions generateInt(ELitInt eInt, String destRegister) {
        Instructions instructions = new Instructions();
        instructions.add(new MovInstruction(destRegister, YieldUtils.number(eInt.integer_)));
        return instructions;
    }

    public static Instructions generateTrue(String destRegister) {
        Instructions instructions = new Instructions();
        instructions.add(new MovInstruction(destRegister, YieldUtils.number(1)));
        return instructions;
    }

    public static Instructions generateFalse(String destRegister) {
        Instructions instructions = new Instructions();
        instructions.add(new MovInstruction(destRegister, YieldUtils.number(0)));
        return instructions;
    }

    public static Instructions generateNeg(Neg neg, String destRegister, String sourceRegister, BackendScope scope) {
        Instructions assemblyInstructions = generateExpr(neg.expr_, destRegister, sourceRegister, scope);
        NegInstruction negInstr = new NegInstruction(destRegister);

        assemblyInstructions.add(negInstr);

        return assemblyInstructions;
    }

    public static Instructions generateNot(Not not, String destRegister, String sourceRegister, BackendScope scope) {
        Instructions assemblyInstructions = generateExpr(not.expr_, destRegister, sourceRegister, scope);

        CompareInstruction cmpInstr = new CompareInstruction(sourceRegister, YieldUtils.number(0));
        MovInstruction movInstruction = new MovInstruction(sourceRegister, YieldUtils.number(0));
        SetIfEqualInstruction setIfEqualInstruction = new SetIfEqualInstruction("al");

        assemblyInstructions.addAll(new ArrayList<>(Arrays.asList(cmpInstr, movInstruction, setIfEqualInstruction)));

        return assemblyInstructions;
    }

    public static Instructions generateAdd(EAdd add, String destRegister, String sourceRegister, BackendScope scope) {
        if (BasicTypeDefinition.STRING.equals(add.type)) {
            return generateStringAdd(add, destRegister, scope);
        }

        Instructions instructions = new Instructions();

        Instructions expr2Instructions = generateExpr(add.expr_2, destRegister, sourceRegister, scope);
        instructions.addAll(expr2Instructions);
        instructions.add(new PushInstruction(Register.RAX, scope));

        Instructions expr1Instructions = generateExpr(add.expr_1, destRegister, sourceRegister, scope);
        instructions.addAll(expr1Instructions);
        instructions.add(new PopInstruction(Register.RCX, scope));

        Instructions oppInstr = add.addop_.match(
                (ignored) -> getPlusInstruction(Register.RAX, Register.RCX, scope),
                (ignored) -> getMinusInstruction(Register.RAX, Register.RCX, scope)
        );

        instructions.addAll(oppInstr);

        instructions.addRegister(Register.RCX);
        instructions.addRegisters(expr1Instructions.usedRegisters);
        instructions.addRegisters(expr2Instructions.usedRegisters);

        return instructions;
    }

    public static Instructions generateStringAdd(EAdd add, String destRegister, BackendScope scope) {
        // here it has to be string + as there's no - in strings
        Instructions instructions = new Instructions();

        Instructions expr1Instructions = generateExpr(add.expr_1, Register.RAX, Register.RAX, scope);
        instructions.addAll(expr1Instructions);
        instructions.add(new MovInstruction(Register.RDI, Register.RAX));


        Instructions expr2Instructions = generateExpr(add.expr_2, Register.RAX, Register.RAX, scope);

        boolean expr2destroysRdi = expr2Instructions.usedRegisters.contains(Register.RDI);

        if (expr2destroysRdi) {
            instructions.add(new PushInstruction(Register.RDI, scope));
        }
        instructions.addAll(expr2Instructions);
        instructions.add(new MovInstruction(Register.RSI, Register.RAX));

        if (expr2destroysRdi) {
            instructions.add(new PopInstruction(Register.RDI, scope));
        }

        instructions.add(new CallInstruction(ExternalFunctions.ADD_STRINGS));

        if (!destRegister.equals(Register.RAX)) {
            instructions.add(new MovInstruction(destRegister, Register.RAX));
        }

        instructions.addRegisters(callUnsafeRegs());
        instructions.addRegisters(expr1Instructions.usedRegisters);
        instructions.addRegisters(expr2Instructions.usedRegisters);

        return instructions;

    }

    public static Instructions getMinusInstruction(String destRegister, String sourceRegister, BackendScope scope) {
        Instructions instructions = new Instructions();
        instructions.add(new SubInstruction(destRegister, sourceRegister));
        return instructions;
    }

    public static Instructions getPlusInstruction(String destRegister, String sourceRegister, BackendScope scope) {
        Instructions instructions = new Instructions();
        instructions.add(new AddInstruction(destRegister, sourceRegister));
        return instructions;

    }

    public static Instructions generateMul(EMul mul, String destRegister, String sourceRegister, BackendScope scope) {
        Instructions instructions = new Instructions();

        Instructions instructions1 = generateExpr(mul.expr_1, destRegister, sourceRegister, scope);
        Instructions instructions2 = generateExpr(mul.expr_2, destRegister, sourceRegister, scope);

        instructions.addAll(instructions2);
        instructions.add(new MovInstruction(Register.RCX, Register.RAX));

        boolean expr1destroysRcx = instructions1.usedRegisters.contains(Register.RCX);

        if (expr1destroysRcx) {
            instructions.add(new PushInstruction(Register.RCX));
        }

        instructions.addAll(instructions1);

        if (expr1destroysRcx) {
            instructions.add(new PopInstruction(Register.RCX));
        }

        Instructions oppInstr = mul.mulop_.match(
                (ignored) -> generateMultiplyInstructions(Register.RAX, Register.RCX, scope),
                (ignored) -> generateDivideInstructions(Register.RAX, Register.RCX, scope),
                (ignored) -> generateModInstruction(Register.RAX, Register.RCX, scope)
        );

        instructions.addAll(oppInstr);

        instructions.addRegisters(instructions1.usedRegisters);
        instructions.addRegisters(instructions2.usedRegisters);
        instructions.addRegisters(oppInstr.usedRegisters);
        instructions.addRegister(Register.RCX);

        return instructions;
    }

    public static Instructions generateMultiplyInstructions(String destRegister, String sourceRegister, BackendScope scope) {
        Instructions instructions = new Instructions();
        instructions.add(new MulInstruction(destRegister, sourceRegister));
        return instructions;
    }

    public static Instructions generateDivideInstructions(String destRegister, String sourceRegister, BackendScope scope) {
        Instructions instructions = new Instructions();

        if (!destRegister.equals(Register.RAX)) {
            instructions.add(new MovInstruction(Register.RAX, destRegister));
        }

        instructions.add(new XorInstruction(Register.RDX, Register.RDX));
        instructions.add(new DivInstruction(sourceRegister));

        if (!destRegister.equals(Register.RAX)) {
            instructions.add(new MovInstruction(destRegister, Register.RAX));
        }

        instructions.addRegister(Register.RDX);

        return instructions;
    }

    public static Instructions generateModInstruction(String destRegister, String sourceRegister, BackendScope scope) {
        Instructions instructions = new Instructions();


        if (!destRegister.equals(Register.RAX)) {
            instructions.add(new MovInstruction(Register.RAX, destRegister));
        }

        instructions.add(new XorInstruction(Register.RDX, Register.RDX));
        instructions.add(new DivInstruction(sourceRegister));

        instructions.add(new MovInstruction(destRegister, Register.RDX));

        instructions.addRegister(Register.RDX);

        return instructions;
    }

    public static Instructions generateLazyOr(EOr eOr, String destRegister, String sourceRegister, BackendScope scope) {
        Instructions expr1Instructions = generateExpr(eOr.expr_1, destRegister, sourceRegister, scope);
        Instructions expr2Instructions = generateExpr(eOr.expr_2, destRegister, sourceRegister, scope);
        Instructions instructions = new Instructions();

        Label endLabel = getNonceLabel("_or_end");

        instructions.addAll(expr1Instructions);
        instructions.add(new CompareInstruction(destRegister, YieldUtils.number(1)));
        // if jumps, in RAX there is 1, so the result is correct
        instructions.add(new JumpInstruction(endLabel, JumpInstruction.Type.EQU));

        // sets to RAX rhs of or, so OR value is the same as value of expr_2
        instructions.addAll(expr2Instructions);
        instructions.add(endLabel);

        instructions.addRegisters(expr1Instructions.usedRegisters);
        instructions.addRegisters(expr2Instructions.usedRegisters);

        return instructions;
    }

    public static Instructions generateLazyAnd(EAnd eAnd, String destRegister, String sourceRegister, BackendScope scope) {
        Instructions expr1Instructions = generateExpr(eAnd.expr_1, destRegister, sourceRegister, scope);
        Instructions expr2Instructions = generateExpr(eAnd.expr_2, destRegister, sourceRegister, scope);
        Instructions instructions = new Instructions();

        Label endLabel = getNonceLabel("_and_end");

        instructions.addAll(expr1Instructions);
        instructions.add(new CompareInstruction(destRegister, YieldUtils.number(0)));
        // if jumps, in RAX there is 0, so the result of AND is 0, so we can end
        instructions.add(new JumpInstruction(endLabel, JumpInstruction.Type.EQU));

        // lhs was 1, so setting RAX value to rhs value is enough to have correct overall AND result
        instructions.addAll(expr2Instructions);
        instructions.add(endLabel);

        instructions.addRegisters(expr1Instructions.usedRegisters);
        instructions.addRegisters(expr2Instructions.usedRegisters);

        return instructions;
    }

    public static Instructions generateRel(ERel rel, String destRegister, String sourceRegister, BackendScope scope) {
        Instructions instructions = new Instructions();
        Instructions expr2Instructions = generateExpr(rel.expr_2, destRegister, sourceRegister, scope);
        Instructions expr1Instructions = generateExpr(rel.expr_1, destRegister, sourceRegister, scope);

        instructions.addAll(expr2Instructions);
        instructions.add(new MovInstruction(Register.RCX, Register.RAX));

        boolean expr1destroysRcx = expr1Instructions.usedRegisters.contains(Register.RCX);

        if (expr1destroysRcx) {
            instructions.add(new PushInstruction(Register.RCX));
        }

        instructions.addAll(expr1Instructions);

        if (expr1destroysRcx) {
            instructions.add(new PopInstruction(Register.RCX, scope));
        }

        instructions.add(new CompareInstruction(Register.RAX, Register.RCX));
        instructions.add(new MovInstruction(destRegister, YieldUtils.number(1)));

        Label endLabel = LabelsGenerator.getNonceLabel("_rel_end");

        instructions.add(rel.relop_.match(
                (lth) -> new JumpInstruction(endLabel, JumpInstruction.Type.LTH),
                (le) -> new JumpInstruction(endLabel, JumpInstruction.Type.LE),
                (gth) -> new JumpInstruction(endLabel, JumpInstruction.Type.GTH),
                (ge) -> new JumpInstruction(endLabel, JumpInstruction.Type.GE),
                (equ) -> new JumpInstruction(endLabel, JumpInstruction.Type.EQU),
                (ne) -> new JumpInstruction(endLabel, JumpInstruction.Type.NE)
        ));

        instructions.add(new MovInstruction(destRegister, YieldUtils.number(0)));
        instructions.add(endLabel);

        instructions.addRegister(Register.RCX);
        instructions.addRegisters(expr1Instructions.usedRegisters);
        instructions.addRegisters(expr2Instructions.usedRegisters);

        return instructions;
    }

    public static Instructions generateCast(ECast cast, String destRegister, String sourceRegister, BackendScope scope) {
        Instructions exprInstructions = generateExpr(cast.expr_, destRegister, sourceRegister, scope);

        TypeDefinition castedToType = getType(cast.typename_, scope.getGlobalEnvironment(), cast.line_num, cast.col_num);

        if (castedToType.equals(BasicTypeDefinition.INT)) {
            // do nothing, treat register value as an int
            return exprInstructions;
        }

        return notImplemented(cast);
    }

    public static Instructions generateStr(EString str, String destRegister, BackendScope scope) {
        Instructions instructions = new Instructions();
        Label l = scope.getGlobalEnvironment().getStringLabel(str.string_);

        instructions.add(new MovInstruction(destRegister, l.getLabelName()));

        return instructions;
    }

    public static Instructions generateNull(String destRegister) {
        Instructions instructions = new Instructions();

        instructions.add(new XorInstruction(destRegister, destRegister));

        return instructions;
    }

    public static Instructions generateArrConstr(EArrConstr constr, String destRegister, BackendScope scope) {
        Instructions instructions = new Instructions();
        Instructions exprInstructions = generateExpr(constr.expr_, Register.RAX, Register.RAX, scope);

        instructions.addAll(exprInstructions);

        instructions.addRegister(Register.RDI);
        instructions.add(new MovInstruction(Register.RDI, Register.RAX));

        instructions.addRegisters(callUnsafeRegs());
        instructions.add(new CallInstruction(ExternalFunctions.MALLOC_ARRAY));

        // od tego memomentu, wskaznik na zaalokowana pamiec jest w RAX

        if (BasicTypeDefinition.STRING.equals(getType(constr.typename_, scope.getGlobalEnvironment(), -1, -1))) {
            instructions.addRegister(Register.RDI);
            instructions.addRegister(Register.RDX);
            instructions.addRegister(Register.RCX);

            instructions.add(new MovInstruction(Register.RDX, Register.RAX));
            // RDI - rozmiar tablicy
            instructions.add(new MovInstruction(Register.RDI, MemoryReference.getRaw(Register.RAX)));
            instructions.add(new MovInstruction(Register.RCX, Register.RAX));

            Label start = LabelsGenerator.getNonceLabel("str_arr_init");
            Label end = LabelsGenerator.getNonceLabel("str_arr_init_end");
            instructions.add(start);
            instructions.add(new CompareInstruction(Register.RDI, YieldUtils.number(0)));
            instructions.add(new JumpInstruction(end, JumpInstruction.Type.EQU));
            instructions.add(new AddInstruction(Register.RCX, YieldUtils.number(WORD_SIZE)));

            instructions.add(new PushInstruction(Register.RDX));
            instructions.add(new PushInstruction(Register.RCX));
            instructions.add(new PushInstruction(Register.RDI));

            instructions.addRegisters(callUnsafeRegs());
            instructions.add(new CallInstruction(ExternalFunctions.EMPTY_STRING));

            instructions.add(new PopInstruction(Register.RDI));
            instructions.add(new PopInstruction(Register.RCX));
            instructions.add(new PopInstruction(Register.RDX));

            instructions.add(new MovInstruction(MemoryReference.getRaw(Register.RCX), Register.RAX));
            instructions.add(new SubInstruction(Register.RDI, YieldUtils.number(1)));
            instructions.add(new JumpInstruction(start, JumpInstruction.Type.ALWAYS));
            instructions.add(end);
            // resotre RAX
            instructions.add(new MovInstruction(Register.RAX, Register.RDX));
        }

        if (!destRegister.equals(Register.RAX)) {
            instructions.add(new MovInstruction(destRegister, Register.RAX));
        }

        return  instructions;
    }

    public static Instructions generateArrAcc(ENDArrAcc acc, String destRegister, BackendScope scope) {
        Instructions instructions = new Instructions();
        Instructions expr1instructions = generateExpr(acc.expr_1, Register.RAX, Register.RAX, scope);
        Instructions expr2instructions = generateExpr(acc.expr_2, Register.RAX, Register.RAX, scope);

        instructions.addRegister(Register.RCX);

        instructions.addAll(expr1instructions);
        instructions.add(new MovInstruction(Register.RCX, Register.RAX));

        boolean expr2destroysRcx = expr2instructions.usedRegisters.contains(Register.RCX);

        if (expr2destroysRcx) {
            instructions.add(new PushInstruction(Register.RCX));
        }

        instructions.addAll(expr2instructions);

        if (expr2destroysRcx) {
            instructions.add(new PopInstruction(Register.RCX));
        }

        instructions.add(new MovInstruction(Register.RAX, MemoryReference.getWithConstOffset(Register.RCX, Register.RAX, 8 , WORD_SIZE)));


        if (!destRegister.equals(Register.RAX)) {
            instructions.add(new MovInstruction(destRegister, Register.RAX));
        }

        instructions.addRegisters(expr1instructions.usedRegisters);
        instructions.addRegisters(expr2instructions.usedRegisters);
        instructions.addRegister(Register.RCX);

        return instructions;
    }

    public static Instructions generateObjAcc(EObjAcc objAcc, String destRegister, BackendScope scope) {
        Instructions instructions = new Instructions();
        Instructions exprInstructions = generateExpr(objAcc.expr_, Register.RAX, Register.RAX, scope);

        instructions.addAll(exprInstructions);

        Instructions accInstructions = objAcc.objacc_.match(
                (fieldAcc) -> generateFieldAcc(fieldAcc, objAcc.expr_.type, Register.RAX, scope),
                (methAcc) -> generateMthAcc(methAcc, objAcc.expr_.type, Register.RAX, scope)
        );

        instructions.addAll(accInstructions);

        instructions.addRegisters(exprInstructions.usedRegisters);
        instructions.addRegisters(accInstructions.usedRegisters);

        return instructions;
    }

    public static Instructions generateFieldAcc(ObjFieldAcc acc, TypeDefinition type, String destRegister, BackendScope scope) {
        // object in RAX, just take the field

        if (type.isArrayType()) {
            return generateArrayFieldAcc(acc, type, destRegister, scope);
        }

        if (type.isClassType()) {
            return generateClassFieldAcc(acc, type, destRegister, scope);
        }

        return new Instructions();
    }

    public static Instructions generateClassFieldAcc(ObjFieldAcc acc, TypeDefinition type, String destRegister, BackendScope scope) {
        // base object's address already in RAX
        return generateClassFieldAcc(acc.ident_, type, destRegister, scope);
    }

    public static Instructions generateClassFieldAcc(String fieldName, TypeDefinition type, String destRegister, BackendScope scope) {
        Instructions instructions = new Instructions();
        int offset = type.getClassDefinition().getFieldOffset(fieldName);

        instructions.add(new MovInstruction(Register.RAX, MemoryReference.getWithOffset(Register.RAX, offset)));

        return instructions;
    }

    public static Instructions generateArrayFieldAcc(ObjFieldAcc acc, TypeDefinition type, String destRegister, BackendScope scope) {
        // expression result in RAX
        // it has to be length field acc, checked in typecheck

        Instructions instructions = new Instructions();

        // length of an array is stored in first 8 bytes
        instructions.add(new MovInstruction(destRegister, MemoryReference.getRaw(Register.RAX)));

        return instructions;
    }

    public static Instructions generateEConstr(EConstr constr, String destRegister, BackendScope scope) {
        int size = scope.getType(constr.ident_).getClassDefinition().getClassSize();

        Instructions instructions = new Instructions();
        instructions.add(new MovInstruction(Register.RDI, YieldUtils.number(size * WORD_SIZE)));

        instructions.add(new CallInstruction(ExternalFunctions.MALLOC_SIZE));

        if (!Register.RAX.equals(destRegister)) {
            instructions.add(new MovInstruction(destRegister, Register.RAX));
        }

        instructions.addRegister(Register.RDI);
        instructions.addRegisters(callUnsafeRegs());

        return instructions;
    }

    public static Instructions generateMthAcc(ObjMethAcc acc, TypeDefinition type, String destRegister, BackendScope scope) {
        Instructions instructions = new Instructions();

        int numberOfArgsPassedOnStack = max(0, (acc.listexpr_.size() - 5)); // - 6 + 1 - dodajemy "this" jako pierwszy argument
        int numberOfTempsOnStack = scope.getNumberOfTempsOnStack();

        boolean hasToAlignStack = (numberOfArgsPassedOnStack + numberOfTempsOnStack) % 2 != 0;

        if (hasToAlignStack) {
            instructions.add(new Comment("aligning stack"));
            instructions.add(new SubInstruction(Register.RSP, YieldUtils.number(WORD_SIZE)));
        }

        // object which method is called upon is in rax
        instructions.add(new MovInstruction(Register.RDI, Register.RAX));

        if (type.isInterfaceType()) {
            // TODO: put to rdi runtime class of object, not the whole interface
        }

        Instructions genMethArgsInstr = generateMethodArguments(acc.listexpr_, scope);
        instructions.addAll(genMethArgsInstr);

        //TODO: change to support interfaces, take offset no from typedef, but from runtime object
        instructions.add(new CallInstruction(type.getClassDefinition().methods.get(acc.ident_).label.getLabelName()));

        // function args pushed on stack?
        int offset = max(0, (acc.listexpr_.size() - 5)) * WORD_SIZE;

        if (offset > 0) {
            instructions.add(new AddInstruction(Register.RSP, YieldUtils.number(offset)));
            scope.changeTempsOnStack(-offset);
        }

        if (hasToAlignStack) {
            instructions.add(new AddInstruction(Register.RSP, YieldUtils.number(WORD_SIZE)));
        }

        instructions.addRegisters(genMethArgsInstr.usedRegisters);
        instructions.addRegisters(callUnsafeRegs());
        instructions.addRegister(Register.RDI);

        return instructions;
    }

    public static Instructions generateThis(String destRegister, BackendScope scope) {
        VariableCompilerInfo varInfo = scope.getVariable(THIS_KEYWORD);

        int offset = varInfo.getOffset() * WORD_SIZE;

        Instructions instructions = new Instructions();
        instructions.add(new MovInstruction(destRegister, MemoryReference.getWithOffset(Register.RBP, offset)));
        return instructions;
    }


    public static Instructions notImplemented(Expr expr) {
        throw new CompilerException("Compiling expression " + expr.getClass() + " not implemented yet");
    }

}
