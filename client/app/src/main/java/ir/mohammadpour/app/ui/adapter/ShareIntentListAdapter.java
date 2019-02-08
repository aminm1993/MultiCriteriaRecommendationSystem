package ir.mohammadpour.app.ui.adapter;

/**
 * Created by Amin on 2017-05-21.
 */
import android.app.Activity;
import android.content.Context;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import ir.mohammadpour.app.R;

public class ShareIntentListAdapter extends BaseAdapter {

    private final Activity context;
    Object[] items;

    public int getCount() {
        return items.length;
    }
    public Object getItem(int position) {
        return items[position];
    }
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }
    private static LayoutInflater inflater = null;
    public ShareIntentListAdapter(Activity context,Object[] items) {

        //super(context, R.layout.social_share, items);
        this.context = context;
        this.items = items;

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }// end HomeListViewPrototype

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        View rowView = inflater.inflate(R.layout.social_share, null, true);
        final Typeface face = Typeface.createFromAsset(context.getAssets(),
                "IRANSans.ttf");
        // set share name
        TextView shareName = (TextView) rowView.findViewById(R.id.shareName);

        shareName.setTypeface(face);
        // Set share image
        ImageView imageShare = (ImageView) rowView.findViewById(R.id.shareImage);

        String [] names=((ResolveInfo)items[position]).activityInfo.applicationInfo.loadLabel(context.getPackageManager()).toString().split(" ");
        // set native name of App to share
        shareName.setText(names[0]);

        // share native image of the App to share
        imageShare.setImageDrawable(((ResolveInfo)items[position]).activityInfo.applicationInfo.loadIcon(context.getPackageManager()));

        return rowView;
    }// end getView

}// end main onCreate