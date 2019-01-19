package src.Frontend;

import src.Absyn.*;
import src.Definitions.*;
import src.Exceptions.IllegalTypeException;
import src.Exceptions.InternalStateException;
import src.Exceptions.TypeCheckException;

import java.util.List;
import java.util.stream.Collectors;

import static src.Frontend.TypeCheckStatement.checkCallableReturns;
import static src.Frontend.TypeCheckStatement.typeCheckStatement;
import static src.Frontend.TypeUtils.getType;

public class TypeCheck {
    public static Environment env = new Environment().withBasicTypes().withBasicFunctions();

    public static Environment check(Program program) {
        program.match(TypeCheck::gatherTypeDeclarations);
        program.match(TypeCheck::gatherInterfaceDefinitions);
        program.match(TypeCheck::gatherTypeDefinitions);
        program.match(TypeCheck::gatherFunctionsDefinitions);
        program.match(TypeCheck::typeCheckMethodsAndFuntions);

        if (!env.declaredFunctions.containsKey("main")) {
            throw new TypeCheckException("Missing main funtion");
        }

        if (!BasicTypeDefinition.INT.equals(env.declaredFunctions.get("main").getReturnType())) {
            throw new TypeCheckException("Main function has to return int");
        }

        return env;
    }

    public static Boolean gatherFunctionsDefinitions(ProgramTD program) {
        for (TopDef def : program.listtopdef_) {
            FunctionDeclaration functionDeclaration = def.match(
                    TypeCheck::gatherFunctionsDefinitions,
                    (ignored) -> null
            );

            if (functionDeclaration != null) {
                env.declareFunction(functionDeclaration.getName(), functionDeclaration);
            }
        }

        return true;
    }


    public static Boolean gatherTypeDeclarations(ProgramTD program) {
        for (TopDef def : program.listtopdef_) {
            def.match(
                    (ignored) -> true,
                    TypeCheck::gatherTypeDeclarations
            );
        }
        return true;
    }

    public static Boolean gatherTypeDeclarations(ClassDecl classDecl) {
        TypeDefinition typeDef = classDecl.classheader_.match(
                TypeCheck::gatherTypeDeclarations,
                TypeCheck::gatherTypeDeclarations
        );

        env.declareType(typeDef.getName(), typeDef);
        return true;
    }

    public static TypeDefinition gatherTypeDeclarations(ClassDec classDec) {
//        if 

        return new ClassTypeDefinition(classDec.ident_);
    }

    public static TypeDefinition gatherTypeDeclarations(InterDec interDec) {
        if (Character.isLowerCase(interDec.ident_.charAt(0))) {
            throw new TypeCheckException("Interface name should start with upper case", interDec.line_num, interDec.col_num);
        }

        return new InterfaceTypeDefinition(interDec.ident_);
    }

    public static Boolean gatherInterfaceDefinitions(ProgramTD programTD) {
        for (TopDef def : programTD.listtopdef_) {
            def.match(
                    (ignored) -> true,
                    TypeCheck::gatherInterfaceDefinitions
            );
        }
        return true;
    }

    public static Boolean gatherInterfaceDefinitions(ClassDecl classDecl) {
        TypeDefinition typeDef = classDecl.classheader_.match(
                (ignored) -> null,
                TypeCheck::gatherInterfaceDefinitions
        );

        if (typeDef == null) {
            return true;
        }

        for (FieldDeclaration field : classDecl.listfielddeclaration_) {
            MethodDeclaration funcDecl = field.match(
                    TypeCheck::gatherInterfaceDefinitions,
                    (dmth) -> gatherInterfaceDefinitions(dmth, typeDef)
            );

            if (typeDef.getInterfaceDefinition().methods.containsKey(funcDecl.getName())) {
                throw new TypeCheckException("Method " + funcDecl.getName() + " already declared", classDecl.line_num, classDecl.col_num);
            }

            typeDef.getInterfaceDefinition().addMethod(funcDecl.getName(), funcDecl);
        }

        return true;
    }

    public static TypeDefinition gatherInterfaceDefinitions(InterDec interDecl) {
        if (!env.declaredTypes.containsKey(interDecl.ident_)) {
            throw new InternalStateException("Interface type should have been previously gathered");
        }

        return env.declaredTypes.get(interDecl.ident_);
    }


    public static MethodDeclaration gatherInterfaceDefinitions(Dvar dVar) {
        throw new TypeCheckException("Interface can't have any fields", dVar.line_num, dVar.col_num);
    }

