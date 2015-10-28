
package unalcol.agents.examples.squares.SII_20152.minotauro;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import unalcol.agents.Percept;
import unalcol.agents.examples.squares.Squares;

/**
 * @author Minotauro
 */
public class Board
{
    public static final int UP = 1;
    public static final int RIGHT = 2;
    public static final int DOWN = 4;
    public static final int LEFT = 8;
    public static final int WHITE = 16;
    protected int[][] board;
    
    /**
     * @param size
     *            the size of the board
     */
    public Board(int size)
    {
        board = new int[size][size];
        for (int i = 0; i < size; i++)
        {
            setEdge(i, size - 1, UP);
            setEdge(size - 1, i, RIGHT);
            setEdge(i, 0, DOWN);
            setEdge(0, i, LEFT);
        }
    }
    
    /**
     * @param p
     *            the perception containing the board
     */
    public Board(Percept p)
    {
        int size = Perceptions.SIZE.getIntPerception(p);
        board = new int[size][size];
        for (int x = 0; x < size; x++)
        {
            for (int y = 0; y < size; y++)
            {
                Box b = Perceptions.BOX.getBox(p, size - 1 - y, x);
                if (b.getEdge(Box.UP))
                {
                    setEdge(x, y, UP);
                }
                if (b.getEdge(Box.RIGHT))
                {
                    setEdge(x, y, RIGHT);
                }
                if (b.getEdge(Box.DOWN))
                {
                    setEdge(x, y, DOWN);
                }
                if (b.getEdge(Box.LEFT))
                {
                    setEdge(x, y, LEFT);
                }
                setOwner(x, y, b.getOwner());
            }
        }
    }
    
    public Board(Board b)
    {
        int size = b.board.length;
        board = new int[size][size];
        for (int x = 0; x < size; x++)
        {
            for (int y = 0; y < size; y++)
            {
                board[x][y] = b.board[x][y];
            }
        }
    }
    
    public boolean getEdge(int x, int y, int e)
    {
        return (board[x][y] & e) == e;
    }
    
    public void setEdge(int x, int y, int e)
    {
        board[x][y] |= e;
    }
    
    public String getOwner(int x, int y)
    {
        return getEdge(x, y, WHITE) ? Squares.WHITE : Squares.BLACK;
    }
    
    public boolean isOwner(int x, int y, String player)
    {
        return player.equals(getOwner(x, y));
    }
    
    public void setOwner(int x, int y, String player)
    {
        setEdge(x, y, player.equals(Squares.WHITE) ? WHITE : 0);
    }
    
    /**
     * @param x
     * @param y
     * @return the edges count of the box at (x,y)
     */
    public int count(int x, int y)
    {
        return (getEdge(x, y, UP) ? 1 : 0) + (getEdge(x, y, RIGHT) ? 1 : 0) + (getEdge(x, y, DOWN) ? 1 : 0)
                + (getEdge(x, y, LEFT) ? 1 : 0);
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
        int count = 1;
        for (int i = 0; i < count(x, y); i++)
        {
            count <<= 1;// count *= 2;
        }
        count--;
        if ((count == 15) && !isOwner(x, y, player))
            return -15;
        return count;
        // Box b = new Box(new boolean[]
        // { getEdge(x, y, UP), getEdge(x, y, RIGHT), getEdge(x, y, DOWN), getEdge(x, y, LEFT) }, getOwner(x, y));
        // return b.eval(player);
    }
    
