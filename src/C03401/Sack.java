package C03401;

/**
 *
 * @author Nick
 */
public class Sack
{
    int id;
    Present[] accumulation;
    private int top = 0;
    private int totalPresents = 0;
    // 5 seconds to change the sack
    private long elfChangingSpeed = 5000;
    public Sack(int id, int capacity)
    {
        accumulation = new Present[capacity];
        this.id = id;
    }

    public boolean isFull() { return top == accumulation.length; }

    public void addPresent(Present p) {
        accumulation[top] = p;
        top++;
        totalPresents++;
    }

    // The elf will change the sack
    public long changeSack() {
        top = 0;
        return elfChangingSpeed;
    }

    public int getNumberOfPresents() { return totalPresents;}
}
