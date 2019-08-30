package Lab1;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class Producer_Consumer {
    //Size of buffer
    private static final int CAPACITY = 10;
    private static int production_num = 0;
    private static int consumption_num = 0;

    /*
     * full: the number of objects produced by producers, initialize it to 0
     * empty: Capacity of the resources size, initialize it to "CAPACITY"
     * mutex: Read-write lock, initialize it to 1
     */
    private static Semaphore full = new Semaphore(0, true);
    private static Semaphore empty = new Semaphore(CAPACITY, true);
    private static Semaphore mutex = new Semaphore(1, true);

    public static void main(String[] args) {
        /*
         * queue is used to be the buffer
         * and decoupling between producers and consumers
         */
        Queue<Integer> buffer = new LinkedList<>();

        new Thread(new Producer("P-1", buffer)).start();
        new Thread(new Producer("P-2", buffer)).start();
        new Thread(new Producer("P-3", buffer)).start();

        new Thread(new Consumer("C-1", buffer)).start();
        new Thread(new Consumer("C-2", buffer)).start();
    }

    static class Producer implements Runnable {
        private Queue<Integer> buffer;
        private String thread_name;

        Producer(String thread_name, Queue<Integer> buffer) {
            this.buffer = buffer;
            this.thread_name = thread_name;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    //if it doesn't have resources, the producer thread will wait for the consumer thread to release the resource
                    empty.acquire();
                    //waiting for other threads to unlock the critical section, to protect the critical section
                    mutex.acquire();

                    //critical section
                    int produce = (int) (Math.random() * 100);
                    buffer.offer(produce);
                    production_num++;
                    System.out.println(">> [" + this.thread_name + "] Produce -> " + produce
                            + " Size: " + buffer.size() + " " + buffer.toString());
                    System.out.println("   Productions: " + production_num + "| Consumptions: " + consumption_num);

                    //unlock the critical section
                    mutex.release();
                    //increase the full variable
                    full.release();

                    Thread.sleep((int) (Math.random() * 2000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Consumer implements Runnable {
        private Queue<Integer> buffer;
        private String thread_name;

        Consumer(String thread_name, Queue<Integer> buffer) {
            this.thread_name = thread_name;
            this.buffer = buffer;
        }

        @Override
        public void run(){
            while (true) {
                try {
                    //if it doesn't have objects, the consumer thread will wait for the producer thread to produce objects
                    full.acquire();
                    //waiting for other threads to unlock the critical section, to protect the critical section
                    mutex.acquire();

                    //critical section
                    int consume = buffer.poll();
                    consumption_num++;
                    System.out.println(">> [" + this.thread_name + "] Consume -> " + consume
                            + " Size: " + buffer.size() + " " + buffer.toString());
                    System.out.println("   Productions: " + production_num + "| Consumptions: " + consumption_num);

                    //Cap the number of productions and consumptions
                    if (production_num == consumption_num) {
                        System.out.println("The number of productions and consumptions are equal, Program terminates.");
                        System.exit(0);
                    }

                    //unlock the critical section
                    mutex.release();
                    //release the resources for the producer
                    empty.release();

                    Thread.sleep((int) (Math.random() * 2000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}