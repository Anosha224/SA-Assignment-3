package DiningPhilosopher;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Semaphore
{
	private int val;

    
    public Semaphore(int val) {
        this.val = val;
    }
   
    public synchronized void up(){
        if (this.val == 0) {
            notify();
        }
        this.val++;
    }
    
   
    public synchronized void down() {
        if (this.val == 0) {
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Semaphore.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
        this.val--;
    }
    
    public synchronized int getVal() {
        return this.val;
    }

}
