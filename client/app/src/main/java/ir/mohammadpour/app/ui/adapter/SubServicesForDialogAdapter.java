package ir.mohammadpour.app.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.snappingstepper.SnappingStepper;
import com.bigkoo.snappingstepper.listener.SnappingStepperValueChangeListener;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

import ir.mohammadpour.app.R;
import ir.mohammadpour.app.model.SubService;
import ir.mohammadpour.app.ui.activity.Service2Activity;
import ir.mohammadpour.app.ui.activity.SubServiceActivity;
import me.grantland.widget.AutofitTextView;

public class SubServicesForDialogAdapter extends BaseAdapter {

    private Activity activity;

    List<SubService> arrayKala;
    List<Integer> counts;
    int parentindex=0;
    private static LayoutInflater inflater = null;

    public SubServicesForDialogAdapter(Activity a, List<SubService> arrkala, int _parentindex, List<Integer> _counts) {

        activity = a;
        counts=_counts;
        arrayKala = arrkala;
        parentindex=_parentindex;
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
        AutofitTextView txtSub;
        SnappingStepper stepperCustom;
        ImageView img;
        CardView itemCard;
    }


    public View getView(final int position, final View convertView, ViewGroup parent) {



        if(convertView!=null)
            return convertView;

        final Holder holder=new Holder();
        final View rowView;
        rowView = inflater.inflate(R.layout.sub_service_item_for_dialog, null);
        holder.itemCard=(CardView)rowView.findViewById(R.id.itemCard);


        holder.txtSub=(AutofitTextView) rowView.findViewById(R.id.txtSub);

        holder.img=(ImageView) rowView.findViewById(R.id.img);
        holder.stepperCustom=(SnappingStepper)rowView.findViewById(R.id.stepperCustom);

        holder.txtSub.setText(arrayKala.get(position).Name);

        Typeface type = Typeface.createFromAsset(rowView.getContext().getAssets(),"IRANSans.ttf");
        holder.txtSub.setTypeface(type);

       /* holder.relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.expandableLayout.toggle();
            }
        });*/

        holder.stepperCustom.setValue(counts.get(position));
        holder.stepperCustom.setOnValueChangeListener(new SnappingStepperValueChangeListener() {
            @Override
            public void onValueChange(View view, int value) {
                if(value!=0)
                    //SubServiceActivity.AddToBasket(parentindex,position,value);
                    Service2Activity.AddToBasket(parentindex,arrayKala.get(position).ID,value);
                else
                    //SubServiceActivity.DeleteBasket(parentindex,position);
                    Service2Activity.DeleteBasket(parentindex,arrayKala.get(position).ID);
            }
        });




        final ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(arrayKala.get(position).ImgUrl, holder.img, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted() {

                    }

                    @Override
                    public void onLoadingFailed(FailReason failReason) {
                        String imageUri = "drawable://" + R.drawable.appicon;
                        //arrayKala.get(position).ImgUrl = Consts.SERVER_URL + "/images/shopdefault.png";
                        imageLoader.displayImage(imageUri, holder.img);
                    }

                    @Override
                    public void onLoadingComplete(Bitmap bitmap) {

                    }

                    @Override
                    public void onLoadingCancelled() {
                        String imageUri = "drawable://" + R.drawable.appicon;
                        //arrayKala.get(position).ImgUrl = Consts.SERVER_URL + "/images/shopdefault.png";
                        imageLoader.displayImage(imageUri, holder.img);
                    }
                }
        );


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
