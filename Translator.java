import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
import java.util.ArrayList;

public class Translator {
    //We need a HashMap to hold all of our variables. Naw, this could probably just be a list.
    //Like in the example Parser, our regular expressions should go here.
    /*We might be able to make a list/array of Patterns, ordered from greatest 
    precedence to least, and iterate through it.
    */
    private static Pattern varAssignRegex = Pattern.compile("(^[_\\w]+)\\s+(is)\\s+(.*)");
    private static Pattern ifRegex = Pattern.compile("(if)\\((.+)\\)");

    public static void main(String[] args) {
        //First read in the program.
        //We're providing the sourceFile as the first CLA
        writeProgramHeader();
        Scanner input = openFile(args[0]);
        //Then translate the program to Java and
        //write the result out to a file named
        while(input.hasNextLine()) {
            parseLine(input.nextLine(), input);
        }
        System.out.println("    }\n}");
    }

    private static void writeProgramHeader() {
        System.out.println("public class Program1 {");
        System.out.println("    public static void main(String[] args) {");
    }

    /**
     * We're just getting the scanner with this
     * @param flename - a String representing the name of the source code file
     * @return input: a Scanner object that scans the source code file
    */
    private static Scanner openFile(String filename) {
        File sourceCodeFile = new File(filename);
        Scanner input = null;
        try {
            input = new Scanner(sourceCodeFile);

        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        return input;
    }

    /**
     * This method will determine how to handle and translate the current 
     * command.
     * We have to arrange the checks in their precedence order.
     * @param command - a String rerpesenting the current command
    */
    private static void parseLine(String command, Scanner input) {
        //First look for assignment.
        Matcher varAssignMatcher = varAssignRegex.matcher(command);
        Matcher ifMatcher = ifRegex.matcher(command);
        if(varAssignMatcher.find()) {
            handleAssignment(varAssignMatcher);
        }
        if(ifMatcher.find()) {
            handleIfStatement(ifMatcher, input);
        }
    }

    private static void handleAssignment(Matcher varAssignMatcher) {
        //Have to infer the type of the value
        String value = varAssignMatcher.group(3);
        System.out.println(getValue(value) + varAssignMatcher.group(1) + " = " + value + ";");
    }

    /**
     * This method determines the type of the variable for the translator.
     * @param value - a String representing a value of a supported type.
     * @throws CustomSyntaxErrorForAssignment - write this later.
    */
    private static String getValue(String value) {
        //Check for String
        if(value.charAt(0) == '\"' && value.charAt(value.length() - 1) == '\"') {
            return "String ";
        }
        //If it's not a String, then it's a statement that doesnt start with \", gotta be a number.
        return "double ";
    }

    private static void handleIfStatement(Matcher ifMatcher, Scanner input) {
        //We might have to do something with the boolean expression later.
        String boolExpr = ifMatcher.group(2);
        System.out.println("if(" + boolExpr + ") {");
        //The next line then has to be either
    }

}