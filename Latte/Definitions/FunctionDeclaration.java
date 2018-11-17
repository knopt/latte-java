package Latte.Definitions;

import java.util.List;
import java.util.Objects;

public class FunctionDeclaration implements  CallableDeclaration {
    public String name;
    public List<CallableArgument> argumentList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FunctionDeclaration that = (FunctionDeclaration) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(argumentList, that.argumentList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, argumentList);
    }

    public FunctionDeclaration(String name, List<CallableArgument> argumentList) {
        this.name = name;
        this.argumentList = argumentList;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public List<CallableArgument> getArgumentList() {
        return this.argumentList;
    }
}
