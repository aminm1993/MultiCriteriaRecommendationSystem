package ir.mohammadpour.app.ui.activity;

import android.animation.Animator;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.activeandroid.query.Select;
import com.activeandroid.util.ReflectionUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import ir.mohammadpour.app.Manifest;
import ir.mohammadpour.app.R;
import ir.mohammadpour.app.model.SelectedAddressModel;
import ir.mohammadpour.app.network.ServiceGenerator;
import ir.mohammadpour.app.ui.adapter.MessageAdapter;
import ir.mohammadpour.app.ui.util.Consts;
import ir.mohammadpour.app.ui.widget.CheckUpdate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collections;
import java.util.List;

public class SplashActivity extends AppCompatActivity {
    ////

    SharedPreferences prefs;
    Context c;
    Activity activity;
    RelativeLayout rootview;
    private static final int REQUEST_WRITE_STORAGE = 112;
    ////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);    // Removes title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);    // Removes notification bar
        setContentView(R.layout.activity_splash);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        c=this;
        activity=this;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            rootview = (RelativeLayout) findViewById(R.id.rootview);
            rootview.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            rootview.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    v.removeOnLayoutChangeListener(this);

              /*//BOTTOM RIGHT TO TOP LEFT ANIMATION
                int cx = (framelayout.getLeft() + framelayout.getRight());
                int cy = (framelayout.getTop() + framelayout.getBottom());
                // get the hypothenuse so the radius is from one corner to the other
                int radius = (int) Math.hypot(right, bottom);
                Animator reveal = ViewAnimationUtils.createCircularReveal(v, cx, cy, 0, radius);
                reveal.setInterpolator(new AccelerateDecelerateInterpolator());
                reveal.setDuration(600);
                reveal.start();*/

              /*  //LEFT TOP TO BOTTOM RIGHT ANIMATION
                int cx1 = 0;
                int cy1 = 0;
                // get the hypothenuse so the radius is from one corner to the other
                int radius1 = (int) Math.hypot(right, bottom);
                Animator reveal1 = ViewAnimationUtils.createCircularReveal(v, cx1, cy1, 0, radius1);
                reveal1.setInterpolator(new DecelerateInterpolator(2f));
                reveal1.setDuration(1000);
                reveal1.start();*/

               /* //EFFECT START WITH CENTER
                float finalRadius = (float) Math.hypot(v.getWidth(), v.getHeight());
                int cx1 = (framelayout.getLeft() + framelayout.getRight()) / 2;
                int cy1 = (framelayout.getTop() + framelayout.getBottom()) / 2;
                Animator anim = ViewAnimationUtils.createCircularReveal(v, cx1, cy1, 0, finalRadius);
                anim.setDuration(1000);
                anim.setInterpolator(new AccelerateDecelerateInterpolator());
                anim.start();*/

