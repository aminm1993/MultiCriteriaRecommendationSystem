package ir.mohammadpour.app.ui.adapter;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.List;

import ir.mohammadpour.app.R;
import ir.mohammadpour.app.model.ServiceModel;
import ir.mohammadpour.app.model.Shop;
import ir.mohammadpour.app.ui.activity.Service2Activity;
import me.grantland.widget.AutofitTextView;

public class ShopAdapter extends BaseAdapter {

    private Activity activity;

    List<Shop> arrayKala;

    private static LayoutInflater inflater = null;

    public ShopAdapter(Activity a, List<Shop> arrkala) {

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
        TextView item_name;
        TextView item_price_strike;
        TextView phone;
        TextView address;
        ImageView icon;
        boolean isBackVisible=false;
    }
    SharedPreferences prefs;

    public View getView(final int position, final View convertView, ViewGroup parent) {

        prefs = PreferenceManager.getDefaultSharedPreferences(activity);

        //if(convertView!=null)
         //   return convertView;

        final Holder holder=new Holder();
        final View rowView = inflater.inflate(R.layout.shop_item2, null);
        holder.icon=(ImageView)rowView.findViewById(R.id.item_image);
        holder.item_name=(TextView) rowView.findViewById(R.id.name) ;
        holder.phone=(TextView) rowView.findViewById(R.id.phone) ;
        holder.address=(TextView) rowView.findViewById(R.id.address) ;
        holder.item_price_strike=(TextView)rowView.findViewById(R.id.item_price_strike);
        //holder.item_address=(TextView) rowView.findViewById(R.id.item_address) ;
        //holder.item_phone=(TextView) rowView.findViewById(R.id.item_phone) ;

        final ImageLoader imageLoader = ImageLoader.getInstance();
        final String url=  prefs.getString("Server_MainUrl", null);
        imageLoader.displayImage(url+"/profiles/" + arrayKala.get(position).getImgUrl(), holder.icon, new ImageLoadingListener() {
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
                        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.fade_in2);
                        holder.icon.setVisibility(View.VISIBLE);
                        holder.icon.startAnimation(animation);



                        //holder.icon.setVisibility(View.VISIBLE);
                        //AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(activity,R.anim.animation_move);
                        //set.setTarget(holder.icon);
                        //set.start();
                    }

                    @Override
                    public void onLoadingCancelled() {

                    }
                }
        );
        final LinearLayout imgFront = (LinearLayout)rowView.findViewById(R.id.fav_grid_single_Front);
        final LinearLayout imgBack = (LinearLayout) rowView.findViewById(R.id.fav_grid_single_Back);
        holder.address.setText(arrayKala.get(position).getAddress());
        holder.phone.setText(arrayKala.get(position).getPhone());






        holder.item_price_strike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimatorSet setRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(activity,
                        R.anim.flip_right_out);


                AnimatorSet setLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(activity,
                        R.anim.flight_left_in);

                if(!holder.isBackVisible){
                    imgBack.setVisibility(View.VISIBLE);
                    setRightOut.setTarget(imgFront);
                    setLeftIn.setTarget(imgBack);
                    setRightOut.start();
                    setLeftIn.start();
                    holder.isBackVisible = true;
                }
                else{
                    imgBack.setVisibility(View.GONE);
                    setRightOut.setTarget(imgBack);
                    setLeftIn.setTarget(imgFront);
                    setRightOut.start();
                    setLeftIn.start();
                    holder.isBackVisible = false;
                }
            }

        });
        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String[] a={url+"/profiles/" + arrayKala.get(position).getImgUrl()};
                final View rowView2 = inflater.inflate(R.layout.my_overlay, null);
                TextView txt_name=(TextView)rowView2.findViewById(R.id.name);
                TextView txt_address=(TextView)rowView2.findViewById(R.id.address);
                TextView txt_phone=(TextView)rowView2.findViewById(R.id.phone);

                txt_name.setText(arrayKala.get(position).getName());
                txt_address.setText(arrayKala.get(position).getAddress());
                txt_phone.setText(arrayKala.get(position).getPhone());
                new ImageViewer.Builder(activity, a).setOverlayView(rowView2).setStartPosition(0).show();
                /*new ImageViewer.Builder<>(rowView, a)
                        .setStartPosition(0)
                        .hideStatusBar(true)
                        .allowZooming(true)
                        .allowSwipeToDismiss(true)
                        //.setBackgroundColorRes(colorRes)
                        //.setBackgroundColor(color)
                       // .setImageMargin(margin)
                        //.setImageMarginPx(marginPx)
                        //.setContainerPadding(this, dimen)
                        //.setContainerPadding(this, dimenStart, dimenTop, dimenEnd, dimenBottom)
                        //.setContainerPaddingPx(padding)
                        //.setContainerPaddingPx(start, top, end, bottom)
                        //.setCustomImageRequestBuilder(imageRequestBuilder)
                        //.setCustomDraweeHierarchyBuilder(draweeHierarchyBuilder)
                        //.setImageChangeListener(imageChangeListener)
                        //.setOnDismissListener(onDismissListener)
                        //.setOverlayView(overlayView)
                        .show();*/
            }

        });
        holder.item_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimatorSet setRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(activity,
                        R.anim.flip_right_out);


                AnimatorSet setLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(activity,
                        R.anim.flight_left_in);

                if(!holder.isBackVisible){
                    imgBack.setVisibility(View.VISIBLE);
                    setRightOut.setTarget(imgFront);
                    setLeftIn.setTarget(imgBack);
                    setRightOut.start();
                    setLeftIn.start();
                    holder.isBackVisible = true;
                }
                else{
                    imgBack.setVisibility(View.GONE);
                    setRightOut.setTarget(imgBack);
                    setLeftIn.setTarget(imgFront);
                    setRightOut.start();
                    setLeftIn.start();
                    holder.isBackVisible = false;
                }
            }

        });

        holder.phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimatorSet setRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(activity,
                        R.anim.flip_right_out);


                AnimatorSet setLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(activity,
                        R.anim.flight_left_in);


                imgBack.setVisibility(View.GONE);
                setRightOut.setTarget(imgBack);
                setLeftIn.setTarget(imgFront);
                setRightOut.start();
                setLeftIn.start();
                holder.isBackVisible = false;
            }
        });

        holder.address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimatorSet setRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(activity,
                        R.anim.flip_right_out);


                AnimatorSet setLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(activity,
                        R.anim.flight_left_in);

                imgBack.setVisibility(View.GONE);
                setRightOut.setTarget(imgBack);
                setLeftIn.setTarget(imgFront);
                setRightOut.start();
                setLeftIn.start();
                holder.isBackVisible = false;
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimatorSet setRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(activity,
                        R.anim.flip_right_out);


                AnimatorSet setLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(activity,
                        R.anim.flight_left_in);

                imgBack.setVisibility(View.GONE);
                setRightOut.setTarget(imgBack);
                setLeftIn.setTarget(imgFront);
                setRightOut.start();
                setLeftIn.start();
                holder.isBackVisible = false;
            }
        });



        holder.item_name.setText(arrayKala.get(position).getName());
        //holder.item_address.setText(arrayKala.get(position).getAddress());
        //holder.item_phone.setText(arrayKala.get(position).getPhone());

        return rowView;
    }


}
