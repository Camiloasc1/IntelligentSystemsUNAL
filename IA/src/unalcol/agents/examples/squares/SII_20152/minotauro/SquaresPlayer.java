
package unalcol.agents.examples.squares.SII_20152.minotauro;

import unalcol.agents.Action;
import unalcol.agents.AgentProgram;
import unalcol.agents.Percept;
import unalcol.agents.examples.squares.Squares;
import unalcol.types.collection.vector.Vector;

/**
 *
 * @author Minotauro
 */
public class SquaresPlayer implements AgentProgram
{
    protected String color;
    
    public SquaresPlayer(String color)
    {
        this.color = color;
    }
    
    @Override
    public void init()
    {
    }

    @Override
    public Action compute(Percept p)
    {
        // Wait my turn
        while (!p.getAttribute(Squares.TURN).equals(color))
        {
            // try
            // {
            // Thread.sleep(10l);
            // }
            // catch (Exception e)
            // {
            // }
        }
        int size = Integer.parseInt((String) p.getAttribute(Squares.SIZE));
        int x = 0;
        int y = 0;
        Vector<String> v = new Vector<String>();
        while (v.size() == 0)
        {
            x = (int) (size * Math.random());
            y = (int) (size * Math.random());
            if (((String) p.getAttribute(x + ":" + y + ":" + Squares.LEFT)).equals(Squares.FALSE))
            {
                v.add(Squares.LEFT);
            }
            if (((String) p.getAttribute(x + ":" + y + ":" + Squares.TOP)).equals(Squares.FALSE))
            {
                v.add(Squares.TOP);
            }
            if (((String) p.getAttribute(x + ":" + y + ":" + Squares.BOTTOM)).equals(Squares.FALSE))
            {
                v.add(Squares.BOTTOM);
            }
            if (((String) p.getAttribute(x + ":" + y + ":" + Squares.RIGHT)).equals(Squares.FALSE))
            {
                v.add(Squares.RIGHT);
            }
        }
        String act = x + ":" + y + ":" + v.get((int) (Math.random() * v.size()));
        // System.out.println(color + " " + act + " " + System.currentTimeMillis());
        return new Action(act);
        // return new Action(Squares.PASS);
    }

}
