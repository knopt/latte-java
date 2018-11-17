package Latte.Definitions;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InterfaceTypeDefinition implements TypeDefinition, Arrayable {
    public String className;
    public Map<String, CallableDeclaration> methods;

    public InterfaceTypeDefinition(String className) {
        this.className = className;
        this.methods = new HashMap<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InterfaceTypeDefinition that = (InterfaceTypeDefinition) o;
        return Objects.equals(className, that.className) &&
                Objects.equals(methods, that.methods);
    }

    @Override
    public int hashCode() {
        return Objects.hash(className, methods);
    }

    public void addMethod(String name, MethodDeclaration d) {
        this.methods.put(name, d);
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
        return false;
    }

    @Override
    public boolean isArrayType() {
        return false;
    }

    @Override
    public boolean isInterfaceType() {
        return true;
    }
}