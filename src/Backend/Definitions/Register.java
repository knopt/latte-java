package src.Backend.Definitions;

public class Register {
    public static String RAX = "rax";
    public static String RCX = "rcx";
    public static String RDX = "rdx";
    public static String RBP = "rbp";
    public static String RSP = "rsp";
    public static String RDI = "rdi";
    public static String RSI = "rsi";
    public static String R8 = "r8";
    public static String R9 = "r9";
    public static String R12 = "r12";

    public static String memAcc(String register) {
        return "[" + register + "]";
    }
}
