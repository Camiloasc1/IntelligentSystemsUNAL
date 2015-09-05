
package sii20152.miprimeranota.labyrinth;

import java.util.HashMap;

/**
 * @author Mi Primera Nota
 *
 * @param <N>
 *            Type of the nodes in the board
 */
class LabyrinthBoard<N extends LabyrinthNode>
{
    private HashMap<N, HashMap<N, Float>> board;
    private HashMap<N, Boolean> explored;
    
    /**
     * New Labyrinth Board
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
