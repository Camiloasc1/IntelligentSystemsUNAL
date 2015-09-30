
package minotauro.puzzle;

import java.util.Random;

/**
 * @author minotauro
 *        
 */
public class PuzzleMain
{
    public static final int SIZE = 3;// 3 * 3 - 1 = 8
    public static final int LENGTH = SIZE * SIZE;
    public static int[] SOLVED;
    public static int[] X;
    public static int[] Y;
    private static Random random = new Random();
    
    static
    {
        SOLVED = new PuzzleBoard(SIZE).getBoard();
        X = new int[SIZE * SIZE];
        Y = new int[SIZE * SIZE];
        for (int i = 0; i < (SIZE * SIZE); i++)
        {
            X[i] = (((i + (SIZE * SIZE)) - 1) % (SIZE * SIZE)) % SIZE;
            Y[i] = (((i + (SIZE * SIZE)) - 1) % (SIZE * SIZE)) / SIZE;
        }
    }
    
    public static void main(String[] args)
    {
        
        int test = 10000;
        long[] sum = new long[]
        { 0, 0, 0, 0, 0 };
        long[] max = new long[]
        { 0, 0, 0, 0, 0 };
        for (int t = 0; t < test; t++)
        {
            PuzzleBoard board = shuffle(new PuzzleBoard(SIZE), 64);
            int[] count = eval(board);
            for (int i = 0; i < count.length; i++)
            {
                sum[i] += count[i];
                if (count[i] > max[i])
                {
                    max[i] = count[i];
                }
            }
        }
        System.out.println("Heuristic\tAverage\tMax");
        System.out.println(":Zero:\t\t" + (sum[0] / test) + "\t" + max[0]);
        System.out.println(":Misplaced:\t" + (sum[1] / test) + "\t" + max[1]);
        System.out.println(":Rows Cols:\t" + (sum[2] / test) + "\t" + max[2]);
        System.out.println(":Manhattan:\t" + (sum[3] / test) + "\t" + max[3]);
        System.out.println(":ManhattanV2:\t" + (sum[4] / test) + "\t" + max[4]);
    }

    public static PuzzleBoard shuffle(PuzzleBoard board, int moves)
    {
        for (int i = 0; i < moves; i++)
        {
            PuzzleBoard[] neighbors = board.getNeighbors();
            board = neighbors[random.nextInt(neighbors.length)];
        }
        return board;
    }
    
    public static int[] eval(PuzzleBoard board)
    {
        return new int[]
        { PuzzleSolver.AStarCount(board, new ZeroHeuristic()), PuzzleSolver.AStarCount(board, new Misplaced()),
                PuzzleSolver.AStarCount(board, new MisplacedRowsCols()), PuzzleSolver.AStarCount(board, new Manhattan()),
                PuzzleSolver.AStarCount(board, new ManhattanPowered()) };

    }
}
