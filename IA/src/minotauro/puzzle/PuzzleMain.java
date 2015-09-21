
package minotauro.puzzle;

/**
 * @author minotauro
 *         
 */
public class PuzzleMain
{
    public static final int SIZE = 3;// 3 * 3 - 1 = 8

    public static void main(String[] args)
    {
        PuzzleBoard board = new PuzzleBoard(SIZE);
        System.out.println(board);
        System.out.println(board.isSolved());
        for (PuzzleBoard b : board.getNeighbors())
        {
            System.out.println(b);
            System.out.println(b.isSolved());
        }
    }
}
