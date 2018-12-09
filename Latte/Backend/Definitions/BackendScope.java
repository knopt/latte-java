package Latte.Backend.Definitions;

import Latte.Definitions.TypeDefinition;
import Latte.Definitions.VariableDefinition;
import Latte.Exceptions.IllegalTypeException;
import Latte.Exceptions.TypeCheckException;
import Latte.Frontend.Environment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BackendScope {
    private Environment globalEnvironment;
    private Map<String, VariableCompilerInfo> declaredVariables;
    private Map<String, VariableCompilerInfo> scopesDeclaredVariables;
    private int numberOfVarsOnStack;

    public Environment getGlobalEnvironment() {
        return globalEnvironment;
    }

    public int getNumberOfVariablesOnStack() {
        return numberOfVarsOnStack;
    }

    public void declareVariable(String name, TypeDefinition type) {
        VariableCompilerInfo info = new VariableCompilerInfo(name, type, -1 * (numberOfVarsOnStack + 1));

        declaredVariables.put(name, info);
        scopesDeclaredVariables.put(name, info);
        numberOfVarsOnStack++;
    }

    public void declareVariable(String name, TypeDefinition type, int offset, boolean onFuncStack) {
        VariableCompilerInfo info = new VariableCompilerInfo(name, type, offset);

        declaredVariables.put(name, info);
        scopesDeclaredVariables.put(name, info);

        if (onFuncStack) {
            this.numberOfVarsOnStack++;
        }
    }


    public VariableCompilerInfo getVariable(String name) {
        if (!declaredVariables.containsKey(name)) {
            throw new TypeCheckException("[INTERNAL ERROR] Variable " + name + " not declared");
        }

        return declaredVariables.get(name);
    }

    public TypeDefinition getType(String name) {
        if (!globalEnvironment.declaredTypes.containsKey(name)) {
            throw new IllegalTypeException(name, -1, -1);
        }

        return globalEnvironment.declaredTypes.get(name);
    }

    public BackendScope(Environment globalEnvironment) {
        this.globalEnvironment = globalEnvironment;
        this.declaredVariables = new HashMap<>();
        this.scopesDeclaredVariables = new HashMap<>();
        this.numberOfVarsOnStack = 0;
    }

    public BackendScope(BackendScope that) {
        this.globalEnvironment = that.globalEnvironment;
        this.declaredVariables = new HashMap<>(that.declaredVariables);
        this.scopesDeclaredVariables = new HashMap<>();
        this.numberOfVarsOnStack = that.numberOfVarsOnStack;
    }

    public BackendScope withVariables(List<VariableCompilerInfo> variables) {
        // TODO: handle function parametes

        return this;
    }
}
