
package unalcol.agents.examples.labyrinth.sii20152.miprimeranota;

class LabyrinthNode
{
    private int x;
    private int y;

    /**
     * New Labyrinth Node at (0,0)
     */
    public LabyrinthNode()
    {
        x = 0;
        y = 0;
    }

    /**
     * New Labyrinth Node at (x,y)
     *
     * @param x
     *            The X pos
     * @param y
     *            The Y pos
     */
    public LabyrinthNode(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * @return the x
     */
    public int getX()
    {
        return x;
    }
    
    /**
     * @return the y
     */
    public int getY()
    {
        return y;
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
     *            The absolute direction to check (0: North, clockwise)
     * @return The node immediately in front towards direction
     */
    public LabyrinthNode forward(int direction)
    {
        switch (direction % 4)
        {
            case 0:
            {
                return new LabyrinthNode(x, y + 1);
            }
            case 1:
            {
                return new LabyrinthNode(x + 1, y);
            }
            case 2:
            {
                return new LabyrinthNode(x, y - 1);
            }
            case 3:
            {
                return new LabyrinthNode(x - 1, y);
            }
        }
        return null; // Unreachable
    }

    /**
     * @param direction
     * @return All the neighbors starting from direction
     */
    public LabyrinthNode[] getNeighbors(int direction)
    {
        LabyrinthNode[] neighbors =
        { forward((direction + 0) % 4), forward((direction + 1) % 4), forward((direction + 2) % 4),
                forward((direction + 3) % 4) };
        return neighbors;
    }

    /**
     * @return All the neighbors starting from the North
     */
    public LabyrinthNode[] getNeighbors()
    {
        return getNeighbors(0);
    }
}
