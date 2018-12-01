package Latte.Backend;

import Latte.Absyn.Program;
import Latte.Absyn.ProgramTD;
import Latte.Backend.Instructions.AssemblyInstruction;
import Latte.Frontend.Environment;

import java.util.LinkedList;
import java.util.List;

public class Compile {
    Environment env;

    public List<AssemblyInstruction> code;

    public Compile(Environment env) {
        this.env = env;
        this.code = new LinkedList<>();
    }

    public void compile(Program p) {
        p.match(this::compile);
    }

    public Boolean compile(ProgramTD p) {
        return true;
    }



}
