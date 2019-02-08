import java.io.*;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.*;

/**
 * The graph loader
 * @author Amin Mohammadpour
 */
public class GraphLoader {
    /**
     * This method for loading graph files and constructing TAG or ATAG.
     * @param road_network_dir the directory maintaining the graph files such as 'Nodes.txt' and 'Edges.txt'.
     * @param G the graph instance needed to be populated
     * @param time_slots_of_day the time slots of the day instance needed to be populated.
     */
    public static void load(String road_network_dir, Graph G, Time_Slots_of_Day time_slots_of_day){
        File file = null;
        FileReader fr = null;
        BufferedReader br = null;
        String line;
        String tokens[];
        char type = 'r';
        File folder = new File(road_network_dir);
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                try {
                    file = new File(listOfFiles[i].getAbsolutePath());
                    fr = new FileReader(file);
                    br = new BufferedReader(fr);
                    if (listOfFiles[i].getName().endsWith("Nodes.txt")) {
                        type = 'n';
                    } else if (listOfFiles[i].getName().endsWith("Edges.txt")) {
                        type = 'e';
                    } else if (listOfFiles[i].getName().endsWith("EdgeGeometry.txt")) {
                        type = 'g';
                    } else if (listOfFiles[i].getName().endsWith("Attributes.txt")) {
                        type = 'a';
                    } else if(listOfFiles[i].getName().endsWith("Time_Slots.txt")){
                        type = 't';
                    }

                    switch (type) {
                        case 't':
                            TimeSlot time_slot = null;
                            while ((line = br.readLine()) != null) {
                                time_slot = new TimeSlot();
                                tokens = line.split(" ");
                                for (String token : tokens) {
                                    time_slot.set_time(LocalTime.parse(token));
                                }
                                time_slots_of_day.add_time_slot(time_slot);
                            }
                            break;

                        case 'n':
                            while ((line = br.readLine()) != null) {
                                tokens = line.split(" ");
                                G.add_node(Integer.parseInt(tokens[0]), new Node(Integer.parseInt(tokens[0]),
                                        Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2])));
                            }
                            break;

                        case 'e':
                            while ((line = br.readLine()) != null) {
                                tokens = line.split(" ");
                                G.add_edge(Integer.parseInt(tokens[0]), new Edge(Integer.parseInt(tokens[0]),
                                        Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2])));
                            }
                            break;

                        case 'g':
                            String filename = "D:\\UW\\FALL 2014\\PreGo\\ATAG2\\5.Attributes.txt";
                            PrintWriter printWriter = new PrintWriter(new FileWriter(filename));
                            StringBuilder sb = new StringBuilder();
                            int edge_id ;
                            float distance = 0f;
                            float travel_time = 0; // in minutes
                            ArrayList<GPSPoint> points = new ArrayList<GPSPoint>() ;
