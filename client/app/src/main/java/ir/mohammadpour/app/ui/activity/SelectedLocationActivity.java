package ir.mohammadpour.app.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import java.util.List;

import ir.mohammadpour.app.R;
import ir.mohammadpour.app.model.SelectedAddressModel;
import ir.mohammadpour.app.ui.adapter.AddressAdapter;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

public class SelectedLocationActivity extends AppCompatActivity {

    FloatingActionButton address_add;
    ListView ls;
    Activity a;
    String state="";
    List<SelectedAddressModel> mItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        setContentView(R.layout.activity_selected_location);
        com.activeandroid.Configuration.Builder configurationBuilder = new com.activeandroid.Configuration.Builder(this);
        configurationBuilder.addModelClass(SelectedAddressModel.class);
        ActiveAndroid.initialize(configurationBuilder.create());
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            state = extras.getString("state");
        }
        a=this;
        ls=(ListView)findViewById(R.id.list);
        address_add=(FloatingActionButton)findViewById(R.id.address_add);
        address_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ChooseLocationActivity.class);
                startActivity(i);
            }
        });

        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                //getIntent().putExtra("res",mItems.get(position).lat+"~"+mItems.get(position).lng );
                //setResult(RESULT_OK, getIntent());
                //finish();
                Log.v("test",position+"");

            }
        });
        RefreshList();





    }



    @Override
    public void onResume(){
        super.onResume();

        RefreshList();

    }

    public void RefreshList()
    {
        mItems = new Select().from(SelectedAddressModel.class).execute();
        if(mItems==null || mItems.size()==0)
            ls.setAdapter(null);
        else {
            AddressAdapter adapter = new AddressAdapter(a, mItems, state);
            ls.setAdapter(adapter);
        }


        if(mItems==null || mItems.size()==0)
        {
            new MaterialTapTargetPrompt.Builder(SelectedLocationActivity.this)
                    .setTarget(address_add)
                    .setPrimaryText("راهنمای ثبت آدرس")
                    .setSecondaryText("برای ثبت آدرس پیش فرض این دکمه را فشار دهید")
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
    }

}
