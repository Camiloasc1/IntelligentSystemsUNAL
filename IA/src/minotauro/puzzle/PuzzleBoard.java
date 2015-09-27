
package minotauro.puzzle;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * @author minotauro
 *         
 */
public class PuzzleBoard
{
    private int size;
    private int[] board;

    /**
     * @param size
     */
    public PuzzleBoard(int size)
    {
        super();
        this.size = size;
        board = new int[size * size];
        for (int i = 0; i < board.length; i++)
        {
            board[i] = (i + 1) % (size * size);
        }
    }

    /**
     * @param size
     * @param board
     */
    private PuzzleBoard(int size, int[] board)
    {
        super();
        // if (board.length != (size * size))
        // throw new IllegalArgumentException("Board does not match with the expected size");
        this.size = size;
        this.board = board.clone();
    }

    /**
     * @return the size
     */
    public int getSize()
    {
        return size;
    }

    /**
     * @return the board
     */
    public int[] getBoard()
    {
        return board.clone();
    }

    /**
     * @param x
     * @param y
     * @return Representative pos for (x,y)
     */
    private int convert(int x, int y)
    {
        return (y * size) + x;
    }

    /**
     * @param direction
     * @return True if movement was done
     */
    public boolean move(int direction)
    {
        for (int i = 0; i < board.length; i++)
        {
            if (board[i] == 0)
            {
                int x = i % size;
                int y = i / size;
                switch (direction % 4)
                {
                    case 0:
                    {
                        y++;
                        break;
                    }
                    case 1:
                    {
                        x++;
                        break;
                    }
                    case 2:
                    {
                        y--;
                        break;
                    }
                    case 3:
                    {
                        x--;
                        break;
                    }
                }
                if ((x < 0) || (x >= size) || (y < 0) || (y >= size))
                    return false;
                swap(i, convert(x, y));
                return true;
            }
        }
        return false;
    }

    /**
     * @param a
     * @param b
     */
    private void swap(int a, int b)
    {
        int tmp = board[a];
        board[a] = board[b];
        board[b] = tmp;
    }

    /**
     * @return The neighbors of the possible movements
     */
    public PuzzleBoard[] getNeighbors()
    {
        LinkedList<PuzzleBoard> neighbors = new LinkedList<>();
        PuzzleBoard neighbor;
        for (int i = 0; i < 4; i++)
        {
            if ((neighbor = getNeighbor(i)) != null)
            {
                neighbors.add(neighbor);
            }
        }
        return neighbors.toArray(new PuzzleBoard[0]);
    }

    /**
     * @param movement
     * @return The Neighbor that represents the movement or null if isn't possible to do
     */
    public PuzzleBoard getNeighbor(int movement)
    {
        PuzzleBoard neighbor = new PuzzleBoard(size, board);
        if (neighbor.move(movement))
            return neighbor;
        return null;
    }

    /**
     * @return True if is solved
     */
    public boolean isSolved()
    {
        return equals(new PuzzleBoard(size));
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#clone()
     */
    @Override
    protected Object clone() throws CloneNotSupportedException
    {
        return new PuzzleBoard(size, board);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + Arrays.hashCode(board);
        result = (prime * result) + size;
        return result;
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
        PuzzleBoard other = (PuzzleBoard) obj;
        if (!Arrays.equals(board, other.board))
            return false;
        if (size != other.size)
            return false;
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        String str = "";
        for (int i = 0; i < board.length; i++)
        {
            if (((i % size) == 0) && (i > 0))
            {
                str += "\n";
            }
            str += board[i];
        }
        return str;
    }
}
