package ir.mohammadpour.app.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import ir.mohammadpour.app.R;
import ir.mohammadpour.app.network.BaseServer;
import ir.mohammadpour.app.network.ServiceGenerator;
import ir.mohammadpour.app.ui.util.CancelTransfer;
import ir.mohammadpour.app.ui.util.ConnectionDetector;
import ir.mohammadpour.app.ui.util.LocationService;
import ir.mohammadpour.app.ui.util.SlideView;
import retrofit2.Call;
import retrofit2.Callback;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class WaitingActivity extends AppCompatActivity {

    TextView txtWait,txtWait2,txtWait3;
    SharedPreferences prefs;
    String transfer_state="";
    Context c;
     Handler h = new Handler();
    Runnable r;
    int counter=0;
    SlideView btn_cancel;
    ProgressDialog simpleLoading;
    String Server_MainUrl;
    Activity a;
    Double price=0.0;
    String customer_lat="",customer_lng="",lat="",lng="",maghsad2_lat,maghsad2_lng="",raft_bargasht="",bar="",tavagof="-1";
    boolean payment_type=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        //Remove notification bar
        // this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        setContentView(R.layout.activity_waiting);

        simpleLoading = new ProgressDialog(this);
        simpleLoading.setMessage("در حال ارسال اطلاعات");
        simpleLoading.setCancelable(false);
        btn_cancel=(SlideView) findViewById(R.id.slidercancel);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        c=this;
        a=this;
        Server_MainUrl = prefs.getString("Server_MainUrl", null);


        btn_cancel.setOnSlideCompleteListener(new SlideView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideView slideView) {
                // vibrate the device
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(100);

                //new CancelTransfer().execute(prefs.getString("transfer_id", null));
                ir.mohammadpour.app.ui.util.CancelTransfer.CancelTransfer(a,c);
            }
        });


        txtWait=(TextView)findViewById(R.id.txtWait);
        txtWait2=(TextView) findViewById(R.id.txtWait2);
        txtWait3=(TextView) findViewById(R.id.txtWait3);
        Typeface type = Typeface.createFromAsset(this.getAssets(),"IRANSans.ttf");
        txtWait.setTypeface(type);
        txtWait2.setTypeface(type);
        txtWait3.setTypeface(type);

        Bundle extras=getIntent().getExtras();
        if(extras!=null)
        {
               //String url= extras.getString("url","");
               //new RequestTaxi().execute(url);


            String address= extras.getString("address","");
            String transfer_type=extras.getString("transfer_type","0");
            lat= extras.getString("lat","");
            lng= extras.getString("lng","");
            String mabdastr= extras.getString("mabdastr","");
            customer_lat= extras.getString("customer_lat","");
            customer_lng= extras.getString("customer_lng","");
            String tozihat= extras.getString("tozihat","");
            maghsad2_lat= extras.getString("maghsad2_lat","");
            maghsad2_lng= extras.getString("maghsad2_lng","");
            String takhfif_unique_id= extras.getString("takhfif_unique_id","");
            raft_bargasht= extras.getString("raft_bargasht","");
            bar= extras.getString("bar","");
            tavagof= extras.getString("tavagof","-1");
            String phone= extras.getString("phone","");
            payment_type= extras.getBoolean("payment_type",false);
            price= extras.getDouble("price",0);

            RequestTaxi2(address,lat,lng
                    ,mabdastr,customer_lat,customer_lng
                    ,tozihat,maghsad2_lat,maghsad2_lng
                    ,takhfif_unique_id,raft_bargasht
                    ,bar,tavagof,phone,transfer_type,payment_type);

        }
        else
            finish();


    }
    private class CheckRequest extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                Log.v("url", Server_MainUrl+"/request/CheckRequestAndroid?uniqueId=" + params[0]+"&phone="+params[1]);

                URL url2 = new URL(Server_MainUrl+"/request/CheckRequestAndroid?uniqueId=" + params[0]+"&phone="+params[1]);

                BufferedReader in2 = new BufferedReader(new InputStreamReader(url2.openStream(), "UTF-8"));

                String builder = "";
                for (String line = null; (line = in2.readLine()) != null; ) {
                    builder += (line + "\n");
                }

                in2.close();

                return builder;


            } catch (Exception e) {
                return null;
            }



        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
               // String transfer_id = prefs.getString("transfer_id", null);
                //String customer_phone = prefs.getString("customer_phone", null);
