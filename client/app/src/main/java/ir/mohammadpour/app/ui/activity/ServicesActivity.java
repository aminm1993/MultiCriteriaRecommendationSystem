package ir.mohammadpour.app.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.mohammadpour.app.R;
import ir.mohammadpour.app.model.ServiceModel;
import ir.mohammadpour.app.model.SlideShowModel;
import ir.mohammadpour.app.model.SubService;
import ir.mohammadpour.app.ui.adapter.ServicesAdapter;
import ir.mohammadpour.app.ui.util.GetResponseWithAsync;
import ir.mohammadpour.app.ui.widget.CheckUpdate;
import ir.mohammadpour.app.ui.widget.ExpandableHeightGridView;
import ir.mohammadpour.app.ui.widget.slider.Animations.DescriptionAnimation;
import ir.mohammadpour.app.ui.widget.slider.SliderLayout;
import ir.mohammadpour.app.ui.widget.slider.SliderTypes.BaseSliderView;
import ir.mohammadpour.app.ui.widget.slider.SliderTypes.TextSliderView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ServicesActivity extends AppCompatActivity {

    ExpandableHeightGridView lv;
    Activity a;
    ScrollView scrollView1;

    ProgressDialog simpleLoading;
    String Server_MainUrl = "",Server_CityId="";
    SharedPreferences prefs;
    public static List<ServiceModel> models;
    TextView txt;
    Activity activity;
    Context c;
    DrawerLayout mDrawerLayout;
    ImageView btn_menu;
    LinearLayout cardview;
    ImageView taxiicon;
    TextView service_name,service_name2;
    private static final int REQUEST_WRITE_STORAGE = 112;
    String phone = "";
    ImageView imgTaxi;


    private SliderLayout mDemoSlider;
    TextView txt1,txt2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);    // Removes title bar
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.service_layout);
        imgTaxi=(ImageView)findViewById(R.id.imgTaxi);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
           // mDrawerLayout.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            mDrawerLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
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
                    int cx = (mDrawerLayout.getLeft() + mDrawerLayout.getRight()) / 2;
                    int cy = (mDrawerLayout.getTop() + mDrawerLayout.getBottom());
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
        cardview=(LinearLayout)findViewById(R.id.expandableLayout);



        scrollView1=(ScrollView)findViewById(R.id.scrollView1);


        txt1=(TextView)findViewById(R.id.txt1);
        txt2=(TextView)findViewById(R.id.txt2);
        //Typeface homa = Typeface.createFromAsset(getAssets(),"homa.ttf");

        //txt1.setTypeface(homa);
       // txt2.setTypeface(homa);
        service_name=(TextView)findViewById(R.id.service_name);
        service_name2=(TextView)findViewById(R.id.service_name2);
            taxiicon=(ImageView)findViewById(R.id.icon) ;

       // expandableLayout.toggle();
        activity=this;
        prefs= PreferenceManager.getDefaultSharedPreferences(this);


        c=this;
        final ImageLoader imageLoader = ImageLoader.getInstance();
        String url=  prefs.getString("Server_MainUrl", null);

        String Server_Name = prefs.getString("Server_Name", null);
        if(Server_Name!=null && !Server_Name.trim().equals(""))
           txt1.setText("سرویس ویژه اسگاد در "+Server_Name);


        imageLoader.displayImage(url + "/icons/taxi3.png", taxiicon, new ImageLoadingListener() {
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


        cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(activity, SearchPlaceOnMapActivity.class);
                activity.startActivity(i);
            }
        });
        imgTaxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(activity, SearchPlaceOnMapActivity.class);
                activity.startActivity(i);
            }
        });

        txt=(TextView)findViewById(R.id.txt);



        a=this;

        phone = prefs.getString("customer_phone", null);

        Server_MainUrl = prefs.getString("Server_MainUrl", null);
        Server_CityId = prefs.getString("Server_CityId", null);

        simpleLoading = new ProgressDialog(this);
        simpleLoading.setMessage("در حال دریافت اطلاعات");
        simpleLoading.setCancelable(false);
        lv=(ExpandableHeightGridView) findViewById(R.id.lv);
        lv.setExpanded(true);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayShowHomeEnabled(false); // show or hide the default home button
        // ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false);

        //ab.show();

        final TextView appname = (TextView) toolbar.findViewById(R.id.toolbar_title);
        btn_menu=(ImageView)toolbar.findViewById(R.id.btn_menu);

        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.RIGHT);
            }
        });
        //appname.setTypeface(homa);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        if (navigationView != null) {
            setupDrawerContent(navigationView);

            View headerLayout = navigationView.getHeaderView(0);
            TextView passanger_name = (TextView) headerLayout.findViewById(R.id.text_username);
            TextView passanger_money = (TextView) headerLayout.findViewById(R.id.text_money);
            TextView passanger_virtual_money = (TextView) headerLayout.findViewById(R.id.text_virtual_money);
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            //symbols.setGroupingSeparator(',');
            symbols.setDecimalSeparator(',');
            symbols.setGroupingSeparator(',');
            DecimalFormat df = new DecimalFormat();
            df.setDecimalFormatSymbols(symbols);
            df.setGroupingSize(3);
            df.setGroupingUsed(true);

            passanger_virtual_money.setVisibility(View.VISIBLE);

            String p_name = prefs.getString("customer_name", null);
            String p_money = prefs.getString("customer_money", "0");
            String customer_virtual_money = prefs.getString("customer_virtual_money", "0");
            passanger_name.setText(p_name);
            passanger_money.setText("موجودی "+df.format(Long.parseLong(p_money))+" ریال");
            passanger_virtual_money.setText("موجودی طرح اسگاد "+df.format(Long.parseLong(customer_virtual_money))+" ریال");

            //TextView tvHeader =(TextView) headerLayout.findViewById(R.id.nav_header_name);
            //tvHeader.setTypeface(face2);
            Menu m = navigationView.getMenu();
            for (int i = 0; i < m.size(); i++) {
                MenuItem mi = m.getItem(i);

                //for aapplying a font to subMenu ...
                SubMenu subMenu = mi.getSubMenu();
                if (subMenu != null && subMenu.size() > 0) {
                    for (int j = 0; j < subMenu.size(); j++) {
                        MenuItem subMenuItem = subMenu.getItem(j);
                        //applyFontToMenuItem(subMenuItem);
                    }
                }

                //the method we have create in activity
                //applyFontToMenuItem(mi);
            }
        }
        boolean HasShop = prefs.getBoolean("HasShop", false);
        if(HasShop)
             navigationView.getMenu().findItem(R.id.nav_shops).setVisible(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                mDrawerLayout.closeDrawers();
                if (item.getItemId() == R.id.exit) {

                    prefs.edit().remove("customer_name").commit();
                    prefs.edit().remove("customer_phone").commit();
                    prefs.edit().remove("customer_mail").commit();
                    prefs.edit().remove("customer_password").commit();
                    prefs.edit().remove("active").commit();

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


                    prefs.edit().remove("Server_MainUrl").commit();
                    prefs.edit().remove("Server_Name").commit();
                    prefs.edit().remove("Server_Lat").commit();
                    prefs.edit().remove("Server_Lng").commit();
                    prefs.edit().remove("Server_ContactUs").commit();
                    prefs.edit().remove("Server_CityId").commit();


                    Intent i = new Intent(c, LoginActivity.class);
                    startActivity(i);
                    finish();

                }
                else  if(item.getItemId()==R.id.rules)
                {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://scad.ir/rules.php"));
                    startActivity(browserIntent);

                }
                else if(item.getItemId()==R.id.about)
                {
                    startActivity(new Intent(a,AboutActivity.class));
                }
                else if(item.getItemId()==R.id.nav_transactions)
                {
                    startActivity(new Intent(activity,TransactionsActivity.class));
                }
                else if(item.getItemId()==R.id.nav_shops)
                {
                    Intent i =new Intent(ServicesActivity.this,ShopListActivity.class);
                    startActivity(i);
                }
                else if(item.getItemId()==R.id.nav_getdiscount)
                {
                    Intent i =new Intent(ServicesActivity.this,ShareAppActivity.class);
                    i.putExtra("key","test");
                    startActivity(i);
                }

                else if(item.getItemId()==R.id.nav_messages)
                {
                    startActivity(new Intent(activity,MessagesActivity.class));
                }
                else if (item.getItemId() == R.id.nav_friends) {


                    /*
                    String Server_ContactUs = prefs.getString("Server_ContactUs", null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(ServicesActivity.this);
                    builder.setTitle("پشتیبانی");
                    builder.setMessage(Server_ContactUs);
                    //TextView msg = new TextView(a);
                    //msg.setText(Html.fromHtml(Server_ContactUs));
                   // builder.setView(msg);
                    String positiveText = "بستن";
                    builder.setPositiveButton(positiveText,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // positive button logic

                                }
                            });


                    AlertDialog dialog = builder.create();
                    // display dialog
                    dialog.show();*/
                    Intent i=new Intent(c,BackupActivity.class);
                    startActivity(i);
                } else if (item.getItemId() == R.id.nav_home) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(c);
                    LayoutInflater inflater = getLayoutInflater();
                    final View dialog = inflater.inflate(R.layout.etebar_custom_dialog, null);
                    dialogBuilder.setView(dialog);

                    final EditText oldpass = (EditText) dialog.findViewById(R.id.value);
                    final TextView newpass = (TextView) dialog.findViewById(R.id.title);
                    Button button3 = (Button) dialog.findViewById(R.id.button3);
                    TextView title = (TextView) dialog.findViewById(R.id.title);
                    final AlertDialog alertDialog = dialogBuilder.create();
                    button3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //new ChangePassword().execute(ShopId,oldpass.getText().toString(),newpass.getText().toString());
                           /* Intent i = new Intent(v.getContext(), IncreaseEtebarActivity.class);
                            i.putExtra("phone", phone);
                            i.putExtra("value", oldpass.getText().toString());
                            startActivity(i);
                            */

                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Server_MainUrl+"/home/IncreasePassengerMoney?phone="+phone+"&value="+toEnglishNumber(oldpass.getText().toString())));
                            startActivity(browserIntent);
                            alertDialog.dismiss();
                        }
                    });

                    alertDialog.show();
                }else if (item.getItemId() == R.id.sendapp) {
                    ApplicationInfo app = getApplicationContext().getApplicationInfo();
                    String filePath = app.sourceDir;

                    Intent intent = new Intent(Intent.ACTION_SEND);

                    intent.setType("*/*");

                    intent.setPackage("com.android.bluetooth");

                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filePath)));
                    startActivity(Intent.createChooser(intent, "ارسال به دوستان"));
                }
                else if(item.getItemId()==R.id.nav_orders)
                {
                    Intent i=new Intent(ServicesActivity.this,OrderActivity.class);
                    i.putExtra("phone",phone);
                    i.putExtra("url",Server_MainUrl);
                    startActivity(i);
                }
                return false;
            }
        });


        boolean hasPermission = (ContextCompat.checkSelfPermission(activity,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        }
        final int ARROW_MESSAGES2 = 1;
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // Discard other messages
                CheckUpdate checkUpdate=new CheckUpdate();
                checkUpdate.Check(activity,c);

            }
        };
        handler.sendEmptyMessage(ARROW_MESSAGES2);


        String phone= prefs.getString("customer_phone","");
        new GetAllServices().execute(phone);//execute(Server_CityId);





        mDemoSlider = (SliderLayout)findViewById(R.id.slider);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        ViewGroup.LayoutParams params = mDemoSlider.getLayoutParams();
        params.height = height*30/100;

        mDemoSlider.setLayoutParams(params);


        ViewGroup.LayoutParams params2 = imgTaxi.getLayoutParams();
        params2.height = (int)(width/2.5);

        imgTaxi.setLayoutParams(params2);


        new GetAllSlideShows().execute();


    }
    private String toEnglishNumber(String input)
    {
        String[] persian = new String[]{ "۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹" };

        for (int j = 0; j < persian.length; j++) {
            input= input.replace(persian[j],j+"");
        }
        return input;
    }
    private void setupDrawerContent(final NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        //menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
    }
    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "Regular.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypeFaceSpan("", font, this), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);

    }

    Boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed()
    {

        if(mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            //drawer is open
            mDrawerLayout.closeDrawers();
        }

        else
        {

            if (doubleBackToExitPressedOnce) {
               /* Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);*/


               super.onBackPressed();
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    // previously visible view
                    final View myView = mDrawerLayout;

                    // get the center for the clipping circle
                    int cx = myView.getWidth() / 2;
                    int cy = myView.getHeight() / 2;

                    // get the initial radius for the clipping circle
                    float initialRadius = (float) Math.hypot(cx, cy);

                    // create the animation (the final radius is zero)
                    Animator anim =
                            ViewAnimationUtils.createCircularReveal(myView, cx, cy, initialRadius, 0);
                    anim.setDuration(800);
                    // make the view invisible when the animation is done
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            myView.setVisibility(View.INVISIBLE);
                        }
                    });

                    // start the animation
                    anim.start();
                }

            } else {
                this.doubleBackToExitPressedOnce = true;
                //Toast.makeText(this, "جهت خروج یک بار دیگر بزنید", Toast.LENGTH_SHORT).show();
                ShowCustomToast("جهت خروج یک بار دیگر بزنید");
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            }
        }
        // this.requestWindowFeature(Window.FEATURE_NO_TITLE);    // Removes title bar
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void onResume()
    {
        super.onResume();
       // this.requestWindowFeature(Window.FEATURE_NO_TITLE);    // Removes title bar
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    private class GetAllSlideShows extends AsyncTask<String, Void, String> {

        @Override
        protected  void onPreExecute()
        {
            //simpleLoading.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {


                URL url2 = new URL(Server_MainUrl+"/home/GetAllSlideShow");
                BufferedReader in2 = new BufferedReader(new InputStreamReader(url2.openStream(), "UTF-8"));

                String builder = "";
                for (String line = null; (line = in2.readLine()) != null; ) {
                    builder += (line + "\n");
                }

                in2.close();

                return builder;


            } catch (Exception e) {
                return null;
            }

        }

        @Override
        protected void onPostExecute(String result) {
            //simpleLoading.cancel();
            if (result == null)
                return;
            else {
                Log.v("result",result);
                HashMap<String,String> url_maps = new HashMap<String, String>();

                try
                {
                    JSONArray array=new JSONArray(result);
                    for(int i=0;i<array.length();i++)
                    {
                        url_maps.put(array.getJSONObject(i).getString("Description"), Server_MainUrl+array.getJSONObject(i).getString("ImgUrl"));

                    }



                    for(String name : url_maps.keySet()){
                        TextSliderView textSliderView = new TextSliderView(activity);
                        // initialize a SliderLayout
                        textSliderView
                                .description(name)
                                .image(url_maps.get(name))
                                .setScaleType(BaseSliderView.ScaleType.Fit);
                        //.setOnSliderClickListener(this);

                        //add your extra information
                        //textSliderView.bundle(new Bundle());
                        textSliderView.getBundle()
                                .putString("extra",name);

                        mDemoSlider.addSlider(textSliderView);
                    }
                    mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
                    mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                    mDemoSlider.setCustomAnimation(new DescriptionAnimation());
                    mDemoSlider.setDuration(4000);

                }
                catch (JSONException ex){}
               /* Type listType = new TypeToken<ArrayList<SlideShowModel>>(){}.getType();

                List<SlideShowModel> list = new Gson().fromJson(result, listType);
                if(list!=null)
                {
                    for(int i=0;i<list.size();i++)
                    {
                        url_maps.put(list.get(i).Description, Server_MainUrl+ list.get(i).ImgUrl);
                    }
                }


*/


                //String result=  GetResponseWithAsync.GetRequest(Server_MainUrl+"/home/GetAllSlideShow",c);


        /*url_maps.put("Hannibal", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
        url_maps.put("Big Bang Theory", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
        url_maps.put("House of Cards", "http://cdn3.nflximg.net/images/3093/2043093.jpg");
        url_maps.put("Game of Thrones", "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");
*/
       /* HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Hannibal",R.drawable.appicon);
        file_maps.put("Big Bang Theory",R.drawable.appicon);
        file_maps.put("House of Cards",R.drawable.appicon);
        file_maps.put("Game of Thrones", R.drawable.appicon);*/

            }
        }
    }



