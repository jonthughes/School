
public class knapsack {
    public static void main(String[] args) {
//        int Wi[] = {0,3,4,1,2};
//        int Vi[] = {0,6,9,3,5};
//        int W = 5;
        int Wi[] = {0,4,6,1,2,3};
        int Vi[] = {0,15,19,5,4,14};
        int W = 9;
        
        int V[][] = new int[Vi.length][W + 1];
        V[0][0] = 0;
        for (int i = 1; i < Vi.length; i++) {
            V[i][0] = 0;
            for (int j = 1; j < W + 1; j++) {
                if (j < Wi[i]) {
                    V[i][j] = V[i-1][j];
                } else {
                    V[i][j] = Math.max(V[i-1][j], V[i-1][j-Wi[i]] + Vi[i]);
                }
                System.out.print(V[i][j]+" ");
            }
            System.out.println();
        }
    }
}
