package Latte.Exceptions;

import Latte.Definitions.TypeDefinition;

public class TypeMismatchException extends RuntimeException {
    public int lineNumber;
    public int columnNumber;

    public TypeMismatchException(TypeDefinition left, TypeDefinition right, int lineNumber, int columnNumber) {
        super("Type mismatch. Expected " + left + ", got " + right);
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }

    public TypeMismatchException(String msg) {
        super(msg);
        this.lineNumber = -1;
        this.columnNumber = -1;
    }
}
