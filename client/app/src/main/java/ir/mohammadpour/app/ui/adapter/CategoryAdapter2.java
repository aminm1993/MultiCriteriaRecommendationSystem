package ir.mohammadpour.app.ui.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.util.Log;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import ir.mohammadpour.app.R;
import ir.mohammadpour.app.ui.activity.RestaurantActivity;
import ir.mohammadpour.app.ui.activity.Service2Activity;
import ir.mohammadpour.app.ui.util.Consts;

public class CategoryAdapter2 extends RecyclerView.Adapter<CategoryAdapter2.MyViewHolder> {

    private String[]  horizontalList;
    Boolean [] list2;
    Activity activity;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        LinearLayout linearLayout;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) itemView.findViewById(R.id.name);
            linearLayout= (LinearLayout) itemView.findViewById(R.id.linearLayout);
            //Typeface type = Typeface.createFromAsset(view.getContext().getAssets(),"Regular.ttf");
           // name.setTypeface(type);


        }
    }


    public CategoryAdapter2(String[] arr,Boolean [] arr2,Activity activity) {

        this.horizontalList = arr;
        this.list2=arr2;
        this.activity=activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_item, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.name.setText(horizontalList[position]);

        if(Consts.spinnerStates[position]) {
            holder.linearLayout.setBackgroundResource(R.drawable.blue_border_button_inside_blue2);
            holder.name.setTextColor(Color.parseColor("#ffffff"));
        }
        else
        {
            holder.linearLayout.setBackgroundResource(R.drawable.blue_border_button);
            holder.name.setTextColor(Color.parseColor("#000000"));
        }

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Consts.spinnerStates[holder.getLayoutPosition()])
                {

                    for(int i=0;i<Consts.spinnerStates.length;i++){
                        if(i==position)
                        {
                            Consts.spinnerStates[i]=true;
                        }
                        else
                        {
                            Consts.spinnerStates[i]=false;
                        }

                    }

                    try {
                        Service2Activity.recyclerviewCategory.getAdapter().notifyDataSetChanged();
                        Service2Activity.ChangeCatgory(position);
                    }
                    catch (NullPointerException ex)
                    {
                        RestaurantActivity.recyclerviewCategory.getAdapter().notifyDataSetChanged();
                        RestaurantActivity.ChangeCatgory(position);
                    }
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return this.horizontalList.length;
    }
}