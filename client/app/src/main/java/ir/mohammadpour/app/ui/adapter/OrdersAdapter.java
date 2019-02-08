package ir.mohammadpour.app.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.mohammadpour.app.R;
import ir.mohammadpour.app.model.PassengerOrder;
import ir.mohammadpour.app.model.ServiceModel;
import ir.mohammadpour.app.ui.activity.SearchPlaceOnMapActivity;
import ir.mohammadpour.app.ui.activity.Service2Activity;

public class OrdersAdapter extends BaseAdapter {

    private Activity activity;

    List<PassengerOrder> arrayKala;

    private static LayoutInflater inflater = null;

    public OrdersAdapter(Activity a, List<PassengerOrder> arrkala) {

        activity = a;
        arrayKala = arrkala;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
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
        TextView createdate;
        TextView service_name;
        TextView date1;
        TextView service_price;
        Button details;
    }
    SharedPreferences prefs;

    public View getView(final int position, final View convertView, ViewGroup parent) {

        prefs = PreferenceManager.getDefaultSharedPreferences(activity);

       // if(convertView!=null)
        //    return convertView;

        final Holder holder=new Holder();
        final View rowView;
        rowView = inflater.inflate(R.layout.order_item, null);

        Typeface type = Typeface.createFromAsset(rowView.getContext().getAssets(),"IRANSans.ttf");
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();//new DecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        DecimalFormat df = new DecimalFormat();
        df.setDecimalFormatSymbols(symbols);
        df.setGroupingSize(3);
        holder.service_name=(TextView)rowView.findViewById(R.id.service_name);
        holder.details=(Button) rowView.findViewById(R.id.button3);
        holder.service_price=(TextView) rowView.findViewById(R.id.service_price);
        holder.date1=(TextView) rowView.findViewById(R.id.date1);
        holder.createdate=(TextView) rowView.findViewById(R.id.createdate);
        holder.service_status=(TextView) rowView.findViewById(R.id.service_status);
        holder.service_price.setText(df.format(arrayKala.get(position).Price) +" ریال ");
        if (arrayKala.get(position).Status==0)
            holder.service_status.setText("نوع پرداخت : نقدی");
        else
            holder.service_status.setText("نوع پرداخت : اعتباری");

        Log.v("itemscount",arrayKala.get(position).Services.size()+"");

        holder.service_name.setTypeface(type);
        holder.date1.setTypeface(type);
        holder.createdate.setTypeface(type);
        holder.service_status.setTypeface(type);
        holder.service_price.setTypeface(type);

        holder.createdate.setText(arrayKala.get(position).CreateDate);
        holder.date1.setText("تاریخ سرویس : "+arrayKala.get(position).Date1);

        holder.service_name.setText("سرویس "+arrayKala.get(position).ServiceName);
       // OrderDetailsAdapter adapter=new OrderDetailsAdapter(activity,arrayKala.get(position).Services);
        //holder.ls.setAdapter(adapter);

        //setListViewHeightBasedOnItems(holder.ls);
        final Typeface myfont2 = Typeface.createFromAsset(activity.getAssets(), "Regular.ttf");
        final Typeface myfont = Typeface.createFromAsset(activity.getAssets(), "BYekan.ttf");
        holder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        final AlertDialog.Builder mydialogBuilder = new AlertDialog.Builder(activity);
                        //dialogBuilder.setCancelable(false);

                        final Typeface face = Typeface.createFromAsset(activity.getAssets(),
                                "IRANSans.ttf");


                        LayoutInflater inflater = activity.getLayoutInflater();
                        View mydialog = inflater.inflate(R.layout.oder_details_custom_dialog, null);
                        mydialogBuilder.setView(mydialog);
                        Button mybutton3 = (Button) mydialog.findViewById(R.id.button3);
                        TextView title = (TextView) mydialog.findViewById(R.id.title);
                        final ListView ls = (ListView) mydialog.findViewById(R.id.ls);

                        if(arrayKala.get(position).Services==null || arrayKala.get(position).Services.size()==0)
                            ls.setAdapter(null);
                        else {
                            OrderDetailsAdapter adapter = new OrderDetailsAdapter(activity, arrayKala.get(position).Services);
                            ls.setAdapter(adapter);
                        }
                        mybutton3.setTypeface(myfont);
                        title.setTypeface(myfont);
                        final AlertDialog myalertDialog = mydialogBuilder.create();
                        mybutton3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myalertDialog.dismiss();
                            }
                        });
                        myalertDialog.show();
                    }
                });

        return rowView;
    }
    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                float px = 500 * (listView.getResources().getDisplayMetrics().density);
                item.measure(View.MeasureSpec.makeMeasureSpec((int)px, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);
            // Get padding
            int totalPadding = listView.getPaddingTop() + listView.getPaddingBottom();

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight + totalPadding;
            listView.setLayoutParams(params);
            listView.requestLayout();


            return true;

        } else {
            return false;
        }

    }

}
