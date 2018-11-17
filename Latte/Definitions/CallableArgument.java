package Latte.Definitions;

import java.util.Objects;

public class CallableArgument {
    public String typeName;
    public String argumentName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CallableArgument that = (CallableArgument) o;
        return Objects.equals(typeName, that.typeName) &&
                Objects.equals(argumentName, that.argumentName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeName, argumentName);
    }

    public CallableArgument(String typeName) {
        this.typeName = typeName;
    }

    public String getArgumentName() {
        return argumentName;
    }

    public String getTypeName() {
        return typeName;
    }
}
