/**
 * File: ZeroHeuristic.java
 * Package: IntelligentSystems.minotauro.puzzle.ZeroHeuristic
 * Creation: 26/09/2015 at 8:26:17 p. m.
 */

package minotauro.puzzle;

/**
 * @author camiloasc1
 *         
 */
public class ZeroHeuristic implements PuzzleHeuristic
{
    /*
     * (non-Javadoc)
     *
     * @see minotauro.puzzle.PuzzleHeuristic#evaluate(minotauro.puzzle.PuzzleBoard)
     */
    @Override
    public int evaluate(PuzzleBoard puzzleBoard)
    {
        return 0;
    }
    
}
