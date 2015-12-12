/* 
 * TCSS 305 – Autumn 2014
 * Assignment 6 - tetris
 */

package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Displays a game GUI called Tetris.
 * 
 * @author Jonathan Hughes
 * @version 18 November 2014
 */
public final class TetrisGUI implements Observer {
    
    /** 
     * The file containing the victory image.  The victory image was used from 
     * http://i.telegraph.co.uk/multimedia/archive/01757/mission-accomplish_1757225i.jpg
     */
    public static final File VICTORY_FILE = new File("background/mission_accomplished.jpg");
    
    /** A Tetris icon from an image file. */
    private static final File ICON_FILE = new File("icons/small_flag3.png");
    
    /** The minimum size of the frame for the Tetris GUI (15 pixel block size). */
    private static final Dimension MINIMUM_FRAME_SIZE = new Dimension(267, 362);
    
    /** The frame for this application's GUI. */
    private final JFrame myFrame;
    
    /** The GUI's menu bar. */
    private TetrisMenuBar myMenuBar;
    
    /** The GUI's preview panel. */
    private TetrisPreviewPanel myPreviewPanel;   
    
    /** The GUI's score panel. */
    private JPanel myScorePanel;
    
    /** The GUI's board panel. */
    private TetrisBoardPanel myBoardPanel; 
    
    /** 
     * Constructor to create GUI. 
     */
    TetrisGUI() {
        myFrame = new JFrame(); //create GUI frame
    }         
    
    /** 
     * Sets up and displays the GUI for this application. 
     */
    public void start() {
        //set initial frame settings
        myFrame.setTitle("'Murica 'Tris");
        myFrame.setIconImage(getImage());
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.setMinimumSize(MINIMUM_FRAME_SIZE);      
        
        //create menu bar (do not reset with new game)
        final Icon icon = new ImageIcon(getImage());
        myMenuBar = new TetrisMenuBar(myFrame, icon);
        myMenuBar.addObserver(this);
        myMenuBar.addObserver(this);
        
        createGUIComponents();

        //close out frame
        myFrame.pack();
        myFrame.setLocationRelativeTo(null);
        myFrame.setVisible(true);
    }
    
    /**
     * Creates the GUI's resettable components.
     */
    private void createGUIComponents() {
        //create the preview panel
        myPreviewPanel = new TetrisPreviewPanel();   
        
        //create score panel
        myScorePanel = new JPanel();
        final TetrisScorePanel scorePanel = new TetrisScorePanel(myPreviewPanel, 
                                                                 myFrame, 
                                                                 myScorePanel);
        scorePanel.addObserver(this);
        
        //create board
        myBoardPanel = new TetrisBoardPanel(myFrame, myPreviewPanel, myMenuBar);
        myFrame.add(myBoardPanel, BorderLayout.CENTER);
        myBoardPanel.addPropertyChangeListener(scorePanel);
        myBoardPanel.addPropertyChangeListener(myMenuBar);
        myMenuBar.addObserver(myBoardPanel);
    }

    /**
     * Returns the BufferedImage contained in the file, if possible.  If not possible, 
     * a JOptionPane is shown that displays a warning message.
     * 
     * @return the BufferedImage version of the file
     */
    private BufferedImage getImage() {
        BufferedImage img = null;
        try { //read the image in the file
            img = ImageIO.read(ICON_FILE);
        } catch (final IOException e) { //show warning message if there is an exception
            JOptionPane.showMessageDialog(null, e.getMessage(), "Icon Load Error:", 
                                          JOptionPane.WARNING_MESSAGE);
        }
        return img;
    }

    /**
     * Receive updates from the menu bar and score panel.
     * 
     * @param theObservable the observable object
     * @param theObject the updated object
     */
    @Override
    public void update(final Observable theObservable, final Object theObject) {
        if (theObservable instanceof TetrisMenuBar && "newgame".equals(theObject)) {
            //remove components of old game (except menu bar)
            myFrame.remove(myBoardPanel);
            myFrame.remove(myScorePanel);
            myFrame.remove(myPreviewPanel);
                
            //restart game
            createGUIComponents();
            myFrame.pack();
        } 
        if (theObservable instanceof TetrisScorePanel) {
            //only happens when players have beat the final level
            showVictoryDialog(theObject);
        } 
    } 
    
    
    /**
     * Shows a victory image dialog with the end score.
     * 
     * @param theObject the updated object (supposed to be final score)
     */
    private void showVictoryDialog(final Object theObject) {
        try {                
            final ImageIcon icon = new ImageIcon(ImageIO.read(VICTORY_FILE));
            
            //remove old board, as play has stopped
            myFrame.remove(myBoardPanel);
            myFrame.remove(myScorePanel);
            myFrame.remove(myPreviewPanel);
            
            JOptionPane.showMessageDialog(myFrame, "Your score was " 
                                          + theObject.toString() + "! \n"
                                          + "Choose new game to play again!", 
                                          "Victory!", 
                                          JOptionPane.INFORMATION_MESSAGE, 
                                          icon);
        } catch (final IOException ex) {
            JOptionPane.showMessageDialog(myFrame, "File error", 
                                         "Invalid image file!", 
                                         JOptionPane.WARNING_MESSAGE);
        }
    }
}
