package ir.mohammadpour.app.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import ir.mohammadpour.app.R;

public class GozinehaActivity extends AppCompatActivity {

    String price="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        setContentView(R.layout.activity_gozineha);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            price = extras.getString("price");
        }

    }
}
