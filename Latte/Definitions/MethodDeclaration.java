package Latte.Definitions;

import java.util.List;
import java.util.Objects;

public class MethodDeclaration implements  CallableDeclaration {
    public String name;
    public List<CallableArgument> argumentList;
    public TypeDefinition thisType;

    public MethodDeclaration(String name, List<CallableArgument> argumentList, TypeDefinition thisType) {
        this.name = name;
        this.argumentList = argumentList;
        this.thisType = thisType;
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
    public List<CallableArgument> getArgumentList() {
        return this.argumentList;
    }
}
