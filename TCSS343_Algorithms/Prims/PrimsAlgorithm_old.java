import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;

/**
 * Homework 6B: Prim's Algorithm
 * 
 * TCSS 343
 * Autumn 2015
 * 27 November 2015
 * 
 * By: Jonathan Hughes, Astam Aliyev (Group 3)
 * 
 * The PrimPowerGridApp class runs Prim's Algorithm on a user specified text file 
 * representation of a graph.  This class uses a graph ADT provided by Professor Donald
 * Chinn from the University of Washington - Tacoma.
 */
public class PrimsAlgorithm_old {

    /** Scanner for console input */
    private Scanner inputReader = new Scanner(System.in);
    
    /** String for storing the fileName of the text file specifying the graph. **/
    private String fileName;
    
    /** A hash table of (String, Vertex) pairs representing the graph. */
    private Hashtable<String, Vertex> graphHashtable;
    
    /** A hash table of (String, Double) pairs representing the vertex costs. */
    private Hashtable<String, Double> costHashtable;
    
    /** A hash table of (String, Boolean) pairs representing if the vertex was checked. */
    private Hashtable<String, Boolean> checkedHashtable;
    
    /** A simpleGraph representing the graph. */
    private SimpleGraph graph;
    
    /** The double containing the total cost of the minimum spanning tree. */
    private double totalCost;
    
    /** The array of Strings, representing the order of vertices in minimum spanning tree. */
    private String[] vertexOrder;
    
    /**
     * Main method to create the PrimPowerGridApp object and run its methods.
     * @param args Command line arguments - none required.
     */
    public static void main(String[] args) {
        PrimsAlgorithm_old prims = new PrimsAlgorithm_old(); //create PrimPowerGridApp Object
    }

    /**
     * Constructs a PrimPowerGridApp object based on user input for graph text file.
     */
    public PrimsAlgorithm_old() {
        showIntro(); //show intro text
        graphHashtable = getGraphHashtable(); //create graph's hash table
        totalCost = runPrimsAlgorithm(graphHashtable);
        System.out.println();
        System.out.println("Total cost of minimum spanning tree: " + totalCost);
    }
    
    /**
     * Calculates the total cost of the graph by running Prim's Algorithm.
     * @param input the graph to get the total cost
     * @return double of the total cost
     */
    private double runPrimsAlgorithm(Hashtable<String, Vertex> input) {
        double cost = 0; //total cost of minimum starting tree starts at 0, before algorithm
        vertexOrder = new String[graphHashtable.size()];
        costHashtable = initializeCostHashtable(graphHashtable); //initialize cost hash table
        checkedHashtable = initializeCheckedHashtable(graphHashtable); //initialize checked hash table
        //get starting vertex (arbitrary starting point, whichever is first in hashtable)
        String firstLabel = (String) graphHashtable.keySet().toArray()[0]; 
        vertexOrder[0] = firstLabel; //start at the first element in the hashtable (arbitrary)
        System.out.println();
        System.out.println("State 1: Added Vertex: "+vertexOrder[0]+ ", Added Cost: 0, Current Cost: "+cost);
        //iterate Prim's algorithm for the same number of times as the there are vertices
        for (int i = 0; i < graphHashtable.size() - 1; i++) {
            //set this vertex has been checked
            checkedHashtable.put(vertexOrder[i], Boolean.TRUE); 
            //update mininum spanning tree costs to adjacent vertices
            updateFromVertex(vertexOrder[i]); 
            //get the next vertex in the minimum spanning tree
            String nextVertexLabel = getNextVertex();
            vertexOrder[i + 1] = nextVertexLabel; //store vertex label for later
            double addedCost = costHashtable.get(nextVertexLabel); //cost that vertex added
            cost += addedCost; //add vertex's cost to running cost
            System.out.println();
            System.out.println("State "+(i+2) +": Added Vertex: "+ nextVertexLabel + ", Added Cost: " + addedCost
                    + ", Current Cost: "+ cost);
        }
        return cost;      
    }
    
