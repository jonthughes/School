/* 
 * TCSS 305 – Autumn 2014
 * Assignment 5 - powerpaint
 */

package actions;

import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;

/**
 * A custom Line action for PowerPaint.
 * 
 * @author Jonathan Hughes
 * @version 30 October 2014
 */
@SuppressWarnings("serial")
public class PPLineAction extends AbstractPPToolAction {

    /** The tool name associated with this action. */
    private static final String TOOL_NAME = "Line";
    
    /** The mnemonic associated with this action. */
    private static final int TOOL_MNEMONIC = KeyEvent.VK_L;
    
    /**
     * Construct this Action.
     */
    public PPLineAction() {
        super(TOOL_NAME, TOOL_MNEMONIC);
    }

    /** 
     * Fires the property change "tool" with this tool as the new value.
     * 
     * {@inheritDoc} 
     */
    @Override
    public void actionPerformed(final ActionEvent theEvent) {
        firePropertyChange("tool", null, this);
    }

    /** 
     * Returns a Shape that is a Line2D.Double created from the given coordinates.
     * 
     * @param x1 the first x coordinate passed
     * @param y1 the first y coordinate passed
     * @param x2 the second x coordinate passed
     * @param y2 the second y coordinate passed
     * @return a Line2D.Double created from the given coordinates
     */
    private Shape getShape(final int x1, final int y1, final int x2, final int y2) {
        return new Line2D.Double(x1, y1, x2, y2);
    }
    
    /** 
     * {@inheritDoc}
     */
    @Override
    public Shape mousePressed(final int x1, final int y1, final int x2, final int y2) {
        return getShape(x1, y1, x2, y2);
    }
    
    /** 
     * {@inheritDoc}
     */
    @Override
    public Shape mouseDragged(final int x1, final int y1, final int x2, final int y2) {
        return getShape(x1, y1, x2, y2);
    }
    
    /** 
     * {@inheritDoc}
     */
    @Override
    public Shape mouseReleased(final int x1, final int y1, final int x2, final int y2) {
        return getShape(x1, y1, x2, y2);
    }
}

