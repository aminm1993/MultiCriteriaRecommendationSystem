package ir.mohammadpour.app.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.firebase.messaging.FirebaseMessaging;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ir.mohammadpour.app.R;
import ir.mohammadpour.app.network.ServiceGenerator;
import ir.mohammadpour.app.ui.util.Consts;
import ir.mohammadpour.app.ui.widget.CheckUpdate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    EditText phone,pass;
    TextView login,to,youraccound,kitkat;
    Button areUnew;
    Button btnlogin,forgetpass;
    ProgressDialog dialog;
    SharedPreferences prefs;
    Context c;
    String [] spinnerNames;

    String [] spinnerIds;
    String [] Lat;
    String [] Lng;
    String [] ServerUrls;
    String [] BackUpPhones;
    int [] ServiceTimeSecond;
    String [] ContactUs;
    Spinner spinnercity;
    RelativeLayout rootview;
    ExpandableRelativeLayout expandableLayout;
    SweetAlertDialog myDialog;
    private static final int REQUEST_WRITE_STORAGE = 112;
    Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        setContentView(R.layout.activity_login);
        expandableLayout=(ExpandableRelativeLayout)findViewById(R.id.expandableLayout);
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
        activity=this;
        c=this;

        forgetpass=(Button)findViewById(R.id.forgetpass);
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


        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        dialog = new ProgressDialog(this);
        dialog.setMessage("در حال ارسال اطلاعات");
        dialog.setCancelable(false);
        spinnercity=(Spinner)findViewById(R.id.citySpinner);

        spinnercity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if(position!=0)
                   expandableLayout.expand();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        Typeface type = Typeface.createFromAsset(this.getAssets(),"IRANSans.ttf");
        phone=(EditText)findViewById(R.id.txtcode);
        pass=(EditText)findViewById(R.id.txtpass);
        phone.setTypeface(type);
        pass.setTypeface(type);
        btnlogin=(Button)findViewById(R.id.btnactive);
        btnlogin.setTypeface(type);

        login=(TextView)findViewById(R.id.textView);
        to=(TextView)findViewById(R.id.textView2);
        youraccound=(TextView)findViewById(R.id.textView3);
        areUnew=(Button)findViewById(R.id.btnreg);
        kitkat=(TextView)findViewById(R.id.textView4);

        login.setTypeface(type);
        to.setTypeface(type);
        youraccound.setTypeface(type);
        areUnew.setTypeface(type);
        kitkat.setTypeface(type);




        new GetCityLists().execute(Consts.SERVER_URL+"/home/GetAllCities");

        areUnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(v.getContext(),RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spinnercity.getSelectedItemPosition()==0)
                {
                    new SweetAlertDialog(c, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("هشدار")
                            .setContentText("لطفا شهر خود را انتخاب نمایید")
                            .show();

                    return;
                }
                if(phone.getText().toString().length()<11 || !phone.getText().toString().startsWith("09")) {
                    phone.setTextColor(Color.parseColor("#FFDC0105"));
                    Animation shake = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.shake2);
                    phone.startAnimation(shake);

                    Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 500 milliseconds
                    vib.vibrate(200);
                    return;
                }

                SweetAlertDialog alert= new SweetAlertDialog(c, SweetAlertDialog.WARNING_TYPE)

                        .setTitleText("تایید شماره تماس")

                        .setContentText("کد فعال سازی به شماره "+phone.getText().toString()+" ارسال خواهد شد")
                        .setConfirmText("بله ارسال شود")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                if(!pressed) {
                                    myDialog = sDialog;



                                    ServiceGenerator.API_BASE_URL = ServerUrls[spinnercity.getSelectedItemPosition()];
                                    new LoginUser().execute(ServerUrls[spinnercity.getSelectedItemPosition()], toEnglishNumber(phone.getText().toString()), toEnglishNumber(pass.getText().toString())
                                            , spinnerIds[spinnercity.getSelectedItemPosition()]);

                                }
                            }
                        });
                alert.setCancelable(false);
                alert.show();


            }
        });

        String active = prefs.getString("active", null);
        if(active!=null)
        {
            if(active.equals("true"))
            {
                Intent i =new Intent(this,ServicesActivity.class);
                startActivity(i);
                finish();

            }
            else if(active.equals("false"))
            {
               // Intent i =new Intent(this,ActiveActivity.class);
                //startActivity(i);
                String myphone = prefs.getString("customer_phone", null);
                String mypassword = prefs.getString("customer_password", null);

                phone.setText(myphone);
                pass.setText(mypassword);
            }
        }


        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(v.getContext(),ForgetPassActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    boolean pressed=false;
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

    private class LoginUser extends AsyncTask<String, Void, String> {

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
                //Log.v("url",params[0]+"/home/LoginUser?phone="+params[1]+"&password="+params[2]);
                //URL url2 = new URL(params[0]+"/home/LoginUser?phone="+params[1]+"&password="+params[2]);

                Log.v("url",params[0]+"/passenger/LoginOrSignup?phone="+params[1]+"&cityid="+params[3]);
                URL url2 = new URL(params[0]+"/passenger/LoginOrSignup?phone="+params[1]+"&cityid="+params[3]);

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
                //dialog.cancel();
                return;

            }
            //Here you are done with the task
            try {
                pressed=true;
                if(result.trim().equals("notregistered"))//if(result.contains("true"))
                {

                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("customer_phone", toEnglishNumber(phone.getText().toString()));
                    //editor.putString("customer_password", pass.getText().toString());
                    editor.putString("Server_MainUrl", ServerUrls[spinnercity.getSelectedItemPosition()]);
                    editor.putString("Server_Name", spinnerNames[spinnercity.getSelectedItemPosition()]);
                    editor.putString("Server_Lat", Lat[spinnercity.getSelectedItemPosition()]);
                    editor.putString("Server_Lng", Lng[spinnercity.getSelectedItemPosition()]);
                    editor.putString("Server_ContactUs", ContactUs[spinnercity.getSelectedItemPosition()]);
                    editor.putString("Server_CityId", spinnerIds[spinnercity.getSelectedItemPosition()]);
                    editor.putInt("ServiceTimeSecond", ServiceTimeSecond[spinnercity.getSelectedItemPosition()]);
                    //editor.putString("active", "true");

                    editor.apply();
                    editor.commit();

                    FirebaseMessaging.getInstance().subscribeToTopic("all_passengers");
                    FirebaseMessaging.getInstance().subscribeToTopic("passenger"+spinnerIds[spinnercity.getSelectedItemPosition()]+ServerUrls[spinnercity.getSelectedItemPosition()].replace("/","").replace(".","").replace(":",""));

                    myDialog
                            .setTitleText("پیام")
                            .setContentText("پیام با موفقیت برای شما ارسال شد")
                            .setConfirmText("باشه")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    Intent i=new Intent(c,ActiveActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            })
                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);


                }
                else
                {
                    try{
                        JSONObject obj=new JSONObject(result);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("customer_phone",toEnglishNumber(phone.getText().toString()));
                        //editor.putString("customer_password", pass.getText().toString());
                        editor.putString("customer_name", obj.getString("name"));
                        editor.putString("moarref_unique_id", obj.getString("uniqueid"));
                        editor.putString("customer_money", obj.getString("money"));

                        editor.putString("BackUpPhone", BackUpPhones[spinnercity.getSelectedItemPosition()]);
                        editor.putString("Server_MainUrl", ServerUrls[spinnercity.getSelectedItemPosition()]);
                        editor.putString("Server_Name", spinnerNames[spinnercity.getSelectedItemPosition()]);
                        editor.putString("Server_Lat", Lat[spinnercity.getSelectedItemPosition()]);
                        editor.putString("Server_Lng", Lng[spinnercity.getSelectedItemPosition()]);
                        editor.putString("Server_ContactUs", ContactUs[spinnercity.getSelectedItemPosition()]);
                        editor.putString("Server_CityId", spinnerIds[spinnercity.getSelectedItemPosition()]);
                        editor.putInt("ServiceTimeSecond", ServiceTimeSecond[spinnercity.getSelectedItemPosition()]);

                        editor.putString("active", "false");

                        editor.apply();
                        editor.commit();
                        myDialog
                                .setTitleText("پیام")
                                .setContentText("پیام با موفقیت برای شما ارسال شد")
                                .setConfirmText("باشه")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        Intent i=new Intent(c,ActiveActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                })
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);



                        FirebaseMessaging.getInstance().subscribeToTopic("all_passengers");
                        FirebaseMessaging.getInstance().subscribeToTopic("passenger"+spinnerIds[spinnercity.getSelectedItemPosition()]+ServerUrls[spinnercity.getSelectedItemPosition()].replace("/","").replace(".","").replace(":",""));
                    }
                    catch (JSONException ex)
                    {
                        Snackbar.make(phone,"مشکل در برقراری ارتباط",Snackbar.LENGTH_LONG).show();

                    }



                }


            }
            catch(Exception ex){}
            dialog.cancel();
        }
    }


    private String toEnglishNumber(String input)
    {
        String[] persian = new String[]{ "۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹" };

        for (int j = 0; j < persian.length; j++) {
            input= input.replace(persian[j],j+"");
        }
        return input;
    }

    private class GetCityLists extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            // progress.show();
            dialog.show();
        }
        @Override
        protected String doInBackground(String... arg0) {


            try
            {
                URL url = new URL(arg0[0]);
                // Read all the text returned by the server
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

                String builder ="";
                for (String line = null; (line = in.readLine()) != null;) {
                    builder+=(line+"\n");
                }
                in.close();


                return builder;


            } catch (Exception e) {

            }



            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            if(result==null) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(c);
                dialog.setMessage("مشکل در برقراری ارتباط");
                dialog.setPositiveButton("سعی مجدد", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        // TODO Auto-generated method stub
                        new GetCityLists().execute(Consts.SERVER_URL+"/home/GetAllCities");
                    }
                });
                dialog.setNegativeButton("انصراف", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        // TODO Auto-generated method stub

                    }
                });
                dialog.show();

                return;
            }
            //Log.v("test",result);
            try {

                JSONArray json;

                result=result.replace("&quot;","\"");
                json = new JSONArray(result);

                spinnerNames=new String[json.length()+1];
                Lat=new String[json.length()+1];
                Lng=new String[json.length()+1];
                BackUpPhones=new String[json.length()+1];
                ServerUrls=new String[json.length()+1];
                ServiceTimeSecond=new int[json.length()+1];
                ContactUs=new String[json.length()+1];
                spinnerIds=new String[json.length()+1];


                spinnerNames[0]="لطفا شهر خود را انتخاب کنید";
                spinnerIds[0]="-1";
                ServerUrls[0]="";
                Lat[0]="";
                ServiceTimeSecond[0]=0;
                Lng[0]="";
                ContactUs[0]="";
                BackUpPhones[0]="";
                for(int i=0;i<json.length();i++) {
                    ServerUrls[i+1]=json.getJSONObject(i).getString("Default_Server_Url");
                    spinnerNames[i+1]=json.getJSONObject(i).getString("CityName");
                    spinnerIds[i+1]=json.getJSONObject(i).getString("Id");
                    BackUpPhones[i+1]=json.getJSONObject(i).getString("BackUpPhone");
                    Lat[i+1]=json.getJSONObject(i).getString("Lat");
                    Lng[i+1]=json.getJSONObject(i).getString("Lng");
                    ContactUs[i+1]=json.getJSONObject(i).getString("ContactUs");
                    ServiceTimeSecond[i+1]=json.getJSONObject(i).getInt("ServiceTimeSecond");
                }


                ArrayAdapter NoCoreAdapter = new ArrayAdapter(c,
                        R.layout.my_spinner_textview3, spinnerNames){

                    public View getView(int position, View convertView,ViewGroup parent) {

                        View v = super.getView(position, convertView, parent);

                        ((TextView) v).setTextSize(16);
                        Typeface font = Typeface.createFromAsset(getAssets(), "IRANSans.ttf");
                        ((TextView) v).setTypeface(font);
                        return v;

                    }

                    public View getDropDownView(int position, View convertView,ViewGroup parent) {

                        View v = super.getDropDownView(position, convertView,parent);

                        ((TextView) v).setGravity(Gravity.CENTER);
                        Typeface font = Typeface.createFromAsset(getAssets(), "IRANSans.ttf");
                        ((TextView) v).setTypeface(font);
                        return v;

                    }
                };
                spinnercity.setAdapter(NoCoreAdapter);







            }
            catch(Exception ec){}

            // progress.dismiss();
            dialog.cancel();
        }

    }

}
