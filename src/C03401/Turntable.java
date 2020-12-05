package C03401;

import java.util.HashMap;
import java.util.*;

/**
 * @author Nick
 */
public class Turntable extends Thread {
    String id;

    static int N = 0;
    static int E = 1;
    static int S = 2;
    static int W = 3;

    Connection[] connections = new Connection[4];
    private Conveyor[] inputBelts = new Conveyor[4];
    // global lookup: age-range -> SackID
    static HashMap<String, Integer> destinations = new HashMap<>();
    public boolean isPresentOnTable = false;
    // this individual table's lookup: SackID -> output port
    HashMap<Integer, Integer> outputMap = new HashMap<>();

    public Turntable(String ID) {
        id = ID;
    }

    public void addConnection(int port, Connection conn) {
        connections[port] = conn;

        if (conn != null) {
            if (conn.connType == ConnectionType.OutputBelt) {
                Iterator<Integer> it = conn.belt.destinations.iterator();
                while (it.hasNext()) {
                    outputMap.put(it.next(), port);
                }
            } else if (conn.connType == ConnectionType.OutputSack) {
                outputMap.put(conn.sack.id, port);
            }
            // Save input belts for polling
            else if (conn.connType == ConnectionType.InputBelt) {
                inputBelts[port] = connections[port].belt;
            }
        }
    }

    public void run() {
        // TODO
        // Count the present that is trapped on the table after the system stops (between 1.5 second sleeps)
        try {
            consume();
        }
        catch (InterruptedException e) {}
    }

    private synchronized void consume() throws InterruptedException {
        while (true)
        {
            // Poll all input belts
            // If they are all empty, then wait for the producer
            // Otherwise, consume and notify the producer
            for (int i = 0; i < inputBelts.length; i++)
            {
                // If the input is not null or empty
                if (!(inputBelts[i] == null || inputBelts[i].isEmpty()))
                {
                    Present p = inputBelts[i].unloadPresent();
                    isPresentOnTable = true;
                    // Map present destination with table port
                    int sackID = destinations.get(p.readDestination());
                    int outputPort = outputMap.get(sackID);

                    int dest = Math.abs(i - outputPort);
                    // If input is north/south and output is east/west or vice versa then simulate table rotation
                    if (Math.abs(i - outputPort) != 2) sleep(500);

                    // Simulate present placement onto table
                    sleep(750);

                    // If output port is a sack
                    if (connections[outputPort].connType == ConnectionType.OutputSack)
                    {
                        if (!connections[outputPort].sack.isFull())
                        {
                            // Simulate present placement in sack
                            sleep(750);
                            connections[outputPort].sack.addPresent(p);
                        }
                    }

                    // If output port is a belt
                    else if (connections[outputPort].connType == ConnectionType.OutputBelt)
                    {
                        if (!connections[outputPort].belt.isFull())
                        {
                            // Simulate present placement on the output belt
                            sleep(750);
                            connections[outputPort].belt.loadPresent(p);
                        }
                    }
                    isPresentOnTable = false;
                    inputBelts[i].notifyAllThreads();
                }
            }
            // After each loop, the belts are empty so we wait for the producers (Hopper)
            // Testing, does not work
//            for (int i = 0; i < inputBelts.length; i++)
//            {
//                if (!(inputBelts[i] == null || inputBelts[i].isEmpty()))
//                {
//                    inputBelts[i].threadWait();
//                }
//            }
        }
    }
}