
package unalcol.agents.examples.squares.SII_20152.minotauro;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Minotauro
 */
public class GameTree
{
    private static final int NUM_CORES = Runtime.getRuntime().availableProcessors();
    
    private Map<Board, Map<Board, String>> gameTree;
    private Map<Board, Board> bestChild;
    private Board root;
    private String player;
    private int depth;
    private Map<Board, Board> maxPruning;
    private Map<Board, Board> minPruning;
    
    /**
     * @param root
     */
    public GameTree(Board root, String player)
    {
        super();
        gameTree = new HashMap<Board, Map<Board, String>>();
        bestChild = new HashMap<Board, Board>();
        this.root = root;
        this.player = player;
        depth = 0;
        maxPruning = new HashMap<Board, Board>();
        minPruning = new HashMap<Board, Board>();
        // ExecutorService exec = Executors.newFixedThreadPool(NUM_CORES);
    }
    
    /**
     * @param root
     *            the root to set
     */
    public void setRoot(Board root)
    {
        this.root = root;
        depth = 0;
    }
    
    /**
     * @return the new depth
     */
    public int increaseDepth()
    {
        return increaseDepth(1);
    }
    
    /**
     * @param increment
     *            the increment
     * @return the new depth
     */
    public int increaseDepth(int increment)
    {
        depth += increment;
        alphabeta(root, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
        return depth;
    }
    
    /**
     * @param root
     *            the new root
     * @return the new depth
     */
    public int increaseDepth(Board root)
    {
        setRoot(root);
        return increaseDepth(1);
    }
    
    public int increaseDepth(Board root, int increment)
    {
        setRoot(root);
        return increaseDepth(increment);
    }
    
    public String get(Board b1, Board b2)
    {
        if (gameTree.containsKey(b1) && gameTree.get(b1).containsKey(b2))
            return gameTree.get(b1).get(b2);
        return null;
    }
    
    public String getBestMove(Board state)
    {
        if (bestChild.containsKey(state))
            return get(state, bestChild.get(state));
        return null;
    }
    
    public Board getBestChild(Board state)
    {
        if (bestChild.containsKey(state))
            return bestChild.get(state);
        return null;
    }
    
    private int alphabeta(Board root, int depth, int a, int b, boolean maximize)
    {
        if ((depth == 0) || root.isFull())
            return root.eval(player);
        if (maximize)
        {
            int bestVal = Integer.MIN_VALUE;
            Board bestBoard = null;
            if (!gameTree.containsKey(root))
            {
                gameTree.put(root, root.getChildren(player));
            }
            Set<Board> children = new LinkedHashSet<Board>();
            if (maxPruning.containsKey(root))
            {
                children.add(maxPruning.get(root));
            }
            children.addAll(gameTree.get(root).keySet());
            for (Board child : children)
            {
                int childVal = alphabeta(child, depth - 1, a, b, false);
                if (childVal > bestVal)
                {
                    bestVal = childVal;
                    bestBoard = child;
                }
                if (bestVal > a)
                {
                    a = bestVal;
                }
                if (b <= a)
                {
                    maxPruning.put(root, child);
                    break;
                }
            }
            bestChild.put(root, bestBoard);
            return bestVal;
        }
        else
        {
            int bestVal = Integer.MAX_VALUE;
            if (!gameTree.containsKey(root))
            {
                gameTree.put(root, root.getChildren(Board.swapPlayer(player)));
            }
            Set<Board> children = new LinkedHashSet<Board>();
            if (minPruning.containsKey(root))
            {
                children.add(minPruning.get(root));
            }
            children.addAll(gameTree.get(root).keySet());
            for (Board child : children)
            {
                int newVal = alphabeta(child, depth - 1, a, b, true);
                if (newVal < bestVal)
                {
                    bestVal = newVal;
                }
                if (bestVal < b)
                {
                    b = bestVal;
                }
                if (b <= a)
                {
                    minPruning.put(root, child);
                    break;
                }
            }
            return bestVal;
        }
    }
}
