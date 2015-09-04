
package unalcol.agents.examples.labyrinth.sii20152.miprimeranota;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import unalcol.agents.Action;
import unalcol.agents.AgentProgram;
import unalcol.agents.Percept;
import unalcol.agents.simulate.util.SimpleLanguage;

/**
 * @author miprimeranota
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

class LabyrinthBoard<N extends LabyrinthNode>
{
    private HashMap<N, HashMap<N, Float>> board;
    private HashMap<N, Boolean> explored;

    /**
     *
     */
    public LabyrinthBoard()
    {
        board = new HashMap<N, HashMap<N, Float>>();
        explored = new HashMap<N, Boolean>();
    }
    
    /**
     * @return the board
     */
    public HashMap<N, HashMap<N, Float>> getBoard()
    {
        return board;
    }

    /**
     * @param n1
     * @param n2
     * @param weight
     */
    public void walkThrough(N n1, N n2)
    {
        addWay(n1, n2);

        // if (getLink(n1, n2) < 0)
        // {
        // board.get(n1).put(n2, 0.0f);
        // }
        board.get(n1).put(n2, getLink(n1, n2) + 1);

        // if (getLink(n2, n1) < 0)
        // {
        // board.get(n2).put(n1, 0.0f);
        // }
        board.get(n2).put(n1, getLink(n2, n1) + 1);
    }

    /**
     * @param n1
     * @param n2
     * @param weight
     */
    public void addWay(N n1, N n2)
    {
        if (!board.containsKey(n1))
        {
            board.put(n1, new HashMap<N, Float>());
        }
        if (!board.get(n1).containsKey(n2))
        {
            board.get(n1).put(n2, 0.0f);
        }

        if (!board.containsKey(n2))
        {
            board.put(n2, new HashMap<N, Float>());
        }
        if (!board.get(n2).containsKey(n1))
        {
            board.get(n2).put(n1, 0.0f);
        }
    }

    /**
     * @param n1
     * @param n2
     */
    public void addWall(N n1, N n2)
    {
        if (board.containsKey(n1) && board.get(n1).containsKey(n2))
        {
            board.get(n1).remove(n2);
        }

        if (board.containsKey(n2) && board.get(n2).containsKey(n1))
        {
            board.get(n2).remove(n1);
        }
    }

    /**
     * @param n1
     * @param n2
     */
    public boolean isConnected(N n1, N n2)
    {
        if (!board.containsKey(n1) || !board.containsKey(n2))
            return false;
        return board.get(n1).containsKey(n2) && board.get(n2).containsKey(n1);
    }

    /**
     * @param n1
     * @param n2
     * @return
     */
    public float getLink(N n1, N n2)
    {
        // if (isConnected(n1, n2))
        return board.get(n1).get(n2);
        // return Float.POSITIVE_INFINITY;
    }

    /**
     * @param n
     * @return
     */
    public void explore(N n)
    {
        explored.put(n, true);
    }

    /**
     * @param n
     * @return
     */
    public boolean isExplored(N n)
    {
        return explored.containsKey(n) ? explored.get(n) : false;
    }
}

class LabyrinthNode
{
    public int x;
    public int y;

    /**
     *
     */
    public LabyrinthNode()
    {
        x = 0;
        y = 0;
    }

    /**
     * @param x
     * @param y
     */
    public LabyrinthNode(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int with = 1000;
        return x + (y * with);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LabyrinthNode other = (LabyrinthNode) obj;
        if (x != other.x)
            return false;
        if (y != other.y)
            return false;
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#clone()
     */
    @Override
    protected Object clone()
    {
        return new LabyrinthNode(x, y);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "LabyrinthNode [x=" + x + ", y=" + y + "]";
    }

    /**
     * @param direction
     */
    public LabyrinthNode forward(int direction)
    {
        LabyrinthNode newNode = new LabyrinthNode(x, y);
        switch (direction % 4)
        {
            case 0:
            {
                newNode.y++;
                break;
            }
            case 1:
            {
                newNode.x++;
                break;
            }
            case 2:
            {
                newNode.y--;
                break;
            }
            case 3:
            {
                newNode.x--;
                break;
            }
        }
        return newNode;
    }

    public LabyrinthNode[] getNeighbors(int direction)
    {
        LabyrinthNode[] neighbors =
        { forward((direction + 0) % 4), forward((direction + 1) % 4), forward((direction + 2) % 4),
                forward((direction + 3) % 4) };
        return neighbors;
    }

    public LabyrinthNode[] getNeighbors()
    {
        return getNeighbors(0);
    }
}

enum Actions
{
    NOP(0),
    DIE(1),
    ADVANCE(2),
    ROTATE(3);

    public static SimpleLanguage language;
    private int actionIndex;

    /**
     * @param action
     */
    private Actions(int actionIndex)
    {
        this.actionIndex = actionIndex;
    }

    /**
     * @return
     */
    public Action getAction()
    {
        return new Action(language.getAction(actionIndex));
    }

    /**
     * @param action1
     * @param action2
     * @return
     */
    public static boolean compare(Action action1, Action action2)
    {
        return action1.getCode().equals(action2.getCode());
    }
}

enum Perceptions
{
    WFRONT(0),
    WRIGHT(1),
    WBACK(2),
    WLEFT(3),
    GOALREACHED(4),
    AFRONT(5),
    ARIGHT(6),
    ABACK(7),
    ALEFT(8);

    public static SimpleLanguage language;
    private int perceptionIndex;

    /**
     * @param action
     */
    private Perceptions(int perceptionIndex)
    {
        this.perceptionIndex = perceptionIndex;
    }

    /**
     * @param p
     * @return
     */
    public Object getPerception(Percept p)
    {
        return p.getAttribute(language.getPercept(perceptionIndex));
    }

    /**
     * @param p
     * @return
     */
    public boolean getBooleanPerception(Percept p)
    {
        return ((Boolean) getPerception(p)).booleanValue();
    }
}
