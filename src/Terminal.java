import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

class Parser {
    String commandName; //store the name of the command entered by the user
    String[] args; // store any arguments provided by the command
    //This method takes the user's input as a string and parses it to extract the command name and any arguments.
    //It should return true if parsing is successful and false otherwise.
    public boolean parse(String input){
        String[] instring=input.split(" ",2);
        if(instring.length<1)
        {
            return false;
        }
        commandName=instring[0];
        if(instring.length>1)
        {
            args= instring[1].split(" ");
        }
        else {
            args= new String[0];
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
    public Terminal()
    {
        parser=new Parser();
    }

    //Implement each command in a method, for example:
    public String pwd(){
        Path curr=Paths.get("").toAbsolutePath();
        return curr.toString();

    }
    public void ls(){
        File curr=new File(".");
        File[] files=curr.listFiles();
        for(int i=0;i< files.length;i++)
        {

            System.out.println(files[i].getName());
        }

    }
    public void ls_r(){
        File curr=new File(".");
        File[] files=curr.listFiles();
        for(int i= files.length-1;i>=0;i--)
        {
            System.out.println(files[i].getName());
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
        for(int i=0;i< args.length;i++)
        {
            File curr=new File(args[i]);
            if(curr.exists()) {
                File[] files=curr.listFiles();
                if(files!=null) {
                    for (int j = 0; j < files.length; j++) {

                        files[j].delete();

                    }
                }
                curr.delete();
            }
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
                File newDirectory = new File(args[i]);

                if (!newDirectory.exists()) {
                    newDirectory.mkdir();
                    System.out.println(args[i] + " directory has been created.");
                } else {
                    System.out.println(args[i] + " directory already exists.");
                }
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

        }
        else if (cmd.equals("ls -r")){
            ls_r();

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

        else
        {
            System.out.println("not recognized");
        }

    }
    public static void main(String[] args){
        Terminal T= new Terminal();
        Scanner s=new Scanner(System.in);
        String input= s.nextLine();

        if (T.parser.parse(input)) {
            T.chooseCommandAction();
        }


    }
}