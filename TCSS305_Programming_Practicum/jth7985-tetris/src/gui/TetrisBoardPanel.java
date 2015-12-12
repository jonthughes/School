/* 
 * TCSS 305 – Autumn 2014
 * Assignment 6 - tetris
 */

package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import model.AbstractPiece;
import model.Block;
import model.Board;
import model.Piece;
import themes.TetrisCamo;

/**
 * A custom JPanel for Tetris that is used for displaying the board.
 * 
 * @author Jonathan Hughes
 * @version 18 November 2014
 */
@SuppressWarnings("serial")
public final class TetrisBoardPanel extends JPanel implements Observer, 
                                                              PropertyChangeListener {
    /** The starting width of the board. */
    private static final int INITIAL_WIDTH = 150;
    
    /** The starting height of the board. */
    private static final int INITIAL_HEIGHT = 300;
    
    /** The panel's starting total number of blocks wide. */
    private static final int BLOCKS_WIDE = 10;
    
    /** The panel's starting total number of blocks high. */
    private static final int BLOCKS_HIGH = 20;
    
    /** The starting block size. */
    private static final int INITIAL_BLOCK_SIZE = 15;
    
    /** The delay (in milliseconds) for the drop timer. */
    private static final int INITIAL_DROP_DELAY = 1000;
    
    /** The delay timer multiplier for each cleared line. */
    private static final double DELAY_MODIFIER = 0.95;
    
    /** The String passed to end the game. */
    private static final String END_GAME_STRING = "endGame";
    
    /** The width in pixels of the "GAME OVER!" String. */
    private static final int GAME_OVER_STRING_WIDTH = 70;
    
    /** The timer that controls when a piece automatically drops one space. */
    private Timer myDropTimer;
    
    /** The board associated with this panel. */
    private Board myBoard;
    
    /** The width of the board. */
    private int myWidth;
    
    /** The height of the board. */
    private int myHeight;
    
    /** The size of the blocks. */
    private int myBlockSize;
    
    /** The panel's current active piece. */
    private Piece myCurrentPiece;
    
    /** The panel's background image. */
    private BufferedImage myBackground;
    
    /** Whether the game has ended. */
    private Boolean myGameEnded;
    
    /** The number of rows that have frozen blocks. */
    private int myFrozenBlockSize;
    
    /** Whether the board is paused. */
    private Boolean myPaused;
    
    /** My camo color palette theme as a TetrisCamo. */
    private TetrisCamo myCamo;
    
    /** The current delay (in milliseconds) for the drop timer. */
    private int myDropDelay;
    
    /** 
     * The characters associated with the key configuration. 
     * Key order: Left, right, down, hard drop, rotate.
     */
    private Character[] myKeyChars;
    
    /** Whether the grid has been enabled. */
    private boolean myGridEnabled;
    
    /** 
     * Constructs the panel.
     * 
     * @param theFrame the frame that this board panel is on
     * @param thePreviewPanel the preview panel that is listening to preview piece changes
     * @param theMenuBar the menu bar for the GUI
     */
    TetrisBoardPanel(final JFrame theFrame, 
                     final TetrisPreviewPanel thePreviewPanel, 
                     final TetrisMenuBar theMenuBar) {
        super();
        initializePanel(theFrame, thePreviewPanel, theMenuBar);     
    }

    /**
     * Initializes the TetrisBoardPanel based upon starting conditions.
     * 
     * @param theFrame the frame that this board panel is on
     * @param thePreviewPanel the preview panel that is listening to preview piece changes
     * @param theMenuBar the menu bar for the GUI
     */
    private void initializePanel(final JFrame theFrame, 
                                 final TetrisPreviewPanel thePreviewPanel, 
                                 final TetrisMenuBar theMenuBar) {
        //set initial settings
        setPreferredSize(new Dimension(INITIAL_WIDTH, INITIAL_HEIGHT));       
        
        //listen to key presses
        final TetrisKeyListener listener = new TetrisKeyListener();
        theFrame.addKeyListener(listener);
        
        //set local fields
        myWidth = INITIAL_WIDTH;
        myHeight = INITIAL_HEIGHT;
        myBlockSize = INITIAL_BLOCK_SIZE;
        myGameEnded = false;
        myKeyChars = theMenuBar.getKeyChars();
        myPaused = false;
        myCamo = theMenuBar.getCamo();
        myBackground = theMenuBar.getBackground();
        myGridEnabled = theMenuBar.isGridEnabled();
        
        //create board
        myBoard = new Board(BLOCKS_WIDE, BLOCKS_HIGH);
        
        //observe the board
        myBoard.addObserver(this);
        myBoard.addObserver(thePreviewPanel);
        
        //create initial pieces
        myCurrentPiece = (AbstractPiece) myBoard.getCurrentPiece();
        
        //add timer
        myDropDelay = INITIAL_DROP_DELAY;
        myDropTimer = new Timer(myDropDelay, null);
        myDropTimer.start();
        myDropTimer.addActionListener(new DropListener());
    }

    /**
     * Paints the current graphics on the board panel.
     * 
     * @param theGraphics The graphics context to use for painting.
     */
    public void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);
        final Graphics2D g2d = (Graphics2D) theGraphics;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (myGameEnded) { //if game is over, change board to red, display game over text
            theGraphics.setColor(Color.RED.darker());
            theGraphics.fillRect(0, 0, getWidth(), getHeight());
            theGraphics.setFont(TetrisScorePanel.MURICA_FONT);
            theGraphics.setColor(TetrisScorePanel.MURICA_FONT_COLOR);
            theGraphics.drawString("GAME OVER!", (getWidth() - GAME_OVER_STRING_WIDTH) / 2, 
                                   getHeight() / 2);
        } else { //if game has not ended
            //draw background
            theGraphics.drawImage(myBackground, 0, 0, null);
            
            //if window size changed, change block size to fit new panel size
            final int panelWidth = getWidth();
            final int panelHeight = getHeight();
            if (myWidth != panelWidth || myHeight != panelHeight) {
                if (panelWidth * 2 < panelHeight) {
                    myBlockSize = panelWidth / BLOCKS_WIDE;
                } else {
                    myBlockSize = panelHeight / BLOCKS_HIGH;
                }
                myWidth = panelWidth;
                myHeight = panelHeight;           
            }
    
            //draw the current piece
            drawPiece(theGraphics);
    
            //draw the frozen blocks
            drawFrozenBlocks(theGraphics);
            
            if (myGridEnabled) {
                drawGrid(g2d);
            }
            
            if (myBoard.isGameOver()) { //game end
                endGame();
            }
            
            //draw border
            theGraphics.setColor(Color.BLACK);  
            theGraphics.drawRect(-1, 0, BLOCKS_WIDE * myBlockSize + 1, 
                                 BLOCKS_HIGH * myBlockSize);
        }    
    }
    
    /**
     * Ends the game.  Displays a dialog and changes screen to red.
     */
    private void endGame() {
        myGameEnded = true;
        myDropTimer.stop();
        firePropertyChange(END_GAME_STRING, false, true);
    }

    /**
     * Draws the frozen blocks on the board.
     * 
     * @param theGraphics the graphics context to draw the frozen blocks on
     */
    private void drawFrozenBlocks(final Graphics theGraphics) {
        final List<Block[]> frozenBlocks = myBoard.getFrozenBlocks();
        final int newFrozenBlockSize = frozenBlocks.size();
        if (myFrozenBlockSize > newFrozenBlockSize) {
            final int blocksCleared = myFrozenBlockSize - newFrozenBlockSize;
            //speed up timer 5% for every line cleared
            myDropDelay = (int) (myDropDelay * Math.pow(DELAY_MODIFIER, blocksCleared));
            myDropTimer.setDelay(myDropDelay);
            firePropertyChange("clearLines", myDropDelay, blocksCleared);
        }
        myFrozenBlockSize = newFrozenBlockSize;
        for (int row = 0; row < frozenBlocks.size(); row++) {
            final Block[] blockRow = frozenBlocks.get(row);
            for (int block = 0; block < blockRow.length; block++) {
                if (!blockRow[block].equals(Block.EMPTY)) {
                    theGraphics.setColor(getColor(blockRow[block]));
                    theGraphics.fillRect(block * myBlockSize, 
                                         ((BLOCKS_HIGH - 1) - row) * myBlockSize
                                         , myBlockSize, myBlockSize);
                }
            }
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
        
        //draw vertical lines
        for (int i = 1; i <= BLOCKS_WIDE; i++) {
            theG2D.draw(new Line2D.Double(i * myBlockSize, 0, 
                                          i * myBlockSize, BLOCKS_HIGH * myBlockSize));
        }
        
        //draw horizontal lines
        for (int i = 1; i <= BLOCKS_HIGH; i++) {
            theG2D.draw(new Line2D.Double(0, i * myBlockSize, 
                                          BLOCKS_WIDE * myBlockSize, i * myBlockSize));
        }
    }

    /**
     * Draws the current piece on the board.
     * 
     * @param theGraphics the drawing context to draw the piece on
     */
    private void drawPiece(final Graphics theGraphics) {
        theGraphics.setColor(getColor(((AbstractPiece) myCurrentPiece).getBlock()));  
        final int[][] coord = ((AbstractPiece) myCurrentPiece).getBoardCoordinates();

        // Construct the string by walking through the piece top to bottom, left to right.
        for (int block = 0; block < coord.length; block++) {
            theGraphics.fillRect((coord[block][0]) * myBlockSize, 
                                 ((BLOCKS_HIGH - 1) - coord[block][1]) * myBlockSize, 
                                 myBlockSize, myBlockSize);
        }    
    }
    
    /**
     * Returns the color for a given block.  
     * The TetrisCamo object holds three colors for the camo palette.
     * Color 1 goes on I, J, and L blocks.
     * Color 2 goes on S, Z and T blocks.
     * Color 3 goes on O blocks.
     * 
     * @param theBlock the block to get the color for
     * @return the color that the block is supposed to be
     */
    private Color getColor(final Block theBlock) {
        Color result = myCamo.getColor1();
        if (theBlock.equals(Block.S) || theBlock.equals(Block.Z) || theBlock.equals(Block.T)) {
            result = myCamo.getColor2();
        } else if (theBlock.equals(Block.O)) {
            result = myCamo.getColor3();
        }
        return result;
    }
    
    /**
     * Receive updates from the board.
     * 
     * @param theObservable the observable object
     * @param theObject the updated object
     */
    @Override
    public void update(final Observable theObservable, final Object theObject) {
        if (theObservable instanceof Board) {
            myBoard = (Board) theObservable;
            final Piece currentPiece = ((Board) theObservable).getCurrentPiece();
            if (myCurrentPiece == null || !myCurrentPiece.equals(currentPiece)) {
                myCurrentPiece = currentPiece;
            } 
            final int newFrozenBlockSize = myBoard.getFrozenBlocks().size();
            //if block size grew, update counter
            if (myFrozenBlockSize < newFrozenBlockSize) {
                myFrozenBlockSize = newFrozenBlockSize;
            }
        }
        if (theObservable instanceof TetrisMenuBar) {
            myKeyChars = ((TetrisMenuBar) theObservable).getKeyChars();
            myCamo = ((TetrisMenuBar) theObservable).getCamo();
            myBackground = ((TetrisMenuBar) theObservable).getBackground();
            myGridEnabled = ((TetrisMenuBar) theObservable).isGridEnabled();
            if (END_GAME_STRING.equals(theObject)) {
                endGame();
            }
        }
        if (theObservable instanceof TetrisScorePanel) {
            //only happens when players have beat the final level
            firePropertyChange(END_GAME_STRING, false, true);          
        }
        repaint();
    } 
    
    /**
     * Receives property change events from the menu bar to change the key configuration.
     * 
     * {@inheritDoc}
     */
    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        System.out.println(theEvent.getPropertyName());
        if ("keyconfig".equals(theEvent.getPropertyName())) {
            myKeyChars = ((Character[]) theEvent.getNewValue()).clone();
        }
    }
    
    /**
     * Listens for key presses.
     */
    private class TetrisKeyListener extends KeyAdapter {

        /**
         * Handles a key being typed. Key order: Left, right, down, hard drop, rotate, pause.
         * 
         * @param theEvent The KeyEvent generated by the key.
         */
        public void keyTyped(final KeyEvent theEvent) {
            //if paused and pause is hit again, unpause game
            if (myPaused && myKeyChars[2 + 2 + 1].equals(theEvent.getKeyChar())) {
                myDropTimer.start();
                myPaused = false;
            //if not paused check for key press
            } else if (!myPaused) {
                checkKeys(theEvent);
            }
        }
        
        /** 
         * Checks for the pressed key and performs the appropriate action.
         * 
         * @param theEvent The KeyEvent generated by the key.
         */
        private void checkKeys(final KeyEvent theEvent) {
            if (myKeyChars[0].equals(theEvent.getKeyChar())) {
                myBoard.moveLeft();
            } else if (myKeyChars[1].equals(theEvent.getKeyChar())) {
                myBoard.moveRight();
            } else if (myKeyChars[2].equals(theEvent.getKeyChar())) {
                myBoard.moveDown();
            } else if (myKeyChars[2 + 1].equals(theEvent.getKeyChar())) {
                myBoard.hardDrop();
            } else if (myKeyChars[2 + 2].equals(theEvent.getKeyChar())) {
                myBoard.rotateCW();
            } else if (myKeyChars[2 + 2 + 1].equals(theEvent.getKeyChar())) {
                myDropTimer.stop();
                myPaused = true;
            }
        }
    }
    
    /**
     * An action listener for listening to the drop timer, which drops the piece 
     * down one tile for every iteration of the timer.
     */
    private class DropListener implements ActionListener {
        /**
         * Drops the piece down one tile for every iteration of the timer.
         * 
         * @param theEvent the event that is triggered by the timer 
         */
        public void actionPerformed(final ActionEvent theEvent) {
            myBoard.step();
        }
    }    
}
