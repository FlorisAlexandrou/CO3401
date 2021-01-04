package C03401;

import java.util.HashSet;
/**
 *
 * @author Nick
 */
public class Conveyor
{
    int id;
    private Present[] presents; // The requirements say this must be a fixed size array
    public  HashSet<Integer> destinations = new HashSet();
    private volatile int top;
    // Object that is used to communicate wait and notify between table and hopper
    private final Object lock = new Object();

    public Conveyor(int id, int size)
    {
        this.id = id;
        presents = new Present[size];
    }

    public void addDestination(int hopperID)
    {
        destinations.add(hopperID);
    }

    // Load presents from hopper to belt
    public void loadPresent(Present p) {
        presents[top] = p;
        top++;
    }

    // Unload presents from belt to turntable
    public Present unloadPresent() {
        top--;
        return presents[top];
    }

    public boolean isFull() { return top == presents.length; }

    public boolean isEmpty() { return top == 0; }

    public void notifyAllThreads() {
        synchronized (lock) { lock.notifyAll(); }
    }

    public void threadWait() throws InterruptedException {
        synchronized (lock) { lock.wait(); }
    }

    public void threadWait(long time) throws InterruptedException {
        synchronized (lock) { lock.wait(time); }
    }

    public int getRemainingPresents() { return top; }

}
