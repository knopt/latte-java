package src.Backend.Instructions;

public class DivInstruction implements AssemblyInstruction {
    private String sourceRegister;

    public DivInstruction(String sourceRegister) {
        this.sourceRegister = sourceRegister;
    }

    @Override
    public String yield() {
        return ConstantUtils.TAB + "idiv " + sourceRegister;
    }


}
