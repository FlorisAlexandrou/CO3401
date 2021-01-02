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
    private int deposited = 0;
    private long waitingTime = 0;
    private volatile boolean isRunning;

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
        deposited++;
    }

    public void run()
    {
        try {
            isRunning = true;
            produce();
        } catch (InterruptedException e) { }
    }

    private synchronized void produce() throws InterruptedException {
        while (isRunning) {
            if (top > 0 )
            {
                // When belt is full, wait for consumer (table)
                if (belt.isFull()) {
                    long startTime = System.nanoTime();
                    belt.threadWait();
                    long endTime = System.nanoTime();
                    // Calculate hopper waiting time
                    waitingTime += endTime - startTime;
                }
                else {
                    top--;
                    belt.loadPresent(collection[top]);
                    belt.notifyAllThreads();
                    // Simulate present placement
                    sleep(1000 / speed);
                }
            }
            else {
                // Notify consumers/tables
                long startTime = System.nanoTime();
                belt.notifyAllThreads();
                long endTime = System.nanoTime();
                // Calculate hopper waiting time
                waitingTime += endTime - startTime;
            }
        }
    }

    public int getRemainingPresents() { return top; }

    public int getDepositedPresents() { return deposited - top; }

    public double getWaitingTime() { return (double)waitingTime / 1_000_000_000.0; }

    public void indicateToStop() { isRunning = false; }
}
