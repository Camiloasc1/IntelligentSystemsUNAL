/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package unalcol.agents.examples.squares;

import unalcol.agents.Agent;
import unalcol.agents.examples.squares.SII_20152.minotauro.SquaresPlayer;
import unalcol.agents.examples.squares.SII_20152.minotauro.SquaresPlayer2;

/**
 * @author Jonatan
 */
public class SquaresMain
{
    public static void main(String[] argv)
    {
        // Reflection
        Agent w_agent = new Agent(new SquaresPlayer(Squares.WHITE));
        Agent b_agent = new Agent(new SquaresPlayer2(Squares.BLACK));
        // Agent w_agent = new Agent(new DummySquaresAgentProgram(Squares.WHITE));
        // Agent b_agent = new Agent(new DummySquaresAgentProgram(Squares.BLACK));
        SquaresMainFrame frame = new SquaresMainFrame(w_agent, b_agent);
        frame.setVisible(true);
    }
    
}
