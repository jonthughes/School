/* 
 * TCSS 305 – Autumn 2014
 * Assignment 6 - tetris
 */

package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Observable;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A class with a custom JPanel for Tetris that is used for displaying the score.
 * 
 * @author Jonathan Hughes
 * @version 18 November 2014
 */
public final class TetrisScorePanel extends Observable implements PropertyChangeListener {
    
    /** The level which once reached, causes victory. */
    public static final int MY_VICTORY_LEVEL = 11;
    
    /** The number of lines to clear to move on to the next level. */
    public static final int LINES_PER_LEVEL = 5;
    
    /** The starting width of the score panel. */
    public static final int INITIAL_WIDTH = 100;
    
    /** The starting height of the score panel. */
    public static final int INITIAL_HEIGHT = 300;
    
    /** The starting color of the score panel. */
    public static final Color INITIAL_BACKGROUND_COLOR = Color.RED.darker();
    
    /** The starting color of the preview panel border. */
    public static final Color INITIAL_BORDER_COLOR = Color.BLACK;
    
    /** The font to use in the score panel. */
    public static final Font MURICA_FONT = new Font("Arial", Font.BOLD, 12);
    
    /** The color for text used in the score panel. */
    public static final Color MURICA_FONT_COLOR = Color.WHITE;
    
    /** The dimension for spacers used between text fields. */
    private static final Dimension SPACER_DIMENSION = new Dimension(96, 0);   
    
    /** The number of seconds in a millisecond. */
    private static final double SECONDS_PER_MS = .001;
    
    /** The number of points for a single line clear. */
    private static final int POINTS_PER_LINE_CLEAR = 100;
    
    /** The score panel. */
    private JPanel myScorePanel;
    
    /** The label for current level of the game. */
    private JLabel myLevelLabel;
    
    /** The label for lines cleared in the game. */
    private JLabel myLinesLabel;
    
    /** The label for current score of the game. */
    private JLabel myScoreLabel;
    
    /** The label for current time delay of the game. */
    private JLabel myTimerDelayLabel;
    
    /** The current level of the game. */
    private int myLevel;
    
    /** The number of lines cleared in the game. */
    private int myLinesCleared;
    
    /** The current score of the game. */
    private int myScore;
    
    /** Whether victory has occurred. */
    private boolean myVictoryEnabled;
    
    /** 
     * Constructs the panel.
     * 
     * @param thePreviewPanel the preview panel to display on the score panel
     * @param theFrame the JFrame that this panel is on
     * @param theScorePanel the JPanel associated with the tetris score panel 
     */
    TetrisScorePanel(final JPanel thePreviewPanel, 
                     final JFrame theFrame, 
                     final JPanel theScorePanel) {
        super();
        initializePanel(thePreviewPanel, theFrame, theScorePanel);       
    }
    
    /**
     * Initializes the TetrisScorePanel based upon starting conditions.
     * 
     * @param thePreviewPanel the preview panel to display on the score panel
     * @param theFrame the JFrame that this panel is on
     * @param theScorePanel the JPanel associated with the tetris score panel 
     */
    private void initializePanel(final JPanel thePreviewPanel, 
                                 final JFrame theFrame, 
                                 final JPanel theScorePanel) {
        //set initial settings
        myScorePanel = theScorePanel;
        myScorePanel.setPreferredSize(new Dimension(INITIAL_WIDTH, INITIAL_HEIGHT));
        myScorePanel.setBackground(INITIAL_BACKGROUND_COLOR);
        myScorePanel.setBorder(BorderFactory.createLineBorder(INITIAL_BORDER_COLOR));
        theFrame.add(myScorePanel, BorderLayout.WEST);
        myLevel = 1;
        myLinesCleared = 0;
        myScore = 0;
        myVictoryEnabled = false;
        
        //add preview panel label
        myScorePanel.add(createLabel("Reinforcements:"));
        
        //add preview panel
        myScorePanel.add(thePreviewPanel);
        
        //display game status text
        displayText();        
    }
    
    /**
     * Displays text on the score panel for level, score, etc.
     */
    private void displayText() {
        //level label
        myScorePanel.add(createLabel("Level:"));
        createSpacer();
        myLevelLabel = createLabel(String.valueOf(myLevel));
        myScorePanel.add(myLevelLabel);
        
        //create spacer border for in-between labels
        createSpacer();
        
        //lines label
        myScorePanel.add(createLabel("Lines Cleared:"));
        createSpacer();
        myLinesLabel = createLabel(String.valueOf(myLinesCleared));
        myScorePanel.add(myLinesLabel);
        
        //create spacer border for in-between labels
        createSpacer();
        
        //score label
        myScorePanel.add(createLabel("Score:"));
        createSpacer();
        myScoreLabel = createLabel(String.valueOf(myScore));
        myScorePanel.add(myScoreLabel);
        
        //create spacer border for in-between labels
        createSpacer();
        
        //timer delay label
        myScorePanel.add(createLabel("Delay:"));
        myTimerDelayLabel = createLabel("1.000s");
        myScorePanel.add(myTimerDelayLabel);
    }
    
    /**
     * Returns a label with the given String as the text.
     * 
     * @param theText the text to display on the label
     * @return a label with the given String as the text
     */
    private JLabel createLabel(final String theText) {
        final JLabel label = new JLabel(theText);
        label.setFont(MURICA_FONT);
        label.setForeground(MURICA_FONT_COLOR);
        return label;
    }
    
    /**
     * Adds a spacer to the panel based upon the static SPACER_DIMENSION.
     */
    private void createSpacer() {
        final JPanel spacer = new JPanel();
        spacer.setPreferredSize(SPACER_DIMENSION);
        spacer.setBackground(INITIAL_BACKGROUND_COLOR);
        myScorePanel.add(spacer);
    }

    /**
     * Updates the level or score label, when their property change event is fired.
     * 
     * {@inheritDoc}
     */
    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        if ("clearLines".equals(theEvent.getPropertyName())) {
            final int linesCleared = (int) theEvent.getNewValue();
            myScore = myScore + linesCleared * linesCleared * POINTS_PER_LINE_CLEAR;
            myScoreLabel.setText(String.valueOf(myScore));
            myLinesCleared = myLinesCleared + linesCleared;
            myLinesLabel.setText(String.valueOf(myLinesCleared));
            myLevel = myLinesCleared / LINES_PER_LEVEL + 1;
            myLevelLabel.setText(String.valueOf(myLevel));
            final BigDecimal secondsDelay = new BigDecimal(SECONDS_PER_MS 
                * (int) theEvent.getOldValue()).setScale(3, RoundingMode.HALF_UP);
            myTimerDelayLabel.setText(secondsDelay + "s");
        } 
        myScorePanel.repaint();
        if (!myVictoryEnabled && myLevel >= MY_VICTORY_LEVEL) {
            myVictoryEnabled = true;
            setChanged();
            notifyObservers(myScore);
        }
    }
}
