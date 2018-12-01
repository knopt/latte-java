package Latte.Definitions;

import Latte.Absyn.MethodBody;

import java.util.List;
import java.util.Objects;

public class MethodDeclaration implements CallableDeclaration {
    public String name;
    public List<VariableDefinition> argumentList;
    public TypeDefinition returnType;
    public MethodBody methodBody;
    public TypeDefinition callerType;

    public MethodDeclaration(String name, List<VariableDefinition> argumentList, MethodBody methodBody, TypeDefinition returnType, TypeDefinition callerType) {
        this.name = name;
        this.argumentList = argumentList;
        this.returnType = returnType;
        this.methodBody = methodBody;
        this.callerType = callerType;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodDeclaration that = (MethodDeclaration) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(argumentList, that.argumentList) &&
                Objects.equals(returnType, that.returnType) &&
                Objects.equals(methodBody, that.methodBody) &&
                Objects.equals(callerType, that.callerType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, argumentList, returnType, methodBody, callerType);
    }

    @Override
    public List<VariableDefinition> getArgumentList() {
        return this.argumentList;
    }

    @Override
    public MethodBody getMethodBody() {
        return methodBody;
    }

    @Override
    public TypeDefinition getReturnType() {
        return returnType;
    }


    public boolean signatureMatches(CallableDeclaration o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodDeclaration mthd = (MethodDeclaration) o;

        if (!returnType.equals(mthd.returnType)) {
            return false;
        }

        if (!name.equals(mthd.getName())) {
            return false;
        }

        return argumentList.equals(mthd.getArgumentList());
    }

    @Override
    public TypeDefinition getCallerType() {
        return callerType;
    }

    @Override
    public Boolean isMethod() {
        return true;
    }

}
