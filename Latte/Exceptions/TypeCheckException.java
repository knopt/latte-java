package Latte.Exceptions;

public class TypeCheckException extends RuntimeException {
    public int lineNumber;
    public int columnNumber;

    public TypeCheckException(String msg, int lineNumber, int columnNumber) {
        super(msg);
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }

    public TypeCheckException(String msg) {
        super(msg);
        this.lineNumber = -1;
        this.columnNumber = -1;
    }
}
