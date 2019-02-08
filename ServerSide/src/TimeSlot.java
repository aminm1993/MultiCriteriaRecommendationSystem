import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * This defines the unit of the time of the day
 * @author Amin Mohammadpour
 */
public class TimeSlot {
    private LocalTime from, to;
    private String description;

    public TimeSlot(LocalTime from, LocalTime to, String description) {
        this.from = from;
        this.to = to;
        this.description = description;
    }

    public TimeSlot() {
    }

    public LocalTime getFrom() {
        return from;
    }

    public void setFrom(LocalTime from) {
        this.from = from;
    }

    public LocalTime getTo() {
        return to;
    }

    public void setTo(LocalTime to) {
        this.to = to;
    }

    /**
     * To populate this.from and this.to on consecutive call
     * @param time the given time in hour:minute format
     */
    public void set_time(LocalTime time){
        if(from == null){
            from = time;
        }
        else{
            to = time;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * To check if a given time is falling within this time slot
     * @param time the given time in hour:minute format
     * @return true if the given time is within this time slot, otherwise false
     */
    public boolean does_include(LocalTime time){
        if(from.equals(LocalTime.of(0,0))) {
            if(time.equals(LocalTime.of(0,0))){
                return true;
            }
            else {
                return time.isAfter(from) && time.isBefore(to);
            }
        }
        else{
            return time.isAfter(from.minusMinutes(1)) && time.isBefore(to);
        }
    }

    /**
     * Calculate the overlapping proportion of a given period with this time slot
     * @param start_time the start time of the given period
     * @param end_time the end time of the given period
     * @return the proportion of the overlapping
     */
    public double overlap_proportion(LocalTime start_time, LocalTime end_time){
        double total_minutes = start_time.until(end_time, ChronoUnit.MINUTES);
        double overlapping_minutes = 0;
        if(end_time.isAfter(to)) {
            overlapping_minutes = start_time.until(to, ChronoUnit.MINUTES);
        }
        else if(start_time.isBefore(from) && end_time.isBefore(to)){
            overlapping_minutes = from.until(end_time, ChronoUnit.MINUTES);
        }
        else if(start_time.isAfter(from) && end_time.isBefore(to)){
            overlapping_minutes = total_minutes;
        }
        return overlapping_minutes / total_minutes;
    }

    /**
     * Calculate the overlapping minutes of a given period with this time slot
     * @param start_time the start time of the given period
     * @param end_time the end time of the given period
     * @return the minutes of the overlapping
     */
    public int overlap_minutes(LocalTime start_time, LocalTime end_time){
        int overlapping_minutes = 0;
        if(end_time.isAfter(to)) {
            overlapping_minutes = (int)start_time.until(to, ChronoUnit.MINUTES);
        }
        else if(start_time.isBefore(from) && end_time.isBefore(to)){
            overlapping_minutes = (int)from.until(end_time, ChronoUnit.MINUTES);
        }
        else if(start_time.isAfter(from) && end_time.isBefore(to)){
            overlapping_minutes = (int) start_time.until(end_time, ChronoUnit.MINUTES);
        }
        return overlapping_minutes;
    }

    @Override
    public String toString() {
        return "TimeSlot{" +
                "from=" + from +
                ", to=" + to +
                ", description='" + description + '\'' +
                '}';
    }

    public static void main(String[]args){
        LocalTime from = LocalTime.of(0,30);
        LocalTime to = LocalTime.of(6,0);
        System.out.println(LocalTime.of(3,30).isAfter(from.minusMinutes(1)));
    }
}
