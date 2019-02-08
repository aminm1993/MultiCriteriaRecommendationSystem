package ir.mohammadpour.app.ui.util;

/**
 * Created by Amin on 2017-01-25.
 */
public class CalcDistance {

    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }





        private static final int earthRadius = 6371;
        public static double calculateDistance(double lat1, double lon1, double lat2, double lon2)
        {
            double dLat = Math.toRadians(lat2 - lat1);
            double dLon =  Math.toRadians(lon2 - lon1);
            double a =
                     (Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1))
                            * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2));
            double c = (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)));
            double d = earthRadius * c;
            return d;
        }

}
