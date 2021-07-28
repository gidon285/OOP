package ex0;


import java.util.*;

public class Graph_Algo implements graph_algorithms {

    private graph graph;

    /** simple constructor */


    public Graph_Algo() {
        this.graph = new Graph_DS();
    }

    /**
     * making a pointer from graph_algo a to graph_algo b
     * @param g
     */
    public Graph_Algo(graph g) {
        this.graph = g;
    }

    /** init given graph to graph_algo */
    @Override
    public void init(graph g) {
        this.graph = g;
    }

    /** this method copies a graph - i'll call it original , and returns a copy - copy.
     * first we ForEach loop on the original graph and creating new nodes as we go to be added
     * to the copy graph, then we double looping on the original graph, said we start on node A,
     * we want to make sure that we check to see all of his edges, a neighbor named B. if there is an edge
     * we connect the same A and B nodes at copy as well.
     *
     * */
    @Override
    public graph copy() {
        graph copy = new Graph_DS(); //                declaring a new graph - naming it copy.
        for (node_data n : graph.getV()) { //          forEach looping through the original graph,
            copy.addNode(n);               //           adding all his nodes to copy as New nodes.
        }
        for (node_data n : graph.getV()) {
            for (node_data n2 : graph.getV(n.getKey())) {//     Again looping through original graph, now to check edges
                if (graph.hasEdge(n.getKey(), n2.getKey())) {// when finding an edge in original, connect
                    copy.connect(n.getKey(), n2.getKey());//    at copy as well.
                }
            }
        }
        return copy;
    }

    /**
     * This method checks if a graph is connected, first we make a queue to keep tack of
     * what node we "met" or "visited". with an Iterator we get the first node (arbitrarily)
     * then we add and remove it from the queue, when removed we check the Tag on all of his neighbors,
     * at creation a node's tag is equal to 0, by that definition we know that an unvisited node's tag
     * will be 0, so we change it to 1. keeping track of how many nodes we visited by increasing an counter named
     * count. finally we reverse the process, looping around again reTagging to 0. and if the counter is equal to
     * the node size we know we visited all of the nodes through the edges and the graph is complete,
     * if the graph has 0 edges and an integer node size, then the graph in not complete.
     * @return
     */
    @Override
    public boolean isConnected() {

        // fail-safe - no edges but 1 or more nodes, not complete!
        if (graph.nodeSize() <= 1) return true;                    // if said graph has 1 or 0 nodes, its complete
        Queue<node_data> queue = new LinkedList<>();               // initializing a queue
        Iterator<node_data> iti = graph.getV().iterator();         // iti the iterator :)
        int count = 0;
        if (iti.hasNext()) {                                       // fail-safe -check if iterator has a next object.
            node_data n = iti.next();                              // pointing to the next object
            queue.add(n);                                          // enqueueing
            while (!queue.isEmpty()) {                             // while condition - if queue is not empty
                n = queue.poll();                                  // the dequeue action
                for (node_data next : graph.getV(n.getKey())) {    // ForEach looping through the neighboring nodes
                    if (next.getTag() != 1) {                      // all nodes are 0 at first, then an unvisited nodes is 0
                        queue.add(next);                           // and when visited we mark it as one.
                        next.setTag(1);                            // and increasing our count so keep track of how many
                        count++;                                   // nodes we met.
                    }
                }
            }
        }
        for (node_data temp : graph.getV()) {        // ForEach looping to set the tags back to zero, because the number
            if (temp.getTag() != 0) {                // we use is count, comparing it to the amount of nodes.
                temp.setTag(0);
            }
        }
        if (count == graph.nodeSize()) {
            return true;
        }

        return false;
    }

