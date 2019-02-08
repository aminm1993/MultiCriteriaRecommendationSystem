package ir.mohammadpour.app.ui.adapter;
        import java.text.DecimalFormat;
        import java.text.DecimalFormatSymbols;
        import java.util.ArrayList;
        import java.util.List;

        import android.app.Activity;
        import android.content.Context;
        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.Paint;
        import android.graphics.Typeface;
        import android.sax.StartElementListener;
        import android.support.v4.app.FragmentPagerAdapter;
        import android.support.v4.view.ViewPager;
        import android.support.v7.widget.CardView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
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
        import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

        import ir.mohammadpour.app.R;
        import ir.mohammadpour.app.model.SubService;
        import ir.mohammadpour.app.ui.activity.DetailActivity;
        import ir.mohammadpour.app.ui.activity.Service2Activity;
        import ir.mohammadpour.app.ui.util.Consts;
        import me.grantland.widget.AutofitTextView;

public class CategoryAdapter extends BaseAdapter {

    private Activity activity;

    List<SubService> arrayKala;

    private static LayoutInflater inflater = null;
    //ImageLoader imageLoader;
    int type2=0; //0 -> dotayi ya setayi | 1 -> yedunei

    int index=-1;
    public CategoryAdapter(Activity a, List<SubService> arrkala,int _index, int t) {

        activity = a;
        this.type2=t;
        arrayKala = arrkala;

        index=_index;
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
        AutofitTextView name;
        TextView count;
        ImageView mosbat;
        ImageView manfi;
        TextView price;
        TextView price_strike;
        //TextView id;
        TextView unit;
        ImageView img;
        CardView itemCard;

        CardView counterCartView;
        CardView itemCard2;
        LinearLayout linearLayout1;
    }


