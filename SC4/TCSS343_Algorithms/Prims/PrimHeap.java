/**
 * Homework 6B: Prim's Algorithm
 * TCSS 343
 * Autumn 2015
 * 
 * @author Jonathan Hughes, Astam Aliyev (Group 3)
 * @version 1 December 2015
 * 
 * The PrimHeap class is a binary min heap of PrimHeapNode's.
 * 
 * Base code used from BinaryHeap by Donald Chinn - September 19, 2003.
 */
public class PrimHeap {
    
    /* the heap is organized using the implicit array implementation.
     * Array index 0 is not used
     */
    private PrimHeapNode[] elements;
    private int size;       // index of last element in the heap
    
    // Constructor
    public PrimHeap() {
        int initialCapacity = 10;
        this.elements = new PrimHeapNode[initialCapacity + 1];
        this.elements[0] = null;
        this.size = 0;
    }    
    
    /**
     * Constructor
     * @param capacity  number of active elements the heap can contain
     */    
    public PrimHeap(int capacity) {
        this.elements = new PrimHeapNode[capacity + 1];
        this.elements[0] = null;
        this.size = 0;
    }
        
    /**
     * Given an array of PrimHeapNodes, return a binary heap of those
     * elements.
     * @param data  an array of data (no particular order)
     * @return  a binary heap of the given data
     */
    public static PrimHeap buildHeap(PrimHeapNode[] data) {
        PrimHeap newHeap = new PrimHeap(data.length);
        for (int i = 0; i < data.length; i++) {
            newHeap.elements[i+1] = data[i];
        }
        newHeap.size = data.length;
        for (int i = newHeap.size / 2; i > 0; i--) {
            newHeap.percolateDown(i);
        }
        return newHeap;
    }


    /**
     * Determine whether the heap is empty.
     * @return  true if the heap is empty; false otherwise
     */
    public boolean isEmpty() {
        return (size < 1);
    }
    
    
    /**
     * Insert an object into the heap.
     * @param key   a key
     */
    public void insert(PrimHeapNode key) {

        if (size >= elements.length - 1) {
            // not enough room -- create a new array and copy
            // the elements of the old array to the new
            PrimHeapNode[] newElements = new PrimHeapNode[2*size];
            for (int i = 0; i < elements.length; i++) {
                newElements[i] = elements[i];
            }
            elements = newElements;
        }
        
        size++;
        elements[size] = key;
        percolateUp(size);
    }
    
    
    /**
     * Remove the object with minimum key from the heap.
     * @return  the object with minimum key of the heap
     */
    public PrimHeapNode deleteMin() throws PrimEmptyHeapException {
        if (!isEmpty()) {
            PrimHeapNode returnValue = elements[1];
            elements[1] = elements[size];
            size--;
            percolateDown(1);
            returnValue.setHeapPosition(0);
            return returnValue;
            
        } else {
            throw new PrimEmptyHeapException();
        }
    } 
    
    /**
     * Given an index in the heap array, percolate that key up the heap.
     * @param index     an index into the heap array
     */
    public void percolateUp(int index) {
        PrimHeapNode temp = elements[index];  // keep track of the item to be moved
        while (index > 1) {
            if (temp.compareTo(elements[index/2]) < 0) {
                elements[index] = elements[index/2];
                elements[index].setHeapPosition(index);
                index = index / 2;
            } else {
                break;
            }
        }
        elements[index] = temp;
        elements[index].setHeapPosition(index);
    }    
    
    /**
     * Given an index in the heap array, percolate that key down the heap.
     * @param index     an index into the heap array
     */
    public void percolateDown(int index) {
        int child;
        PrimHeapNode temp = elements[index];
        
        while (2*index <= size) {
            child = 2 * index;
            if ((child != size) &&
                (elements[child + 1].compareTo(elements[child]) < 0)) {
                child++;
            }
            // ASSERT: at this point, elements[child] is the smaller of
            // the two children
            if (elements[child].compareTo(temp) < 0) {
                elements[index] = elements[child];
                elements[index].setHeapPosition(index);
                index = child;
            } else {
                break;
            }
        }
        elements[index] = temp;
        elements[index].setHeapPosition(index);
    }
}
