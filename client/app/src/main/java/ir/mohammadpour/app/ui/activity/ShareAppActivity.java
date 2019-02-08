package ir.mohammadpour.app.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import ir.mohammadpour.app.R;
import ir.mohammadpour.app.ui.adapter.ShareIntentListAdapter;
import ir.mohammadpour.app.ui.util.Convert;
import ir.mohammadpour.app.ui.util.Util;
import ir.mohammadpour.app.ui.widget.slider.Animations.DescriptionAnimation;
import ir.mohammadpour.app.ui.widget.slider.SliderLayout;
import ir.mohammadpour.app.ui.widget.slider.SliderTypes.BaseSliderView;
import ir.mohammadpour.app.ui.widget.slider.SliderTypes.TextSliderView;

public class ShareAppActivity extends AppCompatActivity {

    TextView tv,tv2,tv3;
    GridView lv;
    SharedPreferences prefs;
    Activity ac;
    String text="";
    String Server_MainUrl = "";
    ProgressDialog simpleLoading;
    String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_app);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        simpleLoading = new ProgressDialog(this);
        simpleLoading.setMessage("در حال دریافت اطلاعات");
        simpleLoading.setCancelable(false);
        Server_MainUrl = prefs.getString("Server_MainUrl", null);
        ac=this;
        tv=(TextView)findViewById(R.id.tv);
        tv2=(TextView)findViewById(R.id.tv2);
        tv3=(TextView) findViewById(R.id.tv3);
        lv=(GridView)findViewById(R.id.lv);
        final Typeface face = Typeface.createFromAsset(getAssets(),
                "IRANSans.ttf");

        tv.setTypeface(face);
        tv2.setTypeface(face);
        tv3.setTypeface(face);
        final String moarref_unique_id = prefs.getString("moarref_unique_id", null);
        tv2.setText("کد دعوت شما "+moarref_unique_id);



       Bundle extras=  getIntent().getExtras();
       if(extras!=null)
       {
           extras.getString("key");
           text="با دریافت و نصب اپلیکیشن اسگاد و معرفی آن به دوستانتان باعث پیشرفت شهر خود شوید و شارژ رایگان دریافت نمایید " +System.getProperty("line.separator")+" کد دعوت من : "+moarref_unique_id+System.getProperty("line.separator")+"  http://cafebazaar.ir/app/ir.mohammadpour.app/?l=fa";
           tv.setText("با دریافت و نصب اپلیکیشن اسگاد و معرفی آن به دوستانتان باعث پیشرفت شهر خود شوید و شارژ رایگان دریافت نمایید ");


          phone = prefs.getString("customer_phone", null);
          new GetDiscountStatus().execute(phone);

       }
       else
       {
           text="با دریافت و نصب اپلیکیشن اسگاد و ثبت اولین سفر به من اعتبار هدیه تعلق میگیره. با اسگاد میتونی خیلی راحت از هر جا که هستی درخواست خودرو بدی" +System.getProperty("line.separator")+" کد دعوت من : "+moarref_unique_id+System.getProperty("line.separator")+"  http://cafebazaar.ir/app/ir.mohammadpour.app/?l=fa";
       }

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);

        // what type of data needs to be send by sharing
        sharingIntent.setType("text/plain");

        // package names
        PackageManager pm = getPackageManager();

        // list package
        List<ResolveInfo> activityList = pm.queryIntentActivities(sharingIntent, 0);

        final ShareIntentListAdapter objShareIntentListAdapter = new ShareIntentListAdapter(ShareAppActivity.this,activityList.toArray());

        // Create alert dialog box
        //AlertDialog.Builder builder = new AlertDialog.Builder(c);
        //builder.setTitle("Share via");

        lv.setAdapter(objShareIntentListAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ResolveInfo info = (ResolveInfo) objShareIntentListAdapter.getItem(position);
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setClassName(info.activityInfo.packageName, info.activityInfo.name);
                intent.setType("text/plain");
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, text);
                intent.putExtra(android.content.Intent.EXTRA_TEXT, text);
                intent.putExtra(Intent.EXTRA_TITLE,  text);
                (ac).startActivity(intent);
            }
        });



    }



    private class GetDiscountStatus extends AsyncTask<String, Void, String> {

        @Override
        protected  void onPreExecute()
        {
            simpleLoading.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {


                Log.v("url",Server_MainUrl+"/passenger/GetDiscountType?phone="+params[0]);
                URL url2 = new URL(Server_MainUrl+"/passenger/GetDiscountType?phone="+params[0]);
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
            simpleLoading.cancel();
            if (result == null)
                return;
            else {

                try {
                    JSONObject obj = Convert.StringToJsonObject(result);
                    int PassengerType= obj.getInt("PassengerType");
                    Boolean Type1= obj.getBoolean("Type1");
                    Boolean Type2= obj.getBoolean("Type2");
                    Boolean Type3= obj.getBoolean("Type3");

                    if (PassengerType==0 && (Type1 || Type2 || Type3)) {
                        final Typeface face = Typeface.createFromAsset(getAssets(),
                                "IRANSans.ttf");
                        final Typeface face2 = Typeface.createFromAsset(getAssets(),
                                "SOGAND.ttf");

                        final Typeface face3 = Typeface.createFromAsset(getAssets(),
                                "Regular.ttf");
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ac);
                        LayoutInflater inflater = getLayoutInflater();
                        final View dialog = inflater.inflate(R.layout.discount_type_custom_dialog, null);
                        dialogBuilder.setView(dialog);

                        final RadioButton radio1 = (RadioButton) dialog.findViewById(R.id.radio1);
                        final RadioButton radio2 = (RadioButton) dialog.findViewById(R.id.radio2);
                        final RadioButton radio3 = (RadioButton) dialog.findViewById(R.id.radio3);

                        if(!Type1)
                            radio1.setVisibility(View.GONE);

                        if(!Type2)
                            radio2.setVisibility(View.GONE);

                        if(!Type3)
                            radio3.setVisibility(View.GONE);

                        Button button3 = (Button) dialog.findViewById(R.id.button3);
                        TextView title = (TextView) dialog.findViewById(R.id.title);
                        title.setTypeface(face2);
                        button3.setTypeface(face);
                        radio1.setTypeface(face);
                        radio2.setTypeface(face);
                        radio3.setTypeface(face);

                        final AlertDialog alertDialog = dialogBuilder.create();
                        button3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                //new ChangePassword().execute(ShopId,oldpass.getText().toString(),newpass.getText().toString());
                           /* Intent i = new Intent(v.getContext(), IncreaseEtebarActivity.class);
                            i.putExtra("phone", phone);
                            i.putExtra("value", oldpass.getText().toString());
                            startActivity(i);
                            */

                                String radiotype = "";
                                if (radio1.isChecked())
                                    radiotype = "1";
                                else if (radio2.isChecked())
                                    radiotype = "2";
                                else
                                    radiotype = "3";
                                new SetDiscountStatus().execute(phone, radiotype);


                                alertDialog.dismiss();
                            }
                        });

                        alertDialog.show();

                    }

                }
                catch (JSONException ex){}
            }

        }
    }





    private class SetDiscountStatus extends AsyncTask<String, Void, String> {

        @Override
        protected  void onPreExecute()
        {
            simpleLoading.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {


                Log.v("url",Server_MainUrl+"/passenger/SetDiscountType?phone="+params[0]+"&type="+params[1]);
                URL url2 = new URL(Server_MainUrl+"/passenger/SetDiscountType?phone="+params[0]+"&type="+params[1]);
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
            simpleLoading.cancel();
            if (result == null)
                return;
            else {

                Toast.makeText(ac,"عملیات با موفقیت انجام شد",Toast.LENGTH_SHORT).show();

            }
        }
    }

}
