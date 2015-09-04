
package unalcol.agents.examples.labyrinth.variable;

import unalcol.agents.Agent;
import unalcol.agents.examples.labyrinth.Labyrinth;
import unalcol.agents.examples.labyrinth.LabyrinthDrawer;
import unalcol.agents.examples.labyrinth.sii20152.miprimeranota.LabyrinthAgent;
import unalcol.agents.simulate.util.SimpleLanguage;

public class TeseoVariableMain
{
    private static SimpleLanguage getLanguage()
    {
        return new SimpleLanguage(new String[]
        { "front", "right", "back", "left", "exit", "afront", "aright", "aback", "aleft" }, new String[]
        { "no_op", "die", "advance", "rotate" });
    }
    
    public static void main(String[] argv)
    {
        // InteractiveAgentProgram p = new InteractiveAgentProgram( getLanguage() );
        // TeseoSimple p = new TeseoSimple();
        LabyrinthAgent p = new LabyrinthAgent(getLanguage());
        // RandomReflexTeseo p = new RandomReflexTeseo();
        // p.setLanguage(getLanguage());
        LabyrinthDrawer.DRAW_AREA_SIZE = 600;
        LabyrinthDrawer.CELL_SIZE = 40;
        Labyrinth.DEFAULT_SIZE = 15;
        Agent agent = new Agent(p);
        TeseoVariableMainFrame frame = new TeseoVariableMainFrame(agent, getLanguage(), 0.5);
        frame.setVisible(true);
    }
}
