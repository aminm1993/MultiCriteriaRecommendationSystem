import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import java.io.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

/**
 * @author Amin Mohammadpour
 */
public class PerformanceTest {
    private static final long MEGABYTE = (long) Math.pow(1024,2);

    public static long bytesToMegabytes(long bytes) {
        return bytes / MEGABYTE;
    }

    public static void test(Graph G, Time_Slots_of_Day time_slots_of_day, long initial_memory_used,
                            String queries_dir, int test_number , String action) throws IOException {
        // Get the Java runtime
        Runtime runtime = Runtime.getRuntime();
        File file = null;
        FileReader fr = null;
        BufferedReader br = null;
        String line;
        String tokens[];
        File folder = new File(queries_dir);
        File[] listOfFiles = folder.listFiles();
        String output;
        int number_of_queries = 0;
        output = queries_dir.substring(0,queries_dir.lastIndexOf("\\")) + "\\" + test_number + ".output.txt";
        PrintWriter printWriter = new PrintWriter(new FileWriter(output));
        if(test_number == 1 || test_number == 3){
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    try {
                        runtime.gc();
                        file = new File(listOfFiles[i].getAbsolutePath());
                        fr = new FileReader(file);
                        br = new BufferedReader(fr);
                        long used_memory_before = 0;
                        long used_memory_after = 0;
                        long cpu_time = 0;
                        long max_memory_consumption = 0;
                        LocalDateTime start, end;
                        int total_queries = 0;
                        boolean pass;
                        int no_answer_count = 0;
                        number_of_queries = Integer.parseInt(file.getName().split("_")[1]);
                        while ((line = br.readLine()) != null) {
                            pass = false;
                            tokens = line.split(" ");
                            // Run the garbage collector
//                            runtime.gc();
                            // Calculate the used memory
                            used_memory_before = bytesToMegabytes(runtime.totalMemory() - runtime.freeMemory());
                            start = LocalDateTime.now();
/*                            pass = Best_Start_Time_2.find_best_start_time(G, time_slots_of_day, Integer.parseInt(tokens[0]),
                                    Integer.parseInt(tokens[1]), new String[]{"travel_time"});*/
                            if(action.equals(Constants.RUN_TP_BEST_START)){
                                pass = Best_Start_Time_3.find_best_start_time(G, time_slots_of_day, Integer.parseInt(tokens[0]),
                                        Integer.parseInt(tokens[1]), new String[]{"distance", "travel_time", "risk"});
                            }
                            else if(action.equals(Constants.RUN_BIDIRECTIONAL_TP_SP)) {
                                pass = TP_SP_Bidirectional_Advance_5.find_optimal_route(G, time_slots_of_day, Integer.parseInt(tokens[0]),
                                        Integer.parseInt(tokens[1]), LocalTime.of(5, 30), new String[]{"distance", "travel_time", "risk"});
                            }
                            else if(action.equals(Constants.RUN_TP_SP)) {
                                pass = TP_SP_Serial_Simple.find_optimal_route(G, time_slots_of_day, Integer.parseInt(tokens[0]),
                                        Integer.parseInt(tokens[1]), LocalTime.of(5, 30), new String[]{"distance"});
                            }
                            end = LocalDateTime.now();
                            if (pass) {
                                used_memory_after = bytesToMegabytes(runtime.totalMemory() - runtime.freeMemory());
                                max_memory_consumption = Math.max(max_memory_consumption, Math.abs(used_memory_after - used_memory_before));
                                cpu_time += start.until(end, ChronoUnit.MILLIS);
                                total_queries++;
/*                                System.out.println(total_queries + ", CPU = " + start.until(end, ChronoUnit.MILLIS) +
                                        ", before =" +  used_memory_before + ", after = " + used_memory_after
                                        + ", total used = " + Math.abs(used_memory_after - used_memory_before) + ", Max=" + max_memory_consumption);*/
                            }
                            else{
                                no_answer_count++;
//                                System.out.println(no_answer_count);
                            }
/*                            if(total_queries == number_of_queries){
                                break;
                            }*/
                        }
                        System.out.println("=============================> File: " + file.getName());
//                        System.out.println("No answer = " + no_answer_count);
                        System.out.println("CPU time:" + (cpu_time/ total_queries));
                        System.out.println("Max Memory usage:" + max_memory_consumption);
                        System.out.println("queries = " + total_queries);
//                        runtime.gc();
//                        max_memory_consumption = Math.max(max_memory_consumption, bytesToMegabytes(runtime.totalMemory() - runtime.freeMemory())) - initial_memory_used;
                        printWriter.append("=============================> File: " + file.getName() + "\r\n");
                        printWriter.append("Initial memory usage:" + initial_memory_used + "\r\n");
                        printWriter.append("Max Memory usage:" + max_memory_consumption + "\r\n");
                        if(total_queries > 0) {
//                            printWriter.append("CPU time:" + (cpu_time / total_queries) + "\r\n");
                            printWriter.append("CPU time:" + (cpu_time/ total_queries) + "\r\n");
                        }
                        printWriter.flush();

                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            }
        }
        else if(test_number == 2){
            //attributes
            ArrayList<String[]> attributes = new ArrayList<String[]>();
//            time_slots_of_day = new Time_Slots_of_Day();
//            time_slots_of_day.add_time_slot(new TimeSlot(LocalTime.of(0,0), LocalTime.of(23,59), null));
//            attributes.add(new String[]{"travel_time"});
//            attributes.add(new String[]{"risk"});
//            attributes.add(new String[]{"distance"});
            attributes.add(new String[]{"travel_time", "risk"});
            attributes.add(new String[]{"distance", "travel_time", "risk"});
            attributes.add(new String[]{"distance", "travel_time", "risk" , "attribute_4"});
            attributes.add(new String[]{"distance", "travel_time", "risk" , "attribute_4", "attribute_5"});
            attributes.add(new String[]{"distance", "travel_time", "risk" , "attribute_4", "attribute_5", "attribute_6"});
            for (int i = 0; i < attributes.size(); i++) {
                if (listOfFiles[0].isFile()) {
                    try {
                        runtime.gc();
                        file = new File(listOfFiles[0].getAbsolutePath());
                        fr = new FileReader(file);
                        br = new BufferedReader(fr);
                        long used_memory_before = 0;
                        long used_memory_after = 0;
                        long cpu_time = 0;
                        long max_memory_consumption = 0;
                        LocalDateTime start, end;
                        int total_queries = 0;
                        boolean pass;
                        int no_answer = 0;
                        while ((line = br.readLine()) != null) {
                            pass = false;
                            tokens = line.split(" ");
                            // Run the garbage collector
//                            runtime.gc();
                            // Calculate the used memory
                            used_memory_before = bytesToMegabytes(runtime.totalMemory() - runtime.freeMemory());
                            start = LocalDateTime.now();
                            if(action.equals(Constants.RUN_TP_BEST_START)) {
                                pass = Best_Start_Time_3.find_best_start_time(G, time_slots_of_day, Integer.parseInt(tokens[0]),
                                        Integer.parseInt(tokens[1]),attributes.get(i));
                            }
                            else if(action.equals(Constants.RUN_BIDIRECTIONAL_TP_SP)) {
                                pass = TP_SP_Bidirectional_Advance_5.find_optimal_route(G, time_slots_of_day, Integer.parseInt(tokens[0]),
                                        Integer.parseInt(tokens[1]), LocalTime.of(8, 30), attributes.get(i));
                            }
                            else if(action.equals(Constants.RUN_TP_SP)) {
                                pass = TP_SP_Serial_Simple.find_optimal_route(G, time_slots_of_day, Integer.parseInt(tokens[0]),
                                        Integer.parseInt(tokens[1]), LocalTime.of(5, 30), attributes.get(i));
                            }
                            end = LocalDateTime.now();
                            if (pass) {
                                used_memory_after = bytesToMegabytes(runtime.totalMemory() - runtime.freeMemory());
                                max_memory_consumption = Math.max(max_memory_consumption, Math.abs(used_memory_after - used_memory_before));
                                cpu_time += start.until(end, ChronoUnit.MILLIS);
                                total_queries++;
/*                            System.out.println(total_queries + ", CPU = " + start.until(end, ChronoUnit.MILLIS) +
                                    ", before =" +  used_memory_before + ", after = " + used_memory_after
                                    + ", total used = " + Math.abs(used_memory_after - used_memory_before));*/
                            }
                            else{
                                no_answer++;
                            }
/*                            if(total_queries == 10000){
                                break;
                            }*/
                        }
                        System.out.println("=============================> Attributes: " + attributes.get(i).length);
                        System.out.println("No answer = " + no_answer);
//                        runtime.gc();
//                        max_memory_consumption = Math.max(max_memory_consumption, bytesToMegabytes(runtime.totalMemory() - runtime.freeMemory())) - initial_memory_used;

                        printWriter.append("=============================> File: " + file.getName() + "\r\n");
//                        printWriter.append("Attributes number: " + attributes.get(i).length + "\r\n");
                        printWriter.append("Attributes number: " + attributes.get(i).length + "\r\n");
                        printWriter.append("Initial memory usage:" + initial_memory_used + "\r\n");
                        printWriter.append("Max Memory usage:" + max_memory_consumption + "\r\n");
                        if(total_queries > 0) {
                            printWriter.append("CPU time:" + (cpu_time / total_queries) + "\r\n");
                        }
                        printWriter.flush();

                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            }
        }
        else if(test_number == 4){
            //times
            ArrayList<Time_Slots_of_Day> times = new ArrayList<Time_Slots_of_Day>();
            Time_Slots_of_Day t ;

            t = new Time_Slots_of_Day();
            t.add_time_slot(new TimeSlot(LocalTime.of(0,0), LocalTime.of(23,59), null));
            times.add(t);

            t = new Time_Slots_of_Day();
            t.add_time_slot(new TimeSlot(LocalTime.of(0,0), LocalTime.of(12,0), null));
            t.add_time_slot(new TimeSlot(LocalTime.of(12,0), LocalTime.of(23,59), null));
            times.add(t);

            t = new Time_Slots_of_Day();
            t.add_time_slot(new TimeSlot(LocalTime.of(0,0), LocalTime.of(8,0), null));
            t.add_time_slot(new TimeSlot(LocalTime.of(8,0), LocalTime.of(16,0), null));
            t.add_time_slot(new TimeSlot(LocalTime.of(16,0), LocalTime.of(23,59), null));
            times.add(t);

            t = new Time_Slots_of_Day();
            t.add_time_slot(new TimeSlot(LocalTime.of(0,0), LocalTime.of(6,0), null));
            t.add_time_slot(new TimeSlot(LocalTime.of(6,0), LocalTime.of(12,0), null));
            t.add_time_slot(new TimeSlot(LocalTime.of(12,0), LocalTime.of(18,0), null));
            t.add_time_slot(new TimeSlot(LocalTime.of(18,0), LocalTime.of(23,59), null));
            times.add(t);

            times.add(time_slots_of_day);

            for (int i = 0; i < times.size(); i++) {
                if (listOfFiles[0].isFile()) {
                    try {
                        runtime.gc();
                        file = new File(listOfFiles[0].getAbsolutePath());
                        fr = new FileReader(file);
                        br = new BufferedReader(fr);
                        long used_memory_before = 0;
                        long used_memory_after = 0;
                        long cpu_time = 0;
                        long max_memory_consumption = 0;
                        LocalDateTime start, end;
                        int total_queries = 0;
                        boolean pass;
                        int no_answer = 0;
                        while ((line = br.readLine()) != null) {
                            pass = false;
                            tokens = line.split(" ");
                            // Run the garbage collector
//                            runtime.gc();
                            // Calculate the used memory
                            used_memory_before = bytesToMegabytes(runtime.totalMemory() - runtime.freeMemory());
                            start = LocalDateTime.now();
                            if(action.equals(Constants.RUN_TP_BEST_START)) {
                                pass = Best_Start_Time_3.find_best_start_time(G, times.get(i), Integer.parseInt(tokens[0]),
                                        Integer.parseInt(tokens[1]), new String[]{"distance", "travel_time", "risk"});
/*                                pass = Best_Start_Time_2.find_best_start_time(G, times.get(i), Integer.parseInt(tokens[0]),
                                        Integer.parseInt(tokens[1]), new String[]{"travel_time"});*/
                            }
                            else if(action.equals(Constants.RUN_BIDIRECTIONAL_TP_SP)) {
                                pass = TP_SP_Bidirectional_Advance_5.find_optimal_route(G, times.get(i), Integer.parseInt(tokens[0]),
                                        Integer.parseInt(tokens[1]), LocalTime.of(8, 30), new String[]{"distance", "travel_time", "risk"});
                            }
                            else if(action.equals(Constants.RUN_TP_SP)) {
                                pass = TP_SP_Serial_Simple.find_optimal_route(G, times.get(i), Integer.parseInt(tokens[0]),
                                        Integer.parseInt(tokens[1]), LocalTime.of(5, 30), new String[]{"distance"});
                            }
                            end = LocalDateTime.now();
                            if (pass) {
                                used_memory_after = bytesToMegabytes(runtime.totalMemory() - runtime.freeMemory());
                                max_memory_consumption = Math.max(max_memory_consumption, Math.abs(used_memory_after - used_memory_before));
                                cpu_time += start.until(end, ChronoUnit.MILLIS);
                                total_queries++;
/*                                System.out.println(total_queries + ", CPU = " + start.until(end, ChronoUnit.MILLIS) +
                                        ", before =" +  used_memory_before + ", after = " + used_memory_after
                                        + ", total used = " + Math.abs(used_memory_after - used_memory_before));*/

                            }
                            else{
                                no_answer++;
                            }
/*                            if(total_queries == 1000){
                                break;
                            }*/
                        }
                        System.out.println("=============================> Time slots : " + times.get(i).get_slots_number());
                        System.out.println("No answer = " + no_answer);
                        System.out.println("queries = " + total_queries);
//                        runtime.gc();
//                        max_memory_consumption = Math.max(max_memory_consumption, bytesToMegabytes(runtime.totalMemory() - runtime.freeMemory())) - initial_memory_used;

                        printWriter.append("=============================> File: " + file.getName() + "\r\n");
                        printWriter.append("Time slots : " + times.get(i).get_slots_number() + "\r\n" );
                        printWriter.append("Initial memory usage:" + initial_memory_used + "\r\n");
                        printWriter.append("Max Memory usage:" + max_memory_consumption + "\r\n");
                        if(total_queries > 0) {
                            printWriter.append("CPU time:" + (cpu_time / total_queries) + "\r\n");
                        }
                        printWriter.flush();

                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            }
        }
        printWriter.close();
    }
}
