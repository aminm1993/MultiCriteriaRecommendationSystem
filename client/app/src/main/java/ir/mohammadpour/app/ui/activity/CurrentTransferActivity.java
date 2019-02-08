package ir.mohammadpour.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import ir.mohammadpour.app.R;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.blurry.Blurry;

public class CurrentTransferActivity extends AppCompatActivity {

    Button button;
    RelativeLayout relativeLayout;
    SharedPreferences prefs;
    CircleImageView user_profile_photo;
    Context c;
    TextView name,carname,carmodel,phone,pelak;

    String Server_MainUrl="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        setContentView(R.layout.activity_current_transfer);
        c=this;
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Typeface type = Typeface.createFromAsset(this.getAssets(),"IRANSans.ttf");

        button=(Button)findViewById(R.id.button);
        user_profile_photo=(CircleImageView)findViewById(R.id.user_profile_photo);
        relativeLayout=(RelativeLayout)findViewById(R.id.relative);

        name=(TextView) findViewById(R.id.name);
        carname=(TextView) findViewById(R.id.carname);
        carmodel=(TextView) findViewById(R.id.carmodel);
        phone=(TextView) findViewById(R.id.phone);
        pelak=(TextView) findViewById(R.id.pelak);

        name.setTypeface(type);
        carname.setTypeface(type);
        carmodel.setTypeface(type);
        phone.setTypeface(type);
        pelak.setTypeface(type);
        button.setTypeface(type);



        String driver_avatar_path = prefs.getString("driver_avatar_path", null);
        String driver_name = prefs.getString("driver_name", null);
        String driver_car_name = prefs.getString("driver_car_name", null);
        String driver_car_model = prefs.getString("driver_car_model", null);
        String driver_pelak_number = prefs.getString("driver_pelak_number", null);
        String driver_phone = prefs.getString("driver_phone", null);
        Server_MainUrl= prefs.getString("Server_MainUrl", null);

        name.setText("نام راننده : "+driver_name);
        carmodel.setText("مدل خودرو : "+driver_car_model);
        carname.setText("نام خودرو : "+driver_car_name);
        pelak.setText("شماره پلاک : "+driver_pelak_number);
        phone.setText("تلفن راننده : "+driver_phone);


        final ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(Server_MainUrl+"/profiles/"+driver_avatar_path, user_profile_photo, new ImageLoadingListener() {
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





        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs.edit().remove("transfer_id").commit();
                prefs.edit().remove("transfer_state").commit();
                prefs.edit().remove("driver_avatar_path").commit();
                prefs.edit().remove("driver_name").commit();
                prefs.edit().remove("driver_car_name").commit();
                prefs.edit().remove("driver_car_model").commit();
                prefs.edit().remove("driver_pelak_number").commit();
                prefs.edit().remove("driver_phone").commit();
                prefs.edit().remove("payment_type").commit();
                prefs.edit().remove("transfer_price").commit();
                prefs.edit().remove("customer_lat");
                prefs.edit().remove("customer_lng");
                prefs.edit().remove("dest_lat");
                prefs.edit().remove("dest_lng");
                prefs.edit().remove("second_dest_lat");
                prefs.edit().remove("second_dest_lng");
                prefs.edit().remove("wait_time_code");
                prefs.edit().remove("Bar");
                prefs.edit().remove("RaftBargasht");
                //stopService(new Intent(c,TransferService.class));
                finish();
            }
        });
    }
}
