
package unalcol.agents.examples.squares.SII_20152.minotauro;

import java.util.LinkedHashMap;
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
    private Set<Board> lastLevel;
    private String currentPlayer;
    private int depth;
    
    /**
     * @param root
     */
    public GameTree(Board root, String player)
    {
        super();
        gameTree = new LinkedHashMap<Board, Map<Board, String>>();
        bestChild = new LinkedHashMap<Board, Board>();
        lastLevel = new LinkedHashSet<Board>();
        currentPlayer = player;
        depth = 0;
        gameTree.put(root, new LinkedHashMap<Board, String>());
        bestChild.put(root, null);
        lastLevel.add(root);
    }
    
    /**
     * @return the new depth
     */
    public int increaseDepth()
    {
        Set<Board> newLastLevel = new LinkedHashSet<Board>();
        // ExecutorService exec = Executors.newFixedThreadPool(NUM_CORES);
        // try
        // {
        for (final Board b1 : lastLevel)
        {
            // exec.submit(new Runnable()
            // {
            // @Override
            // public void run()
            // {
            
            Map<Board, String> children = b1.getChildren(currentPlayer);
            gameTree.put(b1, children);
            System.out.println(b1 + " " + children.size());
            // newLastLevel.addAll(children.keySet());
            Board max = null;
            int maxVal = Integer.MIN_VALUE;
            for (final Board b2 : children.keySet())
            {
                newLastLevel.add(b2);
                int val = b2.eval(currentPlayer);
                if (val > maxVal)
                {
                    max = b2;
                    maxVal = val;
                }
            }
            bestChild.put(b1, max);
            // }
            // });
        }
        // }
        // finally
        // {
        // exec.shutdown();
        // }
        currentPlayer = SquaresPlayer.playerSwap(currentPlayer);
        lastLevel = newLastLevel;
        return depth++;
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
    
    private int alphabeta(Board root, int depth, int a, int b, boolean maximize, String player)
    {
        if ((depth == 0) || root.isFull())
            return root.eval(player);
        if (maximize)
        {
            int bestVal = Integer.MIN_VALUE;
            Board bestBoard = null;
            Map<Board, String> children = root.getChildren(player);
            for (Board child : children.keySet())
            {
                int childVal = alphabeta(child, depth - 1, a, b, false, player);
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
                    break;
                }
            }
            bestChild.put(root, bestBoard);
            return bestVal;
        }
        else
        {
            int bestVal = Integer.MAX_VALUE;
            Map<Board, String> children = root.getChildren(player);
            for (Board child : children.keySet())
            {
                int newVal = alphabeta(child, depth - 1, a, b, true, player);
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
                    break;
                }
            }
            return bestVal;
        }
    }
}
