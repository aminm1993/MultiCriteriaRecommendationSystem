package ir.mohammadpour.app.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ir.mohammadpour.app.R;
import ir.mohammadpour.app.model.ScheduleModel;
import ir.mohammadpour.app.ui.activity.GetOrderTimeAndAddressActivity;

/**
 * Created by Amin on 2017-08-12.
 */
public class SubScheduleListAdapter2 extends RecyclerView.Adapter<SubScheduleListAdapter2.Holder> {

    Activity activity;
    List<ScheduleModel.Time>  Times;
    String DateStr="";
    public SubScheduleListAdapter2(Activity a, List<ScheduleModel.Time> _Times,String _DateStr) {

        activity = a;
        DateStr=_DateStr;
        Times=_Times;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    private static LayoutInflater inflater = null;

    @Override
    public long getItemId(int position) {
        return position;
    }
    class Holder extends RecyclerView.ViewHolder
    {
        TextView time;
        public Holder(View itemView) {
            super(itemView);


            time = (TextView) itemView.findViewById(R.id.time);

            Typeface type = Typeface.createFromAsset(itemView.getContext().getAssets(),"Regular.ttf");

            time.setTypeface(type);
        }
    }
    @Override
    public int getItemCount() {
        return Times.size();
    }
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sub_schedule_item, parent, false);


        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {

        holder.time.setText(" ساعت "+Times.get(position).Time);
        holder.time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int index=position;
                GetOrderTimeAndAddressActivity.SetTime(DateStr+" ساعت "+Times.get(index).Time,Times.get(index).Date);
            }
        });

    }

}
