import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;

/**
 * Homework 6B: Prim's Algorithm
 * TCSS 343
 * Autumn 2015
 * 
 * @author Jonathan Hughes, Astam Aliyev (Group 3)
 * @version 1 December 2015
 * 
 * The PrimPowerGridApp class runs Prim's Algorithm on a user specified text file 
 * representation of a graph.  This class uses a graph ADT provided by Professor Donald
 * Chinn from the University of Washington - Tacoma.
 */
public class PrimPowerGridApp {

    /** The value to put at the initial label of each vertex (simulates infinity) */
    private double initialLabel = Double.MAX_VALUE;
    
    /** Scanner for console input */
    private Scanner inputReader = new Scanner(System.in);
    
    /** String for storing the fileName of the text file specifying the graph. **/
    private String fileName;
    
    /** A hash table of (String, Vertex) pairs representing the graph. */
    private Hashtable<String, Vertex> graphHashtable;
    
    /** A simpleGraph representing the graph. */
    private SimpleGraph graph;
    
    /** The heap that contains the PrimHeapNode's that represent each vertex of the graph. */
    private PrimHeap primHeap;
    
    /** Whether the graph is connected. */
    private boolean connected;
    
    /** Whether the graph has a negative cost in an edge. */
    private boolean negative;
    
    /**
     * Main method to create the PrimPowerGridApp object and run its methods.
     * @param args Command line arguments - none required.
     * @throws PrimEmptyHeapException if PrimHeap is empty
     */
    public static void main(String[] args) throws PrimEmptyHeapException {
        PrimPowerGridApp prims = new PrimPowerGridApp(); //create PrimPowerGridApp Object
        double totalCost = prims.getPrimsTotalCost();
        System.out.println();
        if (prims.connected) {
            System.out.println("Total cost of minimum spanning tree: " + totalCost);
        } else {
            System.out.println("The graph is not connected, so there cannot be a minimum spanning tree."); 
            System.out.println("However, here is the total cost of non-connected components: " + totalCost);
        }
        if (prims.negative) {
            System.out.println("Warning, there was a negative value edge.");
        }
    }

    /**
     * Constructs a PrimPowerGridApp object based on user input for graph text file.
     */
    public PrimPowerGridApp() {
        showIntro(); //show intro text
        graphHashtable = getGraphHashtable(); //create graph's hash table
        primHeap = initializePrimHeap(graphHashtable);
        connected = true;
        negative = false;
    }
    
    /**
     * Returns the PrimHeap with PrimHeapNodes based on the graphHashtable containing
     * vertices from the input graph.
     * @param graphHashtable Hashtable containing vertex names and Vertex objects
     * @return PrimHeap containing all PrimHeapNodes
     */
    private PrimHeap initializePrimHeap(Hashtable<String, Vertex> graphHashtable) {
        PrimHeap result = new PrimHeap();
        Set<String> vertexNames = graphHashtable.keySet();
        for(String vertexName: vertexNames){ //iterate through all vertices
            Vertex currentVertex = graphHashtable.get(vertexName); //get current vertex
            PrimHeapNode currentNode = new PrimHeapNode(initialLabel, currentVertex, 0);
            currentVertex.setData(currentNode); //link vertex back to node
            result.insert(currentNode); //insert node into the heap
        }
        return result;
    }
    
    /**
     * Calculates the total cost of the graph by running Prim's Algorithm.
     * @return double of the total cost
     * @throws PrimEmptyHeapException if heap is empty and deleteMin is called
     */
    private double getPrimsTotalCost() throws PrimEmptyHeapException, NullPointerException {
        double result = 0;
        PrimHeapNode currentNode = null;
        //get first vertex
        if (!primHeap.isEmpty()) {//if primHeap isn't empty
            currentNode = primHeap.deleteMin();
            currentNode.setLabel(0); //first node has no cost
            System.out.println();
            System.out.println("Vertex Added: "+ currentNode.getVertex().getName()+ 
                    ", Edge Added: N/A" + ", Cost Added: " + currentNode.getLabel());
        }
        while (!primHeap.isEmpty()) {//while primHeap isn't empty
            updatePrimHeap(currentNode); //update heap nodes around chosen node
            currentNode = primHeap.deleteMin(); //chose next minimum node
            if (currentNode.getLabel() != Double.MAX_VALUE) {
                System.out.println("Vertex Added: "+ currentNode.getVertex().getName() + 
            
                    ", Edge Added: " + currentNode.getReturnVertex().getName() +" to " 
                    + currentNode.getVertex().getName()
                    + ", Cost Added: " + currentNode.getLabel());
                result += currentNode.getLabel(); //add node's label value to total
            } else { //if node cost is Double.MAX_VALUE - the initial highest value
                System.out.println("WARNING THIS IS NOT A CONNECTED GRAPH! Next component:");
                connected = false;
            }
            if (currentNode.getLabel() < 0) { //if node has a negative cost
                System.out.println("WARNING NEGATIVE COST!");
                negative = true;
            }
        }
        return result;
    }

