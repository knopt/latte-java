package Latte.Definitions;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ClassTypeDefinition implements TypeDefinition, Arrayable {
    public String className;
    public Map<String, ClassFieldDeclaration> fields;
    public Map<String, CallableDeclaration> methods;
    public InterfaceTypeDefinition implementedInterface;

    public ClassTypeDefinition(String className) {
        this.className = className;
        this.fields = new HashMap<>();
        this.methods = new HashMap<>();
        this.implementedInterface = null;
    }

    public void addField(String name, ClassFieldDeclaration d) {
        this.fields.put(name, d);
    }

    public void addMethod(String name, MethodDeclaration d) {
        this.methods.put(name, d);
    }

    public void addInterface(InterfaceTypeDefinition implementedInterface) {
        this.implementedInterface = implementedInterface;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassTypeDefinition that = (ClassTypeDefinition) o;
        return Objects.equals(className, that.className) &&
                Objects.equals(fields, that.fields) &&
                Objects.equals(methods, that.methods);
    }

    @Override
    public int hashCode() {
        return Objects.hash(className, fields, methods);
    }

    @Override
    public String getName() {
        return className;
    }

    @Override
    public boolean isBasicType() {
        return false;
    }

    @Override
    public boolean isClassType() {
        return true;
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
        return this;
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
    public TypeDefinition getOwnType() {
        return this;
    }
}
