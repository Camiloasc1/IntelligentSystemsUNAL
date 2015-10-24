
package unalcol.agents.examples.squares.SII_20152.minotauro;

import unalcol.agents.Action;
import unalcol.agents.AgentProgram;
import unalcol.agents.Percept;
import unalcol.agents.examples.squares.Squares;

/**
 * @author Minotauro
 */
public class SquaresPlayer implements AgentProgram
{
    private static final String PASS = "0:0:" + Squares.TOP;// FIXME error in unalcol lib
    protected String color;
    private GameTree gameTree;
    
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
                return new Action(PASS);
        }
        Board b = new Board(p);
        if (b.isFull())
            return new Action(PASS);
        gameTree = new GameTree(b, color);
        String action;
        while ((action = gameTree.getBestMove(b)) == null)
        {
            gameTree.increaseDepth();
        }
        // System.out.println(color + ":" + action);
        // System.out.println(gameTree.getBestChild(b));
        return new Action(action);
    }
}