    /**
     * this method is part one of three functions that work together to find the shortest path between two nodes on a graph
     * this method functions as the gate keepr checking all of the worst case
     * scenarios -> (-1) is referred to index not found:
     * if the graph doesn't contains a node with the start or end key - we return -1;
     * if the graph it self is empty, meaning no nodes in it, return -1;
     * if our start node is isolated - has no neighbors(edges), return -1;
     * if our start node is isolated - has no neighbors(edges), return -1;
     * then use the method shortestPath to get the list of nodes, if said list is equal to null , return -1
     * else we return the size of the list -1 .
     * @param src - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public int shortestPathDist(int src, int dest) {
        if( src == dest)return 0;
        if ( graph.getNode(src).getNi() ==null || graph.getNode(dest) ==null )return -1;
        if (graph.getNode(src) == null || graph.getNode(dest) == null) return -1;
        if ( graph.getV().isEmpty()) return -1;
        if ( graph.getNode(src).getNi().isEmpty())return -1;
        if ( graph.getNode(dest).getNi().isEmpty()) return -1;

        List<node_data>  path = shortestPath(src,dest); // a fail-safe, if the List is empty, return -1, no path.
        if(path == null)return -1;
        return path.size()-1;
    }

    /**
     * second part of out three method system to find the shortest path between two nodes.
     * we use an Arraylist to utilize the add with an index function.
     * as a fail-safe we make an integer names suspect (sus for short) to find the priority of each node
     * in respect to the start node, this will be explained in detail in the priority method, if -1 (not found)
     * came back we return null the path doesn't exist, we first add the dest node to the Arraylist  then using a
     * boolean flag as false, as in not found yet.
     * then we start a while loop to loop through the graph starting with our dest node - this will be explained
     * at the third method, if we find a node we haven't added to the Arraylist and his tag is lower
     * then our current node we add him to the Array at index 0
     * so we can correct the order of the path, when we find the src node
     * we loop out by changing flag to ture, then we reTag all of the graph's nodes to 0, for future uses.
     * @param src - start node
     * @param dest - end (target) node
     * @return List<node_data>
     */
    @Override
    public List<node_data> shortestPath(int src, int dest) {

        ArrayList<node_data> path = new ArrayList<>();
        int  sus = prority(src,dest);                   // suspect is the number we get back
        if( sus == -1 )return null;                     // if condition to prevent errors.

        path.add(graph.getNode(dest));                  // adding our last node to the path
        boolean flag = false;                           // using a boolean primitive data type named flag to know when
        while ( !flag){                                 // we found our src node.
            node_data curr = path.get(0);
            for ( node_data next : curr.getNi()){
                if ( next.getTag() < curr.getTag() && path.get(0).getTag() != next.getTag()){
                    path.add(0, next);
                }if ( next.getKey() == src){             // looping trough dest neighbors to check if
                    flag =true;                          // their priority is the lowest
                }                                        // and making sure they haven't been added to our path array.
            }
        }
        for ( node_data temp : graph.getV()){            // reTagging all of the nodes in the graph to 0 for future use.
            temp.setTag(0);
        }
        return path;
    }

    /** our last method in the bunch of finding the shortest path among two nodes - src and dest.
     * this uses a bfs algorithm modified for our needs, as a fail-safe we loop on each node and Tagging it to 0
     * to make sure all of them are unvisited, using a queue and a List name visited to keep track on which node we met.
     * this time we start at src node, and prioritize each node we come across in his tag by the number of edges he has
     * with our src node, if we find our dest node we exit the loop, returning 1 - as in we added all the nodes we need,
     * the Array is complete we can exit the loop.
     * we keep track of how many nodes we encounter with an integer named counter and if the loop exceeds the number of
     * nodes in the graph we exit the loop and return -1.
     * @param src
     * @param dest
     * @return int
     */

    public int prority (int src, int dest){
        int count = 0;                                       // mainly a bfs algorithm, using a queue and a list,
        Queue<node_data> queue = new LinkedList<>();         // first we make sure each Tag of each node inside the
        List<node_data> visited = new LinkedList<>();        // graph is set to 0, declaring a queue and a list
        Iterator<node_data> iti = graph.getV().iterator();   // then a count to keep track, and an iterator named iti.
        node_data end = graph.getNode(dest);
        node_data curr;                                      // curr represent the current node we are looking at
        if (iti.hasNext()) {                                 // in the same way next represent the next one or a neighbor
            curr = graph.getNode(src);                       // adding the current node to the queue and the list.
            visited.add(curr);                               // the list keeps track of what node we visited,
            queue.add(curr);                                 // hence the name visited.
            while (!queue.isEmpty() && !visited.contains(end)) {
                curr = queue.poll();                         // polling the first object in the queue, and pointing
                for (iti = graph.getV(curr.getKey()).iterator(); iti.hasNext(); ) { // curr at it.
                    node_data next = iti.next();
                    if (!visited.contains(next)) {            // if we haven't visited the next node, add it to the queue
                        visited.add(next);                    // and to the list, and increase the amount of nodes we
                        queue.add(next);                      // visited.
                        count++;
                        if (next.getTag() == 0) {             // if next's tag is equal to 0 , then we increase the tag
                            int t = curr.getTag();            // by one in respect to the previous node we met.
                            next.setTag(++t);                 // this part is the heart of the processes- prioritizing
                        }                                     // all of the nodes in regards to distance from src node
                                                              // finding the dest nodes means we can stop looking

                    }
                }
            }
        }
        if(! visited.contains(end)) return -1;
         if (count == graph.nodeSize()) {                    // fail-safe if checking if we checked all of the nodes yet
            return -1;                                       // never found our destination.
        }
        return 1;
    }
}
