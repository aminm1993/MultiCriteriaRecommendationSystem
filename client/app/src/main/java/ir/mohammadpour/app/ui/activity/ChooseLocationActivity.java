package ir.mohammadpour.app.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;

import ir.mohammadpour.app.R;
import ir.mohammadpour.app.model.SelectedAddressModel;
import ir.mohammadpour.app.ui.util.LocationService;
import ir.mohammadpour.app.ui.util.PermissionUtils;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

public class ChooseLocationActivity extends AppCompatActivity  implements OnMapReadyCallback{

    SharedPreferences prefs;
    ImageView pin;
    GoogleMap mMap;
    SupportMapFragment mapFragment;
    Context c;
    boolean from_first_activity=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        setContentView(R.layout.activity_choose_location);

        c=this;
        prefs= PreferenceManager.getDefaultSharedPreferences(this);
        pin = (ImageView) findViewById(R.id.iv_pin);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


       Bundle bundle=  getIntent().getExtras();
       if(bundle!=null)
       {
           from_first_activity=  bundle.getBoolean("from_first_activity",false);
       }

       if(!from_first_activity) {
           new MaterialTapTargetPrompt.Builder(ChooseLocationActivity.this)
                   .setTarget(pin)
                   .setPrimaryText("راهنمای ثبت آدرس")
                   .setSecondaryText("برای ثبت آدرس ابتدا نقشه را جا به جا کرده و سپس دکمه تایید آدرس را بزنید")
                   .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                       @Override
                       public void onPromptStateChanged(MaterialTapTargetPrompt prompt, int state) {
                           if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED) {
                               // User has pressed the prompt target
                           }
                       }
                   })
                   .show();
       }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //mMap.getUiSettings().setZoomControlsEnabled(true);
        //mMap.getUiSettings().setMyLocationButtonEnabled(true);


        onmap_load_and_have_permission();





        pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!from_first_activity) {
                    final Typeface face = Typeface.createFromAsset(getAssets(),
                            "IRANSans.ttf");
                    final Typeface face2 = Typeface.createFromAsset(getAssets(),
                            "SOGAND.ttf");

                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(c);
                    LayoutInflater inflater = getLayoutInflater();
                    final View dialog = inflater.inflate(R.layout.add_address_custom_dialog, null);
                    dialogBuilder.setView(dialog);

                    final EditText name = (EditText) dialog.findViewById(R.id.editname);
                    final EditText desc = (EditText) dialog.findViewById(R.id.editdesc);
                    final TextView title = (TextView) dialog.findViewById(R.id.title);
                    Button button3 = (Button) dialog.findViewById(R.id.button3);
                    title.setTypeface(face2);
                    button3.setTypeface(face);
                    name.setTypeface(face);
                    desc.setTypeface(face);
                    final AlertDialog alertDialog = dialogBuilder.create();
                    button3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String last_id = prefs.getString("last_id", null);
                            if (last_id == null || last_id.equals(""))
                                last_id = "1";
                            else {
                                int x = Integer.parseInt(last_id);
                                x++;
                                last_id = x + "";
                            }
                            SelectedAddressModel n = new SelectedAddressModel();
                            n.Name = name.getText().toString();
                            n.lng = mMap.getCameraPosition().target.longitude + "";
                            n.lat = mMap.getCameraPosition().target.latitude + "";
                            n.description = desc.getText().toString();
                            //n.id=last_id;
                            n.save();
                            Toast.makeText(c, "آدرس شما با موفقیت ثبت شد", Toast.LENGTH_LONG).show();

                            alertDialog.dismiss();
                            finish();
                        }
                    });

                    alertDialog.show();


                    //String x= mMap.getCameraPosition().target.latitude+"~"+ mMap.getCameraPosition().target.longitude;
                } else {

                    getIntent().putExtra("Lat",mMap.getCameraPosition().target.latitude + "" );
                    getIntent().putExtra("Lng",mMap.getCameraPosition().target.longitude + "");

                    setResult(RESULT_OK, getIntent());
                    finish();
                }
            }
        });

    }

   void onmap_load_and_have_permission()
   {
       if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
           // TODO: Consider calling
           //    ActivityCompat#requestPermissions
           // here to request the missing permissions, and then overriding
           //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
           //                                          int[] grantResults)
           // to handle the case where the user grants the permission. See the documentation
           // for ActivityCompat#requestPermissions for more details.

           PermissionUtils.requestPermission(this, REQUEST_CODE_ASK_PERMISSIONS,
                   Manifest.permission.ACCESS_FINE_LOCATION, true);
           return;
       }
       try {
           LocationService s = new LocationService(this);
           Location mLastLocation = s.getLocation();


           if (mMap != null) {
               LatLng latLong;


               latLong = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

               CameraPosition cameraPosition = new CameraPosition.Builder()
                       .target(latLong).zoom(15f).build();

               mMap.setMyLocationEnabled(true);
               mMap.animateCamera(CameraUpdateFactory
                       .newCameraPosition(cameraPosition));
           }
       } catch (Exception ex) {


           String Lat=  prefs.getString("Server_Lat", null);
           String Lng=prefs.getString("Server_Lng", null);


           LatLng newLatLngTemp = new LatLng(Double.parseDouble(Lat),Double.parseDouble(Lng));
           CameraPosition cameraPosition = new CameraPosition.Builder()
                   .target(newLatLngTemp).zoom(15f).build();
           mMap.animateCamera(CameraUpdateFactory
                   .newCameraPosition(cameraPosition));
       }
   }
    final int REQUEST_CODE_ASK_PERMISSIONS = 1;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Toast.makeText(this, "Call Permission Granted..Please dial again.", Toast.LENGTH_SHORT).show();

                    onmap_load_and_have_permission();
                } else {
                    Toast.makeText(this, "Call permission not granted", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
