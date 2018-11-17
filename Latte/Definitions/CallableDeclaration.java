package Latte.Definitions;

import java.util.List;

public interface CallableDeclaration {
    public String getName();
    public List<CallableArgument> getArgumentList();
}
