package ir.mohammadpour.app.ui.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ir.mohammadpour.app.R;
import ir.mohammadpour.app.model.Message;


public class MessageAdapter extends BaseAdapter {

    private Context activity;

    List<Message> messges;
    private static LayoutInflater inflater = null;

    public MessageAdapter(Context a, List<Message> _messges) {

        activity = a;
        messges = _messges;

        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return messges.size();
    }

    public Object getItem(int position) {
        return messges.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
    class Holder
    {
        TextView text;
        TextView date;
    }


    SharedPreferences prefs;
    String voiceStoragePath="";
    public View getView(final int position, final View convertView, ViewGroup parent) {


        //if(convertView!=null)
        //  return convertView;
        prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        final Holder holder = new Holder();
         View rowView = inflater.inflate(R.layout.message_item, null);



        holder.text=(TextView)rowView.findViewById(R.id.text) ;
        holder.date=(TextView)rowView.findViewById(R.id.date) ;
        Typeface type = Typeface.createFromAsset(rowView.getContext().getAssets(),"BYekan.ttf");
        Typeface type2 = Typeface.createFromAsset(rowView.getContext().getAssets(),"Regular.ttf");
        holder.text.setTypeface(type);
        holder.date.setTypeface(type2);
        holder.text.setText(messges.get(position).s_name);
        if(messges.get(position).s_time==null || messges.get(position).s_time.equals(""))
            holder.date.setText("");
        else
            holder.date.setText(messges.get(position).s_time);




        return rowView;
    }

    ProgressDialog dialog;


}