    /**
     * Update the costHashtable, based upon which vertex was added to the current iteration of 
     * the minimum spanning tree.  If an edge coming from that vertex has less cost than the
     * opposing vertex's current cost, lower their cost to match the edge's weight (aka cost).
     * @param vertexLabel The vertex that was added.
     */
    private void updateFromVertex(String vertexLabel) {
        //get current vertex object from the vertex's label
        Vertex currentVertex = graphHashtable.get(vertexLabel);
        //get the edges for the current vertex
        LinkedList<Edge> edgeList = currentVertex.incidentEdgeList;
        //iterate through the edge list
        for (int i = 0; i < edgeList.size(); i++) {
            Edge currentEdge = edgeList.get(i); //get edge at this index
            double edgeWeight = (double) currentEdge.getData(); //get edge weight of that edge
            //get the vertex on the other side of the edge
            Vertex otherVertex = graph.opposite(currentVertex, currentEdge);
            String otherVertexLabel = (String)otherVertex.getName();
            //if vertex has not been added to the graph yet, look at it
            if (checkedHashtable.get(otherVertexLabel).equals(Boolean.FALSE)) {
                //if the current cost of that vertex is greater than the edge weight
                if (costHashtable.get(otherVertexLabel) > edgeWeight) {
                    //change the cost of that vertex to the edge's weight (since it has less cost)
                    costHashtable.put(otherVertexLabel, edgeWeight);
                }
            }
        }
    }
    
    /**
     * Finds the vertex with the minimum cost from the current iteration of the minimum spanning
     * tree by checking all current vertex costs and ensuring the vertex was not already taken.
     * @return the vertex to choose in the Prim's iteration
     */
    private String getNextVertex() {
        String result = null;
        double min = Double.MAX_VALUE;
        Set<String> vertexLabels = checkedHashtable.keySet();
        for(String label: vertexLabels){//iterate through hash table
            //if vertex has not been already used and its cost is less than (or equal)  
            //to the current min
            if(checkedHashtable.get(label).equals(Boolean.FALSE) 
                    && costHashtable.get(label) <= min) {
                min = costHashtable.get(label); //set min cost to this cost
                result = label; //set return result to current vertex with least cost            
            }
        }
        return result;
    }

    /**
     * Creates a hash table for cost of each vertex - starting at Double.MAX_VALUE for
     * the initial value of each vertex (before start point is chosen).
     * 
     * @param input Hashtable representing the graph
     * @return hash table representing the cost for each vertex
     */
    private Hashtable<String, Double> initializeCostHashtable(Hashtable<String, Vertex> input) {
        Hashtable<String, Double> result = new Hashtable<>();
        Set<String> vertexLabels = graphHashtable.keySet();
        for(String label: vertexLabels){ //iterate through all vertices
            result.put(label, Double.MAX_VALUE); //put Double.MAX_VALUE at each vertex
        }
        return result;
    }
    
    /**
     * Creates a hash table for if each vertex is checked - starting at false for
     * the initial value of each vertex (before start point is chosen).
     * 
     * @param input Hashtable representing the graph
     * @return hash table representing the cost for each vertex
     */
    private Hashtable<String, Boolean> initializeCheckedHashtable(Hashtable<String, Vertex> input) {
        Hashtable<String, Boolean> result = new Hashtable<>();
        Set<String> vertexLabels = graphHashtable.keySet();
        for(String label: vertexLabels){ //iterate through all vertices
            result.put(label, Boolean.FALSE); //put Boolean.FALSE at each vertex
        }
        return result;
    }
    
    /**
     * Gets the Hashtable<String, Vertex> from the user input for graph text file.
     * If invalid user input is given, continually asks until valid input is given.
     * 
     * @return Hashtable<String, Vertex> of specified graph
     */
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
