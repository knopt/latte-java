package src.Definitions;

import java.util.Objects;

public class ClassFieldDeclaration {
    public String fieldName;
    public TypeDefinition type;

    public ClassFieldDeclaration(String fieldName, TypeDefinition type) {
        this.fieldName = fieldName;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassFieldDeclaration that = (ClassFieldDeclaration) o;
        return Objects.equals(fieldName, that.fieldName) &&
                Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fieldName, type.getName());
    }

    public String getFieldName() {
        return fieldName;
    }

    public TypeDefinition getType() {
        return type;
    }
}
