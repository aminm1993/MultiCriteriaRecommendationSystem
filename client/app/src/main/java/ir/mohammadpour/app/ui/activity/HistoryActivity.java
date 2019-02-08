package ir.mohammadpour.app.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ir.mohammadpour.app.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    ListView lv;
    List<Sabeghe> list;
    SharedPreferences prefs;
    Activity a;
    ProgressDialog simpleLoading;
    String Server_MainUrl="";
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        setContentView(R.layout.activity_history);
        a=this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayShowHomeEnabled(false); // show or hide the default home button
        // ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false);

        //ab.show();

        final ImageView back = (ImageView) toolbar.findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HistoryActivity.super.onBackPressed();
            }
        });
        bundle = getIntent().getExtras();
        simpleLoading = new ProgressDialog(this);
        simpleLoading.setMessage("در حال دریافت اطلاعات");
        simpleLoading.setCancelable(false);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String phone = prefs.getString("customer_phone", null);
        Server_MainUrl= prefs.getString("Server_MainUrl", null);
        lv=(ListView)findViewById(R.id.lv);
        new GetCourses().execute(phone);
    }


    private class GetCourses extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            simpleLoading.show();
            list =new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... arg0) {

            try {
                //URL url = new URL("http://taxnet.ir/api/category/"+categoryID+"/shop/"+shop_id);
                String myurl=Server_MainUrl+"/home/GetTrasfers?phone="+arg0[0]+"&type=0";
                Log.v("url",myurl);
                URL url = new URL(myurl);
                // Read all the text returned by the server
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

                String builder = "";
                for (String line = null; (line = in.readLine()) != null; ) {
                    builder += (line + "\n");
                }
                in.close();
                builder = builder.replace("&quot;", "\"");
                return  builder;

            } catch (Exception e) {

            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            simpleLoading.cancel();
            if (result !=null)
            {
                list=new ArrayList<>();
                try{

                    Type listType = new TypeToken<ArrayList<Sabeghe>>(){}.getType();

                    List<Sabeghe> list = new Gson().fromJson(result, listType);

                    if(list!=null) {
                        SabegheAdapter adapter = new SabegheAdapter(a, list, bundle);
                        lv.setAdapter(adapter);
                    }
                    else
                    {
                        new SweetAlertDialog(a, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("پیام")
                                .setContentText("تاکنون سفری برای شما ثبت نشده است")
                                .show();
                    }
/*

                    JSONArray arr=new JSONArray(result);

                    for(int i=0;i<arr.length();i++)
                    {
                        Sabeghe x=new Sabeghe();

                        x.address= arr.getJSONObject(i).getString("LocationMabda");
                        x.address_maghsad= arr.getJSONObject(i).getString("LocationMaghsad");
                        x.price= arr.getJSONObject(i).getInt("Price")+"";
                        x.tarikh=arr.getJSONObject(i).getString("Created2");
                        x.lat=arr.getJSONObject(i).getString("Customer_lat");
                        x.lng=arr.getJSONObject(i).getString("Customer_lng");
                        x.dest_lat=arr.getJSONObject(i).getString("Dest_lat");
                        x.dest_lng=arr.getJSONObject(i).getString("Dest_lng");


                        list.add(x);
                    }
                */
                }
               // catch(JSONException ex){}
                catch(Exception ex){}
            }
            else
            {
                new SweetAlertDialog(a, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("پیام")
                        .setContentText("مشکل در ارتباط با سرور")
                        .show();
            }


            // new GetBrandsLists().execute();
            //Toast.makeText(activity,result,Toast.LENGTH_LONG).show();

        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
