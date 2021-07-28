package api;

import com.google.gson.*;
import org.w3c.dom.Node;

import java.io.*;
import java.util.*;

public class DWGraph_Algo implements dw_graph_algorithms {
    directed_weighted_graph _algo_graph = new DWGraph_DS();
    static HashSet<node_data> _scc_stack = new HashSet<>();

    public DWGraph_Algo() {
        _algo_graph = null;
    }

    public DWGraph_Algo(directed_weighted_graph graph) {
        _algo_graph = graph;
    }

    /**
     * Init the graph on which this set of algorithms operates on.
     *
     * @param g
     */
    @Override
    public void init(directed_weighted_graph g) {
        this._algo_graph = g;
    }

    /**
     * Return the underlying graph of which this class works.
     *
     * @return
     */
    @Override
    public directed_weighted_graph getGraph() {
        return this._algo_graph;
    }

    /**
     * Compute a deep copy of this weighted graph.
     *
     * @return
     */
    @Override
    public directed_weighted_graph copy() {
        directed_weighted_graph result = new DWGraph_DS();
        if (this._algo_graph.nodeSize() == 0) return result;
        for (node_data n : this._algo_graph.getV()) {
            result.addNode(n);
        }
        if (this._algo_graph.edgeSize() == 0) return result;
        for (node_data n : this._algo_graph.getV()) {
            for (edge_data e : this._algo_graph.getE(n.getKey())) {
                result.connect(e.getSrc(), e.getDest(), e.getWeight());
            }
        }
        return result;
    }

    /**
     * Returns true if and only if (iff) there is a valid path from each node to each
     * other node. NOTE: assume directional graph (all n*(n-1) ordered pairs).
     *
     * @return
     */
    @Override
    public synchronized boolean isConnected() {
        if (this._algo_graph.nodeSize() <= 1 || this._algo_graph == null) return true;
        Queue<Integer> queue = new LinkedList<>();
        HashSet<Integer> visited = new HashSet<>();
        int tmp_node = this._algo_graph.getV().iterator().next().getKey();
        int test_node = tmp_node;
        queue.add(tmp_node);
        visited.add(tmp_node);
        while (!queue.isEmpty()) {
            tmp_node=queue.poll();
            for (edge_data edge : this._algo_graph.getE(tmp_node)) {
                if (!visited.contains(edge.getDest())) {
                    visited.add(edge.getDest());
                    queue.add(edge.getDest());
                }
            }
        }
        if (visited.size() != this._algo_graph.nodeSize()) return false;
        directed_weighted_graph reversed_graph = new DWGraph_DS();
        for (node_data n : this._algo_graph.getV()) {
            reversed_graph.addNode(n);
        }
        for (node_data n : this._algo_graph.getV()) {
            for (edge_data e : this._algo_graph.getE(n.getKey())) {
                reversed_graph.connect(e.getDest(),e.getSrc(),e.getWeight());
            }
        }
        queue.clear();
        visited.clear();
        tmp_node = test_node;
        queue.add(test_node);
        visited.add(tmp_node);
        while (!queue.isEmpty()) {
            tmp_node=queue.poll();
            for (edge_data edge : reversed_graph.getE(tmp_node)) {
                if (!visited.contains(edge.getDest())) {
                    visited.add(edge.getDest());
                    queue.add(edge.getDest());
                }
            }
        }
        if (visited.size() != reversed_graph.nodeSize()) return false;
        else return true;
    }

