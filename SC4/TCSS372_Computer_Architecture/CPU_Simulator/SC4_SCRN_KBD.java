import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import javax.swing.text.DefaultStyledDocument;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;
import java.util.Observer;

/**
 * SC4 CPU
 * TCSS 372 A
 * Autumn 2015
 * By: Jonathan Hughes, David Humphreys, Artem Davtyan
 * 
 * The SC4_SCRN_KBD class simulates the screen and keyboard I/O device of the SC4.
 */
public class SC4_SCRN_KBD extends Observable implements Observer, KeyListener {
   JFrame frame;
   DefaultStyledDocument scrnDoc;
   JTextPane pane;
   int textLoc;

   /**
    * Constructs screen and keyboard listener
    */
   public SC4_SCRN_KBD() {
      frame = new JFrame("SC4_SCRN_KBD");
      scrnDoc = new DefaultStyledDocument();
      pane = new JTextPane(scrnDoc);
      pane.setEditable(false);
      frame.getContentPane().add(new JScrollPane(pane));
      frame.setSize(400, 400);
      frame.setResizable(false);
      frame.setVisible(true);
      pane.addKeyListener(this);
      textLoc = 0;
   }
   
   /**
    * Returns the screen's pane.
    * @return JTextPane of the screen's pane.
    */
   public JTextPane getPane() {
      return pane;
   }
   
   /**
    * Displays given txt on the SC4_SCRN_KBD.
    * @param txt String to add to the screen.
    */
   public void display(String txt) {
      try {
         scrnDoc.insertString(textLoc, txt, null);
      }
      catch (Exception e) {
         System.err.println("Error: add() failed to insert " + txt);
      }
      textLoc += txt.length();
   }
 
   /**
    * Update the screen with scrnData from SC4_CPU.
    */
   @Override
   public void update(Observable obs, Object scrnData) {
       display("" + (char) scrnData);
       setChanged();
       notifyObservers("scrnReady"); //Screen is now ready
   }

   /**
    * Send KBD key press to SC4_CPU via notifyObservers.
    */
   @Override
   public void keyTyped(KeyEvent e) {
       setChanged();
       notifyObservers(e.getKeyChar());
   }

   @Override
   public void keyPressed(KeyEvent e) {
   }

   @Override
   public void keyReleased(KeyEvent e) {
   }
}
