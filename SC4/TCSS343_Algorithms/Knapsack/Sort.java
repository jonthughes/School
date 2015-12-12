import java.util.*;


/**
 * A class that contains a group of sorting algorithms.
 * The input to the sorting algorithms is assumed to be
 * an array of integers.
 * 
 * @author Donald Chinn
 * @version September 19, 2003
 */
public class Sort {

    // Constructor for objects of class Sort
    public Sort() {
    }

    public static void insertionSort (int[] data) {
        for (int i = 1; i < data.length; i++) {
            int temp = data[i];
            int j = i-1;
            while ((j >= 0) && (data[j] > temp)) {
                data[j+1] = data[j];
                j--;
            }
            data[j+1] = temp;
        }
    }
    
    public static void mergesort (int[] data) {
        mergesortRecursive (data, 0, data.length - 1);
    }

    public static void mergesortRecursive (int[] data, int low, int high) {
        if (low < high) {
            mergesortRecursive (data,
                                low,
                                low + (high-low)/2 );
            mergesortRecursive (data,
                                low + (high-low)/2 + 1,
                                high);
            merge (data, low, high);
        }
    }
    
    
    public static void merge (int[] data, int low, int high) {
        // create a new array for the sorted data
        int[] temp = new int[high - low + 1];
        
        int lowIndex = low;
        int highIndex = low + (high-low)/2 + 1;
        int midIndex = low + (high-low)/2;
        int tempIndex = 0;
        
        while ((lowIndex <= midIndex) &&
            (highIndex <= high)) {
            if (data[lowIndex] < data[highIndex]) {
                temp[tempIndex] = data[lowIndex];
                tempIndex++;
                lowIndex++;
            } else {
                temp[tempIndex] = data[highIndex];
                tempIndex++;
                highIndex++;
            }
        }
        
        if (lowIndex > midIndex) {
            // low subarray finished first
            while (highIndex <= high) {
                temp[tempIndex] = data[highIndex];
                tempIndex++;
                highIndex++;
            }
        
        } else {
            // high subarray finished first
            while (lowIndex <= midIndex) {
                temp[tempIndex] = data[lowIndex];
                tempIndex++;
                lowIndex++;
            }
        }
        
        // copy data back from temp to data
        int dataIndex;
        for (tempIndex = 0, dataIndex = low;
            dataIndex <= high;
            tempIndex++, dataIndex++) {
            
            data[dataIndex] = temp[tempIndex];
        }
        
    }
    
    
    /**
     * Given an array of integers and an integer k, sort the array
     * (ascending order) using k-way mergesort.
     * @param data  an array of integers
     * @param k     the k in k-way mergesort
     */
    public static void kwayMergesort (int[] data, int k) {
        kwayMergesortRecursive (data, 0, data.length - 1, k);
    }
    
    /**
     * The recursive part of k-way mergesort.
     * Given an array of integers (data), a low index, high index, and an integer k,
     * sort the subarray data[low..high] (ascending order) using k-way mergesort.
     * @param data  an array of integers
     * @param low   low index
     * @param high  high index
     * @param k     the k in k-way mergesort
     */
    public static void kwayMergesortRecursive (int[] data, int low, int high, int k) {
        if (low < high) {
            for (int i = 0; i < k; i++) {
                kwayMergesortRecursive (data,
                                        low + i*(high-low+1)/k,
                                        low + (i+1)*(high-low+1)/k - 1,
                                        k);
            }
            merge (data, low, high, k);
        }
    }
    

    /**
     * Given an array of integers (data), a low index, a high index, and an integer k,
     * sort the subarray data[low..high].  This method assumes that each of the
     * k subarrays  data[low + i*(high-low+1)/k .. low + (i+1)*(high-low+1)/k - 1],
     * for i = 0..k-1, are sorted.
     * @param data  an array of integers
     * @param low   low index
     * @param high  high index
     * @param k     the k in k-way mergesort
     */
    public static void merge (int[] data, int low, int high, int k) {
    
        if (high < low + k) {
            // the subarray has k or fewer elements
            // just make one big heap and do deleteMins on it
            Comparable[] subarray = new MergesortHeapNode[high - low + 1];
            for (int i = 0, j = low; i < subarray.length; i++, j++) {
                subarray[i] = new MergesortHeapNode(data[j], 0);
            }
            BinaryHeap heap = BinaryHeap.buildHeap(subarray);

            for (int j = low; j <= high; j++) {
                try {
                    data[j] = ((MergesortHeapNode) heap.deleteMin()).getKey();
                }
                catch (EmptyHeapException e) {
                    System.out.println ("Tried to delete from an empty heap.");
                }
            }
            
        } else {
            // do a k-way merge on the k sorted subarrays
            Comparable[] mergeArray = new MergesortHeapNode[k]; //array that does merging
            
            int subarrayLength = (high - low + 1)/k; //length of subarrays (except for last)
            int[] subarrayPointer = new int[k]; //keeps track of front of subarrays
            int[] subarrayEndPointer = new int[k]; //keeps track of end of subarrays
            
            //run through k subarrays
            for (int i = 0; i < k; i++) {
                //beginning of each subarray is low + i*subarrayLength
                subarrayPointer[i] = low + i*(high-low+1)/k;
                //end of each subarray is next one (i+1 instead of i) and then subtract 1
                subarrayEndPointer[i] = low + (i+1)*(high-low+1)/k - 1;
                if (i == (k-1)) { //the last array expand to account for left overs
                    subarrayEndPointer[i] = high; //include up to last element
                }
                //put all first subarray elements into the mergeArray
                mergeArray[i] = new MergesortHeapNode(data[subarrayPointer[i]], i);     
            }
            BinaryHeap heap = BinaryHeap.buildHeap(mergeArray); //the heap!
            int[] sortedData = new int[high - low + 1]; //temp array to store the removed values
            int iterator = 0; //temp array sortedData's iterator
            while (!heap.isEmpty()) { //while there are elements still in heap
                try {
                    //get the min element out of the heap
                    MergesortHeapNode removed = (MergesortHeapNode) heap.deleteMin();
                    //save its value to the temp array sortedData
                    sortedData[iterator] = removed.getKey();
                    iterator++; //iterate since that index is done
                    //get which subarray the removed element belonged to
                    int whichSubarray = removed.getWhichSubarray();
                    //iterate subarray pointer for removed element's subarray and save it
                    int newSubarrayPointer = ++subarrayPointer[whichSubarray];
                    //if not past end of subarray, get the next replacement element
                    if (newSubarrayPointer <= subarrayEndPointer[whichSubarray]) {
                        //create a replacement element, with the iterated pointer, linked to the same subarray
                        MergesortHeapNode replacement = 
                                new MergesortHeapNode(data[newSubarrayPointer], whichSubarray);
                        //put replacement into the heap
                        heap.insert(replacement);
                    }
                } catch (EmptyHeapException e) {
                    System.out.println ("Tried to delete from an empty heap.");
                }
            }
            //Copy merged subarrays back into the data array
            for (int j = low; j <= high; j++) {
                data[j] = sortedData[j - low]; //sortedData starts at 0, not low
            }  
        }
    }
    
    
    /**
     * Given an integer size, produce an array of size random integers.
     * The integers of the array are between 0 and size (inclusive) with
     * random uniform distribution.
     * @param size  the number of elements in the returned array
     * @return      an array of integers
     */
    public static int[] getRandomArrayOfIntegers(int size) {
        int[] data = new int[size];
        for (int i = 0; i < size; i++) {
            data[i] = (int) ((size + 1) * Math.random());
        }
        return data;
    }
    

