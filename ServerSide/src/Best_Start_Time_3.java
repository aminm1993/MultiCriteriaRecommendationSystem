import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * This is an implementation for the Best start time finder algorithm as per TP_SP starting with cost vectors as
 * per the defined time slots of the day.
 * @author Amin Mohammadpour
 */
public class Best_Start_Time_3 {
    /**
     * Find the best start time that can achieve the most optimal routes w.r.t all the specified attributes in the given graph
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
//        double distance = distance(node_s.getLat(), node_s.getLng(), node_d.getLat(), node_d.getLng(), "K");
//        double factorized_distance = distance * 10.0;
        double factorized_distance = 12.0;
        Bound bound = new Bound(move(new GPSPoint(min_lat, min_lng,null), factorized_distance, 225),
                move(new GPSPoint(max_lat, max_lng), factorized_distance, 45));
        // end-bounding-box-definition

        ArrayList<CostVector> global_set = new ArrayList<CostVector>();
        ArrayList<CostVector> solution_set = new ArrayList<CostVector>();
        /* maintains the non-dominated cost vectors at each node. It will be used as a baseline for validating any new
        cost vector before adding it to the global_set */
        Map<Integer, Map<Integer, ArrayList<CostVector>>> M = new HashMap<Integer, Map<Integer, ArrayList<CostVector>>>();
        ArrayList<CostVector> PQ = new ArrayList<CostVector>();
        float[] costs = new float[selected_attributes.length];
        for (int i = 0; i < selected_attributes.length; i++) {
            costs[i] = 0f;
        }
        CostVector cost_vector, cv;
        M.put(s, new HashMap<Integer, ArrayList<CostVector>>());
        for (Integer time_slot_index : time_slots_of_day.getTime_slots().keySet()) {
            cost_vector = new CostVector(s, null, null, costs);
            cost_vector.setStartTime(time_slots_of_day.getTime_slots().get(time_slot_index).getFrom());
            PQ.add(cost_vector);
        }
        Integer time_slot_index;
        int successor_node_id, attribute_index;
        float aggregated_cost, travel_time;
        Edge out_edge;
        Iterator<CostVector> iterator;
        Node node;
        boolean dominated_by_solution_set, not_dominated;
        boolean route_exist = false;
        while(!PQ.isEmpty()){
            for (CostVector non_dominated_cv : PQ) {
                // get the starting time slot of the day
                time_slot_index = time_slots_of_day.get_time_slot_index(non_dominated_cv.getStartTime());
                // iterate on the out-going edges of the current node to continue branching to its accessor
                for (Integer out_edge_index : G.get_node(non_dominated_cv.getNode_id()).getOut_edges()) {
                    out_edge = G.get_edge(out_edge_index);
                    successor_node_id = out_edge.getNode_2_id();
                    node = G.get_node(successor_node_id);
                    if(bound.contains(node.getLat(), node.getLng())){
                    /* It is possible to span on multiple time slots while passing an edge. Hence, the
                       weights need to be factorized by the overlapping proportion of the spanned time slots */
                    /*Map<Integer, Double> overlapping_distribution = time_slots_of_day.
                            get_time_slots_distribution(non_dominated_cv.getStartTime(),
                                    out_edge, time_slot_index);*/

                    /*create a thread per each accessor of this in-hand node to which this cost vector belongs to
                    /generate a new cost_vector per successor*/
                        cost_vector = new CostVector(successor_node_id, non_dominated_cv, selected_attributes.length);
                        attribute_index = 0;
                        for (String attribute_name : selected_attributes) {
                        /*aggregated_cost = non_dominated_cv.getCosts().get(attribute_index);
                        for (int time_slot_index : overlapping_distribution.keySet()) {
                            aggregated_cost += out_edge.getAttributes().get(attribute_name).get_weight(time_slot_index) *
                                    overlapping_distribution.get(time_slot_index));
                        }*/
                            aggregated_cost = non_dominated_cv.getCosts()[attribute_index] +
                                    out_edge.getAttributes().get(attribute_name).get_weight(time_slot_index);
                            cost_vector.add_cost(aggregated_cost);
                            attribute_index++;
                        }
                        //set the start time of the cost vector to its predecessor's start time plus the travel time.
                        travel_time = out_edge.getAttributes().get("travel_time").get_weight(time_slot_index);
                        cost_vector.setStartTime(non_dominated_cv.getStartTime().
                                plusHours((long) (travel_time / 60)).
                                plusMinutes((long) (travel_time % 60)));
                        time_slot_index = time_slots_of_day.get_time_slot_index(cost_vector.getStartTime());

                    /* First, check if the newly generated cost vector belongs to the destination
                       node. If yes, then validate it by verifying its dominance relationship with
                       the cost vectors at the solution set. Otherwise, verify its dominance relationship
                       against the non-dominated cost vectors at recorded at the same node it belongs to.*/
                        if (successor_node_id == d) {
                            dominated_by_solution_set = false;
                            iterator = solution_set.iterator();
                            while (iterator.hasNext()) {
                                cv = iterator.next();
                                if (cv.is_dominant_on(cost_vector)) {
                                    dominated_by_solution_set = true;
                                    break;
                                } else if (cost_vector.is_dominant_on(cv)) {
                                    iterator.remove();
                                }
                            }
                            if (!dominated_by_solution_set) {
                                solution_set.add(cost_vector);
                                // prone the global set
                                iterator = global_set.iterator();
                                while (iterator.hasNext()) {
                                    if (cost_vector.is_dominant_on(iterator.next())) {
                                        iterator.remove();
                                    }
                                }
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

                        /* If it is found as not dominated, then proceed to verify its dominance relationship with the
                        recorded non-dominated cost vectors accrued at the same node it belongs to. It will be add to
                        the non-dominated set if dominates any one and all the dominated ones will be removed. */
                            if (!dominated_by_solution_set) {
                                if (M.containsKey(successor_node_id)) {
                                    if (M.get(successor_node_id).containsKey(time_slot_index)) {
                                        iterator = M.get(successor_node_id).get(time_slot_index).iterator();
                                        not_dominated = false;
                                        while (iterator.hasNext()) {
                                            if (cost_vector.is_dominant_on(iterator.next())) {
                                                iterator.remove();
                                                not_dominated = true;
                                            }
                                        }
                                        if (not_dominated) {
                                            M.get(successor_node_id).get(time_slot_index).add(cost_vector);
                                            global_set.add(cost_vector);
                                        }
                                    } else {
                                        M.get(successor_node_id).put(time_slot_index, new ArrayList<CostVector>(Arrays.asList(cost_vector)));
                                        global_set.add(cost_vector);
                                    }
                                } else {
                                    // first cost vector accrued at the designated node
                                    M.put(successor_node_id, new HashMap<Integer, ArrayList<CostVector>>());
                                    M.get(successor_node_id).put(time_slot_index, new ArrayList<CostVector>(Arrays.asList(cost_vector)));
                                    global_set.add(cost_vector);
                                }
                            }
                        }
                    }
                }
            }
            //cleanup the PQ since its items have been scheduled
            PQ.clear();
            if(global_set.size() > 0) {
                find_non_dominated_cost_vectors(global_set, PQ);
            }
        }

        // print the found routes
/*        Stack<CostVector> route;
        CostVector cv = null;
        for(CostVector solution_cv : solution_set){
            route = new Stack<CostVector>();
            cv = solution_cv;
            while(cv != null){
                route.push(cv);
                cv = cv.getPredecessor();
            }
            System.out.println("Route ==================================>");
            for (int i = route.size() - 1 ; i >=0 ; i--) {
                System.out.println(route.get(i).toString());

            }
        }*/

        if(solution_set.size() > 0){
            route_exist = true;
        }
        return route_exist;
    }

