import java.util.ArrayList;


public class GPSTrack {
	int id;
	ArrayList<GPSPoint> points;
	
	public GPSTrack(int id, ArrayList<GPSPoint> points) {
		this.id = id;
		this.points = points;
	}
	
	@Override
	public String toString() {
		String s = new String();
	
		s += id + ": ";
		for (int i = 0; i < points.size(); i++) {
			s+=points.get(i);
			if (i != points.size()-1) {
				s += ", ";
			}
		}
		return s;
	}
}
