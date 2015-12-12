/* 
 * TCSS 305 – Autumn 2014
 * Assignment 5 - powerpaint
 */

package actions;

import java.awt.Shape;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 * A custom Abstract action for PowerPaint.
 * 
 * @author Jonathan Hughes
 * @version 30 October 2014
 */
@SuppressWarnings("serial")
public abstract class AbstractPPToolAction extends AbstractAction {
    
    /**
     * Construct this Action.
     * 
     * @param theToolName the name of the tool to associate with this Action
     * @param theMnemonic a mnemonic to associate with this Action.
     */
    public AbstractPPToolAction(final String theToolName, final int theMnemonic) {
        super(theToolName);
        putValue(Action.MNEMONIC_KEY, theMnemonic);
        putValue(Action.SELECTED_KEY, true);
    }

    /** 
     * {@inheritDoc} 
     */
    @Override
    public abstract void actionPerformed(final ActionEvent theEvent);
    
    /**
     * Returns the Shape that is created from the given coordinates when the mouse is pressed.
     * 
     * @param x1 the first x coordinate passed
     * @param y1 the first y coordinate passed
     * @param x2 the second x coordinate passed
     * @param y2 the second y coordinate passed
     * @return the Shape that is created from the given coordinates when the mouse is pressed
     */    
    public abstract Shape mousePressed(final int x1, final int y1, 
                                       final int x2, final int y2);
    
    /**
     * Returns the Shape that is created from the given coordinates when the mouse is dragged.
     * 
     * @param x1 the first x coordinate passed
     * @param y1 the first y coordinate passed
     * @param x2 the second x coordinate passed
     * @param y2 the second y coordinate passed
     * @return the Shape that is created from the given coordinates when the mouse is dragged
     */   
    public abstract Shape mouseDragged(final int x1, final int y1, 
                                       final int x2, final int y2);
    
    /**
     * Returns the Shape that is created from the given coordinates when the mouse is released.
     * 
     * @param x1 the first x coordinate passed
     * @param y1 the first y coordinate passed
     * @param x2 the second x coordinate passed
     * @param y2 the second y coordinate passed
     * @return the Shape that is created from the given coordinates when the mouse is released
     */   
    public abstract Shape mouseReleased(final int x1, final int y1, 
                                        final int x2, final int y2);
}

