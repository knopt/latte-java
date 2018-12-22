package Latte.Backend.Instructions;

import Latte.Backend.Definitions.BackendScope;

public class PushInstruction implements AssemblyInstruction {
    private String register;
    private BackendScope scope;

    public PushInstruction(String register) {
        this.register = register;
    }

    public PushInstruction(String register, BackendScope scope) {
        this.register = register;
        this.scope = scope;

        if (scope != null) {
            scope.incrTempsOnStack();
        }
    }

    @Override
    public String yield() {
        return ConstantUtils.TAB + "push " + register;
    }
}
