package ir.mohammadpour.app.ui.activity;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import org.json.JSONException;
import org.json.JSONObject;

import ir.mohammadpour.app.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentActivity extends AppCompatActivity {

    ProgressDialog dialog;
    String driver_name="",driver_car_name=""
            ,transfer_id="",driver_pelak_number="",driver_phone="";
    Button send_comment;
    Context c;
    SharedPreferences prefs;
    RatingBar rate;
    EditText editText;
    TextView rate_text,textView8;

    Button btn_cm1,btn_cm2,btn_cm3,btn_cm4,btn_cm5,btn_cm6;
    int btn_number=0;
    String Server_MainUrl="";
    LinearLayout ll_map_item,ll_map_item2,ll_map_item3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        setContentView(R.layout.activity_comment);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Typeface type = Typeface.createFromAsset(this.getAssets(),"IRANSans.ttf");
        c=this;

        Server_MainUrl= prefs.getString("Server_MainUrl", null);
        ll_map_item=(LinearLayout)findViewById(R.id.ll_map_item);
        ll_map_item2=(LinearLayout)findViewById(R.id.ll_map_item2);
        ll_map_item3=(LinearLayout)findViewById(R.id.ll_map_item3);
        btn_cm1=(Button) findViewById(R.id.btn_cm1);
        btn_cm2=(Button) findViewById(R.id.btn_cm2);
        btn_cm3=(Button) findViewById(R.id.btn_cm3);
        btn_cm4=(Button) findViewById(R.id.btn_cm4);
        btn_cm5=(Button) findViewById(R.id.btn_cm5);
        btn_cm6=(Button) findViewById(R.id.btn_cm6);
        rate_text=(TextView)findViewById(R.id.rate_text);
        textView8=(TextView) findViewById(R.id.textView8);
        editText=(EditText)findViewById(R.id.editText);
        rate=(RatingBar)findViewById(R.id.ratingBar);
        dialog = new ProgressDialog(this);
        dialog.setMessage("در حال ارسال اطلاعات");
        dialog.setCancelable(false);

        send_comment=(Button)findViewById(R.id.send_comment);


        btn_cm1.setTypeface(type);
        btn_cm2.setTypeface(type);
        btn_cm3.setTypeface(type);
        btn_cm4.setTypeface(type);
        btn_cm5.setTypeface(type);
        btn_cm6.setTypeface(type);
        rate_text.setTypeface(type);
        editText.setTypeface(type);
        textView8.setTypeface(type);

        EnableOrDisableRateComments(false);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            //driver_avatar_path = extras.getString("driver_avatar_path");
            driver_name = extras.getString("driver_name");
            driver_car_name= extras.getString("driver_car_name");
            driver_pelak_number=extras.getString("driver_pelak_number");
            driver_phone=extras.getString("driver_phone");
            transfer_id=extras.getString("transfer_id");
        }
       /* final ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(Server_MainUrl+"/profiles/"+driver_avatar_path, user_profile_photo, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted() {

                    }

                    @Override
                    public void onLoadingFailed(FailReason failReason) {
                        // arrayKala.get(position).imgUrl="http://taxnet.ir/images/shopdefault.png";
                        //imageLoader.displayImage("http://taxnet.ir/images/shopdefault.png",relativeLayout);
                    }

                    @Override
                    public void onLoadingComplete(Bitmap bitmap) {
                        //Drawable dr = new BitmapDrawable(bitmap);

                        // relativeLayout.setBackground(dr);
                        //Blurry.with(c).radius(25).sampling(2).onto(relativeLayout);

                    }

                    @Override
                    public void onLoadingCancelled() {

                    }
                }
        );
        */

        //txtName.setText("نام راننده : "+driver_name);
        //txtPelak.setText("شماره پلاک : "+driver_pelak_number);
        //mMap.getUiSettings().setAllGesturesEnabled(false);

        send_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String myphone = prefs.getString("customer_phone", null);

                String mypassword = prefs.getString("customer_password", null);
                if(rate.getRating()<4 && btn_number==0)
                {
                    Toast.makeText(c,"لطفا دلیل نارضایتی خود را انتخاب نمایید",Toast.LENGTH_LONG).show();

                }
                else if(rate.getRating()<4)

                    new SendComment().execute(driver_phone,transfer_id,myphone,mypassword,rate.getRating()+"",editText.getText().toString(),Server_MainUrl,btn_number+"");
                else if(rate.getRating()>=4)
                   new SendComment().execute(driver_phone,transfer_id,myphone,mypassword,rate.getRating()+"",editText.getText().toString(),Server_MainUrl,"");
            }
        });

        rate.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                btn_number=0;
                if(rating<=1)
                {
                    rate_text.setText("کاملا ناراضی");
                    rate.setRating(1);

                    EnableOrDisableRateComments(true);
                }
                else if(rating>1 && rating <=2)
                {
                    rate_text.setText("خیلی ناراضی");
                    rate.setRating(2);

                    EnableOrDisableRateComments(true);
                }
                else if(rating>2 && rating <=3)
                {
                    rate_text.setText("ناراضی");
                    rate.setRating(3);

                    EnableOrDisableRateComments(true);
                }
                else if(rating>3 && rating <=4)
                {
                    rate_text.setText("راضی");
                    rate.setRating(4);
                    EnableOrDisableRateComments(false);
                }
                else if(rating>4 && rating <=5)
                {
                    rate_text.setText("کاملا راضی");
                    rate.setRating(5);

                    EnableOrDisableRateComments(false);
                }
            }
        });

        btn_cm1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btn_number=1;

                btn_cm1.setBackgroundColor(Color.parseColor("#00b1c7"));
                btn_cm1.setTextColor(Color.parseColor("#ffffff"));

                btn_cm2.setBackgroundResource(R.drawable.blue_border_button);
                btn_cm2.setTextColor(Color.parseColor("#00b1c7"));

                btn_cm3.setBackgroundResource(R.drawable.blue_border_button);
                btn_cm3.setTextColor(Color.parseColor("#00b1c7"));

                btn_cm4.setBackgroundResource(R.drawable.blue_border_button);
                btn_cm4.setTextColor(Color.parseColor("#00b1c7"));

                btn_cm5.setBackgroundResource(R.drawable.blue_border_button);
                btn_cm5.setTextColor(Color.parseColor("#00b1c7"));

                btn_cm6.setBackgroundResource(R.drawable.blue_border_button);
                btn_cm6.setTextColor(Color.parseColor("#00b1c7"));


            }
        });

        btn_cm2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btn_number=2;

                btn_cm2.setBackgroundColor(Color.parseColor("#00b1c7"));
                btn_cm2.setTextColor(Color.parseColor("#ffffff"));

                btn_cm1.setBackgroundResource(R.drawable.blue_border_button);
                btn_cm1.setTextColor(Color.parseColor("#00b1c7"));

                btn_cm3.setBackgroundResource(R.drawable.blue_border_button);
                btn_cm3.setTextColor(Color.parseColor("#00b1c7"));

                btn_cm4.setBackgroundResource(R.drawable.blue_border_button);
                btn_cm4.setTextColor(Color.parseColor("#00b1c7"));

                btn_cm5.setBackgroundResource(R.drawable.blue_border_button);
                btn_cm5.setTextColor(Color.parseColor("#00b1c7"));

                btn_cm6.setBackgroundResource(R.drawable.blue_border_button);
                btn_cm6.setTextColor(Color.parseColor("#00b1c7"));


            }
        });
        btn_cm3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_number=3;

                btn_cm3.setBackgroundColor(Color.parseColor("#00b1c7"));
                btn_cm3.setTextColor(Color.parseColor("#ffffff"));

                btn_cm2.setBackgroundResource(R.drawable.blue_border_button);
                btn_cm2.setTextColor(Color.parseColor("#00b1c7"));

                btn_cm1.setBackgroundResource(R.drawable.blue_border_button);
                btn_cm1.setTextColor(Color.parseColor("#00b1c7"));

                btn_cm4.setBackgroundResource(R.drawable.blue_border_button);
                btn_cm4.setTextColor(Color.parseColor("#00b1c7"));

                btn_cm5.setBackgroundResource(R.drawable.blue_border_button);
                btn_cm5.setTextColor(Color.parseColor("#00b1c7"));

                btn_cm6.setBackgroundResource(R.drawable.blue_border_button);
                btn_cm6.setTextColor(Color.parseColor("#00b1c7"));


            }
        });
        btn_cm4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_number=4;

                btn_cm4.setBackgroundColor(Color.parseColor("#00b1c7"));
                btn_cm4.setTextColor(Color.parseColor("#ffffff"));

                btn_cm2.setBackgroundResource(R.drawable.blue_border_button);
                btn_cm2.setTextColor(Color.parseColor("#00b1c7"));

                btn_cm3.setBackgroundResource(R.drawable.blue_border_button);
                btn_cm3.setTextColor(Color.parseColor("#00b1c7"));

                btn_cm1.setBackgroundResource(R.drawable.blue_border_button);
                btn_cm1.setTextColor(Color.parseColor("#00b1c7"));

                btn_cm5.setBackgroundResource(R.drawable.blue_border_button);
                btn_cm5.setTextColor(Color.parseColor("#00b1c7"));

                btn_cm6.setBackgroundResource(R.drawable.blue_border_button);
                btn_cm6.setTextColor(Color.parseColor("#00b1c7"));


            }
        });
        btn_cm5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_number=5;

                btn_cm5.setBackgroundColor(Color.parseColor("#00b1c7"));
                btn_cm5.setTextColor(Color.parseColor("#ffffff"));

                btn_cm2.setBackgroundResource(R.drawable.blue_border_button);
                btn_cm2.setTextColor(Color.parseColor("#00b1c7"));

                btn_cm3.setBackgroundResource(R.drawable.blue_border_button);
                btn_cm3.setTextColor(Color.parseColor("#00b1c7"));

                btn_cm4.setBackgroundResource(R.drawable.blue_border_button);
                btn_cm4.setTextColor(Color.parseColor("#00b1c7"));

                btn_cm1.setBackgroundResource(R.drawable.blue_border_button);
                btn_cm1.setTextColor(Color.parseColor("#00b1c7"));

                btn_cm6.setBackgroundResource(R.drawable.blue_border_button);
                btn_cm6.setTextColor(Color.parseColor("#00b1c7"));


            }
        });
        btn_cm6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_number=6;

                btn_cm6.setBackgroundColor(Color.parseColor("#00b1c7"));
                btn_cm6.setTextColor(Color.parseColor("#ffffff"));

                btn_cm2.setBackgroundResource(R.drawable.blue_border_button);
                btn_cm2.setTextColor(Color.parseColor("#00b1c7"));

                btn_cm3.setBackgroundResource(R.drawable.blue_border_button);
                btn_cm3.setTextColor(Color.parseColor("#00b1c7"));

                btn_cm4.setBackgroundResource(R.drawable.blue_border_button);
                btn_cm4.setTextColor(Color.parseColor("#00b1c7"));

                btn_cm5.setBackgroundResource(R.drawable.blue_border_button);
                btn_cm5.setTextColor(Color.parseColor("#00b1c7"));

                btn_cm1.setBackgroundResource(R.drawable.blue_border_button);
                btn_cm1.setTextColor(Color.parseColor("#00b1c7"));


            }
        });
    }



    void EnableOrDisableRateComments(boolean enable)
    {
        if(enable)
        {
            btn_cm1.setEnabled(true);
            btn_cm2.setEnabled(true);
            btn_cm3.setEnabled(true);
            btn_cm4.setEnabled(true);
            btn_cm5.setEnabled(true);
            btn_cm6.setEnabled(true);

            btn_cm1.setBackgroundResource(R.drawable.blue_border_button);
            btn_cm1.setTextColor(Color.parseColor("#00b1c7"));

            btn_cm2.setBackgroundResource(R.drawable.blue_border_button);
            btn_cm2.setTextColor(Color.parseColor("#00b1c7"));

            btn_cm3.setBackgroundResource(R.drawable.blue_border_button);
            btn_cm3.setTextColor(Color.parseColor("#00b1c7"));

            btn_cm4.setBackgroundResource(R.drawable.blue_border_button);
            btn_cm4.setTextColor(Color.parseColor("#00b1c7"));

            btn_cm5.setBackgroundResource(R.drawable.blue_border_button);
            btn_cm5.setTextColor(Color.parseColor("#00b1c7"));

            btn_cm6.setBackgroundResource(R.drawable.blue_border_button);
            btn_cm6.setTextColor(Color.parseColor("#00b1c7"));


            ll_map_item.setVisibility(View.VISIBLE);
            ll_map_item2.setVisibility(View.VISIBLE);
            ll_map_item3.setVisibility(View.VISIBLE);
        }
        else
        {
            ll_map_item.setVisibility(View.GONE);
            ll_map_item2.setVisibility(View.GONE);
            ll_map_item3.setVisibility(View.GONE);

            btn_cm1.setBackgroundResource(R.drawable.disable_border_button);
            btn_cm1.setTextColor(Color.parseColor("#9D9D9D"));

            btn_cm2.setBackgroundResource(R.drawable.disable_border_button);
            btn_cm2.setTextColor(Color.parseColor("#9D9D9D"));

            btn_cm3.setBackgroundResource(R.drawable.disable_border_button);
            btn_cm3.setTextColor(Color.parseColor("#9D9D9D"));

            btn_cm4.setBackgroundResource(R.drawable.disable_border_button);
            btn_cm4.setTextColor(Color.parseColor("#9D9D9D"));

            btn_cm5.setBackgroundResource(R.drawable.disable_border_button);
            btn_cm5.setTextColor(Color.parseColor("#9D9D9D"));

            btn_cm6.setBackgroundResource(R.drawable.disable_border_button);
            btn_cm6.setTextColor(Color.parseColor("#9D9D9D"));


            btn_cm1.setEnabled(false);
            btn_cm2.setEnabled(false);
            btn_cm3.setEnabled(false);
            btn_cm4.setEnabled(false);
            btn_cm5.setEnabled(false);
            btn_cm6.setEnabled(false);
        }
    }

    private class SendComment extends AsyncTask<String, Void, String> {

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
                String myUrl="";
                if(params[7]!=null && !params[7].equals(""))
                 myUrl=params[6]+"/home/AddComment?driver_phone="+params[0]
                        +"&transferid="+params[1]
                        +"&passenger_phone="+params[2]
                        +"&passenger_password="+params[3]
                        +"&rate="+params[4]
                        +"&comment="+params[5].replace(" ","%20")+"&discontent_id="+params[7];

                else
                    myUrl=params[6]+"/home/AddComment?driver_phone="+params[0]
                            +"&transferid="+params[1]
                            +"&passenger_phone="+params[2]
                            +"&passenger_password="+params[3]
                            +"&rate="+params[4]
                            +"&comment="+params[5].replace(" ","%20");

                Log.v("url",myUrl);
                URL url2 = new URL(myUrl);
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

            if(result.trim().equals("true"))
            {
                Toast.makeText(c,"نظر شما با موفقیت ثبت شد",Toast.LENGTH_SHORT).show();
                /*Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
                */


                String _phone = prefs.getString("customer_phone", "");
                String Server_MainUrl = prefs.getString("Server_MainUrl", "");
                new GetStatus().execute(_phone,Server_MainUrl);


            }
            else if(result.contains("false"))
            {
                Toast.makeText(c,"مشکل در ثبت نظر",Toast.LENGTH_SHORT).show();
                /*Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
                */
                //finish();
                //android.os.Process.killProcess(android.os.Process.myPid());
            }

            dialog.cancel();
        }
    }

    @Override
    public void onBackPressed() {

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
                AlertDialog.Builder builder = new AlertDialog.Builder(c);
                builder.setTitle("");
                builder.setMessage("مشکل در برقراری ارتباط با سرور اسگاد");

                String positiveText = "سعی مجدد";
                builder.setPositiveButton(positiveText,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // positive button logic

                                String _phone = prefs.getString("customer_phone", "");
                                String Server_MainUrl = prefs.getString("Server_MainUrl", "");
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

                if(result.contains("false:"))
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
                            startService(new Intent(c, TransferService.class));
                        }


                    }



                    finish();
                    android.os.Process.killProcess(android.os.Process.myPid());

                }
            }
            catch(JSONException ex){

            }
            //dialog.cancel();
        }
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
