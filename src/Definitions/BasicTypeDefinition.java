package src.Definitions;

import java.util.Objects;

public class BasicTypeDefinition implements TypeDefinition, Arrayable {
    public static BasicTypeDefinition INT = new BasicTypeDefinition(BasicTypeName.INT);
    public static BasicTypeDefinition STRING = new BasicTypeDefinition(BasicTypeName.STRING);
    public static BasicTypeDefinition VOID = new BasicTypeDefinition(BasicTypeName.VOID);
    public static BasicTypeDefinition BOOLEAN = new BasicTypeDefinition(BasicTypeName.BOOLEAN);

    private BasicTypeName typeName;

    @Override
    public String toString() {
        return "BasicTypeDefinition{" +
                "typeName=" + typeName +
                '}';
    }

    public BasicTypeDefinition(BasicTypeName tn) {
        this.typeName = tn;
    }

    public boolean isOfType(BasicTypeName typeName) {
        return this.typeName == typeName;
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
    public boolean isNullType() {
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
    public ArrayTypeDefinition getArrayTypeDefinition() {
        return null;
    }

    @Override
    public BasicTypeDefinition getBasicTypeDefinition() {
        return this;
    }

    @Override
    public TypeDefinition getOwnType() {
        return this;
    }
}
