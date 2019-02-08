package ir.mohammadpour.app.ui.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomButtonsController;
import android.widget.ZoomControls;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.activeandroid.util.ReflectionUtils;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.stfalcon.frescoimageviewer.ImageViewer;


import cn.pedant.SweetAlert.SweetAlertDialog;
import in.goodiebag.carouselpicker.CarouselPicker;
import ir.mohammadpour.app.R;
import ir.mohammadpour.app.constants.NetworkConstants;
import ir.mohammadpour.app.model.Price;
import ir.mohammadpour.app.model.RouteModel;
import ir.mohammadpour.app.model.SelectedAddressModel;
import ir.mohammadpour.app.network.BaseServer;
import ir.mohammadpour.app.network.ServiceGenerator;
import ir.mohammadpour.app.ui.adapter.PlaceAutocompleteAdapter;
import ir.mohammadpour.app.ui.adapter.RouteAdapter;
import ir.mohammadpour.app.ui.adapter.ShareIntentListAdapter;
import ir.mohammadpour.app.ui.util.CalcDistance;
import ir.mohammadpour.app.ui.util.CancelTransfer;
import ir.mohammadpour.app.ui.util.ConnectionDetector;
import ir.mohammadpour.app.ui.util.Convert;
import ir.mohammadpour.app.ui.util.DirectionsJSONParser;
import ir.mohammadpour.app.ui.util.GetResponseWithAsync;
import ir.mohammadpour.app.ui.util.LocationService;
import ir.mohammadpour.app.ui.util.PermissionUtils;
import ir.mohammadpour.app.ui.util.SharedClass;
import ir.mohammadpour.app.ui.util.ShowMsg;
import ir.mohammadpour.app.ui.util.SlideView;
import ir.mohammadpour.app.ui.util.SwitchButton;
import ir.mohammadpour.app.ui.util.getReverseGeoCoding;
import ir.mohammadpour.app.ui.widget.CheckUpdate;
import ir.mohammadpour.app.ui.widget.GetBar;
import ir.mohammadpour.app.ui.widget.GetMaghsadDovvomPrice;
import ir.mohammadpour.app.ui.widget.GetTavaghofPrice;
import ir.mohammadpour.app.ui.widget.TouchableWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.mohammadpour.app.ui.widget.UpdateGooglePlay;
import me.shaohui.bottomdialog.BottomDialog;
import retrofit2.Call;
import retrofit2.Callback;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by skyrreasure on 12/5/16.
 */
