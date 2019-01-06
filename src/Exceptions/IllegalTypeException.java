package src.Exceptions;

public class IllegalTypeException extends TypeCheckException {
    public IllegalTypeException(String typeName, int lineNumber, int colNumber) {
        super("Type " + typeName + " unknown", lineNumber, colNumber);
    }
}
