import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

class Parser {

    String commandName; //store the name of the command entered by the user
    String[] args; // store any arguments provided by the command
    //This method takes the user's input as a string and parses it to extract the command name and any arguments.
    //It should return true if parsing is successful and false otherwise.
    public boolean parse(String input) {
        String[] instring = input.split(" ", 2);
        if (instring.length < 1) {
            return false;
        }

        commandName = instring[0];

        if (instring.length > 1) {

            if (commandName.equals("ls") && instring[1].equals("-r")) {
                commandName += " -r";
            } else {
                args = instring[1].split(" ");
            }
        } else {
            args = new String[0];
        }

        return true;
    }


    //This method returns the parsed command name.
    public String getCommandName(){
        return commandName;
    }
    //This method returns the parsed command name.
    public String[] getArgs(){
        return args;
    }
}
public class Terminal {

    Parser parser;
    File curr;
    public Terminal()
    {
        parser=new Parser();
        curr=new File(".");
    }

    //Implement each command in a method, for example:
    public String pwd(){
        try {
            return curr.getCanonicalPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void ls(){
        File[] files=curr.listFiles();
        for(int i=0;i< files.length;i++)
        {
            System.out.print(files[i].getName() + " " );
        }

    }
    public void ls_r(){

        File[] files=curr.listFiles();
        if(files!=null) {
            for (int i = files.length-1; i >= 0; i--) {
                System.out.print(files[i].getName()+" ");
            }
        }
    }
    public void echo(String[] args)
    {
        for(int i=0;i< args.length;i++)
        {
            System.out.println(args[i]);
        }
    }
    public void rm(String[] args)
    {
       String filename=args[0];
       File file= new File(curr,filename);
       if(file.exists() && file.isFile())
       {
           file.delete();
       }
    }
    public void mkdir(String[] args) {
        if (args == null || args.length == 0) {
            System.out.println("Please enter at least one directory name or path.");
            return;
        }

        for (int i=0;i< args.length;i++) {
            if (args[i].length() == 0) {
                System.out.println("Please enter a valid directory name or path.");
            } else {
                File newdir = new File(curr,args[i]);

                if (!newdir.exists()) {
                    newdir.mkdir();
                    System.out.println(args[i] + " directory has been created.");
                } else {
                    System.out.println(args[i] + " directory already exists.");
                }
            }
        }
    }

    public void cd(String[] args)
    {
        if(args.length==0)
        {
            curr=new File(".");
        } else if (args.length==1) {

            File newDirectory = new File(curr, args[0]);

            if (newDirectory.isDirectory()) {
                curr = newDirectory;
            }

        }
        else if (args[0].equals("..")) {

            File previousDirectory = curr.getParentFile();

            if (previousDirectory.isDirectory()) {
                curr = previousDirectory;
            }

        }


    }


// ...

    //This method will choose the suitable command method to be called
    public void chooseCommandAction(){
        String cmd= parser.getCommandName();
        String[] args = parser.getArgs();
        if(cmd.equals("pwd"))
        {
            System.out.println(pwd());
        }
        else if (cmd.equals("ls")){
            ls();
            System.out.println("");

        }
        else if (cmd.equals("ls -r")){
            ls_r();
            System.out.println("");

        }
        else if (cmd.equals("echo")){
            echo(args);

        }
        else if (cmd.equals("rm")){
            rm(args);

        }
        else if (cmd.equals("mkdir")){
            mkdir(args);

        }

        else if (cmd.equals("cd")){
            cd(args);

        }

        else
        {
            System.out.println("not recognized");
        }

    }
    public static void main(String[] args){
        Terminal T= new Terminal();
        Scanner s=new Scanner(System.in);
        while (true) {
            System.out.print("$ ");
            String input = s.nextLine();

            if (input.equals("exit")) {
                break;
            }

            if (T.parser.parse(input)) {
                T.chooseCommandAction();
            } else {
                System.out.println("Invalid input.");
            }
        }

        System.out.println("Exiting CLI.");
    }


    }