public class SearchPlaceOnMapActivity  extends AppCompatActivity implements
        GoogleMap.OnMarkerClickListener, TouchableWrapper.UpdateMapAfterUserInterection,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMarkerDragListener,
        SeekBar.OnSeekBarChangeListener,
        OnMapReadyCallback,
        GoogleMap.OnInfoWindowLongClickListener,
        GoogleMap.OnInfoWindowCloseListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnCameraChangeListener,
        RouteAdapter.OnRouteClickInAdapter,
        ActivityCompat.OnRequestPermissionsResultCallback  ,GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    //This is for restriciting search only to india, for other countries you can specify lat lng by changing the below values.
    // private static final LatLngBounds BOUNDS_GREATER_INDIA = new LatLngBounds(
    //       new LatLng(8.062148, 68.212642), new LatLng(37.372499, 96.513423));


    String mabda_name = "", maghsad_name = "",city_id="";

    int markers_height_and_wight=100;
    EditText cityname;
    ImageView chat,btn_search;
    LinearLayout linearLayout, linearLayoutaddress,ll_map_item;
    protected GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteAdapter mAdapter;
    private AutoCompleteTextView mAutocompleteView;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    ImageView pin,star;
    private String mLatitude, mLongitude;
    private GoogleMap mMap;
    Boolean mMapIsTouched = false;
    Toolbar toolbar;
    Context c;
    ImageView minimize;
    Activity ac;
    DrawerLayout drawer_layout;
    Marker marker_maghsad, marker_mabda, marker_maghsad_dovvom;
    String tavaghof="-1";
    boolean raft_va_bargasht=false;
    int raft_va_abrgasht_price=0;
    boolean bar=false;
    private boolean mPermissionDenied = false;
    private Toolbar mToolbar;
    TextView appname;
    //ActionBar actionBar;
    List<Marker> taxiMarkers = new ArrayList<Marker>();
    private FloatingActionButton fabAdd;
    TextView title, address, taxicount;
    Boolean StateMabda = false; //is in mabda state or not
    Boolean StateMaghsad = false; //is in maghsad state or not
    SharedPreferences prefs;
    TextView mabda, maghsad;
    Button sendRequest;
    DrawerLayout mDrawerLayout;
    LinearLayout lineardriver;
    CircleImageView user_profile_photo;
    TextView txtName,txtCarName;
    Button btn_pay, btn_call;

    LinearLayout payment_transfer_layout,call_drver_layout;
    SwitchButton switch_payment_type;
    String tozihat_mabda="",tozihat_maghsad="";
    Handler h = new Handler();
    Runnable r;

    double price = 0;
    int primary_price=0;
    int bar_price=0;
    int tavagof_price=0;
    int maghsad_dovvom_price=0;
    String Server_MainUrl = "http://89.39.208.240",Server_Name="ارومیه";

    ProgressDialog simpleLoading;
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {

        @Override
        public void onResult(PlaceBuffer places) {
            Log.v("placeerror",places.toString());
            if (!places.getStatus().isSuccess()) {
                places.release();
                return;
            }
            final Place place = places.get(0);
            hideKeyboard();
            mLatitude = String.valueOf(place.getLatLng().latitude);
            mLongitude = String.valueOf(place.getLatLng().longitude);
            LatLng newLatLngTemp = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);


            // LatLng centerLatLng=new LatLng(mMap.getCameraPosition().target.latitude,mMap.getCameraPosition().target.longitude);
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLatLngTemp, 15f));


            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(newLatLngTemp).zoom(15f).build();
            mMap.setTrafficEnabled(false);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));


            hideKeyboard();


        }
    };

    private AdapterView.OnItemClickListener mAutocompleteClickListener2
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            final PlaceAutocompleteAdapter.PlaceAutocomplete item = mAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);


            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);


            mAutocompleteView.setText("");
        }
    };

    private void setupDrawerContent(final NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        //menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "Regular.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypeFaceSpan("", font, this), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);

    }

    String phone = "";


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1)
        {
            if (resultCode == RESULT_OK) //&& data.getExtras().getString("res").equals("1")
            {
                String driver_phone = prefs.getString("driver_phone", null);
            }


        }
        else if (requestCode == 5) {
            if (resultCode == RESULT_OK) //&& data.getExtras().getString("res").equals("1")
            {
                //String driver_phone = prefs.getString("driver_phone", null);
                //if (driver_phone != null)
                // ShowDriverWithDetails();
            }


        }
        else if(requestCode==10)
        {
            if (resultCode == RESULT_OK) //&& data.getExtras().getString("res").equals("1")
            {
                String [] latlng=data.getExtras().getString("res").split("~");
                if (!StateMabda) {
                    tozihat_mabda=data.getExtras().getString("desc");
                    StateMabda = true;
                    marker_mabda = mMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("mabda1",markers_height_and_wight,markers_height_and_wight)))
                            .position(new LatLng(Double.parseDouble(latlng[0]), Double.parseDouble(latlng[1]))));
                    pin.setImageResource(R.drawable.magsad2);

                    LatLng latLng = new LatLng(Double.parseDouble(latlng[0]) + 0.005,  Double.parseDouble(latlng[1]));
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(latLng).zoom(15f).build();
                    // .target(latLng).zoom(14f).build();

                    mMap.animateCamera(CameraUpdateFactory
                            .newCameraPosition(cameraPosition));

                    // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f));

                    //actionBar.setTitle("کجا می روید ؟");
                    appname.setText("کجا می روید ؟");

                    pin.setVisibility(View.VISIBLE);
                    //mAutocompleteView.setVisibility(View.VISIBLE);
                    //autocompleteLayout.setVisibility(View.VISIBLE);
                } else  {//else if (!StateMaghsad)

                    tozihat_maghsad=data.getExtras().getString("desc");
                    StateMaghsad = true;
                    marker_maghsad = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("magsad1",markers_height_and_wight,markers_height_and_wight)))
                            .position(new LatLng(Double.parseDouble(latlng[0]),Double.parseDouble(latlng[1]))));

                    linearLayout.setVisibility(View.VISIBLE);
                    linearLayoutaddress.setVisibility(View.GONE);

                    boolean first_help= prefs.getBoolean("first_help",false);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("first_help", true);

                    editor.apply();
                    editor.commit();
                    if(!first_help)
                    {
                        new MaterialTapTargetPrompt.Builder(SearchPlaceOnMapActivity.this)
                                .setTarget(tv_more_gozine)
                                .setPrimaryText("راهنما")
                                .setSecondaryText("برای ثبت اطلاعات بیشتر در مورد سفر از این قسمت وارد شوید")
                                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener()
                                {
                                    @Override
                                    public void onPromptStateChanged(MaterialTapTargetPrompt prompt, int state)
                                    {
                                        if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED)
                                        {
                                            // User has pressed the prompt target
                                        }
                                    }
                                })
                                .show();
                    }

                    // animateTextView(0,5000, title);
                    /* double x=CalcDistance.calculateDistance(mMap.getCameraPosition().target.latitude
                            , mMap.getCameraPosition().target.longitude,marker_mabda.getPosition().latitude
                            ,marker_mabda.getPosition().longitude);*/
                    // Log.v("test",mMap.getCameraPosition().target.latitude
                    //        +"~"+ mMap.getCameraPosition().target.longitude+"~"+marker_mabda.getPosition().latitude
                    //        +"~"+marker_mabda.getPosition().longitude);
                    double x = CalcDistance.distance(Double.parseDouble(latlng[0])
                            , Double.parseDouble(latlng[1]), marker_mabda.getPosition().latitude
                            , marker_mabda.getPosition().longitude);
                    //ShowCustomToast(x+"");


                 //   new GetPrice().execute(Double.parseDouble(latlng[0]) + ""
                     //       , Double.parseDouble(latlng[1]) + "", marker_mabda.getPosition().latitude + ""
                    //        , marker_mabda.getPosition().longitude + "",phone);

                    GetRoutes();

                    LatLng latLng = new LatLng(marker_maghsad.getPosition().latitude, marker_maghsad.getPosition().longitude + 0.002);
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(latLng).zoom(15f).build();
                    mMap.animateCamera(CameraUpdateFactory
                            .newCameraPosition(cameraPosition));


                    //////////// برای  نمایش مارکر مبدا و مقصد در یک صفحه با هم


                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    // for (Marker marker : markers) {
                    // builder.include(marker.getPosition());
                    //}
                    builder.include(marker_mabda.getPosition());
                    builder.include(marker_maghsad.getPosition());
                    LatLngBounds bounds = builder.build();
                    //int padding = 40; // offset from edges of the map in pixels

                    int width = getResources().getDisplayMetrics().widthPixels;
                    int height = getResources().getDisplayMetrics().heightPixels - 300;
                    int padding = (int) (width * 0.2); // offset from edges of the map 12% of screen

                    //Toast.makeText(v.getContext(),width+"  "+height,Toast.LENGTH_SHORT).show();
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
                    //CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                    mMap.animateCamera(cu);


                    ////////////


                    //mMap.getUiSettings().setAllGesturesEnabled(false);


                    //هاید کردن وسطی
                    pin.setVisibility(View.GONE);
                    //هاید کردن جستجو
                    //mAutocompleteView.setVisibility(View.GONE);

                   // autocompleteLayout.setVisibility(View.GONE);
                }
                //String driver_phone = prefs.getString("driver_phone", null);
                //if (driver_phone != null)
                // ShowDriverWithDetails();
            }
        }


        else if(requestCode==11)
        {
            if (resultCode == RESULT_OK) //&& data.getExtras().getString("res").equals("1")
            {
                String [] latlng=data.getExtras().getString("res").split("~");
                {//else if (!StateMaghsad)

                    tozihat_maghsad=data.getExtras().getString("desc");
                    int Takhfif_value=data.getExtras().getInt("takhfif_value");
                    Boolean Takhfif_type=data.getExtras().getBoolean("takhfif_type");
                    StateMaghsad = true;
                    marker_maghsad = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("magsad1",markers_height_and_wight,markers_height_and_wight)))
                            .position(new LatLng(Double.parseDouble(latlng[0]),Double.parseDouble(latlng[1]))));

                    linearLayout.setVisibility(View.VISIBLE);
                    linearLayoutaddress.setVisibility(View.GONE);

                    Mahgsad_Takhfif_unique_id=data.getExtras().getString("desc2");

                    boolean first_help= prefs.getBoolean("first_help",false);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("first_help", true);

                    editor.apply();
                    editor.commit();
                    if(!first_help)
                    {
                        new MaterialTapTargetPrompt.Builder(SearchPlaceOnMapActivity.this)
                                .setTarget(tv_more_gozine)
                                .setPrimaryText("راهنما")
                                .setSecondaryText("برای ثبت اطلاعات بیشتر در مورد سفر از این قسمت وارد شوید")
                                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener()
                                {
                                    @Override
                                    public void onPromptStateChanged(MaterialTapTargetPrompt prompt, int state)
                                    {
                                        if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED)
                                        {
                                            // User has pressed the prompt target
                                        }
                                    }
                                })
                                .show();
                    }
                    // animateTextView(0,5000, title);
                    /* double x=CalcDistance.calculateDistance(mMap.getCameraPosition().target.latitude
                            , mMap.getCameraPosition().target.longitude,marker_mabda.getPosition().latitude
                            ,marker_mabda.getPosition().longitude);*/
                    // Log.v("test",mMap.getCameraPosition().target.latitude
                    //        +"~"+ mMap.getCameraPosition().target.longitude+"~"+marker_mabda.getPosition().latitude
                    //        +"~"+marker_mabda.getPosition().longitude);
                    double x = CalcDistance.distance(Double.parseDouble(latlng[0])
                            , Double.parseDouble(latlng[1]), marker_mabda.getPosition().latitude
                            , marker_mabda.getPosition().longitude);
                    //ShowCustomToast(x+"");


                  //  new GetPrice().execute(Double.parseDouble(latlng[0]) + ""
                    //        , Double.parseDouble(latlng[1]) + "", marker_mabda.getPosition().latitude + ""
                    //        , marker_mabda.getPosition().longitude + "",Takhfif_value+"",Takhfif_type.toString().toLowerCase(),phone);

                    GetRoutes();

                    LatLng latLng = new LatLng(marker_maghsad.getPosition().latitude, marker_maghsad.getPosition().longitude + 0.002);
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(latLng).zoom(15f).build();
                    mMap.animateCamera(CameraUpdateFactory
                            .newCameraPosition(cameraPosition));


                    //////////// برای  نمایش مارکر مبدا و مقصد در یک صفحه با هم


                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    // for (Marker marker : markers) {
                    // builder.include(marker.getPosition());
                    //}
                    builder.include(marker_mabda.getPosition());
                    builder.include(marker_maghsad.getPosition());
                    LatLngBounds bounds = builder.build();
                    //int padding = 40; // offset from edges of the map in pixels

                    int width = getResources().getDisplayMetrics().widthPixels;
                    int height = getResources().getDisplayMetrics().heightPixels - 300;
                    int padding = (int) (width * 0.2); // offset from edges of the map 12% of screen

                    //Toast.makeText(v.getContext(),width+"  "+height,Toast.LENGTH_SHORT).show();
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
                    //CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                    mMap.animateCamera(cu);


                    ////////////


                    //mMap.getUiSettings().setAllGesturesEnabled(false);


                    //هاید کردن وسطی
                    pin.setVisibility(View.GONE);
                    //هاید کردن جستجو
                    //mAutocompleteView.setVisibility(View.GONE);
                    //autocompleteLayout.setVisibility(View.GONE);
                }
                //String driver_phone = prefs.getString("driver_phone", null);
                //if (driver_phone != null)
                // ShowDriverWithDetails();
            }
        }
        else if(requestCode==20)
        {
            if (resultCode == RESULT_OK) //&& data.getExtras().getString("res").equals("1")
            {

                String Lat= data.getExtras().getString("Lat");
                String Lng= data.getExtras().getString("Lng");

                if(ChangeState.FirstDest==change_state)
                {
                   if(marker_maghsad!=null)
                       marker_maghsad.remove();

                    marker_maghsad = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("magsad1",markers_height_and_wight,markers_height_and_wight)))
                            .position(new LatLng(Double.parseDouble(Lat),Double.parseDouble(Lng))));

                    btn0.setBackgroundResource(R.drawable.transparent_background_with_raduis_dark);
                }
                else {
                    if (marker_maghsad_dovvom != null)
                        marker_maghsad_dovvom.remove();

                    marker_maghsad_dovvom = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("magsad1", markers_height_and_wight, markers_height_and_wight)))
                            .position(new LatLng(Double.parseDouble(Lat), Double.parseDouble(Lng))));

                    btn1.setBackgroundResource(R.drawable.transparent_background_with_raduis_dark);
                }

            }
        }
    }
    public Bitmap resizeMapIcons(String iconName,int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    boolean isShow=false;
    @Override
    public void onRouteClickInAdapter(int index) {
        DrawPolyGone(routes,index);
        //snackbar.dismiss();
        bottomDialog.dismiss();
        isShow=false;
    }

    private class SendTokenToServer extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

            try {
                Log.v("service url:", Server_MainUrl + "/home/SetToken?phone=" + params[0] + "&password=" + params[1] + "&token=" + params[2]);
                URL url = new URL(Server_MainUrl + "/home/SetToken?phone=" + params[0] + "&password=" + params[1] + "&token=" + params[2]);
                // Read all the text returned by the server
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

                String builder = "";
                for (String line = null; (line = in.readLine()) != null; ) {
                    builder += (line + "\n");
                }
                in.close();

                // Toast.makeText(mContext,builder,Toast.LENGTH_LONG).show();

                return builder;
            } catch (IOException e) {


            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {


        }

    }

    SupportMapFragment mapFragment;
    ProgressBar loading, loading2;
    LinearLayout autocompleteLayout;
    RequestQueue mRequestQueue;
    TextView tv_takhfif,tv_more_gozine;
    TextView price2;
    Activity activity;
    CarouselPicker carouselPicker;
    Boolean show_autocomplete=false;
    TextView my_money;
    int transfer_type=0;
    ImageView increase_money;
    android.location.LocationManager mLocationManager;
    private static final int REQUEST_WRITE_STORAGE = 112;

    Location myLocation=null;
    private final android.location.LocationListener mLocationListener = new android.location.LocationListener() {
        @Override
        public void onLocationChanged(final android.location.Location location) {
            //your code here
            myLocation=location;
        }
        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    SharedClass sharedClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);


        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("com.durga.action.close");
        mIntentFilter.addAction("notification");
        mLocalBroadcastManager.registerReceiver(mBroadcastReceiver, mIntentFilter);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);


        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mRequestQueue = Volley.newRequestQueue(this); // 'this' is Context
        String name = prefs.getString("customer_name", null);
        simpleLoading = new ProgressDialog(this);
        simpleLoading.setMessage("در حال ارسال اطلاعات");
        simpleLoading.setCancelable(false);
        ac = this;
        String _token = prefs.getString("registration_id", null);
        String _phone = prefs.getString("customer_phone", null);
        String _pass = prefs.getString("customer_password", null);
        Server_MainUrl = prefs.getString("Server_MainUrl", "http://89.39.208.240");
        Server_Name = prefs.getString("Server_Name", "ارومیه");
        city_id = prefs.getString("Server_CityId", "2");


        if (_phone != null) {
            //String x= FirebaseInstanceId.getInstance().getToken();
            //Log.v("token",x);
            new SendTokenToServer().execute(_phone, _pass, _token);
        }
        user_profile_photo = (CircleImageView) findViewById(R.id.user_profile_photo);
        //txtPelak = (TextView) findViewById(R.id.txtPelak);
        txtName = (TextView) findViewById(R.id.txtName);
        txtCarName = (TextView) findViewById(R.id.txtCarName);
        lineardriver = (LinearLayout) findViewById(R.id.lineardriver);
        phone = prefs.getString("customer_phone", null);


        String languageToLoad = "fa_";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        //setContentView(R.layout.main_activity);
        setContentView(R.layout.main_activity);
        sharedClass=new SharedClass(this);



        try {
            com.activeandroid.Configuration.Builder configurationBuilder = new com.activeandroid.Configuration.Builder(this);
            configurationBuilder.addModelClass(SelectedAddressModel.class);
            configurationBuilder.addModelClass(ir.mohammadpour.app.model.Message.class);
            ActiveAndroid.initialize(configurationBuilder.create());

            List<ir.mohammadpour.app.model.Message> mItems = new Select().from(ir.mohammadpour.app.model.Message.class).execute();
        }
        catch (RuntimeException ex)
        {
            ActiveAndroid.dispose();

            String aaName = ReflectionUtils.getMetaData(getApplicationContext(), "AA_DB_NAME");

            if (aaName == null) {
                aaName = "Application.db";
            }

            deleteDatabase(aaName);
            com.activeandroid.Configuration.Builder configurationBuilder = new com.activeandroid.Configuration.Builder(this);
            configurationBuilder.addModelClass(ir.mohammadpour.app.model.Message.class);
            configurationBuilder.addModelClass(SelectedAddressModel.class);
            ActiveAndroid.initialize(configurationBuilder.create());
        }
        mLocationManager = (android.location.LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.


            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else {
            mLocationManager.requestLocationUpdates(android.location.LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
        }
        ll_map_item = (LinearLayout) findViewById(R.id.ll_map_item2);
        minimize = (ImageView) findViewById(R.id.minimize);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
            //  drawer_layout.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            drawer_layout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    v.removeOnLayoutChangeListener(this);


                    //OPEN WITH BOTTOM CENTER
                    int cx = (drawer_layout.getLeft() + drawer_layout.getRight()) / 2;
                    int cy = (drawer_layout.getTop() + drawer_layout.getBottom());
                    // get the hypothenuse so the radius is from one corner to the other
                    int radius = (int) Math.hypot(right, bottom);
                    Animator reveal = ViewAnimationUtils.createCircularReveal(v, cx, cy, 0, radius);
                    reveal.setInterpolator(new AccelerateDecelerateInterpolator());
                    reveal.setDuration(800);
                    reveal.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            // rootview.setBackgroundResource(R.color.white);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                    reveal.start();


                }
            });
        }
        autocompleteLayout = (LinearLayout) findViewById(R.id.autocompleteLayout);
        carouselPicker = (CarouselPicker) findViewById(R.id.carousel);
        switch_payment_type = (SwitchButton) findViewById(R.id.switch_payment_type);

        switch_payment_type.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    String p_money = prefs.getString("customer_money", "0");
                    if (Long.parseLong(p_money) < price) {
                        switch_payment_type.setChecked(false);
                        Toast.makeText(c, "موجودی شما برای پرداخت اعتباری این سفر کافی نمی باشد لطفا اعتبار خود را افزایش دهید", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        minimize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ll_map_item.getVisibility() == View.VISIBLE)
                    slideToBottom(ll_map_item, View.GONE, 0, ll_map_item.getHeight());
                else
                    slideToTop(ll_map_item, View.VISIBLE, ll_map_item.getHeight(), 0);
            }
        });

        //Case 3 : To populate the picker with both images and text
        List<CarouselPicker.PickerItem> mixItems = new ArrayList<>();

        mixItems.add(new CarouselPicker.DrawableItem(R.drawable.lady, "بانوان"));
        mixItems.add(new CarouselPicker.DrawableItem(R.drawable.driver, "کلاسیک"));
        mixItems.add(new CarouselPicker.DrawableItem(R.drawable.vip, "تاکسی VIP"));

        CarouselPicker.CarouselViewAdapter mixAdapter = new CarouselPicker.CarouselViewAdapter(this, mixItems, 0);
        carouselPicker.setAdapter(mixAdapter);
        carouselPicker.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //position of the selected item
                transfer_type = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        carouselPicker.setCurrentItem(1);
        chat = (ImageView) findViewById(R.id.chat);
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ChatActivity.class);
                startActivity(i);
            }
        });


        my_money = (TextView) findViewById(R.id.my_money);
        increase_money = (ImageView) findViewById(R.id.increase_money);

        star = (ImageView) findViewById(R.id.star);
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), SelectedLocationActivity.class);
                if (!StateMabda)
                    i.putExtra("state", "mabda");
                else
                    i.putExtra("state", "maghsad");
                startActivityForResult(i, 10);
            }
        });

        activity = this;
        c = this;

        final GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        final int status = api.isGooglePlayServicesAvailable(ac);
        if (status == ConnectionResult.SUCCESS) {
            // continue initializing whatever
        } else if (api.isUserResolvableError(status)) {
            //api.getErrorDialog(activity, status, REQUEST_UPDATE_GOOGLE_PLAY).show();
            //Toast.makeText(this,"لطفا آپدیت",Toast.LENGTH_LONG).show();
            UpdateGooglePlay update = new UpdateGooglePlay();
            update.Check(ac, c);
        } else {
            // tell the user they can't use your app
            UpdateGooglePlay update = new UpdateGooglePlay();
            update.Check(ac, c);
        }


        boolean hasPermission = (ContextCompat.checkSelfPermission(activity,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        }
        final int ARROW_MESSAGES2 = 1;
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // Discard other messages
                CheckUpdate checkUpdate = new CheckUpdate();
                checkUpdate.Check(activity, c);

            }
        };
        handler.sendEmptyMessage(ARROW_MESSAGES2);

        loading = (ProgressBar) findViewById(R.id.loading_progress);
        loading2 = (ProgressBar) findViewById(R.id.loading_progress2);

        // final Typeface face3 = Typeface.createFromAsset(getAssets(),
        //         "Regular.ttf");
        //my_money.setTypeface(face3);
        tv_takhfif = (TextView) findViewById(R.id.tv_takhfif);
        tv_more_gozine = (TextView) findViewById(R.id.tv_more_gozine);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayShowHomeEnabled(false); // show or hide the default home button
        // ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false);

        //ab.show();

        appname = (TextView) toolbar.findViewById(R.id.toolbar_title);
        final ImageView btn_menu = (ImageView) toolbar.findViewById(R.id.btn_menu);
        final ImageView btn_back = (ImageView) toolbar.findViewById(R.id.btn_back);
        final ImageView btn_search = (ImageView) toolbar.findViewById(R.id.btn_search);
        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.RIGHT);
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchPlaceOnMapActivity.super.onBackPressed();
            }
        });
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (autocompleteLayout.getVisibility() == View.VISIBLE) {
                    //Animation fadeInAnimation = AnimationUtils.loadAnimation(c, R.anim.activity_open_translate);

                    show_autocomplete = false;
                    //autocompleteLayout.setVisibility(View.GONE);
                    //autocompleteLayout.startAnimation(fadeInAnimation);
                    slideToTop(autocompleteLayout, View.GONE, 0, -2 * autocompleteLayout.getHeight());
                } else {
                    // Animation fadeInAnimation = AnimationUtils.loadAnimation(c, R.anim.activity_open_scale);

                    show_autocomplete = true;
                    slideToBottom(autocompleteLayout, View.VISIBLE, -2 * autocompleteLayout.getHeight(), 0);
                    // autocompleteLayout.setVisibility(View.VISIBLE);
                    //autocompleteLayout.startAnimation(fadeInAnimation);
                }
            }
        });
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        if (navigationView != null) {
            setupDrawerContent(navigationView);

            View headerLayout = navigationView.getHeaderView(0);
            TextView passanger_name = (TextView) headerLayout.findViewById(R.id.text_username);
            TextView passanger_money = (TextView) headerLayout.findViewById(R.id.text_money);
            TextView passanger_virtual_money = (TextView) headerLayout.findViewById(R.id.text_virtual_money);
            String customer_virtual_money = prefs.getString("customer_virtual_money", "0");
            String p_name = prefs.getString("customer_name", null);
            String p_money = prefs.getString("customer_money", "0");
            passanger_name.setText(p_name);
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            //symbols.setGroupingSeparator(',');
            symbols.setDecimalSeparator(',');
            symbols.setGroupingSeparator(',');
            DecimalFormat df = new DecimalFormat();
            df.setDecimalFormatSymbols(symbols);
            df.setGroupingSize(3);
            df.setGroupingUsed(true);
            //passanger_money.setTypeface(face3);
            passanger_money.setVisibility(View.GONE);
            my_money.setVisibility(View.GONE);
            passanger_money.setText("موجودی " + df.format(Integer.parseInt(p_money)) + " ریال");
            my_money.setText(df.format(Long.parseLong(p_money)));
            passanger_virtual_money.setText("موجودی طرح اسگاد " + df.format(Long.parseLong(customer_virtual_money)) + " ریال");

            passanger_virtual_money.setText("سیستم پیشنهاد مسیر");
            //TextView tvHeader =(TextView) headerLayout.findViewById(R.id.nav_header_name);
            //tvHeader.setTypeface(face2);
            Menu m = navigationView.getMenu();
            for (int i = 0; i < m.size(); i++) {
                MenuItem mi = m.getItem(i);

                //for aapplying a font to subMenu ...
                SubMenu subMenu = mi.getSubMenu();
                if (subMenu != null && subMenu.size() > 0) {
                    for (int j = 0; j < subMenu.size(); j++) {
                        MenuItem subMenuItem = subMenu.getItem(j);
                        //applyFontToMenuItem(subMenuItem);
                    }
                }

                //the method we have create in activity
                //applyFontToMenuItem(mi);
            }
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                mDrawerLayout.closeDrawers();
                if (item.getItemId() == R.id.exit) {

                    prefs.edit().remove("customer_name").commit();
                    prefs.edit().remove("customer_phone").commit();
                    prefs.edit().remove("customer_mail").commit();
                    prefs.edit().remove("customer_password").commit();
                    prefs.edit().remove("active").commit();
                    prefs.edit().remove("payment_type").commit();
                    prefs.edit().remove("transfer_price").commit();
                    prefs.edit().remove("transfer_id").commit();
                    prefs.edit().remove("transfer_state").commit();
                    prefs.edit().remove("driver_avatar_path").commit();
                    prefs.edit().remove("driver_name").commit();
                    prefs.edit().remove("driver_car_name").commit();
                    prefs.edit().remove("driver_car_model").commit();
                    prefs.edit().remove("driver_pelak_number").commit();
                    prefs.edit().remove("driver_phone").commit();

                    prefs.edit().remove("Server_MainUrl").commit();
                    prefs.edit().remove("Server_Name").commit();
                    prefs.edit().remove("Server_Lat").commit();
                    prefs.edit().remove("Server_Lng").commit();
                    prefs.edit().remove("Server_ContactUs").commit();
                    prefs.edit().remove("Server_CityId").commit();

                    prefs.edit().remove("customer_lat");
                    prefs.edit().remove("customer_lng");
                    prefs.edit().remove("dest_lat");
                    prefs.edit().remove("dest_lng");
                    prefs.edit().remove("second_dest_lat");
                    prefs.edit().remove("second_dest_lng");
                    prefs.edit().remove("wait_time_code");
                    prefs.edit().remove("Bar");
                    prefs.edit().remove("RaftBargasht");
                    Intent i = new Intent(c, LoginActivity.class);
                    startActivity(i);
                    finish();

                } else if (item.getItemId() == R.id.nav_transactions) {
                    startActivity(new Intent(activity, TransactionsActivity.class));
                } else if (item.getItemId() == R.id.nav_messages) {
                    startActivity(new Intent(activity, MessagesActivity.class));
                }
                if (item.getItemId() == R.id.nav_address) {

                    Intent i = new Intent(c, SelectedLocationActivity.class);
                    if (!StateMabda)
                        i.putExtra("state", "mabda");
                    else
                        i.putExtra("state", "maghsad");
                    startActivityForResult(i, 10);


                } else if (item.getItemId() == R.id.nav_more_services) {
                    Intent i = new Intent(c, ServicesActivity.class);
                    startActivity(i);
                } else if (item.getItemId() == R.id.nav_takhfif_address) {
                    if (StateMabda) {
                        Intent i = new Intent(SearchPlaceOnMapActivity.this, TakhfifLocationActivity.class);
                        startActivityForResult(i, 11);
                    } else
                        Toast.makeText(c, "ابتدا مبدا خود را انتخاب کنید", Toast.LENGTH_SHORT).show();
                } else if (item.getItemId() == R.id.nav_getdiscount) {

                    Intent i = new Intent(SearchPlaceOnMapActivity.this, ShareAppActivity.class);
                    startActivity(i);



                    /*
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBody = "با ارسال اسگاد به دوستان و آشنایان خود و دریافت اولین سفر آنها از ما اعتبار هدیه دریافت کنید";
                    String body="با دریافت و نصب اپلیکیشن اسگاد و ثبت اولین سفر به من اعتبار هدیه تعلق میگیره. با اسگاد میتونی خیلی راحت از هر جا که هستی درخواست خودرو بدی" +"  http://scad.ir/app/scad.apk";
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "ارسال نرم افزار و دریافت اعتبار هدیه ");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent, shareBody));
                    */
                } else if (item.getItemId() == R.id.nav_history) {

                    Intent i = new Intent(ac, HistoryActivity.class);
                    ac.startActivity(i);
                } else if (item.getItemId() == R.id.sendapp) {
                    ApplicationInfo app = getApplicationContext().getApplicationInfo();
                    String filePath = app.sourceDir;

                    Intent intent = new Intent(Intent.ACTION_SEND);

                    intent.setType("*/*");

                    intent.setPackage("com.android.bluetooth");

                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filePath)));
                    startActivity(Intent.createChooser(intent, "ارسال به دوستان"));
                } else if (item.getItemId() == R.id.nav_profile) {

                    Intent i = new Intent(ac, ProfileActivity.class);
                    ac.startActivity(i);
                } else if (item.getItemId() == R.id.nav_friends) {


                    /*
                    String Server_ContactUs = prefs.getString("Server_ContactUs", null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(SearchPlaceOnMapActivity.this);
                    builder.setTitle("تماس با ما");
                    builder.setMessage(Html.fromHtml(Server_ContactUs));

                    String positiveText = "بستن";
                    builder.setPositiveButton(positiveText,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // positive button logic

                                }
                            });


                    AlertDialog dialog = builder.create();
                    // display dialog
                    dialog.show();
                    */
                    Intent i = new Intent(c, BackupActivity.class);
                    startActivity(i);
                } else if (item.getItemId() == R.id.rules) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://scad.ir/rules.php"));
                    startActivity(browserIntent);

                } else if (item.getItemId() == R.id.about) {
                    startActivity(new Intent(ac, AboutActivity.class));
                } else if (item.getItemId() == R.id.nav_home) {

                }
                return false;
            }
        });


        mabda = (TextView) findViewById(R.id.start);
        maghsad = (TextView) findViewById(R.id.destination);
        sendRequest = (Button) findViewById(R.id.btn_book);

        linearLayout = (LinearLayout) findViewById(R.id.linela);
        linearLayoutaddress = (LinearLayout) findViewById(R.id.line_address);
        address = (TextView) findViewById(R.id.tv_google_suggestions);
        taxicount = (TextView) findViewById(R.id.taxicount);
        title = (TextView) findViewById(R.id.tv_title);
        //actionBar=getSupportActionBar();
        //actionBar.setTitle("کجا هستید ؟");
        appname.setText("کجا هستید ؟");


        pin = (ImageView) findViewById(R.id.iv_pin);


        final RelativeLayout root2 = (RelativeLayout) findViewById(R.id.root2);

        int actionBarHeight = 0;
        // ViewGroup.LayoutParams params = mDemoSlider.getLayoutParams();
        // params.height = height*30/100;

        // mDemoSlider.setLayoutParams(params);
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }


        ViewTreeObserver vto = root2.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                root2.getViewTreeObserver().removeOnPreDrawListener(this);
                int finalHeight = root2.getMeasuredHeight();
                int finalWidth = root2.getMeasuredWidth();
                markers_height_and_wight = finalHeight / 8;

                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(finalHeight / 8, finalHeight / 8);
                lp.setMargins(0, (finalHeight / 2) - (finalHeight / 8), 0, 0);
                lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
                pin.setLayoutParams(lp);

                return true;
            }
        });


        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fabAdd = (FloatingActionButton) findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(this);
        //final Typeface type2 = Typeface.createFromAsset(this.getAssets(), "Regular.ttf");
        mAutocompleteView = (AutoCompleteTextView)
                findViewById(R.id.googleplacesearch);
        //title.setTypeface(face3);
        cityname = (EditText) findViewById(R.id.cityname);

        cityname.setText(Server_Name);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();
        try {
            mAdapter = new PlaceAutocompleteAdapter(this, R.layout.google_places_search_items,
                    mGoogleApiClient, null, null, cityname.getText().toString());
        } catch (IllegalStateException ex) {
        }

        cityname.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                try {
                    mAdapter = new PlaceAutocompleteAdapter(ac, R.layout.google_places_search_items,
                            mGoogleApiClient, null, null, cityname.getText().toString());
                    mAutocompleteView.setAdapter(mAdapter);
                } catch (IllegalStateException ex) {
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {

            }
        });

        //TODO:In order to Restrict search to India uncomment this and comment the above line
        /*
        mAdapter = new PlaceAutocompleteAdapter(this, R.layout.google_places_search_items,
                mGoogleApiClient, BOUNDS_GREATER_INDIA, null);
         */
        try {
            mAutocompleteView.setAdapter(mAdapter);
        } catch (IllegalStateException ex) {
        }
        mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener2);

        mAutocompleteView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                try {


                    final int DRAWABLE_LEFT = 0;
                    final int DRAWABLE_TOP = 1;
                    final int DRAWABLE_RIGHT = 2;
                    final int DRAWABLE_BOTTOM = 3;

                    if (event.getRawX() <= (mAutocompleteView.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width())) {

                        //Intent i = new Intent(SearchPlaceOnMapActivity.this, MainActivity.class);
                        // startActivity(i);

                        //finish();
                        return true;
                    }
                    return false;
                } catch (NullPointerException ex) {
                    return false;
                }
            }

        });

        pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PinClick();
            }
        });

        if(!sharedClass.isSeeMarkerAdd())
        {
            sharedClass.setSeeMarkerAdd();
            new MaterialTapTargetPrompt.Builder(SearchPlaceOnMapActivity.this)
                    .setTarget(pin)
                    .setPrimaryText("راهنما")
                    .setSecondaryText("ابتدا لازم است مبدا و سپس مقصد خود را ثبت نمایید ، برای این منظور لازم است با کشیدن نقشه ، آیکون مورد نظر را روی مبدا (یا مقصد ) خود تنظیم نمایید و سپس روی این قسمت بزنید ")
                    .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener()
                    {
                        @Override
                        public void onPromptStateChanged(MaterialTapTargetPrompt prompt, int state)
                        {
                            if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED)
                            {
                                // User has pressed the prompt target
                            }
                        }
                    })
                    .show();
        }
    }

    LocalBroadcastManager mLocalBroadcastManager;
    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("com.durga.action.close")){
                finish();
            }
        }
    };

    AlertDialog transferRequestDialog;
    ProgressBar send_progress;
    int takhfif_price=0;
    String Takhfif_unique_id="";
    String Mahgsad_Takhfif_unique_id="";


    Boolean PinClicked = false;
    Boolean MaghsadDovvomSelected = false;
    Boolean MaghsadDovvomMikhad = false;

    void PinClick() {
        PinClicked = true;
        if (loading.getVisibility() == View.GONE && loading2.getVisibility() == View.GONE) {
            if (!StateMabda) {
                StateMabda = true;
                marker_mabda = mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("mabda1",markers_height_and_wight,markers_height_and_wight)))
                        .position(new LatLng(mMap.getCameraPosition().target.latitude, mMap.getCameraPosition().target.longitude)));
                pin.setImageResource(R.drawable.magsad2);

                LatLng latLng = new LatLng(mMap.getCameraPosition().target.latitude + 0.005, mMap.getCameraPosition().target.longitude);
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng).zoom(15f).build();
                // .target(latLng).zoom(14f).build();

                mMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));

                // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f));

                //actionBar.setTitle("کجا می روید ؟");
                appname.setText("کجا می روید ؟");

                pin.setVisibility(View.VISIBLE);
                //mAutocompleteView.setVisibility(View.VISIBLE);
                //autocompleteLayout.setVisibility(View.VISIBLE);
            } else if (!StateMaghsad) {
                if(autocompleteLayout.getVisibility()==View.VISIBLE) {
                    Animation fadeInAnimation = AnimationUtils.loadAnimation(c, R.anim.slide_up_dialog);

                    autocompleteLayout.setVisibility(View.GONE);
                    autocompleteLayout.startAnimation(fadeInAnimation);
                }



                StateMaghsad = true;
                marker_maghsad = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("magsad1",markers_height_and_wight,markers_height_and_wight)))
                        .position(new LatLng(mMap.getCameraPosition().target.latitude, mMap.getCameraPosition().target.longitude)));


                GetRoutes();
                LatLng latLng = new LatLng(marker_maghsad.getPosition().latitude, marker_maghsad.getPosition().longitude + 0.002);
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng).zoom(15f).build();
                mMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));


                //////////// برای  نمایش مارکر مبدا و مقصد در یک صفحه با هم


                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                // for (Marker marker : markers) {
                // builder.include(marker.getPosition());
                //}
                builder.include(marker_mabda.getPosition());
                builder.include(marker_maghsad.getPosition());
                LatLngBounds bounds = builder.build();
                //int padding = 40; // offset from edges of the map in pixels

                int width = getResources().getDisplayMetrics().widthPixels;
                int height = getResources().getDisplayMetrics().heightPixels - 300;
                int padding = (int) (width * 0.2); // offset from edges of the map 12% of screen

                //Toast.makeText(v.getContext(),width+"  "+height,Toast.LENGTH_SHORT).show();
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
                //CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                mMap.animateCamera(cu);


                ////////////


                //mMap.getUiSettings().setAllGesturesEnabled(false);


                //هاید کردن وسطی
                pin.setVisibility(View.GONE);
                //هاید کردن جستجو
                //mAutocompleteView.setVisibility(View.GONE);
                autocompleteLayout.setVisibility(View.GONE);
            }
        }
        PinClicked = false;
    }
    boolean visible_address=true;
    TextView txtTransferPrice;
    AlertDialog change_alertDialog;
    TextView change_price;
    enum ChangeState
    {
        FirstDest,
        SecondDest,
        Other
    };
    ProgressBar myloading;
    ChangeState change_state=ChangeState.Other;
    LinearLayout btn0,btn1,btn2,btn3,btn4;


    ValueAnimator animator = new ValueAnimator();




    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }


    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

        }
    }

    Handler threadHandler = new Handler();
    Handler threadHandler2 = new Handler();

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
              if (!mMapIsTouched)

            try {
                if (!StateMabda) {
                    for (int i = 0; i < taxiMarkers.size(); i++) {
                        taxiMarkers.get(i).remove();
                    }
                    taxiMarkers = new ArrayList<>();
                }


                final int ARROW_MESSAGES = 0;
                final int ARROW_MESSAGES2 = 1;

                Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        // Discard other messages
                        removeMessages(ARROW_MESSAGES);
                        // UI update
                        getReverseGeoCoding myclass = new getReverseGeoCoding();
                        myclass.getAddress(mMap.getCameraPosition().target.latitude + "", mMap.getCameraPosition().target.longitude + "");
                        address.setText(myclass.getAddress1());

                    }
                };
                Handler handler2 = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        // Discard other messages
                        removeMessages(ARROW_MESSAGES);
                        // UI update
                        //new GetAroundTaxies().execute(mMap.getCameraPosition().target.latitude + "", mMap.getCameraPosition().target.longitude + "", 500 + "");
                        GetAroundTaxi(mMap.getCameraPosition().target.latitude + "", mMap.getCameraPosition().target.longitude + "");

                    }
                };

                //handler.sendEmptyMessage(ARROW_MESSAGES);
                handler2.sendEmptyMessage(ARROW_MESSAGES2);
            }
                catch (Exception e) {
                e.printStackTrace(); // getFromLocation() may sometimes fail
            }


    }

    void GetAroundTaxi(final String lat,final String lng) {
        try {
            if (lineardriver.getVisibility() == View.VISIBLE)
                return;
        }
        catch (NullPointerException ex){}
        mRequestQueue.cancelAll("taxi");
        if (!StateMabda) {

            loading.setVisibility(View.VISIBLE);
            address.setText("");
            taxicount.setText("انتخاب مبدا (نقشه را جابجا و روی آیکون بزنید)");
        } else {
            loading2.setVisibility(View.VISIBLE);
            taxicount.setText("انتخاب مقصد (نقشه را جابجا و روی آیکون بزنید)");
        }

        mabda_name = "";


        final String URL = Server_MainUrl+"/payanname/GetAllNearStations?lat=" + lat + "&lng=" + lng+"&radius=1000";
        Log.v("url",URL);
        JsonArrayRequest req = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    //VolleyLog.v("Response:%n %s", response.toString(4));


                    loading.setVisibility(View.GONE);
                    loading2.setVisibility(View.GONE);
                    if (response == null)
                    {

                        AlertDialog.Builder dialog = new AlertDialog.Builder(c);
                        dialog.setMessage("مشکل در برقراری ارتباط");
                        dialog.setPositiveButton("سعی مجدد", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                // TODO Auto-generated method stub
                                //new GetAroundTaxies().execute(mMap.getCameraPosition().target.latitude + "", mMap.getCameraPosition().target.longitude + "", 500 + "");
                                GetAroundTaxi(lat,lng);
                            }
                        });
                        dialog.setNegativeButton("انصراف", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                // TODO Auto-generated method stub
                                finish();
                            }
                        });
                        dialog.show();


                        return;
                    }
                    //Here you are done with the task

                        //Log.v("taxi,res:", result);
                        JSONArray jsonArray = response;//new JSONArray(response);


                        for (int i = 0; i < jsonArray.length(); i++) {
                            if (i == 0) {
                                if (!StateMabda)
                                    address.setText( jsonArray.getJSONObject(i).getString("StationName"));
                                else
                                    taxicount.setText(  jsonArray.getJSONObject(i).getString("StationName"));
                                if (!StateMabda) {
                                    mabda.setText( jsonArray.getJSONObject(i).getString("StationName"));
                                } else if (!StateMaghsad) {
                                    maghsad.setText( jsonArray.getJSONObject(i).getString("StationName"));
                                }
                            } else
                            if (!StateMabda) {

                                Marker temp=mMap.addMarker(new MarkerOptions()
                                                .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("station",200,200)))
                                                                    .position(new LatLng(jsonArray.getJSONObject(i).getDouble("Lat"), jsonArray.getJSONObject(i).getDouble("Lng"))));

                               //double lat1= Double.parseDouble(jsonArray.getJSONObject(i).getString("lat2"));
                               //double lng1= Double.parseDouble(jsonArray.getJSONObject(i).getString("lng2"));
                              // double lat2= Double.parseDouble(jsonArray.getJSONObject(i).getString("lat"));
                              // double lng2= Double.parseDouble(jsonArray.getJSONObject(i).getString("lng"));
                              // rotateMarker(lat1,lng1,lat2,lng2,temp);
                               taxiMarkers.add(temp);
                            }
                        }
                        if (jsonArray.length() > 1 && !StateMabda) {
                            taxicount.setText(jsonArray.length() - 1 + " ایستگاه شارژ یافت شد");
                        }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });

