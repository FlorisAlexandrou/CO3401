package C03401;

/**
 *
 * @author Nick
 */
public class Hopper extends Thread
{
    int id;
    Conveyor belt;
    int speed;

    Present[] collection;
    private int top = 0;

    public Hopper(int id, Conveyor con, int capacity, int speed)
    {
        collection = new Present[capacity];
        this.id = id;
        belt = con;
        this.speed = speed;
    }
    
    public void fill(Present p)
    {
        collection[top] = p;
        top++;
    }

    public void run()
    {
        produce();
    }

    private synchronized void produce() {
        while (top > 0)
        {
            while (belt.isFull()) {
                try {
                    belt.threadWait();
                } catch (InterruptedException e) { }
            }
            top--;
            belt.loadPresent(collection[top]);
            belt.notifyAllThreads();
        }
    }

    public int getRemainingPresents() { return this.top; }

    // TODO Add more methods?
}
