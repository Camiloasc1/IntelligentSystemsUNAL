
package unalcol.agents.examples.labyrinth.SII_20152.minotauro;

import unalcol.agents.Percept;
import unalcol.agents.simulate.util.SimpleLanguage;

/**
 * Wrapper for the agent's perceptions
 *
 * @author Mi Primera Nota
 *
 */
enum Perceptions
{
    WFRONT(0),
    WRIGHT(1),
    WBACK(2),
    WLEFT(3),
    GOALREACHED(4),
    FAIL(5),
    AFRONT(6),
    ARIGHT(7),
    ABACK(8),
    ALEFT(9);
    
    public static SimpleLanguage language;
    private int perceptionIndex;
    
    /**
     * @param action
     */
    private Perceptions(int perceptionIndex)
    {
        this.perceptionIndex = perceptionIndex;
    }
    
    /**
     * @param p
     * @return
     */
    public Object getPerception(Percept p)
    {
        return p.getAttribute(language.getPercept(perceptionIndex));
    }
    
    /**
     * @param p
     * @return
     */
    public boolean getBooleanPerception(Percept p)
    {
        return ((Boolean) getPerception(p)).booleanValue();
    }
}
