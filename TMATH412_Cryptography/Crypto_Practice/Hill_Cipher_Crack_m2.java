public class Hill_Cipher_Crack_m2 {
	public static void main(String[] args) {
	// [x0 x1][k0 k1] = [y0 y1]
	// [x2 x3][k2 k3]   [y2 y3]
	    int[] x = {19,20,13,4};
	    int[] y = {6,15,4,5};
	    int[] k = new int[4];
	    for (int k0 = 0; k0 < 26; k0++) {
	        for (int k1 = 0; k1 < 26; k1++) {
	            for (int k2 = 0; k2 < 26; k2++) {
	                for (int k3 = 0; k3 < 26; k3++) {
	                    if ((x[0]*k0 + x[1]*k2)%26 == y[0]) {
	                        if ((x[0]*k1 + x[1]*k3)%26 == y[1]) {
	                            if ((x[2]*k0 + x[3]*k2)%26 == y[2]) {
	                                if ((x[2]*k1 + x[3]*k3)%26 == y[3]) {
	                                    System.out.println("{"+k0+","+k1+","+k2+","+k3+"}");
	                                }
	                            }
	                        }
	                    }
	                }
	            }
	        }
	    }
	}
}