    public static MethodDeclaration gatherInterfaceDefinitions(Dmth dMth, TypeDefinition callerType) {
        if (!dMth.methodbody_.match(
                TypeCheck::isMethodBodyEmpty,
                TypeCheck::isMethodBodyEmpty
        )) {
            throw new TypeCheckException("Interface body has to be empty", dMth.line_num, dMth.col_num);
        }

        return getMethodDefinitionFromDMth(dMth, callerType);
    }

    public static Boolean isMethodBodyEmpty(EmptyMBody ignored) {
        return true;
    }

    public static Boolean isMethodBodyEmpty(MBody ignored) {
        return false;
    }

    public static MethodDeclaration getMethodDefinitionFromDMth(Dmth dMth, TypeDefinition callerType) {
        TypeDefinition returnType = dMth.type_.match(
                (arrayType) -> getType(arrayType, env),
                (typeNameS) -> getType(typeNameS, env)
        );

        String methodName = dMth.ident_;

        List<VariableDefinition> arguments = dMth.listarg_.stream().map(
                (arg) -> arg.match(TypeCheck::getVariable)
        ).collect(Collectors.toList());

        if (arguments.stream().anyMatch((var) -> BasicTypeDefinition.VOID.equals(var.getType()))) {
            throw new TypeCheckException("Function argument can't be of type void", dMth.line_num, dMth.col_num);
        }

        return new MethodDeclaration(methodName, arguments, dMth.methodbody_, returnType, callerType);
    }


    public static FunctionDeclaration gatherFunctionsDefinitions(FnDef fnDef) {
        TypeDefinition returnType = fnDef.type_.match(
                (arrayType) -> getType(arrayType, env),
                (typeNameS) -> getType(typeNameS, env)
        );

        String functionName = fnDef.ident_;

        List<VariableDefinition> arguments = fnDef.listarg_.stream().map(
                (arg) -> arg.match(TypeCheck::getVariable)
        ).collect(Collectors.toList());

        if (arguments.stream().anyMatch((var) -> BasicTypeDefinition.VOID.equals(var.getType()))) {
            throw new TypeCheckException("Function argument can't be of type void", fnDef.line_num, fnDef.col_num);
        }

        return new FunctionDeclaration(functionName, arguments, fnDef.block_, returnType);
    }

    public static VariableDefinition getVariable(ArgTI arg) {
        TypeDefinition argumentType = arg.type_.match(
                (arrayType) -> getType(arrayType, env),
                (typeNameS) -> getType(typeNameS, env)
        );

        return new VariableDefinition(arg.ident_, argumentType);
    }


    public static Boolean gatherTypeDefinitions(ProgramTD programTD) {
        for (TopDef def : programTD.listtopdef_) {
            def.match(
                    (ignored) -> true,
                    TypeCheck::gatherTypeDefinitions
            );
        }
        return true;
    }

    public static Boolean gatherTypeDefinitions(ClassDecl classDecl) {
        TypeDefinition typeDef = classDecl.classheader_.match(
                TypeCheck::gatherTypeDefinitions,
                (ignored) -> null
        );

        Implements impl = classDecl.classheader_.match(
                classDec -> classDec.implements_,
                (ignored) -> null
        );

        if (typeDef == null || impl == null) {
            return true;
        }

        for (FieldDeclaration field : classDecl.listfielddeclaration_) {
            field.match(
                    (var) -> assignFieldDeclaration(typeDef, var),
                    (mth) -> assignMethodDeclaration(typeDef, mth)
            );

        }

        ClassTypeDefinition classTypeDefinition = typeDef.getClassDefinition();
        InterfaceTypeDefinition interfaceTypeDefinition = impl.match(
                TypeCheck::getInterfaceType,
                (ignored) -> null
        );

        if (interfaceTypeDefinition == null) {
            return true;
        }

        if (!interfaceTypeDefinition.isImplementedBy(classTypeDefinition)) {
            throw new TypeCheckException("Class " + classTypeDefinition.getName() + " doesn't implement all of interface "
                    + interfaceTypeDefinition.getName() + " methods", classDecl.line_num, classDecl.col_num);
        }

        classTypeDefinition.addInterface(interfaceTypeDefinition);

        return true;
    }

