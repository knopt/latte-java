package Latte.Definitions;

import Latte.Absyn.MethodBody;

import java.util.List;
import java.util.Objects;

public class MethodDeclaration implements  CallableDeclaration {
    public String name;
    public List<VariableDefinition> argumentList;
    public TypeDefinition returnType;
    public MethodBody methodBody;

    public MethodDeclaration(String name, List<VariableDefinition> argumentList, MethodBody methodBody, TypeDefinition returnType) {
        this.name = name;
        this.argumentList = argumentList;
        this.returnType = returnType;
        this.methodBody = methodBody;
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
                Objects.equals(argumentList, that.argumentList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, argumentList);
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

    public boolean signatureMatches(MethodDeclaration mthd) {
        if (mthd == null) {
            return false;
        }

        if (!returnType.equals(mthd.returnType)) {
            return false;
        }

        if (!name.equals(mthd.getName())) {
            return false;
        }

        return argumentList.equals(mthd.getArgumentList());
    }
}
