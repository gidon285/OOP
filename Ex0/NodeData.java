package ex0;

import java.util.*;

public class NodeData implements node_data{

    private String info;                            // each node contains:
    private int  key, tag;                          // a String to hold its info, his unique key, tag, and Hashmap
    private static int totalkey ;                   // totalkey is to keep track of how many keys we name and its static
    private HashMap<Integer,node_data> niberhood;   // to prevent two nodes having the same key.

    /**
     * a simple constructor to create a new node, keeping the key diffrent in each node.
     */
    public  NodeData (){
        this.info=null;
        this.tag=0;
        this.key = totalkey++;
        this.niberhood= new HashMap<Integer, node_data>();
    }

    /**
     * a constructor to create a node with a given key .
     * @param key
     */
    public NodeData(int key){
        this.key = key;
        this.info = this.getInfo();
        this.tag = this.getTag() ;
        this.niberhood = new HashMap<Integer, node_data>();
    }

    @Override
    public int getKey() {
        return this.key;    // returns this node's key.
    }

    @Override
    public Collection<node_data> getNi() {
        return this.niberhood.values(); // using Hashmap build in function .values() to return a Collection.
    }

    @Override
    public boolean hasNi(int key) {
       if(this.niberhood.containsKey(key))return true; // if this node's Hashmap contains the given key
       else return false;                              // then return true, else return false.

    }

    @Override
    public void addNi(node_data t) {
        this.niberhood.put(t.getKey(),t);              // adding a given node t to this node's Hashmap.
    }

    @Override
    public void removeNode(node_data node) {           // removing a given node node from this node's Hashmap
            niberhood.remove(node.getKey());
    }

    @Override
    public String getInfo() {                          // returning this node's info
        return info;
    }

    @Override
    public void setInfo(String s) {                    // setting this node's info as String s
        this.info = s;
    }

    @Override
    public int getTag() {                               // returning this node's Tag
        return tag;
    }

    @Override
    public void setTag(int t) {                         // setting this node's Tag as int t
        tag = t;
    }

}