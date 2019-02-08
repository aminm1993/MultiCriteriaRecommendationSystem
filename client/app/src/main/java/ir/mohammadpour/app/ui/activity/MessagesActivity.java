package ir.mohammadpour.app.ui.activity;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.activeandroid.query.Select;
import com.activeandroid.util.ReflectionUtils;

import java.util.Collections;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ir.mohammadpour.app.R;
import ir.mohammadpour.app.model.Message;
import ir.mohammadpour.app.model.SelectedAddressModel;
import ir.mohammadpour.app.ui.adapter.MessageAdapter;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MessagesActivity extends AppCompatActivity {

    ListView lv;
    Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        c=this;
        lv=(ListView) findViewById(R.id.lv);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayShowHomeEnabled(false); // show or hide the default home button
        // ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false);

        //ab.show();

        final ImageView back = (ImageView) toolbar.findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessagesActivity.super.onBackPressed();
            }
        });
        try {
            Configuration.Builder configurationBuilder = new Configuration.Builder(this);
            configurationBuilder.addModelClass(Message.class);
            ActiveAndroid.initialize(configurationBuilder.create());
            List<Message> mItems = new Select().from(Message.class).execute();
            if (mItems != null) {
                //mItems = mItems.subList(Math.max(mItems.size() - 10, 0), mItems.size());
                if(mItems.size()!=0) {
                    Collections.reverse(mItems);
                    MessageAdapter adapter = new MessageAdapter(c, mItems);
                    lv.setAdapter(adapter);
                }
                else
                {
                    new SweetAlertDialog(c, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("پیام")
                            .setContentText("شما تاکنون پیامی دریافت نکرده اید")
                            .show();
                }
            }

            else
            {
                new SweetAlertDialog(c, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("پیام")
                        .setContentText("شما تاکنون پیامی دریافت نکرده اید")
                        .show();
            }
        }
        catch (RuntimeException ex)
        {

        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
