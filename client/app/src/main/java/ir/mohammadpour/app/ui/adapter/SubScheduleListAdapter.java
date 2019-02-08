package ir.mohammadpour.app.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;

import ir.mohammadpour.app.R;
import ir.mohammadpour.app.model.ScheduleModel;
import ir.mohammadpour.app.ui.activity.GetOrderTimeAndAddressActivity;
import me.grantland.widget.AutofitTextView;

/**
 * Created by Amin on 2017-08-12.
 */
public class SubScheduleListAdapter extends RecyclerView.Adapter<SubScheduleListAdapter.Holder> {

    public static int selectedPosition = -1;
    Activity activity;
    List<ScheduleModel> DateStr;
    public SubScheduleListAdapter(Activity a,List<ScheduleModel> _DateStr) {

        activity = a;
        DateStr=_DateStr;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    private static LayoutInflater inflater = null;

    @Override
    public long getItemId(int position) {
        return position;
    }
    class Holder extends RecyclerView.ViewHolder
    {
        TextView daytype;
        AutofitTextView weekday;
        TextView daynumber;
        AutofitTextView month;

        LinearLayout linearLayout;

        public Holder(View itemView) {
            super(itemView);


            daytype = (TextView) itemView.findViewById(R.id.daytype);
            weekday = (AutofitTextView) itemView.findViewById(R.id.weekday);
            daynumber = (TextView) itemView.findViewById(R.id.daynumber);
            month = (AutofitTextView) itemView.findViewById(R.id.month);
            linearLayout=(LinearLayout)itemView.findViewById(R.id.linearLayout);

            Typeface type = Typeface.createFromAsset(itemView.getContext().getAssets(),"BYekan.ttf");
            Typeface type2 = Typeface.createFromAsset(itemView.getContext().getAssets(),"titr.ttf");

            daytype.setTypeface(type);
            weekday.setTypeface(type);
            daynumber.setTypeface(type2);
            month.setTypeface(type);
        }
    }
    @Override
    public int getItemCount() {
        return DateStr.size();
    }
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.schedule_item, parent, false);


        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {

        holder.daytype.setText(DateStr.get(position).daytype);
        holder.daynumber.setText(DateStr.get(position).daynumber);
        holder.weekday.setText(DateStr.get(position).weekday);
        holder.month.setText(DateStr.get(position).month);

        if(selectedPosition==position)
        {
            holder.linearLayout.setBackgroundResource(R.drawable.blue_border_button_inside_blue2);
            holder.daynumber.setTextColor(Color.parseColor("#ffffff"));
            holder.daytype.setTextColor(Color.parseColor("#ffffff"));
            holder.weekday.setTextColor(Color.parseColor("#ffffff"));
            holder.month.setTextColor(Color.parseColor("#ffffff"));
        }
        else
        {
            holder.linearLayout.setBackgroundResource(R.drawable.black_border_button);
            holder.daynumber.setTextColor(Color.parseColor("#000000"));
            holder.daytype.setTextColor(Color.parseColor("#000000"));
            holder.weekday.setTextColor(Color.parseColor("#000000"));
            holder.month.setTextColor(Color.parseColor("#000000"));
        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetOrderTimeAndAddressActivity.ClickDate(position);
                selectedPosition = position;
                notifyDataSetChanged();
            }
        });
        holder.daytype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetOrderTimeAndAddressActivity.ClickDate(position);
                selectedPosition = position;
                notifyDataSetChanged();
            }
        });
        holder.daynumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetOrderTimeAndAddressActivity.ClickDate(position);
                selectedPosition = position;
                notifyDataSetChanged();
            }
        });
        holder.weekday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetOrderTimeAndAddressActivity.ClickDate(position);
                selectedPosition = position;
                notifyDataSetChanged();
            }
        });
        holder.month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetOrderTimeAndAddressActivity.ClickDate(position);
                selectedPosition = position;
                notifyDataSetChanged();
            }
        });

    }

}
