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

    public Sack(int id, int capacity)
    {
        accumulation = new Present[capacity];
        this.id = id;
    }

    public boolean isFull() { return top == accumulation.length; }

    public void addPresent(Present p) {
        accumulation[top] = p;
        top++;
    }

    public int getNumberOfPresents() { return top;}
    //TODO - Add more methods
    
}
