
package unalcol.agents.examples.squares.SII_20152.minotauro;

import java.util.HashMap;

import unalcol.agents.Action;
import unalcol.agents.AgentProgram;
import unalcol.agents.Percept;
import unalcol.agents.examples.squares.Squares;

/**
 *
 * @author Minotauro
 */
public class SquaresPlayer implements AgentProgram
{
    protected String color;

    public SquaresPlayer(String color)
    {
        this.color = color;
    }

    @Override
    public void init()
    {
    }

    @Override
    public Action compute(Percept p)
    {
        // Wait my turn
        while (!p.getAttribute(Squares.TURN).equals(color))
        {
            Board b = new Board(p);
            if (b.isFull())
                return new Action(Squares.PASS);
        }
        // try
        // {
        // Thread.sleep(2000l);
        // }
        // catch (Exception e)
        // {
        // }
        Board b = new Board(p);
        // System.out.println(b);
        if (b.isFull())
            return new Action("0:0:" + Squares.TOP);// FIXME error in unalcol lib
        // return new Action(Squares.PASS);
        HashMap<Board, String> childs = b.getChilds(color);
        Board max = null;
        int maxVal = Integer.MIN_VALUE;
        for (Board board : childs.keySet())
        {
            int val = board.eval(color);
            if (val > maxVal)
            {
                max = board;
                maxVal = val;
            }
        }
        String act = childs.get(max);
        // System.out.println(color + "-" + act + "-" + System.currentTimeMillis());
        // System.out.println(max);
        return new Action(act);
    }
}
