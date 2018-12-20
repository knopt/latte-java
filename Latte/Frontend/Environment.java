package Latte.Frontend;

import Latte.Backend.Instructions.Label;
import Latte.Definitions.BasicTypeDefinition;
import Latte.Definitions.FunctionDeclaration;
import Latte.Definitions.TypeDefinition;
import Latte.Definitions.VariableDefinition;
import Latte.Exceptions.InternalStateException;
import Latte.Exceptions.TypeCheckException;

import java.util.*;

public class Environment {
    public static List<TypeDefinition> basicTypes = Arrays.asList(
            BasicTypeDefinition.INT,
            BasicTypeDefinition.BOOLEAN,
            BasicTypeDefinition.STRING,
            BasicTypeDefinition.VOID
    );

    public static List<FunctionDeclaration> basicFunctions = Arrays.asList(
            new FunctionDeclaration(
                    "printInt",
                    Arrays.asList(new VariableDefinition("x", BasicTypeDefinition.INT)),
                    null,
                    BasicTypeDefinition.VOID).setExternal(true),
            new FunctionDeclaration(
                    "printString",
                    Arrays.asList(new VariableDefinition("x", BasicTypeDefinition.STRING)),
                    null,
                    BasicTypeDefinition.VOID).setExternal(true),
            new FunctionDeclaration(
                    "error",
                    Collections.emptyList(),
                    null,
                    BasicTypeDefinition.VOID).setExternal(true),
            new FunctionDeclaration(
                    "readInt",
                    Collections.emptyList(),
                    null,
                    BasicTypeDefinition.INT).setExternal(true),
            new FunctionDeclaration(
                    "readString",
                    Collections.emptyList(),
                    null,
                    BasicTypeDefinition.STRING
            ).setExternal(true)
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
