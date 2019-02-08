import java.util.ArrayList;
import java.util.Iterator;

/**
 * This defines the node type of both TAG and ATAG. It is labeled with an id similar to its corresponding one at the Graph
 * it belongs to and defined with a GPS location values (latitude and longitude). It maintains also a list of the
 * in-going and out-going edges' ids.
 * @author Amin Mohammadpour
 */
public class Node {
	private int id;
	private double lat;
	private double lng;
    private ArrayList<Integer> out_edges, in_edges;
	
	public Node(int id, double lat, double lng) {
		this.id = id;
		this.lat = lat;
		this.lng = lng;
        out_edges = new ArrayList<Integer>();
        in_edges = new ArrayList<Integer>();
	}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    /**
     * Add an out-going edge's index to this node.
     * @param edge_id the id of the out-going edge.
     */
    public void add_out_edge(int edge_id){
        out_edges.add(edge_id);
    }

    /**
     * Add an in-going edge's index to this node.
     * @param edge_id the id of the in-going edge.
     */
    public void add_in_edge(int edge_id){
        in_edges.add(edge_id);
    }

    public ArrayList<Integer> getOut_edges() {
        return out_edges;
    }

    public ArrayList<Integer> getIn_edges() {
        return in_edges;
    }

    @Override
    public String toString() {
        return id + " (" + lat + ", " + lng + ")";
    }
	
}
