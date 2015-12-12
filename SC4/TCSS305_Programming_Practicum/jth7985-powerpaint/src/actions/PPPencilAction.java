/* 
 * TCSS 305 – Autumn 2014
 * Assignment 5 - powerpaint
 */

package actions;

import java.awt.BasicStroke;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.GeneralPath;

/**
 * A custom JPanel for PowerPaint that is used for drawing.
 * 
 * @author Jonathan Hughes
 * @version 30 October 2014
 */
@SuppressWarnings("serial")
public class PPPencilAction extends AbstractPPToolAction {

    /** The tool name associated with this action. */
    private static final String TOOL_NAME = "Pencil";
    
    /** The mnemonic associated with this action. */
    private static final int TOOL_MNEMONIC = KeyEvent.VK_P;
    
    /** A temporary GeneralPath object for use with the pencil tool. */
    private GeneralPath myPath;
    
    /**
     * Construct this Action.
     */
    public PPPencilAction() {
        super(TOOL_NAME, TOOL_MNEMONIC);
    }

    /** 
     * Fires the property change "tool" with this tool as the new value and 
     * the property change "join" with the BasicStroke.JOIN_BEVEL property to 
     * change the join property when drawing with a pencil to even out pointy
     * edges.
     * 
     * {@inheritDoc} 
     */
    @Override
    public void actionPerformed(final ActionEvent theEvent) {
        firePropertyChange("tool", null, this);
        firePropertyChange("join", null, BasicStroke.JOIN_BEVEL);       
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Shape mousePressed(final int x1, final int y1, final int x2, final int y2) {
        myPath = new GeneralPath();
        myPath.setWindingRule(GeneralPath.WIND_EVEN_ODD);
        myPath.moveTo(x1, y1);
        return myPath;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Shape mouseDragged(final int x1, final int y1, final int x2, final int y2) {
        myPath.lineTo(x2, y2);
        return myPath;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Shape mouseReleased(final int x1, final int y1, final int x2, final int y2) {
        myPath.lineTo(x2, y2);
        return myPath;
    }
}