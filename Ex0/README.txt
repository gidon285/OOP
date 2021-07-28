
                                                                     
                                                                      
            this methods all sorund the unwigthed                     
             undirected graph. including its nodes,                    
           edges verdabres etc.                                      
                                                                      
                                                                     
 ---------------------------------------------------------                                                                      


What do we acutal have here? :
each graph is made of nodes, edges a HashMap and mode count.
each node contains a key (unique) , a Tag, info String, and a HashMap.

Data structurs:
HashMap- Each node contains a diffrent Hasmap, why HashMap?
HashMap can be used in a way that benifets this kind of programing,
using the HashMap Builder we can create a map with a unique key(later to be explained)
that is also used to mark each and evry node.
further more each graph has a unique key, that and more helps us save on valuble 
run time.
Algorithms:
BFS - the main algorithm in Graph_algo, this is a travel Algoritem desind to spread across
the whole graph, basiclly it works like a spread, given a start node, we check his adjesent
nodes, if we havent visited them we insert them to a queue that keep tracks of last one we
enqueued.
 
each * will be explained in the end of the segment.


Three class inside this folder:

1. NodeData
2. Graph_Ds
3. Graph_algo


NodeData:
Info : reffers to the info contained by the node.
Key: an integer unique to each node.
toatolkey : an integer that represent the number of keys made
niberhood: the HashMap of each individial node. 

NodeData is pritty staight forward, there are some ifs to check
the user doesn't make an error (or to check ourself) but the code is very detaild.

Graph_DS:
vertebrae : reffers to the number of nodes in a graph.
edges* : reffers to the number of links* between all of the graph's nodes.
guts : reffersto the acutal Data structure (Hashmap) that holds the inside of a graph.
modecunt :reffers to the number of changes made in a grpah.

Important "ifs" in Graph_DS:
checks to se if this graph contains the spesific nodes were looking for
if not does nothing.
this is a general if that comes back a lot, from now on each method has it.

1.HasEdge:I-if- else add node A to node B's Hashmap.

2.addNode:I-if- else put( Hashmap function to add) the node we got n with his key.

3.connect*:I-if- else adds node A to node B's Hashmap and the other way around*. 

4.removeNode:I-if- else continue, loops on all of curr (current) node Hashmap,

for Each node in there removes current from the HashMap. in the prossece lowering the

number of edges by one.

5. removeEdge:I-if-else if Node A is in B's HashMap remove him, and the same

the other way around.

* edge - defenetion, Let A,B be two nodes in Graph G.
A and B have an edge between them, if and only if B is present inside of A's Hashmap,
and A is present inside of B's HashMap.

Graph_algo:
1.copy: first adds all of the given graph's nodes and adds them to the copy node,

the loops again and check if in the original graph any two nodes A and B has an edge

if they do connect(A,B)

then return the copy.

2.isConnected ; uses a bfs search to go trough each and every node inside of the graph.

when entered changes the tag of a given node to 1, and keep tracks of how many

nodes we visited.

3.shortestPathDist: this method is a gate way to ShortestPath, this return the sieze of

the pathc from shortestPath -1, and checks if the graph is not connected.

4.shorthestPath: this method gets its graph modified so each node will hold his priority 

in respect to the src node, which is the start. and here we start at the end of the given graph,

adding the last node to the first position. always to the first position so we get the 

correct order.

5. Priority: this is the bfs method, that prioritize each node with his distance (by edges)

to the start node, this is a void method because the graph is alredy defined.

