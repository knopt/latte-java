package Latte.Definitions;

import Latte.Exceptions.TypeCheckException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ArrayTypeDefinition implements TypeDefinition {
    private TypeDefinition typeDefinition;
    public Map<String, ClassFieldDeclaration> fields;

    public ArrayTypeDefinition(TypeDefinition td) {
        if (td.isArrayType()) {
            throw new IllegalArgumentException("Can't create array of arrays");
        }
        this.typeDefinition = td;

        ClassFieldDeclaration lengthField = new ClassFieldDeclaration("length", new BasicTypeDefinition(BasicTypeName.INT));
        this.fields = new HashMap<>();
        this.fields.put(lengthField.fieldName, lengthField);
    }

    @Override
    public String getName() {
        return typeDefinition.getName();
    }

    public TypeDefinition getInnerTypeDefinition() {
        return typeDefinition;
    }

    public ClassFieldDeclaration getFieldDeclaration(String name, int lineNumber, int colNumber) {
        if (!this.fields.containsKey(name)) {
            throw new TypeCheckException("Arrays don't have a field called " + name, lineNumber, colNumber);
        }

        return this.fields.get(name);
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
        return this;
    }

    @Override
    public BasicTypeDefinition getBasicTypeDefinition() {
        return null;
    }
}
