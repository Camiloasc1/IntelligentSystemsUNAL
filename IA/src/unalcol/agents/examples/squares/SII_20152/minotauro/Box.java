
package unalcol.agents.examples.squares.SII_20152.minotauro;

import java.util.Arrays;

import unalcol.agents.examples.squares.Squares;

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
    private String owner;

    /**
     *
     */
    public Box()
    {
        super();
        edges = new boolean[]
        { false, false, false, false };
        owner = Squares.SPACE;
    }

    /**
     * @param edges
     * @param owner
     */
    public Box(boolean[] edges, String owner)
    {
        super();
        // if (edges.length > 4)
        // throw new IllegalArgumentException();
        this.edges = edges.clone();
        this.owner = owner;
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
     * @return the status of the edge
     */
    public boolean getEdge(int edge)
    {
        return edges[edge];
    }

    /**
     * @param edge
     *            the edge to set
     */
    public void setEdge(int edge)
    {
        edges[edge] = true;
    }

    /**
     * @param edge
     *            the edge to clear
     */
    public void clearEdge(int edge)
    {
        edges[edge] = false;
    }

    /**
     * @return the owner
     */
    public String getOwner()
    {
        return owner;
    }

    /**
     * @param owner
     *            the owner to set
     */
    public void setOwner(String owner)
    {
        this.owner = owner;
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

    public int eval(String player)
    {
        int count = 1;
        for (boolean edge : edges)
        {
            if (edge)
            {
                count <<= 1;// count *= 2;
            }
        }
        count--;
        if ((count == 15) && !player.equals(owner))
        {
            count = -15;
        }
        return count;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "[edges=" + Arrays.toString(edges) + ", owner=" + owner + "]";
    }
}
