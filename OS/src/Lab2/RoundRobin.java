package Lab2;

import java.util.LinkedList;
import java.util.Queue;

public class RoundRobin {
    public static void main(String[] args) {
        //store the different quantum's average wait time
        double[] diffQ = new double[10];
        int[] arriveTime = {0, 2, 4, 6, 8};
        int[] serviceTime = {3, 6, 4, 5, 2};

        //Using the quantum from 1 to 10
        for (int q = 1; q <= 10; q++) {
            double averageWaitTime = roundRobin(arriveTime, serviceTime, q);
            diffQ[q - 1] = averageWaitTime;
            System.out.println("Quantum: " + q + " | Average Wait Time: " + averageWaitTime + "\n");
        }

        //find the minimum from the list of different quantum's average wait time
        System.out.println("The best quantum : " + findMin(diffQ));
    }

    public static class Process {
        int arriveTime;
        int executeTime;
        int processId;
        int finishTime;

        Process(int arriveTime, int executeTime,int processId) {
            this.arriveTime = arriveTime;
            this.executeTime = executeTime;
            this.processId = processId;
        }
    }

    private static double roundRobin(int[] arriveTime, int[] serviceTime, int q) {
        /*
        * readyQueue used to store the ready processes
        * executed used to store the executed processes
        * */
        Queue<Process> readyQueue = new LinkedList<>();
        Queue<Process> executedQueue = new LinkedList<>();
        int currentTime = 0;
        int waitTime = 0;
        int nextProcessId = 0;

        while (!readyQueue.isEmpty() || nextProcessId < arriveTime.length) {
            if (!readyQueue.isEmpty()) {
                Process currentProcess = readyQueue.poll();
                waitTime += (currentTime - currentProcess.arriveTime);

                //choose the smaller time between current process's execute time and the quantum
                currentTime += Math.min(currentProcess.executeTime, q);

                /*
                * find the following process which arrive time is smaller than the current process's execute time
                * then add these processes to the ready queue
                * */
                for (int i = nextProcessId; i < arriveTime.length; i ++) {
                    if (arriveTime[i] <= currentTime) {
                        readyQueue.offer(new Process(arriveTime[i], serviceTime[i], i));
                        nextProcessId = i + 1;
                    } else {
                        break;
                    }
                }

                /*if current process's execute time is larger than quantum
                * this process's execute time divide the quantum and add this process to the ready queue
                * else add this process to the executed queue
                * */
                if (currentProcess.executeTime > q) {
                    currentProcess.arriveTime = currentTime;
                    currentProcess.executeTime -= q;
                    readyQueue.offer(currentProcess);
                } else {
                    currentProcess.finishTime = currentTime;
                    executedQueue.offer(currentProcess);
                }
            } else {
                //if the ready queue is empty, add the next process to the queue
                readyQueue.offer(new Process(arriveTime[nextProcessId], serviceTime[nextProcessId], nextProcessId));
                currentTime = arriveTime[nextProcessId++];
            }

            showReadyQueue(readyQueue);
        }
        showTime(executedQueue, arriveTime, serviceTime);
        //return the average wait time
        return ((double) waitTime / arriveTime.length);
    }

    private static void showTime(Queue<Process> executedQueue, int[] arriveTime, int[] serviceTime) {
        int size = executedQueue.size();
        int[] finishTimeList = new int[size];
        int[] turnaroundTimeList = new int[size];
        double[] weighted_turnaround_list = new double[size];
        int total_turnaroundTime = 0;
        double total_weighted_turnaround_time = 0;

        if (!executedQueue.isEmpty()) {
            for (Process process : executedQueue) {
                int id = process.processId;
                finishTimeList[id] = process.finishTime;
                turnaroundTimeList[id] = process.finishTime - arriveTime[process.processId];
                total_turnaroundTime += turnaroundTimeList[id];
                weighted_turnaround_list[id] = turnaroundTimeList[id] / (double)serviceTime[id];
                total_weighted_turnaround_time += weighted_turnaround_list[id];
            }
            System.out.printf("%16s", "Finish Time");
            for (int i = 0; i < size; i++) {
                System.out.printf("%5d", finishTimeList[i]);
            }
            System.out.printf("%16s", "\nTurn around Time");
            for (int i = 0; i < size; i++) {
                System.out.printf("%5d", turnaroundTimeList[i]);
            }
            System.out.printf("%8.2f\n", (double) total_turnaroundTime / size);
            System.out.printf("%16s", "Tr/Ts");
            for (int i = 0; i < size; i++) {
                System.out.printf("%5.2f", weighted_turnaround_list[i]);
            }
            System.out.printf("%8.2f\n", total_weighted_turnaround_time / size);
        }
    }

    private static void showReadyQueue(Queue<Process> queue) {
        if (!queue.isEmpty()) {
            System.out.print(">>\t");
            for (Process process : queue) {
                System.out.print("Process" + process.processId + "\t");
            }
            System.out.println();
        }
    }

    private static double findMin(double[] list) {
        double min = list[0];
        for (int i = 1; i < list.length; i++) {
            if (list[i] < min) {
                min = list[i];
            }
        }
        return min;
    }
}