    /**
     * Given an integer size, produce an array of size random integers.
     * The integers of the output array are between 0 and size-1 with
     * exactly one of each in the array.  Each permutation is generated
     * with random uniform distribution.
     * @param size  the number of elements in the returned array
     * @return      an array of integers
     */
    public static int[] getRandomPermutationOfIntegers(int size) {
        int[] data = new int[size];
        for (int i = 0; i < size; i++) {
            data[i] = i;
        }
        // shuffle the array
        for (int i = 0; i < size; i++) {
            int temp;
            int swap = i + (int) ((size - i) * Math.random());
            temp = data[i];
            data[i] = data[swap];
            data[swap] = temp;
        }
        return data;
    }


    /**
     * Perform checks to see if the algorithm has a bug.
     */
    private static void testCorrectness(int k, int n) {
        int[] data = getRandomPermutationOfIntegers(100);
        
//        for (int i = 0; i < data.length; i++) {
//            System.out.println("data[" + i + "] = " + data[i]);
//        }
        
        kwayMergesort(data, k);
        
//        for (int i = 0; i < data.length; i++) {
//            System.out.println("data[" + i + "] = " + data[i]);
//        }
        boolean result = true;
        // verify that data[i] = i
        for (int i = 0; i < data.length; i++) {
            if (data[i] != i) {
//                System.out.println ("Error!  data[" + i + "] = " + data[i] + ".");
                result = false;
            }
        }
        if (result) {
            System.out.println("k="+k+" and n="+n+" test success!");
        } else {
            System.out.println("k="+k+" and n="+n+" test failed :(");
        }
        
    }
    
    
    /**
     * Perform timing experiments.
     */
    private static long testTiming (int k, int n) {
        // timer variables
        long totalTime = 0;
        long startTime = 0;
        long finishTime = 0;

        // start the timer
        Date startDate = new Date();
        startTime = startDate.getTime();
        

        int[] data = getRandomArrayOfIntegers(n);
//        insertionSort(data);
//        mergesort(data);
        kwayMergesort(data, k);
    
        // stop the timer
        Date finishDate = new Date();
        finishTime = finishDate.getTime();
        totalTime += (finishTime - startTime);
        
//        System.out.println("** Results for k-way mergesort:");
////        System.out.println("** Results for mergesort:");
////        System.out.println("** Results for insertionSort:");
////        System.out.println("** Results for heapSort:");
//        System.out.println("    " + "n = " + n + "    " + "k = " + k);
//        System.out.println("    " + "Time: " + totalTime + " ms.");
        return totalTime;
    }

    /**
     * code to test the sorting algorithms
     */
    public static void main (String[] argv) {
        testCorrectness(2, 100000);
        testCorrectness(3, 123423);
        testCorrectness(4, 123423);
        testCorrectness(5, 123423);
        testCorrectness(6, 123423);
        testCorrectness(7, 123423);
        testCorrectness(123, 9834873);
        testCorrectness(40984, 983444873);
        System.out.println();
//        System.out.println(testTiming(5, 32000000));
        
        int[] k = {2,3,5,10,20,50}; // k = k in k-way mergesort
        int[] n = {200000,400000,800000,1600000,3200000}; // n = size of the array      
        
        for(int a = 0; a < k.length ; a++) {
            for(int i = 0; i < n.length; i++) {
                testTiming(k[5], n[i]);
                long avgTime = (testTiming(k[5], n[i]) + testTiming(k[5], n[i]) + testTiming(k[5], n[i]))/3;
                System.out.println("k="+k[a]+" n="+n[i]+ " avgTime="+avgTime+"ms.");
                
            }
            System.out.println();
        }
        System.out.println("Done.");
    }
}
