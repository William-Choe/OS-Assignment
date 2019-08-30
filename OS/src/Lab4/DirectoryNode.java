package Lab4;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

class DirectoryNode {
    String name;
    //Parent directory
    DirectoryNode father = null;
    //Directory permission, the default permission is 777
    int permission = 777;
    Date date;
    //File cache
    List<FileNode> files = new LinkedList<>();
    //Directory cache
    List<DirectoryNode> dirs = new LinkedList<>();


    public DirectoryNode(String name) {
        this.name = name;
        this.date = new Date();
    }

    public DirectoryNode(String name, DirectoryNode father) {
        this.name = name;
        this.father = father;
        this.date = new Date();
    }

    public String toString() {
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
        return "-" + permission + "-" + "  " + ft.format(date) + "  " + name + "/";
    }
}