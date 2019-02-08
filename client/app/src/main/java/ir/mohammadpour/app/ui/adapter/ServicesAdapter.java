package ir.mohammadpour.app.ui.adapter;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Update;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.mohammadpour.app.R;
import ir.mohammadpour.app.model.SelectedAddressModel;
import ir.mohammadpour.app.model.ServiceModel;
import ir.mohammadpour.app.ui.activity.RestaurantActivity;
import ir.mohammadpour.app.ui.activity.SearchPlaceOnMapActivity;
import ir.mohammadpour.app.ui.activity.SelectedLocationActivity;
import ir.mohammadpour.app.ui.activity.Service2Activity;
import ir.mohammadpour.app.ui.activity.SubServiceActivity;
import me.grantland.widget.AutofitTextView;

public class ServicesAdapter extends BaseAdapter {

    private Activity activity;

    List<ServiceModel> arrayKala;

    private static LayoutInflater inflater = null;

    public ServicesAdapter(Activity a, List<ServiceModel> arrkala) {

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
        CardView layout1;
        //LinearLayout layout2;
        AutofitTextView service_name;
        //RelativeLayout rlLayout;
        //ListView subservice_list;
        //ExpandableRelativeLayout expandableLayout;
        //ImageView open;
        ImageView icon;
    }
    SharedPreferences prefs;

    public View getView(final int position, final View convertView, ViewGroup parent) {

        prefs = PreferenceManager.getDefaultSharedPreferences(activity);

        //if(convertView!=null)
         //   return convertView;

        final Holder holder=new Holder();
        final View rowView;
        //rowView = inflater.inflate(R.layout.service_item, null);
        //rowView = inflater.inflate(R.layout.service_item_circle_mode, null);
        rowView = inflater.inflate(R.layout.service_item_material_mode, null);
        holder.icon=(ImageView)rowView.findViewById(R.id.item_image);
        //holder.rlLayout=(RelativeLayout)rowView.findViewById(R.id.rlLayout);
        //if(position%2==1)
         //   holder.rlLayout.setBackgroundColor(Color.parseColor("#FF7098EF"));
        holder.layout1=(CardView) rowView.findViewById(R.id.itemCard) ;
       // holder.layout2=(LinearLayout) rowView.findViewById(R.id.linearLayout1) ;
        holder.service_name=(AutofitTextView) rowView.findViewById(R.id.item_name) ;
        //holder.subservice_list=(ListView) rowView.findViewById(R.id.subservice_list) ;
        //holder.expandableLayout=(ExpandableRelativeLayout) rowView.findViewById(R.id.expandableLayout) ;
        //holder.open=(ImageView) rowView.findViewById(R.id.open) ;


        final ImageLoader imageLoader = ImageLoader.getInstance();
        String url=  prefs.getString("Server_MainUrl", null);
        imageLoader.displayImage(url + "/icons/" + arrayKala.get(position).IconUrl, holder.icon, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted() {

                    }

                    @Override
                    public void onLoadingFailed(FailReason failReason) {
                        // arrayKala.get(position).imgUrl="http://taxnet.ir/images/shopdefault.png";
                        //imageLoader.displayImage("http://taxnet.ir/images/shopdefault.png",relativeLayout);
                    }

                    @Override
                    public void onLoadingComplete(Bitmap bitmap) {
                        //Drawable dr = new BitmapDrawable(bitmap);

                        // relativeLayout.setBackground(dr);
                        //Blurry.with(c).radius(25).sampling(2).onto(relativeLayout);

                    }

                    @Override
                    public void onLoadingCancelled() {

                    }
                }
        );



        if(!arrayKala.get(position).has_services) {
           // holder.layout1.setBackgroundColor(Color.parseColor("#FF98ACD7"));

            if(arrayKala.get(position).DeActiveServiceText.trim().equals(""))
                arrayKala.get(position).DeActiveServiceText="سرویس مورد نظر غیرفعال است";
            holder.layout1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(activity,arrayKala.get(position).DeActiveServiceText,Toast.LENGTH_LONG).show();
                }
            });
            /*holder.layout2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(activity,arrayKala.get(position).DeActiveServiceText,Toast.LENGTH_LONG).show();
                }
            });*/
            holder.service_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(activity,arrayKala.get(position).DeActiveServiceText,Toast.LENGTH_LONG).show();
                }
            });
        }
        Typeface type = Typeface.createFromAsset(rowView.getContext().getAssets(),"IRANSans.ttf");
        holder.service_name.setTypeface(type);
        holder.service_name.setText(arrayKala.get(position).Name);

        if(arrayKala.get(position).has_services) {
            /*
            Log.v("sub_count",arrayKala.get(position).Details.size()+"");
            SubServicesAdapter adapter = new SubServicesAdapter(activity, arrayKala.get(position).Details);
            holder.subservice_list.setAdapter(adapter);


            holder.open.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.expandableLayout.toggle();
                }
            });

            holder.layout1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.expandableLayout.toggle();
                }
            });
            holder.layout2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.expandableLayout.toggle();
                }
            });
            holder.service_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.expandableLayout.toggle();
                }
            });
            */



            holder.layout1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // if(position==0) {

                  //  }
                 //   else
                    if(arrayKala.get(position).Name.contains("غذا"))
                    {
                        Intent i = new Intent(activity, RestaurantActivity.class);
                        i.putExtra("index", position);
                        activity.startActivity(i);
                    }
                    else
                    {
                        //Intent i = new Intent(activity, SubServiceActivity.class);
                        Intent i = new Intent(activity, Service2Activity.class);
                        i.putExtra("index", position);
                        activity.startActivity(i);
                    }
                }
            });
           /* holder.layout2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
             //       if(position==0) {
                 //       Intent i = new Intent(activity, SearchPlaceOnMapActivity.class);
               //         activity.startActivity(i);
               //     }
               //     else
                    {
                        //Intent i = new Intent(activity, SubServiceActivity.class);
                        Intent i = new Intent(activity, Service2Activity.class);
                        i.putExtra("index", position);
                        activity.startActivity(i);
                    }
                }
            });
            */
            holder.service_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  //  if(position==0) {
                  //      Intent i = new Intent(activity, SearchPlaceOnMapActivity.class);
                 //       activity.startActivity(i);
                 //   }
                  //  else

                    if(arrayKala.get(position).Name.contains("غذا"))
                    {
                        Intent i = new Intent(activity, RestaurantActivity.class);
                        i.putExtra("index", position);
                        activity.startActivity(i);
                    }
                    else
                    {
                        //Intent i = new Intent(activity, SubServiceActivity.class);
                        Intent i = new Intent(activity, Service2Activity.class);
                        i.putExtra("index", position);
                        activity.startActivity(i);
                    }
                }
            });
        }

       /* Animation animation = null;
        animation = AnimationUtils.loadAnimation(activity, R.anim.fade_out);
        animation.setDuration(500);
        holder.layout1.startAnimation(animation);
        animation = null;*/
        return rowView;
    }


}
