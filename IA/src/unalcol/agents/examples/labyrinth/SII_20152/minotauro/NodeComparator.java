/**
 * File: NodeComparator.java
 * Package: IntelligentSystems.sii20152.miprimeranota.labyrinth.NodeComparator
 * Creation: 12/09/2015 at 7:45:25 p. m.
 */

package unalcol.agents.examples.labyrinth.SII_20152.minotauro;

import java.util.Comparator;
import java.util.HashMap;

/**
 * @author camiloasc1
 *        
 */
public class NodeComparator implements Comparator<LabyrinthNode>
{
    private HashMap<LabyrinthNode, Integer> dist;

    /**
     * @param dist
     */
    public NodeComparator(HashMap<LabyrinthNode, Integer> dist)
    {
        super();
        this.dist = dist;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(LabyrinthNode arg0, LabyrinthNode arg1)
    {
        return dist.get(arg0) - dist.get(arg1);
    }

}
