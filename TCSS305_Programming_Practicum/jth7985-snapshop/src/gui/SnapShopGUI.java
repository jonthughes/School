/* 
 * TCSS 305 – Autumn 2014
 * Assignment 4 - snapshot
 */

package gui;

import filters.EdgeDetectFilter;
import filters.EdgeHighlightFilter;
import filters.Filter;
import filters.FlipHorizontalFilter;
import filters.FlipVerticalFilter;
import filters.GrayscaleFilter;
import filters.SharpenFilter;
import filters.SoftenFilter;
import image.Pixel;
import image.PixelImage;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Displays an image filtering GUI.
 * 
 * @author Jonathan Hughes
 * @version 23 October 2014
 */
public final class SnapShopGUI {
    
    /** The "Filter" suffix. */
    private static final String FILTER_SUFFIX = "Filter";
    
    /** A JFileChooser for opening files. */
    private static final JFileChooser FILE_CHOOSER = new JFileChooser();
    
    /** The frame for this application's GUI. */
    private final JFrame myFrame;
    
    /** Stores displayed image. */
    private PixelImage myImage;
    
    /** Stores backup images. */
    private List<Raster> myBackupImageList;
    
    /** Stores the label that will hold the icon version of the image. */
    private final JLabel myLabelWithIcon = new JLabel();
    
    //panels
    /** The top panel. */
    private JPanel myTopPanel;
    
    /** The center panel. */
    private JPanel myCenterPanel;
    
    /** The bottom panel. */
    private JPanel myBottomPanel;
    
    //buttons
    /** A list of filter JButtons. */
    private List<JButton> myTogglableButtons;
    
    /** The undo filter button. */
    private JButton myUndoButton;
    
    /** 
     * Constructor to create GUI. 
     */
    SnapShopGUI() {
        myFrame = new JFrame(); //create GUI frame
    }
    
    /** 
     * Sets up and displays the GUI for this application. 
     */
    public void start() {
        
        //set initial frame settings
        myFrame.setTitle("TCSS 305 SnapShop");
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.setLocationRelativeTo(null);
        
        //create GUI components
        createComponents();

        //close out frame
        myFrame.pack();
        myFrame.setLocationRelativeTo(null);
        myFrame.setVisible(true);
    }
    
    /**
     * Create components for the JFrame.
     */
    private void createComponents() {
        
        //create panels
        myTopPanel = new JPanel();
        myCenterPanel = new JPanel(new BorderLayout());
        myBottomPanel = new JPanel();
        
        //add panels with buttons to frame
        myFrame.add(myTopPanel, BorderLayout.NORTH);
        myFrame.add(myBottomPanel, BorderLayout.SOUTH);
        
        //create buttons for top panel
        myTogglableButtons = new ArrayList<>(); //stores buttons for easier toggling
        myTogglableButtons.add(createFilterButton(new EdgeDetectFilter()));
        myTogglableButtons.add(createFilterButton(new EdgeHighlightFilter()));
        myTogglableButtons.add(createFilterButton(new FlipHorizontalFilter()));
        myTogglableButtons.add(createFilterButton(new FlipVerticalFilter()));
        myTogglableButtons.add(createFilterButton(new GrayscaleFilter()));
        myTogglableButtons.add(createFilterButton(new SharpenFilter()));
        myTogglableButtons.add(createFilterButton(new SoftenFilter()));
        
        //create buttons for bottom panel
        createOpenButton();
        myTogglableButtons.add(createSaveButton());
        myTogglableButtons.add(createCloseButton());
        myUndoButton = createUndoButton();
        
        //initially disable all buttons other than open
        disableTogglableButtons();
    }

