import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Each time a node is visited, a CostVector object is generated at that node and be used as a processing unit
 * while expanding the graph.  Essentially, it maintains a list of costs each of which represents an accumulated
 * accrued weights of an attribute up to the associated node. It is defined with a node id at where it has been
 * accrued and a reference to its predecessor CostVector and the time it has been visited that's considered as
 * a start time when it got expanded.
 *
 * @author Amin Mohammadpour
 */
public class CostVector implements Comparator<CostVector> {
    private int node_id;
    private CostVector predecessor;
    private int edge_to_successor;
    private LocalTime startTime;
    private float[] costs;
    private int next_index;

    public CostVector(int node_id, CostVector predecessor, LocalTime startTime, float[] costs) {
        this.node_id = node_id;
        this.predecessor = predecessor;
        this.startTime = startTime;
        this.costs = costs;
        this.next_index = 0;
    }

    public CostVector(int node_id, int edge_to_successor, float[] costs) {
        this.predecessor = predecessor;
        this.node_id = node_id;
        this.edge_to_successor = edge_to_successor;
        this.costs = costs;
    }

    public CostVector(float[] costs) {
        this.costs = costs;
    }

    public CostVector(int node_id, CostVector predecessor, int attributes) {
        this.node_id = node_id;
        this.predecessor = predecessor;
        this.costs = new float[attributes];
        this.next_index = 0;
    }

    public int getNode_id() {
        return node_id;
    }

    public void setNode_id(int node_id) {
        this.node_id = node_id;
    }

    public CostVector getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(CostVector predecessor) {
        this.predecessor = predecessor;
    }

    public float[] getCosts() {
        return costs;
    }

    public float getCost(int index){
        if(index > -1 && index < costs.length) {
            return costs[index];
        }
        return 0f;
    }

    /**
     * Add an accrued cost corresponding to an attribute to this CostVector. The index of this cost
     * will be corresponding to the attribute index at the passed edge.
     * @param cost the cost to be added. The travel_time cost will be represented in minutes
     */
    public void add_cost(float cost){
        costs[next_index] = cost;
        next_index++;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setCosts(float[] costs) {
        this.costs = costs;
    }

    /**
     * A cost vector is said to be dominant on other one when its attributes' weights of minimization type
     * are less than or equal to the other attributes' weights. Vise versa for the attributes of maximization
     * preference type. Note that, the order of the costs is same as the attributes order on each edge. This
     * means that, the costs corresponding to the attributes with minimization preference function are coming
     * before the ones with maximization. Basically, this function verifys the dominancy relationship of this
     * cost vector with other given cost vector.
     * @param other_cost_vector the CostVector to be check with
     * @return true if this dominate the given cost vector. False otherwise.
     */
    public boolean is_dominant_on(CostVector other_cost_vector){
        int score = 0;
        if (Constants.FIRST_MAX_ATTR >= 0) {
            for (int i = 0; i < Constants.FIRST_MAX_ATTR; i++) {
                if (costs[i] < other_cost_vector.getCosts()[i]) {
                    score++;
                }
            }
            for (int i = Constants.FIRST_MAX_ATTR; i < costs.length; i++) {
                if (costs[i] > other_cost_vector.getCosts()[i]) {
                    score++;
                }
            }
        } else {
            for (int i = 0; i < costs.length; i++) {
                if (costs[i] <= other_cost_vector.getCosts()[i]) {
                    score++;
                }
            }
        }

        if (score == costs.length) {
            return true;
        } else {
            return false;
        }
    }

    public boolean is_dominated_by(CostVector other_cost_vector){
        int score = 0;
        if (Constants.FIRST_MAX_ATTR >= 0) {
            for (int i = 0; i < Constants.FIRST_MAX_ATTR; i++) {
                if (costs[i] < other_cost_vector.getCosts()[i]) {
                    score++;
                }
            }
            for (int i = Constants.FIRST_MAX_ATTR; i < costs.length; i++) {
                if (costs[i] > other_cost_vector.getCosts()[i]) {
                    score++;
                }
            }
        } else {
            for (int i = 0; i < costs.length; i++) {
                if (costs[i] > other_cost_vector.getCosts()[i]) {
                    score++;
                }
            }
        }

        if (score == costs.length) {
            return true;
        } else {
            return false;
        }
    }

    public CostVector plus(CostVector other_cost_vector){
        float[] total_cost_vector = new float[costs.length];
        for (int i = 0; i < costs.length; i++) {
             total_cost_vector[i] = costs[i] + other_cost_vector.costs[i];
        }
        return new CostVector(this.node_id, this.predecessor, this.startTime, total_cost_vector);
    }

    public CostVector connect(CostVector other_cost_vector){
        float[] total_cost_vector = new float[costs.length];
        for (int i = 0; i < costs.length; i++) {
            total_cost_vector[i] = costs[i] + other_cost_vector.costs[i];
        }
        return new CostVector(this.node_id, this.predecessor, this.startTime, total_cost_vector);
    }

    public int getEdge_to_successor() {
        return edge_to_successor;
    }

    public void setEdge_to_successor(int edge_to_successor) {
        this.edge_to_successor = edge_to_successor;
    }

    @Override
    public int compare(CostVector o1, CostVector o2) {
        // the costs are ordered according to the user's preference
        int i = 0;
        while (i < o1.getCosts().length && o1.getCosts()[i] == o2.getCosts()[i]) {
            i++;
        }
        if (i != o1.getCosts().length) {
            return o1.getCosts()[i] > o2.getCosts()[i] ? 1 : -1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return "CostVector{" +
                "node_id=" + node_id +
                ", startTime=" + startTime +
                ", costs=" + costs +
                '}';
    }
}
