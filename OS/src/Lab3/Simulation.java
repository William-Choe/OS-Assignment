package Lab3;

import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;

public class Simulation {
    //Specify a simulation time
    private static int time = 0;

    //The linkedList used to store the work can read in the memory
    private static LinkedList<String[]> workList = new LinkedList<>();

    //The linkedList used to store the work denied from the memory (no free space)
    private static LinkedList<String[]> deniedList = new LinkedList<>();

    //Three allocation algorithm
    private static final int FIRST_FIT = 1;
    private static final int NEXT_FIT = 2;
    private static final int BEST_FIT = 3;

    public static void main(String[] args) throws InterruptedException {
        //Initialize a new memory and input the size of memory
        Scanner in = new Scanner(System.in);
        System.out.print("Initialize the memory size: ");
        int size = in.nextInt();
        Memory memory = new Memory(size);

        System.out.print("Choose the allocation algorithm (1.First Fit; 2.Next Fit; 3.Best Fit) : ");
        int algorithm = in.nextInt();

        //Memory starts working
        while (true) {
            //Set a stop time
            if (time == 100) {
                memory.showZones();
                break;
            }
            //Each cycle is a simulated time slices
            time++;
            System.out.println("Time :" + time);
            //Inspect the memory if there are some works finished and release that space
            inspectTime(memory);

            //Read the memory file
            String[] work = readMemory();
            //To judge the allocation algorithm is succeeded
            boolean success = true;

            /*
            * If this time slide has a work, allocate this work to the memory
            * If this time slide doesn't have work, start the next time slide*/
            if (work != null) {
                //log if the allocation is successful, if the memory doesn't have space, set success to false
                switch (algorithm) {
                    case FIRST_FIT:
                        success = memory.firstFit(Integer.parseInt(work[1]));
                        break;
                    case NEXT_FIT:
                        success = memory.nextFit(Integer.parseInt(work[1]));
                        break;
                    case BEST_FIT:
                        success = memory.bestFit(Integer.parseInt(work[1]));
                        break;
                }
            } else {
                System.out.println("No ready task now.");
                continue;
            }

            //If the allocation is successful, add the work to the work list and show the current status
            if (success) {
                workList.add(work);
                memory.showZones();
                //Record the data of this memory allocation
                recordData(memory);
            } else {
                // Add allocate failed work to the list
                deniedList.add(work);
                System.out.println("Task rejection.");
            }

            //Set the duration of the simulation -> 1s
            Thread.sleep(100);
        }
    }

    /**
     * The method to make some statistics from the free region in the memory
     * Record statistics to the file
     * @param memory memory that requires statistics
     */
    private static void recordData(Memory memory) {
        LinkedList<Zone> zones = memory.getZones();
        //Number of free blips
        int totalFreeBlips = 0;
        //Maximum free region size
        int maxFreeSize = 0;
        //Minimum free region size
        int minFreeSize = memory.getSize();
        //Total free region size
        int totalFreeSize = 0;

        for (Zone zone : zones) {
            if (zone.isFree) {
                totalFreeBlips++;
                totalFreeSize += zone.size;
                if (zone.size > maxFreeSize) {
                    maxFreeSize = zone.size;
                }
                if (zone.size < minFreeSize) {
                    minFreeSize = zone.size;
                }
            }
        }

        String content = totalFreeSize + "," + totalFreeBlips + "," + maxFreeSize + "," + minFreeSize
                + "," + (totalFreeSize / (double)totalFreeBlips) + "\n";

        try {
            File file = new File("src\\Lab3\\data.txt");
            BufferedWriter bw;

            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
            bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inspect the memory if there are some works finished and release that space
     * @param memory memory that requires to inspect
     */
    private static void inspectTime(Memory memory) {
        for (int i = 0; i < workList.size(); i++) {
            int startTime = Integer.parseInt(workList.get(i)[0]);
            int duration = Integer.parseInt(workList.get(i)[2]);
            if (time == startTime + duration) {
                memory.collection(i);
            }
        }
    }

    /**
     * Read the memory file
     * @return return each line as a work and store it in the string[]
     */
    private static String[] readMemory() {
        File memoryFile = new File("src\\Lab3\\memrun.txt");
        BufferedReader in;

        try {
            in = new BufferedReader(new FileReader(memoryFile));
            String str;
            String[] work;
            while ((str = in.readLine()) != null) {
                work = str.split(",");
                if (Integer.parseInt(work[0]) == time) {
                    return work;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}