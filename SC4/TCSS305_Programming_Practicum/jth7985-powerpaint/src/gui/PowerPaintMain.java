/* 
 * TCSS 305 – Autumn 2014
 * Assignment 5 - powerpaint
 */

package gui;

import java.awt.EventQueue;

/**
 * Runs PowerPaint by instantiating and starting the PowerPaintGUI.  
 * The starting code for this class was used from the given GUIMain class.
 * 
 * @author Jonathan Hughes
 * @version 30 October 2014
 */
public final class PowerPaintMain {

    /**
     * Private constructor, to prevent instantiation of this class.
     */
    private PowerPaintMain() {
        throw new IllegalStateException();
    }

    /**
     * The main method, invokes the PowerPaint GUI. Command line arguments are
     * ignored.
     * 
     * @param theArgs Command line arguments.
     */
    public static void main(final String[] theArgs) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                final PowerPaintGUI gui = new PowerPaintGUI();
                gui.start();    
            }
        });
    }
}
