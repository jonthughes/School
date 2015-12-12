/* 
 * TCSS 305 – Autumn 2014
 * Assignment 0 - Orientation
 */

/**
 * This program displays a secret message, which is hidden in the SecretMessage class.
 * Note: http://www.text-image.com/convert/ascii.html was used to help make the logo.
 * 
 * @author Jonathan Hughes
 * @version 25 September 2014
 */

final class HelloMain {
    
    /**
     * This is a private instructor to inhibit instantiation.
     */
    private HelloMain() {
        //do nothing
    }
    
    /**
     * This is the main method.  It displays a secret message.
     * 
     * @param theArgs These are the arguments passed from the command line.
     */
    public static void main(final String[] theArgs) {
        final SecretMessage message1 = new SecretMessage();
        message1.displaySecretMessage();
    }
}