    public static InterfaceTypeDefinition getInterfaceType(InterImpl interImpl) {
        if (!env.declaredTypes.containsKey(interImpl.ident_)) {
            throw new IllegalTypeException(interImpl.ident_, interImpl.line_num, interImpl.col_num);
        }

        TypeDefinition typeDefinition = env.declaredTypes.get(interImpl.ident_);

        if (!typeDefinition.isInterfaceType()) {
            throw new TypeCheckException("Type " + interImpl.ident_ + " is not an interface type", interImpl.line_num, interImpl.col_num);
        }

        return typeDefinition.getInterfaceDefinition();
    }

    public static TypeDefinition gatherTypeDefinitions(ClassDec classDecl) {
        if (!env.declaredTypes.containsKey(classDecl.ident_)) {
            throw new InternalStateException("Class type should have been previously gathered");
        }

        return env.declaredTypes.get(classDecl.ident_);
    }

    public static Boolean assignFieldDeclaration(TypeDefinition typeDefinition, Dvar var) {
        ClassTypeDefinition classDefinition = typeDefinition.getClassDefinition();

        TypeDefinition fieldType = var.type_.match(
                (arrayType) -> getType(arrayType, env),
                (typeNameS) -> getType(typeNameS, env)
        );

        for (Item i : var.listitem_) {
            String variableName = i.match(
                    (noInit) -> noInit.ident_,
                    TypeCheck::staticFieldsInitialization
            );

            ClassFieldDeclaration declaration = new ClassFieldDeclaration(variableName, fieldType);

            if (classDefinition.fields.containsKey(variableName)) {
                throw new TypeCheckException("Class field with name " + variableName + " previously declared", var.line_num, var.col_num);
            }

            classDefinition.addField(variableName, declaration);
        }

        return true;
    }

    public static Boolean assignMethodDeclaration(TypeDefinition typeDefinition, Dmth mth) {
        ClassTypeDefinition classDefinition = typeDefinition.getClassDefinition();

        MethodDeclaration mthDeclaration = getMethodDefinitionFromDMth(mth, classDefinition);

        if (classDefinition.methods.containsKey(mth.ident_)) {
            throw new TypeCheckException("Method " + mth.ident_ + " already declared", mth.line_num, mth.col_num);
        }

        classDefinition.addMethod(mthDeclaration.name, mthDeclaration);

        return true;
    }

    public static String staticFieldsInitialization(Init init) {
        throw new TypeCheckException("Static initialization not supported", init.line_num, init.col_num);
    }

    public static Boolean typeCheckMethodsAndFuntions(ProgramTD p) {
        for (TypeDefinition def : env.declaredTypes.values()) {
            if (!def.isClassType()) {
                continue;
            }

            ClassTypeDefinition classTypeDefinition = def.getClassDefinition();

            for (CallableDeclaration mth : classTypeDefinition.methods.values()) {
                typeCheckCallable(mth);
            }

        }

        for (FunctionDeclaration fun : env.declaredFunctions.values()) {
            if (!Environment.basicFunctions.contains(fun)) {
                typeCheckCallable(fun);
            }
        }

        return true;
    }

    public static void typeCheckCallable(CallableDeclaration callableDeclaration) {
        callableDeclaration.getMethodBody().match(
                TypeCheck::typeCheckCallable,
                (body) -> typeCheckCallable(body, callableDeclaration)
        );

        callableDeclaration.setNumberOfVariables(LocalVariablesCounter.callableVariablesCounter(callableDeclaration));
    }

    public static Boolean typeCheckCallable(EmptyMBody emptyMBody) {
        throw new TypeCheckException("Empty body is not allowed", emptyMBody.line_num, emptyMBody.col_num);
    }

    public static Boolean typeCheckCallable(MBody mBody, CallableDeclaration callableDeclaration) {
        try {
            mBody.block_.match((block) -> checkCallableReturns(block, callableDeclaration));
        } catch (Exception e) {
            if (callableDeclaration.getReturnType().equals(BasicTypeDefinition.VOID)) {
                mBody.block_.match((blockS) -> { blockS.liststmt_.addLast(new VRet()); return null;});
            } else {
                throw e;
            }
        }

        mBody.block_.match((block) -> typeCheckCallablesBlock(block, callableDeclaration));

        return true;
    }

    public static Boolean typeCheckCallablesBlock(BlockS block, CallableDeclaration callableDeclaration) {
        FrontendScope scope = new FrontendScope(env).withVariables(callableDeclaration.getArgumentList(), block.line_num, block.col_num);

        for (Stmt stmt : block.liststmt_) {
            typeCheckStatement(
                    stmt,
                    scope,
                    callableDeclaration
            );
        }

        return true;
    }
}
