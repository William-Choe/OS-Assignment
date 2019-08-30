package Lab4;

import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        //Declare a scanner for user Input
        Scanner scanner = new Scanner(System.in);
        String command;

        //Initialize the root directory
        DirectoryNode root = new DirectoryNode("root");
        DirectoryNode currentDir = root;
        DirectoryNode returnDir;
        String path = currentDir.name + "/";

        Menu: while (true) {
            System.out.print("cui@OperatingSystem:" + path + "$ ");
            command = scanner.nextLine();
            String[] formatStr = command.split("[ ]+");

            switch (formatStr[0]) {
                case "pwd":
                    if (formatStr.length != 1) {
                        System.out.println("pwd: Invalid command");
                        break;
                    }
                    String pwd = FileUtility.pwd(currentDir);
                    System.out.println(pwd);
                    break;

                case "cd":
                    if (formatStr.length != 2) {
                        System.out.println("cd: Invalid command");
                        break;
                    }
                    returnDir = FileUtility.cd(currentDir, formatStr);
                    if (returnDir != null) {
                        currentDir = returnDir;
                    }
                    DirectoryNode tmp = currentDir;
                    path = currentDir.name + "/";
                    StringBuilder sb = new StringBuilder(path);
                    while (tmp.father != null) {
                        sb.insert(0, tmp.father.name + "/");
                        tmp = tmp.father;
                    }
                    path = sb.toString();
                    break;

                case "ls":
                    if (formatStr.length > 2) {
                        System.out.println("ls: Invalid command");
                        break;
                    }
                    FileUtility.ls(currentDir, formatStr);
                    break;

                case "mkdir":
                    if (formatStr.length != 2) {
                        System.out.println("mkdir: Invalid command");
                        break;
                    }
                    returnDir = FileUtility.mkdir(currentDir, formatStr);
                    currentDir = returnDir;
                    break;

                case "rmdir":
                    if (formatStr.length != 2) {
                        System.out.println("rmdir: Invalid command");
                        break;
                    }

                    returnDir = FileUtility.rmdir(currentDir, formatStr);
                    if (returnDir != null) {
                        currentDir = returnDir;
                    } else {
                        System.out.println("rmdir: No such directory");
                    }
                    break;

                case "touch":
                    if (formatStr.length != 2) {
                        System.out.println("touch: Invalid command");
                        break;
                    }
                    returnDir = FileUtility.touch(currentDir, formatStr);
                    currentDir = returnDir;
                    break;

                case "rm":
                    if (formatStr.length != 2) {
                        System.out.println("rm: Invalid command");
                        break;
                    }

                    returnDir = FileUtility.rm(currentDir, formatStr);
                    if (returnDir != null) {
                        currentDir = returnDir;
                    } else {
                        System.out.println("rm: No such file");
                    }
                    break;

                case "chmod":
                    if (formatStr.length != 3) {
                        System.out.println("chmod: Invalid command");
                        break;
                    }

                    returnDir = FileUtility.chmod(currentDir, formatStr);
                    if (returnDir != null) {
                        currentDir = returnDir;
                    } else {
                        System.out.println("chmod: No such file");
                    }
                    break;

                case "exit":
                    System.out.println("exit");
                    break Menu;

                case "quit":
                    System.out.println("exit");
                    break Menu;
            }
        }
    }
}