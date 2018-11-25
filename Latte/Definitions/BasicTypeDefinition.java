package Latte.Definitions;

import java.util.Objects;

public class BasicTypeDefinition implements TypeDefinition, Arrayable {
    private BasicTypeName typeName;

    public BasicTypeDefinition(BasicTypeName tn) {
        this.typeName = tn;
    }

    @Override
    public String getName() {
        return typeName.toString().toLowerCase();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasicTypeDefinition that = (BasicTypeDefinition) o;
        return typeName == that.typeName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeName);
    }

    @Override
    public boolean isBasicType() {
        return true;
    }

    @Override
    public boolean isClassType() {
        return false;
    }

    @Override
    public boolean isArrayType() {
        return false;
    }

    @Override
    public boolean isInterfaceType() {
        return false;
    }

    @Override
    public ClassTypeDefinition getClassDefinition() {
        return null;
    }

    @Override
    public InterfaceTypeDefinition getInterfaceDefinition() {
        return null;
    }

    @Override
    public TypeDefinition getOwnType() {
        return this;
    }
}
