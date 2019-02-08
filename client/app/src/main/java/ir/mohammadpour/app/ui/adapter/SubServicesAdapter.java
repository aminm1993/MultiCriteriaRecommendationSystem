package ir.mohammadpour.app.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.snappingstepper.SnappingStepper;
import com.bigkoo.snappingstepper.listener.SnappingStepperValueChangeListener;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

import ir.mohammadpour.app.R;
import ir.mohammadpour.app.model.ServiceModel;
import ir.mohammadpour.app.model.SubService;
import ir.mohammadpour.app.ui.activity.SubServiceActivity;

public class SubServicesAdapter extends BaseAdapter {

    private Activity activity;

    List<SubService> arrayKala;

    int parentindex=0;
    private static LayoutInflater inflater = null;

    public SubServicesAdapter(Activity a, List<SubService> arrkala,int _parentindex) {

        activity = a;
        arrayKala = arrkala;
        parentindex=_parentindex;
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
        TextView txtSub;
        TextView txtSub2;
        TextView txtSub3;
        CheckBox checkBox;
        TextView description;
        ExpandableRelativeLayout expandableLayout;
        RelativeLayout relative;
        ImageView bottom;
        SnappingStepper stepperCustom;
    }


    public View getView(final int position, final View convertView, ViewGroup parent) {



        if(convertView!=null)
            return convertView;

        final Holder holder=new Holder();
        final View rowView;
        rowView = inflater.inflate(R.layout.sub_service_item, null);

        holder.txtSub=(TextView) rowView.findViewById(R.id.txtSub);
        holder.txtSub2=(TextView) rowView.findViewById(R.id.txtSub2);
        holder.txtSub3=(TextView) rowView.findViewById(R.id.txtSub3);
        holder.description=(TextView) rowView.findViewById(R.id.description);
        holder.expandableLayout=(ExpandableRelativeLayout) rowView.findViewById(R.id.expandableLayout);
        holder.checkBox=(CheckBox) rowView.findViewById(R.id.checkBox);
        holder.relative=(RelativeLayout)rowView.findViewById(R.id.relative);
        holder.bottom=(ImageView) rowView.findViewById(R.id.bottom);
        holder.stepperCustom=(SnappingStepper)rowView.findViewById(R.id.stepperCustom);

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        DecimalFormat df = new DecimalFormat();
        df.setDecimalFormatSymbols(symbols);
        df.setGroupingSize(3);

        holder.txtSub3.setText(arrayKala.get(position).Name);
        holder.txtSub2.setText(df.format(((int)arrayKala.get(position).Price))+" ریال");
        holder.txtSub.setText(arrayKala.get(position).Unit);
        holder.description.setText(arrayKala.get(position).Description);

        Typeface type = Typeface.createFromAsset(rowView.getContext().getAssets(),"IRANSans.ttf");
        Typeface type2 = Typeface.createFromAsset(rowView.getContext().getAssets(),"Regular.ttf");
        holder.txtSub.setTypeface(type);
        holder.txtSub2.setTypeface(type2);
        holder.txtSub3.setTypeface(type);
        holder.description.setTypeface(type);

       /* holder.relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.expandableLayout.toggle();
            }
        });*/
        holder.bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.expandableLayout.toggle();
            }
        });

        holder.stepperCustom.setOnValueChangeListener(new SnappingStepperValueChangeListener() {
            @Override
            public void onValueChange(View view, int value) {
                if(value!=0)
                    SubServiceActivity.AddToBasket(parentindex,position,value);
                else
                    SubServiceActivity.DeleteBasket(parentindex,position);
            }
        });
        return rowView;
    }


}
