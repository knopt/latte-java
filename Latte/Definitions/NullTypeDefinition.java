package Latte.Definitions;

import java.util.Objects;

public class NullTypeDefinition implements TypeDefinition {

    @Override
    public String getName() {
        return "null";
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
        return false;
    }

    @Override
    public boolean isInterfaceType() {
        return false;
    }

    @Override
    public boolean isNullType() {
        return true;
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
    public ArrayTypeDefinition getArrayTypeDefinition() {
        return null;
    }

    @Override
    public BasicTypeDefinition getBasicTypeDefinition() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return o != null && getClass() == o.getClass();
    }
}
