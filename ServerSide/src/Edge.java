import org.w3c.dom.Attr;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * The edge class of both TAG and ATAG. The edge is labeled with the id associated with it at the Graph class and
 * defined with a start and end node. Along with that, it is augmented with attributes set.
 * @author Amin Mohammadpour
 */
public class Edge {
    private int id;
    private int node_1_id;
    private int node_2_id;
    private Map<String , Attribute> attributes;

    public Edge (int edge_id, int node1_id, int node2_id) {
        this.id = edge_id;
        node_1_id = node1_id;
        node_2_id = node2_id;
        attributes = new HashMap<String, Attribute>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNode_1_id() {
        return node_1_id;
    }

    public void setNode_1_id(int node_1_id) {
        this.node_1_id = node_1_id;
    }

    public int getNode_2_id() {
        return node_2_id;
    }

    public void setNode_2_id(int node_2_id) {
        this.node_2_id = node_2_id;
    }

    public Map<String, Attribute> getAttributes() {
        return attributes;
    }

    /**
     * Add an attribute to this edge. As a rule, attributes with minimization preference function type
     * are added first then followed by the ones of maximization preference function type.
     * @param name the name of the attribute to be added.
     * @param attribute the Attribute object associated with the provided name to be added.
     */
    public void add_attribute(String name, Attribute attribute){
        attributes.put(name, attribute);
    }

    /**
     * Get the LocalTime object of format 'hour:minute' of the travel_time attribute at a given time slot index
     * of the day. The travel_time weight defined with a double value denoting the total minutes.
     * @param time_slot_index the index of the time slot of the day to be considered when graping the travel time
     * cost from the travel_time attributes' weight set
     * @return LocalTime object for the located travel time cost.
     */
    public LocalTime get_travel_time(int time_slot_index){
        int travel_time_in_minutes = (int) attributes.get("travel_time").get_weight(time_slot_index);
        return LocalTime.of(travel_time_in_minutes / 60 , travel_time_in_minutes % 60);
    }

    public int get_travel_time_in_minutes(int time_slot_index){
        return (int) attributes.get("travel_time").get_weight(time_slot_index);
    }

    public int get_attribute_index(String attribute_name){
        Iterator<String> iterator = attributes.keySet().iterator();
        int i = 0;
        while(iterator.hasNext()){
            if(iterator.next().equals(attribute_name)){
                return i;
            }
            i++;
        }
        return -1;
    }

    @Override
    public String toString() {
        StringBuilder st = new StringBuilder();
        st.append(node_1_id + " -> " + node_2_id);
        st.append("\t");
        Iterator<String> it = attributes.keySet().iterator();
        while(it.hasNext()){
            Attribute a = attributes.get(it.next());
            st.append(a.getName() + " " + a.getWeights());
        }
        st.append("\r\n");
        return st.toString();
    }

    public static void main(String[] args){
        float f = 1.5f;
        System.out.println(f);
        System.out.println((int)f);

    }
}

