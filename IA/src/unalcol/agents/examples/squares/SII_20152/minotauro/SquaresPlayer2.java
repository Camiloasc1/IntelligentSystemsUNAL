
package unalcol.agents.examples.squares.SII_20152.minotauro;

import java.util.Map;

import unalcol.agents.Action;
import unalcol.agents.AgentProgram;
import unalcol.agents.Percept;
import unalcol.agents.examples.squares.Squares;

/**
 * @author Minotauro
 */
public class SquaresPlayer2 implements AgentProgram
{
    private static final String PASS = "0:0:" + Squares.TOP;// FIXME error in unalcol lib
    protected String color;
    
    public SquaresPlayer2(String color)
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
                return new Action(PASS);
        }
        Board b = new Board(p);
        if (b.isFull())
            return new Action(PASS);
        Map<Board, String> children = b.getChildren(color);
        Board max = null;
        int maxVal = Integer.MIN_VALUE;
        for (Board action : children.keySet())
        {
            int val = action.eval(color);
            if (val > maxVal)
            {
                max = action;
                maxVal = val;
            }
        }
        
        String action = children.get(max);
        return new Action(action);
    }
}
