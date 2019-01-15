package src;

import src.Absyn.Program;
import src.Backend.Compile;
import src.Frontend.Environment;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import static src.Frontend.TypeCheck.check;

public class Generate {
    Yylex l;
    parser p;

    public Generate(String[] args) {
        try {
            Reader input;
            if (args.length == 0) input = new InputStreamReader(System.in);
            else input = new FileReader(args[0]);
            l = new Yylex(input);
        } catch (IOException e) {
            System.err.println("Error: File not found: " + args[0]);
            System.exit(1);
        }
        p = new parser(l, l.getSymbolFactory());
    }

    public src.Absyn.Program parse() throws Exception {
        /* The default parser is the first-defined entry point. */
        /* Other options are: */
        /* not available. */
        src.Absyn.Program ast = p.pProgram();
        return ast;
    }

    public static void main(String args[]) {
        Generate t = new Generate(args);
        Program program;
        try {
            program = t.parse();
        } catch (Throwable e) {
            System.err.println("ERROR");
            System.err.println("Syntax error at line " + t.l.line_num());
            System.exit(1);
            return;
        }

        Environment env;

        try {
            env = check(program);
        } catch (Exception e) {
            System.err.println("ERROR");
            System.err.println(e.getMessage());
            System.exit(1);
            return;
        }

        Compile compiler = new Compile(env);
        compiler.generate();
        compiler.print();

        System.err.println("OK");
        System.exit(0);
    }
}