    /**
     * returns the length of the shortest path between src to dest
     * Note: if no such path --> returns -1
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public synchronized double shortestPathDist(int src, int dest) {
        if (this._algo_graph.nodeSize()<=1 || this._algo_graph.getNode(src)==null || this._algo_graph.getNode(dest)==null) return -1;
        if (src == dest) return 0.0;
        PriorityQueue<node_data> pq = new PriorityQueue<>();
        double result;
        node_data current = this._algo_graph.getNode(src);
        pq.add(current);
        current.setTag(0);
        while (!pq.isEmpty()) {
            current = pq.poll();
            if (!Objects.equals(current.getInfo(), "y")) {
                current.setInfo("y");
                if (current.getKey() == dest) break;
                for (edge_data e : this._algo_graph.getE(current.getKey())) {
                    if (this._algo_graph.getNode(e.getDest()).getTag() == -1) {
                        this._algo_graph.getNode(e.getDest()).setTag(Integer.MAX_VALUE);
                    }
                    double tmp_tag = current.getTag()+this._algo_graph.getEdge(current.getKey(),e.getDest()).getWeight();
                    if (tmp_tag < this._algo_graph.getNode(e.getDest()).getTag()) {
                        this._algo_graph.getNode(e.getDest()).setTag((int) tmp_tag);
                        pq.add(this._algo_graph.getNode(e.getDest()));
                    }
                }
            }
        }
        current = this._algo_graph.getNode(dest);
        result = current.getTag();
        if (!Objects.equals(current.getInfo(), "y")) {
            this.reset();
            return -1;
        }
        this.reset();
        return result;
    }

    /**
     * returns the the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest
     * see: https://en.wikipedia.org/wiki/Shortest_path_problem
     * Note if no such path --> returns null;
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public synchronized List<node_data> shortestPath(int src, int dest) {
        if (this._algo_graph.nodeSize()<=1 || this._algo_graph.getNode(src)==null || this._algo_graph.getNode(dest)==null) return null;
        if (src == dest) return new ArrayList<>();
        PriorityQueue<node_data> pq = new PriorityQueue<>();
        List<node_data> result = new ArrayList<>();
        HashMap<node_data,node_data> parent = new HashMap<>();
        node_data current = this._algo_graph.getNode(src);
        pq.add(current);
        current.setTag(0);
        while (!pq.isEmpty()) {
            current = pq.poll();
            if (!Objects.equals(current.getInfo(),"y")) {
                current.setInfo("y");
                if (current.getKey() == dest) break;
                for (edge_data e : this._algo_graph.getE(current.getKey())) {
                    if (this._algo_graph.getNode(e.getDest()).getTag() == -1) {
                        this._algo_graph.getNode(e.getDest()).setTag(Integer.MAX_VALUE);
                    }
                    double tmp_tag = current.getTag()+this._algo_graph.getEdge(current.getKey(),e.getDest()).getWeight();
                    if (tmp_tag < this._algo_graph.getNode(e.getDest()).getTag()) {
                        this._algo_graph.getNode(e.getDest()).setTag((int) tmp_tag);
                        parent.put(this._algo_graph.getNode(e.getDest()),current);
                        pq.add(this._algo_graph.getNode(e.getDest()));
                    }
                }
            }
        }
        current = this._algo_graph.getNode(dest);
        if (!Objects.equals(current.getInfo(), "y")) {
            this.reset();
            return null;
        }
        result.add(0,current);
        while (current.getKey() != src) {
            result.add(0,parent.get(current));
            current = parent.get(current);
        }
        this.reset();
        return result;
    }

    /**
     * Saves this weighted (directed) graph to the given
     * file name - in JSON format
     *
     * @param file - the file name (may include a relative path).
     * @return true - iff the file was successfully saved
     */
    @Override
    public boolean save(String file) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        ArrayList<edge_data> graph_edges = new ArrayList<>();
        String[] nodes_arr = new String[this._algo_graph.getV().size()];
        int index = 0;
        for (node_data n :this._algo_graph.getV()) {
            nodes_arr[index] = "{\"pos\":\""+gson.toJson(n.getLocation())
                    .replaceAll("\\{\"x\":","")
                    .replaceAll("\"y\":","")
                    .replaceAll("\"z\":","")
                    .replaceAll("}","")+"\",\"id\":"+n.getKey()+"}";
            index++;
        }
        for (node_data n :this._algo_graph.getV()) {
            graph_edges.addAll(this._algo_graph.getE(n.getKey()));
        }
        String result = "{\"Edges\":"+gson.toJson(graph_edges)
                +",\"Nodes\":"+Arrays.toString(nodes_arr).replaceAll(" ","")+"}";
        System.out.println(result);
        try {
            FileWriter output = new FileWriter(file);
            output.write(result);
            output.flush();
        } catch (IOException e) {
            System.out.println("Wrong Input");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     *
     * @param file - file name of JSON file
     * @return true - iff the graph was successfully loaded.
     */
    @Override
    // TODO What we loaded here is the absolute minimum, we can/should load more data
    public boolean load(String file) {
        boolean flag = false;
        try {
            File fi = new File(file);
            directed_weighted_graph result = new DWGraph_DS();
            JsonElement file_element = JsonParser.parseReader(new FileReader(fi));
            JsonObject file_object = file_element.getAsJsonObject();
            JsonArray edges_arr = file_object.get("Edges").getAsJsonArray();
            JsonArray nodes_arr = file_object.get("Nodes").getAsJsonArray();
            for (JsonElement node : nodes_arr) {
                JsonObject node_object = node.getAsJsonObject();
                Integer key = node_object.get("id").getAsInt();
                String[] tmp_str = node_object.getAsJsonPrimitive("pos").getAsString().split(",");
                Double[] pos_arr = new Double[tmp_str.length];
                for (int i=0; i<tmp_str.length; i++) {
                    pos_arr[i] = Double.parseDouble(tmp_str[i]);
                }
                node_data tmp_node = new NodeData(key);
                geo_location tmp_location = new GeoLocation(pos_arr[0],pos_arr[1],pos_arr[2]);
                tmp_node.setLocation(tmp_location);
                result.addNode(tmp_node);
            }
            for (JsonElement edge : edges_arr) {
                JsonObject edge_object = edge.getAsJsonObject();
                Integer src = edge_object.get("src").getAsInt();
                Integer dest = edge_object.get("dest").getAsInt();
                Double weight = edge_object.get("w").getAsDouble();
                result.connect(src,dest,weight);
            }
            this._algo_graph = result;
            flag = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * Used to reset the tags and metadata of each node after finishing
     * BFS and Dijkstra's algorithms
     */
    public void reset() {
        for (node_data n : this._algo_graph.getV()) {
            if (n.getTag() != 0 || n.getInfo() != null) {
                n.setTag(-1);
                n.setInfo(null);
            }
        }
    }

    /**
     * This method overrides the equals method from Object interface.
     * It is used for graph comparing. Used vastly in testing and debugging.
     * It uses the toString method which is override as well for this project.
     *
     * For more information and for the full doc:
     * https://docs.oracle.com/javase/7/docs/api/java/lang/Object.html
     *
     * @param obj the reference object with which to compare.
     * @return boolean - if this object is the same as the obj
     */
    @Override
    public boolean equals(Object obj) {
        directed_weighted_graph g_obj = (DWGraph_DS) obj;
        if (_algo_graph.edgeSize() != g_obj.edgeSize() || _algo_graph.nodeSize() != g_obj.nodeSize()) return false;
        return Objects.equals(_algo_graph.toString(), g_obj.toString());
    }

    /**
     * Returns the graph connected components
     *
     * @return List of connected components list's
     */
    public ArrayList<ArrayList> scc() {
        ArrayList<ArrayList> result = new ArrayList<>();
        if (this._algo_graph.nodeSize() == 0) {
            return result;
        }
        _scc_stack.clear();
        for (node_data node : this._algo_graph.getV()) {
            if (!_scc_stack.contains(node)) {
                ArrayList<Integer> tmp_list = scc_bfs(node.getKey());
                result.add(tmp_list);
                for (Integer nk : tmp_list) {
                    _scc_stack.add(this._algo_graph.getNode(nk));
                }
            }
        }
        return result;
    }

    /**
     * Returns true if and only if (iff) there is a valid path from each node to each
     * other node. NOTE: assume directional graph (all n*(n-1) ordered pairs).
     *
     * @return
     */
    public ArrayList<Integer> scc_bfs(int node) {
        Queue<Integer> queue = new LinkedList<>();
        HashSet<Integer> visited = new HashSet<>();
        ArrayList<Integer> result = new ArrayList<>();
        ArrayList<Integer> list1 = new ArrayList<>();
        ArrayList<Integer> list2 = new ArrayList<>();
        int tmp_node = this._algo_graph.getV().iterator().next().getKey();
        int test_node = tmp_node;
        queue.add(tmp_node);
        visited.add(tmp_node);
        if (this._algo_graph.getE(node).size() == 0) {
            return result;
        }
        while (!queue.isEmpty()) {
            tmp_node=queue.poll();
            for (edge_data edge : this._algo_graph.getE(tmp_node)) {
                if (!visited.contains(edge.getDest())) {
                    visited.add(edge.getDest());
                    queue.add(edge.getDest());
                }
            }
        }
        directed_weighted_graph reversed_graph = new DWGraph_DS();
        for (node_data n : this._algo_graph.getV()) {
            if (n.getInfo() == "y") {
                list1.add(n.getKey());
            }
            reversed_graph.addNode(n);
        }
        for (node_data n : this._algo_graph.getV()) {
            for (edge_data e : this._algo_graph.getE(n.getKey())) {
                reversed_graph.connect(e.getDest(),e.getSrc(),e.getWeight());
            }
        }
        if (reversed_graph.getE(node).size() == 0) {
            return result;
        }
        queue.clear();
        visited.clear();
        tmp_node = test_node;
        queue.add(test_node);
        visited.add(tmp_node);
        while (!queue.isEmpty()) {
            tmp_node=queue.poll();
            for (edge_data edge : reversed_graph.getE(tmp_node)) {
                if (!visited.contains(edge.getDest())) {
                    visited.add(edge.getDest());
                    queue.add(edge.getDest());
                }
            }
        }
        for (node_data nnn : reversed_graph.getV()) {
            if (nnn.getInfo() == "y") {
                list2.add(nnn.getKey());
            }
        }
        list1.retainAll(list2);
        return list1;
    }
}
