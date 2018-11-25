package Latte.Definitions;

import Latte.Absyn.MethodBody;

import java.util.List;
import java.util.Objects;

public class FunctionDeclaration implements  CallableDeclaration {
    public String name;
    public List<VariableDefinition> argumentList;
    public MethodBody methodBody;
    public TypeDefinition returnType;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<VariableDefinition> getArgumentList() {
        return argumentList;
    }

    @Override
    public MethodBody getMethodBody() {
        return methodBody;
    }

    @Override
    public TypeDefinition getReturnType() {
        return returnType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FunctionDeclaration that = (FunctionDeclaration) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(argumentList, that.argumentList) &&
                Objects.equals(methodBody, that.methodBody) &&
                Objects.equals(returnType, that.returnType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, argumentList, methodBody, returnType);
    }

    public FunctionDeclaration(String name, List<VariableDefinition> argumentList, MethodBody methodBody, TypeDefinition returnType) {
        this.name = name;
        this.argumentList = argumentList;
        this.methodBody = methodBody;
        this.returnType = returnType;
    }
}
