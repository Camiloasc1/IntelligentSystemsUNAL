/**
 * File:		LabyrinthNode.java
 * Package:		IntelligentSystems.sii20152.miprimeranota.labyrinth.LabyrinthNode
 * Creation:	5/09/2015 at 2:49:16 p. m.
 */
package sii20152.miprimeranota.labyrinth;

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