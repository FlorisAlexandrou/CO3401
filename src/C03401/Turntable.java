package C03401;

import java.util.HashMap;
import java.util.*;

/**
 *
 * @author Nick
 */
public class Turntable extends Thread
{
    String id;

    static int N = 0;
    static int E = 1;
    static int S = 2;
    static int W = 3;
    
    Connection[] connections = new Connection[4];
    // global lookup: age-range -> SackID
    static HashMap<String, Integer> destinations = new HashMap<>();

    // this individual table's lookup: SackID -> output port
    HashMap<Integer, Integer> outputMap = new HashMap<>();
    
    public Turntable (String ID)
    {
        id = ID;
    }
    
    public void addConnection(int port, Connection conn)
    {
        connections[port] = conn;
        
        if(conn != null)
        {
            if(conn.connType == ConnectionType.OutputBelt)
            {
                Iterator<Integer> it = conn.belt.destinations.iterator();
                while(it.hasNext())
                {
                    outputMap.put(it.next(), port);
                }
            }
            else if(conn.connType == ConnectionType.OutputSack)
            {
                outputMap.put(conn.sack.id, port);
            }
        }
    }
    
    public void run()
    {
        // TODO
        consume();
    }

    private synchronized void consume() {
        for (Connection connection: connections)
        {
            if (connection == null)
                continue;

            switch (connection.connType)
            {
                case InputBelt:
                    while (true) {
                        while (connection.belt.isEmpty()) {
                            try {
                                connection.belt.threadWait();

                            } catch (InterruptedException e) {
                            }
                        }

                        Present p = connection.belt.unloadPresent();

                        // Map present destination with table port
                        int sackID = destinations.get(p.readDestination());
                        int outputPort = outputMap.get(sackID);

                        try {
                            sleep(750);
                        } catch (InterruptedException e) {
                        }


                        if (!connections[outputPort].sack.isFull()) {
                            try {
                                sleep(750);
                            } catch (InterruptedException e) {
                            }
                            connections[outputPort].sack.addPresent(p);
                            connection.belt.notifyAllThreads();
                        }
                    }

                case OutputBelt:

                case OutputSack:
                    break;
            }
        }
    }
}