//
               // new CheckRequest().execute(transfer_id,customer_phone);
               // return;


            }
            //Here you are done with the task
            try {


                JSONObject obj=new JSONObject(result);
                if(!obj.getString("FullName").contains("false"))
                {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("transfer_state", "true");

                    editor.putString("driver_avatar_path", obj.getString("AvatarPath"));
                    editor.putString("driver_name", obj.getString("FullName"));
                    editor.putString("driver_car_name", obj.getString("CarName"));
                    editor.putString("driver_car_model", obj.getString("CarModel"));
                    editor.putString("driver_pelak_number", obj.getString("PelakNumber"));
                    editor.putString("driver_phone", obj.getString("Phone"));
                    editor.putBoolean("payment_type", payment_type);
                    editor.putString("transfer_price", price.toString());


                    editor.putString("customer_lat", customer_lat);
                    editor.putString("customer_lng", customer_lng);
                    editor.putString("dest_lat", lat);
                    editor.putString("dest_lng", lng);
                    editor.putString("second_dest_lat", maghsad2_lat);
                    editor.putString("second_dest_lng", maghsad2_lng);

                    try {
                        editor.putInt("wait_time_code", Integer.parseInt(tavagof));
                    }
                    catch (NullPointerException ex)
                    {

                    }
                    editor.putBoolean("Bar", Boolean.parseBoolean(bar));
                    editor.putBoolean("RaftBargasht", Boolean.parseBoolean(raft_bargasht));

                    editor.apply();
                    editor.commit();

                    setResult(RESULT_OK, getIntent());
                    finish();
                    //stopService(new Intent(c,TransferService.class));

                    h.removeCallbacks(r);
                    h.removeCallbacksAndMessages(null);
                   // startService(new Intent(c, TransferService.class));
                }
                else
                {

                }

                //if(Integer.parseInt(animator.getAnimatedValue().toString())<Integer.parseInt(result.trim()))
                //   animator.setObjectValues(Integer.parseInt(animator.getAnimatedValue().toString()),Integer.parseInt(result.trim()));
                // else {

            } catch (JSONException ex) {
            }



        }
    }
    @Override
    public void onBackPressed() {

    }

    @Override
    public void onDestroy () {

        h.removeCallbacks(r);
        h.removeCallbacksAndMessages(null);
        super.onDestroy ();

    }


    private class RequestTaxi extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            // progress= new ProgressDialog(context);
            // progress.setMessage("Loading");
            //progress.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                Log.v("url", params[0]);
                URL url2 = new URL(params[0]
                );
                BufferedReader in2 = new BufferedReader(new InputStreamReader(url2.openStream(), "UTF-8"));

                String builder = "";
                for (String line = null; (line = in2.readLine()) != null; ) {
                    builder += (line + "\n");
                }

                in2.close();

                return builder;


            } catch (Exception e) {
                return null;
            }

        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                Toast.makeText(c,"مشکل در درخواست",Toast.LENGTH_LONG).show();
                setResult(RESULT_CANCELED, getIntent());
                finish();
                return;
            }
            //Here you are done with the task
            try {


                String res = result.trim();
                //Toast.makeText(c,res,Toast.LENGTH_SHORT).show();
                if (res.contains("transfer")) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("transfer_id", res.replace("~", "").trim());
                    editor.putString("transfer_state", "false");

                    editor.apply();
                    editor.commit();
                    //startService(new Intent(c, TransferService.class));

                    //Intent i = new Intent(c, WaitingActivity.class);
                   // i.putExtra("transfer_id", res.replace("~", "").trim());
                    //startActivityForResult(i, 1);
                    final int delay = 1000; //milliseconds

                    final int ServiceTimeSecond = prefs.getInt("ServiceTimeSecond", 25);
                    r=new Runnable(){
                        public void run(){
                            try {
                                // Toast.makeText(mContext, "test : " + loc, Toast.LENGTH_LONG).show();

                                counter++;
                                transfer_state = prefs.getString("transfer_state", "false");
                                if(transfer_state.equals("true"))
                                {
                                    //Intent i=new Intent(c,CurrentTransferActivity.class);
                                    //startActivity(i);
                                    //getIntent().putExtra("res", "1");
                                    setResult(RESULT_OK, getIntent());
                                    finish();
                                    //stopService(new Intent(c,TransferService.class));

                                    h.removeCallbacks(this);
                                    h.removeCallbacksAndMessages(null);


                                }

                                if(counter>=ServiceTimeSecond)
                                {
                                    String transfer_id = prefs.getString("transfer_id", null);
                                    String customer_phone = prefs.getString("customer_phone", null);

                                    new CheckRequest().execute(transfer_id,customer_phone);
                                }
                            }
                            catch(Exception ex){
                                Log.v("transfer service:",ex.getMessage().toString());
                            }
                            h.postDelayed(this, delay);
                        }
                    };
                    h.postDelayed(r, delay);
                }

            } catch (Exception ex) {
                Toast.makeText(c,"مشکل در درخواست",Toast.LENGTH_LONG).show();
                setResult(RESULT_CANCELED, getIntent());
                finish();
            }
        }
    }





    private void RequestTaxi2(final String address,final String lat,final String lng
           , final String mabdaStr,final String Customer_lat,final String Customer_lng
           ,final String tozihat,final String maghsad2_lat,final String maghsad2_lng
           ,final String Takhfif_unique_id,final String raft_bargasht
            ,final String bar,final String Tavagof,final String phone,final String transfer_type
    ,final boolean payment_type)
    {
       // loading.setVisibility(View.VISIBLE);

        Log.v("testReq","1");
        String token=prefs.getString("token","");

        String devicename = Build.MANUFACTURER
                + " " + Build.MODEL + " " + Build.VERSION.RELEASE
                + " " + Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName();
        ServiceGenerator.API_BASE_URL=Server_MainUrl;

        Log.v("testReq",devicename);
        BaseServer apiService = ServiceGenerator.createService(BaseServer.class);
        if (new ConnectionDetector(a).isConnectingToInternet()) {
            //Connected to the Internet
            Call<String> call = apiService.addTransferRequest(
                     address
                    ,lat
                    ,lng
                    ,mabdaStr
                    ,Customer_lat
                    ,Customer_lng
                    ,mabdaStr
                    ,tozihat
                    ,Takhfif_unique_id
                    ,maghsad2_lat
                    ,maghsad2_lng
                    ,raft_bargasht
                    ,bar
                    ,Tavagof
                    ,""
                    ,phone
                    ,token
                    ,devicename
                    ,transfer_type
                    ,payment_type+""
            );
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                    String res = response.body();
                    Log.v("testReq","2");
                   // loading.setVisibility(View.GONE);
                   // Log.v("testReq", new Gson().toJson(response));
                    if (res != null) {
                        Log.v("testReq",res);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("transfer_id", res.replace("~", "").trim());
                            editor.putString("transfer_state", "false");

                            editor.apply();
                            editor.commit();
                            //startService(new Intent(c, TransferService.class));

                            //Intent i = new Intent(c, WaitingActivity.class);
                            // i.putExtra("transfer_id", res.replace("~", "").trim());
                            //startActivityForResult(i, 1);
                            final int delay = 2000; //milliseconds
                        Log.v("testReq","4");
                            final int ServiceTimeSecond = prefs.getInt("ServiceTimeSecond", 25);
                        Log.v("testReq","5~"+ServiceTimeSecond);
                            r=new Runnable(){
                                public void run(){
                                    try {
                                        // Toast.makeText(mContext, "test : " + loc, Toast.LENGTH_LONG).show();

                                        counter+=2;
                                        String transfer_id = prefs.getString("transfer_id", null);
                                        String customer_phone = prefs.getString("customer_phone", null);

                                        if(counter>=ServiceTimeSecond)
                                        {
                                            setResult(RESULT_CANCELED, getIntent());
                                            finish();
                                            //stopService(new Intent(c,TransferService.class));

                                            h.removeCallbacks(r);
                                            h.removeCallbacksAndMessages(null);

                                            Toast.makeText(c, "متاسفانه راننده ای درخواست سفر شما را نپذیرفت لطفا دوباره درخواست دهید", Toast.LENGTH_LONG).show();

                                        }
                                        else
                                             new CheckRequest().execute(transfer_id,customer_phone);
                                        //transfer_state = prefs.getString("transfer_state", "false");
                                        //Log.v("testReq","7"+transfer_state);

                                     }
                                    catch (NullPointerException ex){

                                    }
                                    catch(Exception ex){
                                        //Log.v("transfer service:",ex.getMessage().toString());
                                    }

                                    h.postDelayed(this, delay);
                                }
                            };
                            h.postDelayed(r, delay);

                    }
                    else {

                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                  //  loading.setVisibility(View.GONE);
                    // Log error here since request failed
                    //Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    //intent.putExtra("regid", regId);
                    //startActivity(intent);
                    //finish();


                    Toast.makeText(c,"مشکل در دریافت اطلاعات",Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            //loading.setVisibility(View.GONE);


            AlertDialog.Builder dialog = new AlertDialog.Builder(c);
            dialog.setMessage("مشکل در برقراری ارتباط");
            dialog.setPositiveButton("سعی مجدد", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    //new GetAroundTaxies().execute(mMap.getCameraPosition().target.latitude + "", mMap.getCameraPosition().target.longitude + "", 500 + "");
                    RequestTaxi2(address,lat,lng
                   ,mabdaStr,Customer_lat,Customer_lng
                   ,tozihat,maghsad2_lat,maghsad2_lng
                   ,Takhfif_unique_id,raft_bargasht
                    ,bar,Tavagof,phone,transfer_type,payment_type);
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

        }
    }

}
