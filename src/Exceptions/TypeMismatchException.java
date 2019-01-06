package src.Exceptions;

import src.Definitions.BasicTypeDefinition;
import src.Definitions.TypeDefinition;

import java.util.List;

public class TypeMismatchException extends TypeCheckException {
    public int lineNumber;
    public int columnNumber;

    public TypeMismatchException(TypeDefinition left, TypeDefinition right, int lineNumber, int columnNumber) {
        super("Type mismatch. Expected " + left + ", got " + right, lineNumber, columnNumber);
    }

    public TypeMismatchException(List<BasicTypeDefinition> lefts, TypeDefinition right, int lineNumber, int columnNumber) {
        super("Type mismatch. Expected one of" + lefts.toString() + ", got " + right, lineNumber, columnNumber);
    }

    public TypeMismatchException(String msg) {
        super(msg);
        this.lineNumber = -1;
        this.columnNumber = -1;
    }
}
