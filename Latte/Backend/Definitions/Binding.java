package Latte.Backend.Definitions;

import Latte.Definitions.TypeDefinition;

import java.util.Objects;

public class Binding {
    private static String FIELD_BINDING_NAME = "FIELD";
    private static String VARIABLE_BINDING_NAME = "VARIABLE";

    public static Binding VARIABLE_BINDING = new Binding(VARIABLE_BINDING_NAME, null);


    private String bindingType;
    private TypeDefinition bindedClass;

    public Binding(String bindingType, TypeDefinition bindedClass) {
        this.bindingType = bindingType;
        this.bindedClass = bindedClass;
    }

    public String getBindingType() {
        return bindingType;
    }

    public TypeDefinition getBindedClass() {
        return bindedClass;
    }

    public static Binding getFieldBinding(TypeDefinition type) {
        return new Binding(FIELD_BINDING_NAME, type);
    }

    public boolean isField() {
        return bindingType.equals(FIELD_BINDING_NAME);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Binding binding = (Binding) o;
        return Objects.equals(bindingType, binding.bindingType) &&
                Objects.equals(bindedClass, binding.bindedClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bindingType, bindedClass);
    }
}
