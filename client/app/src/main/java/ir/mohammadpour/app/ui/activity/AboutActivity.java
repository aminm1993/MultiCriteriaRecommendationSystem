package ir.mohammadpour.app.ui.activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import ir.mohammadpour.app.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AboutActivity extends AppCompatActivity {

    TextView txt_version_app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2_about);

        txt_version_app=(TextView)findViewById(R.id.txt_version_app);

        PackageManager manager = this.getPackageManager();
        try
        {
            PackageInfo info = manager.getPackageInfo(
                    this.getPackageName(), 0);
            String version = info.versionName+"";
            txt_version_app.setText(version);

        }
        catch (PackageManager.NameNotFoundException ex){

        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
