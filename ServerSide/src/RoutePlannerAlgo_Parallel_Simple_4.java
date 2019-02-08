import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * This is a parallel implementation for Time Parameterized Shortest Path algorithm.
 * @author Amin Mohammadpour
 */
public class TP_SP_Parallel_Simple_4 {
    /**
     * Find the optimal routes w.r.t all the specified attributes in the given graph
     * @param s the source node id
     * @param d the destination node id
     * @param startTime the start time of the commuting in the form of 'hour:minute'
     */
    public static boolean find_optimal_route(final Graph G, final Time_Slots_of_Day time_slots_of_day, final int s,
                                             final int d, LocalTime startTime, final String [] selected_attributes){
        boolean route_exist = false;
        final Vector<CostVector> global_set = new Vector<CostVector>();
        final Vector<CostVector> solution_set = new Vector<CostVector>();
        /* maintains the non-dominated cost vectors at each node. It will be used as a baseline for validating any new
        cost vector before adding it to the global_set */
        final Hashtable<Integer, Vector<CostVector>>non_dominated_set = new Hashtable<Integer, Vector<CostVector>>();
        final Vector<CostVector> PQ = new Vector<CostVector>();
        float[] initial_costs = new float[selected_attributes.length];
        for (int i = 0; i < selected_attributes.length; i++) {
            initial_costs[i] = 0f;
        }
        CostVector cost_vector_s = new CostVector(s, null, startTime, initial_costs);
        ExecutorService expanding_executor_service = null;
        PQ.add(cost_vector_s);

        while(!PQ.isEmpty()){
//            expanding_executor_service = Executors.newFixedThreadPool(PQ.size());
            expanding_executor_service = Executors.newFixedThreadPool(10);
            for (final CostVector non_dominated_cv : PQ) {
                // create a thread per item in the priority queue to be processed in parallel
                expanding_executor_service.execute( new Runnable(){
                    int time_slot_index;
                    public void run() {
                        ExecutorService branching_executor_service = Executors.newFixedThreadPool(4);
                        // get the starting time slot of the day
                        time_slot_index = time_slots_of_day.get_time_slot_index(non_dominated_cv.getStartTime());
                        // iterate on the out-going edges of the current node to continue branching to its accessor
                        int size = G.get_node(non_dominated_cv.getNode_id()).getOut_edges().size();
                        for (final Integer out_edge_index : G.get_node(non_dominated_cv.getNode_id()).getOut_edges()){
                            final Edge out_edge = G.get_edge(out_edge_index);
                            /* It is possible to span on multiple time slots while passing an edge. Hence, the
                               weights need to be factorized by the overlapping proportion of the spanned time slots */
//                            final Map<Integer, Double> overlapping_distribution = time_slots_of_day.
//                                    get_time_slots_distribution(non_dominated_cv.getStartTime(),
//                                            out_edge, time_slot_index);

                            // create a thread per each accessor of this in-hand node to which this cost vector belongs to
                            branching_executor_service.execute( new Runnable() {
                                @Override
                                public void run() {
                                    Iterator<CostVector> iterator;
                                    // generate a new cost_vector per accessor
                                    CostVector cost_vector = new CostVector(out_edge.getNode_2_id(),
                                            non_dominated_cv, time_slots_of_day.get_slots_number());
                                    float aggregated_cost ;
                                    int attribute_index = 0;
                                    for(String attribute_name: selected_attributes) {
/*                                            aggregated_cost = non_dominated_cv.getCosts().get(attribute_index);
                                            for (int time_slot_index : overlapping_distribution.keySet()) {
                                                aggregated_cost += out_edge.getAttributes().get(attribute_name).get_weight(time_slot_index) *
                                                        overlapping_distribution.get(time_slot_index));
                                            }*/
                                        aggregated_cost = non_dominated_cv.getCosts()[attribute_index] +
                                                out_edge.getAttributes().get(attribute_name).get_weight(time_slot_index);
                                        cost_vector.add_cost(aggregated_cost);
                                        attribute_index ++;
                                    }
                                        /* set the start time of the cost vector to its predecessor's start time plus
                                           the travel time. */
                                    float travel_time = out_edge.getAttributes().get("travel_time").get_weight(time_slot_index);
                                    cost_vector.setStartTime(non_dominated_cv.getStartTime().
                                            plusHours((long) (travel_time / 60)).
                                            plusMinutes((long) (travel_time % 60)));

                                        /* First, check if the newly generated cost vector belongs to the destination
                                           node. If yes, then validate it by verifying its dominancy relationship with
                                           the cost vectors at the solution set. Otherwise, verify its dominancy
                                           relationship against the non-dominated cost vectors at recorded at the
                                           same node it belongs to.*/
                                    if (out_edge.getNode_2_id() == d){
                                        boolean dominate_other_cv_in_solution_set = false;
                                        if(solution_set.isEmpty()){
                                            dominate_other_cv_in_solution_set = true;
                                        }
                                        else {
                                            iterator = solution_set.iterator();
                                            try {
                                                while (iterator.hasNext()) {
                                                    if (cost_vector.is_dominant_on(iterator.next())) {
                                                        iterator.remove();
                                                        dominate_other_cv_in_solution_set = true;
                                                    }
                                                }
                                            }catch (ConcurrentModificationException e){
                                                e.printStackTrace();
                                            }
                                        }
                                        if(dominate_other_cv_in_solution_set) {
                                            solution_set.add(cost_vector);
                                            // prone the global set
                                                iterator = global_set.iterator();
                                                try {
                                                    while (iterator.hasNext()) {
                                                        if (cost_vector.is_dominant_on(iterator.next())) {
                                                            iterator.remove();
                                                        }
                                                    }
                                                }catch (ConcurrentModificationException e){
                                                 e.printStackTrace();
                                            }
                                        }
                                    }
                                    else {
                                        // accrued at some node between s-d
                                        boolean dominated_by_solution_set = false;
                                        for (int i = 0; i < solution_set.size(); i++) {
                                            if (solution_set.get(i).is_dominant_on(cost_vector)) {
                                                dominated_by_solution_set = true;
                                                break;
                                            }
                                        }

                                            /* If it is found as not dominated, then proceed to verify its dominancy
                                              relationship with the recorded non-dominated cost vectors accrued at
                                              the same node it belongs to. It will be add to the non-dominated set
                                              if dominates any one and all the dominated ones will be removed. */
                                        if (!dominated_by_solution_set){
                                            if(non_dominated_set.get(out_edge.getNode_2_id()) != null){
                                                boolean dominant_on_other = false;
                                                synchronized (non_dominated_set) {
                                                    iterator = non_dominated_set.get(out_edge.getNode_2_id()).iterator();
                                                    try {
                                                        while (iterator.hasNext()) {
                                                            if (cost_vector.is_dominant_on(iterator.next())) {
                                                                iterator.remove();
                                                                dominant_on_other = true;
                                                            }
                                                        }
                                                    }catch (ConcurrentModificationException e){
                                                        e.printStackTrace();
                                                    }
                                                }
                                                if (dominant_on_other){
                                                    non_dominated_set.get(out_edge.getNode_2_id()).add(cost_vector);
                                                    global_set.add(cost_vector);
                                                }
                                            } else {
                                                // first cost vector accrued at the designated node
                                                Vector<CostVector> vector = new Vector<CostVector>();
                                                vector.add(cost_vector);
                                                non_dominated_set.put(out_edge.getNode_2_id(), vector);
                                                global_set.add(cost_vector);
                                            }
                                        }
                                    }
                                }
                            });
                        }

                        branching_executor_service.shutdown();
                        try {
                            branching_executor_service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            expanding_executor_service.shutdown();
            try {
                expanding_executor_service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
     * Move the non-dominated cost vectors from the global_set to the PQ
     */
    public static void find_non_dominated_cost_vectors(final Vector<CostVector> global_set, final Vector<CostVector> PQ) {
        ExecutorService executor_service = Executors.newFixedThreadPool(global_set.size());
        final Vector<CostVector> non_dominated_cvs = new Vector<CostVector>();
        for (final CostVector cost_vector : global_set) {
            executor_service.execute(new Runnable() {
                                         public void run() {
                                             boolean dominated = false;
                                             try {
                                                 for (CostVector cv : global_set){
                                                     if(!cost_vector.equals(cv) && cost_vector.is_dominated_by(cv)){
                                                         dominated = true;
                                                         break;
                                                     }
                                                 }
                                                 if(!dominated){
                                                     PQ.add(cost_vector);
                                                     non_dominated_cvs.add(cost_vector);
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
        for(CostVector non_dominated_cv : non_dominated_cvs){
            global_set.remove(non_dominated_cv);
        }
    }

    public static void main(String[] args){
        ExecutorService executor_service = Executors.newFixedThreadPool(1);
        for (int i = 0; i < 100; i++) {
            final int id = i;
            executor_service.execute(new Runnable() {
                                         public void run() {
                                             try {
                                                 Thread.sleep(1000);
                                                 System.out.println(id);
                                             } catch (InterruptedException e) {
                                                 e.printStackTrace();
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

        System.out.println("==================================== End ===========================================");
    }
}

