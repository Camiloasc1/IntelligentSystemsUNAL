
package sii20152.miprimeranota.labyrinth;

import unalcol.agents.Action;
import unalcol.agents.simulate.util.SimpleLanguage;

/**
 * Wrapper for the agent's actions
 *
 * @author Mi Primera Nota
 *         
 */
enum Actions
{
    NOP(0),
    DIE(1),
    ADVANCE(2),
    ROTATE(3);

    public static SimpleLanguage language;
    private int actionIndex;

    /**
     * @param action
     */
    private Actions(int actionIndex)
    {
        this.actionIndex = actionIndex;
    }

    /**
     * @return
     */
    public Action getAction()
    {
        return new Action(language.getAction(actionIndex));
    }

    /**
     * @param action1
     * @param action2
     * @return
     */
    public static boolean compare(Action action1, Action action2)
    {
        return action1.getCode().equals(action2.getCode());
    }
}
