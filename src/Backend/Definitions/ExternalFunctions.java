package src.Backend.Definitions;

import src.Definitions.BasicTypeDefinition;
import src.Definitions.FunctionDeclaration;
import src.Definitions.VariableDefinition;

import java.util.Arrays;
import java.util.Collections;

public class ExternalFunctions {

    public static FunctionDeclaration PRINT_INT = new FunctionDeclaration(
                    "printInt",
                    Arrays.asList(new VariableDefinition("x", BasicTypeDefinition.INT)),
            null,
    BasicTypeDefinition.VOID).setExternal(true);

    public static FunctionDeclaration PRINT_STRING = new FunctionDeclaration(
                    "printString",
                    Arrays.asList(new VariableDefinition("x", BasicTypeDefinition.STRING)),
            null,
    BasicTypeDefinition.VOID).setExternal(true);

    public static FunctionDeclaration ERROR = new FunctionDeclaration(
                    "error",
                    Collections.emptyList(),
                    null,
    BasicTypeDefinition.VOID).setExternal(true);


    public static FunctionDeclaration READ_INT = new FunctionDeclaration(
                    "readInt",
                    Collections.emptyList(),
                    null,
    BasicTypeDefinition.INT).setExternal(true);

    public static FunctionDeclaration READ_STRING = new FunctionDeclaration(
                    "readString",
                    Collections.emptyList(),
                    null,
    BasicTypeDefinition.STRING
            ).setExternal(true);


    public static String ADD_STRINGS = "_addStrings";
    public static String MALLOC_ARRAY = "_mallocArray";
    public static String MALLOC_SIZE = "_mallocSize";

}
