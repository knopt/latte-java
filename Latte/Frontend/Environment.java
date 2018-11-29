package Latte.Frontend;

import Latte.Definitions.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Environment {
    public static List<TypeDefinition> basicTypes = Arrays.asList(
            new BasicTypeDefinition(BasicTypeName.INT),
            new BasicTypeDefinition(BasicTypeName.BOOLEAN),
            new BasicTypeDefinition(BasicTypeName.STRING),
            new BasicTypeDefinition(BasicTypeName.VOID)
    );

    public static List<FunctionDeclaration> basicFunctions = Arrays.asList(
            new FunctionDeclaration(
                    "printInt",
                    Arrays.asList(new VariableDefinition("x", new BasicTypeDefinition(BasicTypeName.INT))),
                    null,
                    new BasicTypeDefinition(BasicTypeName.VOID)),
            new FunctionDeclaration(
                    "printString",
                    Arrays.asList(new VariableDefinition("x", new BasicTypeDefinition(BasicTypeName.STRING))),
                    null,
                    new BasicTypeDefinition(BasicTypeName.VOID))
    );

    public Map<String, TypeDefinition> declaredTypes;
    public Map<String, FunctionDeclaration> declaredFunctions;

    public Environment() {
        this.declaredTypes = new HashMap<>();
        this.declaredFunctions = new HashMap<>();
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
}
