
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
    private static int DEFAULT_SIZE = 8;
    protected String color;
    private boolean fitSize;
    private GameTree gameTree;
    private long realTime;
    
    public SquaresPlayer(String color)
    {
        this.color = color;
        init();
    }
    
    @Override
    public void init()
    {
        fitSize = false;
        Board b = new Board(DEFAULT_SIZE);
        gameTree = new GameTree(b, color);
        int i = 11 - DEFAULT_SIZE;
        i = (i < 1) ? 1 : i;
        gameTree.increaseDepth(i);
        realTime = 0;
    }
    
    @Override
    public Action compute(Percept p)
    {
        long start = System.currentTimeMillis();
        if (!fitSize)
        {
            int size = Perceptions.SIZE.getIntPerception(p);
            if (size != DEFAULT_SIZE)
            {
                DEFAULT_SIZE = size;
                Board b = new Board(size);
                gameTree = new GameTree(b, color);
                gameTree.increaseDepth();
            }
            fitSize = true;
        }
        
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
            
        gameTree.setRoot(b);
        
        String action;
        
        int turn = b.turnCount();
        int moves = b.totalMoves();
        int depth = 1;
        double frac = 1.0 / 4.0;
        if (turn > (moves * frac))
        {
            depth = 2;
        }
        frac = 2.0 / 4.0;
        if (turn > (moves * frac))
        {
            depth = 4;
            // depth = moves - turn;
        }
        frac = 3.0 / 4.0;
        if (turn > (moves * frac))
        {
            depth = moves - turn;
        }
        while ((action = gameTree.getBestMove(b)) == null)
        {
            gameTree.increaseDepth(depth);
        }
        realTime += System.currentTimeMillis() - start;
        System.out.println(
                depth + ":" + turn + ":" + color + ":" + action + ":" + (System.currentTimeMillis() - start) + ":" + realTime);
        // System.out.println(gameTree.getBestChild(b));
        return new Action(action);
    }
}
