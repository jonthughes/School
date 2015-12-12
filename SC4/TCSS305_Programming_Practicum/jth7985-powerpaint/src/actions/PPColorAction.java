/* 
 * TCSS 305 – Autumn 2014
 * Assignment 5 - powerpaint
 */

package actions;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 * A custom Color action for PowerPaint that can choose a color and display that 
 * color in its icon.
 * 
 * @author Jonathan Hughes
 * @version 30 October 2014
 */
@SuppressWarnings("serial")
public class PPColorAction extends AbstractAction {
    
    /** The tool name associated with this action. */
    private static final String TOOL_NAME = "Color...";
    
    /** The mnemonic associated with this action. */
    private static final int TOOL_MNEMONIC = KeyEvent.VK_C;
    
    /** A PPColorChooser for creating a color choosing dialog. */
    private final PPColorChooser myColorChooser;
    
    /** The currently selected color. */
    private Color myColor;
    
    /**
     * Construct this Action.
     */
    public PPColorAction() {
        super(TOOL_NAME);
        putValue(Action.MNEMONIC_KEY, TOOL_MNEMONIC);
        setIcon(Color.BLACK);
        myColorChooser = new PPColorChooser();
    }

    /** 
     * Chooses a Color from a color chooser and fires the property change.
     * 
     * {@inheritDoc} 
     */
    @Override
    public void actionPerformed(final ActionEvent theEvent) {
        final Color chosenColor = myColorChooser.getColor(myColor);
        if (chosenColor != null) {
            setIcon(chosenColor);
            myColor = chosenColor;
            firePropertyChange("color", null, chosenColor);
        }
    }
    
    /**
     * Changes this action's icon to the icon created by the specified color.
     * 
     * @param theColor the specified color to make the new icon
     */
    private void setIcon(final Color theColor) {
        putValue(Action.SMALL_ICON, new PPColorIcon(theColor));
    }
}