                    //OPEN WITH BOTTOM CENTER
                    int cx = (rootview.getLeft() + rootview.getRight()) / 2;
                    int cy = (rootview.getTop() + rootview.getBottom());
                    // get the hypothenuse so the radius is from one corner to the other
                    int radius = (int) Math.hypot(right, bottom);
                    Animator reveal = ViewAnimationUtils.createCircularReveal(v, cx, cy, 0, radius);
                    reveal.setInterpolator(new AccelerateDecelerateInterpolator());
                    reveal.setDuration(350);
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
            Configuration.Builder configurationBuilder = new Configuration.Builder(this);
            configurationBuilder.addModelClass(ir.mohammadpour.app.model.Message.class);
            configurationBuilder.addModelClass(SelectedAddressModel.class);
            ActiveAndroid.initialize(configurationBuilder.create());
        }

        /*SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Server_MainUrl", Consts.SERVER_URL);
        editor.apply();
        editor.commit();
        */


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
                CheckUpdate checkUpdate=new CheckUpdate();
                checkUpdate.Check(activity,c);

            }
        };
        handler.sendEmptyMessage(ARROW_MESSAGES2);


        IntentLauncher launcher = new IntentLauncher();
        launcher.start();
       //startService(new Intent(this, FirebaseMessagingService.class));

       // String refreshedToken = FirebaseInstanceId.getInstance().getToken();
       // Log.d("token", "Refreshed token: " + refreshedToken);
    }
    private static int SLEEP_TIME = 5;
    private class IntentLauncher extends Thread {
        @Override
        /**
         * Sleep for some time and than start new activity.
         */
        public void run() {
            String _phone = prefs.getString("customer_phone", null);

            try {
                Thread.sleep(SLEEP_TIME * 1000);
            }
            catch (InterruptedException ex){}
            if(_phone!=null) {
                    String Server_MainUrl = prefs.getString("Server_MainUrl", null);
                    ServiceGenerator.API_BASE_URL=Server_MainUrl;

                    String Server_CityId=prefs.getString("Server_CityId","");
                FirebaseMessaging.getInstance().subscribeToTopic("all_passengers");
                FirebaseMessaging.getInstance().subscribeToTopic("passenger"+Server_CityId+Server_MainUrl.replace("/","").replace(".","").replace(":",""));

                String active = prefs.getString("active", null);
                if(active!=null)
                {
                    if(active.equals("true"))
                    {

                        new GetStatus().execute(_phone,Server_MainUrl);
                    }
                    else if(active.equals("false"))
                    {
                        Intent i =new Intent(c,LoginActivity.class);
                        startActivity(i);
                        SplashActivity.this.finish();
                    }
                }
                // new GetUserDetails().execute(_phone,Server_MainUrl);

            }
            if(_phone==null)
            {
                Intent i =new Intent(c,LoginActivity.class);
                startActivity(i);
                SplashActivity.this.finish();
            }

        }
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
    }
    private class GetStatus extends AsyncTask<String, Void, String> {

        @Override
        protected  void onPreExecute()
        {
            //dialog.show();
            // progress= new ProgressDialog(context);
            // progress.setMessage("Loading");
            //progress.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try
            {
                Log.v("url",params[1]+"/home/CheckPassengerStatus?phone="+params[0]);
                URL url2 = new URL(params[1]+"/home/CheckPassengerStatus?phone="+params[0]);
                BufferedReader in2 = new BufferedReader(new InputStreamReader(url2.openStream(), "UTF-8"));

                String builder ="";
                for (String line = null; (line = in2.readLine()) != null;) {
                    builder+=(line+"\n");
                }

                in2.close();

                return builder;



            } catch (Exception e) {
                return null;
            }

        }

        @Override
        protected void onPostExecute(String result) {

            if(result==null) {
                ///dialog.cancel();
                AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                builder.setTitle("");
                builder.setMessage("مشکل در برقراری ارتباط با سرور اسگاد");

                String positiveText = "سعی مجدد";
                builder.setPositiveButton(positiveText,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // positive button logic

                                String _phone = prefs.getString("customer_phone", null);
                                String Server_MainUrl = prefs.getString("Server_MainUrl", null);
                                new GetStatus().execute(_phone,Server_MainUrl);

                            }
                        });


                builder.setCancelable(false);
                builder.setNegativeButton("انصراف", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                });
                AlertDialog dialog = builder.create();
                // display dialog
                dialog.show();
                return;

            }

            //Here you are done with the task
            try {

                 if(result.trim().equals("notregister"))
                {
                    prefs.edit().remove("transfer_id").commit();
                    prefs.edit().remove("transfer_state").commit();
                    prefs.edit().remove("driver_avatar_path").commit();
                    prefs.edit().remove("driver_name").commit();
                    prefs.edit().remove("driver_car_name").commit();
                    prefs.edit().remove("driver_car_model").commit();
                    prefs.edit().remove("driver_pelak_number").commit();
                    prefs.edit().remove("driver_phone").commit();
                    prefs.edit().remove("customer_phone").commit();
                    prefs.edit().remove("moarref_unique_id").commit();
                    prefs.edit().remove("customer_money").commit();
                    prefs.edit().remove("customer_password").commit();
                    prefs.edit().remove("active").commit();
                    prefs.edit().remove("payment_type").commit();
                    prefs.edit().remove("transfer_price").commit();
                    Intent i =new Intent(c,LoginActivity.class);
                    startActivity(i);
                    SplashActivity.this.finish();
                    return;
                }
                else if(result.contains("false:"))
                {

                    Toast.makeText(c,"مشکل در سوابق",Toast.LENGTH_SHORT).show();

                }
                else
                 {
                     JSONObject obj=new JSONObject(result);

                     if(!obj.getBoolean("Active")) {
                         Toast.makeText(c, "حساب کاربری شما مسدود می باشد لطفا با پشتیبانی مرکزی تماس بگیرید", Toast.LENGTH_LONG).show();
                         return;
                     }
                     SharedPreferences.Editor editor2 = prefs.edit();
                     editor2.putString("BackUpPhone", obj.getString("BackUpPhone"));
                     editor2.putBoolean("HasShop", obj.getBoolean("HasShop"));
                     editor2.putString("customer_name",obj.getString("Name"));
                     editor2.putString("moarref_unique_id", obj.getString("Uniqueid"));
                     editor2.putString("customer_money", obj.getString("Money"));
                     editor2.putString("Server_Name", obj.getString("CityName"));
                     editor2.putBoolean("Has_Service", obj.getBoolean("Has_Service"));
                     editor2.putString("Server_ContactUs", obj.getString("Server_ContactUs"));
                     editor2.putInt("ServiceTimeSecond",obj.getInt("ServiceTimeSecond"));
                     editor2.putString("customer_virtual_money", obj.getString("VirtualMoney"));
                     editor2.apply();
                     editor2.commit();
                     if(obj.isNull("transfer") || obj.getString("transfer")==null || obj.getString("transfer").equals(""))
                     {
                         prefs.edit().remove("transfer_id").commit();
                         prefs.edit().remove("transfer_state").commit();
                         prefs.edit().remove("driver_avatar_path").commit();
                         prefs.edit().remove("driver_name").commit();
                         prefs.edit().remove("driver_car_name").commit();
                         prefs.edit().remove("driver_car_model").commit();
                         prefs.edit().remove("driver_pelak_number").commit();
                         prefs.edit().remove("driver_phone").commit();
                         prefs.edit().remove("payment_type").commit();
                         prefs.edit().remove("transfer_price").commit();
                         prefs.edit().remove("customer_lat");
                         prefs.edit().remove("customer_lng");
                         prefs.edit().remove("dest_lat");
                         prefs.edit().remove("dest_lng");
                         prefs.edit().remove("second_dest_lat");
                         prefs.edit().remove("second_dest_lng");
                         prefs.edit().remove("wait_time_code");
                         prefs.edit().remove("Bar");
                         prefs.edit().remove("RaftBargasht");
                     }
                     else
                     {
                         JSONObject transfer=new JSONObject(obj.getString("transfer"));
                         SharedPreferences.Editor editor = prefs.edit();
                         editor.putString("transfer_id",transfer.getString("Uniqueid"));
                         editor.putString("transfer_state","true");

                         editor.putString("BackUpPhone", obj.getString("BackUpPhone"));
                         editor.putBoolean("HasShop", obj.getBoolean("HasShop"));
                         editor.putString("customer_virtual_money", obj.getString("VirtualMoney"));
                         editor.putString("Server_MainUrl", obj.getString("Server_URL"));
                         editor.putString("driver_name", transfer.getString("DriverName"));
                         editor.putBoolean("payment_type", transfer.getBoolean("payment_type"));
                         editor.putString("driver_avatar_path", transfer.getString("DriverAvatar"));
                         editor.putString("driver_car_name", transfer.getString("CarName"));
                         editor.putString("transfer_price", transfer.getString("Price"));


                         editor.putString("customer_lat", transfer.getString("Customer_lat"));
                         editor.putString("customer_lng", transfer.getString("Csutomer_lng"));
                         editor.putString("dest_lat", transfer.getString("Dest_lat"));
                         editor.putString("dest_lng", transfer.getString("Dest_lng"));
                         editor.putString("second_dest_lat", transfer.getString("Second_Dest_lat"));
                         editor.putString("second_dest_lng", transfer.getString("Second_dest_lng"));
                         editor.putInt("wait_time_code", transfer.getInt("waittime"));
                         editor.putBoolean("Bar", transfer.getBoolean("Bar"));
                         editor.putBoolean("RaftBargasht", transfer.getBoolean("RaftBargasht"));

                         editor.putString("driver_car_model", transfer.getString("CarModel"));
                         editor.putString("driver_pelak_number", transfer.getString("CarPelak"));
                         editor.putString("driver_phone", transfer.getString("DriverPhone"));
                         editor.putString("Server_Name", obj.getString("CityName"));
                         editor.putBoolean("Has_Service", obj.getBoolean("Has_Service"));
                         editor.putString("Server_ContactUs", obj.getString("Server_ContactUs"));
                         editor.putInt("ServiceTimeSecond",obj.getInt("ServiceTimeSecond"));

                         editor.apply();
                         editor.commit();


                         boolean isStarted= isMyServiceRunning(TransferService.class);
                         if(!isStarted)
                         {
                             startService(new Intent(activity, TransferService.class));
                         }

                         Intent i =new Intent(c,SearchPlaceOnMapActivity.class);
                         startActivity(i);
                         SplashActivity.this.finish();
                         return;

                     }




                     if(!obj.getBoolean("Has_Service"))
                     {
                         String Server_Name = prefs.getString("Server_Name", null);

                        /* if(Server_Name.contains("تهران"))
                         {
                             Intent i = new Intent(c, ServicesActivity.class);
                             startActivity(i);
                             SplashActivity.this.finish();
                         }
                         else */
                             {
                             Intent i = new Intent(c, SearchPlaceOnMapActivity.class);
                             startActivity(i);
                             SplashActivity.this.finish();
                         }
                     }
                     else {
                         Intent i = new Intent(c, ServicesActivity.class);
                         startActivity(i);
                         SplashActivity.this.finish();
                     }


                 }
            }
            catch(JSONException ex){

            }
            //dialog.cancel();
        }
    }




    private class GetUserDetails extends AsyncTask<String, Void, String> {

        @Override
        protected  void onPreExecute()
        {
            //dialog.show();
            // progress= new ProgressDialog(context);
            // progress.setMessage("Loading");
            //progress.show();
        }
        @Override
        protected String doInBackground(String... params) {
            try
            {
                Log.v("url",params[1]+"/home/GetUserDetailsByPhone?phone="+params[0]);
                URL url2 = new URL(params[1]+"/home/GetUserDetailsByPhone?phone="+params[0]);
                BufferedReader in2 = new BufferedReader(new InputStreamReader(url2.openStream(), "UTF-8"));

                String builder ="";
                for (String line = null; (line = in2.readLine()) != null;) {
                    builder+=(line+"\n");
                }

                in2.close();

                return builder;



            } catch (Exception e) {
                return null;
            }

        }

        @Override
        protected void onPostExecute(String result) {
            if(result==null) {

                return;
            }
            //Here you are done with the task
            try {

                JSONObject obj=new JSONObject(result);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("customer_name", obj.getString("name"));
                editor.apply();
                editor.commit();
            }
            catch(Exception ex){}
            //dialog.cancel();
        }
    }

    private class GetCityDetails extends AsyncTask<String, Void, String> {

        @Override
        protected  void onPreExecute()
        {
            //dialog.show();
            // progress= new ProgressDialog(context);
            // progress.setMessage("Loading");
            //progress.show();
        }
        @Override
        protected String doInBackground(String... params) {
            try
            {
                Log.v("url","http://scad.ir/getcitybyid.php?id="+params[0]);
                URL url2 = new URL("http://scad.ir/getcitybyid.php?id="+params[0]);
                BufferedReader in2 = new BufferedReader(new InputStreamReader(url2.openStream(), "UTF-8"));

                String builder ="";
                for (String line = null; (line = in2.readLine()) != null;) {
                    builder+=(line+"\n");
                }

                in2.close();

                return builder;



            } catch (Exception e) {
                return null;
            }

        }

        @Override
        protected void onPostExecute(String result) {
            if(result==null) {
                return;
            }

            try {

                JSONArray json=new JSONArray(result);

                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("Server_MainUrl", json.getJSONObject(0).getString("MainUrl"));
                ServiceGenerator.API_BASE_URL=json.getJSONObject(0).getString("MainUrl");
                editor.putString("Server_Name", json.getJSONObject(0).getString("Name"));
                editor.putString("Server_ContactUs", json.getJSONObject(0).getString("ContactUs"));
                editor.putString("Server_CityId", json.getJSONObject(0).getString("Id"));
                editor.putString("Server_Lat", json.getJSONObject(0).getString("Lat"));
                editor.putString("Server_Lng", json.getJSONObject(0).getString("Lng"));
                editor.apply();
                editor.commit();

                String _phone = prefs.getString("customer_phone", null);
                String Server_MainUrl = prefs.getString("Server_MainUrl", null);
                new GetStatus().execute(_phone,Server_MainUrl,json.getJSONObject(0).getString("Id"));



            }
            catch(JSONException ex){}
            //dialog.cancel();
        }
    }
    
}
