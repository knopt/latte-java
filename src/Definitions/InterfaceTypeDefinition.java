package src.Definitions;

import src.Exceptions.TypeCheckException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static src.Backend.Instructions.ConstantUtils.WORD_SIZE;

public class InterfaceTypeDefinition implements TypeDefinition, Arrayable, MethodType {
    public String className;
    public Map<String, CallableDeclaration> methods;
    public Map<String, Integer> methodsOffsetTable;

    public InterfaceTypeDefinition(String className) {
        this.className = className;
        this.methods = new HashMap<>();
        this.methodsOffsetTable = new HashMap<>();
    }

    @Override
    public String toString() {
        return "InterfaceTypeDefinition{" +
                "className='" + className + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InterfaceTypeDefinition that = (InterfaceTypeDefinition) o;
        return Objects.equals(className, that.className) &&
                Objects.equals(methods, that.methods);
    }

    @Override
    public int hashCode() {
        return Objects.hash(className, methods);
    }

    public void addMethod(String name, MethodDeclaration d) {
        this.methods.put(name, d);
    }

    public CallableDeclaration getCallableDeclaration(String name, int lineNumber, int colNumber) {
        if (!this.methods.containsKey(name)) {
            throw new TypeCheckException("Class " + className + " doesn't have a method called " + name, lineNumber, colNumber);
        }

        return this.methods.get(name);
    }

    public boolean isImplementedBy(TypeDefinition typeDefinition) {
        if (!typeDefinition.isClassType()) {
            return false;
        }

        ClassTypeDefinition classTypeDefinition = typeDefinition.getClassDefinition();

        for (Map.Entry<String, CallableDeclaration> iEntry : methods.entrySet()) {
            if (!classTypeDefinition.methods.containsKey(iEntry.getKey())) {
                return false;
            }

            CallableDeclaration cValue = classTypeDefinition.methods.get(iEntry.getKey());

            if (!iEntry.getValue().signatureMatches(cValue)) {
                return false;
            }
        }

        return true;
    }


    public void createOffSetTable() {
        createMethodsOffsetTable();
    }

    public int getMethodOffset(String methodName) {
        return methodsOffsetTable.get(methodName);
    }

    public void createMethodsOffsetTable() {
        int i = 0;

        for (String method : methods.keySet()) {
            methodsOffsetTable.put(method, i * WORD_SIZE);
            i++;
        }
    }


    @Override
    public String getName() {
        return className;
    }

    @Override
    public boolean isBasicType() {
        return false;
    }

    @Override
    public boolean isClassType() {
        return false;
    }

    @Override
    public boolean isArrayType() {
        return false;
    }

    @Override
    public boolean isInterfaceType() {
        return true;
    }

    @Override
    public boolean isNullType() {
        return false;
    }

    @Override
    public ClassTypeDefinition getClassDefinition() {
        return null;
    }

    @Override
    public InterfaceTypeDefinition getInterfaceDefinition() {
        return this;
    }

    @Override
    public ArrayTypeDefinition getArrayTypeDefinition() {
        return null;
    }

    @Override
    public BasicTypeDefinition getBasicTypeDefinition() {
        return null;
    }

    @Override
    public TypeDefinition getOwnType() {
        return this;
    }
}
