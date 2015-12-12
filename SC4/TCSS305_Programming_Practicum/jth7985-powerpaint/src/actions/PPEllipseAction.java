/* 
 * TCSS 305 – Autumn 2014
 * Assignment 5 - powerpaint
 */

package actions;

import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;

/**
 * A custom Ellipse action for PowerPaint.
 * 
 * @author Jonathan Hughes
 * @version 30 October 2014
 */
@SuppressWarnings("serial")
public class PPEllipseAction extends AbstractPPToolAction {
    
    /** The tool name associated with this action. */
    private static final String TOOL_NAME = "Ellipse";
    
    /** The mnemonic associated with this action. */
    private static final int TOOL_MNEMONIC = KeyEvent.VK_E;

    /**
     * Construct this Action.
     */
    public PPEllipseAction() {
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
     * Returns a Shape that is an Ellipse2D.Double created from the given coordinates.
     * 
     * @param x1 the first x coordinate passed
     * @param y1 the first y coordinate passed
     * @param x2 the second x coordinate passed
     * @param y2 the second y coordinate passed
     * @return an Ellipse2D.Double created from the given coordinates
     */
    public Shape getShape(final int x1, final int y1, final int x2, final int y2) {
        int upperLeftX = x1;
        if (x2 < upperLeftX) {
            upperLeftX = x2;
        }
        int upperLeftY = y1;
        if (y2 < upperLeftY) {
            upperLeftY = y2;
        }
        final int width = Math.abs(x2 - x1);
        final int height = Math.abs(y2 - y1);
        return new Ellipse2D.Double(upperLeftX, upperLeftY, width, height);
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

