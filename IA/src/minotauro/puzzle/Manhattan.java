
package minotauro.puzzle;

/**
 * @author minotauro
 *         
 */
public class Manhattan implements PuzzleHeuristic
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
            value += Math.abs((i % PuzzleMain.SIZE) - PuzzleMain.X[board[i]]);
            value += Math.abs((i / PuzzleMain.SIZE) - PuzzleMain.Y[board[i]]);
        }
        return value;
    }
    
}
