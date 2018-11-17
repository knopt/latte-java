package Latte.Frontend;

import Latte.Definitions.FunctionDeclaration;
import Latte.Definitions.TypeDefinition;

import java.util.Map;

public class Environment {
    public Map<String, TypeDefinition> declaredTypes;
    public Map<String, FunctionDeclaration> declaredFunctions;
}
