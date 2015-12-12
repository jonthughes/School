/* 
 * TCSS 305 – Autumn 2014
 * Assignment 6 - tetris
 */

package themes;

import java.awt.Color;

/**
 * A class that contains three camo colors for the Tetris GUI.
 * 
 * @author Jonathan Hughes
 * @version 02 December 2014
 */
public class TetrisCamo {

    /** The primary color for the camo. */
    private final Color myColor1;
    
    /** The secondary color for the camo. */
    private final Color myColor2;
    
    /** The tertiary color for the camo. */
    private final Color myColor3;
    
    /** 
     * Constructs this object with three Colors.
     * 
     * @param theColor1 the primary color for the camo
     * @param theColor2 the secondary color for the camo
     * @param theColor3 the tertiary color for the camo
     */
    public TetrisCamo(final Color theColor1, final Color theColor2, final Color theColor3) {
        super();
        myColor1 = theColor1;
        myColor2 = theColor2;
        myColor3 = theColor3;
    }
    
    /**
     * Returns the primary color for the camo.
     * 
     * @return the primary color for the camo
     */
    public Color getColor1() {
        return myColor1;
    }
    
    /**
     * Returns the secondary color for the camo.
     * 
     * @return the secondary color for the camo
     */
    public Color getColor2() {
        return myColor2;
    }
    
    /**
     * Returns the tertiary color for the camo.
     * 
     * @return the tertiary color for the camo
     */
    public Color getColor3() {
        return myColor3;
    }
}
