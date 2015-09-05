
package sii20152.miprimeranota.labyrinth;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import unalcol.agents.Action;
import unalcol.agents.AgentProgram;
import unalcol.agents.Percept;
import unalcol.agents.simulate.util.SimpleLanguage;

/**
 * @author Mi Primera Nota
 */
public class LabyrinthAgent implements AgentProgram
{
    static boolean DEBUG = false;

    private Queue<Action> actionsQueue;
    
    private LabyrinthBoard<LabyrinthNode> board;
    private int direction;
    private LabyrinthNode currentPos;
    
    /**
     * @param language
     */
    public LabyrinthAgent(SimpleLanguage language)
    {
        super();
        Actions.language = language;
        Perceptions.language = language;
        
        actionsQueue = new LinkedList<Action>();

        board = new LabyrinthBoard<LabyrinthNode>();
        direction = 0;
        currentPos = new LabyrinthNode(0, 0);
    }
    
    /*
     * (non-Javadoc)
     *
     * @see unalcol.agents.AgentProgram#init()
     */
    @Override
    public void init()
    {
        actionsQueue.clear();
        
        board = new LabyrinthBoard<LabyrinthNode>();
        direction = 0;
        currentPos = new LabyrinthNode(0, 0);
    }
    
    /*
     * (non-Javadoc)
     *
     * @see unalcol.agents.AgentProgram#compute(unalcol.agents.Percept)
     */
    @Override
    public Action compute(Percept p)
    {
        boolean GR = Perceptions.GOALREACHED.getBooleanPerception(p);

        if (GR) // No more to do...
            return Actions.NOP.getAction();

        boolean[] W =
        { Perceptions.WFRONT.getBooleanPerception(p), Perceptions.WRIGHT.getBooleanPerception(p),
                Perceptions.WBACK.getBooleanPerception(p), Perceptions.WLEFT.getBooleanPerception(p) };

        boolean[] A =
        { Perceptions.AFRONT.getBooleanPerception(p), Perceptions.ARIGHT.getBooleanPerception(p),
                Perceptions.ABACK.getBooleanPerception(p), Perceptions.ALEFT.getBooleanPerception(p) };

        boolean[] O =
        { W[0] || A[0], W[1] || A[1], W[2] || A[2], W[3] || A[3] };
        
        // Don't hit the wall
        if (!actionsQueue.isEmpty() && O[0] && Actions.compare(actionsQueue.element(), Actions.ADVANCE.getAction()))
        {
            // Recalculate
            actionsQueue.clear();
        }

        // Pre Exploration
        exploreCurrentPos(W, A, O, GR);

        if (actionsQueue.isEmpty()) // Calculate actions
        {
            // Exploration
            exploration(W, A, O, GR);
        }

        // ...
        if (actionsQueue.isEmpty())
            return Actions.NOP.getAction();

        // Post Exploration
        updateCurrentPos(W, A, O, GR);

        return actionsQueue.remove();
    }

    /**
     * @param W
     * @param A
     * @param O
     * @param GR
     */
    public void exploration(boolean[] W, boolean[] A, boolean[] O, boolean GR)
    {
        
        int targetDirection = findNearestUnexplored(W, A, O, GR);
        
        if ((targetDirection != -1) && !O[targetDirection])
        {
            for (int i = 0; i < targetDirection; i++)
            {
                actionsQueue.add(Actions.ROTATE.getAction());
            }
            actionsQueue.add(Actions.ADVANCE.getAction());
            return;
        }
        if (DEBUG)
        {
            System.out.println("ERROR (mapa accesible ya explorado)");
        }
        
        LabyrinthNode[] neighbors = currentPos.getNeighbors(direction);
        
        int min = -1;
        float minVal = Float.POSITIVE_INFINITY;
        for (int i = 0; i < neighbors.length; i++)
        {
            if (!O[i] && board.isConnected(currentPos, neighbors[i]) && (board.getLink(currentPos, neighbors[i]) < minVal))
            {
                min = i;
                minVal = board.getLink(currentPos, neighbors[i]);
            }
        }
        
        if (min != -1)
        {
            if (!O[min])
            {
                for (int i = 0; i < min; i++)
                {
                    actionsQueue.add(Actions.ROTATE.getAction());
                }
                actionsQueue.add(Actions.ADVANCE.getAction());
            }
            return;
        }
        if (DEBUG)
        {
            System.out.println("ERROR (encerrado)");
        }

        rightHandOnWall(W, A, O, GR);
        return;
    }
    
