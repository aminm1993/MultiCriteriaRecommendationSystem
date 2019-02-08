package ir.mohammadpour.app.ui.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Update;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ir.mohammadpour.app.R;
import ir.mohammadpour.app.model.MaghsadTakhfifModel;
import ir.mohammadpour.app.model.SelectedAddressModel;
import ir.mohammadpour.app.ui.activity.SelectedLocationActivity;

public class TakhfifAdapter extends BaseAdapter {

    private Activity activity;

    List<MaghsadTakhfifModel> arrayKala;

    private static LayoutInflater inflater = null;

    public TakhfifAdapter(Activity a, List<MaghsadTakhfifModel> arrkala) {

        activity = a;
        arrayKala = arrkala;

        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return arrayKala.size();
    }

    public Object getItem(int position) {
        return arrayKala.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
    class Holder
    {
        TextView name;
        TextView name2;
        TextView name3;
        TextView name4;

        Button b;
    }
    String Server_MainUrl="";
    SharedPreferences prefs;
    ProgressDialog simpleLoading;
    int current_pos=-1;
    public View getView(final int position, final View convertView, ViewGroup parent) {



        if(convertView!=null)
            return convertView;

        final Holder holder=new Holder();
        final View rowView;
        rowView = inflater.inflate(R.layout.takhfif_item, null);

        prefs= PreferenceManager.getDefaultSharedPreferences(activity);
        Server_MainUrl = prefs.getString("Server_MainUrl", null);
        simpleLoading = new ProgressDialog(activity);
        simpleLoading.setMessage("در حال دریافت اطلاعات");
        simpleLoading.setCancelable(false);

        holder.name=(TextView)rowView.findViewById(R.id.name) ;
        holder.name2=(TextView)rowView.findViewById(R.id.name2) ;
        holder.name3=(TextView)rowView.findViewById(R.id.name3) ;
        holder.name4=(TextView)rowView.findViewById(R.id.name4) ;
        holder.b=(Button) rowView.findViewById(R.id.choose) ;

        Typeface type = Typeface.createFromAsset(rowView.getContext().getAssets(),"IRANSans.ttf");
        Typeface type2 = Typeface.createFromAsset(rowView.getContext().getAssets(),"Regular.ttf");
        holder.name2.setTypeface(type);
        holder.name.setTypeface(type);
        holder.name3.setTypeface(type);
        holder.name4.setTypeface(type2);
        holder.b.setTypeface(type);

        holder.name.setText(arrayKala.get(position).description);
        holder.name2.setText("آدرس : "+arrayKala.get(position).description);
        if(arrayKala.get(position).type)//darsadi
        {
            holder.name3.setText("نوع تخفیف : درصدی");
            holder.name4.setText(arrayKala.get(position).value+" درصد");
        }
        else
        {
            holder.name3.setText("نوع تخفیف : مبلغ ثابت");
            holder.name4.setText(arrayKala.get(position).value+" ریال");
        }
        holder.b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String customer_phone = prefs.getString("customer_phone", null);
                current_pos=position;
                new GetTakhfifInfo().execute(arrayKala.get(position).uniqucode,customer_phone);


            }
        });


        return rowView;
    }

    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }


    private class GetTakhfifInfo extends AsyncTask<String, Void, String> {

        @Override
        protected  void onPreExecute()
        {
            simpleLoading.show();
        }
        @Override
        protected String doInBackground(String... params) {
            try
            {

                URL url2 = new URL(Server_MainUrl+"/takhfif/GetMaghsadDiscount?uniquecode="+params[0]+"&phone="+params[1]);
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
                JSONObject obj=new JSONObject(result);
                if(obj.getBoolean("usable")) {
                    activity.getIntent().putExtra("res", arrayKala.get(current_pos).lat + "~" + arrayKala.get(current_pos).lng);
                    activity.getIntent().putExtra("desc", arrayKala.get(current_pos).description);
                    activity.getIntent().putExtra("desc2", arrayKala.get(current_pos).uniqucode);
                    activity.getIntent().putExtra("takhfif_value", arrayKala.get(current_pos).value);
                    activity.getIntent().putExtra("takhfif_type", arrayKala.get(current_pos).type);
                    activity.setResult(activity.RESULT_OK, activity.getIntent());
                    Toast.makeText(activity,"آدرس با موفقیت افزوده شد",Toast.LENGTH_SHORT).show();
                }
                else
                {

                    Toast.makeText(activity,obj.getString("description"),Toast.LENGTH_SHORT).show();
                    activity.setResult(activity.RESULT_CANCELED, activity.getIntent());
                }
                activity.finish();

            }
            catch (JSONException ex)
            {
                Toast.makeText(activity,"مشکل در درخواست",Toast.LENGTH_SHORT).show();
                activity.setResult(activity.RESULT_CANCELED, activity.getIntent());
            }

        }
    }

}
