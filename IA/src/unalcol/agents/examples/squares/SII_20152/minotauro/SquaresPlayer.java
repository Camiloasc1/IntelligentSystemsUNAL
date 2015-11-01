
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
    private static final long SLEEP_TIME = 10l;
    private static final int DEFAULT_SIZE = 8;
    private static final String ZERO_TIME = "0:0:0:0";
    private static final String PASS = "0:0:" + Squares.TOP;// FIXME error in unalcol lib
    protected String color;
    private GameTree gameTree;
    private long realTime;
    private Percept lastPercept;
    private TimeCheck timeCheck;
    
    public SquaresPlayer(String color)
    {
        this.color = color;
        init();
        timeCheck = new TimeCheck();
        timeCheck.start();
        // Board b = new Board(4);
        // gameTree = new GameTree(b, color);
        // for (int i = 0; i < 32; i++)
        // {
        // long start = System.currentTimeMillis();
        // gameTree.increaseDepth();
        // long end = System.currentTimeMillis();
        // System.out.println(i + ":" + (end - start));
        // }
    }
    
    @Override
    public void init()
    {
        gameTree = new GameTree(new Board(DEFAULT_SIZE), color);
        realTime = 0;
    }
    
    @Override
    public Action compute(Percept p)
    {
        long start = System.currentTimeMillis();
        lastPercept = p;
        // Wait my turn
        while (!p.getAttribute(Squares.TURN).equals(color))
        {
            Board b = new Board(p);
            if (b.isFull())
                return new Action(PASS);
        }
        
        // Get current board
        Board b = new Board(p);
        if (b.isFull())
            return new Action(PASS);
        gameTree.setRoot(b);
        
        // Get best move
        String action;
        while ((action = gameTree.getBestMove(b)) == null)
        {
            // System.out.println("wait");
            try
            {
                Thread.sleep(SLEEP_TIME);
            }
            catch (InterruptedException e)
            {
            }
        }
        realTime += System.currentTimeMillis() - start;
        // System.out.println(b.turnCount() + ":" + color + ":" + action + ":" + gameTree.getValue(b) + ":"
        // + (System.currentTimeMillis() - start) + ":" + realTime);
        // System.out.println(b + "\n" + b.eval(color));
        // System.out.println(gameTree.getBestChild(b) + "\n" + gameTree.getBestChild(b).eval(color));
        return new Action(action);
    }
    
    private class TimeCheck extends Thread
    {
        
        @Override
        public void run()
        {
            while (true)
            {
                try
                {
                    sleep(250l);
                }
                catch (InterruptedException e)
                {
                }
                if ((lastPercept == null) || (gameTree == null))
                {
                    continue;
                }
                // System.out.println(Perceptions.W_TIME.getStringPerception(lastPercept));
                // System.out.println(Perceptions.B_TIME.getStringPerception(lastPercept));// FIXME unalcol always return the white time
                // Check time
                if (Perceptions.W_TIME.getStringPerception(lastPercept).equals(ZERO_TIME)
                        || Perceptions.B_TIME.getStringPerception(lastPercept).equals(ZERO_TIME))
                {
                    // System.out.println("kill");
                    gameTree.stopExplorer();
                    // return;
                }
            }
        }
    };
}
