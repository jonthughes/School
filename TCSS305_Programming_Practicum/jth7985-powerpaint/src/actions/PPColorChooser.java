/* 
 * TCSS 305 – Autumn 2014
 * Assignment 5 - powerpaint
 */

package actions;

import java.awt.Color;

import javax.swing.JColorChooser;

/**
 * Runs a JChooserDialog that returns the selected Color.  
 * The starting code for this class was used from the given ColorChooser class.
 * 
 * @author Jonathan Hughes
 * @version 30 October 2014
 */
public final class PPColorChooser {
    
    /**
     * Runs a JColorChooser and returns the chosen color as a Color object.
     * 
     * @param theStartingColor the Color to start with in the JColorChooser
     * @return the chosen Color
     */
    public Color getColor(final Color theStartingColor) {
        Color result = null;
        do {
            result = JColorChooser.showDialog(null, "Select a color", theStartingColor);
            return result;
        } while (result != null); // user chooses cancel or closes window
    }
}
