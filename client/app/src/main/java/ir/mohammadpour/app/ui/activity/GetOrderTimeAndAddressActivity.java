package ir.mohammadpour.app.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ir.mohammadpour.app.R;
import ir.mohammadpour.app.model.ScheduleModel;
import ir.mohammadpour.app.ui.adapter.ScheduleListAdapter;
import ir.mohammadpour.app.ui.adapter.SubScheduleListAdapter;
import ir.mohammadpour.app.ui.adapter.SubScheduleListAdapter2;
import ir.mohammadpour.app.ui.util.CalcDistance;
import ir.mohammadpour.app.ui.util.Consts;
import ir.mohammadpour.app.ui.util.ShamsiCalleder;
import ir.mohammadpour.app.ui.util.ShowMsg;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class GetOrderTimeAndAddressActivity extends AppCompatActivity {
    public Calendar addDays(Date baseDate, int daysToAdd) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(baseDate);
        calendar.add(Calendar.DAY_OF_YEAR, daysToAdd);
        return calendar;
    }
    TextView tatDiscount;
    ExpandableRelativeLayout expandableLayout;
    RecyclerView lvTimes;
    static RecyclerView lvTimes2;
    Spinner time1,date1,time2,date2,addressSpinner;
    static Activity a;
    Button btn_sabt,btn_cancel,btn_discount_check;
    String [] latlng;
    ProgressDialog simpleLoading;
    String Server_MainUrl="",phone="",requests="";
    int serviceID=-1,extravalue=0,price=0,extravalue2=0,ExtraMoneyAfterXPrice=0;
    String extratext="هزینه اضافی";
    SharedPreferences prefs;
    EditText takhfif,description;
    RadioButton radioEtebari,radioNaghdi;
    LinearLayout daryaftLayout,tahvilLayout;
    TextView daryaft,tahvil,textView6,textView5;
    TextView mablaghSefareshText,mablaghSefareshValue,mablaghExtraText,mablaghExtraValue,mablaghKolText,mablaghKolValue;
    public static TextView editdaryaft,edittahvil;
    ProgressBar circle_progress;
    public static String url="";
    public static String daryaftDateCode="",tahvilDateCode="";
    public static  AlertDialog alertDialog;
    Boolean SecondTime=false,FirstTime=false;
    DecimalFormat df = new DecimalFormat();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_get_order_time_and_address);
        radioEtebari=(RadioButton)findViewById(R.id.radioEtebari);
        radioNaghdi=(RadioButton)findViewById(R.id.radioNaghdi);
        editdaryaft=(TextView) findViewById(R.id.editdaryaft);
        btn_discount_check=(Button)findViewById(R.id.btn_discount_check);
        expandableLayout=(ExpandableRelativeLayout)findViewById(R.id.expandableLayout);
        edittahvil=(TextView)findViewById(R.id.edittahvil);
        tatDiscount=(TextView)findViewById(R.id.tatDiscount);
        time1=(Spinner)findViewById(R.id.timeSpinner);
        textView6=(TextView)findViewById(R.id.textView6);
        date1=(Spinner)findViewById(R.id.dateSpinner);
        time2=(Spinner)findViewById(R.id.timeSpinner2);
        daryaftLayout=(LinearLayout)findViewById(R.id.daryaftLayout) ;
        tahvilLayout=(LinearLayout)findViewById(R.id.tahvilLayout) ;
        tahvil=(TextView)findViewById(R.id.tahvil);
        daryaft=(TextView)findViewById(R.id.daryaft);
        date2=(Spinner)findViewById(R.id.dateSpinner2);
        textView5=(TextView)findViewById(R.id.textView5);
        addressSpinner=(Spinner)findViewById(R.id.addressSpinner);
        takhfif=(EditText)findViewById(R.id.takhfif);
        description=(EditText)findViewById(R.id.description);
        btn_sabt=(Button)findViewById(R.id.btn_sabt);
        btn_cancel=(Button)findViewById(R.id.btn_cancel);

        mablaghSefareshText=(TextView)findViewById(R.id.mablaghSefareshText);
        mablaghSefareshValue=(TextView)findViewById(R.id.mablaghSefareshValue);
        mablaghExtraText=(TextView)findViewById(R.id.mablaghExtraText);
        mablaghExtraValue=(TextView)findViewById(R.id.mablaghExtraValue);
        mablaghKolText=(TextView)findViewById(R.id.mablaghKolText);
        mablaghKolValue=(TextView)findViewById(R.id.mablaghKolValue);


        tatDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                expandableLayout.toggle();
            }
        });



        simpleLoading = new ProgressDialog(this);
        simpleLoading.setMessage("در حال ارسال اطلاعات");
        simpleLoading.setCancelable(false);

        prefs= PreferenceManager.getDefaultSharedPreferences(this);
        Server_MainUrl = prefs.getString("Server_MainUrl", null);
        phone= prefs.getString("customer_phone", null);

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(',');

        df.setDecimalFormatSymbols(symbols);
        df.setGroupingSize(3);
        //df.setMaximumFractionDigits(2);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            requests = extras.getString("requests");
            serviceID=extras.getInt("serviceID");

            price=extras.getInt("price",0);
            extratext=extras.getString("extratext","هزینه های اضافی");
            extravalue=extras.getInt("extravalue",0);
            extravalue2=extras.getInt("extravalue2",0);
            ExtraMoneyAfterXPrice=extras.getInt("ExtraMoneyAfterXPrice",0);
            if(extratext==null || extratext.equals("") || extratext.equals("null"))
                extratext="هزینه های اضافی";
            mablaghSefareshValue.setText(df.format(price)+" ریال");
            mablaghExtraText.setText(extratext);
            if(price<ExtraMoneyAfterXPrice)
            {
                mablaghExtraValue.setText(df.format(extravalue)+" ریال");
                mablaghKolValue.setText((df.format(extravalue+price))+" ریال");
            }
            else {
                mablaghExtraValue.setText(df.format(extravalue2)+" ریال");
                mablaghKolValue.setText((df.format(extravalue2+price))+" ریال");
            }


            btn_discount_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new CheckDiscount().execute(takhfif.getText().toString().trim());
                }
            });

            FirstTime=extras.getBoolean("FirstTime",false);
            if(FirstTime)
            {
                String FirstText1=extras.getString("FirstTimeText","");
                daryaft.setText(FirstText1);
                String FirstText2=extras.getString("FirstTimeText2","");
                textView5.setText(FirstText2);
            }
            else
            {
                daryaft.setVisibility(View.GONE);
                daryaftLayout.setVisibility(View.GONE);
            }
            SecondTime=extras.getBoolean("SecondTime",false);
            if(SecondTime)
            {
                String SecondText1=extras.getString("SecondTimeText","");
                tahvil.setText(SecondText1);
                String SecondText2=extras.getString("SecondTimeText2","");
                textView6.setText(SecondText2);
            }
            else
            {
                tahvil.setVisibility(View.GONE);
                tahvilLayout.setVisibility(View.GONE);
            }
        }

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(a,"سفارش شما لغو شد",Toast.LENGTH_LONG).show();
                Consts.arr2=new ArrayList<>();
                Consts.arr_count=new ArrayList<>();
                Consts.last=0;
                Intent i=new Intent(getApplicationContext(),ServicesActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

                finish();
            }
        });


        editdaryaft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HideKeyboard();
                url=Server_MainUrl+"/services/GetTimes?serviceid="+serviceID+"&phone="+phone;
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(a);
                SubScheduleListAdapter.selectedPosition=0;
                LayoutInflater inflater = getLayoutInflater();
                final View dialog = inflater.inflate(R.layout.time_list_custom_dialog, null);
                dialogBuilder.setView(dialog);
                circle_progress=(ProgressBar) dialog.findViewById(R.id.circle_progress);
                TextView choosetime=(TextView)dialog.findViewById(R.id.choosetime);
                TextView choosedate=(TextView)dialog.findViewById(R.id.choosedate);
                lvTimes=(RecyclerView)dialog.findViewById(R.id.lvTimes);
                lvTimes2=(RecyclerView)dialog.findViewById(R.id.lvTimes2);
                final TextView title = (TextView) dialog.findViewById(R.id.title);
                Button button3 = (Button) dialog.findViewById(R.id.button3);
                new GetScheduleListData().execute(url);
                alertDialog = dialogBuilder.create();
                button3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();



                //Intent i=new Intent(v.getContext(),TimeListActivity.class);
               // i.putExtra("url",Server_MainUrl+"/services/GetTimes?serviceid="+serviceID+"&phone="+phone+"&after=");
               // startActivityForResult(i,2);
            }
        });

        edittahvil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(daryaftDateCode.equals(""))
                {
                    Toast.makeText(a,"ابتدا زمان مراجعه را انتخاب کنید",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    HideKeyboard();
                    url=Server_MainUrl + "/services/GetTimes?serviceid=" + serviceID + "&phone=" + phone + "&after=" + daryaftDateCode.replace(" ","%20");
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(a);
                    LayoutInflater inflater = getLayoutInflater();
                    SubScheduleListAdapter.selectedPosition=0;
                    final View dialog = inflater.inflate(R.layout.time_list_custom_dialog, null);
                    dialogBuilder.setView(dialog);
                    circle_progress=(ProgressBar) dialog.findViewById(R.id.circle_progress);
                    TextView choosetime=(TextView)dialog.findViewById(R.id.choosetime);
                    TextView choosedate=(TextView)dialog.findViewById(R.id.choosedate);
                    lvTimes=(RecyclerView)dialog.findViewById(R.id.lvTimes);
                    lvTimes2=(RecyclerView)dialog.findViewById(R.id.lvTimes2);
                    final TextView title = (TextView) dialog.findViewById(R.id.title);
                    Button button3 = (Button) dialog.findViewById(R.id.button3);
                    new GetScheduleListData().execute(url);
                    alertDialog = dialogBuilder.create();
                    button3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            alertDialog.dismiss();
                        }
                    });

                    alertDialog.show();

                }
            }
        });
        a=this;
        Calendar c1 = Calendar.getInstance();

        int day = c1.get(Calendar.DATE);
        int mounth = c1.get(Calendar.MONTH)+1;
        int year = c1.get(Calendar.YEAR);

        String d1= ShamsiCalleder.getCurrentShamsidate(year+"/"+mounth+"/"+day);

        Calendar c2 = addDays(c1.getTime(),1);

        int day2 = c2.get(Calendar.DATE);
        int mounth2 = c2.get(Calendar.MONTH)+1;
        int year2 = c2.get(Calendar.YEAR);

        String d2= ShamsiCalleder.getCurrentShamsidate(year2+"/"+mounth2+"/"+day2);

        Calendar c3 = addDays(c1.getTime(),2);

        int day3 = c3.get(Calendar.DATE);
        int mounth3 = c3.get(Calendar.MONTH)+1;
        int year3 = c3.get(Calendar.YEAR);

        String d3= ShamsiCalleder.getCurrentShamsidate(year3+"/"+mounth3+"/"+day3);

        Calendar c4 = addDays(c1.getTime(),3);

        int day4 = c4.get(Calendar.DATE);
        int mounth4 = c4.get(Calendar.MONTH)+1;
        int year4 = c4.get(Calendar.YEAR);

        String d4= ShamsiCalleder.getCurrentShamsidate(year4+"/"+mounth4+"/"+day4);

        Calendar c5 = addDays(c1.getTime(),4);

        int day5 = c5.get(Calendar.DATE);
        int mounth5 = c5.get(Calendar.MONTH)+1;
        int year5 = c5.get(Calendar.YEAR);

        String d5= ShamsiCalleder.getCurrentShamsidate(year5+"/"+mounth5+"/"+day5);

        Calendar c6 = addDays(c1.getTime(),5);

        int day6 = c6.get(Calendar.DATE);
        int mounth6 = c6.get(Calendar.MONTH)+1;
        int year6 = c6.get(Calendar.YEAR);

        String d6= ShamsiCalleder.getCurrentShamsidate(year6+"/"+mounth6+"/"+day6);


        Calendar c7 = addDays(c1.getTime(),6);

        int day7 = c7.get(Calendar.DATE);
        int mounth7 = c7.get(Calendar.MONTH)+1;
        int year7 = c7.get(Calendar.YEAR);

        String d7= ShamsiCalleder.getCurrentShamsidate(year7+"/"+mounth7+"/"+day7);


        int Hr24=c1.get(Calendar.HOUR_OF_DAY);

        final String [] timestr=new String[]{"7:00 - 8:00","8:00 - 9:00","9:00 - 10:00"
                ,"10:00 - 11:00","11:00 - 12:00","12:00 - 13:00","13:00 - 14:00"
        ,"14:00 - 15:00","15:00 - 16:00","16:00 - 17:00","17:00 - 18:00","18:00 - 19:00"
        ,"19:00 - 20:00","20:00 - 21:00","21:00 - 22:00","22:00 - 23:00","23:00 - 24:00"};

        final String[] timestr_today=new String[24-Hr24];
        for (int i=Hr24;i<24;i++) {
            timestr_today[i-Hr24] = i + ":00 - " + (i + 1)+":00";

        }
        String [] datestr=new String[]{d1,d2,d3,d4,d5,d6,d7};
        ArrayAdapter NoCoreAdapter = new ArrayAdapter(this,
                R.layout.my_spinner_textview3, timestr_today){

            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);

                ((TextView) v).setTextSize(16);
                return v;

            }

            public View getDropDownView(int position, View convertView,ViewGroup parent) {

                View v = super.getDropDownView(position, convertView,parent);

                ((TextView) v).setGravity(Gravity.CENTER);
                return v;

            }
        };
        time1.setAdapter(NoCoreAdapter);
        time1.setSelection(0);
        time2.setAdapter(NoCoreAdapter);
        time2.setSelection(0);

        ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(this,
                R.layout.my_spinner_textview3, datestr){

            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);

                ((TextView) v).setTextSize(16);
                return v;

            }

            public View getDropDownView(int position, View convertView,ViewGroup parent) {

                View v = super.getDropDownView(position, convertView,parent);

                ((TextView) v).setGravity(Gravity.CENTER);
                return v;

            }
        };
        date1.setAdapter(NoCoreAdapter2);
        date1.setSelection(0);
        date2.setAdapter(NoCoreAdapter2);
        date2.setSelection(0);




        date1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                {
                    ArrayAdapter NoCoreAdapter = new ArrayAdapter(a,
                            R.layout.my_spinner_textview3, timestr_today){

                        public View getView(int position, View convertView, ViewGroup parent) {

                            View v = super.getView(position, convertView, parent);

                            ((TextView) v).setTextSize(16);
                            return v;

                        }

                        public View getDropDownView(int position, View convertView,ViewGroup parent) {

                            View v = super.getDropDownView(position, convertView,parent);

                            ((TextView) v).setGravity(Gravity.CENTER);
                            return v;

                        }
                    };
                    time1.setAdapter(NoCoreAdapter);
                    time1.setSelection(0);
                }
                else
                {

                    ArrayAdapter NoCoreAdapter = new ArrayAdapter(a,
                            R.layout.my_spinner_textview3, timestr){

                        public View getView(int position, View convertView, ViewGroup parent) {

                            View v = super.getView(position, convertView, parent);

                            ((TextView) v).setTextSize(16);
                            return v;

                        }

                        public View getDropDownView(int position, View convertView,ViewGroup parent) {

                            View v = super.getDropDownView(position, convertView,parent);

                            ((TextView) v).setGravity(Gravity.CENTER);
                            return v;

                        }
                    };
                    time1.setAdapter(NoCoreAdapter);
                    time1.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        date2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                {
                    ArrayAdapter NoCoreAdapter = new ArrayAdapter(a,
                            R.layout.my_spinner_textview3, timestr_today){

                        public View getView(int position, View convertView, ViewGroup parent) {

                            View v = super.getView(position, convertView, parent);

                            ((TextView) v).setTextSize(16);
                            return v;

                        }

                        public View getDropDownView(int position, View convertView,ViewGroup parent) {

                            View v = super.getDropDownView(position, convertView,parent);

                            ((TextView) v).setGravity(Gravity.CENTER);
                            return v;

                        }
                    };
                    time2.setAdapter(NoCoreAdapter);
                    time2.setSelection(0);
                }
                else
                {

                    ArrayAdapter NoCoreAdapter = new ArrayAdapter(a,
                            R.layout.my_spinner_textview3, timestr){

                        public View getView(int position, View convertView, ViewGroup parent) {

                            View v = super.getView(position, convertView, parent);

                            ((TextView) v).setTextSize(16);
                            return v;

                        }

                        public View getDropDownView(int position, View convertView,ViewGroup parent) {

                            View v = super.getDropDownView(position, convertView,parent);

                            ((TextView) v).setGravity(Gravity.CENTER);
                            return v;

                        }
                    };
                    time2.setAdapter(NoCoreAdapter);
                    time2.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_sabt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Toast.makeText(a,"لطفا فیلد آدرس را وارد نمایید",Toast.LENGTH_SHORT).show();
            }});



        addressSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
              if( /* addressSpinner.getSelectedItemPosition()<0 &&*/  !notFirst) {
                    Intent i = new Intent(v.getContext(), SelectedLocationActivity.class);
                    i.putExtra("state", "maghsad");
                    notFirst=true;
                    startActivityForResult(i, 10);
                }

                return false;
            }
        });



