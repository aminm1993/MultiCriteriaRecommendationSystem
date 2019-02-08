package ir.mohammadpour.app.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.activeandroid.query.Select;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ir.mohammadpour.app.R;
import ir.mohammadpour.app.model.MaghsadTakhfifModel;
import ir.mohammadpour.app.model.SelectedAddressModel;
import ir.mohammadpour.app.ui.adapter.AddressAdapter;
import ir.mohammadpour.app.ui.adapter.TakhfifAdapter;

public class TakhfifLocationActivity extends AppCompatActivity {

    FloatingActionButton address_add;
    ListView ls;
    Activity a;
    String state="";
    List<MaghsadTakhfifModel> mItems;
    SharedPreferences prefs;
    String Server_MainUrl="";
    ProgressDialog simpleLoading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        setContentView(R.layout.activity_selected_location);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            //state = extras.getString("state");
        }
        simpleLoading = new ProgressDialog(this);
        simpleLoading.setMessage("در حال دریافت اطلاعات");
        simpleLoading.setCancelable(false);
        a=this;
        prefs= PreferenceManager.getDefaultSharedPreferences(this);
        Server_MainUrl = prefs.getString("Server_MainUrl", null);
        ls=(ListView)findViewById(R.id.list);
        address_add=(FloatingActionButton)findViewById(R.id.address_add);
        address_add.setVisibility(View.GONE);

        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                //getIntent().putExtra("res",mItems.get(position).lat+"~"+mItems.get(position).lng );
                //setResult(RESULT_OK, getIntent());
                //finish();
                //Log.v("test",position+"");

            }
        });
        RefreshList();




    }


    @Override
    public void onResume(){
        super.onResume();

        RefreshList();

    }


    public void RefreshList()
    {
           mItems=new ArrayList<>();
        //mItems = new Select().from(SelectedAddressModel.class).execute();
            new GetAllTakhfifs().execute();
    }
    private class GetAllTakhfifs extends AsyncTask<String, Void, String> {

        @Override
        protected  void onPreExecute()
        {
            mItems=new ArrayList<>();
            simpleLoading.show();
        }
        @Override
        protected String doInBackground(String... params) {
            try
            {

                URL url2 = new URL(Server_MainUrl+"/takhfif/GetAllMaghsadDiscount");
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
            if(result==null)
                return;

            try
            {
                JSONArray json=new JSONArray(result);
                for(int i=0;i<json.length();i++)
                {
                    MaghsadTakhfifModel model=new MaghsadTakhfifModel();
                    model.description=json.getJSONObject(i).getString("description");
                    model.ID=json.getJSONObject(i).getInt("ID");
                    model.uniqucode=json.getJSONObject(i).getString("uniqucode");
                    model.type=json.getJSONObject(i).getBoolean("type");
                    model.value=json.getJSONObject(i).getInt("value");
                    model.lat=json.getJSONObject(i).getString("lat");
                    model.lng=json.getJSONObject(i).getString("lng");

                    mItems.add(model);

                }
                if(mItems==null || mItems.size()==0)
                    ls.setAdapter(null);
                else {
                    TakhfifAdapter adapter = new TakhfifAdapter(a, mItems);
                    ls.setAdapter(adapter);
                }
            }
            catch (JSONException ex)
            {

            }

        }
    }



}
