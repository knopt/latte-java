package Latte.Definitions;

import Latte.Exceptions.TypeCheckException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static Latte.Backend.Instructions.ConstantUtils.WORD_SIZE;

public class ClassTypeDefinition implements TypeDefinition, Arrayable {
    public String className;
    public Map<String, ClassFieldDeclaration> fields;
    public Map<String, MethodDeclaration> methods;
    public InterfaceTypeDefinition implementedInterface;
    private Map<String, Integer> fieldsOffsetTable;
    private Map<String, Integer> methodsOffsetTable;

    @Override
    public String toString() {
        return "ClassTypeDefinition{" +
                "className='" + className + '\'' +
                '}';
    }

    public ClassTypeDefinition(String className) {
        this.className = className;
        this.fields = new HashMap<>();
        this.methods = new HashMap<>();
        this.implementedInterface = null;
        this.fieldsOffsetTable = new HashMap<>();
        this.methodsOffsetTable = new HashMap<>();
    }

    public void addField(String name, ClassFieldDeclaration d) {
        this.fields.put(name, d);
    }

    public void addMethod(String name, MethodDeclaration d) {
        this.methods.put(name, d);
    }

    public void addInterface(InterfaceTypeDefinition implementedInterface) {
        this.implementedInterface = implementedInterface;
    }

    public ClassFieldDeclaration getFieldDeclaration(String name, int lineNumber, int colNumber) {
        if (!this.fields.containsKey(name)) {
            throw new TypeCheckException("Class " + className + " doesn't have a field called " + name, lineNumber, colNumber);
        }

        return this.fields.get(name);
    }


    public CallableDeclaration getCallableDeclaration(String name, int lineNumber, int colNumber) {
        if (!this.methods.containsKey(name)) {
            throw new TypeCheckException("Class " + className + " doesn't have a method called " + name, lineNumber, colNumber);
        }

        return this.methods.get(name);
    }

    public void createOffSetTable() {
        createFieldsOffsetTable();
        createMethodsOffsetTable();
    }


    public void createFieldsOffsetTable() {
        int i = 0;

        for (String field : fields.keySet()) {
            fieldsOffsetTable.put(field, i * WORD_SIZE);
            i++;
        }
    }

    public void createMethodsOffsetTable() {
        int i = 0;

        for (String method : methods.keySet()) {
            methodsOffsetTable.put(method, i * WORD_SIZE);
            i++;
        }
    }

    public int getFieldOffset(String fieldName) {
        return fieldsOffsetTable.get(fieldName);
    }

    public int getMethodOffset(String methodName) {
        return fieldsOffsetTable.size() * WORD_SIZE + methodsOffsetTable.get(methodName);
    }

    public int getClassSize() {
        return fieldsOffsetTable.size() + methodsOffsetTable.size();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassTypeDefinition that = (ClassTypeDefinition) o;
        return Objects.equals(className, that.className) &&
                Objects.equals(fields, that.fields) &&
                Objects.equals(methods, that.methods);
    }

    @Override
    public int hashCode() {
        return Objects.hash(className, fields, methods);
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
        return true;
    }

    @Override
    public boolean isArrayType() {
        return false;
    }

    @Override
    public boolean isInterfaceType() {
        return false;
    }

    @Override
    public boolean isNullType() {
        return false;
    }

    @Override
    public ClassTypeDefinition getClassDefinition() {
        return this;
    }

    @Override
    public InterfaceTypeDefinition getInterfaceDefinition() {
        return null;
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
