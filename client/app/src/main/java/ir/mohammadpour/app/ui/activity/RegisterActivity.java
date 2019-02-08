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
import android.graphics.Typeface;
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
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.ghyeok.stickyswitch.widget.StickySwitch;
import ir.mohammadpour.app.R;
import ir.mohammadpour.app.network.ServiceGenerator;
import ir.mohammadpour.app.ui.widget.CheckUpdate;
import ir.smartlab.persiandatepicker.PersianDatePicker;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

public class RegisterActivity extends AppCompatActivity {

    TextView txt_success_name,textView6;
    Button btnreg;
    EditText mail,name,moarref;
    ProgressDialog dialog;
    SharedPreferences prefs;
    Context c;
    Activity activity;
    RadioButton male,female;
    StickySwitch stickySwitch;
    String phone="",Server_MainUrl="",customer_password="",Server_CityId="";
    String mytype="0";
    RelativeLayout rootview;
    private static final int REQUEST_WRITE_STORAGE = 112;
    SweetAlertDialog myDialog;
    PersianDatePicker datepicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        setContentView(R.layout.activity_register);
        datepicker=(PersianDatePicker)findViewById(R.id.datepicker);
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
        // Set Selected Change Listener
        stickySwitch = (StickySwitch) findViewById(R.id.sticky_switch);
        stickySwitch.setOnSelectedChangeListener(new StickySwitch.OnSelectedChangeListener() {
            @Override
            public void onSelectedChange(@NotNull StickySwitch.Direction direction, @NotNull String text) {
                if(stickySwitch.getDirection()==StickySwitch.Direction.LEFT)
                    mytype="0";
                else
                    mytype="1";
            }
        });
        activity=this;
        c=this;

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
        male=(RadioButton)findViewById(R.id.radioMale);
        female=(RadioButton)findViewById(R.id.radioFemale);

        txt_success_name=(TextView)findViewById(R.id.txt_success_name);
        textView6=(TextView)findViewById(R.id.textView6);

        btnreg=(Button)findViewById(R.id.btn_reg);
        mail=(EditText)findViewById(R.id.txtemail_reg);
        name=(EditText)findViewById(R.id.txtname_reg);
        //phone=(EditText)findViewById(R.id.txtmobile_reg);
        //pass=(EditText)findViewById(R.id.txtpass_reg);
        moarref=(EditText)findViewById(R.id.txt_moarref);

        //phone.setTypeface(type);
        //pass.setTypeface(type);

        phone=prefs.getString("customer_phone","");
        Server_CityId=prefs.getString("Server_CityId","");

        Server_MainUrl=prefs.getString("Server_MainUrl","");
        //new GetCityLists().execute("http://scad.ir/getcity.php");
        customer_password=prefs.getString("customer_password","");
        name.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(name, InputMethodManager.SHOW_IMPLICIT);
        btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*if(pass.getText().toString().length()<6)
                    Snackbar.make(phone,"رمز عبور حداقل باید 6 رقمی باشد",Snackbar.LENGTH_LONG).show();
                else if(phone.getText().toString().length()!=11)
                    Snackbar.make(phone,"تلفن همراه باید 11 رقمی باشد",Snackbar.LENGTH_LONG).show();
                else if(phone.getText().toString().charAt(0)!='0' || phone.getText().toString().charAt(1)!='9') {

                    Snackbar.make(phone, "لطفا تلفن همراه خود را به طور صحیح وارد کنید", Snackbar.LENGTH_LONG).show();
                }
                else */if(name.getText().toString().length()<5)
                    new SweetAlertDialog(c, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("هشدار")
                            .setContentText("لطفا نام خود را به طور کامل وارد نمایید")
                            .show();

                else
                {




                    new SweetAlertDialog(c, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("تایید اطلاعات")
                            .setContentText("آیا از اطلاعات وارد شده اطمینان دارید")
                            .setConfirmText("بله")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    myDialog=sDialog;
                                    String nm=name.getText().toString();
                                    String my_mail=mail.getText().toString();
                                    String date_str=datepicker.getDisplayPersianDate().getPersianShortDate().replace(" ","");
                                    String mymoarref=moarref.getText().toString();
                                    try
                                    {
                                        nm=URLEncoder.encode(name.getText().toString(), "utf-8");
                                        date_str=URLEncoder.encode(datepicker.getDisplayPersianDate().getPersianShortDate(), "utf-8");
                                        my_mail=URLEncoder.encode(mail.getText().toString(), "utf-8");
                                        mymoarref=URLEncoder.encode(mymoarref, "utf-8");
                                        if(mymoarref!=null && !mymoarref.equals(""))
                                            mymoarref=toEnglishNumber(mymoarref);
                                    }
                                    catch (UnsupportedEncodingException ex)
                                    {
                                        Log.v("Unsupported",ex.getMessage().toString());
                                    }
                                    phone=toEnglishNumber(phone);
                                    customer_password=toEnglishNumber(customer_password);
                                    new RegisterUser().execute(nm,phone,
                                            customer_password,my_mail,Server_MainUrl,mymoarref,mytype,date_str);

                                }
                            })
                            .show();


                }

            }
        });
    }
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
    }
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
    private String toEnglishNumber(String input)
    {
        try {
            String[] persian = new String[]{"۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹"};

            for (int j = 0; j < persian.length; j++) {
                input = input.replace(persian[j], j + "");
            }
            return input;
        }
        catch(NullPointerException ex)
        {
            return input;
        }
    }

    private class RegisterUser extends AsyncTask<String, Void, String> {

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
                Log.v("url",params[4]+"/home/AddUser?name="+params[0]+"&phone="+params[1]+"&password="+params[2]+"&mail="+params[3]+"&moarref="+params[5]+"&type="+params[6]+"&birthday="+params[7]+"&city_id="+Server_CityId);
                URL url2 = new URL(params[4]+"/home/AddUser?name="+params[0]+"&phone="+params[1]+"&password="+params[2]+"&mail="+params[3]+"&moarref="+params[5]+"&type="+params[6]+"&birthday="+params[7]+"&city_id="+Server_CityId);
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
                if(result.contains("true"))
                {

                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("customer_name", name.getText().toString());
                    //editor.putString("customer_phone", phone);
                    editor.putString("customer_mail", mail.getText().toString());
                    editor.putString("customer_password", customer_password);
                    //editor.putString("active", "false");
                    editor.putString("active", "true");

                    editor.apply();
                    editor.commit();

                    myDialog
                            .setTitleText("تبریک")
                            .setContentText("شما با موفقیت در اسگاد عضو شدید")
                            .setConfirmText("باشه")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    Intent i=new Intent(c,ServicesActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            })
                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);




                }
                else if(result.contains("moarref not exist"))
                {
                    new SweetAlertDialog(c, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("پیام")
                            .setContentText("کد معرف صحیح نمی باشد")
                            .show();

                }
                else
                {
                    new SweetAlertDialog(c, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("پیام")
                            .setContentText("شماره همراه وارد شده قبلا ثبت شده است")
                            .show();

                }

            }
            catch(Exception ex){}
            dialog.cancel();
        }
    }



    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
