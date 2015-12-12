/* 
 * TCSS 305 – Autumn 2014
 * Assignment 5 - powerpaint
 */

package gui;

import actions.AbstractPPToolAction;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayDeque;
import java.util.Deque;

import javax.swing.Action;
import javax.swing.JPanel;

/**
 * A custom JPanel for PowerPaint that is used for drawing.
 * 
 * @author Jonathan Hughes
 * @version 30 October 2014
 */
@SuppressWarnings("serial")
public final class PPDrawingPanel extends JPanel implements PropertyChangeListener {
    
    /** The starting width of the drawing panel. */
    public static final int INITIAL_WIDTH = 550;
    
    /** The starting height of the drawing panel. */
    public static final int INITIAL_HEIGHT = 300;
    
    /** The starting color of the drawing panel. */
    public static final Color INITIAL_BACKGROUND_COLOR = Color.WHITE;
    
    /** The starting thickness for the drawing panel's Stroke. */
    public static final int INITIAL_THICKNESS = 5;
    
    /** The starting cap property for the drawing panel's Stroke. */
    public static final int INITIAL_CAP = BasicStroke.CAP_BUTT;
    
    /** The starting join property for the drawing panel's Stroke. */
    public static final int INITIAL_JOIN = BasicStroke.JOIN_BEVEL;
    
    /** The starting stroke for the drawing panel's tools. */
    public static final Color INITIAL_COLOR = Color.BLACK;
    
    /** The spacing for the displayed grid, if enabled. */
    public static final int GRID_SPACING = 10;
    
    /** The active Action whose getShape method will be used. */
    private Action myActiveAction;

    /** An array of saved PPGraphics. */
    private Deque<PPGraphic> myPPGraphics;
    
    /** An array of saved PPGraphics that were removed. */
    private Deque<PPGraphic> myUndonePPGraphics;
    
    /** The currently displayed shape. */
    private Shape myShape;
    
    /** The current stroke (line thickness). */
    private Stroke myStroke;
    
    /** The Stroke's thickness. */
    private int myThickness;
    
    /** The Stroke's cap property. */
    private int myCap;
    
    /** The Stroke's join property. */
    private int myJoin;
    
    /** The current color. */
    private Color myColor;
    
    /** Whether the grid is enabled or not. */
    private boolean myGridEnabled;
    
    /** Whether in edit process or not. */
    private boolean myEditInProgress;
    
    /** Whether the panel was just cleared or not. */
    private boolean myJustCleared;
    
    /** Whether the undo button is enabled or not. */
    private boolean myUndoEnabled;
    
    /** Whether the redo button is enabled or not. */
    private boolean myRedoEnabled;
    
    /** 
     * Constructs the PPDrawingPanel.
     * 
     * @param theActions that the PPDrawingPanel can use as tools
     * @param theColorAction the color action that sets drawn shape color
     */
    PPDrawingPanel(final Action[] theActions, 
                   final Action theColorAction) {
        super();
        initializePPDrawingPanel(theActions, theColorAction);       
    }
    
    /**
     * Initializes the PPDrawingPanel based upon starting conditions.
     * 
     * @param theActions that the PPDrawingPanel can use as tools
     * @param theColorAction the color action that sets drawn shape color
     */
    private void initializePPDrawingPanel(final Action[] theActions, 
                                      final Action theColorAction) {
        //set initial settings
        setPreferredSize(new Dimension(INITIAL_WIDTH, INITIAL_HEIGHT));
        setBackground(INITIAL_BACKGROUND_COLOR);
        myThickness = INITIAL_THICKNESS;
        myCap = INITIAL_CAP;
        myJoin = INITIAL_JOIN;
        myStroke = new BasicStroke(myThickness, myCap, myJoin);
        myColor = INITIAL_COLOR;
        myGridEnabled = false;
        myActiveAction = (AbstractPPToolAction) theActions[0]; //start with first action
        myPPGraphics = new ArrayDeque<>();
        myUndonePPGraphics = new ArrayDeque<>();
        myEditInProgress = false;
        
        //listen to mouse buttons and movement
        final PPMouseListener listener = new PPMouseListener();
        addMouseListener(listener);
        addMouseMotionListener(listener);
        
        //listen to all the actions to see if they get activated
        for (final Action current : theActions) {
            current.addPropertyChangeListener(this);
        }
        theColorAction.addPropertyChangeListener(this);
    }
    
