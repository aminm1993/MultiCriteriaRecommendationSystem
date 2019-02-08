package ir.mohammadpour.app.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ir.mohammadpour.app.R;
import ir.mohammadpour.app.model.OrderDetails;
import ir.mohammadpour.app.model.PassengerOrder;
import ir.mohammadpour.app.ui.adapter.OrdersAdapter;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class OrderActivity extends AppCompatActivity {

    ListView listOrders;
    String phone="",url="";
    ProgressDialog simpleLoading;
    List<PassengerOrder> list;
    Activity a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        setContentView(R.layout.activity_order);

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
                OrderActivity.super.onBackPressed();
            }
        });
        simpleLoading = new ProgressDialog(this);
        simpleLoading.setMessage("در حال دریافت اطلاعات");
        simpleLoading.setCancelable(false);

        listOrders=(ListView)findViewById(R.id.listOrders);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            phone = extras.getString("phone");
            url= extras.getString("url");

            new GetAllOrders().execute();
        }


    }


    private class GetAllOrders extends AsyncTask<String, Void, String> {

        @Override
        protected  void onPreExecute()
        {
            list=new ArrayList<>();
            simpleLoading.show();
        }
        @Override
        protected String doInBackground(String... params) {
            try
            {

                Log.v("url",url+"/services/GetPassengerRequests?phone="+phone);
                URL url2 = new URL(url+"/services/GetPassengerRequests?phone="+phone);
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
            simpleLoading.cancel();
            if (result == null) {
                new SweetAlertDialog(a, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("پیام")
                        .setContentText("تاکنون سفارشی برای شما ثبت نشده است")
                        .show();
                return;

            }
            else
            {

                try {
                    JSONArray arr=new JSONArray(result);

                    for (int i=0;i<arr.length();i++)
                    {
                        PassengerOrder mymodel=new PassengerOrder();
                        mymodel.ID=arr.getJSONObject(i).getInt("ID");
                        mymodel.ServiceName=arr.getJSONObject(i).getString("ServiceName");
                        mymodel.Status=arr.getJSONObject(i).getInt("Status");
                        mymodel.Price=arr.getJSONObject(i).getLong("Price");
                        mymodel.Date1=arr.getJSONObject(i).getString("Date1");
                        mymodel.CreateDate=arr.getJSONObject(i).getString("CreatedDateStr");
                        mymodel.Services=new ArrayList<>();

                        try {
                            List<OrderDetails> subServices = new ArrayList<>();
                            JSONArray sub_arr = new JSONArray(arr.getJSONObject(i).getString("Services"));
                            for (int j = 0; j < sub_arr.length(); j++) {
                                OrderDetails mysubmodel = new OrderDetails();

                                mysubmodel.ID = sub_arr.getJSONObject(j).getInt("ID");
                                mysubmodel.Name = sub_arr.getJSONObject(j).getString("Name");
                                mysubmodel.Count = sub_arr.getJSONObject(j).getInt("Count");
                                mysubmodel.Price = sub_arr.getJSONObject(j).getLong("Price");
                                subServices.add(mysubmodel);
                            }
                            mymodel.Services = subServices;
                            list.add(mymodel);
                        }
                        catch (JSONException ex2){
                            list.add(mymodel);
                        }


                    }


                    if(list==null || list.size()==0) {
                        listOrders.setAdapter(null);
                        new SweetAlertDialog(a, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("پیام")
                                .setContentText("تاکنون سفارشی برای شما ثبت نشده است")
                                .show();
                    }
                    else {
                        OrdersAdapter adapter = new OrdersAdapter(a, list);
                        listOrders.setAdapter(adapter);
                    }
                }
                catch (JSONException ex){

                }


            }
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
