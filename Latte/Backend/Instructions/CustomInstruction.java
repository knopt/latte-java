package Latte.Backend.Instructions;

public class CustomInstruction implements AssemblyInstruction {
    public String instruction;

    public CustomInstruction(String instruction) {
        this.instruction = instruction;
    }


    @Override
    public String yield() {
        return instruction;
    }
}
