package ir.mohammadpour.app.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import ir.mohammadpour.app.R;
import ir.mohammadpour.app.model.Transaction;


public class TransactionAdapter extends BaseAdapter {

    private Activity activity;

    List<Transaction> arrayKala;

    private static LayoutInflater inflater = null;

    public TransactionAdapter(Activity a, List<Transaction> arrkala) {

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
        TextView txtType;
        ImageView imgType;

        TextView txtDescription;
        TextView txtDescription2;

        TextView txtDate;
        TextView txtDate2;

        TextView txtPrice;
        TextView txtPrice2;

    }


    public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView!=null)
                return convertView;

            final Holder holder=new Holder();
            final View rowView;
            rowView = inflater.inflate(R.layout.transaction_item, null);
            holder.txtDate=(TextView)rowView.findViewById(R.id.txtDate) ;
            holder.txtDate2=(TextView)rowView.findViewById(R.id.txtDate2) ;

            holder.txtType=(TextView)rowView.findViewById(R.id.txtType) ;
            holder.imgType=(ImageView) rowView.findViewById(R.id.imgType) ;

            holder.txtDescription=(TextView)rowView.findViewById(R.id.txtDescription);
            holder.txtDescription2=(TextView)rowView.findViewById(R.id.txtDescription2);

            holder.txtPrice=(TextView)rowView.findViewById(R.id.txtPrice);
            holder.txtPrice2=(TextView)rowView.findViewById(R.id.txtPrice2);


            Typeface type = Typeface.createFromAsset(rowView.getContext().getAssets(),"IRANSans.ttf");

            holder.txtDate.setTypeface(type);
            holder.txtDate2.setTypeface(type);

            holder.txtPrice.setTypeface(type);
            holder.txtPrice2.setTypeface(type);

            holder.txtType.setTypeface(type);

            holder.txtDescription.setTypeface(type);
            holder.txtDescription2.setTypeface(type);

            holder.txtDate2.setText(arrayKala.get(position).getCreateDateStr());
            holder.txtDescription2.setText(arrayKala.get(position).getDescription());
            if(arrayKala.get(position).getType())
                holder.imgType.setImageResource(R.drawable.up);
            else
                holder.imgType.setImageResource(R.drawable.down);

            DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
            DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();//new DecimalFormatSymbols();
            symbols.setGroupingSeparator(',');
            DecimalFormat df = new DecimalFormat();
            df.setDecimalFormatSymbols(symbols);
            df.setGroupingSize(3);

            holder.txtPrice2.setText(df.format(arrayKala.get(position).getValue()) +" ریال");

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

}
