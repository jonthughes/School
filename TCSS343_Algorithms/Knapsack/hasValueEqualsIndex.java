
public class hasValueEqualsIndex {

    public static void main(String[] args) {
        int[] a = {-1, 0, 0, 0, 3};
        int[] b = {-1, 0, 0, 0, 3, 5};
        int[] c = {6, 7, 8, 9, 10, 11};
        int[] d = {-1, -1, -1, 99, 99, 99};
        int[] e = {-1, -1, -1, 99, 99, 99, 99};
//        System.out.println(a + " " + hasValueEqualsIndex(a));
//        System.out.println(b + " " + hasValueEqualsIndex(b));
//        System.out.println(c + " " + hasValueEqualsIndex(c));
        System.out.println(d + " " + hasValueEqualsIndex(d));
        System.out.println(e + " " + hasValueEqualsIndex(e));
    }

    private static boolean hasValueEqualsIndex(int[] a) {
        int low = 0;
        int high = a.length - 1;
        int count = 3;
        while (low < high) {
            count++;
//            System.out.println(""+ a[low]); 
//                    System.out.println(""+ low);
//                            System.out.println(""+ a[high]);
//                                    System.out.println(""+ high);
            count = count + 5;
            if(a[low] == low || a[high] == high) {
                return true;
            }
            if(a[low] < low) {
                low++;
                count = count + 4;
            } else {
                low = a[low];
                count = count + 4;
            } 
            if(a[high] > high) {
                high--;
                count = count + 4;
            } else
                high = a[high];
                count = count + 4;
        }
        count++;
        System.out.println("n="+a.length + " count="+count);
        return false;
    }
    
    

}
