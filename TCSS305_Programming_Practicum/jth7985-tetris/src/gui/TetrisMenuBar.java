/* 
 * TCSS 305 – Autumn 2014
 * Assignment 6 - tetris
 */

package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;

import themes.TetrisBackgroundMap;
import themes.TetrisCamo;
import themes.TetrisCamoMap;

/**
 * A custom class with a JMenuBar for Tetris.
 * 
 * @author Jonathan Hughes
 * @version 18 November 2014
 */
public final class TetrisMenuBar extends Observable implements PropertyChangeListener {

    /** 
     * The characters associated with the key configuration. 
     * Key order: Left, right, down, hard drop, rotate.
     */
    public static final Character[] DEFAULT_KEY_CHARS = {'a', 'd', 's', 'w', 'e', 'p'};
    
    /** The single quote character. */
    public static final Character SINGLE_QUOTE = '\'';
    
    /** The String passed to end the game. */
    private static final String END_GAME_STRING = "endGame";
    
    /** The menu bar for the JFrame. */
    private final JMenuBar myMenuBar;
    
    /** The JFrame that this menu bar will be on. */
    private final JFrame myFrame;
    
    /** The icon for this menu bar. */
    private final Icon myIcon;
    
    /** The new game menu item. */
    private JMenuItem myNewGameItem;
    
    /** The end game menu item. */
    private JMenuItem myEndGameItem;
    
    /** 
     * The characters associated with the key configuration. 
     * Key order: Left, right, down, hard drop, rotate.
     */
    private Character[] myKeyChars;
    
    /** The current camo color palette as a TetrisCamo object. */
    private TetrisCamo myCurrentCamo;
    
    /** The current background image.*/
    private BufferedImage myBackgroundImage;
    
    /** The grid option checkbox. */
    private final JCheckBoxMenuItem myOptionsGrid = new JCheckBoxMenuItem("Grid");
    
    /** 
     * Constructs the TetrisMenuBar. 
     * 
     * @param theFrame the frame that this menu bar is on
     * @param theIcon the icon to use with this menu bar
     */
    TetrisMenuBar(final JFrame theFrame, 
                  final Icon theIcon) {
        super();
        myMenuBar = new JMenuBar();
        myFrame = theFrame;
        myFrame.setJMenuBar(myMenuBar);
        myIcon = theIcon;
        myKeyChars = DEFAULT_KEY_CHARS;
        
        createFileMenu();
        createOptionsMenu();
        createHelpMenu();        
    }
    
    /**
     * Creates a file menu for the menu bar, which includes a new game and exit 
     * button.
     */
    private void createFileMenu() {
        final JMenu fileMenu = createMenu("File", KeyEvent.VK_F);
        
        //set up new game button
        myNewGameItem = createMenuItem("New Game", KeyEvent.VK_N, fileMenu);
        myNewGameItem.setEnabled(false);
        myNewGameItem.addActionListener(new ActionListener() {
            //starts new game when button is pressed
            public void actionPerformed(final ActionEvent theEvent) {
                setChanged();
                notifyObservers("newgame");
                myNewGameItem.setEnabled(false);
                myEndGameItem.setEnabled(true);
            }
        });
        fileMenu.add(myNewGameItem);
        
        //set up game button
        myEndGameItem = createMenuItem("End Game", KeyEvent.VK_E, fileMenu);
        myEndGameItem.setEnabled(true);
        myEndGameItem.addActionListener(new ActionListener() {
            //starts new game when button is pressed
            public void actionPerformed(final ActionEvent theEvent) {
                setChanged();
                notifyObservers(END_GAME_STRING);
                myNewGameItem.setEnabled(true);
                myEndGameItem.setEnabled(false);
            }
        });
        fileMenu.add(myEndGameItem);
        
        fileMenu.addSeparator();
        
        //add exit button
        final JMenuItem exitItem = createMenuItem("Exit", KeyEvent.VK_X, fileMenu);
        exitItem.addActionListener(new ActionListener() {
            //close window when button is pressed
            public void actionPerformed(final ActionEvent theEvent) {
                myFrame.dispatchEvent(new WindowEvent(myFrame, WindowEvent.WINDOW_CLOSING));
            }
        });
    }
    
    /**
     * Gets the currently selected camo.
     * 
     * @return the currently selected TetrisCamo
     */
    public TetrisCamo getCamo() {
        //return defensive copy
        return new TetrisCamo(myCurrentCamo.getColor1(), 
                              myCurrentCamo.getColor2(), 
                              myCurrentCamo.getColor3());
    }
    
    /**
     * Gets the currently selected background image.
     * 
     * @return the currently selected TetrisCamo
     */
    public BufferedImage getBackground() {
        return myBackgroundImage;
    }
    
