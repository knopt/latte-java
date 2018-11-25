package Latte.Definitions;

import java.util.Objects;

public class VariableDefinition {
    public String variableName;
    public TypeDefinition type;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VariableDefinition that = (VariableDefinition) o;
        return Objects.equals(variableName, that.variableName) &&
                Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(variableName, type);
    }

    public String getVariableName() {
        return variableName;
    }

    public TypeDefinition getType() {
        return type;
    }

    public VariableDefinition(String variableName, TypeDefinition type) {
        this.variableName = variableName;
        this.type = type;
    }
}