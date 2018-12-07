package Latte.Backend.Instructions;

import Latte.Exceptions.CompilerException;

public class JumpInstruction implements AssemblyInstruction {
    public enum Type {
        LTH,
        LE,
        GTH,
        GE,
        EQU,
        NE,
        ALWAYS
    }

    private Label label;
    private Type type;

    public JumpInstruction(Label label, Type type) {
        this.label = label;
        this.type = type;
    }

    @Override
    public String yield() {
        return ConstantUtils.TAB + jumpInstr() + " " + label.getLabelName();
    }

    private String jumpInstr() {
        switch (this.type) {
            case LTH:
                return "jl";
            case LE:
                return "jle";
            case GTH:
                return "jg";
            case GE:
                return "jge";
            case EQU:
                return "je";
            case NE:
                return "jne";
            case ALWAYS:
                return "jmp";
        }

        throw new CompilerException("Jump instructions expteded to cover all cases");
    }
}
