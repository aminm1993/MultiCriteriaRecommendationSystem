package ir.mohammadpour.app.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ir.mohammadpour.app.R;
import ir.mohammadpour.app.model.ScheduleModel;
import ir.mohammadpour.app.ui.adapter.ScheduleListAdapter;

public class TimeListActivity extends AppCompatActivity {

    ListView lvTimes;
    ProgressDialog dialog;
    Context c;
    String url="";
    Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        setContentView(R.layout.activity_time_list);
        c=this;
        activity=this;
        lvTimes=(ListView)findViewById(R.id.lvTimes);

        dialog = new ProgressDialog(this);
        dialog.setMessage("در حال ارسال اطلاعات");
        dialog.setCancelable(false);
        Bundle extras=getIntent().getExtras();
        if(extras!=null)
        {
            url=extras.getString("url");
            new GetScheduleListData().execute(url);
        }
    }




    private class GetScheduleListData extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            dialog.show();
        }

        @Override
        protected String doInBackground(String... arg0) {

            try {
                //URL url = new URL("http://taxnet.ir/api/category/"+categoryID+"/shop/"+shop_id);
                Log.v("url", arg0[0]);
                URL url = new URL(arg0[0]);
                // Read all the text returned by the server
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

                String builder = "";
                for (String line = null; (line = in.readLine()) != null; ) {
                    builder += (line + "\n");
                }
                in.close();
                builder = builder.replace("&quot;", "\"");
                return builder;

            } catch (Exception e) {

                return null;
            }



        }

        @Override
        protected void onPostExecute(String result) {

            dialog.dismiss();
            if(result==null) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(c);
                dialog.setMessage("مشکل در برقراری ارتباط");
                dialog.setPositiveButton("سعی مجدد", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        // TODO Auto-generated method stub
                        new GetScheduleListData().execute(url);
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

            else
            {
                try {
                    JSONArray json=new JSONArray(result);
                    List<ScheduleModel> sch_data=new ArrayList<>();

                    for (int i=0;i<json.length();i++)
                    {
                        ScheduleModel item1=new ScheduleModel();
                        item1.DateStr=json.getJSONObject(i).getString("DateStr");
                        JSONArray subjson=new JSONArray(json.getJSONObject(i).getString("Dates"));
                        for(int j=0;j<subjson.length();j++)
                        {
                            item1.AddToList(subjson.getJSONObject(j).getString("Time"),subjson.getJSONObject(j).getString("Date"));
                        }
                        sch_data.add(item1);
                    }


                    ScheduleListAdapter myadapter=new ScheduleListAdapter(activity,sch_data);
                    lvTimes.setAdapter(myadapter);
                }
                catch (JSONException ex)
                {

                }
            }
        }
    }
}