private class GetAllServices extends AsyncTask<String, Void, String> {

        @Override
        protected  void onPreExecute()
        {
            models=new ArrayList<>();
            simpleLoading.show();
        }
        @Override
        protected String doInBackground(String... params) {
            try
            {

                URL url2 = new URL(Server_MainUrl+"/services/GetAllServices?phone="+params[0]);
                BufferedReader in2 = new BufferedReader(new InputStreamReader(url2.openStream(), "UTF-8"));

                String builder ="";
                for (String line = null; (line = in2.readLine()) != null;) {
                    builder+=(line+"\n");
                }

                in2.close();

                return builder;



            } catch (Exception e) {
                return null;
            }

        }

        @Override
        protected void onPostExecute(String result) {
            simpleLoading.cancel();
            if(result==null)
                return;
            else
            {

                try {
                    JSONArray arr=new JSONArray(result);


                    boolean kollan_yedune_service_hast=true;
                    for (int i=0;i<arr.length();i++)
                    {
                        ServiceModel mymodel=new ServiceModel();
                        mymodel.IconUrl=arr.getJSONObject(i).getString("IconUrl");
                        mymodel.ID=arr.getJSONObject(i).getInt("ID");

                        mymodel.ExtraText=arr.getJSONObject(i).getString("ExtraText");
                        mymodel.ExtraValue=arr.getJSONObject(i).getInt("ExtraValue");
                        mymodel.ExtraValue2=arr.getJSONObject(i).getInt("ExtraValue2");
                        mymodel.ExtraMoneyAfterXPrice=arr.getJSONObject(i).getInt("ExtraMoneyAfterXPrice");
                        mymodel.FirstTime=arr.getJSONObject(i).getBoolean("FirstTime");
                        mymodel.SecondTime=arr.getJSONObject(i).getBoolean("SecondTime");
                        mymodel.FirstTimeText=arr.getJSONObject(i).getString("FirstTimeText");
                        mymodel.FirstTimeText2=arr.getJSONObject(i).getString("FirstTimeText2");
                        mymodel.SecondTimeText=arr.getJSONObject(i).getString("SecondTimeText");
                        mymodel.SecondTimeText2=arr.getJSONObject(i).getString("SecondTimeText2");

                        mymodel.DeActiveServiceText=arr.getJSONObject(i).getString("DeActiveServiceText");
                        mymodel.Name=arr.getJSONObject(i).getString("Name");
                        mymodel.Description=arr.getJSONObject(i).getString("Description");
                        mymodel.has_services=arr.getJSONObject(i).getBoolean("has_services");
                        if(mymodel.has_services)
                            kollan_yedune_service_hast=false;
                        try {
                            List<SubService> subServices = new ArrayList<>();
                            JSONArray sub_arr = new JSONArray(arr.getJSONObject(i).getString("Details"));
                            for (int j = 0; j < sub_arr.length(); j++) {
                                SubService mysubmodel = new SubService();


                                mysubmodel.ID = sub_arr.getJSONObject(j).getInt("ID");
                                mysubmodel.Name = sub_arr.getJSONObject(j).getString("Name");
                                mysubmodel.ImgUrl = Server_MainUrl+ sub_arr.getJSONObject(j).getString("ImgUrl");
                                mysubmodel.CategoryId=sub_arr.getJSONObject(j).getInt("CategoryId")+"";
                                mysubmodel.Name = sub_arr.getJSONObject(j).getString("Name");
                                mysubmodel.Price = sub_arr.getJSONObject(j).getDouble("Price");
                                mysubmodel.Unit = sub_arr.getJSONObject(j).getString("Unit");
                                mysubmodel.Description = sub_arr.getJSONObject(j).getString("Description");
                                if(mysubmodel.Description==null)
                                    mysubmodel.Description="";
                                subServices.add(mysubmodel);
                            }
                            mymodel.Details = subServices;

                            if(mymodel.has_services) {

                                String Server_Name = prefs.getString("Server_Name", null);
                               /* if(Server_Name.contains("تهران")) {
                                    //cardview.setVisibility(View.GONE);
                                    // txt.setVisibility(View.VISIBLE);
                                    if (mymodel.Name.contains("کتاب"))
                                    {
                                        ketab_model=mymodel;

                                        final ImageLoader imageLoader = ImageLoader.getInstance();
                                        String url=  prefs.getString("Server_MainUrl", null);
                                        imageLoader.displayImage(url + "/icons/"+ mymodel.IconUrl, taxiicon, new ImageLoadingListener() {
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


                                        cardview.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                //Intent i = new Intent(activity, SearchPlaceOnMapActivity.class);
                                                //activity.startActivity(i);

                                                Intent i = new Intent(activity, Service2Activity.class);
                                                i.putExtra("index", -1);
                                                startActivity(i);
                                            }
                                        });

                                        service_name.setText(mymodel.Name);
                                        service_name2.setText("سامانه خرید آنلاین کتاب");
                                    }
                                    else
                                    {
                                        models.add(mymodel);
                                    }


                                }
                                else*/
                                {
                                    models.add(mymodel);
                                }
                            }
                        }
                        catch (JSONException ex2){
                            if(mymodel.has_services)
                            {

                                String Server_Name = prefs.getString("Server_Name", null);
                               /* if(Server_Name.contains("تهران")) {
                                    //cardview.setVisibility(View.GONE);
                                    // txt.setVisibility(View.VISIBLE);
                                    if (mymodel.Name.contains("کتاب"))
                                    {
                                        ketab_model=mymodel;

                                        final ImageLoader imageLoader = ImageLoader.getInstance();
                                        String url=  prefs.getString("Server_MainUrl", null);
                                        imageLoader.displayImage(url + "/icons/"+ mymodel.IconUrl, taxiicon, new ImageLoadingListener() {
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


                                        cardview.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                //Intent i = new Intent(activity, SearchPlaceOnMapActivity.class);
                                                //activity.startActivity(i);

                                                Intent i = new Intent(activity, Service2Activity.class);
                                                i.putExtra("index", -1);
                                                activity.startActivity(i);
                                            }
                                        });

                                        service_name.setText(mymodel.Name);
                                        service_name2.setText("سامانه خرید آنلاین کتاب");
                                    }
                                    else
                                    {
                                        models.add(mymodel);
                                    }


                                }
                                else*/
                                {
                                    models.add(mymodel);
                                }
                            }
                        }


                    }

                    if(kollan_yedune_service_hast)
                    {
                        Intent i=new Intent(a,SearchPlaceOnMapActivity.class);
                        startActivity(i);
                        finish();
                    }


                    if(models==null || models.size()==0)
                        lv.setAdapter(null);
                    else {



                        ServicesAdapter adapter = new ServicesAdapter(a, models);
                        lv.setAdapter(adapter);
                        //adapter.notifyDataSetChanged();
                        //scrollView1.fullScroll(ScrollView.FOCUS_UP);
                        scrollView1.scrollTo(0,0);
                    }
                }
                catch (JSONException ex){

                }


            }
            cardview.setVisibility(View.VISIBLE);
        }


    }
   public static ServiceModel ketab_model=new ServiceModel();
    public void ShowCustomToast(String msg) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.toast_layout_root));

        ImageView image = (ImageView) layout.findViewById(R.id.image);
        //image.setImageResource(R.drawable.android);
        //Typeface type = Typeface.createFromAsset(this.getAssets(),"IRANSans.ttf");
        TextView text = (TextView) layout.findViewById(R.id.text);
        //text.setTypeface(type);
        text.setText(msg);

        final Toast toast = new Toast(this);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 800);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