    /**
     * Find and them move the non-dominated cost vectors from the global_set to the PQ (Parallel implementation)
     */
/*    public static void find_non_dominated_cost_vectors(final ArrayList<CostVector> global_set, final ArrayList<CostVector> PQ) {
        ExecutorService executor_service;
        if(global_set.size() > 10){
            executor_service = Executors.newFixedThreadPool(10);
        }
        else{
            executor_service = Executors.newFixedThreadPool(global_set.size());
        }
        for (final CostVector cost_vector : global_set) {
            executor_service.execute(new Runnable() {
                                         public void run() {
                                             boolean dominated = false;
                                             try {
                                                 for (CostVector cv : global_set) {
                                                     if (!cost_vector.equals(cv) && cost_vector.is_dominated_by(cv)) {
                                                         dominated = true;
                                                         break;
                                                     }
                                                 }
                                                 if (!dominated) {
                                                     synchronized (PQ) {
                                                         PQ.add(cost_vector);
                                                     }
                                                 }
                                             } catch (Exception ex) {
                                                 ex.printStackTrace();
                                             }
                                         }
                                     }
            );
        }

        executor_service.shutdown();
        try {
            executor_service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // remove the non dominated cost vectors from the global_set
        for(CostVector non_dominated_cv : PQ){
            global_set.remove(non_dominated_cv);
        }
    }*/

    /**
     * Find and then move the non-dominated cost vectors from the global_set to the PQ (serial implementation)
     * @param global_set the global set
     * @param PQ the priority queue
     */
    public static void find_non_dominated_cost_vectors(ArrayList<CostVector> global_set, ArrayList<CostVector> PQ) {
        if(global_set.size() == 1){
            PQ.add(global_set.get(0));
        }
        else {
            boolean dominated;
            for (CostVector cost_vector : global_set) {
                dominated = false;
                for (CostVector cv : global_set) {
                    if (!cost_vector.equals(cv) && cost_vector.is_dominated_by(cv)) {
                        dominated = true;
                        break;
                    }
                }
                if (!dominated) {
                    PQ.add(cost_vector);
                }
            }
        }
        // remove the non dominated cost vectors from the global_set
        for(CostVector non_dominated_cv : PQ){
            global_set.remove(non_dominated_cv);
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

    public static void main(String[] args){
    }
}

