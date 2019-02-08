package ir.mohammadpour.app.ui.activity;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import ir.mohammadpour.app.R;
import ir.mohammadpour.app.model.ServiceModel;
import ir.mohammadpour.app.model.SubService;
import ir.mohammadpour.app.ui.adapter.ServicesAdapter;
import ir.mohammadpour.app.ui.adapter.SubServicesAdapter;
import ir.mohammadpour.app.ui.adapter.SubServicesForDialogAdapter;
import ir.mohammadpour.app.ui.widget.GetTavaghofPrice;

public class SubServiceActivity extends AppCompatActivity {

    ListView lv;
    int index=-1;
    static TextView mablagh,mablagh2;
    public static int last=0;
    ProgressDialog simpleLoading;
    SharedPreferences prefs;
    Button btn_sabt;
    String Server_MainUrl="",phone="";
    Activity ac;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_service);

        prefs= PreferenceManager.getDefaultSharedPreferences(this);
        Server_MainUrl = prefs.getString("Server_MainUrl", null);
        phone= prefs.getString("customer_phone", null);
        simpleLoading = new ProgressDialog(this);
        simpleLoading.setMessage("در حال ارسال اطلاعات");
        simpleLoading.setCancelable(false);
        ac=this;
        final Typeface myfont2 = Typeface.createFromAsset(getAssets(), "Regular.ttf");
        final Typeface myfont = Typeface.createFromAsset(getAssets(), "BYekan.ttf");
        lv=(ListView)findViewById(R.id.lv);
        btn_sabt=(Button) findViewById(R.id.btn_sabt);
        mablagh=(TextView)findViewById(R.id.mablagh) ;
        mablagh.setTypeface(myfont2);
        btn_sabt.setTypeface(myfont);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            index = extras.getInt("index");


            if(ServicesActivity.models.get(index).Details==null || ServicesActivity.models.get(index).Details.size()==0)
                lv.setAdapter(null);
            else {

                SubServicesAdapter adapter = new SubServicesAdapter(this, ServicesActivity.models.get(index).Details, index);
                lv.setAdapter(adapter);
            }
        }
        else
            finish();



        btn_sabt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(last==0) {
                    Toast.makeText(ac,"سفارشی ثبت نشده است",Toast.LENGTH_SHORT).show();
                    return;
                }
                final AlertDialog.Builder mydialogBuilder = new AlertDialog.Builder(ac);
                //dialogBuilder.setCancelable(false);

                LayoutInflater inflater = ac.getLayoutInflater();
                View mydialog = inflater.inflate(R.layout.service_custom_dialog, null);
                mydialogBuilder.setView(mydialog);
                Button mybutton3 = (Button) mydialog.findViewById(R.id.button3);
                TextView title = (TextView) mydialog.findViewById(R.id.title);
                ListView list = (ListView) mydialog.findViewById(R.id.list_service);
                mablagh2= (TextView) mydialog.findViewById(R.id.mablagh2);
                SubServicesForDialogAdapter adapter = new SubServicesForDialogAdapter(ac, arr,index,arr_count);
                list.setAdapter(adapter);
                mybutton3.setTypeface(myfont);
                title.setTypeface(myfont);
                final AlertDialog myalertDialog = mydialogBuilder.create();
                mablagh2.setTypeface(myfont2);
                mablagh2.setText(last+" ریال");
                mybutton3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(last!=0) {
                            String requests="";
                            for(int i=0;i<arr.size();i++)
                            {
                                if(i!=arr.size()-1)
                                    requests+=arr.get(i).ID+"~"+arr_count.get(i).toString()+"_";
                                else
                                    requests+=arr.get(i).ID+"~"+arr_count.get(i).toString();
                            }
                            new SendRequest().execute(requests);
                        }
                    }
                });
                myalertDialog.show();
            }
        });



    }



    public static List<SubService> arr=new ArrayList<>();
    public static List<Integer> arr_count=new ArrayList<>();
    public static void AddToBasket(int index,int index2,int count)
    {
        boolean has=false;
        for(int i=0;i<arr.size();i++)
        {
            if(arr.get(i).ID== ServicesActivity.models.get(index).Details.get(index2).ID) {
                arr_count.set(i, count);
                has=true;
            }
        }
        if(!has)
        {
            arr.add(ServicesActivity.models.get(index).Details.get(index2));
            arr_count.add(count);
        }

        showvalue();
    }

    public  static void DeleteBasket(int index,int index2)
    {
        for(int i=0;i<arr.size();i++)
        {
            if(arr.get(i).ID== ServicesActivity.models.get(index).Details.get(index2).ID) {
                arr.remove(i);
                arr_count.remove(i);
            }
        }


        showvalue();
    }

    static void showvalue()
    {
        int val=0;
        for(int i=0;i<arr.size();i++)
        {
            val+=arr.get(i).Price*arr_count.get(i);
        }

        startCountAnimation(val);
    }
    public static void startCountAnimation(int newval) {
        ValueAnimator animator = new ValueAnimator();
        int local_last=last;
        last=newval;
        animator.setObjectValues(local_last, newval);
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                    mablagh.setText((int) animation.getAnimatedValue() + "  ریال");

                if(mablagh2!=null)
                    mablagh2.setText((int) animation.getAnimatedValue() + "  ریال");
            }
        });
        animator.start();
    }





    private class SendRequest extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            simpleLoading.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                Log.v("url",Server_MainUrl + "/services/AddRequest?phone="+phone+"&requests=" + params[0]);
                URL url2 = new URL(Server_MainUrl + "/services/AddRequest?phone="+phone+"&requests=" + params[0]);
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
                    if(result.contains("true"))
                    {
                        Toast.makeText(ac,"سفارش شما با موفقیت ثبت شد ، جهت تایید سفارش با شما تماس گرفته خواهد شد",Toast.LENGTH_LONG).show();
                        finish();
                    }
                else
                    {
                        Toast.makeText(ac,result,Toast.LENGTH_LONG).show();
                        finish();
                    }



            }
        }
    }
}
