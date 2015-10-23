
package unalcol.agents.examples.squares.SII_20152.minotauro;

import unalcol.agents.Percept;
import unalcol.agents.examples.squares.Squares;
import unalcol.agents.simulate.util.SimpleLanguage;

/**
 * Wrapper for the agent's perceptions
 *
 * @author Minotauro
 */
enum Perceptions
{
    TURN(Squares.TURN),
    W_TIME(Squares.WHITE + "_" + Squares.TIME),
    B_TIME(Squares.BLACK + "_" + Squares.TIME),
    SIZE(Squares.SIZE),
    BOX("");
    
    public static SimpleLanguage language;
    private String query;
    
    /**
     * @param query
     */
    private Perceptions(String query)
    {
        this.query = query;
    }
    
    /**
     * @param p
     * @return
     */
    public Object getPerception(Percept p)
    {
        return p.getAttribute(query);
    }
    
    /**
     * @param p
     * @return
     */
    public boolean getBooleanPerception(Percept p)
    {
        return ((Boolean) getPerception(p)).booleanValue();
    }
    
    /**
     * @param p
     * @return
     */
    public String getStringPerception(Percept p)
    {
        return (String) getPerception(p);
    }
    
    /**
     * @param p
     * @return
     */
    public int getIntPerception(Percept p)
    {
        return Integer.valueOf(getStringPerception(p));
    }
    
    /**
     * @param p
     * @param x
     * @param y
     * @param color
     * @return
     */
    public Box getBox(Percept p, int x, int y)
    {
        boolean[] edges = new boolean[]
        { ((String) p.getAttribute(x + ":" + y + ":" + Squares.TOP)).equals(Squares.TRUE),
                ((String) p.getAttribute(x + ":" + y + ":" + Squares.RIGHT)).equals(Squares.TRUE),
                ((String) p.getAttribute(x + ":" + y + ":" + Squares.BOTTOM)).equals(Squares.TRUE),
                ((String) p.getAttribute(x + ":" + y + ":" + Squares.LEFT)).equals(Squares.TRUE) };
        String player = (String) p.getAttribute(x + ":" + y + ":" + Squares.COLOR);
        if (player.equals(Squares.WHITE))
        {
            player = Squares.BLACK;
        }
        else if (player.equals(Squares.BLACK))
        {
            player = Squares.WHITE;
        }
        return new Box(edges, player);
    }
}
