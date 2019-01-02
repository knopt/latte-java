package Latte.Backend.Definitions;

import Latte.Definitions.TypeDefinition;

import java.util.Objects;

public class Binding {
    private static String FIELD_BINDING_NAME = "FIELD";
    private static String VARIABLE_BINDING_NAME = "VARIABLE";
    private static String FUNCTION_BINDING_NAME = "FUNCTION";
    private static String METHOD_BINDING_NAME = "METHOD";

    public static Binding VARIABLE_BINDING = new Binding(VARIABLE_BINDING_NAME, null);
    public static Binding FUNCTION_BINDING = new Binding(FUNCTION_BINDING_NAME, null);


    private String typeOfBinding;
    private TypeDefinition bindedClass;

    public Binding(String typeOfBinding, TypeDefinition bindedClass) {
        this.typeOfBinding = typeOfBinding;
        this.bindedClass = bindedClass;
    }

    public String getTypeOfBinding() {
        return typeOfBinding;
    }

    public TypeDefinition getBindedClass() {
        return bindedClass;
    }

    public static Binding getFieldBinding(TypeDefinition type) {
        return new Binding(FIELD_BINDING_NAME, type);
    }
    public static Binding getMethodBinding(TypeDefinition type) {
        return new Binding(METHOD_BINDING_NAME, type);
    }

    public boolean isField() {
        return typeOfBinding.equals(FIELD_BINDING_NAME);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Binding binding = (Binding) o;
        return Objects.equals(typeOfBinding, binding.typeOfBinding) &&
                Objects.equals(bindedClass, binding.bindedClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeOfBinding, bindedClass);
    }
}
