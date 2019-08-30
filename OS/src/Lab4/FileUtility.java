package Lab4;

import java.util.List;

class FileUtility {
    ///File operations
    static String pwd(DirectoryNode currentDir) {
        StringBuilder sb = new StringBuilder(currentDir.name + "/");
        while (currentDir.father != null) {
            DirectoryNode fatherDir = currentDir.father;
            sb.insert(0, fatherDir.name + "/");
            currentDir = fatherDir;
        }
        return sb.toString();
    }

    static DirectoryNode cd(DirectoryNode currentDir, String[] formatStr) {
        String[] allPaths = formatStr[1].split("/");
        //Search the next level directory in the current directory cache
        for (String subDir:allPaths){
            for (DirectoryNode dir : currentDir.dirs) {
                if (dir.name.equals(subDir)) {
                    currentDir = dir;
                } else {
                    System.out.println("cd: No such file or directory");
                    return null;
                }
            }
        }
        return currentDir;
    }

    static void ls(DirectoryNode currentDir, String[] formatStr) {
        List<DirectoryNode> dirs = currentDir.dirs;
        List<FileNode> files = currentDir.files;
        //ls operation
        if (formatStr.length == 1) {
            for (DirectoryNode dir : dirs) {
                System.out.print(dir.name + " ");
            }
            for (FileNode file : files) {
                System.out.print(file.name);
            }
            System.out.println();
        }
        //ls -l operation
        if (formatStr.length == 2 && formatStr[1].equals("-l")) {
            for (DirectoryNode dir : dirs) {
                System.out.println(dir.toString());
            }
            for (FileNode file : files) {
                System.out.println(file.toString());
            }
        }
    }

    static DirectoryNode mkdir(DirectoryNode currentDir, String[] formatStr) {
        //Add new directory node to the directory cache of the current directory
        currentDir.dirs.add(new DirectoryNode(formatStr[1], currentDir));
        return currentDir;
    }

    static DirectoryNode rmdir(DirectoryNode currentDir, String[] formatStr) {
        //Set a sentinel value to verify that the directory is found in the directory cache
        boolean flag = false;
        for (DirectoryNode subDir : currentDir.dirs) {
            //if found, update current directory
            if (subDir.name.equals(formatStr[1])) {
                currentDir.dirs.remove(subDir);
                flag = true;
            }
        }

        if (flag) {
            return currentDir;
        } else {
            return null;
        }
    }

    static DirectoryNode touch(DirectoryNode currentDir, String[] formatStr) {
        //Add new file node to the file cache of the current directory
        currentDir.files.add(new FileNode(formatStr[1]));
        return currentDir;
    }

    static DirectoryNode rm(DirectoryNode currentDir, String[] formatStr) {
        //Set a sentinel value to verify that the file is found in the file cache
        boolean flag = false;
        for (FileNode file : currentDir.files) {
            //if found, update current directory
            if (file.name.equals(formatStr[1])) {
                currentDir.files.remove(file);
                flag = true;
            }
        }

        if (flag) {
            return currentDir;
        } else {
            return null;
        }
    }

    static DirectoryNode chmod(DirectoryNode currentDir, String[] formatStr) {
        boolean flag = false;
        for (FileNode file : currentDir.files) {
            if (file.name.equals(formatStr[2])) {
                file.permission = Integer.parseInt(formatStr[1]);
                flag = true;
            }
        }
        for (DirectoryNode dir : currentDir.dirs) {
            if (dir.name.equals(formatStr[2])) {
                dir.permission = Integer.parseInt(formatStr[1]);
                flag = true;
            }
        }

        if (flag) {
            return currentDir;
        } else {
            return null;
        }
    }
}