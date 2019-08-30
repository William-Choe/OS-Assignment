package Lab3;

import java.util.LinkedList;

class Memory {
    //minimum residual partition
    private static final int MIN_SIZE = 5;
    //size of memory
    private int size;
    //memory partition
    private LinkedList<Zone> zones;
    //free space allocated last time
    private int pointer;

    /**
     * Default construction method
     * Set the default memory size to 100
     */
    Memory(){
        this.size = 100;
        this.pointer = 0;
        this.zones = new LinkedList<>();
        zones.add(new Zone(0, size));
    }

    Memory(int size) {
        this.size = size;
        this.pointer = 0;
        this.zones = new LinkedList<>();
        zones.add(new Zone(0, size));
    }

    /**
     * First Fit algorithm
     * @param size the size of work
     * @return return the allocation result
     */
    boolean firstFit(int size){
        //traverses the partitioned linked list
        for (pointer = 0; pointer < zones.size(); pointer++){
            Zone tmp = zones.get(pointer);
            //find available partitions (free and large enough)
            if (tmp.isFree && (tmp.size > size)){
                doAllocation(size, pointer, tmp);
                return true;
            }
        }
        //memory allocation fails if no available partition is found at the end of the traversal
        System.out.println("No memory available.");
        return false;
    }

    /**
     * Next Fit algorithm
     * @param size the size of work
     * @return return the allocation result
     */
    boolean nextFit(int size){
        //traverses the partition linked list from the last free zone location allocated
        Zone tmp = zones.get(pointer);
        if (tmp.isFree && (tmp.size > size)){
            doAllocation(size, pointer, tmp);
            return true;
        }
        int len = zones.size();
        int i = (pointer + 1) % len;
        for (; i != pointer; i = (i+1) % len){
            tmp = zones.get(i);
            //find available partitions (free and large enough)
            if (tmp.isFree && (tmp.size > size)){
                doAllocation(size, i, tmp);
                return true;
            }
        }
        //memory allocation fails if no available partition is found at the end of the traversal
        System.out.println("No memory available.");
        return false;
    }

    /**
     * Best Fit algorithm
     * @param size the size of work
     * @return return the allocation result
     */
     boolean bestFit(int size){
        int flag = -1;
        int min = this.size;
        for (pointer = 0; pointer < zones.size(); pointer++){
            Zone tmp = zones.get(pointer);
            if (tmp.isFree && (tmp.size > size)){
                if (min > tmp.size - size){
                    min = tmp.size - size;
                    flag = pointer;
                }
            }
        }
        if (flag == -1){
            System.out.println("No memory available.");
            return false;
        }else {
            doAllocation(size, flag, zones.get(flag));
            return true;
        }
     }


    /**
     * Execute allocation
     * @param size applied size
     * @param location current available partition location
     * @param tmp available free space
     */
    private void doAllocation(int size, int location, Zone tmp) {
        /*
        * if the remaining size of the partition after partition is too small (MIN_SIZE)
        * all partitions will be allocated, otherwise split into two partitions*/
        if (tmp.size - size <= MIN_SIZE){
            tmp.isFree = false;
        } else {
            Zone split = new Zone(tmp.head + size, tmp.size - size);
            zones.add(location + 1, split);
            tmp.size = size;
            tmp.isFree = false;
        }
        System.out.println("Successful Allocation " + size + "KB memory.");
    }

    /**
     * memory release
     * @param id Specifies the partition number to reclaim
     */
    void collection(int id){
        if (id >= zones.size()){
            System.out.println("No such partition number.");
            return;
        }
        Zone tmp = zones.get(id);
        int size = tmp.size;
        if (tmp.isFree) {
            System.out.println("Specifies that the partition is not allocated and does not need to be reclaimed");
            return;
        }
        /*
        * If the reclaimed partition is not the tail partition and the latter partition is idle
        * it is merged with the latter partition*/
        if (id < zones.size() - 1 && zones.get(id + 1).isFree){
            Zone next = zones.get(id + 1);
            tmp.size += next.size;
            zones.remove(next);
        }
        /*
        * If the reclaimed partition is not the first partition and the previous partition is idle
        * it is merged with the previous partition*/
        if (id > 0 && zones.get(id - 1).isFree){
            Zone previous = zones.get(id - 1);
            previous.size += tmp.size;
            zones.remove(id);
            id--;
        }
        zones.get(id).isFree = true;
        System.out.println("Memory release successful!\nRelease :" + size + "KB memory.");
    }

    /**
     * Shows the state of memory partitions
     */
    void showZones(){
        System.out.println("| id | startAddress | size | isFree |");
        for (int i = 0; i < zones.size(); i++){
            Zone tmp = zones.get(i);
            System.out.printf("%4s%15s%7s%9s\n", i, tmp.head, tmp.size, tmp.isFree);
        }
        System.out.println();
    }

    LinkedList<Zone> getZones() {
        return zones;
    }

    int getSize() {
        return size;
    }
}
