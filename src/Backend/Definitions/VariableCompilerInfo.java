package src.Backend.Definitions;

import src.Definitions.TypeDefinition;

import java.util.Objects;

public class VariableCompilerInfo {
    private String name;
    private TypeDefinition type;
    private int offset; // not in bytes, in numer of variables;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VariableCompilerInfo that = (VariableCompilerInfo) o;
        return offset == that.offset &&
                Objects.equals(name, that.name) &&
                Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, offset);
    }

    public String getName() {
        return name;
    }

    public TypeDefinition getType() {
        return type;
    }

    public int getOffset() {
        return offset;
    }

    public VariableCompilerInfo(String name, TypeDefinition type, int offset) {
        this.name = name;
        this.type = type;
        this.offset = offset;
    }
}
