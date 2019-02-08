import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalTime;
import java.util.*;

/**
 * @author Amin Mohammadpour
 */
public class QueryGenerator {
    public static void generate_old(final Graph G, int queries, final double distance){
        try {
            String filename = "D:\\UW\\FALL 2014\\PreGo\\ATAG2\\queries_" + queries + ".txt";
            final PrintWriter printWriter = new PrintWriter(new FileWriter(filename));
            ArrayList<Thread> branching_threads = new ArrayList<Thread>(queries);
            Random random = new Random();
            Random random_distance = new Random();
            int node_id;
            int max = 535451;
            ArrayList<Integer> nominated_sources = new ArrayList<Integer>();

            while(branching_threads.size() < queries) {
                do {
                    node_id = random.nextInt(max);
                } while (nominated_sources.contains(node_id));

                nominated_sources.add(node_id);

                final Node source_node = G.get_node(node_id);
                branching_threads.add(new Thread() {
                    public void run() {
                        try {
                            double dist, theta;
                            Node neighbor = source_node ;
                            ArrayList<Integer> visited_nodes = new ArrayList<Integer>();
                            visited_nodes.add(source_node.getId());
                            Iterator<Integer> out_edges_iterator;
                            do {
                                out_edges_iterator = neighbor.getOut_edges().iterator();
                                while(out_edges_iterator.hasNext() && visited_nodes.contains(neighbor.getId())) {
                                    neighbor = G.get_node(G.get_edge(out_edges_iterator.next()).getNode_2_id());
                                }

                                theta = source_node.getLng() - neighbor.getLng();
                                dist = Math.sin(deg2rad(source_node.getLat())) * Math.sin(deg2rad(neighbor.getLat())) +
                                        Math.cos(deg2rad(source_node.getLat())) * Math.cos(deg2rad(neighbor.getLat())) * Math.cos(deg2rad(theta));
                                dist = Math.acos(dist);
                                dist = rad2deg(dist);
                                dist = dist * 60 * 1.1515;

//                            float d = (float) Math.sqrt(Math.pow(neighbor.getLat() - source_node.getLat(), 2) +
//                                    Math.pow(neighbor.getLng() - source_node.getLng(), 2));
                            } while (dist < distance);

                            if (dist >= distance) {
                                printWriter.print(source_node.getId() + " " + neighbor.getId());
                                printWriter.flush();
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }

            for (Thread thread : branching_threads) {
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            printWriter.close();
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }
    public static void generate(String dir,final Graph G, final Time_Slots_of_Day time_slots_of_day, int queries, final double distance){
        try {
            String filename = dir + "queries_" + queries + "_distance_" + distance + ".txt";
            final PrintWriter printWriter = new PrintWriter(new FileWriter(filename));
            ArrayList<Thread> branching_threads = new ArrayList<Thread>(queries);
            Random random = new Random();
            int node_id;
            int max = 535451;
            final double min_lat = 46.877743;
            final double min_lng = -122.686678;
            final double max_lat = 47.791138;
            final double max_lng = -121.972370;
            ArrayList<Integer> nominated_sources = new ArrayList<Integer>();
            while(branching_threads.size() < queries) {
                do {
                    node_id = random.nextInt(max);
                } while (nominated_sources.contains(node_id)
                        && G.get_node(node_id).getLat() >= min_lat && G.get_node(node_id).getLat() <= max_lat
                        && G.get_node(node_id).getLng() >= min_lng && G.get_node(node_id).getLng() <= max_lng);

                nominated_sources.add(node_id);
                final Node source_node = G.get_node(node_id);

                branching_threads.add(new Thread() {
                    public void run() {
                        try {
                            double dist, theta;
                            Node neighbor ;
                            Iterator<Integer> out_edges_iterator;
                            boolean pass;
                            int paths = 0;
                            if(distance > 0) {
                                for (int i = 0; i < G.getNodes().size(); i++) {
                                    if (i != source_node.getId() &&
                                            G.get_node(i).getLat() >= min_lat && G.get_node(i).getLat() <= max_lat
                                            && G.get_node(i).getLng() >= min_lng && G.get_node(i).getLng() <= max_lng) {
                                        neighbor = G.get_node(i);

                                        theta = source_node.getLng() - neighbor.getLng();
                                        dist = Math.sin(deg2rad(source_node.getLat())) * Math.sin(deg2rad(neighbor.getLat())) +
                                                Math.cos(deg2rad(source_node.getLat())) * Math.cos(deg2rad(neighbor.getLat())) * Math.cos(deg2rad(theta));
                                        dist = Math.acos(dist);
                                        dist = rad2deg(dist);
                                        dist = dist * 60 * 1.1515;
                                        dist = Math.round(dist * 10.0) / 10.0;

                                        if (dist >= distance && dist < (distance + 0.3)) {
                                            pass = TP_SP_Serial_Simple.find_optimal_route(G, time_slots_of_day, source_node.getId(),
                                                    neighbor.getId(), LocalTime.of(5, 30), new String[]{"distance", "travel_time", "risk"});
                                            if (pass) {
                                                printWriter.print(source_node.getId() + " " + neighbor.getId() + " " + dist + "\r\n");
                                                printWriter.flush();
                                                if (paths > 2) {
                                                    break;
                                                }
                                                paths++;
                                            }
                                        }
                                    }
                                }
                            }
                            else{
                                Random random_distance = new Random();
                                double new_distance;
                                for (int i = 0; i < G.getNodes().size() ; i++) {
                                    if(i != source_node.getId() &&
                                            G.get_node(i).getLat() >= min_lat && G.get_node(i).getLat() <= max_lat
                                            && G.get_node(i).getLng() >= min_lng && G.get_node(i).getLng() <= max_lng) {
                                        neighbor = G.get_node(i);

                                        theta = source_node.getLng() - neighbor.getLng();
                                        dist = Math.sin(deg2rad(source_node.getLat())) * Math.sin(deg2rad(neighbor.getLat())) +
                                                Math.cos(deg2rad(source_node.getLat())) * Math.cos(deg2rad(neighbor.getLat())) * Math.cos(deg2rad(theta));
                                        dist = Math.acos(dist);
                                        dist = rad2deg(dist);
                                        dist = dist * 60 * 1.1515;
                                        dist = Math.round(dist * 10.0) / 10.0;

                                        new_distance = (Math.round(random_distance.nextInt(15 - 1) + 1) * 10.0) / 10.0;
                                        if (dist >= new_distance && dist < (new_distance + 0.3)) {
                                            pass = TP_SP_Serial_Simple.find_optimal_route(G, time_slots_of_day, source_node.getId(),
                                                    neighbor.getId(), LocalTime.of(5, 30), new String[]{"distance", "travel_time", "risk"});
                                            if(pass) {
                                                printWriter.print(source_node.getId() + " " + neighbor.getId() + " " + dist + "\r\n");
                                                printWriter.flush();
                                                if(paths > 2) {
                                                    break;
                                                }
                                                paths++;
                                            }
                                        }
                                    }
                                }
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }

            for (Thread thread : branching_threads) {
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            printWriter.close();
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    public static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    public static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    public static void main(String[] args){
        Random random = new Random();
        for (int i = 0; i < 50; i++) {
            System.out.println((Math.round(random.nextInt(50 - 1) + 1) * 10.0) / 10.0);
        }
    }


}
