package ir.mohammadpour.app.ui.activity;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import ir.mohammadpour.app.R;

public class IncreaseEtebarActivity extends AppCompatActivity {

    String phone="",value="";
    Button My_btn;
    SharedPreferences prefs;
    String Server_MainUrl="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        setContentView(R.layout.activity_increase_etebar);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Server_MainUrl= prefs.getString("Server_MainUrl", null);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            phone = extras.getString("phone");
            value = extras.getString("value");

            String url=Server_MainUrl+"/home/IncreasePassengerMoney?phone="+phone+"&value="+value;

            My_btn=(Button)findViewById(R.id.My_btn);
            My_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            Log.v("url",url);
            WebView webView = (WebView) findViewById(R.id.webView2);

            webView.setWebViewClient(new WebViewClient());

            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(url);
        }
        else
           finish();





    }
}
