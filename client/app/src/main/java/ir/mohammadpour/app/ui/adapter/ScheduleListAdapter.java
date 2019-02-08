package ir.mohammadpour.app.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import ir.mohammadpour.app.R;
import ir.mohammadpour.app.model.ScheduleModel;

/**
 * Created by Amin on 2017-08-12.
 */
public class ScheduleListAdapter extends BaseAdapter {

    Activity activity;
    List<ScheduleModel> models;
    public ScheduleListAdapter(Activity a, List<ScheduleModel> _models) {

        activity = a;
        models=_models;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    private static LayoutInflater inflater = null;
    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public Object getItem(int position) {
        return models.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    class Holder
    {
        TextView datestr;
        RecyclerView recyclerview;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.schedule_item, null);

        //holder.datestr = (TextView) rowView.findViewById(R.id.datestr);
        holder.recyclerview = (RecyclerView) rowView.findViewById(R.id.recyclerview);

        Typeface type = Typeface.createFromAsset(rowView.getContext().getAssets(),"BYekan.ttf");

        holder.datestr.setTypeface(type);
        holder.datestr.setText(models.get(position).DateStr);

        holder.recyclerview.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        holder.recyclerview.setLayoutManager(layoutManager);

        //SubScheduleListAdapter myadapter = new SubScheduleListAdapter(activity,models.get(position).Dates,models.get(position).DateStr);
        //holder.recyclerview.setAdapter(myadapter);

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