// add the request object to the queue to be executed
        App.getInstance().addToRequestQueue(req);
    }
    double maxAnim = 10000;
    List<RouteModel.CompleteDirection> routes;
    private void GetRoutes() {

        simpleLoading.show();
        Double lat = marker_mabda.getPosition().latitude ;//originLatLng.latitude+"";
        Double lng = marker_mabda.getPosition().longitude ;//originLatLng.longitude+"";
        Double lat2 = marker_maghsad.getPosition().latitude ;//destinationLatLng.latitude+"";
        Double lng2 = marker_maghsad.getPosition().longitude ;//destinationLatLng.longitude+"";

        ServiceGenerator.API_BASE_URL="http://89.39.208.240";
        BaseServer apiService = ServiceGenerator.createService(BaseServer.class);
        if (new ConnectionDetector(c).isConnectingToInternet()) {
            //Connected to the Internet
            Call<List<RouteModel.CompleteDirection>> call = apiService.getRoute(lat, lng, lat2,lng2,50);
            call.enqueue(new Callback<List<RouteModel.CompleteDirection>>() {
                @Override
                public void onResponse(Call<List<RouteModel.CompleteDirection>> call, retrofit2.Response<List<RouteModel.CompleteDirection>> response) {
                    routes = response.body();
                    if (routes != null) {
                        if(routes.size()>0)
                            //ShowDirections(routes);
                            ShowDirection2(routes);
                        else
                            new SweetAlertDialog(c).setTitleText("هشدار")
                                    .setContentText("متاسفانه مسیری یافت نشد")
                                    .setConfirmText("سعی مجدد")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.cancel();
                                            GetRoutes();
                                        }
                                    }).show();

                    }
                    else
                    {
                         /*new SweetAlertDialog(c).setTitleText("هشدار")
                                .setContentText("متاسفانه مسیری یافت نشد")
                                .setConfirmText("سعی مجدد")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.cancel();
                                        GetRoutes();
                                    }
                                }).show();*/
                         routes=new ArrayList<>();
                         routes.add(null);
                         routes.add(null);
                        routes.add(null);
                        routes.add(null);
                        routes.add(null);
                        routes.add(null);
                        routes.add(null);
                        routes.add(null);
                        ShowDirection2(routes);
                    }

                    simpleLoading.dismiss();
                }

                @Override
                public void onFailure(Call<List<RouteModel.CompleteDirection>> call, Throwable t) {
                    simpleLoading.dismiss();
                    new SweetAlertDialog(c).setTitleText("هشدار")
                            .setContentText("مشکل در ارتباط با سرور")
                            .setConfirmText("سعی مجدد")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.cancel();
                                    GetRoutes();
                                }
                            }).show();

                }
            });
        } else {
            //No internet connection
            simpleLoading.dismiss();
            new SweetAlertDialog(c).setTitleText("هشدار")
                    .setContentText("لطفا ارتباط اینترنتی خود را بررسی بفرمایید")
                    .setConfirmText("سعی مجدد")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.cancel();
                            GetRoutes();
                        }
                    }).show();


        }

    }



    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onInfoWindowClose(Marker marker) {

    }

    @Override
    public void onInfoWindowLongClick(Marker marker) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setTrafficEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnCameraChangeListener(this);

        enableMyLocation();
        final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10,
                getResources().getDisplayMetrics());

        final int ZOOM_CONTROL_ID = 0x1;
        View locationButton = ((View) findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
// position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        final int mymargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 260,
                getResources().getDisplayMetrics());
        rlp.setMargins(margin, margin, margin, mymargin);


        View zoomControls = mapFragment.getView().findViewById(0x1);

        if (zoomControls != null && zoomControls.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
            // ZoomControl is inside of RelativeLayout
            RelativeLayout.LayoutParams params_zoom = (RelativeLayout.LayoutParams) zoomControls.getLayoutParams();

            // Align it to - parent top|left
            params_zoom.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            params_zoom.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            // Update margins, set to 10dp

            final int marginbottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 270,
                    getResources().getDisplayMetrics());
            params_zoom.setMargins(margin, margin, margin, marginbottom);

        }


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.


            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        }

        else {

            onmap_load_and_have_permission();
        }
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


           // String Lat = prefs.getString("Server_Lat", null);
           // String Lng = prefs.getString("Server_Lng", null);


           // LatLng newLatLngTemp = new LatLng(Double.parseDouble(Lat), Double.parseDouble(Lng));
           // CameraPosition cameraPosition = new CameraPosition.Builder()
           ///         .target(newLatLngTemp).zoom(15f).build();
         //   mMap.animateCamera(CameraUpdateFactory
          //          .newCameraPosition(cameraPosition));
        }

    }
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);

            //mMap.displayZoomControls(true);
        }


        String transfer_state = prefs.getString("transfer_state", null);
        if (transfer_state != null) {
            if (transfer_state.equals("false")) {
                //String transfer_id = prefs.getString("transfer_id", null);
                //Intent i = new Intent(this, WaitingActivity.class);
                //i.putExtra("transfer_id", transfer_id);
                //startActivityForResult(i, 1);
            } else if (transfer_state.equals("true")) {

                try {
                    // stopService(new Intent(c, TransferService.class));
                } catch (Exception ex) {
                }
                //Intent i =new Intent(this,CurrentTransferActivity.class);
                //startActivity(i);

                //Toast.makeText(this,"ghofl shavad",Toast.LENGTH_SHORT).show();
                //mMap.getUiSettings().setAllGesturesEnabled(false);

                pin.setVisibility(View.GONE);
                //mAutocompleteView.setVisibility(View.GONE);
                autocompleteLayout.setVisibility(View.GONE);
                final int delay = 100; //milliseconds
                final Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    public void run() {
                        String transfer = prefs.getString("transfer_state", null);
                        if (transfer == null) {
                          //  mMap.getUiSettings().setAllGesturesEnabled(true);

                            lineardriver.setVisibility(View.GONE);
                            pin.setVisibility(View.VISIBLE);
                            //mAutocompleteView.setVisibility(View.VISIBLE);
                            //autocompleteLayout.setVisibility(View.VISIBLE);
                            //Log.v("transfer",transfer);
                        } else {
                            h.removeCallbacks(this);
                            h.removeCallbacksAndMessages(null);
                        }


                        h.postDelayed(this, delay);
                    }
                }, delay);

            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onUpdateMapAfterUserInterection() {

    }

    final int REQUEST_CODE_ASK_PERMISSIONS = 1;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        // Toast.makeText(this, "Call Permission Granted..Please dial again.", Toast.LENGTH_SHORT).show();
                        onmap_load_and_have_permission();
                        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
                    }
                    catch (NullPointerException ex)
                    {

                    }

                } else {
                    Toast.makeText(this, "Call permission not granted", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_add:
                /*
                mLatitude=mMap.getCameraPosition().target.latitude+"";
                mLongitude=mMap.getCameraPosition().target.longitude+"";

                Toast.makeText(SearchPlaceOnMapActivity.this, "Latitude:"+mLatitude +" Longitude:"+mLongitude,Toast.LENGTH_LONG).show();
*/
                Intent i = new Intent(v.getContext(), WaitingActivity.class);
                startActivityForResult(i, 1);
                break;
        }
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    public void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    Boolean doubleBackToExitPressedOnce = false;
    Boolean StateDriverShow_runningService = false;
    @Override
    public void onDestroy () {

        h.removeCallbacks(r);
        h.removeCallbacksAndMessages(null);
        super.onDestroy ();

    }

    Polyline polylineFinal;

    Marker station_loc;

    void DrawPolyGone(List<RouteModel.CompleteDirection> obj,int index)
    {
        if(polylineFinal!=null)
            polylineFinal.remove();

        ArrayList points = null;
        PolylineOptions lineOptions = null;
        MarkerOptions markerOptions = new MarkerOptions();
        List<List<HashMap<String, String>>> routes = null;
        List<List<HashMap<String, String>>> routes2 = null;
        try {
            if(station_loc!=null)
                station_loc.remove();
            station_loc=mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("station",200,200)))
                    .position(new LatLng(obj.get(index).station.Lat,obj.get(index).station.Lng)));

            DirectionsJSONParser parser = new DirectionsJSONParser();

            routes = parser.parse(obj.get(index).OriToStation);
            routes2 = parser.parse(obj.get(index).StationToDest);

            points = new ArrayList();
            lineOptions = new PolylineOptions();

            List<HashMap<String, String>> path = routes.get(0);
            path.addAll(routes2.get(0));


                for (int j = 0; j < path.size(); j++) {
                    HashMap point = path.get(j);

                    double lat = Double.parseDouble((String) point.get("lat"));
                    double lng = Double.parseDouble((String) point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(15);
                lineOptions.color(R.color.colorPrimary);
                lineOptions.geodesic(true);
                polylineFinal = mMap.addPolyline(lineOptions);



        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onBackPressed() {

        //mMap.getUiSettings().setAllGesturesEnabled(true);

        if (!StateMabda && !StateMaghsad) {
            if (doubleBackToExitPressedOnce) {
               /* Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);*/
                //finish();
                //android.os.Process.killProcess(android.os.Process.myPid());
                super.onBackPressed();
            } else {
                this.doubleBackToExitPressedOnce = true;
                //Toast.makeText(this, "جهت خروج یک بار دیگر بزنید", Toast.LENGTH_SHORT).show();
                ShowCustomToast("جهت خروج یک بار دیگر بزنید");
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            }

            pin.setVisibility(View.VISIBLE);
            //mAutocompleteView.setVisibility(View.VISIBLE);
            //autocompleteLayout.setVisibility(View.VISIBLE);
        } else if (!StateMaghsad && StateMabda) {
            marker_mabda.remove();
            StateMabda = false;
            //actionBar.setTitle("کجا هستید  ؟");
            price=0;
            maghsad_dovvom_price=0;
            bar_price=0;
            tavagof_price=0;
            tozihat_mabda="";
            tozihat_maghsad="";
            appname.setText("کجا هستید  ؟");
            pin.setImageResource(R.drawable.mabda2);
            LatLng latLng = new LatLng(mMap.getCameraPosition().target.latitude, mMap.getCameraPosition().target.longitude);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng).zoom(15f).build();
            mMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));

          //  linearLayout.setVisibility(View.GONE);
           // linearLayoutaddress.setVisibility(View.VISIBLE);


            pin.setVisibility(View.VISIBLE);
            taxicount.setText("انتخاب مبدا (نقشه را جابجا و روی آیکون بزنید)");
            address.setText("");
            //mAutocompleteView.setVisibility(View.VISIBLE);
            //autocompleteLayout.setVisibility(View.VISIBLE);
        } else {

            if(bottomDialog!=null && isShow)
            {
                bottomDialog.dismiss();
                isShow=false;
                if(polylineFinal!=null)
                    polylineFinal.remove();


                pin.setImageResource(R.drawable.magsad2);
                bar = false;
                raft_va_bargasht = false;
                tavaghof = "";
                marker_maghsad.remove();
                StateMaghsad = false;
                price = 0;
                tozihat_maghsad = "";
                // LatLng latLng = new LatLng(mMap.getCameraPosition().target.latitude + 0.005, mMap.getCameraPosition().target.longitude);
                LatLng latLng = new LatLng(mMap.getCameraPosition().target.latitude, mMap.getCameraPosition().target.longitude);
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng).zoom(15f).build();
                //.target(latLng).zoom(14f).build();
                mMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));


                // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f));
                //actionBar.setTitle("کجا می روید ؟");

                appname.setText("کجا می روید ؟");
                // linearLayout.setVisibility(View.GONE);
                // linearLayoutaddress.setVisibility(View.VISIBLE);

                pin.setVisibility(View.VISIBLE);
                //mAutocompleteView.setVisibility(View.VISIBLE);
                //autocompleteLayout.setVisibility(View.VISIBLE);
            }
            else if(bottomDialog!=null && !isShow)
            {
                if(polylineFinal!=null)
                    polylineFinal.remove();
                //ShowDirections(routes);

                ShowDirection2(routes);
            }
            else
            {
                pin.setImageResource(R.drawable.magsad2);
                bar = false;
                raft_va_bargasht = false;
                tavaghof = "";
                marker_maghsad.remove();
                StateMaghsad = false;
                price = 0;
                tozihat_maghsad = "";
                // LatLng latLng = new LatLng(mMap.getCameraPosition().target.latitude + 0.005, mMap.getCameraPosition().target.longitude);
                LatLng latLng = new LatLng(mMap.getCameraPosition().target.latitude, mMap.getCameraPosition().target.longitude);
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng).zoom(15f).build();
                //.target(latLng).zoom(14f).build();
                mMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));


                // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f));
                //actionBar.setTitle("کجا می روید ؟");

                appname.setText("کجا می روید ؟");
                // linearLayout.setVisibility(View.GONE);
                // linearLayoutaddress.setVisibility(View.VISIBLE);

                pin.setVisibility(View.VISIBLE);
                //mAutocompleteView.setVisibility(View.VISIBLE);
                //autocompleteLayout.setVisibility(View.VISIBLE);
            }
        }
    }
    float dX, dY;


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: // Contact has moved across screen
                try {
                    ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(pin, "scaleX", 0.7f);
                    ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(pin, "scaleY", 0.7f);
                    scaleDownX.setDuration(200);
                    scaleDownY.setDuration(200);

                    ObjectAnimator moveUpY = ObjectAnimator.ofFloat(pin, "translationY", 0);
                    moveUpY.setDuration(200);

                    AnimatorSet scaleDown = new AnimatorSet();
                    AnimatorSet moveUp = new AnimatorSet();

                    scaleDown.play(scaleDownX).with(scaleDownY);
                    moveUp.play(moveUpY);

                    scaleDown.start();
                    moveUp.start();


                    //Animation fadeInAnimation = AnimationUtils.loadAnimation(c, R.anim.slide_out_down);

                    // toolbar.setVisibility(View.GONE);
                    // toolbar.startAnimation(fadeInAnimation);

                    //  autocompleteLayout.setVisibility(View.GONE);
                    // autocompleteLayout.startAnimation(fadeInAnimation);
                    if(linearLayout.getVisibility()==View.GONE ) {
                        if(show_autocomplete)
                            slideToTop(autocompleteLayout, View.GONE, 0, -2*autocompleteLayout.getHeight());

                        if(visible_address)
                            slideToBottom(linearLayoutaddress, View.GONE, 0, linearLayoutaddress.getHeight());

                    }
                    ///SlideToAbove();

                }
                catch (NullPointerException ex) {

                }
                break;
            case MotionEvent.ACTION_UP:
                try {
                    ObjectAnimator scaleDownX2 = ObjectAnimator.ofFloat(pin, "scaleX", 1f);
                    ObjectAnimator scaleDownY2 = ObjectAnimator.ofFloat(pin, "scaleY", 1f);
                    scaleDownX2.setDuration(200);
                    scaleDownY2.setDuration(200);

                    ObjectAnimator moveUpY2 = ObjectAnimator.ofFloat(pin, "translationY", 0);
                    moveUpY2.setDuration(200);

                    AnimatorSet scaleDown2 = new AnimatorSet();
                    AnimatorSet moveUp2 = new AnimatorSet();

                    scaleDown2.play(scaleDownX2).with(scaleDownY2);
                    moveUp2.play(moveUpY2);

                    scaleDown2.start();
                    moveUp2.start();

                    // Animation fadeInAnimation2 = AnimationUtils.loadAnimation(c, R.anim.slide_up_dialog);
                    //autocompleteLayout.setVisibility(View.VISIBLE);
                    // autocompleteLayout.startAnimation(fadeInAnimation2);
                    //toolbar.setVisibility(View.VISIBLE);
                    //toolbar.startAnimation(fadeInAnimation2);
                    // SlideToDown();
                    if(linearLayout.getVisibility()==View.GONE )  {
                        if(show_autocomplete)
                            slideToBottom(autocompleteLayout, View.VISIBLE, -2*autocompleteLayout.getHeight(), 0);

                        if(visible_address)
                             slideToTop(linearLayoutaddress, View.VISIBLE, linearLayoutaddress.getHeight(), 0);
                    }
                }
                catch (NullPointerException ex){

                }

                break;
        }
        return super.dispatchTouchEvent(ev);
    }
    Snackbar snackbar;
    void ShowDirections(final List<RouteModel.CompleteDirection> obj)
    {
        LayoutInflater inflater = getLayoutInflater();
        // Create the Snackbar
        snackbar = Snackbar.make(linearLayout, "", Snackbar.LENGTH_INDEFINITE);
        // Get the Snackbar's layout view
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        // Hide the text
        TextView textView = (TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);


       final View snackView = inflater.inflate(R.layout.route_layout, null);
        ListView listView=(ListView) snackView.findViewById(R.id.lv);
        if(obj.size()>0) {

            if(taxiMarkers!=null)
            {
                for (Marker item:taxiMarkers) {
                    item.remove();
                }
            }

            RouteAdapter adapter = new RouteAdapter(ac, obj);
            listView.setAdapter(adapter);
            DrawPolyGone(obj,0);
        }
        snackView.setBackgroundColor(ContextCompat.getColor(ac, R.color.white));
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(ac, R.color.white));

        // Add the view to the Snackbar's layout
        layout.addView(snackView, 0);
        // Show the Snackbar
        snackbar.show();
    }
    BottomDialog bottomDialog;
    void ShowDirection2(final List<RouteModel.CompleteDirection> obj)
    {
        isShow=true;
        bottomDialog= BottomDialog.create(getSupportFragmentManager())
                .setViewListener(new BottomDialog.ViewListener() {
                    @Override
                    public void bindView(View v) {
                        // // You can do any of the necessary the operation with the view
                        ListView listView=(ListView) v.findViewById(R.id.lv);
                        //if(obj.size()>0)
                        {

                            //if(taxiMarkers!=null)
                          //  {
                            //    for (Marker item:taxiMarkers) {
                            //        item.remove();
                          //      }
                          //  }
                            //obj=new ArrayList<>();
                            RouteAdapter adapter = new RouteAdapter(ac, obj);
                            listView.setAdapter(adapter);
                           // DrawPolyGone(obj,0);
                        }
                    }
                })
                .setLayoutRes(R.layout.route_layout)
                .setDimAmount(0.1f)            // Dialog window dim amount(can change window background color）, range：0 to 1，default is : 0.2f
                .setCancelOutside(false)     // click the external area whether is closed, default is : true
                .setTag("BottomDialog");     // setting the DialogFragment tag

        bottomDialog.show();

    }
    public void slideToBottom(View view ,int Visibility,float from,float to){
        TranslateAnimation animate = new TranslateAnimation(0,0,from,to);
        animate.setDuration(200);
        animate.setFillAfter(true);

        view.startAnimation(animate);
        view.setVisibility(Visibility);
    }
    public void slideToTop(View view,int Visibility,float from,float to){
        TranslateAnimation animate = new TranslateAnimation(0,0,from,to);
        animate.setDuration(200);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(Visibility);
    }


    public void ShowCustomToast(String msg) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.toast_layout_root));

        ImageView image = (ImageView) layout.findViewById(R.id.image);
        //image.setImageResource(R.drawable.android);
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(msg);

       final Toast toast = new Toast(this);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 800);
    }





    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