/*
        switch (serviceID)
        {
            case 4:
                tahvil.setText("زمان مراجعه");
                textView6.setText("تاریخ و ساعت مراجعه به کارواش");
                daryaft.setVisibility(View.GONE);
                daryaftLayout.setVisibility(View.GONE);

            break;
            case 2 :
                //tahvil.setText("زمان مراجعه");
               // textView6.setText("تاریخ و ساعت مراجعه برای تحویل");
                //daryaft.setVisibility(View.VISIBLE);
                //daryaftLayout.setVisibility(View.VISIBLE);
                break;
            case 1:
                break;

            default:
                tahvil.setVisibility(View.GONE);
                daryaft.setVisibility(View.GONE);
                daryaftLayout.setVisibility(View.GONE);
                tahvilLayout.setVisibility(View.GONE);
            break;

        }
*/
    }


    @Override
    public void onBackPressed()
    {

        GetOrderTimeAndAddressActivity.tahvilDateCode="";
        GetOrderTimeAndAddressActivity.daryaftDateCode="";
        GetOrderTimeAndAddressActivity.url="";
        super.onBackPressed();  // optional depending on your needs

    }


    void HideKeyboard()
    {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public  static  void SetTime(String time,String date)
    {
        if(!url.contains("after="))
        {
            editdaryaft.setText(time);
            daryaftDateCode=date;
            edittahvil.setText("انتخاب زمان");
        }
        else
        {
            edittahvil.setText(time);
            tahvilDateCode=date;
        }
        alertDialog.dismiss();
    }
    Boolean notFirst=false;
    String Tozihat="";
    String Name="";
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==10)
        {
            if (resultCode == RESULT_OK) //&& data.getExtras().getString("res").equals("1")
            {
                latlng=data.getExtras().getString("res").split("~");
                 Tozihat=data.getExtras().getString("desc");
                 Name=data.getExtras().getString("name");

                String [] timestr=new String[2];
                timestr[0]=Name+" , "+Tozihat;
                timestr[1]="-- تغییر آدرس --";

                ArrayAdapter NoCoreAdapter = new ArrayAdapter(a,
                        R.layout.my_spinner_textview3, timestr){

                    public View getView(int position, View convertView, ViewGroup parent) {

                        View v = super.getView(position, convertView, parent);

                        ((TextView) v).setTextSize(16);
                        return v;

                    }

                    public View getDropDownView(int position, View convertView,ViewGroup parent) {

                        View v = super.getDropDownView(position, convertView,parent);

                        ((TextView) v).setGravity(Gravity.CENTER);
                        return v;

                    }
                };
                addressSpinner.setAdapter(NoCoreAdapter);
                addressSpinner.setSelection(0);
                notFirst=false;

                addressSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if(position==1)
                        {
                            Intent i = new Intent(view.getContext(), SelectedLocationActivity.class);
                            i.putExtra("state", "maghsad");
                            notFirst=true;
                            startActivityForResult(i, 10);

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


            }
            else
            {
                notFirst=false;
            }
        }


        btn_sabt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(FirstTime && editdaryaft.getText().toString().equals("انتخاب زمان"))
                    Toast.makeText(a,"لطفا قسمت "+daryaft.getText().toString()+" را وارد کنید",Toast.LENGTH_SHORT).show();
                else if(SecondTime && edittahvil.getText().toString().equals("انتخاب زمان"))
                    Toast.makeText(a,"لطفا قسمت "+tahvil.getText().toString()+" را وارد کنید",Toast.LENGTH_SHORT).show();
                else if(latlng==null || latlng.length==0)
                    Toast.makeText(a,"لطفا قسمت آدرس را وارد کنید",Toast.LENGTH_SHORT).show();
                else {
                    String type = "0";//naghdi
                    if (radioEtebari.isChecked())//etebari
                        type = "1";
                    new SendRequest().execute(requests, URLEncoder.encode(description.getText().toString()),
                            takhfif.getText().toString(), URLEncoder.encode(Tozihat),
                            URLEncoder.encode(date1.getSelectedItem().toString() + " , " + time1.getSelectedItem().toString()),
                            URLEncoder.encode(date2.getSelectedItem().toString() + " , " + time2.getSelectedItem().toString())
                            , type);
                }
            }
        });



    }




    private class SendRequest extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            simpleLoading.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                Log.v("url",Server_MainUrl + "/services/AddRequest?phone="+phone+"&requests=" + params[0]+"&lat="+latlng[0]
                        +"&lng="+latlng[1]+"&description="+params[1]+"&takhfifuniqueid="+params[2]+"&address="+params[3]
                        +"&date1="+daryaftDateCode.replace(" ","%20")+"&date2="+tahvilDateCode.replace(" ","%20")+"&type="+params[6]
                );
                URL url2 = new URL(Server_MainUrl + "/services/AddRequest?phone="+phone+"&requests=" + params[0]+"&lat="+latlng[0]
                        +"&lng="+latlng[1]+"&description="+params[1]+"&takhfifuniqueid="+params[2]+"&address="+params[3]
                        +"&date1="+daryaftDateCode.replace(" ","%20")+"&date2="+tahvilDateCode.replace(" ","%20")+"&type="+params[6]);
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
                if(result.contains("true~"))
                {
                    String [] res=result.split("~");
                    //Toast.makeText(a,,Toast.LENGTH_LONG).show();


                    Consts.arr2=new ArrayList<>();
                    Consts.arr_count=new ArrayList<>();
                    Consts.last=0;
                    ShowMsg.Show(a,"پیام","تبریک سفارش شما با احتساب کد تخفیف با موفقیت ثبت شد مبلغ تخفیف شما "+res[1]+" ریال می باشد، جهت تایید سفارش با شما تماس گرفته خواهد شد",true,true);

                }
                else if(result.contains("true"))
                {
                    //Toast.makeText(a,"سفارش شما با موفقیت ثبت شد ، جهت تایید سفارش با شما تماس گرفته خواهد شد",Toast.LENGTH_LONG).show();


                    Consts.arr2=new ArrayList<>();
                    Consts.arr_count=new ArrayList<>();
                    Consts.last=0;

                    ShowMsg.Show(a,"پیام","سفارش شما با موفقیت ثبت شد ، جهت تایید سفارش با شما تماس گرفته خواهد شد",true,true);

                }
                else if(result.contains("not money"))
                {

                   // Toast.makeText(a,"موجودی حساب شما کافی نمی باشد",Toast.LENGTH_LONG).show();
                    //finish();
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(a);
                    LayoutInflater inflater = getLayoutInflater();
                    final View dialog = inflater.inflate(R.layout.etebar_custom_dialog, null);
                    dialogBuilder.setView(dialog);

                    final EditText oldpass = (EditText) dialog.findViewById(R.id.value);
                    final TextView newpass = (TextView) dialog.findViewById(R.id.title);
                    Button button3 = (Button) dialog.findViewById(R.id.button3);
                    TextView title = (TextView) dialog.findViewById(R.id.title);
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

                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Server_MainUrl+"/home/IncreasePassengerMoney?phone="+phone+"&value="+toEnglishNumber(oldpass.getText().toString())));
                            startActivity(browserIntent);
                            alertDialog.dismiss();
                        }
                    });

                    alertDialog.show();
                    ShowMsg.Show(a,"پیام","موجودی حساب شما کافی نمی باشد",false,false);

                }

                else
                {



                    //Toast.makeText(a,result,Toast.LENGTH_LONG).show();
                   // finish();
                    ShowMsg.Show(a,"پیام",result,false,false);
                }
            }
        }
    }
    static List<ScheduleModel> sch_data;
    public static void ClickDate(int index)
    {


        lvTimes2.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(a, LinearLayoutManager.HORIZONTAL, false);
        layoutManager.setReverseLayout(true);
        lvTimes2.setLayoutManager(layoutManager);

        SubScheduleListAdapter2 myadapter = new SubScheduleListAdapter2(a, sch_data.get(index).Dates,sch_data.get(index).DateStr);
        lvTimes2.setAdapter(myadapter);
        myadapter.notifyDataSetChanged();

    }
    private String toEnglishNumber(String input)
    {
        String[] persian = new String[]{ "۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹" };

        for (int j = 0; j < persian.length; j++) {
            input= input.replace(persian[j],j+"");
        }
        return input;
    }
    private class GetScheduleListData extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            simpleLoading.show();
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
            simpleLoading.cancel();

            if(result==null) {

                android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(a);
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
                    sch_data =new ArrayList<>();

                    for (int i=0;i<json.length();i++)
                    {
                        ScheduleModel item1=new ScheduleModel();
                        item1.DateStr=json.getJSONObject(i).getString("DateStr");

                        item1.daytype=json.getJSONObject(i).getString("daytype");
                        item1.weekday=json.getJSONObject(i).getString("weekday");
                        item1.daynumber=json.getJSONObject(i).getString("daynumber");
                        item1.month=json.getJSONObject(i).getString("month");

                        JSONArray subjson=new JSONArray(json.getJSONObject(i).getString("Dates"));
                        for(int j=0;j<subjson.length();j++)
                        {
                            item1.AddToList(subjson.getJSONObject(j).getString("Time"),subjson.getJSONObject(j).getString("Date"));
                        }
                        sch_data.add(item1);
                    }


                    if(sch_data==null || sch_data.size()==0) {
                        lvTimes.setAdapter(null);
                        lvTimes2.setAdapter(null);
                    }
                    else {

                        lvTimes.setHasFixedSize(true);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(a, LinearLayoutManager.HORIZONTAL, false);
                        layoutManager.setReverseLayout(true);
                        lvTimes.setLayoutManager(layoutManager);

                        SubScheduleListAdapter  myadapter = new SubScheduleListAdapter(a, sch_data);
                        lvTimes.setAdapter(myadapter);


                        ClickDate(0);

                        circle_progress.setVisibility(View.GONE);
                        lvTimes.setVisibility(View.VISIBLE);
                    }
                }
                catch (JSONException ex)
                {

                }
            }
        }
    }





    private class CheckDiscount extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... arg0) {

            try {
                //URL url = new URL("http://taxnet.ir/api/category/"+categoryID+"/shop/"+shop_id);
                Log.v("url", Server_MainUrl+"/services/checkdiscount?serviceid="+serviceID+"&phone="
                        +phone+"&discountcode="+arg0[0]+"&price="+price);
                URL url = new URL(Server_MainUrl+"/services/checkdiscount?serviceid="+serviceID+"&phone="
                        +phone+"&discountcode="+arg0[0]+"&price="+price);
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


            if (result == null) {

                android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(a);
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

                try
                {
                    JSONObject obj=new JSONObject(result);
                    if(obj.getBoolean("status"))
                    {
                         String text=obj.getString("text");
                         long newprice=obj.getLong("newprice");

                        mablaghKolValue.setText((df.format(extravalue+newprice))+" ریال");
                        mablaghKolText.setText("مبلغ قابل پرداخت با کسر مبلغ تخفیف");
                        ShowMsg.Show(a,"پیام",text,false,false);
                        //Toast.makeText(a,text,Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        String text=obj.getString("text");
                        //Toast.makeText(a,text,Toast.LENGTH_LONG).show();
                        ShowMsg.Show(a,"پیام",text,false,false);
                    }
                }
                catch (JSONException ex){}
            }

        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
