
package unalcol.agents.examples.labyrinth.SII_20152.minotauro;

import java.util.HashMap;

/**
 * @author Mi Primera Nota
 *
 * @param <N>
 *            Type of the nodes in the board
 */
class LabyrinthBoard<N extends LabyrinthNode>
{
    private HashMap<N, HashMap<N, Long>> board;
    private HashMap<N, Long> explored;

    /**
     * New Labyrinth Board
     */
    public LabyrinthBoard()
    {
        board = new HashMap<N, HashMap<N, Long>>();
        explored = new HashMap<N, Long>();
    }
    
    /**
     * @return the board
     */
    public HashMap<N, HashMap<N, Long>> getBoard()
    {
        return board;
    }

    /**
     * @return the explored
     */
    public HashMap<N, Long> getExplored()
    {
        return explored;
    }
    
    /**
     * @param n1
     * @param n2
     */
    public void addWay(N n1, N n2)
    {
        if (!board.containsKey(n1))
        {
            board.put(n1, new HashMap<N, Long>());
        }
        board.get(n1).put(n2, System.currentTimeMillis());
        tryExplore(n1);

        if (!board.containsKey(n2))
        {
            board.put(n2, new HashMap<N, Long>());
        }
        board.get(n2).put(n1, System.currentTimeMillis());
        tryExplore(n2);
    }

    /**
     * @param n1
     * @param n2
     */
    public void addWall(N n1, N n2)
    {
        if (!board.containsKey(n1))
        {
            board.put(n1, new HashMap<N, Long>());
        }
        if (board.get(n1).containsKey(n2))
        {
            board.get(n1).remove(n2);
        }

        if (!board.containsKey(n2))
        {
            board.put(n2, new HashMap<N, Long>());
        }
        if (board.get(n2).containsKey(n1))
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
     * @param n
     */
    public void tryExplore(N n)
    {
        if (!explored.containsKey(n))
        {
            explored.put(n, 0l);
        }
    }

    /**
     * @param n
     */
    public void explore(N n)
    {
        explored.put(n, System.currentTimeMillis());
    }

    /**
     * @param n
     * @return
     */
    public boolean isExplored(N n)
    {
        return getExplored(n) > 0;
    }

    /**
     * @param n
     * @return
     */
    public long getExplored(N n)
    {
        return explored.containsKey(n) ? explored.get(n) : 0;
    }
}
