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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ir.mohammadpour.app.R;
import ir.mohammadpour.app.model.Transaction;
import ir.mohammadpour.app.ui.adapter.TransactionAdapter;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TransactionsActivity extends AppCompatActivity {

    ProgressDialog simpleLoading;
    List<Transaction> list;
    ListView lv;
    Activity a;
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        prefs= PreferenceManager.getDefaultSharedPreferences(this);
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
                TransactionsActivity.super.onBackPressed();
            }
        });
        simpleLoading = new ProgressDialog(this);
        simpleLoading.setMessage("در حال دریافت اطلاعات تراکنش ها");
        simpleLoading.setCancelable(false);
        lv=(ListView) findViewById(R.id.lv);
        String Server_MainUrl = prefs.getString("Server_MainUrl", null);
        String _phone = prefs.getString("customer_phone", null);
        new GetTransactions().execute(Server_MainUrl,_phone);
    }

    private class GetTransactions extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            simpleLoading.show();
            list =new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... arg0) {

            try {
                //URL url = new URL("http://taxnet.ir/api/category/"+categoryID+"/shop/"+shop_id);
                String myurl=arg0[0]+"/transaction/GetAllPassengerTransactions?phone="+arg0[1];
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

                    Type listType = new TypeToken<ArrayList<Transaction>>(){}.getType();

                    list = new Gson().fromJson(result, listType);

                    if(list!=null && list.size()>0) {
                        TransactionAdapter adapter = new TransactionAdapter(a, list);
                        lv.setAdapter(adapter);
                    }
                }
                catch(Exception ex){
                    Log.v("error",ex.getMessage());
                }
            }


        }
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
