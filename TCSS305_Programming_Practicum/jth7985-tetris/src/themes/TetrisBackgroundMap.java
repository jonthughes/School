/* 
 * TCSS 305 – Autumn 2014
 * Assignment 6 - tetris
 */

package themes;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * A class that contains the background Tetris themes.
 * 
 * @author Jonathan Hughes
 * @version 02 December 2014
 */
public class TetrisBackgroundMap {

    /** 
     * The jungle image.  It was used from http://mariposajunglelodge.com/images/bg-page.jpg
     */
    public static final File JUNGLE_IMAGE_FILE = new File("background/jungle1.jpg");
    
    /** 
     * The desert image.  It was used from 
     * http://www.hdwallpapers3d.co/wp-content/uploads/2014/02/
     * african_desert-www.hdwallpapers3d.co_.jpg
     */
    public static final File DESERT_IMAGE_FILE = new File("background/desert.jpg");
    
    /** 
     * The forest image.  It was used from 
     * http://www.hdwallsource.com/img/2014/2/forest-background-18576-19046-hd-wallpapers.jpg
     */
    public static final File FOREST_IMAGE_FILE = new File("background/forest.jpg");
    
    /** 
     * The urban image.  It was used from 
     * http://upload.wikimedia.org/wikipedia/commons/e/e5/University_of_Washington,_Tacoma.jpg
     */
    public static final File URBAN_IMAGE_FILE = new File("background/urban.jpg");
    
    /** 
     * The ocean image.  It was used from 
     * http://img.wallpaperstock.net:81/blue-ocean-wallpapers_35232_1280x800.jpg
     */
    public static final File OCEAN_IMAGE_FILE = new File("background/ocean.jpg");
    
    /** 
     * The mountain image.  It was used from 
     * http://wallpapers5.com/getimage.php?img=21258913/Landscape/Nature/Mount-Rainier-
     * From-Chinook-Pass-Washington.jpg&w=2560&h=1600&q=100&mode=fit&f=bw
     */
    public static final File MOUNTAIN_IMAGE_FILE = new File("background/mountain.jpg");
    
    /** The background image's JFrame. */
    private final JFrame myFrame;
    
    /** The map with palette name as a String and TetrisCamo with the palette's colors. */
    private final Map<String, BufferedImage> myMap;
    
    /** 
     * Constructs the TetrisBackgroundMap. 
     * 
     * @param theFrame the JFrame to display a warning message, if the file does not load
     */
    public TetrisBackgroundMap(final JFrame theFrame) {
        myFrame = theFrame;
        myMap = new HashMap<>();
        myMap.put("Jungle", getBackground(JUNGLE_IMAGE_FILE));
        myMap.put("Desert", getBackground(DESERT_IMAGE_FILE));
        myMap.put("Forest", getBackground(FOREST_IMAGE_FILE));
        myMap.put("Urban", getBackground(URBAN_IMAGE_FILE));
        myMap.put("Ocean", getBackground(OCEAN_IMAGE_FILE));
        myMap.put("Mountain", getBackground(MOUNTAIN_IMAGE_FILE));
    }
    
    
    
    /**
     * Returns the background image from the background's image file.
     * 
     * @param theFile the file that contains the background image
     * @return the background image
     */
    private BufferedImage getBackground(final File theFile) {
        BufferedImage result = null;
        try {                
            result = ImageIO.read(theFile);
        } catch (final IOException ex) {
            JOptionPane.showMessageDialog(myFrame, "File error", 
                                         "Invalid background image file!", 
                                         JOptionPane.WARNING_MESSAGE);
        }
        return result;
    }
    
    /**
     * Returns the Map that contains background names and their BufferedImage.
     * 
     * @return the Map that contains background names and their BufferedImage
     */
    public Map<String, BufferedImage> getMap() {
        return myMap;
    }
}
