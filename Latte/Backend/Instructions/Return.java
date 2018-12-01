package Latte.Backend.Instructions;

public class Return implements AssemblyInstruction {

    @Override
    public String yield() {
        return ConstantUtils.TAB + "ret";
    }
}
