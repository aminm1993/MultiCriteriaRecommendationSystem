package ir.mohammadpour.app.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import ir.mohammadpour.app.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class ActiveActivity extends AppCompatActivity {

    Button btnactive;
    EditText txtcode;
    TextView textView,textView2,textView3,btnretry,textView4;
    ProgressDialog dialog;
    Context c;
    RelativeLayout rootview;
    Activity activity;
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
        setContentView(R.layout.activity_active);

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


        Typeface type = Typeface.createFromAsset(this.getAssets(),"IRANSans.ttf");
        c=this;
        activity=this;
        dialog = new ProgressDialog(this);
        dialog.setMessage("در حال ارسال اطلاعات");
        dialog.setCancelable(false);
        btnactive=(Button)findViewById(R.id.btnactive);
        txtcode=(EditText)findViewById(R.id.txtcode);

        textView=(TextView)findViewById(R.id.textView);
        textView2=(TextView)findViewById(R.id.textView2);
        textView3=(TextView)findViewById(R.id.textView3);
        btnretry=(TextView)findViewById(R.id.btnretry);
        textView4=(TextView)findViewById(R.id.textView4);

        btnactive.setTypeface(type);
        txtcode.setTypeface(type);
        textView.setTypeface(type);
        textView2.setTypeface(type);
        textView3.setTypeface(type);
        btnretry.setTypeface(type);
        textView4.setTypeface(type);

        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final String customer_phone = prefs.getString("customer_phone", null);
        final String Server_MainUrl = prefs.getString("Server_MainUrl", null);

        btnactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ActiveUser().execute(customer_phone,toEnglishNumber(txtcode.getText().toString()),Server_MainUrl);
            }
        });


    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");
                //Do whatever you want with the code here

                Log.v("SmsReceiver", message);

                txtcode.setText(message);
                final String customer_phone = prefs.getString("customer_phone", null);
                final String Server_MainUrl = prefs.getString("Server_MainUrl", null);
                new ActiveUser().execute(customer_phone,toEnglishNumber(txtcode.getText().toString()),Server_MainUrl);

            }
        }
    };
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            // previously visible view
            final View myView = findViewById(R.id.rootview);

            // get the center for the clipping circle
            int cx = myView.getWidth() / 2;
            int cy = myView.getHeight() / 2;

            // get the initial radius for the clipping circle
            float initialRadius = (float) Math.hypot(cx, cy);

            // create the animation (the final radius is zero)
            Animator anim =
                    ViewAnimationUtils.createCircularReveal(myView, cx, cy, initialRadius, 0);
            anim.setDuration(800);
            // make the view invisible when the animation is done
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    myView.setVisibility(View.INVISIBLE);
                }
            });

            // start the animation
            anim.start();
        }
    }
    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
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
    private String toEnglishNumber(String input)
    {
        String[] persian = new String[]{ "۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹" };

        for (int j = 0; j < persian.length; j++) {
            input= input.replace(persian[j],j+"");
        }
        return input;
    }
    private class ActiveUser extends AsyncTask<String, Void, String> {

        @Override
        protected  void onPreExecute()
        {
            dialog.show();
            // progress= new ProgressDialog(context);
            // progress.setMessage("Loading");
            //progress.show();
        }
        @Override
        protected String doInBackground(String... params) {
            try
            {
                //Log.v("url",params[2]+"/home/ActiveUser?phone="+params[0]+"&code="+params[1]);
                //URL url2 = new URL(params[2]+"/home/ActiveUser?phone="+params[0]+"&code="+params[1]);
                Log.v("url",params[2]+"/passenger/ActiveOnly?phone="+params[0]+"&code="+params[1]);
                URL url2 = new URL(params[2]+"/passenger/ActiveOnly?phone="+params[0]+"&code="+params[1]);
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
                dialog.cancel();
                return;

            }
            //Here you are done with the task
            try {
                if(result.contains("true"))
                {

                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("active", "true");
                    editor.putString("customer_password",txtcode.getText().toString());

                    editor.apply();
                    editor.commit();

                    String customer_name=prefs.getString("customer_name",null);
                    if(customer_name==null || customer_name.equals(""))
                    {
                        Intent i=new Intent(c,RegisterActivity.class);
                        startActivity(i);
                        finish();
                    }
                    else {

                        String _phone = prefs.getString("customer_phone", null);
                        String Server_MainUrl = prefs.getString("Server_MainUrl", null);
                        new GetStatus().execute(_phone,Server_MainUrl);
                    }


                }
                else
                {
                    Snackbar.make(btnactive,"کد فعال سازی منقضی شده یا صحیح نمی باشد",Snackbar.LENGTH_LONG).show();
                }

            }
            catch(Exception ex){}
            dialog.cancel();
        }
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
                AlertDialog.Builder builder = new AlertDialog.Builder(ActiveActivity.this);
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

                    Intent i =new Intent(c,RegisterActivity.class);
                    startActivity(i);
                    ActiveActivity.this.finish();
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
                        editor.putString("driver_name", transfer.getString("DriverName"));
                        editor.putString("driver_avatar_path", transfer.getString("DriverAvatar"));
                        editor.putString("driver_car_name", transfer.getString("CarName"));
                        editor.putBoolean("payment_type", transfer.getBoolean("payment_type"));
                        editor.putString("customer_lat", transfer.getString("Customer_lat"));
                        editor.putString("customer_lng", transfer.getString("Csutomer_lng"));
                        editor.putString("dest_lat", transfer.getString("Dest_lat"));
                        editor.putString("dest_lng", transfer.getString("Dest_lng"));
                        editor.putString("second_dest_lat", transfer.getString("Second_Dest_lat"));
                        editor.putString("second_dest_lng", transfer.getString("Second_dest_lng"));
                        editor.putInt("wait_time_code", transfer.getInt("waittime"));
                        editor.putBoolean("Bar", transfer.getBoolean("Bar"));
                        editor.putBoolean("RaftBargasht", transfer.getBoolean("RaftBargasht"));
                        editor.putString("transfer_price", transfer.getString("Price"));
                        editor.putString("driver_car_model", transfer.getString("CarModel"));
                        editor.putString("driver_pelak_number", transfer.getString("CarPelak"));
                        editor.putString("driver_phone", transfer.getString("DriverPhone"));
                        editor.putString("Server_Name", obj.getString("CityName"));
                        editor.putBoolean("Has_Service", obj.getBoolean("Has_Service"));
                        editor.putString("BackUpPhone", obj.getString("BackUpPhone"));
                        editor.putString("Server_ContactUs", obj.getString("Server_ContactUs"));
                        editor.apply();
                        editor.commit();


                        boolean isStarted= isMyServiceRunning(TransferService.class);
                        if(!isStarted)
                        {
                            startService(new Intent(activity, TransferService.class));
                        }

                        String Server_Name = prefs.getString("Server_Name", null);

                       /* if(Server_Name.contains("تهران"))
                        {
                            Intent i = new Intent(c, ServicesActivity.class);
                            startActivity(i);
                            finish();
                        }
                        else */
                            {

                            Intent i = new Intent(c, SearchPlaceOnMapActivity.class);
                            startActivity(i);
                            ActiveActivity.this.finish();
                        }
                        return;

                    }


                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("customer_name",obj.getString("Name"));
                    editor.putString("moarref_unique_id", obj.getString("Uniqueid"));
                    editor.putString("customer_money", obj.getString("Money"));
                    editor.putString("Server_Name", obj.getString("CityName"));
                    editor.putString("BackUpPhone", obj.getString("BackUpPhone"));
                    editor.putBoolean("Has_Service", obj.getBoolean("Has_Service"));
                    editor.putString("Server_ContactUs", obj.getString("Server_ContactUs"));
                    editor.apply();
                    editor.commit();


                    if(!obj.getBoolean("Has_Service"))
                    {
                        String Server_Name = prefs.getString("Server_Name", null);

                        /*if(Server_Name.contains("تهران"))
                        {
                            Intent i = new Intent(c, ServicesActivity.class);
                            startActivity(i);
                            finish();
                        }
                        else */{
                            Intent i = new Intent(c, SearchPlaceOnMapActivity.class);
                            startActivity(i);
                            ActiveActivity.this.finish();
                        }
                    }
                    else {

                        Intent i = new Intent(c, ServicesActivity.class);
                        startActivity(i);
                        ActiveActivity.this.finish();
                    }


                }
            }
            catch(JSONException ex){

            }
            //dialog.cancel();
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
