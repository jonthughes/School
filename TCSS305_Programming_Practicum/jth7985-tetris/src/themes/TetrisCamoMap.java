/* 
 * TCSS 305 – Autumn 2014
 * Assignment 6 - tetris
 */

package themes;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * A class that contains camo color palette name and set of colors for the Tetris GUI.
 * 
 * @author Jonathan Hughes
 * @version 02 December 2014
 */
public class TetrisCamoMap {

    /** The TetrisCamo colors for the ACU palette. */
    private static final TetrisCamo ACU_COLORS = new TetrisCamo(new Color(105, 111, 101), 
                                                                new Color(162, 155, 137), 
                                                                new Color(131, 131, 120));
    
    /** The TetrisCamo colors for the Multicam palette. */
    private static final TetrisCamo MULTICAM_COLORS = new TetrisCamo(new Color(129, 97, 62), 
                                                                     new Color(164, 177, 103), 
                                                                     new Color(214, 210, 180));
    
    /** The TetrisCamo colors for the woodland palette. */
    private static final TetrisCamo WOODLAND_COLORS = new TetrisCamo(Color.GREEN.darker(), 
                                                                     new Color(123, 85, 33), 
                                                                     Color.BLACK);
    
    /** The TetrisCamo colors for the desert palette. */
    private static final TetrisCamo DESERT_COLORS = new TetrisCamo(new Color(244, 214, 176),
                                                                   new Color(209, 169, 120),
                                                                   new Color(123, 68, 29));
    
    /** The TetrisCamo colors for the navy palette. */
    private static final TetrisCamo NAVY_COLORS = new TetrisCamo(new Color(0, 0, 128),
                                                                 new Color(76, 81, 109),
                                                                 new Color(204, 204, 255));
    
    /** The map with palette name as a String and TetrisCamo with the palette's colors. */
    private final Map<String, TetrisCamo> myMap;

    /** 
     * Constructs this object.
     */
    public TetrisCamoMap() {
        super();
        myMap = new HashMap<>();
        myMap.put("ACU", ACU_COLORS);
        myMap.put("Multicam", MULTICAM_COLORS);
        myMap.put("Desert", DESERT_COLORS);
        myMap.put("Navy", NAVY_COLORS);
        myMap.put("Woodland", WOODLAND_COLORS);
    }
    
    /**
     * Returns the map for the camo palettes.
     * 
     * @return the map for the camo palettes
     */
    public Map<String, TetrisCamo> getMap() {
        return myMap;
    }
}
