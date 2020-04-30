package DiningPhilosopher;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DiningPhilosophers {
	private static int eat = 0; 
    private static int printStatements = 0;
    private static final int N = 5; 
    private static Philosopher[] philosophers = new Philosopher[N];
    private static Fork[] forks = new Fork[N];
    
    static void updateEat() {
        eat++;
        if (eat == N) {
            System.out.println("All philosophers have successfully completed dinner!");
            System.exit(0);
        }
    }
    
    
    static void updateEatStatus() {
        printStatements++;
        if (printStatements % 5 == 0) {
            System.out.println("Till now num of philosophers completed dinner is " + eat);
        }
    }
    
    static class Fork {
        private final Semaphore mutex = new Semaphore(1); 
        private final int id;
        
        Fork(int id) {
            this.id = id + 1;
        }
        
        
        void acquire() {
            mutex.down();
        }
        
      
        void release() {
            mutex.up();
        }
        
        boolean isAvailable() {
            return mutex.getVal() != 0;
        }
        
        int getID() {
            return this.id;
        }
    }
    
    static class Philosopher extends Thread {
        private final int id;
        private final Fork leftFork;
        private final Fork rightFork;
        
        Philosopher(int id, Fork leftFork, Fork rightFork) {
            this.id = id + 1;
            this.leftFork = leftFork;
            this.rightFork = rightFork;
        }
        
       
        void eat() {
            try {
                Thread.sleep(10);
                System.out.println("Philosopher " + this.id + " completed their dinner");
                updateEat();
                DiningPhilosophers.updateEatStatus();
            } catch (InterruptedException ex) {
                Logger.getLogger(DiningPhilosophers.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        void tryAcquireFork(Fork fork) {
            if (!fork.isAvailable()) {
                System.out.println("Philosopher " + this.id + " is waiting for Fork " + fork.getID());
                DiningPhilosophers.updateEatStatus();
            }
            fork.acquire();
            System.out.println("Fork " + fork.getID() + " taken by Philosopher " + this.id);
            DiningPhilosophers.updateEatStatus();
        }
        
        @Override
        public void run() {
            try {
                
                Thread.sleep(new Random().nextInt(10));
                
                this.tryAcquireFork(this.leftFork);
                this.tryAcquireFork(this.rightFork);
                
                this.eat();
                System.out.println("Philosopher " + this.id + " released fork " + this.leftFork.getID() + " and fork " + this.rightFork.getID());
                this.leftFork.release();
                this.rightFork.release();
                DiningPhilosophers.updateEatStatus();
            } catch (InterruptedException ex) {
                Logger.getLogger(DiningPhilosophers.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public static void main(String[] args) {
        
        for (int i = 0; i < N; i++) {
            forks[i] = new Fork(i);
        }

        for (int i = 0; i < N; i++) {
            philosophers[i] = new Philosopher(i, forks[i], forks[(i + 1) % N]);
            philosophers[i].start();
        }
    }

}
