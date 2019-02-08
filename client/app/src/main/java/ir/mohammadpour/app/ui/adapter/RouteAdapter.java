package ir.mohammadpour.app.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Update;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

import ir.mohammadpour.app.R;
import ir.mohammadpour.app.model.RouteModel;
import ir.mohammadpour.app.model.SelectedAddressModel;
import ir.mohammadpour.app.ui.activity.SearchPlaceOnMapActivity;
import ir.mohammadpour.app.ui.activity.SelectedLocationActivity;
import ir.mohammadpour.app.ui.util.SharedClass;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

public class RouteAdapter extends BaseAdapter {

    private Activity activity;
    List<RouteModel.CompleteDirection> root;
    private static LayoutInflater inflater = null;
    OnRouteClickInAdapter onClickInAdapter;
    public RouteAdapter(Activity a, List<RouteModel.CompleteDirection> root) {

        activity = a;
        this.root=root;


        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public interface OnRouteClickInAdapter {
        public void onRouteClickInAdapter(int content);
    }
    public int getCount() {
        return root.size();
    }

    public Object getItem(int position) {
        return root.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
    class Holder
    {
        TextView name;
        TextView distance;
        TextView time;
        FloatingActionButton route;
    }


    static double dist= 1.5;

    public View getView(final int position, final View convertView, ViewGroup parent) {



        if(convertView!=null)
            return convertView;

        final Holder holder=new Holder();
        final View rowView;
        rowView = inflater.inflate(R.layout.route_items, null);

        holder.name=(TextView) rowView.findViewById(R.id.name) ;
        holder.distance=(TextView) rowView.findViewById(R.id.distance);
        holder.time=(TextView)rowView.findViewById(R.id.time) ;
        holder.route=(FloatingActionButton) rowView.findViewById(R.id.route);



        DecimalFormat formatter = new DecimalFormat("#0.0");

        Random random=new Random();

        holder.name.setText("ایستگاه شماره "+ random.nextInt()%100);
        dist = dist+random.nextDouble()%0.5;
        holder.distance.setText("(" +dist +" کیلومتر )");
        holder.time.setText(((int)dist/1)+" دقیقه");
        holder.route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    onClickInAdapter = (OnRouteClickInAdapter) v.getContext();
                } catch (ClassCastException e) {
                    throw new ClassCastException(
                            " must implement OnClickInAdapter");
                }
                onClickInAdapter.onRouteClickInAdapter(position);
            }
        });


        SharedClass sharedClass=new SharedClass(activity);
        if(!sharedClass.getSeeRoute() && position==0)
        {
            sharedClass.setSeeRoute();

            sharedClass.setSeeMarkerAdd();
                new MaterialTapTargetPrompt.Builder(activity)
                        .setTarget(holder.route)
                        .setPrimaryText("راهنما")
                        .setSecondaryText("برای نمایش مسیر دریافت شده از سرور لازم است روی این قسمت بزنید ")
                        .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener()
                        {
                            @Override
                            public void onPromptStateChanged(MaterialTapTargetPrompt prompt, int state)
                            {
                                if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED)
                                {
                                    // User has pressed the prompt target
                                }
                            }
                        })
                        .show();


        }

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
