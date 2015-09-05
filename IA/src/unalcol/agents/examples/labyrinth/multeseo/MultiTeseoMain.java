/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package unalcol.agents.examples.labyrinth.multeseo;

import sii20152.miprimeranota.labyrinth.LabyrinthAgent;
import unalcol.agents.Agent;
import unalcol.agents.AgentProgram;
import unalcol.agents.examples.labyrinth.Labyrinth;
import unalcol.agents.examples.labyrinth.LabyrinthDrawer;
import unalcol.agents.examples.labyrinth.teseo.simple.RandomReflexTeseo;
import unalcol.agents.simulate.util.SimpleLanguage;
import unalcol.types.collection.vector.Vector;

public class MultiTeseoMain
{
    private static SimpleLanguage getLanguage()
    {
        return new SimpleLanguage(new String[]
        { "front", "right", "back", "left", "exit", "afront", "aright", "aback", "aleft" }, new String[]
        { "no_op", "die", "advance", "rotate" });
    }
    
    public static void main(String[] argv)
    {
        AgentProgram[] teseo = new AgentProgram[4];
        int index1 = 0;
        int index2 = 2;
        teseo[0] = new LabyrinthAgent(getLanguage());
        teseo[1] = new LabyrinthAgent(getLanguage());
        teseo[2] = new RandomReflexTeseo();
        ((RandomReflexTeseo) teseo[2]).setLanguage(getLanguage());
        teseo[3] = new RandomReflexTeseo();
        ((RandomReflexTeseo) teseo[3]).setLanguage(getLanguage());
        
        LabyrinthDrawer.DRAW_AREA_SIZE = 600;
        LabyrinthDrawer.CELL_SIZE = 40;
        Labyrinth.DEFAULT_SIZE = 15;
        
        Agent agent1 = new Agent(teseo[index1]);
        Agent agent2 = new Agent(teseo[index2]);
        
        // Agent agent3 = new Agent(p3);
        Vector<Agent> agent = new Vector<Agent>();
        agent.add(agent1);
        agent.add(agent2);
        // Agent agent = new Agent( new RandomReflexTeseoAgentProgram( getLanguage() ) );
        MultiAgentLabyrinthMainFrame frame = new MultiAgentLabyrinthMainFrame(agent, getLanguage());
        frame.setVisible(true);
    }
}
