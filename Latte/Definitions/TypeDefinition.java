package Latte.Definitions;

public interface TypeDefinition {
    public String getName();
    public boolean isBasicType();
    public boolean isClassType();
    public boolean isArrayType();
    public boolean isInterfaceType();
}
