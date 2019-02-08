import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.*;

/**
 * This is an implementation for the Best start time finder algorithm as per SP-TAG Best-Star idea.
 * @author Amin Mohammadpour
 */
public class Best_Start_Time_2 {
    /**
     * Find the best start time that can achieve the most optimal route w.r.t all the specified attributes in the given graph
     * @param s the source node id
     * @param d the destination node id
     * @param time_slots_of_day the time periods/slots distribution over a day
     * @param selected_attributes the user's preferred preferences
     */
    public static boolean find_best_start_time(final Graph G, final Time_Slots_of_Day time_slots_of_day, final int s,
                                               final int d, final String [] selected_attributes) {
        // start-bounding-box-definition: determining the search scope by defining the bounding box
        double min_lat, min_lng, max_lat, max_lng;
        Node node_s = G.get_node(s);
        Node node_d = G.get_node(d);
        min_lat = Math.min(node_s.getLat(), node_d.getLat());
        max_lat = Math.max(node_s.getLat(), node_d.getLat());
        min_lng = Math.min(node_s.getLng(), node_d.getLng());
        max_lng = Math.max(node_s.getLng(), node_d.getLng());
        double distance = distance(node_s.getLat(), node_s.getLng(), node_d.getLat(), node_d.getLng(), "K");
//        double factorized_distance = distance * 0.5;
        double factorized_distance = 10.0;
        Bound bound = new Bound(move(new GPSPoint(min_lat, min_lng,null), factorized_distance, 225),
                move(new GPSPoint(max_lat, max_lng), factorized_distance, 45));
        // end-bounding-box-definition

        ArrayList<CostVector> solution_set = new ArrayList<CostVector>();
        Map<Integer, Map<Integer, ArrayList<CostVector>>> M = new HashMap<Integer, Map<Integer, ArrayList<CostVector>>>();
        ArrayList<Integer> PQ = new ArrayList<Integer>();
        float[] costs = new float[selected_attributes.length];
        for (int i = 0; i < selected_attributes.length; i++) {
            costs[i] = 0f;
        }
        CostVector cost_vector, cv;
        M.put(s, new HashMap<Integer, ArrayList<CostVector>>());
        for (Integer time_slot_index : time_slots_of_day.getTime_slots().keySet()) {
            cost_vector = new CostVector(s, null, null, costs);
            cost_vector.setStartTime(time_slots_of_day.getTime_slots().get(time_slot_index).getFrom());
            M.get(s).put(time_slot_index, new ArrayList<CostVector>(Arrays.asList(cost_vector)));
        }
        PQ.add(s);
        int settled_node_id, successor_node_id, attribute_index;
        float aggregated_cost, travel_time;
        ArrayList<CostVector> settled_cost_vectors = null;
        Edge out_edge;
        Iterator<CostVector> iterator;
        Node node;
        boolean dominated_by_solution_set, dominated_by_node_cvs;
        while (!PQ.isEmpty()) {
            settled_node_id = PQ.remove(0);
            // iterate over the out-going edges of the current node to continue branching to its successors
            for (Integer out_edge_index : G.get_node(settled_node_id).getOut_edges()) {
                out_edge = G.get_edge(out_edge_index);
                successor_node_id = out_edge.getNode_2_id();
                node = G.get_node(successor_node_id);
                if (bound.contains(node.getLat(), node.getLng())){
                    // generate cost_vector per each successor as many as the defined time slots of the day
                    for (Integer time_slot_index : time_slots_of_day.getTime_slots().keySet()) {
                        try {
                            settled_cost_vectors = M.get(settled_node_id).get(time_slot_index);
                        } catch (Exception ex) {}
                        if (settled_cost_vectors != null && settled_cost_vectors.size() > 0) {
                            for (CostVector settled_cost_vector : settled_cost_vectors) {
                                dominated_by_node_cvs = false;
                                cost_vector = new CostVector(successor_node_id, settled_cost_vector, selected_attributes.length);
                                attribute_index = 0;
                                for (String attribute_name : selected_attributes) {
                                /*aggregated_cost = non_dominated_cv.getCosts().get(attribute_index);
                                for (int time_slot_index : overlapping_distribution.keySet()) {
                                    aggregated_cost += out_edge.getAttributes().get(attribute_name).get_weight(time_slot_index) *
                                            overlapping_distribution.get(time_slot_index));
                                }*/
                                    try {
                                        aggregated_cost = settled_cost_vector.getCosts()[attribute_index] +
                                                out_edge.getAttributes().get(attribute_name).get_weight(time_slot_index);
                                        cost_vector.add_cost(aggregated_cost);
                                        attribute_index++;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                /* set the start time of the cost vector to be equal to its predecessor's start time plus
                                   the travel time. */
                                travel_time = out_edge.getAttributes().get("travel_time").get_weight(time_slot_index);
                                cost_vector.setStartTime(settled_cost_vector.getStartTime().
                                        plusHours((long) (travel_time / 60)).
                                        plusMinutes((long) (travel_time % 60)));
                                time_slot_index = time_slots_of_day.get_time_slot_index(cost_vector.getStartTime());
                                if (successor_node_id == d) {
                                    // in case of visiting the destination node
                                    dominated_by_solution_set = false;
                                    iterator = solution_set.iterator();
                                    while (iterator.hasNext()) {
                                        cv = iterator.next();
                                        if (cost_vector.is_dominant_on(cv)) {
                                            iterator.remove();
                                        } else if (cv.is_dominant_on(cost_vector)) {
                                            dominated_by_solution_set = true;
                                            break;
                                        }
                                    }
                                    if (!dominated_by_solution_set) {
                                        solution_set.add(cost_vector);
                                        // prone the M set
                                        /*for (Integer node_id : M.keySet()){
                                            for(Integer time_index : M.get(node_id).keySet()) {
                                                iterator = M.get(node_id).get(time_index).iterator();
                                                while (iterator.hasNext()) {
                                                    if (cost_vector.is_dominant_on(iterator.next())) {
                                                        iterator.remove();
                                                    }
                                                }
                                            }
                                        }*/
                                    }
                                } else {
                                    // accrued at some node between s-d
                                    dominated_by_solution_set = false;
                                    for (int i = 0; i < solution_set.size(); i++) {
                                        if (solution_set.get(i).is_dominant_on(cost_vector)) {
                                            dominated_by_solution_set = true;
                                            break;
                                        }
                                    }
                                    /* If it is found as not dominated, then proceed to verify its dominance relationship
                                       with the recorded non-dominated cost vectors accrued at the same node it belongs to.
                                       It will be add to the non-dominated set if dominates any one and all the dominated
                                       ones will be removed. */
                                    if (!dominated_by_solution_set) {
                                        if (M.containsKey(successor_node_id)) {
                                            if (M.get(successor_node_id).containsKey(time_slot_index)) {
                                                iterator = M.get(successor_node_id).get(time_slot_index).iterator();
                                                while (iterator.hasNext()) {
                                                    cv = iterator.next();
                                                    if (cv.is_dominant_on(cost_vector)) {
                                                        dominated_by_node_cvs = true;
                                                        break;
                                                    } else if (cost_vector.is_dominant_on(cv)) {
                                                        iterator.remove();
                                                    }
                                                }
                                                if (!dominated_by_node_cvs) {
                                                    M.get(successor_node_id).get(time_slot_index).add(cost_vector);
                                                }
                                            } else {
                                                M.get(successor_node_id).put(time_slot_index, new ArrayList<CostVector>(
                                                        Arrays.asList(cost_vector)));
                                            }
                                        } else {
                                            M.put(successor_node_id, new HashMap<Integer, ArrayList<CostVector>>());
                                            M.get(successor_node_id).put(time_slot_index, new ArrayList<CostVector>(
                                                    Arrays.asList(cost_vector)));
                                        }
                                        if (!PQ.contains(successor_node_id) && ! dominated_by_node_cvs) {
                                            PQ.add(successor_node_id);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if(solution_set.size() > 0){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * calculate the distance in the specified unit between two GPS points
     * @param lat1 first gps point's latitude
     * @param lon1 first gps point's longitude
     * @param lat2 second gps point's latitude
     * @param lon2 second gps point's longitude
     * @param unit distance measurement unit {K, N, M}
     * @return distance in the specified unit
     */
    public static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        double mile = dist * 60 * 1.1515;
        if (unit == "K") {
            dist = mile * 1.609344;
        } else if (unit == "N") {
            dist = mile * 0.8684;
        } else if (unit == "M") {
            dist = mile;
        }
        return (dist);
    }

    /**
     * compute the new location of the given point after performing the transaction in the direction with the specified bearing degree
     * @param p GPS point
     * @param d distance of how much p need to be moved
     * @param bearingInDegree movement direction angle
     * @return GPS point instance of p after movement
     */
    public static GPSPoint move(GPSPoint p, double d, int bearingInDegree) {
        double R = 6378.1; //Radius of the Earth
        double bearing = Math.toRadians(bearingInDegree); //converted to radians.

        double lat1 = Math.toRadians(p.getLat()); //Current lat point converted to radians
        double lon1 = Math.toRadians(p.getLng());//Current long point converted to radians

        double lat2 = Math.asin(Math.sin(lat1) * Math.cos(d / R) +
                Math.cos(lat1) * Math.sin(d / R) * Math.cos(bearing));

        double lon2 = lon1 + Math.atan2(Math.sin(bearing) * Math.sin(d/R) * Math.cos(lat1),
                Math .cos(d / R) - Math.sin(lat1) * Math.sin(lat2));

        lat2 = Math.toDegrees(lat2);
        lon2 = Math.toDegrees(lon2);
        return new GPSPoint(lat2, lon2, null);
    }

    //This function converts decimal degrees to radians
    public static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    //This function converts radians to decimal degrees
    public static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
}

