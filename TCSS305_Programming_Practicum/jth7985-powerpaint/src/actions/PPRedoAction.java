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
 * A redo action for PowerPaint, fires "file" property change with new value 
 * "redo" when pressed.
 * 
 * @author Jonathan Hughes
 * @version 15 November 2014
 */
@SuppressWarnings("serial")
public class PPRedoAction extends AbstractAction {

    /** The tool name associated with this action. */
    private static final String TOOL_NAME = "Redo";
    
    /** The mnemonic associated with this action. */
    private static final int TOOL_MNEMONIC = KeyEvent.VK_R;
    
    /** The accelerator associated with this action. */
    private static final KeyStroke TOOL_ACCEL = KeyStroke.getKeyStroke("control Y");
    
    /**
     * Construct this Action.
     */
    public PPRedoAction() {
        super(TOOL_NAME);
        putValue(Action.MNEMONIC_KEY, TOOL_MNEMONIC);
        putValue(Action.ACCELERATOR_KEY, TOOL_ACCEL);
    }

    /** 
     * Fires "file" property change with value "redo".
     * 
     * {@inheritDoc} 
     */
    @Override
    public void actionPerformed(final ActionEvent theEvent) {
        firePropertyChange("file", null, "redo");
    } 
}