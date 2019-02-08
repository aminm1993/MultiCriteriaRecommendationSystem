package ir.mohammadpour.app.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

import ir.mohammadpour.app.R;
import ir.mohammadpour.app.model.OrderDetails;
import ir.mohammadpour.app.model.PassengerOrder;

public class OrderDetailsAdapter extends BaseAdapter {

    private Activity activity;

    List<OrderDetails> arrayKala;

    private static LayoutInflater inflater = null;

    public OrderDetailsAdapter(Activity a, List<OrderDetails> arrkala) {

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
        TextView service_status;
        TextView service_price;
        TextView service_unit;
    }


    public View getView(final int position, final View convertView, ViewGroup parent) {


        //if(convertView!=null)
         //   return convertView;

        final Holder holder=new Holder();
        final View rowView;
        rowView = inflater.inflate(R.layout.order_details_item, null);
        Typeface type = Typeface.createFromAsset(rowView.getContext().getAssets(),"IRANSans.ttf");
        holder.service_unit=(TextView) rowView.findViewById(R.id.service_unit);
        holder.service_price=(TextView) rowView.findViewById(R.id.service_price);
        holder.service_status=(TextView) rowView.findViewById(R.id.service_status);


        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        DecimalFormat df = new DecimalFormat();
        df.setDecimalFormatSymbols(symbols);
        df.setGroupingSize(3);


        holder.service_price.setText(df.format(arrayKala.get(position).Price) +" ریال ");
        holder.service_status.setText(arrayKala.get(position).Name);
        holder.service_unit.setText(arrayKala.get(position).Count+" واحد");


        holder.service_price.setTypeface(type);
        holder.service_status.setTypeface(type);
        holder.service_unit.setTypeface(type);

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
