package Latte.Frontend;

import Latte.Definitions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Environment {
    public static List<TypeDefinition> basicTypes = Arrays.asList(
            new BasicTypeDefinition(BasicTypeName.INT),
            new BasicTypeDefinition(BasicTypeName.BOOLEAN),
            new BasicTypeDefinition(BasicTypeName.STRING),
            new BasicTypeDefinition(BasicTypeName.VOID)
    );

    public Map<String, TypeDefinition> declaredTypes;
    public Map<String, FunctionDeclaration> declaredFunctions;
    public Map<String, VariableDefinition> declaredVariables;

    public void declareVariable(String name, String type) {
        if (!declaredTypes.containsKey(type)) {
            throw new IllegalArgumentException("Unknown type " + type);
        }

        if (declaredVariables.containsKey(name)) {
            throw new IllegalArgumentException("Variable " + name + " already declared");
        }

        declaredVariables.put(name, new VariableDefinition(name, declaredTypes.get(type)));
    }

    public Environment withBasicTypes() {

        for (TypeDefinition type : basicTypes) {
            declareType(type.getName(), type);
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
