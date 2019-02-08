import java.time.LocalDateTime;

public class GPSPoint {
    private double lat;
	private double lng;
	LocalDateTime timestamp;
	
	public GPSPoint(double lat, double lng, LocalDateTime timestamp) {
		this.lat = lat;
		this.lng = lng;
		this.timestamp = timestamp;
	}

    public GPSPoint(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
	public String toString() {
		return "(" + lat + ", " + lng + ", " + timestamp + ")";
	}
}
