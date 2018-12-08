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

    public Environment getGlobalEnvironment() {
        return globalEnvironment;
    }

    public void declareVariable(String name, TypeDefinition type) {
        VariableCompilerInfo info = new VariableCompilerInfo(name, type, declaredVariables.size() + 1);

        declaredVariables.put(name, info);
        scopesDeclaredVariables.put(name, info);
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
    }

    public BackendScope(BackendScope that) {
        this.globalEnvironment = that.globalEnvironment;
        this.declaredVariables = new HashMap<>(that.declaredVariables);
        this.scopesDeclaredVariables = new HashMap<>();
    }

    public BackendScope withVariables(List<VariableCompilerInfo> variables) {
        // TODO: handle function parametes

        return this;
    }
}
