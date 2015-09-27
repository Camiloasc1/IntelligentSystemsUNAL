
package minotauro.puzzle;

/**
 * @author minaturo
 *        
 */
public class Misplaced implements PuzzleHeuristic
{

    /*
     * (non-Javadoc)
     *
     * @see minotauro.puzzle.PuzzleHeuristic#evaluate(minotauro.puzzle.PuzzleBoard)
     */
    @Override
    public int evaluate(PuzzleBoard puzzleBoard)
    {
        int value = 0;
        int[] board = puzzleBoard.getBoard();
        for (int i = 0; i < PuzzleMain.LENGTH; i++)
        {
            if (board[i] == 0)
            {
                continue;
            }
            if (board[i] != PuzzleMain.SOLVED[i])
            {
                value++;
            }
        }
        return value;
    }

}
