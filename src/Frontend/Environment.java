package src.Frontend;

import src.Backend.Definitions.ExternalFunctions;
import src.Backend.Instructions.Label;
import src.Definitions.BasicTypeDefinition;
import src.Definitions.FunctionDeclaration;
import src.Definitions.TypeDefinition;
import src.Exceptions.InternalStateException;
import src.Exceptions.TypeCheckException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Environment {
    public static List<TypeDefinition> basicTypes = Arrays.asList(
            BasicTypeDefinition.INT,
            BasicTypeDefinition.BOOLEAN,
            BasicTypeDefinition.STRING,
            BasicTypeDefinition.VOID
    );

    public static List<FunctionDeclaration> basicFunctions = Arrays.asList(
            ExternalFunctions.PRINT_INT,
            ExternalFunctions.PRINT_STRING,
            ExternalFunctions.READ_INT,
            ExternalFunctions.READ_STRING,
            ExternalFunctions.ERROR
    );

    public Map<String, TypeDefinition> declaredTypes;
    public Map<String, FunctionDeclaration> declaredFunctions;
    public Map<String, Label> stringLiterals;

    public Environment() {
        this.declaredTypes = new HashMap<>();
        this.declaredFunctions = new HashMap<>();
        this.stringLiterals = new HashMap<>();
    }

    public Environment withBasicTypes() {

        for (TypeDefinition type : basicTypes) {
            declareType(type.getName(), type);
        }

        return this;
    }

    public Environment withBasicFunctions() {
        for (FunctionDeclaration func : basicFunctions) {
            declareFunction(func.getName(), func);
        }

        return this;
    }


    public void declareType(String name, TypeDefinition def) {
        if (declaredTypes.containsKey(name)) {
            throw new IllegalArgumentException("Type " + name + " is already declared");
        }

        declaredTypes.put(name, def);
    }

    public void declareFunction(String name, FunctionDeclaration dec) {
        if (declaredFunctions.containsKey(name)) {
            throw new IllegalArgumentException("Function " + name + " is already declared");
        }

        declaredFunctions.put(name, dec);
    }

    public FunctionDeclaration getFunction(String name, int lineNumber, int colNumber) {
        if (!declaredFunctions.containsKey(name)) {
            throw new TypeCheckException("Function " + name + " not declared", lineNumber, colNumber);
        }

        return declaredFunctions.get(name);
    }

    public void declareStringLiteral(String str) {
        this.stringLiterals.put(str, null);
    }

    public void declareStringLiteral(String str, Label label) {
        this.stringLiterals.put(str, label);
    }

    public Label getStringLabel(String s) {
        if (!this.stringLiterals.containsKey(s)) {
            throw new InternalStateException("Env is supposed to have string literal declared");
        }

        return this.stringLiterals.get(s);
    }
}
