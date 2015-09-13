
package sii20152.miprimeranota.labyrinth;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

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
    
    private Deque<Action> actionsQueue;
    private Action lastAction;
    
    private LabyrinthBoard<LabyrinthNode> board;
    private int currentDir;
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
        lastAction = null;
        
        board = new LabyrinthBoard<LabyrinthNode>();
        currentDir = 0;
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
        currentDir = 0;
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
        boolean F = Perceptions.FAIL.getBooleanPerception(p);
        
        if (GR) // No more to do...
            return Actions.NOP.getAction();
            
        boolean[] W =
        { Perceptions.WFRONT.getBooleanPerception(p), Perceptions.WRIGHT.getBooleanPerception(p),
                Perceptions.WBACK.getBooleanPerception(p), Perceptions.WLEFT.getBooleanPerception(p) };
                
        boolean[] A =
        // { false, false, false, false };
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
        // Fail
        if (F)
        {
            // Recalculates
            actionsQueue.clear();
        }
        
        // Pre Exploration
        updateCurrentPos(W, A, O, GR, F);
        exploreCurrentPos(W, A, O, GR, F);
        
        if (actionsQueue.isEmpty()) // Calculate actions
        {
            // Exploration
            exploration(W, A, O, GR, F);
        }
        
        // ...
        if (actionsQueue.isEmpty())
            return Actions.NOP.getAction();
            
        // Post Exploration
        
        return (lastAction = actionsQueue.remove());
    }
    
    /**
     * @param W
     * @param A
     * @param O
     * @param GR
     */
    private void exploration(boolean[] W, boolean[] A, boolean[] O, boolean GR, boolean F)
    {
        if (findNearestUnexplored(W, A, O, GR, F))
            return;
        if (DEBUG)
        {
            System.out.println("ERROR (Encerrado)");
        }

        rightHandOnWall(W, A, O, GR, F);
        return;
    }
    
    private boolean findNearestUnexplored(boolean[] W, boolean[] A, boolean[] O, boolean GR, boolean F)
    {
        HashMap<LabyrinthNode, LabyrinthNode> parent = new HashMap<LabyrinthNode, LabyrinthNode>();
        HashMap<LabyrinthNode, Integer> absDir = new HashMap<LabyrinthNode, Integer>();
        HashMap<LabyrinthNode, Integer> localDir = new HashMap<LabyrinthNode, Integer>();
        HashMap<LabyrinthNode, Integer> dist = new HashMap<LabyrinthNode, Integer>();
        
        long currentTimeMillis = System.currentTimeMillis();
        long exploredDelta;
        long targetVal = 0;
        LabyrinthNode target = null;
        
        for (LabyrinthNode n : board.getBoard().keySet())
        {
            parent.put(n, null);
            absDir.put(n, -1);
            localDir.put(n, -1);
            dist.put(n, -1);
        }
        
        parent.put(currentPos, currentPos);
        absDir.put(currentPos, currentDir);
        localDir.put(currentPos, 0);
        dist.put(currentPos, 0);
        
        PriorityQueue<LabyrinthNode> queue = new PriorityQueue<LabyrinthNode>(new NodeComparator(dist));
        queue.add(currentPos);
        
        while (!queue.isEmpty())
        {
            LabyrinthNode n = queue.remove();
            // for (LabyrinthNode m : board.getBoard().get(n).keySet())
            LabyrinthNode[] neighbors = n.getNeighbors(absDir.get(n));
            for (int i = 0; i < neighbors.length; i++)
            {
                if ((n == currentPos) && O[i])
                {
                    continue;
                }
                LabyrinthNode m = neighbors[i];
                if (board.isConnected(n, m) && (parent.get(m) == null))
                {
                    parent.put(m, n);
                    absDir.put(m, (absDir.get(n) + i) % 4);
                    localDir.put(m, i);
                    dist.put(m, dist.get(n) + i + 1);
                    queue.add(m);
                    
                    exploredDelta = currentTimeMillis - board.getExplored(m);
                    if (exploredDelta == currentTimeMillis) // Unexplored pos
                    {
                        if (DEBUG)
                        {
                            System.out.println("Target(" + dist.get(m) + "): " + m);
                        }
                        parentRoute(parent, localDir, m);
                        return true;
                    }
                    if (exploredDelta > targetVal)
                    {
                        targetVal = exploredDelta;
                        target = m;
                    }
                }
            }
        }
        parent.put(currentPos, null);
        
        if (DEBUG)
        {
            System.out.println("Target(" + dist.get(target) + "): " + target);
        }
        
        parentRoute(parent, localDir, target);
        return target != null;
    }
    
    private void parentRoute(HashMap<LabyrinthNode, LabyrinthNode> parent, HashMap<LabyrinthNode, Integer> dir,
            LabyrinthNode target)
    {
        if (target == null)
            return;
        int localDir = dir.get(parent.get(target));
        while (target != currentPos)
        {
            actionsQueue.addFirst(Actions.ADVANCE.getAction());
            for (int i = 0; i < dir.get(target); i++)
            {
                actionsQueue.addFirst(Actions.ROTATE.getAction());
                localDir = ++localDir % 4;
            }
            target = parent.get(target);
        }
    }
    
    /**
     * @param W
     * @param A
     * @param O
     * @param GR
     */
    private void rightHandOnWall(boolean[] W, boolean[] A, boolean[] O, boolean GR, boolean F)
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
    
    /**
     * @param W
     * @param A
     * @param O
     * @param GR
     * @param F
     */
    private void exploreCurrentPos(boolean[] W, boolean[] A, boolean[] O, boolean GR, boolean F)
    {
        for (int i = 0; i < W.length; i++)
        {
            if (W[i])
            {
                board.addWall(currentPos, currentPos.forward((currentDir + i) % 4));
            }
            else
            {
                board.addWay(currentPos, currentPos.forward((currentDir + i) % 4));
            }
        }
        board.explore(currentPos);
    }
    
    /**
     * @param W
     * @param A
     * @param O
     * @param GR
     * @param F
     */
    private void updateCurrentPos(boolean[] W, boolean[] A, boolean[] O, boolean GR, boolean F)
    {
        if (lastAction == null)
            return;
        if (F)
        {
            if (DEBUG)
            {
                System.out.println("Fail: " + currentPos + "(" + currentDir + ")");
            }
            return;
        }
        if (Actions.compare(lastAction, Actions.ROTATE.getAction()))
        {
            currentDir = (currentDir + 1) % 4;
            if (DEBUG)
            {
                System.out.println("Rotation: to -> " + currentDir);
            }
        }
        if (Actions.compare(lastAction, Actions.ADVANCE.getAction()))
        {
            LabyrinthNode newPos = currentPos.forward(currentDir);
            if (DEBUG)
            {
                System.out.println(
                        "Movement : " + currentPos + " -(" + currentDir + ", " + board.getExplored(newPos) + ")> " + newPos);
            }
            currentPos = newPos;
        }
    }
    
}
