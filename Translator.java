import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Set;
import java.util.HashSet;

public class Translator {
    //We need a HashMap to hold all of our variables. Naw, this could probably just be a list.
    private static Set<String> varSet = new HashSet<String>();

    //Like in the example Parser, our regular expressions should go here.
    //TODO: PUT REGULAR EXPRESSIONS HERE
    private static Pattern varAssignRegex = Pattern.compile("(^\\s*[_\\w]+)\\s+(is)\\s+(.*)");
    private static Pattern ifRegex = Pattern.compile("(if)\\((.+)\\)");
	private static Pattern orPattern = Pattern.compile("(or)\\((.*)\\)");
	
	private static int nestingDepth = 2;


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
        //TODO: PUT MATCHERS HERE
        Matcher varAssignMatcher = varAssignRegex.matcher(command);
        Matcher ifMatcher = ifRegex.matcher(command);
        Matcher orMatcher = orPattern.matcher(command);
        //TODO: ADD IF STATEMENT HERE TO FIND MATCHES
        if(varAssignMatcher.find()) {
            handleAssignment(varAssignMatcher);
        }
        if(ifMatcher.find()) {
            handleIfStatement(ifMatcher, null, input);
        }
        if(orMatcher.find()) {
        	System.out.println("NOTICE: OR FOUND");
        	handleIfStatement(null, orMatcher, input);
        }
    }

    private static void handleAssignment(Matcher varAssignMatcher) {
        //First check to see if the varName, group1, already exists
        String varName = varAssignMatcher.group(1);
        String value = varAssignMatcher.group(3);
        if(varSet.contains(varName)) {
            //Just simple reassignment
            System.out.println(varName + " = " + value + ";");
        } else {
            //Have to infer the type of the value
            varSet.add(varName);
            System.out.println(getValue(value) + varName + " = " + value + ";");
        }
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

    private static void handleIfStatement(Matcher ifMatcher, Matcher orMatcher, Scanner input) {
        //We might have to do something with the boolean expression later.
    	String boolExpr;
    	//This is being called if the current command is either an ifMatcher or an orMatcher
    	if(ifMatcher!= null && orMatcher == null) {
    		boolExpr = ifMatcher.group(2);
    		//TODO: Have to translate the boolean expression to java
            System.out.println("if(" + boolExpr + ") {");
    	} else {
    		boolExpr = orMatcher.group(2);
    		//TODO: have to translate the boolean expression to java
    		System.out.println("else if(" + boolExpr + ") {");
    	}
        //Start reading more lines
        String nextCommand = input.nextLine();
        //Check for either an "or" or default or endif
        while(!isBlockDone(nextCommand)) {
        	parseLine(nextCommand.trim(), input);
        	nextCommand = input.nextLine();
        }
        System.out.println("}");
        //If it's an or, then we need to recall this method. TODO: Check to see if nextCommand is or block
        
    }
    
    /**
     * This is a helper to check to see if we're done with the current if block
     * We're done with it if we encounter either the "or", "default", or "endif" keywords*/
    private static boolean isBlockDone(String nextCommand) {
    	Pattern defaultPattern = Pattern.compile("(default:)");
    	Pattern endIfPattern = Pattern.compile("(endif)");
    	Matcher orMatcher = orPattern.matcher(nextCommand);
    	Matcher defaultMatcher = defaultPattern.matcher(nextCommand);
    	Matcher endIfMatcher = endIfPattern.matcher(nextCommand);
    	boolean isOr = orMatcher.find(), isDefault = defaultMatcher.find(), isEndIf = endIfMatcher.find();
    	return isOr || isDefault || isEndIf;
    }
}