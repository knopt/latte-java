package src.Backend.Definitions;

import src.Absyn.Str;
import src.Backend.Instructions.AssemblyInstruction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Instructions {
    public List<AssemblyInstruction> instructions;
    public Set<String> usedRegisters;

    public List<AssemblyInstruction> getInstructions() {
        return instructions;
    }

    public Set<String> getRegisters() {
        return usedRegisters;
    }

    public void addAll(List<AssemblyInstruction> instructions) {
        this.instructions.addAll(instructions);
    }

    public void addAll(Instructions instructions) {
        this.instructions.addAll(instructions.getInstructions());
    }

    public void add(AssemblyInstruction instruction) {
        this.instructions.add(instruction);
    }

    public Instructions() {
        this.instructions = new ArrayList<>();
        this.usedRegisters = new HashSet<>();
    }

    public void addRegisters(Set<String> regs) {
        this.usedRegisters.addAll(regs);
    }

    public void addRegister(String reg) {
        this.usedRegisters.add(reg);
    }
}