//                            0^Supermall Way^residential^18.076637118384^47.2964240^-122.2445086^47.2964237^-122.2442696
                            while ((line = br.readLine()) != null) {
                                tokens = line.split("\\^");
                                edge_id = Integer.parseInt(tokens[0]);
/*                                for (int j = 5; j < tokens.length ; j+=2) {
                                    points.add(new GPSPoint(Double.parseDouble(tokens[j-1]) ,
                                            Double.parseDouble(tokens[j])));
                                }
                                for (int j = 1; j < points.size() ; j++) {
                                   distance += Math.sqrt(Math.pow(points.get(j).getLat() - points.get(j-1).getLat() , 2) +
                                           Math.pow(points.get(j).getLng() - points.get(j-1).getLng() , 2));
                                }
                                travel_time = Math.round(distance / Constants.SPEED_THRESHOLD);*/

                                // add attributes to the designated edge

                                ArrayList<Float> random_weights = new ArrayList<Float>();
                                Random random = new Random();
                                float number ;
                                int max = 10;
                                sb.append(edge_id + " ");

//                                distance = new BigDecimal(random.nextFloat()).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
                                distance = Math.round(random.nextFloat() * 10f) / 10f;
                                if(distance < 0.1){
                                    distance+=0.2;
                                }
/*                                G.get_edge(edge_id).add_attribute("distance" , new Attribute("distance", Type.MIN ,
                                        new ArrayList<Float>(Collections.nCopies(time_slots_of_day.get_slots_number(), distance))));*/
                                sb.append("-a distance min ");
                                for (int j = 0; j < time_slots_of_day.get_slots_number() ; j++) {
                                    sb.append(distance + " ");
                                }

                                sb.append("-a travel_time min ");
                                for (int j = 0; j < time_slots_of_day.get_slots_number() ; j++) {
                                    number = (float) random.nextInt(max);
                                    if(number < 1){
                                        number++;
                                    }
                                    random_weights.add(number);
                                    sb.append(number + " ");
                                }
//                                G.get_edge(edge_id).add_attribute("travel_time" , new Attribute("travel_time", Type.MIN , random_weights));

                                sb.append("-a risk min ");
                                for (int j = 0; j < time_slots_of_day.get_slots_number() ; j++) {
                                    number = (float) random.nextInt(max);
                                    if(number < 1){
                                        number++;
                                    }
                                    random_weights.add(number);
                                    sb.append(number + " ");

                                }
//                                G.get_edge(edge_id).add_attribute("risk" , new Attribute("risk", Type.MIN , random_weights));

                                sb.append("-a attribute_4 min ");
                                for (int j = 0; j < time_slots_of_day.get_slots_number() ; j++) {
                                    number = (float) random.nextInt(max);
                                    if(number < 1){
                                        number++;
                                    }
                                    random_weights.add(number);
                                    sb.append(number + " ");
                                }
//                                G.get_edge(edge_id).add_attribute("attribute_4" , new Attribute("attribute_4", Type.MIN , random_weights));

                                sb.append("-a attribute_5 min ");
                                for (int j = 0; j < time_slots_of_day.get_slots_number() ; j++) {
                                    number = (float) random.nextInt(max);
                                    if(number < 1){
                                        number++;
                                    }
                                    random_weights.add(number);
                                    sb.append(number + " ");
                                }
//                                G.get_edge(edge_id).add_attribute("attribute_5" , new Attribute("attribute_5", Type.MIN , random_weights));

                                sb.append("-a attribute_6 min ");
                                for (int j = 0; j < time_slots_of_day.get_slots_number() ; j++) {
                                    number = (float) random.nextInt(max);
                                    if(number < 1){
                                        number++;
                                    }
                                    random_weights.add(number);
                                    sb.append(number + " ");
                                }
//                                G.get_edge(edge_id).add_attribute("attribute_6" , new Attribute("attribute_6", Type.MIN , random_weights));

                                sb.append("\r\n");
                                printWriter.print(sb.toString());
                                sb.delete(0, sb.length());
                            }
                            printWriter.close();
                            break;

                        case 'a':
                            Attribute attribute = null;
                            String [] attributes_weight ;

                            int edge_index;
                            while ((line = br.readLine()) != null) {
                                edge_index = Integer.parseInt(line.split(" ")[0]);
                                line = line.substring(line.indexOf("-a") + 3);
                                attributes_weight = line.split("-a ");
                                int j ;
                                String name = "";
                                for(String attr : attributes_weight) {
                                    j = 0;
                                    tokens = attr.split(" ");
                                    name = tokens[j++];
                                    attribute = new Attribute(name, time_slots_of_day.get_slots_number());
                                    if (tokens[j++].toUpperCase().equals("MAX")) {
                                        attribute.setType(Type.MAX);
                                    } else {
                                        attribute.setType(Type.MIN);
                                    }
                                    // for loop is for reading the weights
                                    if (name.equals("travel_time")) {
                                        while (j < tokens.length) {
                                            attribute.add_weight(Float.parseFloat(tokens[j]) * 60);
                                            j++;
                                        }
                                    } else {
                                        while (j < tokens.length) {
                                            attribute.add_weight(Float.parseFloat(tokens[j]));
                                            j++;
                                        }
                                    }
                                    G.get_edge(edge_index).add_attribute(attribute.getName(), attribute);
                                    //}
                                }
                            }
                            break;
                    }
                    br.close();
                    fr.close();
                }catch (IOException ex){
                    ex.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args){
        float f = Float.parseFloat("0.4");
        System.out.println(f);
    }
}