    /**
     * Updates the nodes adjacent to the chosen node with their edge weights, if it is lower than
     * their current label.
     * @param currentNode the node that was chosen by Prim's algorithm
     */
    private void updatePrimHeap(PrimHeapNode currentNode) {
        //get current vertex object from the node
        Vertex currentVertex = currentNode.getVertex();
        //get the edges for the current vertex
        @SuppressWarnings("unchecked")
        LinkedList<Edge> edgeList = currentVertex.incidentEdgeList;
        //iterate through the edge list
        for (int i = 0; i < edgeList.size(); i++) {
            Edge currentEdge = (Edge) edgeList.get(i); //get edge at this index
            double edgeWeight = (double) currentEdge.getData(); //get edge weight of that edge
            //get the vertex on the other side of the edge
            Vertex otherVertex = graph.opposite(currentVertex, currentEdge);
            //get PrimHeapNode that represents the vertex
            PrimHeapNode otherPrimHeapNode = (PrimHeapNode) otherVertex.getData();
            //get node's position in heap
            int otherPosition = otherPrimHeapNode.getHeapPosition();
            if (otherPosition > 0) { //if node is still in the heap
                //if old label is greater than edge weight
                if (otherPrimHeapNode.getLabel() > edgeWeight)  { 
                    otherPrimHeapNode.setLabel(edgeWeight); //update its label to its edge weight
                    otherPrimHeapNode.setReturnVertex(currentVertex); //update return vertex
                    primHeap.percolateUp(otherPosition); //percolate up if updated  
                }
            }
        }      
    }

    /**
     * Gets the Hashtable<String, Vertex> from the user input for graph text file.
     * If invalid user input is given, continually asks until valid input is given.
     * 
     * @return Hashtable<String, Vertex> of specified graph
     */
    @SuppressWarnings("unchecked")
    private Hashtable<String, Vertex> getGraphHashtable() {
        Hashtable<String, Vertex> result = null;
        while (result == null) {
            try { //try to load the file specified by user
                fileName = getFileName();
                graph = new SimpleGraph();
                result = GraphInput.LoadSimpleGraph(graph, fileName);
            }
            catch (Error e) { //if invalid, warn user and ask again
                System.out.println("Error: " + e.getMessage());  
                System.out.println("Please try again."); 
                System.out.println();
            }
        }
        return result;
    }

    /** 
     * Show program intro and file format notes in console. 
     */
    private void showIntro() {
        System.out.println("*** Welcome to the Prim's Algorithm Program! ***");
        System.out.println("                 by: Group 3");
        System.out.println();
        System.out.println("This program requires a graph text file.");
        System.out.println();
        System.out.println("The format of the file is: ");
        System.out.println("* Each line of the file contains 3 tokens, where the first two are strings");
        System.out.println("  representing vertex labels and the third is an edge weight (a double).");
        System.out.println("* Each line represents one edge.");
        System.out.println();       
    }
    
    /**
     * Queries for user input of the graph's text file location.
     * 
     * @return String name of the text file specified by the user
     */
    private String getFileName() {
        System.out.print("Enter full path of graph text file to load: ");
        String result = inputReader.nextLine().trim();
        //if file does not end in .txt, put .txt on the end
        if (!result.toLowerCase().endsWith(".txt")) { 
            result += ".txt";
        }
        System.out.println();
        return result;
    }
}
