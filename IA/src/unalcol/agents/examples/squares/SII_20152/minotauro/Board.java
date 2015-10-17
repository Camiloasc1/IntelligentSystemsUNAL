
package unalcol.agents.examples.squares.SII_20152.minotauro;

import unalcol.agents.Percept;

/**
 * @author Minotauro
 *        
 */
public class Board
{
    protected Box[][] board;

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

    public Board(Percept p)
    {
        int size = 0;
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
    
    public int count(int x, int y)
    {
        return board[x][y].count();
    }

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
    
    public int eval(int x, int y)
    {
        return board[x][y].eval();
    }

    public int eval()
    {
        int count = 0;
        for (int x = 0; x < board.length; x++)
        {
            for (int y = 0; y < board.length; y++)
            {
                count += eval(x, y);
            }
        }
        return count;
    }

    public int points(boolean player)
    {
        int points = 0;
        for (int x = 0; x < board.length; x++)
        {
            for (int y = 0; y < board.length; y++)
            {
                if ((count(x, y) == 4) && (board[x][y].getPlayer() == player))
                {
                    points++;
                }
            }
        }
        return points;
    }
    
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

    protected void eat(int x, int y, boolean player)
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
            board[x][y].setPlayer(player);
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

    public boolean play(int x, int y, int edge, boolean player)
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
        return true;
    }
}
