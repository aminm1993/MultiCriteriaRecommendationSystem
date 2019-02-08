package ir.mohammadpour.app.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import com.akexorcist.googledirection.model.Direction;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

import ir.mohammadpour.app.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SabegheAdapter extends BaseAdapter {

    private Activity activity;

    List<Sabeghe> arrayKala;
    Bundle bundle;

    private static LayoutInflater inflater = null;

    public SabegheAdapter(Activity a, List<Sabeghe> arrkala,Bundle _bundle) {

        activity = a;

        bundle=_bundle;
        arrayKala = arrkala;

        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return arrayKala.size();
    }

    public Object getItem(int position) {
        return arrayKala.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
    class Holder
    {
        TextView tarihk;
        TextView tarihk2;

        TextView mabda;
        TextView mabda2;

        TextView maghsad;
        TextView maghsad2;

        TextView maghsaddovvom;
        TextView maghsaddovvom2;

        TextView bar;
        TextView bar2;

        TextView txtRaftOBargasht;
        TextView txtRaftOBargasht2;

        TextView tavaghof;
        TextView tavaghof2;

        TextView takhfif;
        TextView takhfif2;

        TextView price;
        TextView price2;

        LinearLayout linearMaghsadDovvom;

        MapView mMapView;
        GoogleMap googleMap;
    }


    public View getView(final int position, View convertView, ViewGroup parent) {

        try {

        if(convertView!=null)
            return convertView;

            final Holder holder=new Holder();
            final View rowView;
            rowView = inflater.inflate(R.layout.sabeghe_item, null);
            holder.tarihk=(TextView)rowView.findViewById(R.id.txtdarkhast) ;
            holder.tarihk2=(TextView)rowView.findViewById(R.id.txtdarkhast2) ;

            holder.mabda=(TextView)rowView.findViewById(R.id.txtMabda) ;
            holder.mabda2=(TextView)rowView.findViewById(R.id.txtMabda2) ;

            holder.txtRaftOBargasht=(TextView)rowView.findViewById(R.id.txtRaftOBargasht);
            holder.txtRaftOBargasht2=(TextView)rowView.findViewById(R.id.txtRaftOBargasht2);

            holder.bar=(TextView)rowView.findViewById(R.id.txtBar);
            holder.bar2=(TextView)rowView.findViewById(R.id.txtBar2);

            holder.tavaghof=(TextView)rowView.findViewById(R.id.txtTavaghof);
            holder.tavaghof2=(TextView)rowView.findViewById(R.id.txtTavaghof2);

            holder.takhfif=(TextView)rowView.findViewById(R.id.txtTakhfif);
            holder.takhfif2=(TextView)rowView.findViewById(R.id.txtTakhfif2);

            holder.maghsad=(TextView)rowView.findViewById(R.id.txtMaghsad) ;
            holder.maghsad2=(TextView)rowView.findViewById(R.id.txtMaghsad2) ;

            holder.price=(TextView)rowView.findViewById(R.id.txtGheymat) ;
            holder.price2=(TextView)rowView.findViewById(R.id.txtGheymat2) ;

            holder.linearMaghsadDovvom=(LinearLayout)rowView.findViewById(R.id.linearMaghsadDovvom);
            holder.maghsaddovvom=(TextView)rowView.findViewById(R.id.maghsadDovvom) ;
            holder.maghsaddovvom2=(TextView)rowView.findViewById(R.id.maghsadDovvom2) ;

            Typeface type = Typeface.createFromAsset(rowView.getContext().getAssets(),"IRANSans.ttf");
            Typeface type2 = Typeface.createFromAsset(rowView.getContext().getAssets(),"Regular.ttf");

            holder.tarihk.setTypeface(type);
            holder.tarihk2.setTypeface(type);

            holder.mabda.setTypeface(type);
            holder.mabda2.setTypeface(type);

            holder.maghsad.setTypeface(type);
            holder.maghsad2.setTypeface(type);

            holder.price.setTypeface(type2);
            holder.price2.setTypeface(type);

            holder.maghsaddovvom.setTypeface(type);
            holder.maghsaddovvom2.setTypeface(type);

            holder.bar.setTypeface(type);
            holder.bar2.setTypeface(type);

            holder.txtRaftOBargasht2.setTypeface(type);
            holder.txtRaftOBargasht.setTypeface(type);

            holder.takhfif.setTypeface(type);
            holder.takhfif2.setTypeface(type);

            holder.tavaghof2.setTypeface(type);
            holder.tavaghof.setTypeface(type2);

            holder.tarihk.setText(arrayKala.get(position).Created2);
            holder.mabda.setText(arrayKala.get(position).LocationMabda);
            holder.maghsad.setText(arrayKala.get(position).LocationMaghsad);


            DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
            DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();//new DecimalFormatSymbols();
            symbols.setGroupingSeparator(',');
            DecimalFormat df = new DecimalFormat();
            df.setDecimalFormatSymbols(symbols);
            df.setGroupingSize(3);
            holder.price.setText(df.format((long)arrayKala.get(position).Price)+" ریال");


            if(arrayKala.get(position).Second_Dest_Lat==null || arrayKala.get(position).Second_Dest_Lat.equals("null")
                    || arrayKala.get(position).Second_Dest_Lat.equals(""))
            {
                holder.linearMaghsadDovvom.setVisibility(View.GONE);
            }
            else
            {
                holder.maghsaddovvom.setText(arrayKala.get(position).LocationMaghsadDovvom);
            }


            if(arrayKala.get(position).Bar)
                holder.bar.setText("دارد");
            else
                holder.bar.setText("-");

            if(arrayKala.get(position).WastedTimeCode==-1)
                holder.tavaghof.setText("-");
            else
                holder.tavaghof.setText(GetTavagofTime(arrayKala.get(position).WastedTimeCode));

            if(arrayKala.get(position).UniqueID==null || arrayKala.get(position).UniqueID.equals("null")
                    || arrayKala.get(position).UniqueID.equals(""))
            {
                holder.takhfif.setText("-");
            }
            else
            {
                holder.takhfif.setText("دارد");
            }

            if(arrayKala.get(position).RaftBargasht)
                holder.txtRaftOBargasht.setText("دارد");
            else
                holder.txtRaftOBargasht.setText("-");




            holder.mMapView = (MapView) rowView.findViewById(R.id.mapViewDetail);

        try{

            holder.mMapView.onCreate(bundle);
            holder.mMapView.onResume();
        }
        catch (OutOfMemoryError ex){}
        catch(Exception ex){

        }
        try {
            MapsInitializer.initialize(rowView.getContext());
        } catch (Exception e) {
            e.printStackTrace();

        }
        // if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        //  requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        // else
        {


            holder.mMapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap mMap) {
                    holder.googleMap = mMap;
                    holder.googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                    holder.googleMap.getUiSettings().setZoomControlsEnabled(false);
                    holder.googleMap.setTrafficEnabled(false);

                    Marker from= holder.googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.valueOf(arrayKala.get(position).Customer_lat), Double.valueOf(arrayKala.get(position).Customer_lng))));
                    final Marker to= holder.googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.valueOf(arrayKala.get(position).Dest_lat), Double.valueOf(arrayKala.get(position).Dest_lng))));









                    //googleMap.setMyLocationEnabled(false);
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    // for (Marker marker : markers) {
                    // builder.include(marker.getPosition());
                    //}
                    builder.include(new LatLng(Double.valueOf(arrayKala.get(position).Customer_lat), Double.valueOf(arrayKala.get(position).Customer_lng)));
                    builder.include(new LatLng(Double.valueOf(arrayKala.get(position).Dest_lat), Double.valueOf(arrayKala.get(position).Dest_lng)));
                    if(arrayKala.get(position).Second_Dest_Lat==null || arrayKala.get(position).Second_Dest_Lat.equals("null")
                            || arrayKala.get(position).Second_Dest_Lat.equals(""))
                    {

                    }
                    else
                    {
                        builder.include(new LatLng(Double.valueOf(arrayKala.get(position).Second_Dest_Lat), Double.valueOf(arrayKala.get(position).Second_Dest_Lng)));
                    }

                    LatLngBounds bounds = builder.build();
                    //int padding = 40; // offset from edges of the map in pixels
                    int width = holder.mMapView.getResources().getDisplayMetrics().widthPixels;
                    int height = holder.mMapView.getResources().getDisplayMetrics().heightPixels;
                    int padding = (int) (width * 0.3); // offset from edges of the map 12% of screen

                    //Toast.makeText(v.getContext(),width+"  "+height,Toast.LENGTH_SHORT).show();
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);

                    //CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                    mMap.animateCamera(cu);
                    mMap.setMaxZoomPreference(14);

                    ////////////
                    //drawPoly(from,to,holder.googleMap);
                    showCurvedPolyline(from.getPosition(),to.getPosition(),0.2,holder.googleMap);
                    if(arrayKala.get(position).Second_Dest_Lat==null || arrayKala.get(position).Second_Dest_Lat.equals("null")
                            || arrayKala.get(position).Second_Dest_Lat.equals(""))
                    {

                    }
                    else
                    {
                        //loc2
                        Marker to2= holder.googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.valueOf(arrayKala.get(position).Second_Dest_Lat), Double.valueOf(arrayKala.get(position).Second_Dest_Lng))));
                        // drawPoly(to,to2,holder.googleMap);
                        showCurvedPolyline(to.getPosition(),to2.getPosition(),0.2,holder.googleMap);
                    }

                    mMap.getUiSettings().setAllGesturesEnabled(true);
                    // For showing a move to my location button
                    // For dropping a marker at a point on the Map
                    //LatLng sydney = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                    //googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

                    // For zooming automatically to the location of the marker
                    //CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(14).build();
                    //googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));




                   /* mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            return true;
                        }
                    });
                    */

                }
            });



        }



        return rowView;
        }

        catch (NullPointerException ex)
        {
            return  null;

        }

    }

    String GetTavagofTime(int id)
    {


        if (id == 0)
            return "5 دقیقه";
        else if (id == 1)
            return "10 دقیقه";
        else if (id == 2)
            return "20 دقیقه";
        else if (id == 3)
            return "30 دقیقه";
        else if (id == 4)
            return "45 دقیقه";
        else if (id == 5)
            return "60 دقیقه";
        else
            return "بیشتر از 60 دقیقه";
    }

    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }
    Polyline showCurvedPolyline (LatLng p1, LatLng p2, double k, GoogleMap map) {
        //Calculate distance and heading between two points
        double d = SphericalUtil.computeDistanceBetween(p1,p2);
        double h = SphericalUtil.computeHeading(p1, p2);

        //Midpoint position
        LatLng p = SphericalUtil.computeOffset(p1, d*0.5, h);

        //Apply some mathematics to calculate position of the circle center
        double x = (1-k*k)*d*0.5/(2*k);
        double r = (1+k*k)*d*0.5/(2*k);

        LatLng c = SphericalUtil.computeOffset(p, x, h + 90.0);

        //Polyline options
        PolylineOptions options = new PolylineOptions();
        List<PatternItem> pattern = Arrays.<PatternItem>asList(new Dash(30), new Gap(20));

        //Calculate heading between circle center and two points
        double h1 = SphericalUtil.computeHeading(c, p1);
        double h2 = SphericalUtil.computeHeading(c, p2);

        //Calculate positions of points on circle border and add them to polyline options
        int numpoints = 100;
        double step = (h2 -h1) / numpoints;

        for (int i=0; i < numpoints; i++) {
            LatLng pi = SphericalUtil.computeOffset(c, r, h1 + i * step);
            options.add(pi);
        }

        //Draw polyline
        return map.addPolyline(options.width(10).color(Color.BLACK).geodesic(false).pattern(pattern));

    }
    void drawPoly(Marker from, Marker to, GoogleMap map)
    {
        double cLat = ((from.getPosition().latitude + to.getPosition().latitude) / 2);
        double cLon = ((from.getPosition().longitude + from.getPosition().longitude) / 2);

        //add skew and arcHeight to move the midPoint
        if(Math.abs(from.getPosition().longitude - from.getPosition().longitude) < 0.0001){
            cLon -= 0.0195;
        } else {
            cLat += 0.0195;
        }

        double tDelta = 1.0/50;
        List<LatLng> alLatLng=new ArrayList<>();
        for (double t = 0;  t <= 1.0; t+=tDelta) {
            double oneMinusT = (1.0-t);
            double t2 = Math.pow(t, 2);
            double lon = oneMinusT * oneMinusT * from.getPosition().longitude
                    + 2 * oneMinusT * t * cLon
                    + t2 * to.getPosition().longitude;
            double lat = oneMinusT * oneMinusT * from.getPosition().latitude
                    + 2 * oneMinusT * t * cLat
                    + t2 * to.getPosition().latitude;
            alLatLng.add(new LatLng(lat, lon));
        }

        // draw polyline
        PolylineOptions line = new PolylineOptions();
        line.width(5);
        line.color(Color.RED);
        line.addAll(alLatLng);
        map.addPolyline(line);
    }
}
