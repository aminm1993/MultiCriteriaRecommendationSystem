package ir.mohammadpour.app.ui.activity;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ir.mohammadpour.app.R;
import ir.mohammadpour.app.model.BackUpMessageModel;
import ir.mohammadpour.app.model.ChatModel;
import ir.mohammadpour.app.model.Shop;
import ir.mohammadpour.app.network.BaseServer;
import ir.mohammadpour.app.network.ServiceGenerator;
import ir.mohammadpour.app.ui.adapter.BackUpAdapter;
import ir.mohammadpour.app.ui.adapter.ShopAdapter;
import ir.mohammadpour.app.ui.util.ConnectionDetector;
import pl.bclogic.pulsator4droid.library.PulsatorLayout;
import retrofit2.Call;
import retrofit2.Callback;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BackupActivity extends AppCompatActivity {

    ListView lv;
    Activity a;
    TextView noMsg;
    List<BackUpMessageModel> list=null;
    ImageButton chatSendButton;
    EditText messageEdit;
    ImageView call;
    SharedPreferences prefs;
    TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        a=this;
        noMsg=(TextView)findViewById(R.id.noMsg);

        lv=(ListView)findViewById(R.id.messagesContainer);
        chatSendButton=(ImageButton)findViewById(R.id.chatSendButton);
        messageEdit=(EditText)findViewById(R.id.messageEdit);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String url=  prefs.getString("Server_MainUrl", null);
        ServiceGenerator.API_BASE_URL=url;
        final String _phone = prefs.getString("customer_phone", null);
        getAllMsgs(_phone);

        chatSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddMsgToBackUp(_phone,messageEdit.getText().toString());
            }
        });

        String Server_ContactUs = prefs.getString("Server_ContactUs", null);

        txt=(TextView)findViewById(R.id.txt);
        txt.setText(Server_ContactUs);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            final LinearLayout rootview = (LinearLayout) findViewById(R.id.rootview);
            //rootview.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            rootview.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    v.removeOnLayoutChangeListener(this);

              /*//BOTTOM RIGHT TO TOP LEFT ANIMATION
                int cx = (framelayout.getLeft() + framelayout.getRight());
                int cy = (framelayout.getTop() + framelayout.getBottom());
                // get the hypothenuse so the radius is from one corner to the other
                int radius = (int) Math.hypot(right, bottom);
                Animator reveal = ViewAnimationUtils.createCircularReveal(v, cx, cy, 0, radius);
                reveal.setInterpolator(new AccelerateDecelerateInterpolator());
                reveal.setDuration(600);
                reveal.start();*/

              /*  //LEFT TOP TO BOTTOM RIGHT ANIMATION
                int cx1 = 0;
                int cy1 = 0;
                // get the hypothenuse so the radius is from one corner to the other
                int radius1 = (int) Math.hypot(right, bottom);
                Animator reveal1 = ViewAnimationUtils.createCircularReveal(v, cx1, cy1, 0, radius1);
                reveal1.setInterpolator(new DecelerateInterpolator(2f));
                reveal1.setDuration(1000);
                reveal1.start();*/

               /* //EFFECT START WITH CENTER
                float finalRadius = (float) Math.hypot(v.getWidth(), v.getHeight());
                int cx1 = (framelayout.getLeft() + framelayout.getRight()) / 2;
                int cy1 = (framelayout.getTop() + framelayout.getBottom()) / 2;
                Animator anim = ViewAnimationUtils.createCircularReveal(v, cx1, cy1, 0, finalRadius);
                anim.setDuration(1000);
                anim.setInterpolator(new AccelerateDecelerateInterpolator());
                anim.start();*/

                    //OPEN WITH BOTTOM CENTER
                    int cx = (rootview.getLeft() + rootview.getRight()) / 2;
                    int cy = (rootview.getTop() + rootview.getBottom());
                    // get the hypothenuse so the radius is from one corner to the other
                    int radius = (int) Math.hypot(right, bottom);
                    Animator reveal = ViewAnimationUtils.createCircularReveal(v, cx, cy, 0, radius);
                    reveal.setInterpolator(new AccelerateDecelerateInterpolator());
                    reveal.setDuration(800);
                    reveal.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            // rootview.setBackgroundResource(R.color.white);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                    reveal.start();


                }
            });
        }

        call = (ImageView) findViewById(R.id.call);
        Animation pulse = AnimationUtils.loadAnimation(this, R.anim.bounce2);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 10);
        pulse.setInterpolator(interpolator);

        call.startAnimation(pulse);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String BackUpPhone = prefs.getString("BackUpPhone", null);
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + BackUpPhone));
                startActivity(intent);
            }
        });
       // PulsatorLayout pulsator = (PulsatorLayout) findViewById(R.id.pulsator);
        //pulsator.start();

        //hideKeyboard();
   }
    class MyBounceInterpolator implements android.view.animation.Interpolator {
        private double mAmplitude = 1;
        private double mFrequency = 10;

        MyBounceInterpolator(double amplitude, double frequency) {
            mAmplitude = amplitude;
            mFrequency = frequency;
        }

        public float getInterpolation(float time) {
            return (float) (-1 * Math.pow(Math.E, -time/ mAmplitude) *
                    Math.cos(mFrequency * time) + 1);
        }
    }
    public void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    private void getAllMsgs(final String phone)
    {
        BaseServer apiService = ServiceGenerator.createService(BaseServer.class);
        if (new ConnectionDetector(a).isConnectingToInternet()) {
            //Connected to the Internet
            Call<List<BackUpMessageModel>> call = apiService.getAllMsgs(phone);
            call.enqueue(new Callback<List<BackUpMessageModel>>() {
                @Override
                public void onResponse(Call<List<BackUpMessageModel>> call, retrofit2.Response<List<BackUpMessageModel>> response) {
                    List<BackUpMessageModel> res = response.body();
                    if (res != null) {
                        list=res;
                        if(res.size()!=0) {
                            BackUpAdapter adapter = new BackUpAdapter(a, res);
                            lv.setAdapter(adapter);
                        }
                        else
                        {
                            lv.setVisibility(View.GONE);
                            noMsg.setVisibility(View.VISIBLE);
                        }
                    }
                    else {

                    }
                }

                @Override
                public void onFailure(Call<List<BackUpMessageModel>> call, Throwable t) {

                    Toast.makeText(a,"مشکل در دریافت اطلاعات",Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            //loading.setVisibility(View.GONE);


            AlertDialog.Builder dialog = new AlertDialog.Builder(a);
            dialog.setMessage("مشکل در برقراری ارتباط");
            dialog.setPositiveButton("سعی مجدد", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    getAllMsgs(phone);
                }
            });
            dialog.setNegativeButton("انصراف", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    finish();
                }
            });
            dialog.show();

        }
    }


    private void AddMsgToBackUp(final String phone,final String msg)
    {
        BaseServer apiService = ServiceGenerator.createService(BaseServer.class);
        if (new ConnectionDetector(a).isConnectingToInternet()) {
            //Connected to the Internet
            Call<String> call = apiService.AddMsgToBackUp(phone,msg);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                    String res = response.body();
                    if (res != null && res.contains("true")) {
                        if(list==null)
                            list=new ArrayList<>();


                        lv.setVisibility(View.VISIBLE);
                        noMsg.setVisibility(View.GONE);

                        BackUpMessageModel my_msg=new BackUpMessageModel();
                        my_msg.who=false;
                        my_msg.date="لحظاتی پیش";
                        my_msg.Message=msg;
                        list.add(my_msg);
                        BackUpAdapter adapter=new BackUpAdapter(a,list);
                        lv.setAdapter(adapter);

                        messageEdit.setText("");

                    }
                    else {

                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                    Toast.makeText(a,"مشکل در دریافت اطلاعات",Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            //loading.setVisibility(View.GONE);


            AlertDialog.Builder dialog = new AlertDialog.Builder(a);
            dialog.setMessage("مشکل در برقراری ارتباط");
            dialog.setPositiveButton("سعی مجدد", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    getAllMsgs(phone);
                }
            });
            dialog.setNegativeButton("انصراف", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    finish();
                }
            });
            dialog.show();

        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
