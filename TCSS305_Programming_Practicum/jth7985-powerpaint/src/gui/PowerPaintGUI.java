/* 
 * TCSS 305 – Autumn 2014
 * Assignment 5 - powerpaint
 */

package gui;

import actions.PPColorAction;
import actions.PPEllipseAction;
import actions.PPLineAction;
import actions.PPPencilAction;
import actions.PPRectangleAction;
import actions.PPRedoAction;
import actions.PPUndoAction;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Displays a drawing GUI called PowerPaint.
 * 
 * @author Jonathan Hughes
 * @version 30 October 2014
 */
public final class PowerPaintGUI {
    
    /** A Seahawk icon from an image file. */
    private static final File ICON_FILE = new File("icons/Seahawk.png");
    
    /** The frame for this application's GUI. */
    private final JFrame myFrame;
    
    /** 
     * Constructor to create GUI. 
     */
    PowerPaintGUI() {
        myFrame = new JFrame(); //create GUI frame
    }         
    
    /** 
     * Sets up and displays the GUI for this application. 
     */
    public void start() {
        //set initial frame settings
        myFrame.setTitle("TCSS 305 PowerPaint");
        final BufferedImage img = getImage(ICON_FILE);
        myFrame.setIconImage(img);
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
        createGUIComponents();

        //close out frame
        myFrame.pack();
        myFrame.setLocationRelativeTo(null);
        myFrame.setVisible(true);
    }
    
    /**
     * Sets up the GUI's components including custom actions, a menu bar, tool bar, 
     * and drawing panel.
     */
    private void createGUIComponents() {
        //create actions
        final Action colorAction = new PPColorAction();
        final Action undoAction = new PPUndoAction();
        final Action redoAction = new PPRedoAction();
        final Action[] toolActions = {new PPPencilAction(), 
                                      new PPLineAction(),
                                      new PPRectangleAction(),
                                      new PPEllipseAction()};
        
        //create GUI components
        final PPMenuBar menuBar = new PPMenuBar(toolActions, 
                                                colorAction, 
                                                undoAction, 
                                                redoAction, 
                                                myFrame);
        myFrame.setJMenuBar(menuBar);
        myFrame.add(new PPToolBar(toolActions, colorAction), BorderLayout.SOUTH);
        final PPDrawingPanel drawingPanel = new PPDrawingPanel(toolActions, colorAction);
        myFrame.add(drawingPanel, BorderLayout.CENTER);
        
        //tell components to listen to property changes from each other
        menuBar.addPropertyChangeListener(drawingPanel);
        drawingPanel.addPropertyChangeListener(menuBar);
        undoAction.addPropertyChangeListener(drawingPanel);
        redoAction.addPropertyChangeListener(drawingPanel);        
    }

    /**
     * Returns the BufferedImage contained in the file, if possible.  If not possible, 
     * a JOptionPane is shown that displays a warning message.
     * 
     * @param theFile the file which contains the BufferedImage
     * @return the BufferedImage version of the file
     */
    private BufferedImage getImage(final File theFile) {
        BufferedImage img = null;
        try { //read the image in the file
            img = ImageIO.read(theFile);
        } catch (final IOException e) { //show warning message if there is an exception
            JOptionPane.showMessageDialog(null, e.getMessage(), "Icon Load Error:", 
                                          JOptionPane.WARNING_MESSAGE);
        }
        return img;
    }
}
