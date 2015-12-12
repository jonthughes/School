/* 
 * TCSS 305 – Autumn 2014
 * Assignment 6 - tetris
 */

package gui;

import java.awt.EventQueue;

/**
 * Runs Tetris by instantiating and starting the TetrisGUI.  
 * The starting code for this class was used from the PowerPaintMain class.
 * 
 * @author Jonathan Hughes
 * @version 18 November 2014
 */
public final class TetrisMain {

    /**
     * Private constructor, to prevent instantiation of this class.
     */
    private TetrisMain() {
        throw new IllegalStateException();
    }

    /**
     * The main method, invokes the GUI. Command line arguments are
     * ignored.
     * 
     * @param theArgs Command line arguments.
     */
    public static void main(final String[] theArgs) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                final TetrisGUI gui = new TetrisGUI();
                gui.start();    
            }
        });
    }
}
