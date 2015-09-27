
package minotauro.puzzle;

/**
 * @author minotauro
 *         
 */
public interface PuzzleHeuristic
{
    /**
     * @param puzzleBoard
     * @return The heuristic evaluation for the given PuzzleBoard
     */
    public int evaluate(PuzzleBoard puzzleBoard);
}
