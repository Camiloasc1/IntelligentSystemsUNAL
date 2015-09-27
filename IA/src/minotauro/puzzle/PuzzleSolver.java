
package minotauro.puzzle;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * @author minotauro
 *        
 */
public class PuzzleSolver
{
    static class NodeComparator implements Comparator<PuzzleBoard>
    {
        HashMap<PuzzleBoard, Integer> dist;
        PuzzleHeuristic heuristic;

        /**
         * @param dist
         * @param heuristic
         */
        public NodeComparator(HashMap<PuzzleBoard, Integer> dist, PuzzleHeuristic heuristic)
        {
            super();
            this.dist = dist;
            this.heuristic = heuristic;
        }
        
        /*
         * (non-Javadoc)
         *
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        @Override
        public int compare(PuzzleBoard arg0, PuzzleBoard arg1)
        {
            return (dist.get(arg0) + heuristic.evaluate(arg0)) - dist.get(arg1) - heuristic.evaluate(arg1);
        }
    }

    public static int AStarCount(PuzzleBoard root, PuzzleHeuristic heuristic)
    {
        int count = 0;
        HashMap<PuzzleBoard, Integer> dist = new HashMap<PuzzleBoard, Integer>();
        dist.put(root, 0);

        PriorityQueue<PuzzleBoard> queue = new PriorityQueue<PuzzleBoard>(new NodeComparator(dist, heuristic));
        queue.add(root);
        
        while (!queue.isEmpty())
        {
            PuzzleBoard n = queue.remove();
            if (n.isSolved())
                return count;
            count++;
            for (PuzzleBoard m : n.getNeighbors())
            {
                int nd = dist.get(n) + 1;
                if (!dist.containsKey(m) || (nd < dist.get(m)))
                {
                    dist.put(m, nd);
                    queue.add(m);
                }
            }
        }
        
        return -1;
    }
}
