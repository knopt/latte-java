package Latte;

import Latte.Absyn.*;

import java.io.*;

import Latte.Backend.Compile;
import Latte.Exceptions.TypeCheckException;
import Latte.Frontend.Environment;

import static Latte.Frontend.TypeCheck.check;

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

    public Latte.Absyn.Program parse() throws Exception {
        /* The default parser is the first-defined entry point. */
        /* Other options are: */
        /* not available. */
        Latte.Absyn.Program ast = p.pProgram();
//        System.err.println();
//        System.err.println("Parse Succesful!");
//        System.err.println();
//        System.err.println("[Abstract Syntax]");
//        System.err.println();
//        System.err.println(PrettyPrinter.show(ast));
//        System.err.println();
//        System.err.println("[Linearized Tree]");
//        System.err.println();
//        System.err.println(PrettyPrinter.print(ast));
        return ast;
    }

    public static void main(String args[]) {
        Generate t = new Generate(args);
        Program program;
        try {
            program = t.parse();
        } catch (Throwable e) {
//      System.err.println("At line " + String.valueOf(t.l.line_num()) + ", near \"" + t.l.buff() + "\" :");
//      System.err.println("     " + e.getMessage());
//      System.exit(1);
            System.err.println("[ERROR] Syntax error at " + t.l.line_num());
            return;
        }

        Environment env;

        try {
            env = check(program);
        } catch (TypeCheckException e) {
            System.err.println("[ERROR] " + e.getMessage());
            return;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        Compile compiler = new Compile(env);
        compiler.generate();
        compiler.print();
    }
}