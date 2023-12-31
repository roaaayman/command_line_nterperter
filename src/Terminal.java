import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
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
            }
            else {
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
    private List<String> commandHistory = new ArrayList<>();

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
       if(file.exists() )
       {
           if(file.isFile())
           {
               file.delete();
           }
           else if(file.isDirectory())
           {
               System.out.println("rm: cannot remove '"+filename+"': Is a directory");

           }

       }
       else
       {
           System.out.println("rm: cannot remove '"+filename+"': file does not exist");

       }
    }
    public void cat(String[] args) throws FileNotFoundException {
        if(args.length==1)
        {
            File file=new File(curr,args[0]);
            if(file.isFile() && file.exists())
            {
                Scanner readfile=new Scanner(file);
                while(readfile.hasNextLine())
                {
                    System.out.println((readfile.nextLine()));
                }
                readfile.close();
            }
            else
            {
                System.out.println("File doesn't exist");
            }
        } else if (args.length==2) {
            File file1=new File(curr,args[0]);
            File file2=new File(curr,args[1]);
            if(file1.isFile() && file2.isFile() && file1.exists() && file2.exists())
            {
                Scanner readfile1=new Scanner(file1);
                Scanner readfile2=new Scanner(file2);
                while(readfile1.hasNextLine())
                {
                    System.out.println((readfile1.nextLine()));
                }
                while(readfile2.hasNextLine())
                {
                    System.out.println((readfile2.nextLine()));
                }
                readfile1.close();
                readfile2.close();

            }
            else
            {
                System.out.println("one or both files don't exist");
            }


        }
        else
        {
            System.out.println("invalid number of arguments enter 1 or 2 arguments");
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
    public void rmdir(String[] args) {

        if ( args[0].equals("*")) {
            File[] subs= curr.listFiles(File::isDirectory);
           for (int i=0;i< subs.length;i++)
           {
               if(subs[i].list().length==0)
               {
                   subs[i].delete();
               }
               else
               {
                   System.out.println("rmdir: failed to remove directories");
               }
           }
        }
        else {

            for (int i = 0; i < args.length; i++) {

                    String filename = args[0];
                    File directory = new File(curr, filename);
                    if (directory.exists() && directory.isDirectory()) {
                        if(directory.list().length==0)
                        {
                            directory.delete();
                        }
                        else {
                            System.out.println("rmdir: failed to remove '"+args[0]+"': Directory not empty");
                        }

                    }
                    else {
                        System.out.println("rmdir: failed to remove '"+args[0]+"': Directory does not exist");

                    }


            }
        }
    }

    public void cd(String[] args) {
        if (args.length == 0) {
            curr = new File(".").toPath().toAbsolutePath().normalize().toFile();
        } else if (args.length == 1) {
            Path newDirectory = Paths.get(args[0]);
            if (Files.isDirectory(newDirectory)) {
                curr = newDirectory.toFile();
            }
        } else if (args[0].equals("..")) {
            File parentDirectory = curr.getParentFile();
            if (parentDirectory != null && parentDirectory.isDirectory()) {
                curr = parentDirectory;
            }
        }
    }

    public void cp(String[] args) {
      ;
        if (args.length !=2) {
            if(args[0].equals("-r"))
            {
                args = new String[]{args[1], args[2]};
                cp_r(args);
            }
            else {
                System.out.println("Usage: cp <source_file> <destination_file>");
            }     return;
        }

        File sourceFile = new File(curr, args[0]);
        File destinationFile = new File(curr, args[1]);

        if (!sourceFile.exists()) {
            System.out.println("cp: " + args[0] + " does not exist.");
        } else if (!sourceFile.isFile()) {
            System.out.println("cp: " + args[0] + " is not a regular file.");
        } else if (destinationFile.exists() && destinationFile.isDirectory()) {
            System.out.println("cp: " + args[1] + " is a directory.");
        } else {
            try {
                Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("File copied successfully.");
            } catch (IOException e) {
                System.out.println("cp: An error occurred while copying the file.");
            }
        }
    }
    public void cp_r(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: cp -r <source_directory> <destination_directory>");
            return;
        }

        File sourceDirectory = new File(curr, args[0]);
        File destinationDirectory = new File(curr, args[1]);

        if (!sourceDirectory.exists() || !sourceDirectory.isDirectory()) {
            System.out.println("cp -r: Source directory does not exist or is not a directory.");
            return;
        }

        if (destinationDirectory.exists() && !destinationDirectory.isDirectory()) {
            System.out.println("cp -r: Destination exists but is not a directory.");
            return;
        }

        copyDirectory(sourceDirectory, destinationDirectory);
        System.out.println("Directory copied successfully.");
    }

    private void copyDirectory(File source, File destination) {
        if (source.isDirectory()) {
            if (!destination.exists()) {
                destination.mkdir();
            }

            String[] files = source.list();
            for (String file : files) {
                File srcFile = new File(source, file);
                File destFile = new File(destination, file);
                copyDirectory(srcFile, destFile);
            }
        } else {
            try {
                Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                System.out.println("cp -r: An error occurred while copying " + source);
            }
        }
    }
    public void touch(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: touch <file_path>");
            return;
        }

        String filePath = args[0];
        Path fileToCreate = Paths.get(curr.toString(), filePath);

        try {
            if (!Files.exists(fileToCreate)) {
                Files.createFile(fileToCreate);
                System.out.println("File created: " + filePath);
            } else {
                System.out.println("File already exists: " + filePath);
            }
        } catch (IOException e) {
            System.err.println("Error creating the file: " + e.getMessage());
        } catch (InvalidPathException e) {
            System.err.println("Invalid path: " + e.getMessage());
        }
    }


    public void history() {

        for (int i = 0; i < commandHistory.size(); i++) {
            System.out.println((i + 1) + " " + commandHistory.get(i));
        }
    }




// ...

    //This method will choose the suitable command method to be called
    public void chooseCommandAction() throws FileNotFoundException {
        String cmd= parser.getCommandName();
        String[] args = parser.getArgs();
        commandHistory.add(cmd);
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
        else if (cmd.equals("touch")){
            touch(args);

        }
        else if (cmd.equals("history")){
            history();

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
        else if (cmd.equals("rmdir")){
            rmdir(args);

        } else if (cmd.equals("cat")) {
            cat(args);

        } else if (cmd.equals("cp")) {
            cp(args);
        }
        else if (cmd.equals("cp -r")) {
            cp_r(args);
        }
        else
        {
            System.out.println("not recognized");
        }


    }
    public static void main(String[] args) throws FileNotFoundException {
        Terminal T= new Terminal();
        Scanner s=new Scanner(System.in);
        while (true) {
            System.out.print(">");
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