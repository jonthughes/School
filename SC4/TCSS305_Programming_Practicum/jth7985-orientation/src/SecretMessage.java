/* 
 * TCSS 305 – Autumn 2014
 * Assignment 0 - Orientation
 */

/**
 * This program is used to display the secret message that is hidden from the HelloMain class.
 * Note: http://www.text-image.com/convert/ascii.html was used to help make the logo.
 * 
 * @author Jonathan Hughes
 * @version 25 September 2014
 */

final class SecretMessage {
    
    /**
     * This String stores a secret message.
     */
    private final String mySecretMessage;
    
    /**
     * This creates a SecretMessage object.
     */
    public SecretMessage() {
        mySecretMessage = "Go Hawks!";
    }
    
    /**
     * displaySecretMessage prints the secret message to the console.
     * Note: http://www.text-image.com/convert/ascii.html was used to help make the logo.
     */
    public void displaySecretMessage() {
        System.out.println("        /-`                                                   ");
        System.out.println("       oNmddhhhhhhhhhhhhhhhhhhhhhhhhhhhhhys+:.                ");
        System.out.println("      oNNNNNNNNNNNNNNNNNNNNmhyssssyhdmNNNNNNNmh/.``           ");
        System.out.println("     sNNNNNNNNNNNNNNNNNNho:/+shdhhhysooshdNNNNNNhhdhys+:.`    ");
        System.out.println("   `sNNNNNNNNNNNNNNNmds:-odmmy+-.sNmymmdhysyhdd/./syhdmNmds:` ");
        System.out.println("   `::::::::::::::::-` +mNNNy.  `hmmhhdddh+omh`     ``.:smNNh.");
        System.out.println(" `+ooooooooooooooooo+:../hmNNmyo//+oooooooshs`           -dNNy");
        System.out.println(" ohhhhhhhhhhhhhhhhhhhhy+.`:mNNmyo//++++++//-`             oNNh");
        System.out.println(":hhhhhhhhhhhhhhhhhhhhhhy:.hNNs-.+ss+++++++++oo+//-..`    `hNm:");
        System.out.println(".syhhhhhhhhhhhhhhhhhhhh/ yNNs-+md/ohdmmmmmdddhhhyyysso/-.sNm/ ");
        System.out.println(" `.--------------------. .://///////////////////++ossydmdNh-  ");
        System.out.println("                                                      .mm+`   ");
        System.out.println("                                                      `+.     ");
        System.out.println(mySecretMessage);
    }
}
