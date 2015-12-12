
public class Vigenere_Cipher_Crack {
    static final double[] idealFreq = {.082,.015,.028,.043,.127,.022,.020,.061,.070,.002,.008,.040,.024,
            .067,.075,.019,.001,.060,.063,.091,.028,.010,.023,.001,.020,.001};
    
    public static void main(String[] args) {
        String cipherText = "ROVVYOFOBILYNIRYGKBOIYEKVVNYSXQSWNYSXQQBOKDGOVVOFOX";
        System.out.println(cipherText);
        System.out.println("is decrypted to:");
        int[] cipherTextArray = new int[cipherText.length()];
        for (int i=0; i < cipherText.length(); i++) {
            char c = cipherText.charAt(i);
            cipherTextArray[i] = c - 'A';
        }
        String[] possibleShifts = new String[26];
        double[] indicesOfCoincidence = new double[26];
        int max = 0;
        double maxprob = 0;
        for (int m=0; m < 26; m++) {
            String possibility = "";
            for (int i=0; i < cipherText.length(); i++) {
                int n = cipherTextArray[i] + m + 'A';
                if (n > 'Z') {
                    n = n - 26;
                }
                possibility += (char) n;                
            }
//            System.out.println("m="+m);
//            System.out.println(possibility);
            possibleShifts[m] = possibility;
//            indicesOfCoincidence[m] = getIndexOfCoincidence(possibility);
            double probs = probs(possibility);
            if (probs > maxprob) {
                max = m;
                maxprob = probs;
            }
//            System.out.println(probs(possibility));
//            System.out.println();
        }
        System.out.println(possibleShifts[max]);
   }
   
    private static double probs(String input) {
        char[] chars = new char[26];
        for (int i=0; i < 26; i++) {
            chars[i] = (char)('A' + i);
        }
        double[] relFreq = new double[26];
        double Mg = 0;
        for (int i=0; i < 26; i++) {
            double count = 0;
            for (int j=0; j < input.length(); j++) {
                if (input.charAt(j) == chars[i]) {
                    count++;
                }               
            }
            relFreq[i] = count/input.length();
            Mg += relFreq[i] * idealFreq[i];
        
        }       
        return Mg;
    }    
    
   private static double getIndexOfCoincidence(String input) {
       char[] chars = new char[26];
       for (int i=0; i < 26; i++) {
           chars[i] = (char)('A' + i);
       }
       double ic = 0;
       int[] freq = new int[26];
       for (int i=0; i < 26; i++) {
           int count = 0;
           for (int j=0; j < input.length(); j++) {
               if (input.charAt(j) == chars[i]) {
                   count++;                   
               }               
           }
           freq[i] = count;
           ic += choose2(count);
       }
       int n = input.length();
       double denom = choose2(n);
       return (ic/denom);
   }
   
   private static int choose2(int n) {
       return n*(n-1)/2;
   }
}