    /**
     * Creates a filter button.
     * 
     * Citation: The code truncating the filter's name from the AbstractFilter class was used.
     * 
     * @param theFilter the filter that is to be performed when this button is clicked
     * @return the filter JBbutton
     */
    private JButton createFilterButton(final Filter theFilter) {
        
        //get class name of the filter
        final String name = theFilter.getClass().getName();
        
        //store the name of the filter without the suffix "Filter"
        final String filterName;
        final int dot = name.lastIndexOf('.');
        if (dot >= 0 && name.endsWith(FILTER_SUFFIX)) {
            // truncate the word "Filter"
            filterName = name.substring(dot + 1, name.length() - FILTER_SUFFIX.length());
        } else {
            filterName = name.substring(dot + 1, name.length());
        }
        
        //create button with the filter's name as the label
        final JButton button = new JButton(filterName);
        
        //action when button is pressed
        button.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
                backup(); //backup image
                theFilter.filter(myImage); //filter image
                refresh(); //refresh the image
            }
        });
        
        //add filter button to top panel and return the button
        myTopPanel.add(button);
        return button;
    }
    
    /**
     * Creates the open image button.
     * 
     * @return the open image JButton
     */
    private JButton createOpenButton() {
        
        //create button and add it to bottom panel
        final JButton button = new JButton("Open...");
        myBottomPanel.add(button);
        
        //set starting directory
        FILE_CHOOSER.setCurrentDirectory(new File(System.getProperty("user.dir")));
        
        //action when button is pressed
        button.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
                openAction(); //perform the open action
            }
        });  
        return button;
    }
    
    /**
     * Performs the open action for when the open button is pressed.
     */
    private void openAction() {
        
        //display an "open" dialog
        final int returnVal = FILE_CHOOSER.showOpenDialog(myFrame);
        
        //if file is chosen
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            final File file = FILE_CHOOSER.getSelectedFile(); //get file
            FILE_CHOOSER.setCurrentDirectory(file); //set current directory to match file
           
            //try to load file using PixelImage's load method
            try { 
                myImage = PixelImage.load(file); //load file
                enableTogglableButtons(); //enable other buttons
                myLabelWithIcon.setHorizontalAlignment(SwingConstants.CENTER); //center content
                myCenterPanel.add(myLabelWithIcon, BorderLayout.CENTER); //add label to panel
                myFrame.add(myCenterPanel, BorderLayout.CENTER); //add panel to the frame
                refresh(); //run refresh method to display loaded image
            
            //display error message if unable to load
            } catch (final IOException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Load Error:", 
                                                  JOptionPane.WARNING_MESSAGE);
            }                        
        }                
    }
    
    /**
     * Creates the save image button.
     * 
     * @return the save image JButton
     */
    private JButton createSaveButton() {
        
        //create button and add it to bottom panel
        final JButton button = new JButton("Save As...");
        myBottomPanel.add(button);
        
        //action when button is pressed
        button.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
                saveAction(); //perform the save action
            }
        }); 
        return button;
    }
    
    /**
     * Performs the save action for when the save button is pressed.
     */
    private void saveAction() {
          
        //display a "save" dialog
        final int returnVal = FILE_CHOOSER.showSaveDialog(myFrame);
        
        //if file is chosen
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            final File file = FILE_CHOOSER.getSelectedFile(); //get file
            FILE_CHOOSER.setCurrentDirectory(file); //set current directory to match file
            
            //try to save file using PixelImage's save method
            try {
                myImage.save(file); //save file
                
            //display error message if unable to load
            } catch (final IOException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Save Error:", 
                                              JOptionPane.WARNING_MESSAGE);
            }
        }
        
    }
    
    /**
     * Creates the close image button.
     * 
     * @return the close image JButton
     */
    private JButton createCloseButton() {
        
        //create button and add it to bottom panel
        final JButton button = new JButton("Close Image");
        myBottomPanel.add(button);
        
        //action when button is pressed
        button.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
                myFrame.remove(myCenterPanel);
                disableTogglableButtons();
                myFrame.pack();
                myFrame.setLocationRelativeTo(null);
            }
        });  
        return button;
    }
    
    /**
     * Creates the undo filter button.
     * 
     * @return the undo filter JButton
     */
    private JButton createUndoButton() {
        
        //create button, set disable it initially, and add it to bottom panel
        final JButton button = new JButton("Undo");
        button.setEnabled(false);
        myBottomPanel.add(button);
        
        //create an ArrayList to hold backup images
        myBackupImageList = new ArrayList<>();
        
        //action when button is pressed
        button.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
                undo(); //changes image to most recent backup image
                refresh(); //refresh with backup image
            }
        });  
        return button;
    }

    /**
     * Disables all buttons except for the open button.
     */
    private void disableTogglableButtons() {
        
        //iterate through all of the togglable buttons and disable them
        for (final Iterator<JButton> it = myTogglableButtons.iterator(); it.hasNext();) {
            it.next().setEnabled(false);
        }
        myUndoButton.setEnabled(false); //disable undo button
    }
    
    /**
     * Enables all buttons except for the open and undo button.
     */
    private void enableTogglableButtons() {
        
        //iterate through all of the togglable buttons and enable them
        for (final Iterator<JButton> it = myTogglableButtons.iterator(); it.hasNext();) {
            it.next().setEnabled(true);
        } 
    }
    
    /**
     * Resets center panel's icon, packs and repositions the frame - refreshing it.
     */
    private void refresh() {
        myLabelWithIcon.setIcon(new ImageIcon(myImage)); //change icon to new image
        myFrame.pack(); //re-pack the contents of the frame
        myFrame.setLocationRelativeTo(null); //center frame
    }
    
    /**
     * Recalls the latest backup image from the backup image list - undoing an action.
     * 
     * Citation: The code copying pixels from the PixelImage class was used.
     */    
    private void undo() {
        
        //if at least one backup image, convert myImage to that backup
        int size = myBackupImageList.size(); //get number of backup images
        if (size > 0) { 
            final Raster r = myBackupImageList.get(size - 1); //get latest backup
            final Pixel[][] data = new Pixel[r.getHeight()][r.getWidth()]; //convert to pixels
            int[] samples = new int[Pixel.NUM_CHANNELS]; //get samples
            
            //copy pixel values, one by one
            for (int row = 0; row < r.getHeight(); row++) {
                for (int col = 0; col < r.getWidth(); col++) {
                    samples = r.getPixel(col, row, samples);
                    final Pixel newPixel = new Pixel(samples[0], samples[1], samples[2]);
                    data[row][col] = newPixel;
                }
            }
            myImage.setPixelData(data); //set myImage to match backup's data
            myBackupImageList.remove(size - 1); //remove the backup that was used
            size--; //reduce size of list by 1
            if (size == 0) { //if number of backups is now zero
                myUndoButton.setEnabled(false); //disable undo button
            }
        }
    }
    
    /**
      * Backs up the image in a list, so that it can be recalled later if requested.
      */    
    private void backup() {
        final Raster raster = myImage.getData(); //get raster data from myImage
        myBackupImageList.add(raster); //add raster data to ArrayList of backup images
        myUndoButton.setEnabled(true); //enable undo button
    }
}
