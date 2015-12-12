/* 
 * TCSS 305 – Autumn 2014
 * Assignment 5 - powerpaint
 */

package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * A custom JMenuBar for PowerPaint.
 * 
 * @author Jonathan Hughes
 * @version 30 October 2014
 */
@SuppressWarnings("serial")
public final class PPMenuBar extends JMenuBar implements PropertyChangeListener {

    /** The major tick spacing for the slider. */
    private static final int SLIDER_MAJOR_TICK_SPACING = 5;
    
    /** The minor tick spacing for the slider. */
    private static final int SLIDER_MINOR_TICK_SPACING = 1;
    
    /** The starting position for the slider. */
    private static final int SLIDER_STARTING_POSITION = 5;
    
    /** The max value for the slider. */
    private static final int SLIDER_MAX = 20;
    
    /** The undo button. */
    private JMenuItem myUndoItem;
    
    /** The redo button. */
    private JMenuItem myRedoItem;
    
    /** 
     * Constructs the PPMenuBar. 
     * 
     * @param theActions the actions that will be turned into tool buttons
     * @param theColorAction the color action to be used in the menu bar
     * @param theUndoAction the undo action to be used in the menu bar
     * @param theRedoAction the redo action to be used in the menu bar
     * @param theFrame the frame that this menu bar is on
     */
    PPMenuBar(final Action[] theActions, 
              final Action theColorAction, 
              final Action theUndoAction,
              final Action theRedoAction,
              final JFrame theFrame) {
        super();
        createFileMenu(theUndoAction, theRedoAction, theFrame);
        createOptionsMenu();
        createToolsMenu(theActions, theColorAction);
        createHelpMenu(theFrame);
    }
    
