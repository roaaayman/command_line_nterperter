import java.io.File;
import java.util.Arrays;


public class Main {
    public static String current_directory;
    public static final String home_directory = null;

    Terminal terminal = new Terminal();
    Parser parser = new Parser();

    public static void main(String[] args) {
        Main main = new Main();
        Terminal terminal = main.terminal;

        // Example usage of the mkdir function
        String[] directoriesToCreate = {"new_dir1", "path/to/new_dir2", "another_dir"};
        terminal.mkdir(directoriesToCreate);

        // You can add more command calls here for testing other functions
    }
}

class Parser {
    String commandName;
    String[] args;

    public boolean parse(String input) {
        String[] inputParts = input.split(" "); // Split the input by spaces
        if (inputParts.length > 0) {
            commandName = inputParts[0]; // The first part is the command name
            if (inputParts.length > 1) {
                args = Arrays.copyOfRange(inputParts, 1, inputParts.length); // The rest are arguments
            } else {
                args = new String[0]; // No arguments
            }
            return true;
        } else {
            return false; // Parsing failed
        }
    }

    public String getCommandName() {
        return commandName;
    }

    public String[] getArgs() {
        return args;
    }
}

 class Terminal {
    Parser parser;
    public void mkdir(String[] dirNames) {
        if (dirNames == null || dirNames.length == 0) {
            System.out.println("Please enter at least one directory name or path.");
            return;
        }

        for (String dirName : dirNames) {
            if (dirName.length() == 0) {
                System.out.println("Please enter a valid directory name or path.");
            } else {
                File newDirectory = new File(dirName);

                if (!newDirectory.exists()) {
                    newDirectory.mkdir();
                    System.out.println(dirName + " directory has been created.");
                } else {
                    System.out.println(dirName + " directory already exists.");
                }
            }
        }
    }

    //Implement each command in a method, for example:
//   public String pwd(){...}
//    public void cd(String[] args){...}
//    // ...
////This method will choose the suitable command method to be called
////    public void chooseCommandAction(){...}
//    public static void main(String[] args){...}
}