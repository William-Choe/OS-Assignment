package Concurrent.Semaphore;

import java.util.concurrent.Semaphore;

public class Driver {
    private Semaphore semaphore = new Semaphore(2, true);

    public void driveCar() {
        try {
            semaphore.acquire();
            System.out.println(Thread.currentThread().getName() + " start at " + System.currentTimeMillis());
            Thread.sleep(5000);
            System.out.println(Thread.currentThread().getName() + " stop at " + System.currentTimeMillis());
            semaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Car extends Thread {
    private Driver driver;

    public Car(Driver driver) {
        super();
        this.driver = driver;
    }

    public void run() {
        driver.driveCar();
    }
}

class Run{
    public static void main(String[] args) {
        Driver driver = new Driver();
        for (int i = 0; i < 5; i++) {
            (new Car(driver)).start();
        }
    }
}
