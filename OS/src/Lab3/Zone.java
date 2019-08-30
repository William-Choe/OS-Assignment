package Lab3;

/*
* Zone node class
*/
class Zone {
    //size of the zone
    int size;
    //start address of the zone
    int head;
    //status
    boolean isFree;

    Zone(int head, int size) {
        this.head = head;
        this.size = size;
        this.isFree = true;
    }
}
