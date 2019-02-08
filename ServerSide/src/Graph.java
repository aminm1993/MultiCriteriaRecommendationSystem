import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * The class defining the component of TAG 
 * @author Amin Mohammadpour
 */
public class Graph {
    private Map<Integer, Node> nodes;
    private Map<Integer, Edge> edges;

    public Graph(){
        nodes = new HashMap<Integer, Node>();
        edges = new HashMap<Integer, Edge>();
    }

    public Graph(Map<Integer, Node> nodes, Map<Integer, Edge> edges) {
        this.nodes = nodes;
        this.edges = edges;
    }

    /**
     * Find the Node object associated with the given id
     * @param id a node id
     * @return Node object corresponding to the given id
     */
    public Node get_node(int id){
        return nodes.get(id);
    }

    /**
     * Add a given Node to the graph associated with a given node id given along with it.
     * @param id the id of the new node that's going to be added to the garph
     * @param node the Node object need to be added to the graph
     */
    public void add_node(int id, Node node) { nodes.put(id, node);}

    /**
     * Find the Edge object associated with a given edge id
     * @param id the edge id to find
     * @return the Edge object corresponding to the given edge id
     */
    public Edge get_edge(int id){
        return edges.get(id);
    }

    /**
     * Add a given edge to the graph associated with the given id. Then, add this edge as an out-going edge to its
     * start node and as in-going edge to it end node.
     * @param id the edge id to be associated to the given edge that's going to be add to the graph
     * @param edge the Edge object to be add to the graph
     */
    public void add_edge(int id, Edge edge){
        edges.put(id, edge);
        nodes.get(edge.getNode_1_id()).add_out_edge(id);
        nodes.get(edge.getNode_2_id()).add_in_edge(id);
    }

    public Map<Integer, Edge> getEdges() {
        return edges;
    }

    public void setEdges(Map<Integer, Edge> edges) {
        this.edges = edges;
    }

    public Map<Integer, Node> getNodes() {
        return nodes;
    }

    public void setNodes(Map<Integer, Node> nodes) {
        this.nodes = nodes;
    }

    public ArrayList<Node> get_adjacent_nodes(int node_id){
        ArrayList<Node> adjacent_nodes = new ArrayList<Node>();
        for(Integer e_id : get_node(node_id).getOut_edges()){
            adjacent_nodes.add(get_node(get_edge(e_id).getNode_2_id()));
        }
        return adjacent_nodes;
    }

    public void optimize_nodes_size(){
        for (int i = 0; i < nodes.size(); i++) {
            nodes.get(i).getIn_edges().trimToSize();
            nodes.get(i).getOut_edges().trimToSize();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("nodes: \n");
        Iterator<Integer> it = nodes.keySet().iterator();
        while(it.hasNext()) {
            Integer node_id = it.next();
            sb.append(node_id);
            if (it.hasNext())
                sb.append(", ");
        }
        sb.append("\n\nedges: \n");
        it = edges.keySet().iterator();
        while(it.hasNext()) {
            Integer edge_id = it.next();
            sb.append(edge_id);
            if (it.hasNext())
                sb.append(", ");
        }
        return sb.toString();
    }
}