    /**
     * Creates a file menu for the menu bar, which includes an undo, redo, clear, and exit 
     * button.
     * 
     * @param theUndoAction the undo action to be used in the file menu
     * @param theRedoAction the redo action to be used in the file menu
     * @param theFrame the frame that this menu bar is on
     */
    private void createFileMenu(final Action theUndoAction, 
                                final Action theRedoAction, 
                                final JFrame theFrame) {
        final JMenu fileMenu = createMenu("File", KeyEvent.VK_F);
        
        //add undo button
        myUndoItem = new JMenuItem(theUndoAction);
        myUndoItem.setEnabled(false);
        fileMenu.add(myUndoItem);
        
        //add redo button
        myRedoItem = new JMenuItem(theRedoAction);
        myRedoItem.setEnabled(false);
        fileMenu.add(myRedoItem);
        
        //add clear button
        final JMenuItem clearItem = createMenuItem("Clear", KeyEvent.VK_C, fileMenu);
        clearItem.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
                firePropertyChange("file", null, "clear");
            }
        });
        fileMenu.addSeparator();
        
        //add exit button
        final JMenuItem exitItem = createMenuItem("Exit", KeyEvent.VK_X, fileMenu);
        exitItem.addActionListener(new ActionListener() {
            //close window when button is pressed
            public void actionPerformed(final ActionEvent theEvent) {
                theFrame.dispatchEvent(new WindowEvent(theFrame, WindowEvent.WINDOW_CLOSING));
            }
        });
    }
    

    /**
     * Create an options menu for the menu bar with a toggle grid check box and a slider to 
     * change thickness (line width).
     */
    private void createOptionsMenu() {
        final JMenu optionsMenu = createMenu("Options", KeyEvent.VK_O);
        
        //add grid check box
        final JMenuItem optionsGrid = new JCheckBoxMenuItem("Grid");
        optionsGrid.setMnemonic(KeyEvent.VK_G);
        optionsMenu.add(optionsGrid);
        optionsGrid.addActionListener(new ActionListener() {
            //fire a "grid" property change
            public void actionPerformed(final ActionEvent theEvent) {
                firePropertyChange("grid", null, null);
            }
        });
        
        //create and add thickness slider
        final JMenu optionsThickness = new JMenu("Thickness");
        optionsThickness.setMnemonic(KeyEvent.VK_T);
        optionsMenu.add(optionsThickness);
        final JSlider thicknessSlider = new JSlider(SwingConstants.HORIZONTAL, 0, SLIDER_MAX,
                                                    SLIDER_STARTING_POSITION);
        thicknessSlider.setMajorTickSpacing(SLIDER_MAJOR_TICK_SPACING);
        thicknessSlider.setMinorTickSpacing(SLIDER_MINOR_TICK_SPACING);
        thicknessSlider.setPaintLabels(true);
        thicknessSlider.setPaintTicks(true);
        thicknessSlider.addChangeListener(new ChangeListener() {
            //fire property change event with the new value when slider is moved
            public void stateChanged(final ChangeEvent theEvent) {
                final int value = thicknessSlider.getValue();
                if (value >= 0) {
                    thicknessSlider.setValue(value);
                    firePropertyChange("thickness", null, value);
                }
            }
        });
        optionsThickness.add(thicknessSlider);
    }
    
    /**
     * Creates a tools menu for the menu bar.
     * 
     * @param theActions the actions that will be turned into tool buttons
     * @param theColorAction the color action to be used in the tool menu
     */
    private void createToolsMenu(final Action[] theActions, 
                                 final Action theColorAction) {
        final JMenu toolsMenu = createMenu("Tools", KeyEvent.VK_T);
        
        //add the color chooser button
        toolsMenu.add(new JMenuItem(theColorAction));
        toolsMenu.addSeparator();
        
        //iterate through given actions and turn them all into buttons
        final ButtonGroup buttonGroup = new ButtonGroup();
        for (final Action action : theActions) {
            final JRadioButtonMenuItem button = new JRadioButtonMenuItem(action);
            toolsMenu.add(button);
            buttonGroup.add(button);
        }        
    }
    
    /**
     * Creates a help menu for the menu bar with an about button.
     * 
     * @param theFrame the frame to show the about message on
     */
    private void createHelpMenu(final JFrame theFrame) {
        final JMenu helpMenu = createMenu("Help", KeyEvent.VK_H);
        
        //add the about button
        final JMenuItem helpAbout = new JMenuItem("About...");
        helpAbout.setMnemonic(KeyEvent.VK_A);
        helpMenu.add(helpAbout);
        helpAbout.addActionListener(new ActionListener() {
            //display about dialog when about button is pressed
            public void actionPerformed(final ActionEvent theEvent) {
                JOptionPane.showMessageDialog(theFrame, "TCSS 305 PowerPaint\nAutumn 2014", 
                                              "About", JOptionPane.INFORMATION_MESSAGE);
            }
        });        
    }

    /**
     * Creates and returns a JMenu based upon the given name and mnemonic, while 
     * adding it to the menu bar.
     * 
     * @param theName the name of the JMenu
     * @param theMnemonic the mnemonic to be used by the JMenu
     * @return the JMenu that was created
     */
    private JMenu createMenu(final String theName, final int theMnemonic) {
        final JMenu menu = new JMenu(theName);
        menu.setMnemonic(theMnemonic);
        add(menu);
        return menu;        
    }
    
    /**
     * Creates and returns a JMenuItem based upon the given name and mnemonic, while 
     * adding it to the given menu.
     * 
     * @param theName the name of the JMenuItem
     * @param theMnemonic the mnemonic to be used by the JMenuItem
     * @param theMenu the menu that this JMenuItem will fall under
     * @return the JMenuItem that was created
     */
    private JMenuItem createMenuItem(final String theName, 
                                     final int theMnemonic, 
                                     final JMenu theMenu) {
        final JMenuItem menuItem = new JMenuItem(theName);
        menuItem.setMnemonic(theMnemonic);
        theMenu.add(menuItem);
        return menuItem; 
    }

    /**
     * Enables or disables the undo or redo buttons depending on the PropertyChangeEvent.
     * 
     * {@inheritDoc}
     */
    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        if ("undoEnabled".equals(theEvent.getPropertyName())) {
            myUndoItem.setEnabled((Boolean) theEvent.getNewValue());
        } 
        if ("redoEnabled".equals(theEvent.getPropertyName())) {
            myRedoItem.setEnabled((Boolean) theEvent.getNewValue());
        } 
    }
}
