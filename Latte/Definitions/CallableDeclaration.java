package Latte.Definitions;

import Latte.Absyn.MethodBody;

import java.util.List;

public interface CallableDeclaration {
    public String getName();
    public List<VariableDefinition> getArgumentList();
    public MethodBody getMethodBody();
    public TypeDefinition getReturnType();
}
