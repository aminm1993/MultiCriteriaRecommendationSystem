import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * This is a parallel implementation for Time Parametrized Shortest Path algorithm.
 * @author Amin Mohammadpour
 */
public class TP_SP_Bidirectional_Advance {
    /**
     * Find the optimal routes w.r.t all the specified attributes in the given graph
     * @param s the source node id
     * @param d the destination node id
     * @param startTime the start time of the commuting in the form of 'hour:minute'
     */
    public static boolean find_optimal_route(final Graph G, final Time_Slots_of_Day time_slots_of_day, final int s,
                                             final int d, final LocalTime startTime, final String [] selected_attributes) {
        final ArrayList<CostVector> solution_set = new ArrayList<CostVector>();
        final Hashtable<Integer, Hashtable<Integer, CostVector>> M = new Hashtable<Integer, Hashtable<Integer, CostVector>>();
        float[] aggregated_costs = new float[selected_attributes.length];
        for (int i = 0; i < selected_attributes.length; i++) {
            aggregated_costs[i] = Float.POSITIVE_INFINITY;
        }
        final CostVector U = new CostVector(aggregated_costs);
        final JobState forward_thread_status = new JobState();
        final JobState backward_thread_status = new JobState();

        Thread backward_thread = new Thread(){
            public void run() {
                ArrayList<Integer> Q = new ArrayList<Integer>();
                ArrayList<Integer> nominated_nodes = new ArrayList<Integer>();
                float[] costs = new float[selected_attributes.length];
                for (int i = 0; i < selected_attributes.length; i++) {
                    costs[i] = 0f;
                }
                CostVector cost_vector_d = new CostVector(d, 0, costs);
                M.put(d, new Hashtable<Integer, CostVector>());
                for (Integer time_slot_index : time_slots_of_day.getTime_slots().keySet()) {
                    M.get(d).put(time_slot_index, cost_vector_d);
                }
                Q.add(d);
                CostVector cost_vector;
                int attribute_index, predecessor_node_id, next_time_slot_index;
                Edge in_edge;
                LocalTime travel_time;
                Hashtable<Integer, CostVector> cost_vectors_for_settled_node;

                while (!Q.isEmpty() && !forward_thread_status.isCompleted()) {
                    for (final Integer settled_node_id : Q) {
                        // iterate on the in-going edges of the current node to continue branching to its accessor
                        for (Integer in_edge_index : G.get_node(settled_node_id).getIn_edges()) {
                            in_edge = G.get_edge(in_edge_index);
                            predecessor_node_id = in_edge.getNode_1_id();
                            cost_vectors_for_settled_node = new Hashtable<Integer, CostVector>();
                            // generate cost_vector per each predecessor as many as time slots of the day
                            for (Integer time_slot_index : time_slots_of_day.getTime_slots().keySet()) {
                                attribute_index = 0;
                                costs = new float[selected_attributes.length];
                                travel_time = in_edge.get_travel_time(time_slot_index);
                                next_time_slot_index = time_slots_of_day.get_time_slot_index(
                                        time_slots_of_day.getTime_slots().get(time_slot_index).getFrom().
                                                plusHours(travel_time.getHour()).plusMinutes(travel_time.getMinute()));
                                if (M.get(settled_node_id).containsKey(next_time_slot_index)){
                                    for (String attribute_name : selected_attributes) {
                                        costs[attribute_index] = in_edge.getAttributes().get(attribute_name).get_weight(time_slot_index)
                                                + M.get(settled_node_id).get(next_time_slot_index).getCost(attribute_index);
                                        attribute_index++;
                                    }
                                    cost_vector = new CostVector(predecessor_node_id, in_edge.getId(), costs);
                                    if (!U.is_dominant_on(cost_vector)) {
                                        if (!M.containsKey(predecessor_node_id)) {
                                            cost_vectors_for_settled_node.put(time_slot_index, cost_vector);
                                        } else {
                                            if(M.get(predecessor_node_id).containsKey(time_slot_index)) {
                                                if (!M.get(predecessor_node_id).get(time_slot_index).is_dominant_on(cost_vector)) {
                                                    M.get(predecessor_node_id).put(time_slot_index, cost_vector); //overwrite the previous cost vector
                                                }
                                            }
                                            else{
                                                M.get(predecessor_node_id).put(time_slot_index, cost_vector);
                                            }
                                        }
                                    }
                                }
                            }
                            if (!M.containsKey(predecessor_node_id)) {
                                M.put(predecessor_node_id, cost_vectors_for_settled_node);
                            }

                            if (M.get(predecessor_node_id).size() > 0) {
                                nominated_nodes.add(predecessor_node_id);
                            }
                        }
                    }
                    Q.clear();
                    Q.addAll(nominated_nodes);
                    nominated_nodes.clear();
                }
//                synchronized (backward_thread_status) {
                    backward_thread_status.setCompleted(true);
//                }
            }
        };
        backward_thread.start();

        ArrayList<CostVector> global_set = new ArrayList<CostVector>();
        /* maintains the non-dominated cost vectors at each node. It will be used as a baseline for validating any new
           cost vector before adding it to the global_set */
        Map<Integer, ArrayList<CostVector>> non_dominated_set = new HashMap<Integer, ArrayList<CostVector>>();
//        ArrayList<CostVector> PQ = new ArrayList<CostVector>();
        Vector<CostVector> PQ = new Vector<CostVector>();
        aggregated_costs = new float[selected_attributes.length];
        for (int i = 0; i < selected_attributes.length; i++) {
            aggregated_costs[i] = 0f;
        }
        CostVector cost_vector_s = new CostVector(s, null, startTime, aggregated_costs);
        PQ.add(cost_vector_s);
        int time_slot_index;
        CostVector cost_vector, total_cost_vector,settled_cost_vector ;
        float aggregated_cost, travel_time;
        int attribute_index, successor_node_id;
        boolean dominate_some_cv_in_solution_set, dominated_by_solution_set, non_dominated;
        while (!PQ.isEmpty()) {
            for (final CostVector non_dominated_cv : PQ) {
                // get the starting time slot of the day
                time_slot_index = time_slots_of_day.get_time_slot_index(non_dominated_cv.getStartTime());
                // iterate on the out-going edges of the current node to continue branching to its accessor
                Edge out_edge;
                Iterator<CostVector> iterator;
                for (Integer out_edge_index : G.get_node(non_dominated_cv.getNode_id()).getOut_edges()) {
                    out_edge = G.get_edge(out_edge_index);
                    successor_node_id = out_edge.getNode_2_id();
                    /* It is possible to span on multiple time slots while passing an edge. Hence, the
                       weights need to be factorized by the overlapping proportion of the spanned time slots */
/*                     final Map<Integer, Double> overlapping_distribution = time_slots_of_day.
                     get_time_slots_distribution(non_dominated_cv.getStartTime(),
                     out_edge, time_slot_index);*/

                    // create a thread per each successor of this in-hand node to which this cost vector belongs to
                    // generate a new cost_vector per accessor
                    cost_vector = new CostVector(successor_node_id, non_dominated_cv, selected_attributes.length);
                    attribute_index = 0;
                    for (String attribute_name : selected_attributes) {
/*                        aggregated_cost = non_dominated_cv.getCosts().get(attribute_index);
                        for (int time_slot_index : overlapping_distribution.keySet()) {
                            aggregated_cost += out_edge.getAttributes().get(attribute_name).get_weight(time_slot_index) *
                                    overlapping_distribution.get(time_slot_index));
                        }*/
                        try {
                            aggregated_cost = non_dominated_cv.getCosts()[attribute_index] +
                                    out_edge.getAttributes().get(attribute_name).get_weight(time_slot_index);
                            cost_vector.add_cost(aggregated_cost);
                            attribute_index++;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    //set the start time of the cost vector to its predecessor's start time plus the travel time.
                    travel_time = out_edge.getAttributes().get("travel_time").get_weight(time_slot_index);
                    cost_vector.setStartTime(non_dominated_cv.getStartTime().
                            plusHours((long) (travel_time / 60)).
                            plusMinutes((long) (travel_time % 60)));

                    // check if this successor node is an intersection between the forward and backward threads
                    time_slot_index = time_slots_of_day.get_time_slot_index(cost_vector.getStartTime());
                    if (M.containsKey(successor_node_id) && M.get(successor_node_id).containsKey(time_slot_index)) {
                        settled_cost_vector = M.get(successor_node_id).get(time_slot_index);
                        total_cost_vector = cost_vector.plus(settled_cost_vector);
                        if (total_cost_vector.is_dominant_on(U)){
//                            synchronized (U) {
                                U.setCosts(total_cost_vector.getCosts());
//                            }
                            // prone the global set
                            iterator = global_set.iterator();
                            while (iterator.hasNext()) {
                                if (U.is_dominant_on(iterator.next())) {
                                    iterator.remove();
                                }
                            }
                            while(successor_node_id != d){
                                time_slot_index = time_slots_of_day.get_time_slot_index(cost_vector.getStartTime());
                                out_edge = G.get_edge(settled_cost_vector.getEdge_to_successor());
                                successor_node_id = out_edge.getNode_2_id();
                                settled_cost_vector = M.get(successor_node_id).get(time_slot_index);
                                /* It is possible to span on multiple time slots while passing an edge. Hence, the
                                   weights need to be factorized by the overlapping proportion of the spanned time slots */
                                /*final Map<Integer, Double> overlapping_distribution = time_slots_of_day.
                                 get_time_slots_distribution(non_dominated_cv.getStartTime(),
                                 out_edge, time_slot_index);*/

                                attribute_index = 0;
                                aggregated_costs = new float[selected_attributes.length];
                                for (String attribute_name : selected_attributes) {
                                /*aggregated_cost = non_dominated_cv.getCosts().get(attribute_index);
                                for (int time_slot_index : overlapping_distribution.keySet()) {
                                    aggregated_cost += out_edge.getAttributes().get(attribute_name).get_weight(time_slot_index) *
                                            overlapping_distribution.get(time_slot_index));
                                }*/
                                    try {
                                        aggregated_cost = cost_vector.getCosts()[attribute_index] +
                                                out_edge.getAttributes().get(attribute_name).get_weight(time_slot_index);
                                        aggregated_costs[attribute_index] = aggregated_cost;
                                        attribute_index++;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                //set the start time of the cost vector to its predecessor's start time plus the travel time.
                                travel_time = out_edge.getAttributes().get("travel_time").get_weight(time_slot_index);
                                // generate a new cost_vector per accessor
                                cost_vector = new CostVector(successor_node_id, cost_vector, cost_vector.getStartTime().
                                        plusHours((long) (travel_time / 60)).
                                        plusMinutes((long) (travel_time % 60)), aggregated_costs);

                            }
                        }
                        else if(total_cost_vector.is_dominated_by(U)){
                            continue;
                        }
                    }
                    if (!backward_thread_status.isCompleted() || (backward_thread_status.isCompleted() && M.containsKey(successor_node_id))) {
                        /* First, check if the newly generated cost vector belongs to the destination
                           node. If yes, then validate it by verifying its dominance relationship with
                           the cost vectors at the solution set. Otherwise, verify its dominance relationship
                           against the non-dominated cost vectors at recorded at the same node it belongs to.*/
                        if (successor_node_id == d) {
                            dominate_some_cv_in_solution_set = false;
                            if (solution_set.isEmpty()) {
                                dominate_some_cv_in_solution_set = true;
                            } else {
                                iterator = solution_set.iterator();
                                while (iterator.hasNext()) {
                                    if (cost_vector.is_dominant_on(iterator.next())) {
                                        iterator.remove();
                                        dominate_some_cv_in_solution_set = true;
                                    }
                                }
                            }
                            if (dominate_some_cv_in_solution_set) {
                                solution_set.add(cost_vector);
                                // prone the global set
/*                                iterator = global_set.iterator();
                                while (iterator.hasNext()) {
                                    if (cost_vector.is_dominant_on(iterator.next())) {
                                        iterator.remove();
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
                                if (!U.is_dominant_on(cost_vector)) {
                                    if (non_dominated_set.get(successor_node_id) != null) {
                                        iterator = non_dominated_set.get(successor_node_id).iterator();
                                        non_dominated = false;
                                        while (iterator.hasNext()) {
                                            if (cost_vector.is_dominant_on(iterator.next())) {
                                                iterator.remove();
                                                non_dominated = true;
                                            }
                                        }
                                        if (non_dominated) {
                                            non_dominated_set.get(successor_node_id).add(cost_vector);
                                            global_set.add(cost_vector);
                                        }
                                    } else {
                                        // first cost vector accrued at the designated node
                                        ArrayList<CostVector> list = new ArrayList<CostVector>();
                                        list.add(cost_vector);
                                        non_dominated_set.put(successor_node_id, list);
                                        global_set.add(cost_vector);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            //cleanup the PQ since its items have been scheduled
            PQ.clear();
            if (global_set.size() > 0) {
                find_non_dominated_cost_vectors(global_set, PQ);
            }
        }
        forward_thread_status.setCompleted(true);
        if(solution_set.size() > 0){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Move the non-dominated cost vectors from the global_set to the PQ
     */
    public static void find_non_dominated_cost_vectors(final ArrayList<CostVector> global_set, final Vector<CostVector> PQ) {
        if(global_set.size() == 1){
            PQ.add(global_set.get(0));
        }
        else {
            int processors = Runtime.getRuntime().availableProcessors();;
            ExecutorService executor_service;
            if (global_set.size() < processors) {
                executor_service = Executors.newFixedThreadPool(global_set.size());
            } else {
                executor_service = Executors.newFixedThreadPool(processors);
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
//                                                         synchronized (PQ) {
                                                             PQ.add(cost_vector);
//                                                         }
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
        }
        // remove the non dominated cost vectors from the global_set
        for(CostVector non_dominated_cv : PQ){
            global_set.remove(non_dominated_cv);
        }
    }

    /**
     * Move the non-dominated cost vectors from the global_set to the PQ
     */
    public static void find_non_dominated_cost_vectors(final ArrayList<CostVector> global_set,
                                                       final Map<CostVector, CostVector> map, final ArrayList<CostVector> PQ) {
        if(map.size() == 1){
            PQ.add((CostVector)map.values().toArray()[0]);
        }
        else {
            ExecutorService executor_service;
            if (map.size() > 10) {
                executor_service = Executors.newFixedThreadPool(10);
            } else {
                executor_service = Executors.newFixedThreadPool(map.size());
            }
            for (final CostVector cost_vector : map.keySet()) {
                executor_service.execute(new Runnable() {
                                             public void run() {
                                                 boolean dominated = false;
                                                 try {
                                                     for (CostVector cv : map.keySet()) {
                                                         if (!cost_vector.equals(cv) && cost_vector.is_dominated_by(cv)) {
                                                             dominated = true;
                                                             break;
                                                         }
                                                     }
                                                     if (!dominated) {
                                                         synchronized (PQ) {
                                                             PQ.add(map.get(cost_vector));
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
        }
        // remove the non dominated cost vectors from the global_set
        for(CostVector non_dominated_cv : PQ){
            global_set.remove(non_dominated_cv);
        }
    }

    public static void main(String[] args){
    }
}

