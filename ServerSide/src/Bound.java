/**
 * This class for defending a bounding box for route search for example
 * @author Amin Mohammadpour
 */
public class Bound {
    GPSPoint min_gps_point, max_gps_point;

    public Bound(GPSPoint min_gps_point, GPSPoint max_gps_point) {
        this.min_gps_point = min_gps_point;
        this.max_gps_point = max_gps_point;
    }

    public boolean contains(double lat, double lng) {
        boolean result = false;
        if(lat >= min_gps_point.getLat() && lat <= max_gps_point.getLat() &&
                lng >= min_gps_point.getLng() && lng <= max_gps_point.getLng()){
            result = true;
        }
        return result;
    }
}
