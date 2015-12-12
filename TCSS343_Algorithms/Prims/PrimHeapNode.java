/**
 * Homework 6B: Prim's Algorithm
 * TCSS 343
 * Autumn 2015
 * 
 * @author Jonathan Hughes, Astam Aliyev (Group 3)
 * @version 1 December 2015
 * 
 * The PrimHeapNode class allows the PrimPowerGridApp to implement Prim's algorithm using
 * each vertex being represented by a heap node.
 * 
 * Base code used from MergesortHeapNode by Donald Chinn - September 19, 2003.
 */
public class PrimHeapNode implements Comparable<PrimHeapNode> {
    double myLabel;     //the vertex's label (or cost)
    Vertex myVertex;    //the vertex
    int myHeapPosition; //the vertex's position in the heap
    Vertex myReturnVertex; //the vertex that this vertex is connected to in Prim's
    
    /**
     * Constructs the PrimHeapNode.
     * @param label the PrimHeapNode's label
     * @param vertex the PrimHeapNode's Vertex
     * @param heapPosition the PrimHeapNode's heapPosition
     */
    public PrimHeapNode (double label, Vertex vertex, int heapPosition) {
        myLabel = label;
        myVertex = vertex;
        myHeapPosition = heapPosition;
        myReturnVertex = null;
    }
    
    /**
     * Accessor method for the label.
     * @return the double label of the vertex
     */
    public double getLabel() {
        return myLabel;
    }
    
    /**
     * Setter method for the label
     * @param input the new label of the PrimHeapNode
     */
    public void setLabel(double input) {
        myLabel = input;
    }
    
    /**
     * Accessor method for the vertex.
     * @return the Vertex object
     */
    public Vertex getVertex() {
        return myVertex;
    }
    
    /**
     * Accessor method for the heap position.
     * @return the heap position
     */
    public int getHeapPosition() {
        return myHeapPosition;
    }
    
    /**
     * Setter method for myHeapPosition
     * @param input the new myHeapPosition of the PrimHeapNode
     */
    public void setHeapPosition(int input) {
        myHeapPosition = input;
    }
    
    /**
     * Accessor method for the vertex that this vertex is connected to in Prim's
     * @return Vertex the vertex that this vertex is connected to in Prim's
     */
    public Vertex getReturnVertex() {
        return myReturnVertex;
    }
    
    /**
     * Setter method for the vertex that this vertex is connected to in Prim's
     * @param Vertex the vertex that this vertex is connected to in Prim's
     */
    public void setReturnVertex(Vertex input) {
        myReturnVertex = input;
    }       
    
    /**
     * Compares an input object to this PrimHeapNode.
     * 
     * @param rhs the other MergesortHeapNode object.
     * @return 0 if two objects are equal;
     *     less than zero if this object is smaller;
     *     greater than zero if this object is larger.
     * @exception ClassCastException if otherObject is not a PrimHeapNode.
     */
    public int compareTo (PrimHeapNode otherPrimHeapNode) {
        if (this.myLabel < otherPrimHeapNode.myLabel) {
            return -1;
        } else if (this.myLabel == otherPrimHeapNode.myLabel) {
            return 0;
        } else {
            return 1;
        }
    }
}