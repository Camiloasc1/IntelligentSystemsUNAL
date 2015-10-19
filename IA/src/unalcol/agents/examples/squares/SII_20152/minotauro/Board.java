
package unalcol.agents.examples.squares.SII_20152.minotauro;

import unalcol.agents.Percept;

/**
 * @author Minotauro
 *         
 */
public class Board
{
    protected Box[][] board;

    /**
     * @param size
     *            the size of the board
     */
    public Board(int size)
    {
        board = new Box[size][size];
        for (int x = 0; x < size; x++)
        {
            for (int y = 0; y < size; y++)
            {
                board[x][y] = new Box();
            }
        }
        for (int i = 0; i < size; i++)
        {
            board[i][size - 1].setEdge(Box.UP);
            board[size - 1][i].setEdge(Box.RIGHT);
            board[i][0].setEdge(Box.DOWN);
            board[0][i].setEdge(Box.LEFT);
        }
    }

    /**
     * @param p
     *            the perception containing the board
     */
    public Board(Percept p)
    {
        int size = Perceptions.SIZE.getIntPerception(p);
        board = new Box[size][size];
        for (int x = 0; x < size; x++)
        {
            for (int y = 0; y < size; y++)
            {
                board[x][y] = Perceptions.BOX.getBox(p, size - 1 - y, x);
            }
        }
    }

    /**
     * @param x
     * @param y
     * @return the edges count of the box at (x,y)
     */
    public int count(int x, int y)
    {
        return board[x][y].count();
    }

    /**
     * @return the edges count of the board
     */
    public int count()
    {
        int count = 0;
        for (int x = 0; x < board.length; x++)
        {
            for (int y = 0; y < board.length; y++)
            {
                count += count(x, y);
            }
        }
        return count;
    }

    /**
     * @param x
     * @param y
     * @param player
     * @return the value of the box at (x,y) for player
     */
    public int eval(int x, int y, String player)
    {
        return board[x][y].eval(player);
    }

    /**
     * @param player
     * @return the value of the board for player
     */
    public int eval(String player)
    {
        int count = 0;
        for (int x = 0; x < board.length; x++)
        {
            for (int y = 0; y < board.length; y++)
            {
                count += eval(x, y, player);
            }
        }
        return count;
    }

    /**
     * @param player
     * @return the points for the player
     */
    public int points(String player)
    {
        int points = 0;
        for (int x = 0; x < board.length; x++)
        {
            for (int y = 0; y < board.length; y++)
            {
                if ((count(x, y) == 4) && player.equals(board[x][y].getOwner()))
                {
                    points++;
                }
            }
        }
        return points;
    }

    /**
     * @return True if the board is full, else False
     */
    public boolean isFull()
    {
        for (int x = 0; x < board.length; x++)
        {
            for (int y = 0; y < board.length; y++)
            {
                if ((count(x, y) != 4))
                    return false;
            }
        }
        return true;
    }

    /**
     * @param x
     * @param y
     * @param player
     */
    protected void eat(int x, int y, String player)
    {
        if (count(x, y) < 3)
            return;
        for (int i = 0; i < 4; i++)
        {
            if (board[x][y].getEdge(i))
            {
                continue;
            }
            board[x][y].setEdge(i);
            board[x][y].setOwner(player);
            switch (i)
            {
                case Box.UP:
                    y++;
                    break;
                case Box.RIGHT:
                    x++;
                    break;
                case Box.DOWN:
                    y--;
                    break;
                case Box.LEFT:
                    x--;
                    break;
            }
            board[x][y].setEdge((i + 2) % 4);
            eat(x, y, player);
        }
    }

    /**
     * @param x
     * @param y
     * @param edge
     * @param player
     */
    public void play(int x, int y, int edge, String player)
    {
        board[x][y].setEdge(edge);
        int x2 = x;
        int y2 = y;
        switch (edge)
        {
            case Box.UP:
                y++;
                break;
            case Box.RIGHT:
                x++;
                break;
            case Box.DOWN:
                y--;
                break;
            case Box.LEFT:
                x--;
                break;
        }
        board[x][y].setEdge((edge + 2) % 4);
        eat(x, y, player);
        eat(x2, y2, player);
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
        str += "Board [\n";
        for (int x = 0; x < board.length; x++)
        {
            for (int y = 0; y < board.length; y++)
            {
                str += "(" + x + "," + y + ") " + board[x][y] + "\n";
            }
        }
        str += "]";
        return str;
    }
}
