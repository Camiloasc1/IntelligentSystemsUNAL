
package unalcol.agents.examples.squares.SII_20152.minotauro;

import java.util.Map;

import unalcol.agents.Action;
import unalcol.agents.AgentProgram;
import unalcol.agents.Percept;
import unalcol.agents.examples.squares.Squares;

/**
 * @author Minotauro
 */
public class SquaresPlayer implements AgentProgram
{
    private static final int NUM_CORES = Runtime.getRuntime().availableProcessors();
    private static final String PASS = "0:0:" + Squares.TOP;// FIXME error in unalcol lib
    protected String color;
    private Map<Board, Map<String, Board>> game;
    private Map<Board, String> bestAction;
    
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
            return new Action(PASS);
        // return new Action(Squares.PASS);
        Map<String, Board> children = b.getChildren(color);
        String max = null;
        int maxVal = Integer.MIN_VALUE;
        for (String action : children.keySet())
        {
            int val = children.get(action).eval(color);
            if (val > maxVal)
            {
                max = action;
                maxVal = val;
            }
        }
        
        // ExecutorService exec = Executors.newFixedThreadPool(NUM_CORES);
        // try
        // {
        // for (final Object board : children.keySet())
        // {
        // exec.submit(new Runnable()
        // {
        // @Override
        // public void run()
        // {
        // // System.out.println("Hi!");
        // }
        // });
        // }
        // }
        // finally
        // {
        // exec.shutdown();
        // }
        String action = max;
        // System.out.println(color + "-" + act + "-" + System.currentTimeMillis());
        // System.out.println(max);
        return new Action(action);
    }
}