    public int findNearestUnexplored(boolean[] W, boolean[] A, boolean[] O, boolean GR)
    {
        HashMap<LabyrinthNode, Integer> dir = new HashMap<LabyrinthNode, Integer>();
        HashMap<LabyrinthNode, Boolean> visited = new HashMap<LabyrinthNode, Boolean>();
        
        for (LabyrinthNode n : board.getBoard().keySet())
        {
            dir.put(n, -1);
            visited.put(n, false);
        }
        
        dir.put(currentPos, 0);
        visited.put(currentPos, true);
        
        Queue<LabyrinthNode> queue = new LinkedList<LabyrinthNode>();
        {
            LabyrinthNode[] neighbors = currentPos.getNeighbors(direction);
            for (int i = 0; i < neighbors.length; i++)
            {
                if (!O[i])
                {
                    if (!board.isExplored(neighbors[i])) // Unexplored pos
                    {
                        if (DEBUG)
                        {
                            System.out.println("Target(1): " + neighbors[i]);
                        }
                        return i;
                    }
                    dir.put(neighbors[i], i);
                    visited.put(neighbors[i], true);
                    queue.add(neighbors[i]);
                }
            }
        }
        
        while (!queue.isEmpty())
        {
            LabyrinthNode n = queue.remove();
            for (LabyrinthNode m : board.getBoard().get(n).keySet())
            {
                if (!visited.get(m))
                {
                    if (!board.isExplored(m)) // Unexplored pos
                    {
                        if (DEBUG)
                        {
                            System.out.println("Target(2): " + m);
                        }
                        return dir.get(n);
                    }
                    dir.put(m, dir.get(n));
                    visited.put(m, true);
                    queue.add(m);
                }
            }
        }
        return -1;
    }

    /**
     * @param W
     * @param A
     * @param O
     * @param GR
     */
    public void rightHandOnWall(boolean[] W, boolean[] A, boolean[] O, boolean GR)
    {
        if (!O[1])
        {
            actionsQueue.add(Actions.ROTATE.getAction());
            actionsQueue.add(Actions.ADVANCE.getAction());
        }
        else if (!O[0])
        {
            actionsQueue.add(Actions.ADVANCE.getAction());
        }
        else if (!O[3])
        {
            actionsQueue.add(Actions.ROTATE.getAction());
            actionsQueue.add(Actions.ROTATE.getAction());
            actionsQueue.add(Actions.ROTATE.getAction());
            actionsQueue.add(Actions.ADVANCE.getAction());
        }
        else if (!O[2])
        {
            actionsQueue.add(Actions.ROTATE.getAction());
            actionsQueue.add(Actions.ROTATE.getAction());
            actionsQueue.add(Actions.ADVANCE.getAction());
        }
        else
        {
            actionsQueue.add(Actions.ROTATE.getAction());
        }
    }

    public void exploreCurrentPos(boolean[] W, boolean[] A, boolean[] O, boolean GR)
    {
        for (int i = 0; i < W.length; i++)
        {
            if (W[i])
            {
                board.addWall(currentPos, currentPos.forward((direction + i) % 4));
            }
            else
            {
                board.addWay(currentPos, currentPos.forward((direction + i) % 4));
            }
        }
        board.explore(currentPos);
    }

    public void updateCurrentPos(boolean[] W, boolean[] A, boolean[] O, boolean GR)
    {
        if (Actions.compare(actionsQueue.element(), Actions.ROTATE.getAction()))
        {
            direction = (direction + 1) % 4;
            if (DEBUG)
            {
                System.out.println("Rotation: to -> " + direction);
            }
        }
        if (Actions.compare(actionsQueue.element(), Actions.ADVANCE.getAction()))
        {
            // The graph becomes useless when a movement fails
            LabyrinthNode newPos = currentPos.forward(direction);
            board.walkThrough(currentPos, newPos);
            if (DEBUG)
            {
                System.out.println("Movement : " + currentPos + " -(" + direction + ", " + board.getLink(currentPos, newPos)
                        + ")> " + newPos);
            }
            currentPos = newPos;
        }
    }
    
}
