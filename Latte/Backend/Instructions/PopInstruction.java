package Latte.Backend.Instructions;

import Latte.Backend.Definitions.BackendScope;

public class PopInstruction implements AssemblyInstruction {
    private String register;
    private BackendScope scope;

    public PopInstruction(String register) {
        this.register = register;
    }

    public PopInstruction(String register, BackendScope scope) {
        this.scope = scope;
        this.register = register;

        if (scope != null) {
            scope.decrTempsOnStack();
        }
    }

    @Override
    public String yield() {
        return ConstantUtils.TAB + "pop " + register;
    }
}
