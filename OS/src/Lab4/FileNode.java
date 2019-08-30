package Lab4;

import java.text.SimpleDateFormat;
import java.util.Date;

class FileNode {
    String name;
    int size;
    //File permission, the default permission is 777
    int permission = 777;
    Date date;

    FileNode(String name) {
        this.name = name;
        this.size = 1;
        this.date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
    }

    FileNode(String name, int size){
        this.name = name;
        this.size = size;
    }

    public String toString() {
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
        return "-" + permission + "-" + "  " + size + "  " + ft.format(date) + "  " + name;
    }
}