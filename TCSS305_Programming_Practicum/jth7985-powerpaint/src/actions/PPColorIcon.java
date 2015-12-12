/* 
 * TCSS 305 – Autumn 2014
 * Assignment 5 - powerpaint
 */

package actions;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

/**
 * A custom icon for PowerPaint based upon the specified color.
 * Citation: Code from http://www.codebeach.com/2007/06/creating-dynamic-icons-in-java.html 
 * was used as the shell for this class.
 * 
 * @author Jonathan Hughes
 * @version 30 October 2014
 */
public class PPColorIcon implements Icon {

    /** The height of the icon. */
    private static final int ICON_HEIGHT = 14;  
    
    /** The width of the icon. */
    private static final int ICON_WIDTH = 14;  
    
    /** The Color of the icon. */
    private final Color myColor;  
    
    /**
     * Constructs a PPColorIcon with the specified Color.
     * 
     * @param theColor the specified Color to base the PPColorIcon upon
     */
    public PPColorIcon(final Color theColor)  {  
        myColor = theColor;  
    }  
    
    /**
     * Returns the icon's height.
     * 
     * {@inheritDoc}
     */
    @Override
    public int getIconHeight() {  
        return ICON_HEIGHT;  
    }  
    
    /**
     * Returns the icon's width.
     * 
     * {@inheritDoc}
     */
    @Override
    public int getIconWidth() {  
        return ICON_WIDTH;  
    }  
    
    /**
     * Paints the icon based upon the PPColorIcon's color with a black border.
     * 
     * {@inheritDoc}
     */
    @Override
    public void paintIcon(final Component theComponent, 
                          final Graphics theGraphics, 
                          final int theX, 
                          final int theY) {
        //draw colored icon
        theGraphics.setColor(myColor);  
        theGraphics.fillRect(theX, theY, ICON_WIDTH - 1, ICON_HEIGHT - 1);
        //draw black border
        theGraphics.setColor(Color.BLACK);  
        theGraphics.drawRect(theX, theY, ICON_WIDTH - 1, ICON_HEIGHT - 1);          
    } 
}
