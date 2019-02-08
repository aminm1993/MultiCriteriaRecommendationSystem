package ir.mohammadpour.app.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Update;
import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import ir.mohammadpour.app.R;
import ir.mohammadpour.app.model.AddressModel;
import ir.mohammadpour.app.model.SelectedAddressModel;
import ir.mohammadpour.app.ui.activity.Sabeghe;
import ir.mohammadpour.app.ui.activity.SelectedLocationActivity;

public class AddressAdapter extends BaseAdapter {

    private Activity activity;

    List<SelectedAddressModel> arrayKala;

    private static LayoutInflater inflater = null;

    String state="";
    public AddressAdapter(Activity a, List<SelectedAddressModel> arrkala,String state) {

        activity = a;

        this.state=state;
        arrayKala = arrkala;

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
        TextView name;
        TextView name2;
        TextView description;

        Button b;
        ImageView edit;
        ImageView delete;
    }


    public View getView(final int position, final View convertView, ViewGroup parent) {



        if(convertView!=null)
            return convertView;

        final Holder holder=new Holder();
        final View rowView;
        rowView = inflater.inflate(R.layout.address_item, null);

        holder.edit=(ImageView) rowView.findViewById(R.id.edit) ;
        holder.delete=(ImageView)rowView.findViewById(R.id.delete) ;
        holder.name=(TextView)rowView.findViewById(R.id.name) ;
        holder.name2=(TextView)rowView.findViewById(R.id.name2) ;
        holder.description=(TextView)rowView.findViewById(R.id.description) ;
        holder.b=(Button) rowView.findViewById(R.id.choose) ;

        Typeface type = Typeface.createFromAsset(rowView.getContext().getAssets(),"IRANSans.ttf");
        holder.name2.setTypeface(type);
        holder.name.setTypeface(type);
        holder.description.setTypeface(type);
        holder.b.setTypeface(type);

        holder.name.setText(arrayKala.get(position).Name);
        holder.name2.setText("آدرس : "+arrayKala.get(position).Name);
        holder.description.setText("توضیحات : "+arrayKala.get(position).description);

        if(state.equals("mabda"))
        {
            holder.b.setText("انتخاب به عنوان مبدا");
        }
        else //if(state.equals("maghsad"))
        {
            holder.b.setText("انتخاب به عنوان مقصد");
        }


        holder.b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.getIntent().putExtra("res",arrayKala.get(position).lat+"~"+arrayKala.get(position).lng );
                activity.getIntent().putExtra("name",arrayKala.get(position).Name);
                activity.getIntent().putExtra("desc",arrayKala.get(position).description );
                activity.setResult(activity.RESULT_OK, activity.getIntent());
                activity.finish();

            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(rowView.getContext());
                builder.setTitle("");
                builder.setMessage("آیا از حذف آدرس مطمئن هستید ؟");

                String positiveText = "حذف آدرس";
                builder.setPositiveButton(positiveText,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // positive button logic


                                new Delete().from(SelectedAddressModel.class).where("lng =?",arrayKala.get(position).lng).execute();
                                arrayKala.get(position).delete();
                                notifyDataSetChanged();
                                SelectedLocationActivity x=(SelectedLocationActivity) activity;
                                x.RefreshList();
                            }
                        });


                builder.setCancelable(false);
                builder.setNegativeButton("انصراف", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                // display dialog
                dialog.show();

            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Typeface face = Typeface.createFromAsset(rowView.getContext().getAssets(),
                        "IRANSans.ttf");
                final Typeface face2 = Typeface.createFromAsset(rowView.getContext().getAssets(),
                        "SOGAND.ttf");

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(rowView.getContext());
                LayoutInflater inflater = activity.getLayoutInflater();
                final View dialog = inflater.inflate(R.layout.add_address_custom_dialog, null);
                dialogBuilder.setView(dialog);

                final EditText name = (EditText) dialog.findViewById(R.id.editname);
                final EditText desc = (EditText) dialog.findViewById(R.id.editdesc);
                final TextView title = (TextView) dialog.findViewById(R.id.title);
                Button button3 = (Button) dialog.findViewById(R.id.button3);
                title.setText("ویرایش آدرس");
                name.setText(arrayKala.get(position).Name);
                desc.setText(arrayKala.get(position).description);
                title.setTypeface(face2);
                button3.setTypeface(face);
                button3.setText("ویرایش آدرس");
                name.setTypeface(face);
                desc.setTypeface(face);
                final AlertDialog alertDialog = dialogBuilder.create();
                button3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.v("mylng",arrayKala.get(position).lng);
                        new Update(SelectedAddressModel.class)
                                .set("Name = ?", name.getText().toString())
                                //.set("Name = "+name.getText().toString())
                                .where("lng = ?",arrayKala.get(position).lng)
                                .execute();
                        new Update(SelectedAddressModel.class)
                                .set("description = ?", desc.getText().toString())
                                //.set("description = "+desc.getText().toString())
                                .where("lng = ?",arrayKala.get(position).lng)
                                .execute();


                        Toast.makeText(rowView.getContext(),"آدرس شما با موفقیت ویرایش شد",Toast.LENGTH_LONG).show();
                        notifyDataSetChanged();
                        SelectedLocationActivity x=(SelectedLocationActivity) activity;
                        x.RefreshList();
                        alertDialog.dismiss();

                    }
                });

                alertDialog.show();
            }
        });
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
