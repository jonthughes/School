public class Hill_Involutory {
	public static void main(String[] args) {
	    int total = 0;
		for (int a = 0; a < 26; a++) {
		    int count = 0;
		    for (int b = 0; b < 26; b++) {
		        for (int c = 0; c < 26; c++) {
		            if ((Math.pow(a, 2)+(b*c))%26 == 1) {
		                count++;
		            }
				}
			}
		    total += count;
		    System.out.println(a+":"+count);
		}
		System.out.println("Total:"+total);
		
		    int a = 3;
            int count = 0;
            for (int b = 0; b < 26; b++) {
                for (int c = 0; c < 26; c++) {
                    if ((Math.pow(a, 2)+(b*c))%26 == 1) {
                        count++;
                        System.out.println(" "+b+":"+c);
                    }
                }
            }
            total += count;
            System.out.println(a+":"+count);
        
	}
}

