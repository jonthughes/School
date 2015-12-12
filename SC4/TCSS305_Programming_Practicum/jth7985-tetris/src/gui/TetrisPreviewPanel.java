/* 
 * TCSS 305 – Autumn 2014
 * Assignment 6 - tetris
 */

package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import model.AbstractPiece;
import model.Board;
import model.Piece;

/**
 * A custom JPanel for Tetris that is used for displaying the score.
 * 
 * @author Jonathan Hughes
 * @version 18 November 2014
 */
@SuppressWarnings("serial")
public final class TetrisPreviewPanel extends JPanel implements Observer {
    
    /** The starting width of the preview panel. */
    public static final int INITIAL_WIDTH = 100;
    
    /** The starting height of the preview panel. */
    public static final int INITIAL_HEIGHT = 100;
    
    /** The starting color of the preview panel. */
    public static final Color INITIAL_BACKGROUND_COLOR = Color.BLUE.darker();
    
    /** The starting color of the preview panel border. */
    public static final Color INITIAL_BORDER_COLOR = Color.BLACK;
    
    /** The size of the blocks when displayed. */
    public static final int BLOCK_SIZE = 20;
    
    /** The starting color of the stars. */
    public static final Color INITIAL_STAR_COLOR = Color.WHITE;
    
    /** The x-coordinates for drawing a star shape of size 20. */
    public static final int[] STAR_X = {9, 12, 19, 13, 16, 9, 2, 5, 0, 6, 9};
    
    /** The y-coordinates for drawing a star shape of size 20. */
    public static final int[] STAR_Y = {0, 6, 6, 11, 19, 14, 19, 11, 6, 6, 0};
    
    /** The compensation for the width of the blue rectangle. */
    private static final int WIDTH_COMPENSATOR = 3;
    
    /** The piece to show on the preview panel. */
    private Piece myPreviewPiece;     
    
    /** 
     * Constructs the panel.
     */
    TetrisPreviewPanel() {
        super();
        initializePanel();       
    }
    
    /**
     * Initializes the TetrisPreviewPanel based upon starting conditions.
     */
    private void initializePanel() {
        //set initial settings
        setPreferredSize(new Dimension(INITIAL_WIDTH, INITIAL_HEIGHT));
        setBackground(INITIAL_BORDER_COLOR);
    }
    
    /**
     * Paints the current graphics on the preview panel.
     * 
     * @param theGraphics The graphics context to use for painting.
     */
    public void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);
        final Graphics2D g2d = (Graphics2D) theGraphics;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
        
        //draw background
        theGraphics.setColor(INITIAL_BACKGROUND_COLOR);  
        theGraphics.fillRect(1, 1, getWidth() - WIDTH_COMPENSATOR, getHeight() - 2);
        
        //draw preview piece as stars
        if (myPreviewPiece != null) {
            drawStars(g2d);
        }      
    }
    
    /**
     * Draws stars representing the preview piece.
     * 
     * @param theG2D the graphics context to use for painting
     */
    private void drawStars(final Graphics2D theG2D) {
        theG2D.setColor(INITIAL_STAR_COLOR);  
        final int[][] coord = ((AbstractPiece) myPreviewPiece).getRotation();

        //find piece dimensions
        int maxX = 0;
        int maxY = 0;
        int minX = coord.length;
        int minY = coord.length;
        for (int block = 0; block < coord.length; block++) {
            final int blockX = coord[block][0];
            final int blockY = coord[block][1];
            if (blockX > maxX) {
                maxX = blockX;
            }
            if (blockY > maxY) {
                maxY = blockY;
            }
            if (blockX < minX) {
                minX = blockX;
            }
            if (blockY < minY) {
                minY = blockY;
            }
        }
        final int blockWidth = maxX - minX + 1;
        final int blockHeight = maxY - minY + 1;
        
        //find starting grid
        final int startX = (getWidth() - (blockWidth * BLOCK_SIZE)) / 2;
        final int startY = (getHeight() + ((blockHeight - 2) * BLOCK_SIZE)) / 2;
        
        //draw blocks
        for (int block = 0; block < coord.length; block++) {
            final int blockX = coord[block][0];
            final int blockY = coord[block][1];
            theG2D.fill(getStar(startX + (blockX - minX) * BLOCK_SIZE, 
                             startY - (blockY - minY) * BLOCK_SIZE));
            
        }    
    }

    /**
     * Returns a star shape starting at the given coordinates and vertices at the 
     * STAR_X and STAR_Y coordinates (relative to the starting coordinate).
     * 
     * @param theX the starting X coordinate of the star - the bottom left corner of the star
     * @param theY the starting Y coordinate of the star - the bottom left corner of the star
     * @return a star shape starting at the given coordinates and vertices at the 
     * STAR_X and STAR_Y coordinates (relative to the starting coordinate)
     */
    private GeneralPath getStar(final int theX, final int theY) {
        final GeneralPath starPath = new GeneralPath();
        starPath.setWindingRule(GeneralPath.WIND_EVEN_ODD);
        starPath.moveTo(theX + STAR_X[0], theY + STAR_Y[0]);
        for (int i = 1; i < STAR_X.length; i++) {
            starPath.lineTo(theX + STAR_X[i], theY + STAR_Y[i]);
        }                
        starPath.closePath();
        return starPath;
    }
    
    /**
     * Updates the preview piece, if it has changed.
     * 
     * {@inheritDoc}
     */
    @Override
    public void update(final Observable theObservable, final Object theObject) {
        if (theObservable instanceof Board) {
            final Piece previewPiece = ((Board) theObservable).getNextPiece();
            if (myPreviewPiece == null || !myPreviewPiece.equals(previewPiece)) {
                myPreviewPiece = previewPiece;
                repaint();
            }            
        }           
    }
}
