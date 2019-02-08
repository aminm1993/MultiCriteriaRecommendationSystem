package ir.mohammadpour.app.model;

import java.util.List;

/**
 * Created by Amin on 2/17/2018.
 */

public class RouteModel {
    public class GeocodedWaypoint
    {
        public String geocoder_status ;
        public String place_id ;
        public List<String> types ;
    }

    public class Northeast
    {
        public double lat ;
        public double lng ;
    }

    public class Southwest
    {
        public double lat ;
        public double lng ;
    }

    public class Bounds
    {
        public Northeast northeast ;
        public Southwest southwest ;
    }

    public class Distance
    {
        public String text ;
        public int value ;
    }

    public class Duration
    {
        public String text ;
        public int value ;
    }

    public class EndLocation
    {
        public double lat ;
        public double lng ;
    }

    public class StartLocation
    {
        public double lat ;
        public double lng ;
    }

    public class Distance2
    {
        public String text ;
        public int value ;
    }

    public class Duration2
    {
        public String text ;
        public int value ;
    }

    public class EndLocation2
    {
        public double lat ;
        public double lng ;
    }

    public class Polyline
    {
        public String points ;
    }

    public class StartLocation2
    {
        public double lat ;
        public double lng ;
    }

    public class Step
    {
        public Distance2 distance ;
        public Duration2 duration ;
        public EndLocation2 end_location ;
        public String html_instructions ;
        public Polyline polyline ;
        public StartLocation2 start_location ;
        public String travel_mode ;
        public String maneuver ;
    }

    public class Leg
    {
        public Distance distance ;
        public Duration duration ;
        public String end_address ;
        public EndLocation end_location ;
        public String start_address ;
        public StartLocation start_location ;
        public List<Step> steps ;
      //  public List<String> traffic_speed_entry ;
      //  public List<String> via_waypoint ;
    }

    public class OverviewPolyline
    {
        public String points ;
    }

    public class Route
    {
        public Bounds bounds ;
        public String copyrights ;
        public List<Leg> legs ;
        public OverviewPolyline overview_polyline ;
        public String summary ;
      //  public List<object> warnings ;
      //  public List<object> waypoint_order ;
    }

    public class RootObject
    {
        public List<GeocodedWaypoint> geocoded_waypoints ;
        public List<Route> routes ;
        public String status ;
    }

    public class Station
    {
        public  int Id;
        public  String StationName;
        public  int StationPortsCount;
        public  double SOC ;
        public  double Lat;
        public  double Lng ;

    }
    public class CompleteDirection
    {
        public CompleteDirection(){}
        public RootObject OriToStation;
        public RootObject StationToDest;
        public Station station;
    }
}
