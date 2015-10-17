
package unalcol.agents.examples.squares.SII_20152.minotauro;

/**
 * @author Minotauro
 *        
 */
public class Box
{
    public static final int UP = 0;
    public static final int RIGHT = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 3;
    private boolean[] edges;
    private boolean player;// False: me - True:enemy
    
    /**
     * @param edges
     * @param player
     */
    public Box()
    {
        super();
        edges = new boolean[]
        { false, false, false, false };
        player = false;
    }
    
    /**
     * @param edges
     * @param enemy
     */
    public Box(boolean[] edges, boolean enemy)
    {
        super();
        // if (edges.length > 4)
        // throw new IllegalArgumentException();
        this.edges = edges.clone();
        player = enemy;
    }
    
    /**
     * @return the edges
     */
    public boolean[] getEdges()
    {
        return edges;
    }
    
    /**
     * @param edges
     *            the edges to set
     */
    public void setEdges(boolean[] edges)
    {
        this.edges = edges.clone();
    }
    
    /**
     * @param edge
     * @return The status of the edge
     */
    public boolean getEdge(int edge)
    {
        return edges[edge];
    }
    
    /**
     * @param edge
     *            The edge to set
     */
    public void setEdge(int edge)
    {
        edges[edge] = true;
    }
    
    /**
     * @param edge
     *            The edge to clear
     */
    public void clearEdge(int edge)
    {
        edges[edge] = false;
    }

    /**
     * @return the player
     */
    public boolean getPlayer()
    {
        return player;
    }

    /**
     * @param player
     *            the player to set
     */
    public void setPlayer(boolean player)
    {
        this.player = player;
    }

    public int count()
    {
        int count = 0;
        for (boolean edge : edges)
        {
            if (edge)
            {
                count++;
            }
        }
        return count;
    }
    
    public int eval()
    {
        int count = 1;
        for (boolean edge : edges)
        {
            if (edge)
            {
                count *= 2;
            }
        }
        --count;
        if (player)
        {
            count *= -1;
        }
        return count;
    }
}
