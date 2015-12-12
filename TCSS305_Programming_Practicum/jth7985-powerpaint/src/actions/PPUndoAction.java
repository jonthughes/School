/* 
 * TCSS 305 – Autumn 2014
 * Assignment 5 - powerpaint
 */

package actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

/**
 * An undo action for PowerPaint, fires "file" property change with new value 
 * "undo" when pressed.
 * 
 * @author Jonathan Hughes
 * @version 15 November 2014
 */
@SuppressWarnings("serial")
public class PPUndoAction extends AbstractAction {

    /** The tool name associated with this action. */
    private static final String TOOL_NAME = "Undo";
    
    /** The mnemonic associated with this action. */
    private static final int TOOL_MNEMONIC = KeyEvent.VK_U;
    
    /** The accelerator associated with this action. */
    private static final KeyStroke TOOL_ACCEL = KeyStroke.getKeyStroke("control Z");
    
    /**
     * Construct this Action.
     */
    public PPUndoAction() {
        super(TOOL_NAME);
        putValue(Action.MNEMONIC_KEY, TOOL_MNEMONIC);
        putValue(Action.ACCELERATOR_KEY, TOOL_ACCEL);
    }

    /** 
     * Fires "file" property change with value "undo".
     * 
     * {@inheritDoc} 
     */
    @Override
    public void actionPerformed(final ActionEvent theEvent) {
        firePropertyChange("file", null, "undo");
    } 
}