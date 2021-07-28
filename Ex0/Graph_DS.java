package ex0;


import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;


public class Graph_DS implements graph{
    private int vertebrae;                          // the amount of nodes inside of the graph.
    private int edges;                              // the amount of edges inside the graph.
    private HashMap<Integer,node_data> guts;        // the data structure that will contain all of the nodes.
    private  int modecunt;                          // the mode count - keeping track of how many changes were made
                                                    // to the graph.

    /**
     * a simple constructor of the graph, sets the number of nodes edges and mode count to 0
     * and making a new Hashmap to contain the nodes.
     */
    public Graph_DS(){
        vertebrae =0;
        edges = 0;
        guts = new HashMap<>();
        modecunt = 0;
    }

    /**
     * using Hashmap's built in function get, using the given key.
     * @param key - the node_id
     * @return node
     */
    @Override
    public node_data getNode(int key) {
       return guts.get(key);
    }

    /**
     * this method checks if node1 = A Hashmap's contains node2 = B key's.
     * @param node1
     * @param node2
     * @return boolean
     */
    @Override
    public boolean hasEdge(int node1, int node2) {
        if( !guts.containsKey(node1) || !guts.containsKey(node2)) return false;
        node_data a = guts.get(node1);                                         // fail-safe if the graph's Hashmap
        node_data b = guts.get(node2);                                         // doesn't contain either one of the nodes
        if( a.getNi().contains(b) && b.getNi().contains(a)) {                  // we return false else we check, and return the answer.
            return true;
       }
        return false;
    }

    /**
     * this method adds a node = n to this graph.
     * @param n
     */
    @Override
    public void addNode(node_data n) {
        if( ! guts.containsKey(n.getKey())){        // fail-safing - if the graph doesn't contain n's key, then add it.
            this.guts.put(n.getKey(),n);            // each key is unique!
            this.vertebrae++;
            this.modecunt++;
        }
    }

    /**
     * this method connects an edge between two nodes, adding to the edges integer and mode count.
     * using pointers a and b to be more visual.
     * @param node1
     * @param node2
     */
    @Override
    public void connect(int node1, int node2) {
        if(node1 == node2) return;                                         // if the keys are the same , nothing to connect.
        if ( !guts.containsKey(node1) || !guts.containsKey(node2))return;  // again  if the graph doesn't contain
        node_data a = guts.get(node1);                                     // the given nodes, nothing to connect.
        node_data b = guts.get(node2);
        if (a.getNi().contains(b) || b.getNi().contains(a))return;         // if a's Hashmap contains b's key, they are
            if( ! a.getNi().contains(b)){                                  // already connected, same check the other way
                a.addNi(b);                                                // finally adding the two nodes to each other
                b.addNi(a);                                                // Hashmaps, then increasing the number of edges
            }                                                              // and the mode count by 1.
        edges++;
        modecunt++;

    }

    /**
     * return a pointer to this graph's Hashmap in the figure of a collection.
     * @return
     */
    @Override
    public Collection<node_data> getV() {
        return this.guts.values();      // using Hashmap built in method .values() .
    }

    /**
     * return pointer to a specific node's Hashmap, by checking his key.
     * @param node_id
     * @return
     */
    @Override
    public Collection<node_data> getV(int node_id) {
        return this.guts.get(node_id).getNi();                              // using the get build in function.
    }

    /**
     * removing a node from his graph, while making sure we delete the deleted node from
     * all of his connected nodes as well.
     * @param key
     * @return
     */
    @Override
    public node_data removeNode(int key) {
       if ( ! guts.containsKey(key)) return null;                      // making sure the node we want to remove belongs
       node_data curr = guts.get(key);                                 // to this graph, then ForEach looping on all
       for(node_data next : curr.getNi()){                             // of his surrounding removing current from
               next.getNi().remove(curr);                              // neighboring nodes decreasing the edges size
               modecunt++;                                             // by one, then finally removing the given key
               edges--;                                                // from the graph, decreasing the number of nodes
       }                                                               // and decreasing the number of mode count.
       guts.remove(curr.getKey());
       vertebrae--;
       modecunt++;
       return curr;
    }
    @Override
    /**
     * this method removes the edges between two given nodes byt getting their node key, and checking
     * if the graph contains them, and if they contain eachother.
     */
    public void removeEdge(int node1, int node2) {
        if ( !guts.containsKey(node1) || !guts.containsKey(node2))return;       // same fail-safe.
        node_data a = guts.get(node1);                                          // making two pointers a and b.
        node_data b = guts.get(node2);

        if( a.getNi().contains(b)){                                             // making sure they contain each other.
            a.removeNode(b);
            b.removeNode(a);
        }else return;                                                           // else no edge, nothing to remove
        edges--;                                                                // finally decreasing edges size, and
        modecunt++;                                                             // increasing mode count.
    }

    @Override
    public int nodeSize() {
        return this.vertebrae; // return number of nodes in this graph.
    }

    @Override
    public int edgeSize() {
        return this.edges;    // return number of edges in this graph.
    }

    @Override
    public int getMC() {      // return number of changes done to this graph.
        return this.modecunt;
    }

}