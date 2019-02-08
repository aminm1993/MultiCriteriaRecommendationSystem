import java.io.*;
import java.time.LocalTime;

/**
 * @author Amin Mohammadpour
 */
public class Main {
    private static final long MEGABYTE = 1024L * 1024L;
    public static long bytesToMegabytes(long bytes) {
        return bytes / MEGABYTE;
    }

    public Main() {
    }

    public static void main(String[]args){
//        int p = Runtime.getRuntime().availableProcessors();
        // input parameters
        String input_dir = args[0];
        String ATAG = input_dir + "\\ATAG";
        String queries = input_dir + "\\Queries";
        String action = args[1];

        Graph G = new Graph();
        Time_Slots_of_Day time_slots_of_day = new Time_Slots_of_Day();
        GraphLoader.load(ATAG, G, time_slots_of_day);
        G.optimize_nodes_size();
//        Best_Start_Time_2.find_best_start_time(G, time_slots_of_day, 0, 7, new String[]{"travel_time", "risk"});
/*        TP_SP_Bidirectional_Advance_4.find_optimal_route(G, time_slots_of_day, 0, 7, LocalTime.of(5, 0),
                new String[]{"travel_time", "risk"});*/
//        QueryGenerator.generate("C:\\Users\\Amruta\\Desktop\\workplace\\PreGo_2\\Queries\\",G, time_slots_of_day, 11000, 1.0);
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long initial_used_memory = bytesToMegabytes(runtime.totalMemory() - runtime.freeMemory());
        System.out.println("initial memory: " + initial_used_memory);

        try {
            runtime.gc();
            PerformanceTest.test(G, time_slots_of_day, initial_used_memory,queries + "\\1.distance", 1, action);
            runtime.gc();
            PerformanceTest.test(G, time_slots_of_day, initial_used_memory, queries + "\\2.attribute", 2, action);
            runtime.gc();
            PerformanceTest.test(G, time_slots_of_day, initial_used_memory, queries + "\\3.query", 3, action);
            runtime.gc();
            PerformanceTest.test(G, time_slots_of_day, initial_used_memory, queries + "\\4.time", 4, action);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
