package Latte.Frontend;

import Latte.Definitions.TypeDefinition;
import Latte.Definitions.VariableDefinition;
import Latte.Exceptions.IllegalTypeException;
import Latte.Exceptions.TypeCheckException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FrontendScope {
    public Environment globalEnvironment;
    public Map<String, VariableDefinition> declaredVariables;
    public Map<String, VariableDefinition> scopesDeclaredVariables;

    public void declareVariable(String name, String type) {
        if (!globalEnvironment.declaredTypes.containsKey(type)) {
            throw new IllegalArgumentException("Unknown type " + type);
        }

        if (scopesDeclaredVariables.containsKey(name)) {
            throw new IllegalArgumentException("Variable " + name + " already declared in the scope");
        }

        VariableDefinition var = new VariableDefinition(name, globalEnvironment.declaredTypes.get(type));
        declaredVariables.put(name, var);
        scopesDeclaredVariables.put(name, var);
    }

    public void declareVariable(String name, TypeDefinition type, int lineNumber, int colNumber) {
        declareVariable(new VariableDefinition(name, type), lineNumber, colNumber);
    }

    public void declareVariable(VariableDefinition var, int lineNumber, int colNumber) {
        if (scopesDeclaredVariables.containsKey(var.getVariableName())) {
            throw new TypeCheckException("Variable " + var.getVariableName() + " already declared in the scope", lineNumber, colNumber);
        }

        declaredVariables.put(var.getVariableName(), var);
        scopesDeclaredVariables.put(var.getVariableName(), var);
    }

    public VariableDefinition getVariable(String name, int lineNumber, int colNumber) {
        if (!declaredVariables.containsKey(name)) {
            throw new TypeCheckException("Variable " + name + " not declared", lineNumber, colNumber);
        }

        return declaredVariables.get(name);
    }

    public TypeDefinition getType(String name, int lineNumber, int colNumber) {
        if (!globalEnvironment.declaredTypes.containsKey(name)) {
            throw new IllegalTypeException(name, lineNumber, colNumber);
        }

        return globalEnvironment.declaredTypes.get(name);
    }

    public FrontendScope(Environment globalEnvironment) {
        this.globalEnvironment = globalEnvironment;
        this.declaredVariables = new HashMap<>();
        this.scopesDeclaredVariables = new HashMap<>();
    }

    public FrontendScope(FrontendScope that) {
        this.globalEnvironment = that.globalEnvironment;
        this.declaredVariables = new HashMap<>(that.declaredVariables);
        this.scopesDeclaredVariables = new HashMap<>();
    }

    public FrontendScope withVariables(List<VariableDefinition> variables, int lineNumber, int colNumber) {
        for (VariableDefinition var : variables) {
            declareVariable(var, lineNumber, colNumber);
        }

        return this;
    }
}
