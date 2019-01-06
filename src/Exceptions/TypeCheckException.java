package src.Exceptions;

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

    private String getPosInfo() {
        if (lineNumber >= 0 && columnNumber >= 0) {
            return "[l:" + lineNumber + ", c:" + columnNumber + "]";
        }

        if (lineNumber >= 0) {
            return "[l:" + lineNumber + "]";
        }

        if (columnNumber >= 0) {
            return "[c:" + columnNumber + "]";
        }

        return "";
    }

    @Override
    public String getMessage() {
        return getPosInfo() + super.getMessage();
    }
}
