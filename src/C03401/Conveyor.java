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
    private int top;
    private final Object lock = new Object();
    // TODO - add more members?
    
    public Conveyor(int id, int size)
    {
        this.id = id;
        presents = new Present[size];
        
        //TODO - more construction likely!
    }

    public void addDestination(int hopperID)
    {
        destinations.add(hopperID);
    }

    // Load presents from hopper to belt
    public void loadPresent(Present p) {
        presents[top] = p;
        top++;
//        System.out.println(id + "Produced+, size is: " + top);
    }

    // Unload presents from belt to turntable
    public Present unloadPresent() {
//        System.out.println(id + "Consumed-, size is: " + top);
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

    public int getRemainingPresents() { return top; }

    // TODO - add more functions
}