    /**
     * @param player
     * @return the value of the board for player
     */
    public int eval(String player)
    {
        int eval;
        int points = 0;
        int count = 0;
        for (int x = 0; x < board.length; x++)
        {
            for (int y = 0; y < board.length; y++)
            {
                eval = eval(x, y, player);
                count += eval;
                if (eval == 15)
                {
                    points++;
                }
                else if (eval == -15)
                {
                    points--;
                }
            }
        }
        // count += (points(player) - points(swapPlayer(player))) << 16;
        count += points << 16;
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
                if ((count(x, y) == 4) && isOwner(x, y, player))
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
    private void eat(int x, int y, String player)
    {
        int[] edges =
        { UP, RIGHT, DOWN, LEFT };
        if (count(x, y) != 3)
            return;
        for (int e : edges)
        {
            if (getEdge(x, y, e))
            {
                continue;
            }
            setEdge(x, y, e);
            setOwner(x, y, player);
            switch (e)
            {
                case UP:
                    y++;
                    break;
                case RIGHT:
                    x++;
                    break;
                case DOWN:
                    y--;
                    break;
                case LEFT:
                    x--;
                    break;
            }
            setEdge(x, y, complementEdge(e));
            eat(x, y, player);
            return;
        }
    }
    
    /**
     * @param x
     * @param y
     * @param e
     * @param player
     */
    public void play(int x, int y, int e, String player)
    {
        setEdge(x, y, e);
        int x2 = x;
        int y2 = y;
        switch (e)
        {
            case UP:
                y++;
                break;
            case RIGHT:
                x++;
                break;
            case DOWN:
                y--;
                break;
            case LEFT:
                x--;
                break;
        }
        setEdge(x, y, complementEdge(e));
        player = swapPlayer(player);
        eat(x, y, player);
        eat(x2, y2, player);
    }
    
    Map<Board, String> getChildren(String player)
    {
        int[] edges =
        { UP, RIGHT };
        Map<Board, String> children = new HashMap<Board, String>();
        Board b;
        String action;
        for (int x = 0; x < board.length; x++)
        {
            for (int y = 0; y < board.length; y++)
            {
                for (int e : edges)
                {
                    if (!getEdge(x, y, e))
                    {
                        b = new Board(this);
                        b.play(x, y, e, player);
                        action = (board.length - 1 - y) + ":" + (x) + ":" + ((e == UP) ? Squares.TOP : Squares.RIGHT);
                        children.put(b, action);
                    }
                }
            }
        }
        return children;
    }
    
    public int turnCount()
    {
        return (count() - (4 * board.length)) / 2;
    }
    
    public int totalMoves()
    {
        return 2 * board.length * (board.length - 1);
    }
    
    public int totalEdges()
    {
        return 2 * board.length * (board.length + 1);
    }
    
    public static String swapPlayer(String player)
    {
        return player.equals(Squares.WHITE) ? Squares.BLACK : Squares.WHITE;
    }
    
    /**
     * @param e
     * @return
     */
    public int complementEdge(int e)
    {
        switch (e)
        {
            case UP:
                return DOWN;
            case RIGHT:
                return LEFT;
            case DOWN:
                return UP;
            case LEFT:
                return RIGHT;
        }
        return -1;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return Arrays.deepHashCode(board);
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
        Board other = (Board) obj;
        if (!Arrays.deepEquals(board, other.board))
            return false;
        return true;
    }
    
    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#clone()
     */
    @Override
    protected Object clone() throws CloneNotSupportedException
    {
        return new Board(this);
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
                str += "(" + x + "," + y + ") ";
                str += (getEdge(x, y, UP)) ? "U" : "-";
                str += (getEdge(x, y, RIGHT)) ? "R" : "-";
                str += (getEdge(x, y, DOWN)) ? "D" : "-";
                str += (getEdge(x, y, LEFT)) ? "L" : "-";
                if (count(x, y) == 4)
                {
                    str += (getEdge(x, y, WHITE)) ? "W" : "B";
                }
                else
                {
                    str += "-";
                }
                str += "\n";
            }
        }
        str += "]";
        return str;
    }
    
    // public static void main(String[] args)
    // {
    // Board b = new Board(3);
    // System.out.println(b);
    // System.out.println(b.eval(Squares.WHITE));
    // System.out.println(b.eval(Squares.BLACK));
    // Map<Board, String> children = b.getChildren(Squares.WHITE);
    // for (Board a : children.keySet())
    // {
    // System.out.println(children.get(a));
    // System.out.println(a);
    // System.out.println(a.eval(Squares.WHITE));
    // System.out.println(a.eval(Squares.BLACK));
    // }
    // }
}
