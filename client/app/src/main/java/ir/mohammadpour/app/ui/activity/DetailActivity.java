/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ir.mohammadpour.app.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import ir.mohammadpour.app.R;

public class DetailActivity extends AppCompatActivity {

    String imgUrl="",name="",id="",price="",desc="",parant_id="-1",mycount="0",unit="";
    TextView price1,price2,price3
            ,name1,name2
            ,id1,id2
            ,desc1,desc2
            ,addToBasket,txtUnit2,txtUnit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        setContentView(R.layout.activity_detail);

        Typeface face= Typeface.createFromAsset(getAssets(),
                "BYekan.ttf");
        Typeface face2= Typeface.createFromAsset(getAssets(),
                "SOGAND.ttf");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final ActionBar ab = getSupportActionBar();
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayShowHomeEnabled(false); // show or hide the default home button
        // ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false);

        TextView appname=(TextView) toolbar.findViewById(R.id.toolbar_title);
        appname.setTypeface(face2);


        addToBasket=(TextView)findViewById(R.id.txtSee_Cart);
        name1=(TextView)findViewById(R.id.txtWeight_Cart);
        name2=(TextView)findViewById(R.id.txtWeightOne_Cart);

        price1=(TextView)findViewById(R.id.txtGheymat_Cart);
        price2=(TextView)findViewById(R.id.txtGheymatOne_Cart);
        price3=(TextView)findViewById(R.id.toman);

        id1=(TextView)findViewById(R.id.txtPost);
        id2=(TextView)findViewById(R.id.txtWeightAll_Cart);

        desc1=(TextView)findViewById(R.id.Texttozihat);
        desc2=(TextView)findViewById(R.id.Tozihat);

        txtUnit2=(TextView)findViewById(R.id.txtUnit2);
        txtUnit=(TextView)findViewById(R.id.txtUnit);

        addToBasket.setTypeface(face);
        name1.setTypeface(face);
        txtUnit2.setTypeface(face);
        txtUnit.setTypeface(face);
        name2.setTypeface(face);
        price1.setTypeface(face);
        price2.setTypeface(face);
        price3.setTypeface(face);
        id1.setTypeface(face);
        id2.setTypeface(face);
        desc1.setTypeface(face);
        desc2.setTypeface(face);

        TextView TextView05=(TextView)findViewById(R.id.TextView05);
        final TextView TxtCount=(TextView)findViewById(R.id.TxtCount);
        final TextView txtGeymatAllOne_Cart=(TextView)findViewById(R.id.txtGeymatAllOne_Cart);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            imgUrl = extras.getString("img");
            id = extras.getString("id");
            price = extras.getString("price");
            name = extras.getString("name");
            desc = extras.getString("desc");
            parant_id=extras.getString("parant_id");
            mycount=extras.getString("count");
            unit=extras.getString("unit");

        }
        txtUnit2.setText(unit);
        name2.setText(name);
        id2.setText(id);
        price2.setText(addPadingToPrice(Double.parseDouble(price)));
        desc2.setText(desc);
        TxtCount.setText(mycount);
        txtGeymatAllOne_Cart.setText(addPadingToPrice(Double.parseDouble(price)*Double.parseDouble(mycount)));

        loadBackdrop();

        TextView05.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                final Dialog d = new Dialog(arg0.getContext());
                d.setTitle("تعداد کالا را وارد کنید");
                d.setContentView(R.layout.number_dialog);
                Button b1 = (Button) d.findViewById(R.id.button1);
                Button b2 = (Button) d.findViewById(R.id.button2);
                final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
                np.setMaxValue(100);
                np.setMinValue(0);
                np.setValue(Integer.parseInt(TxtCount.getText().toString()));
                np.setWrapSelectorWheel(false);
                np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

                    @Override
                    public void onValueChange(NumberPicker arg0, int arg1, int arg2) {
                        // TODO Auto-generated method stub

                    }
                });
                b1.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {

                        NumberFormat n = new DecimalFormat("#.####");
                        TxtCount.setText(String.valueOf(np.getValue()));
                        mycount=String.valueOf(np.getValue());
                        double all=Double.parseDouble(TxtCount.getText().toString())*Double.parseDouble(price);
                        txtGeymatAllOne_Cart.setText(addPadingToPrice(all));
                        d.dismiss();


                    }
                });
                b2.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                    }
                });
                d.show();
            }
        });


        addToBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Service2Activity.AddToBasket(Integer.parseInt(parant_id),Integer.parseInt(id),Integer.parseInt(mycount));

                /*
                Intent intent = new Intent();
                intent.putExtra("addtobasket",imgUrl+"~"+name+"~"+price+"~"+id+"~"+count);
                setResult(RESULT_OK, intent);
                */

                setResult(RESULT_CANCELED);
                finish();



            }
        });
    }
    private void loadBackdrop() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        AppBarLayout appbar = (AppBarLayout) findViewById(R.id.appbar);
       // float heightDp = getResources().getDisplayMetrics().heightPixels / 3;
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams)appbar.getLayoutParams();
        lp.height = width;
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        final ImageView imageView2 = (ImageView) findViewById(R.id.imgPhoto_Cart);
        //imageView.getLayoutParams().height=width;
        //Glide.with(this).load(Cheeses.getRandomCheeseDrawable()).centerCrop().into(imageView);
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(imgUrl, imageView);
        imageLoader.displayImage(imgUrl, imageView2);
    }

    String addPadingToPrice(double price)
    {

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        DecimalFormat df = new DecimalFormat();
        df.setDecimalFormatSymbols(symbols);
        df.setGroupingSize(3);

        return df.format(price);
    }
}
