package Latte.Definitions;

import java.util.Objects;

public class ArrayTypeDefinition implements TypeDefinition {
    private TypeDefinition typeDefinition;

    public ArrayTypeDefinition(TypeDefinition td) {
        if (td.isArrayType()) {
            throw new IllegalArgumentException("Can't create array of arrays");
        }
        this.typeDefinition = td;
    }

    @Override
    public String getName() {
        return typeDefinition.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArrayTypeDefinition that = (ArrayTypeDefinition) o;
        return typeDefinition.equals(that.typeDefinition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeDefinition);
    }

    @Override
    public boolean isBasicType() {
        return false;
    }

    @Override
    public boolean isClassType() {
        return false;
    }

    @Override
    public boolean isArrayType() {
        return true;
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
}
