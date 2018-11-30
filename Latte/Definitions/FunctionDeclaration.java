package Latte.Definitions;

import Latte.Absyn.Block;
import Latte.Absyn.MBody;
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
    public String toString() {
        return "FunctionDeclaration{" +
                "name='" + name + '\'' +
                '}';
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


    public boolean signatureMatches(CallableDeclaration o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FunctionDeclaration mthd = (FunctionDeclaration) o;

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
        return null;
    }

    @Override
    public Boolean isMethod() {
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, argumentList, methodBody, returnType);
    }

    public FunctionDeclaration(String name, List<VariableDefinition> argumentList, Block block, TypeDefinition returnType) {
        this.name = name;
        this.argumentList = argumentList;
        this.methodBody = new MBody(block);
        this.returnType = returnType;
    }
}
