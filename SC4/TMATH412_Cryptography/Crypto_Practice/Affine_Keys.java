public class Affine_Keys {
	public static void main(String[] args) {
		int m =1225;
		int count = 0;
		for (int a = 0; a < m; a++) {
			for (int x = 0; x < m; x++) {
				if ((a*x)%m == 1) {
					count++;
				}
			}
				
		}
		int keys = m*count;
		System.out.println(keys);
	}
}

