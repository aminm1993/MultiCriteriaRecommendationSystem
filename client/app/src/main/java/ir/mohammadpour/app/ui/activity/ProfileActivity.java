package ir.mohammadpour.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ir.mohammadpour.app.R;

public class ProfileActivity extends AppCompatActivity {

    TextView myname
    ,myphone
    ,mymail;

    SharedPreferences prefs;
    Button signout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);


        myname=(TextView)findViewById(R.id.title);

        myphone=(TextView)findViewById(R.id.phone);

        mymail=(TextView)findViewById(R.id.mail);

        Typeface type = Typeface.createFromAsset(this.getAssets(),"IRANSans.ttf");
        myname.setTypeface(type);

        myphone.setTypeface(type);

        mymail.setTypeface(type);

        String name= prefs.getString("customer_name", null);
        String phone= prefs.getString("customer_phone", null);
        String mail= prefs.getString("customer_mail", null);

        myname.setText(name);
        myphone.setText(phone);
        mymail.setText(mail);
        /*signout.setVisibility(View.GONE);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                prefs.edit().remove("customer_name").commit();
                prefs.edit().remove("customer_phone").commit();
                prefs.edit().remove("customer_mail").commit();
                finish();
               // Intent intent = new Intent(v.getContext(), MainActivity.class);
                //v.getContext().startActivity(intent);
            }
        });
        */
    }

}
