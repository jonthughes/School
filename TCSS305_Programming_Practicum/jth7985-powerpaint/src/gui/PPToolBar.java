/* 
 * TCSS 305 – Autumn 2014
 * Assignment 5 - powerpaint
 */

package gui;

import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

/**
 * A custom JToolBar for PowerPaint.
 * 
 * @author Jonathan Hughes
 * @version 30 October 2014
 */
@SuppressWarnings("serial")
public final class PPToolBar extends JToolBar {

    /** 
     * Constructs the PPToolBar. 
     * 
     * @param theActions the actions that will be turned into tool buttons
     * @param theColorAction the color action to be turned into a color button
     */
    PPToolBar(final Action[] theActions, final Action theColorAction) {
        super();
        createPPToolBar(theActions, theColorAction);                
    }
    
    /**
     * Creates a tool bar with tool buttons for the given Actions.
     * 
     * @param theActions the actions that will be turned into tool buttons
     * @param theColorAction the color action to be turned into a color button
     */
    private void createPPToolBar(final Action[] theActions, 
                                 final Action theColorAction) {
        //put in color button
        add(new JButton(theColorAction));
        addSeparator();
        //iterate through given actions and turn them all into buttons
        final ButtonGroup buttonGroup = new ButtonGroup();
        for (final Action action : theActions) {
            final JToggleButton button = new JToggleButton(action);
            add(button);
            buttonGroup.add(button);
        }
    }
}
