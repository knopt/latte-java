package src.Frontend;

import src.Absyn.*;
import src.Backend.Definitions.Binding;
import src.Definitions.*;
import src.Exceptions.IllegalTypeException;
import src.Exceptions.TypeCheckException;
import src.Exceptions.TypeMismatchException;

import java.util.List;

public class TypeUtils {

    public static TypeDefinition getType(ArrayType arrayType, Environment env) {
        String typeName = arrayType.typename_.match(
                TypeUtils::getTypeName,
                TypeUtils::getTypeName
        );


        if (!TypeCheck.env.declaredTypes.containsKey(typeName)) {
            throw new IllegalTypeException(typeName, arrayType.line_num, arrayType.col_num);
        }

        TypeDefinition arrayedType = env.declaredTypes.get(typeName);

        return new ArrayTypeDefinition(arrayedType);
    }

    public static TypeDefinition getType(TypeName typeNameE, Environment env, int lineNum, int colNum) {
        String typeName = typeNameE.match(
                TypeUtils::getTypeName,
                TypeUtils::getTypeName
        );


        if (!env.declaredTypes.containsKey(typeName)) {
            throw new IllegalTypeException(typeName, lineNum, colNum);
        }

        return env.declaredTypes.get(typeName);
    }

    public static TypeDefinition getType(TypeNameS typeNameS, Environment env) {
        return getType(typeNameS.typename_, env, typeNameS.line_num, typeNameS.col_num);
    }

    public static String getTypeName(BuiltIn builtIn) {
        return builtIn.basictype_.match(
                (ignored) -> BasicTypeName.INT.toString().toLowerCase(),
                (ignored) -> BasicTypeName.STRING.toString().toLowerCase(),
                (ignored) -> BasicTypeName.BOOLEAN.toString().toLowerCase(),
                (ignored) -> BasicTypeName.VOID.toString().toLowerCase()
        );
    }

    public static String getTypeName(ClassName className) {
        return className.ident_;
    }

    public static TypeDefinition getVariableType(EVar eVar, Lhs lhs, String varName, FrontendScope scope, CallableDeclaration callableDeclaration, int lineNum, int colNum) {
        TypeCheckException e;

        if (lhs != null) {
            lhs.binding = Binding.VARIABLE_BINDING;
        }

        if (eVar != null) {
            eVar.binding = Binding.VARIABLE_BINDING;
        }

        try {
            return scope.getVariable(varName, lineNum, colNum).getType();
        } catch (TypeCheckException exception) {
            e = exception;
        }

        if (callableDeclaration.isMethod() && callableDeclaration.getCallerType().isClassType()) {
            ClassTypeDefinition classTypeDefinition = callableDeclaration.getCallerType().getClassDefinition();
            if (classTypeDefinition.fields.containsKey(varName)) {

                if (lhs != null) {
                    lhs.binding = Binding.getFieldBinding(classTypeDefinition);
                }

                if (eVar != null) {
                    eVar.binding = Binding.getFieldBinding(classTypeDefinition);
                }

                return classTypeDefinition.fields.get(varName).getType();
            }
        }

        throw e;
    }

    public static TypeDefinition getVariableType(Lhs lhs, String varName, FrontendScope scope, CallableDeclaration callableDeclaration, int lineNum, int colNum) {
        return getVariableType(null , lhs, varName, scope, callableDeclaration, lineNum, colNum);
    }

    public static TypeDefinition getVariableType(EVar eVar, String varName, FrontendScope scope, CallableDeclaration callableDeclaration, int lineNum, int colNum) {
        return getVariableType(eVar, null, varName, scope, callableDeclaration, lineNum, colNum);
    }

    public static TypeDefinition getVariableType(String varName, FrontendScope scope, CallableDeclaration callableDeclaration, int lineNum, int colNum) {
        return getVariableType(null ,null, varName, scope, callableDeclaration, lineNum, colNum);
    }

    public static TypeDefinition validateTypes(TypeDefinition expected, TypeDefinition actual, int lineNum, int colNum) {
        if (TypeCheckStatement.typesMatch(expected, actual)) {
            return expected;
        }

        throw new TypeMismatchException(expected, actual, lineNum, colNum);
    }

    public static TypeDefinition validateTypes(List<BasicTypeDefinition> types, TypeDefinition type1, int lineNum, int colNum) {
        if (types.stream().anyMatch((type) -> TypeCheckStatement.typesMatch(type1, type))) {
            return type1;
        }

        throw new TypeMismatchException(types, type1, lineNum, colNum);
    }
}
