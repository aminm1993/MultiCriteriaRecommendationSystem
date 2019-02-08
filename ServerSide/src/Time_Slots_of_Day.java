import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * The distribution of the time slots of the day
 * @author Amin Mohammadpour
 */
public class Time_Slots_of_Day {
    private Map<Integer, TimeSlot> time_slots;

    public Time_Slots_of_Day(Map<Integer, TimeSlot> time_slots) {
        this.time_slots = time_slots;
    }

    public Time_Slots_of_Day() {
        time_slots = new HashMap<Integer, TimeSlot>();
    }

    public void add_time_slot(TimeSlot ts){
        int index = time_slots.size();
        time_slots.put(index, ts);
    }

    public Map<Integer, TimeSlot> getTime_slots() {
        return time_slots;
    }

    /**
     * Finds the corresponding time slot index for the given time
     * @param time the given time in hour:minute format
     * @return the index of the corresponding time slot, otherwise -1 when there is no corresponding time slot
     */
    public int get_time_slot_index(LocalTime time){
        for (int i = 0; i < time_slots.size() ; i++) {
            if(time_slots.get(i).does_include(time)){
                return i;
            }
        }
        System.out.println("get_time_slot_index = -1");
        return 1;
    }

    /**
     * Calculate the overlapping proportions of a given travel period time with the defined time slots of the day
     * @param start_time the start time of the travel
     * @param travel_time the duration of the travel
     * @param time_slot_index the index of the time slot of the day when the travel start.
     * @return HashMap with key set representing the indices of the overlapped time slots of the day
     * mapped to the overlapping proportion value.
     */
    public Map<Integer,Double> get_time_slots_distribution(LocalTime start_time, LocalTime travel_time, int time_slot_index){
        Map result = new HashMap<Integer, Double>();
        LocalTime end_time = start_time.plusHours(travel_time.getHour()).plusMinutes(travel_time.getMinute());
        if(time_slots.get(time_slot_index).does_include(end_time)){
            result.put(time_slot_index, 1.0);
        }
        else{
            while(!time_slots.get(time_slot_index).does_include(end_time)) {
                result.put(time_slot_index, time_slots.get(time_slot_index).overlap_proportion(start_time, end_time));
                time_slot_index++;
            }
            result.put(time_slot_index, time_slots.get(time_slot_index).overlap_proportion(start_time, end_time));
            result.put(time_slot_index + 1, time_slots.get(time_slot_index + 1).overlap_proportion(start_time, end_time));
        }
        return result;
    }

    /**
     * Calculate the overlapping proportions of a given travel period time with the defined time slots of the day
     * @param start_time the start time of the travel
     * @param edge the passed edge
     * @param time_slot_index the index of the time slot of the day when the travel start.
     * @return HashMap with key set representing the indices of the overlapped time slots of the day
     * mapped to the overlapping proportion value.
     */
    public Map<Integer,Double> get_time_slots_distribution(LocalTime start_time, Edge edge, int time_slot_index){
        // fist find the arrival time, and then calculate the overlapping proportion so all will be on the same scale.
        //Map temp = new HashMap<Integer, Double>();
        ArrayList<Integer> overlapped_time_slots_indices = new ArrayList<Integer>();
        Map result = new HashMap<Integer, Double>();
        LocalTime travel_time, end_time;
        int traveled_time_in_minutes = 0;
//        double overlapping_proportion;
        int overlapping_minutes;
        time_slot_index--;

        do{
            time_slot_index++;
            // this is to handle the case of overlapping two time slots on different day
            if(time_slot_index == time_slots.size()){
                time_slot_index = 0;
            }
            travel_time = edge.get_travel_time(time_slot_index);
            end_time = start_time.plusHours(travel_time.getHour()).plusMinutes(travel_time.getMinute()).minusMinutes(traveled_time_in_minutes);

/*            overlapping_proportion = time_slots.get(time_slot_index).overlap_proportion(start_time, end_time);
            temp.put(time_slot_index, overlapping_proportion);
            traveled_time_in_minutes += overlapping_proportion * edge.get_travel_time_in_minutes(time_slot_index);*/

            overlapped_time_slots_indices.add(time_slot_index);
            overlapping_minutes = time_slots.get(time_slot_index).overlap_minutes(start_time, end_time);
            traveled_time_in_minutes += overlapping_minutes;

        }while(!time_slots.get(time_slot_index).does_include(end_time));

        Iterator<Integer> time_slots_iterator = overlapped_time_slots_indices.iterator();
        int index ;
        while(time_slots_iterator.hasNext()){
            index = time_slots_iterator.next();
            result.put(index, Math.round(time_slots.get(index).overlap_proportion(start_time, end_time) * 100.0) / 100.0);
        }
        return result;
    }

    /**
     * Get the number of the time slots of the day
     * @return the count of the time slots of the day
     */
    public int get_slots_number(){
        return time_slots.size();
    }

    @Override
    public String toString() {
        return "Time_Slots_of_Day{" +
                "time_slots=" + time_slots +
                '}';
    }
}
