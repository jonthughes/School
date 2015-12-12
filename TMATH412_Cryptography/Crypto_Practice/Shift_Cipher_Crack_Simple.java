
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/*
 * Jonathan Hughes
 * TMATH 412
 * Autumn 2015
 * 
 * This program decrypts a shift cipher encrypted text.
 */
public class Shift_Cipher_Crack_Simple {
    //English text ideal frequencies
    static final double[] idealFreq = {.082,.015,.028,.043,.127,.022,.020,.061,.070,.002,.008,.040,.024,
            .067,.075,.019,.001,.060,.063,.091,.028,.010,.023,.001,.020,.001};
    
    /*
     * Main method to run the program.  Create new shift cipher crack.
     */
    public static void main(String[] args) {
        String input = "UBZPGUBCEISGUBUPTVUFIPBFDUIFIPSUFBONICTTUPQEIBZHJTQNMSIEX";
        Shift_Cipher_Crack_Simple scc = new Shift_Cipher_Crack_Simple(input);
    }
    
    /*
     * Create shift cipher crack object and run decrypt on input. 
     */
    public Shift_Cipher_Crack_Simple(String input) {
        decrypt(input);
    }
    
    /*
     * Decrypt the text from the given text file.
     */
    public void decrypt(String input) {
        String cipherText = input; 
//        System.out.println(cipherText);
//        System.out.println("is decrypted to:");
        int[] cipherTextArray = new int[cipherText.length()];
        for (int i=0; i < cipherText.length(); i++) {
            char c = cipherText.charAt(i);
            cipherTextArray[i] = c - 'A';
        }
        String[] possibleShifts = new String[26];        
        int bestM = 0;
        double maxMg = 0;
        for (int m = 0; m < 26; m++) {
            String possibility = "";
            for (int i=0; i < cipherText.length(); i++) {
                int n = cipherTextArray[i] + m + 'A';
                if (n > 'Z') {
                    n = n - 26;
                }
                possibility += (char) n;                
            }
            possibleShifts[m] = possibility;
//            System.out.println("m:"+m+" "+possibleShifts[m]);
            double possibility_Mg = Mg(possibility);
            if (possibility_Mg > maxMg) {
                bestM = m;
                maxMg = possibility_Mg;
            }
        }
        System.out.println("k:"+bestM+" "+possibleShifts[bestM]);
    }
    
    
    /*
     * Calculate Mg for one row.
     */
    private static double Mg(String input) {
        char[] chars = new char[26];
        for (int i=0; i < 26; i++) {
            chars[i] = (char)('A' + i);
        }
        double[] relFreq = new double[26];
        double result = 0;
        for (int i=0; i < 26; i++) {
            double count = 0;
            for (int j=0; j < input.length(); j++) {
                if (input.charAt(j) == chars[i]) {
                    count++;
                }               
            }
            relFreq[i] = count/input.length();
            result += relFreq[i] * idealFreq[i];        
        }       
        return result;
   }
    
    /*
     * Loads the text file from String inputFile as the file name.
     * Returns encrypted cipher text as a String.
     */
    private String getCipherText(String inputFile) {
        String result = "";
        //Bring in text file to be read
        InputStream input = getClass().getResourceAsStream(inputFile); 
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String line;
        try { //Read through each line of text file
            while ((line = reader.readLine()) != null) { //While there is another line to read
                result += line; //append to result
            }
        } catch (IOException e) { //Catch exceptions caused by reading the file
            e.printStackTrace();
        }
        try {
            reader.close(); //Close file when done reading it
        } catch (IOException e2) { //Catch exceptions caused by closing the file
            e2.printStackTrace();
        }
        return result;
    }
}
