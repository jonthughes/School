/* 
 * TCSS 305 – Autumn 2014
 * Assignment 5 - powerpaint
 */

package gui;

import java.awt.Color;
import java.awt.Shape;
import java.awt.Stroke;

/**
 * A PPGraphic stores a Shape, Color, and Stroke to be used for drawing the graphic in 
 * PowerPaint.
 * 
 * @author Jonathan Hughes
 * @version 30 October 2014
 */
public class PPGraphic {
    
    /** The Shape of this graphic. */
    private final Shape myShape;
    
    /** The Color of this graphic. */
    private final Color myColor;
    
    /** The Stroke of this graphic. */
    private final Stroke myStroke;
    
    /**
     * Constructs a PPGraphic based upon the current shape, color, and stroke.
     * 
     * @param theShape the Shape that this graphic is to represent
     * @param theColor the color that this graphic is to have
     * @param theStroke the stroke that this graphic is to have
     */
    PPGraphic(final Shape theShape, final Color theColor, final Stroke theStroke) {
        myShape = theShape;
        myColor = theColor;
        myStroke = theStroke;
    }
    
    /**
     * Returns the PPGraphic's Shape.
     * 
     * @return the PPGraphic's Shape
     */
    public Shape getShape() {
        return myShape;
    }
    
    /**
     * Returns the PPGraphic's Color.
     * 
     * @return the PPGraphic's Color
     */
    public Color getColor() {
        return myColor;
    }
    
    /**
     * Returns the PPGraphic's Stroke.
     * 
     * @return the PPGraphic's Stroke
     */
    public Stroke getStroke() {
        return myStroke;
    }
}