    /**
     * Paints the current graphics on the drawing panel.
     * 
     * @param theGraphics The graphics context to use for painting.
     */
    public void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);
        final Graphics2D g2d = (Graphics2D) theGraphics;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
        
        //iterate through saved graphics and draw them one by one
        for (final PPGraphic graphic : myPPGraphics) {
            g2d.setPaint(graphic.getColor());
            g2d.setStroke(graphic.getStroke());
            g2d.draw(graphic.getShape());
        }
        
        //draw the current shape (if editing is in progress), also do not draw if width is 0)
        if (myEditInProgress && ((BasicStroke) myStroke).getLineWidth() > 0) {
            g2d.setPaint(myColor);
            g2d.setStroke(myStroke);
            if (myShape != null) {
                g2d.draw(myShape);
            }
        }
        
        //draw a grid if enabled
        if (myGridEnabled) {
            drawGrid(g2d);
        }
    }
    
    /**
     * Draws a gray grid of 1 pixel thickness with the specified grid spacing.
     * 
     * @param theG2D the Graphics2D object to draw the grid on
     */
    private void drawGrid(final Graphics2D theG2D) {
        theG2D.setPaint(Color.GRAY);
        theG2D.setStroke(new BasicStroke(1));
        final int width = getWidth();
        final int height = getHeight();
        
        //draw vertical lines
        for (int i = 1; i <= (width / GRID_SPACING); i++) {
            theG2D.draw(new Line2D.Double(i * GRID_SPACING, 0, i * GRID_SPACING, height));
        }
        
        //draw horizontal lines
        for (int i = 1; i <= (height / GRID_SPACING); i++) {
            theG2D.draw(new Line2D.Double(0, i * GRID_SPACING, width, i * GRID_SPACING));
        }
    }

    /**
     * Changes fields based upon the PropertyChangeEvent.
     * 
     * When the tool is changed, the myActiveTool is changed to match the tool.  
     * When the color is changed, myColor is changed to match the color.
     * When the thickness is changed, the stroke is changed to the new setting.
     * When the join is changed, the stroke is changed to the new setting.
     * When the grid is changed, the grid is toggled on or off.
     * When a file menu button is pressed, the corresponding action is taken.
     * 
     * {@inheritDoc}
     */
    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        if ("tool".equals(theEvent.getPropertyName())) {
            myActiveAction = (AbstractPPToolAction) theEvent.getNewValue();
        } else if ("color".equals(theEvent.getPropertyName())) {
            myColor = (Color) theEvent.getNewValue();
        } else if ("thickness".equals(theEvent.getPropertyName())) {
            myThickness = (int) theEvent.getNewValue();
            myStroke = new BasicStroke(myThickness, myCap, myJoin);
        } else if ("join".equals(theEvent.getPropertyName())) {
            myJoin = (int) theEvent.getNewValue();
            myStroke = new BasicStroke(myThickness, myCap, myJoin);
        } else if ("grid".equals(theEvent.getPropertyName())) {
            if (myGridEnabled) {
                myGridEnabled = false;
            } else {
                myGridEnabled = true;
            }
        } else if ("file".equals(theEvent.getPropertyName())) {
            filePropertyChange(theEvent);
        }
        repaint();
    }
    
    /**
     * Handles property change events when file menu buttons are pressed.
     * Clears panel if clear is passed, performs undo if undo is passed, 
     * performs redo if redo is passed.
     * 
     * @param theEvent the PropertyChangeEvent to process
     */
    private void filePropertyChange(final PropertyChangeEvent theEvent) {
        if ("clear".equals(theEvent.getNewValue())) {
            clear();
        } else if ("undo".equals(theEvent.getNewValue()) && myUndoEnabled) {
            undo();
        } else if ("redo".equals(theEvent.getNewValue()) && myRedoEnabled) {
            redo();            
        }
    }
    
    /**
     * Clears the panel of PPGraphics.  It also saves the graphics in the 
     * "undone graphics" array in case the user changes their mind.
     */
    private void clear() {
        if (myPPGraphics.size() > 0) {
            myUndonePPGraphics = new ArrayDeque<>();
            while (myPPGraphics.size() > 0) {
                myUndonePPGraphics.add(myPPGraphics.removeLast());
                if (myPPGraphics.size() == 0) {
                    changeMyUndoState(true);
                    changeMyRedoState(false);
                }
            }
            myJustCleared = true;    
        }
    }

    /**
     * Removes the last drawn PPGraphic, or restores all PPGraphics if clear was pressed.
     */
    private void undo() {
        //if it was just cleared, all PPGraphics are restored
        if (myJustCleared && myUndonePPGraphics.size() > 0) {
            while (myUndonePPGraphics.size() > 0) {
                myPPGraphics.add(myUndonePPGraphics.removeLast());
                changeMyRedoState(true);
                myJustCleared = false;
            }
            
        //else remove the last graphic and add it to the undone graphic array
        } else if (myPPGraphics.size() > 0) {
            myUndonePPGraphics.add(myPPGraphics.removeLast());
            changeMyRedoState(true);
            if (myPPGraphics.size() == 0) {
                changeMyUndoState(false);
            }           
        } 
    }
    
    /**
     * Brings back the last undone PPGraphic, or clears all PPGraphics if clear was undone.
     */
    private void redo() {
        //if it was just cleared and undo was hit then redo, clear it again
        if (myJustCleared) {
            clear();
            
        //else bring back the last undone PPGraphic
        } else if (myUndonePPGraphics.size() > 0) {
            myPPGraphics.add(myUndonePPGraphics.removeLast());
            changeMyUndoState(true);
            if (myUndonePPGraphics.size() == 0) {
                changeMyRedoState(false);
            }
        }     
    }
    
    /**
     * Changes the undo setting to enabled if true is passed or disabled if false is passed.
     * This method changes the local boolean and fires a property change of name "undoEnabled" 
     * with the new value being the boolean that was passed.
     * 
     * @param theBoolean whether undo is enabled or not
     */
    private void changeMyUndoState(final boolean theBoolean) {
        firePropertyChange("undoEnabled", myUndoEnabled, theBoolean);
        myUndoEnabled = theBoolean;
    }
    
    /**
     * Changes the redo setting to enabled if true is passed or disabled if false is passed.
     * This method changes the local boolean and fires a property change of name "redoEnabled" 
     * with the new value being the boolean that was passed.
     * 
     * @param theBoolean whether redo is enabled or not
     */
    private void changeMyRedoState(final boolean theBoolean) {
        firePropertyChange("redoEnabled", myRedoEnabled, theBoolean);
        myRedoEnabled = theBoolean;
    }

    /**
     * A custom MouseListener for PowerPaint that is used to support drawing on the 
     * PPDrawingPanel.
     * 
     * @author Jonathan Hughes
     * @version 30 October 2014
     */
    private class PPMouseListener extends MouseAdapter implements MouseMotionListener {
        
        /** The first x coordinate. */
        private int myX1;
        
        /** The first y coordinate. */
        private int myY1;
        
        /** The second x coordinate. */
        private int myX2;
        
        /** The second y coordinate. */
        private int myY2;
         
        /**
         * Gets the current shape to draw when the mouse is pressed and repaints the panel.
         * 
         * {@inheritDoc}
         */
        @Override
        public void mousePressed(final MouseEvent theEvent) {
            myEditInProgress = true;
            myX1 = theEvent.getX();
            myY1 = theEvent.getY();
            myX2 = myX1;
            myY2 = myY1;
            myShape = ((AbstractPPToolAction) myActiveAction).mousePressed(myX1, myY1, 
                                                                       myX2, myY2);
            repaint();
        }
        
        /**
         * Gets the current shape to draw when the mouse is dragged and repaints the panel.
         * 
         * {@inheritDoc}
         */
        @Override
        public void mouseDragged(final MouseEvent theEvent) {
            myX2 = theEvent.getX();
            myY2 = theEvent.getY();
            myShape = ((AbstractPPToolAction) myActiveAction).mouseDragged(myX1, myY1, 
                                                                       myX2, myY2);
            repaint();
        }
        
        /**
         * Gets the current shape to draw when the mouse is released, saves it to an array 
         * with its current color and stroke settings, enables the undo button, disables the 
         * redo button, and repaints the panel.
         * 
         * {@inheritDoc}
         */
        @Override
        public void mouseReleased(final MouseEvent theEvent) {
            myEditInProgress = false;
            myX2 = theEvent.getX();
            myY2 = theEvent.getY();
            myShape = ((AbstractPPToolAction) myActiveAction).mouseReleased(myX1, myY1, 
                                                                        myX2, myY2);
            final PPGraphic saveGraphic = new PPGraphic(myShape, myColor, myStroke);
            if (((BasicStroke) myStroke).getLineWidth() > 0) { //do not draw if width is 0
                myPPGraphics.add(saveGraphic); //save to array
            }
            changeMyUndoState(true);
            changeMyRedoState(false);
            myJustCleared = false;
            repaint();
        }      
    }
}