    public View getView(final int position, View convertView, ViewGroup parent) {



        if(convertView!=null)
            return convertView;
         final Holder holder = new Holder();

            if (type2 == 1)
                convertView = inflater.inflate(R.layout.deal_item_linear2, null);
                //convertView = inflater.inflate(R.layout.deal_item_linear, null);
            else {
                convertView = inflater.inflate(R.layout.deal_item2, null);
               // holder.counterCartView=(CardView)convertView.findViewById(R.id.counterCartView);
                //holder.itemCard2=(CardView)convertView.findViewById(R.id.itemCard2);
                //holder.linearLayout1=(LinearLayout)convertView.findViewById(R.id.linearLayout1);
            }
        holder.counterCartView=(CardView)convertView.findViewById(R.id.counterCartView);
        holder.itemCard2=(CardView)convertView.findViewById(R.id.itemCard2);
        holder.linearLayout1=(LinearLayout)convertView.findViewById(R.id.linearLayout1);


            holder.unit=(TextView)convertView.findViewById(R.id.unit);
            holder.name = (AutofitTextView) convertView.findViewById(R.id.item_name);
            holder.count = (TextView) convertView.findViewById(R.id.count);
            holder.price = (TextView) convertView.findViewById(R.id.item_price);
            holder.price_strike = (TextView) convertView.findViewById(R.id.item_price_strike);
        //Typeface type = Typeface.createFromAsset(convertView.getContext().getAssets(),"Regular.ttf");
        //holder.price.setTypeface(type);
        //holder.price_strike.setTypeface(type);
        for(int i = 0; i< Consts.arr2.size(); i++)
        {
            Boolean added=false;
            if(arrayKala.get(position).ID==Consts.arr2.get(i).ID)
            {
                holder.count.setText(Consts.arr_count.get(i).toString());
                //if(type2!=1)
                {
                    holder.counterCartView.setVisibility(View.VISIBLE);
                    holder.itemCard2.setVisibility(View.GONE);
                    holder.linearLayout1.setBackgroundResource(R.drawable.cardview_border);
                }
                added=true;
            }
            if(!added)
                holder.count.setText("1");
        }

            //holder.price_strike.setVisibility(View.GONE);
            //holder.id = (TextView) convertView.findViewById(R.id.item_store);
            holder.img = (ImageView) convertView.findViewById(R.id.item_image);
            holder.mosbat = (ImageView) convertView.findViewById(R.id.mosbat);
            holder.manfi = (ImageView) convertView.findViewById(R.id.manfi);



            //holder.id.setTypeface(type);
        /*if(arrayKala.get(position).Name.length()>30)
        {
            holder.name.setText(arrayKala.get(position).Name.substring(0,26)+"...");
        }
        else*/

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        //symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator(',');
        DecimalFormat df = new DecimalFormat();
        df.setDecimalFormatSymbols(symbols);
        df.setGroupingSize(3);
        df.setGroupingUsed(true);



            holder.name.setText(arrayKala.get(position).Name);

            holder.unit.setText("واحد : "+ arrayKala.get(position).Unit);
            holder.price.setText((df.format((int)arrayKala.get(position).Price))+" ریال");
            /*if (arrayKala.get(position).Price_Strike!=0) {
                holder.price_strike.setText(arrayKala.get(position).Price_Strike + " تومان");
                holder.price_strike.setPaintFlags(holder.price_strike.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }*/
            //holder.id.setText(arrayKala.get(position).ID+"");
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

            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TextView tv_id=(TextView) view.findViewById(R.id.item_store);
                    //Toast.makeText(view.getContext(),tv_id.getText(),Toast.LENGTH_LONG).show();
                    Intent i = new Intent(activity, DetailActivity.class);
                    i.putExtra("id", arrayKala.get(position).ID+"");
                    i.putExtra("parant_id", index+"");
                    i.putExtra("count", holder.count.getText().toString());
                    i.putExtra("img", arrayKala.get(position).ImgUrl);
                    i.putExtra("unit", arrayKala.get(position).Unit);
                    i.putExtra("name", arrayKala.get(position).Name);
                    i.putExtra("desc", arrayKala.get(position).Description);
                    i.putExtra("price", arrayKala.get(position).Price+"");
                    activity.startActivityForResult(i, 1);


                }
            });
          /*  holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(activity, DetailActivity.class);
                    i.putExtra("id", arrayKala.get(position).ID+"");
                    i.putExtra("parant_id", index+"");
                    i.putExtra("count", holder.count.getText().toString());
                    i.putExtra("img", arrayKala.get(position).ImgUrl);
                    i.putExtra("name", arrayKala.get(position).Name);
                    i.putExtra("desc", arrayKala.get(position).Description);
                    i.putExtra("price", arrayKala.get(position).Price+"");
                    activity.startActivityForResult(i, 1);
                }
            });
*/
            holder.price_strike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  /*  Intent i = new Intent(activity, CheeseDetailActivity.class);
                    i.putExtra("id", arrayKala.get(position).Id);
                    i.putExtra("img", arrayKala.get(position).imgURL);
                    i.putExtra("name", arrayKala.get(position).Name);
                    i.putExtra("price", arrayKala.get(position).Price);
                    activity.startActivityForResult(i, 1);
                    */
                }
            });
            holder.mosbat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    holder.count.setText((Integer.parseInt(holder.count.getText().toString()) + 1) + "");

                    if(!holder.count.getText().equals("0"))
                        Service2Activity.AddToBasket(index,arrayKala.get(position).ID,Integer.parseInt(holder.count.getText().toString()));
                    else
                        Service2Activity.DeleteBasket(index,arrayKala.get(position).ID);
                  //  BasketFragment viewPagerFragment = (BasketFragment) fragmentPagerAdapter.getItem(2);
                    //viewPagerFragment.AddToBasket(arrayKala.get(position).imgURL, arrayKala.get(position).Name, arrayKala.get(position).Price, arrayKala.get(position).Id, 1);
                }
            });

            holder.manfi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!holder.count.getText().equals("0")) {

                        if(holder.count.getText().equals("1"))
                        {
                            Service2Activity.DeleteBasket(index,arrayKala.get(position).ID);
                            //if(type2!=1)
                            {

                                Animation fadeInAnimation = AnimationUtils.loadAnimation(activity, R.anim.fade_in);

                                holder.counterCartView.setVisibility(View.GONE);
                                holder.itemCard2.setVisibility(View.VISIBLE);
                                holder.linearLayout1.setBackgroundResource(0);

                                holder.itemCard2.startAnimation(fadeInAnimation);
                            }

                        }
                        else {
                            holder.count.setText((Integer.parseInt(holder.count.getText().toString()) - 1) + "");
                            Service2Activity.AddToBasket(index, arrayKala.get(position).ID, Integer.parseInt(holder.count.getText().toString()));
                        }



                    }
                    else
                    {
                        //if(type2!=1)
                        {
                            holder.counterCartView.setVisibility(View.GONE);
                            holder.itemCard2.setVisibility(View.VISIBLE);
                            holder.linearLayout1.setBackgroundResource(0);
                        }
                    }
                   // else
                       // Service2Activity.DeleteBasket(index,position);

                  //  ViewPager viewPager = (ViewPager) activity.findViewById(R.id.view_pager);
                   // FragmentPagerAdapter fragmentPagerAdapter = (FragmentPagerAdapter) viewPager.getAdapter();
                   // BasketFragment viewPagerFragment = (BasketFragment) fragmentPagerAdapter.getItem(2);
                   // viewPagerFragment.RemoveOneFromBasket(arrayKala.get(position).Id);

                }
            });

        holder.price_strike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation fadeInAnimation = AnimationUtils.loadAnimation(activity, R.anim.fade_in);

                    holder.count.setText("1");
                    holder.counterCartView.setVisibility(View.VISIBLE);
                    holder.counterCartView.startAnimation(fadeInAnimation);
                    holder.itemCard2.setVisibility(View.GONE);
                    holder.linearLayout1.setBackgroundResource(R.drawable.cardview_border);
                    holder.linearLayout1.startAnimation(fadeInAnimation);
                Service2Activity.AddToBasket(index,arrayKala.get(position).ID,1);

            }
        });




        holder.price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(holder.itemCard2.getVisibility()==View.VISIBLE) {

                    Animation fadeInAnimation = AnimationUtils.loadAnimation(activity, R.anim.fade_in);

                    holder.count.setText("1");
                    holder.counterCartView.setVisibility(View.VISIBLE);
                    holder.counterCartView.startAnimation(fadeInAnimation);
                    holder.itemCard2.setVisibility(View.GONE);
                    holder.linearLayout1.setBackgroundResource(R.drawable.cardview_border);
                    holder.linearLayout1.startAnimation(fadeInAnimation);
                    Service2Activity.AddToBasket(index,arrayKala.get(position).ID,1);
                }
            }
        });

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(holder.itemCard2.getVisibility()==View.VISIBLE) {

                    Animation fadeInAnimation = AnimationUtils.loadAnimation(activity, R.anim.fade_in);

                    holder.count.setText("1");
                    holder.counterCartView.setVisibility(View.VISIBLE);
                    holder.counterCartView.startAnimation(fadeInAnimation);
                    holder.itemCard2.setVisibility(View.GONE);
                    holder.linearLayout1.setBackgroundResource(R.drawable.cardview_border);
                    holder.linearLayout1.startAnimation(fadeInAnimation);
                    Service2Activity.AddToBasket(index,arrayKala.get(position).ID,1);
                }
            }
        });









            //holder.itemCard = (CardView) convertView.findViewById(R.id.itemCard);
            //holder.itemCard.setBackgroundResource(R.drawable.listviewborder);
            return convertView;

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