    /**
     * Create an options menu.
     */
    private void createOptionsMenu() {
        final JMenu optionsMenu = createMenu("Options", KeyEvent.VK_O);
        myMenuBar.add(optionsMenu);
        
        //add grid check box
        myOptionsGrid.setMnemonic(KeyEvent.VK_G);
        optionsMenu.add(myOptionsGrid);
        myOptionsGrid.addActionListener(new ActionListener() {
            //fire a "grid" property change
            public void actionPerformed(final ActionEvent theEvent) {
                setChanged();
                notifyObservers();
            }
        });
        
        optionsMenu.add(createBackgroundMenu());
        optionsMenu.add(createCamoMenu());
        
        //key config menu item
        final JMenuItem keyConfigItem = createMenuItem("Keys...", KeyEvent.VK_K, optionsMenu);
        optionsMenu.add(keyConfigItem);
        //key configuration window with buttons that display a dialog when pressed
        keyConfigItem.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
                final String[] buttons = getButtons();
                final int keyIndex = JOptionPane.showOptionDialog(null, 
                                                              "Configure your keys:",
                                                              "Key configuration:",
                                                              JOptionPane.OK_CANCEL_OPTION,
                                                              JOptionPane.QUESTION_MESSAGE,
                                                              null, buttons, null);
                if (keyIndex >= 0 && keyIndex <= myKeyChars.length) {
                    myKeyChars[keyIndex] = configureKey(keyIndex);
                }
                setChanged();
                notifyObservers();
            }
        });        
    }
    
    /**
     * Returns whether the grid option box has been clicked.
     * 
     * @return whether the grid option box has been clicked
     */
    public boolean isGridEnabled() {
        return myOptionsGrid.isSelected();
    }
    
    /**
     * Creates the background image radio menu.
     *
     * @return  the background image radio menu
     */
    private JMenu createBackgroundMenu() {
        final JMenu backgroundMenu = new JMenu("Background");
        backgroundMenu.setMnemonic(KeyEvent.VK_B);
        final Map<String, BufferedImage> backgroundMap = 
                                        new TetrisBackgroundMap(myFrame).getMap();
        final ButtonGroup buttonGroup1 = new ButtonGroup();
        //iterate through map and turn them all into radio buttons
        for (final Entry<String, BufferedImage> entry : backgroundMap.entrySet()) {
            final JRadioButton bgButton = new JRadioButton(entry.getKey());
            if ("Jungle".equals(entry.getKey())) {
                bgButton.setSelected(true);
                myBackgroundImage = entry.getValue();
            }
            backgroundMenu.add(bgButton);
            buttonGroup1.add(bgButton);
            bgButton.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent theEvent) {
                    myBackgroundImage = entry.getValue();
                    setChanged();
                    notifyObservers();
                }
            });
        }
        return backgroundMenu;
    }
    
    /**
     * Creates the camo pattern radio menu.
     *
     * @return  the camo pattern radio menu
     */
    private JMenu createCamoMenu() {
        //create camo menu
        final JMenu camoMenu = new JMenu("Camo Pattern");
        camoMenu.setMnemonic(KeyEvent.VK_C);
        final Map<String, TetrisCamo> camoMap = new TetrisCamoMap().getMap();
        final ButtonGroup buttonGroup2 = new ButtonGroup();
        //iterate through map and turn them all into radio buttons
        for (final Entry<String, TetrisCamo> entry : camoMap.entrySet()) {
            final JRadioButton camoButton = new JRadioButton(entry.getKey());
            if ("Woodland".equals(entry.getKey())) {
                camoButton.setSelected(true);
                myCurrentCamo = entry.getValue();
            }
            camoMenu.add(camoButton);
            buttonGroup2.add(camoButton);
            camoButton.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent theEvent) {
                    myCurrentCamo = entry.getValue();
                    setChanged();
                    notifyObservers();
                }
            });
        }
        return camoMenu;
    }
    
    /** 
     * Returns a String[] of button names for the key mappings.
     * 
     * @return a String[] of button names for the key mappings
     */
    private String[] getButtons() {
        int iterator = 0;
        final String[] buttons = {"Left: \'" + myKeyChars[iterator++] + SINGLE_QUOTE, 
                                  "Right: \'" + myKeyChars[iterator++] + SINGLE_QUOTE, 
                                  "Down: \'" + myKeyChars[iterator++] + SINGLE_QUOTE, 
                                  "Hard Down: \'" + myKeyChars[iterator++] + SINGLE_QUOTE, 
                                  "Rotate CW: \'" + myKeyChars[iterator++] + SINGLE_QUOTE,
                                  "Pause: \'" + myKeyChars[iterator++] + SINGLE_QUOTE};
        return buttons;
    }
    
    /**
     * Runs a simple key configure input dialog.
     * 
     * @param theKeyIndex the key index of the chosen key
     * @return the new key binding
     */
    private Character configureKey(final int theKeyIndex) {
        Character result = null;
        String newKey = null;
        do {
            newKey = JOptionPane.showInputDialog(null, "Press a new character for your key.", 
                                                 myKeyChars[theKeyIndex]);
            if (newKey == null) { //if null, dialog cancelled, keep old value
                result = myKeyChars[theKeyIndex];
            } else {
                //check for empty input
                if (newKey.isEmpty()) {
                    JOptionPane.showMessageDialog(myFrame, "Type a character, please.", 
                                                  "No Input", 
                                                  JOptionPane.WARNING_MESSAGE, 
                                                  myIcon);
                //check for too many characters (should be only 1)
                } else if (newKey.length() > 1) {
                    JOptionPane.showMessageDialog(myFrame, "Only type one character, please.", 
                                                  "Invalid Input", 
                                                  JOptionPane.WARNING_MESSAGE, 
                                                  myIcon);
                //check for duplicates with the other key chars
                } else {
                    boolean duplicate = false;
                    for (int i = 0; i < myKeyChars.length; i++) {
                        if (i != theKeyIndex && myKeyChars[i] == newKey.charAt(0)) {
                            duplicate = true;
                        }
                    }
                    if (duplicate) {
                        JOptionPane.showMessageDialog(myFrame, 
                                                      "Do not duplicate keys, please.", 
                                                      "Duplicate Input", 
                                                      JOptionPane.WARNING_MESSAGE, 
                                                      myIcon);
                    //all requirements met, use the character
                    } else {
                        result = newKey.charAt(0);
                    }
                }
            }
        } while (result == null); //have not chosen an option yet
        return result;
    }
    
    /**
     * Creates a help menu for the menu bar with an about button.
     */
    private void createHelpMenu() {
        final JMenu helpMenu = createMenu("Help", KeyEvent.VK_H);
        
        //add rules button
        final JMenuItem helpRules = new JMenuItem("Rules...");
        helpRules.setMnemonic(KeyEvent.VK_R);
        helpMenu.add(helpRules);
        final String rules = "Rules:\n"
            + "Tetris is played using seven types of tetrominoes or \"Tetriminos\".\n"
            + "You can rotate these pieces or move them left, right, or down.\n"
            + "After a certain amount of time, it will automatically drop a space.\n"
            + "Once they cannot go down any further, they are frozen in place.\n"
            + "This occurs when they land at the bottom or atop another piece.\n"
            + "If all blocks are filled on a row, that row is cleared and points\n"
            + "are received.  The game ends when a piece extends above the screen.\n\n"
            + "Scoring: \n"
            + "One line cleared = 100 points\n"
            + "Two lines cleared = 400 points\n"
            + "Three lines cleared = 900 points\n"
            + "Four lines cleared = 1600 points (aka a \"Tetris\")\n\n"
            + "Victory: \n"
            + "Clear 5 lines to advanced a level.\n"
            + "Warning - the game speeds up 5% for every line cleared!\n"
            + "Beat level 10 for ultimate victory!\n";
        helpRules.addActionListener(new ActionListener() {
            //display rules dialog when rules button is pressed
            public void actionPerformed(final ActionEvent theEvent) {
                JOptionPane.showMessageDialog(myFrame, rules, "Rules", 
                                              JOptionPane.INFORMATION_MESSAGE, 
                                              myIcon);
            }
        });      
        
        //add the about button
        final JMenuItem helpAbout = new JMenuItem("About...");
        helpAbout.setMnemonic(KeyEvent.VK_A);
        helpMenu.add(helpAbout);
        helpAbout.addActionListener(new ActionListener() {
            //display about dialog when about button is pressed
            public void actionPerformed(final ActionEvent theEvent) {
                JOptionPane.showMessageDialog(myFrame, 
                    "\'Murica \'Tris\n"
                    + "Author: Jonathan Hughes\n" 
                    + "Date: 26 November 2014\n\n"
                    + "Background Image Sources:\n"
                    + getSources(), 
                    "About", 
                    JOptionPane.INFORMATION_MESSAGE, 
                    myIcon);
            }
        });        
    }
    
    /**
     * Returns a String of the background image sources.
     * @return a String of the background image sources
     */
    private String getSources() {
        return "http://mariposajunglelodge.com/images/bg-page.jpg\n" 
               + "http://www.hdwallpapers3d.co/wp-content/uploads/2014/02/\n"
               + "     african_desert-www.hdwallpapers3d.co_.jpg\n"
               + "http://www.hdwallsource.com/img/2014/2/forest-background-\n"
               + "     18576-19046-hd-wallpapers.jpg\n"
               + "http://upload.wikimedia.org/wikipedia/commons/e/e5/University_\n"
               + "     of_Washington,_Tacoma.jpg\n" 
               + "http://img.wallpaperstock.net:81/blue-ocean-wallpapers_35232_\n"
               + "     1280x800.jpg\n"
               + "http://wallpapers5.com/getimage.php?img=21258913/Landscape\n"
               + "     /Nature/Mount-Rainier-From-Chinook-Pass-Washington.jpg\n"
               + "     &w=2560&h=1600&q=100&mode=fit&f=bw\n";
                                        
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
        myMenuBar.add(menu);
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
     * Returns the current key configuration as Characters.  
     * Key order: Left, right, down, hard drop, rotate.
     * @return the current key configuration as Characters
     */
    public Character[] getKeyChars() {
        return myKeyChars.clone();
    }
    
    /**
     * Enables new game button on property change event, if the game is over.
     * 
     * {@inheritDoc}
     */
    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        if (END_GAME_STRING.equals(theEvent.getPropertyName())) {
            myNewGameItem.setEnabled(true);
            myEndGameItem.setEnabled(false);
        }       
    }
}
