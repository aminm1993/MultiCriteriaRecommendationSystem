package ir.mohammadpour.app.ui.activity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.aakira.expandablelayout.Utils;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ir.mohammadpour.app.R;
import ir.mohammadpour.app.model.ServiceModel;
import ir.mohammadpour.app.model.SubService;
import ir.mohammadpour.app.ui.adapter.CategoryAdapter;
import ir.mohammadpour.app.ui.adapter.CategoryAdapter2;
import ir.mohammadpour.app.ui.adapter.SubServicesForDialogAdapter;
import ir.mohammadpour.app.ui.fragment.MySampleFabFragment;
import me.grantland.widget.AutofitTextView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import static ir.mohammadpour.app.ui.util.Consts.*;
public class RestaurantActivity extends AppCompatActivity{


    ImageView imgCat;
    static TextView mablagh;
    static AutofitTextView mablagh2;
    String id="";
    static LinearLayout expandableLayout,linearExpandable;
    int categoryID=0;
    String shop_id="";
    Spinner spinnerBrand;
    String [] spinnerNames;
    Boolean state=false;
    private static GridView gv;
    EditText edtInput;
    //TextView masir;
    Boolean first_search=true;
    ProgressDialog dialog;
    RelativeLayout relative1;
    RelativeLayout relativeSort;
    ImageView img2;
    Context c;
    String Server_MainUrl="",Server_CityId="",phone="";
    ProgressDialog simpleLoading;


    SharedPreferences prefs;


    public static RecyclerView recyclerviewCategory;
    public static LinearLayoutManager layoutManager;
    Spinner addressSpinner;
    TextView orderNotExist;
    Boolean notFirst=false;

    static  ServiceModel serviceModel;
    FloatingActionButton fab;
    MySampleFabFragment dialogFrag;

   static  Activity a;
    static TextView txtCategoryDesc,addressCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState)  {

        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        setContentView(R.layout.activity_service_list2);
        fab=(FloatingActionButton)findViewById(R.id.fab) ;

        txtCategoryDesc=(TextView)findViewById(R.id.txtCategoryDesc);
        linearExpandable=(LinearLayout) findViewById(R.id.linearExpandable);
        expandableLayout=(LinearLayout)findViewById(R.id.expandableLayout);
        /*View view = inflater.inflate(R.layout.category_fragment , container, false);

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        adapter = new Adapter(getActivity().getSupportFragmentManager());
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        if (viewPager != null) {
            setupViewPager();

        }*/

        orderNotExist=(TextView)findViewById(R.id.orderNotExist);
        addressCategory=(TextView)findViewById(R.id.addressCategory);
        imgCat=(ImageView) findViewById(R.id.imgCat);
        imgCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(expandableLayout.getVisibility()==View.VISIBLE)
                    expandableLayout.setVisibility(View.GONE);
                else
                    expandableLayout.setVisibility(View.VISIBLE);
            }
        });
        prefs= PreferenceManager.getDefaultSharedPreferences(this);
        Server_MainUrl = prefs.getString("Server_MainUrl", null);
        Server_CityId= prefs.getString("Server_CityId", null);
        phone= prefs.getString("customer_phone", null);
        orderNotExist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder mydialogBuilder = new AlertDialog.Builder(a);
                //dialogBuilder.setCancelable(false);


                LayoutInflater inflater = a.getLayoutInflater();
                View mydialog = inflater.inflate(R.layout.order_book_custom_dialog, null);
                mydialogBuilder.setView(mydialog);
                Button mybutton3 = (Button) mydialog.findViewById(R.id.button3);
                TextView title = (TextView) mydialog.findViewById(R.id.title);
                final EditText editBookName = (EditText) mydialog.findViewById(R.id.editBookName);
                final EditText editNevisande = (EditText) mydialog.findViewById(R.id.editNevisande);
                final EditText editEntesharat = (EditText) mydialog.findViewById(R.id.editEntesharat);
                final EditText editISBN = (EditText) mydialog.findViewById(R.id.editISBN);
                final EditText editCount = (EditText) mydialog.findViewById(R.id.editCount);
                addressSpinner=(Spinner)mydialog.findViewById(R.id.addressSpinner);

                addressSpinner.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if( /* addressSpinner.getSelectedItemPosition()<0 &&*/  !notFirst) {
                            Intent i = new Intent(v.getContext(), SelectedLocationActivity.class);
                            i.putExtra("state", "maghsad");
                            notFirst=true;
                            startActivityForResult(i, 10);
                        }

                        return false;
                    }
                });

                final AlertDialog myalertDialog = mydialogBuilder.create();
                mybutton3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if(editBookName.getText().toString().trim().equals("") ||
                                editNevisande.getText().toString().trim().equals("") ||
                                editCount.getText().toString().trim().equals("")
                                ||addressSpinner.getSelectedItemPosition()<0
                                )
                        {
                            Toast.makeText(c,"لطفا فیلدهای اجباری را پر بفرمایید",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            String bookname=editBookName.getText().toString();
                            String nevisande=editNevisande.getText().toString();
                            String entesharat=editEntesharat.getText().toString();
                            String isbn=editISBN.getText().toString();
                            String count=editCount.getText().toString();
                            try
                            {
                                bookname= URLEncoder.encode(editBookName.getText().toString(), "utf-8");
                                nevisande= URLEncoder.encode(editNevisande.getText().toString(), "utf-8");
                                entesharat= URLEncoder.encode(editEntesharat.getText().toString(), "utf-8");
                                isbn= URLEncoder.encode(editISBN.getText().toString(), "utf-8");
                                count= URLEncoder.encode(editCount.getText().toString(), "utf-8");
                            }
                            catch (UnsupportedEncodingException ex) {
                                Log.v("Unsupported", ex.getMessage().toString());
                            }
                             new Orderbook().execute(phone,bookname,entesharat,nevisande,isbn,count, URLEncoder.encode(Tozihat));
                        }



                    }
                });
                myalertDialog.show();
            }
        });

        simpleLoading = new ProgressDialog(this);
        simpleLoading.setMessage("در حال ارسال اطلاعات");
        simpleLoading.setCancelable(false);
        a=this;
        c=this;

        recyclerviewCategory=(RecyclerView)findViewById(R.id.recyclerviewCategory);
        recyclerviewCategory.setAdapter(null);
        recyclerviewCategory.setHasFixedSize(true);
         layoutManager = new LinearLayoutManager(c, LinearLayoutManager.HORIZONTAL, false);
        recyclerviewCategory.setLayoutManager(layoutManager);

        mablagh=(TextView)findViewById(R.id.mablagh) ;
       // Typeface type = Typeface.createFromAsset(a.getAssets(),"Regular.ttf");
        //mablagh.setTypeface(type);

        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(mablagh,
                PropertyValuesHolder.ofFloat("scaleX", .9f),
                PropertyValuesHolder.ofFloat("scaleY", .9f));
        scaleDown.setDuration(800);

        scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);

        scaleDown.start();
        img2=(ImageView)findViewById(R.id.img2);
        dialog = new ProgressDialog(c);
        dialog.setMessage("در حال دریافت اطلاعات");
        dialog.setCancelable(true);
        gv = (GridView) findViewById(R.id.recyclerview);


        //edtInput=(EditText)view.findViewById(R.id.edtInput);
        relative1=(RelativeLayout) findViewById(R.id.relative1);
        final TextView price3=(TextView)findViewById(R.id.price3);
        final TextView price4=(TextView)findViewById(R.id.price4);
        final TextView price1=(TextView)findViewById(R.id.price1);
        final TextView price2=(TextView)findViewById(R.id.price2);
        relativeSort=(RelativeLayout) findViewById(R.id.relativeSort);
        //masir=(TextView)view.findViewById(R.id.textViewMasir);
        //spinnerBrand = (Spinner) view.findViewById(R.id.brand);
        //Bundle args = getArguments();
        //categoryID = 37;
        //shop_id=MainActivity.shop_id;
       // if(MainActivity.order_id==null)
          // new GetKala().execute();
       // else
       // {
       //     GetKalaForOrder(MainActivity.failures, shop_id, "ویرایش سفارش اخیر");
       // }

       /* gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Kala item=arr.get(position);
                //TextView tv_id=(TextView) view.findViewById(R.id.item_store);
                //Toast.makeText(view.getContext(),tv_id.getText(),Toast.LENGTH_LONG).show();
                Intent i =new Intent(view.getContext(),CheeseDetailActivity.class);
                i.putExtra("id",item.Id);
                i.putExtra("img",item.imgURL);
                i.putExtra("name",item.Name);
                i.putExtra("price",item.Price);
                startActivity(i);
            }
        });*/
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            index = extras.getInt("index");


            if(index==-1)
                serviceModel=ServicesActivity.ketab_model;

            else
                serviceModel=ServicesActivity.models.get(index);


            if(serviceModel.Details==null || serviceModel.Details.size()==0) {
                gv.setAdapter(null);
            }
            else {
                //SubServicesAdapter adapter = new SubServicesAdapter(this, serviceModel.Details,index);
                temp= Sort(serviceModel.Details);
                adapter = new CategoryAdapter(a, Sort(serviceModel.Details), index, 0);
                categoryID = serviceModel.ID;
                if (serviceModel.Name.contains("کتاب"))
                    orderNotExist.setVisibility(View.VISIBLE);


                gv.setAdapter(adapter);
                new GetBrandsLists().execute();
            }
        }
        else
            finish();



        relative1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(spinnerNames==null || serviceModel.Details.size()==0)
                    return;
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(c,R.style.DialogSlideAnim);
                LayoutInflater inflater =a.getLayoutInflater();
                final View dialog = inflater.inflate(R.layout.filter_custom_dialog, null);
                dialogBuilder.setView(dialog);

                edtInput=(EditText)dialog.findViewById(R.id.etName);
                spinnerBrand = (Spinner) dialog.findViewById(R.id.spinnerbrand);

                ArrayAdapter NoCoreAdapter = new ArrayAdapter(a,
                        R.layout.my_spinner_textview_red, spinnerNames){

                    public View getView(int position, View convertView,ViewGroup parent) {

                        View v = super.getView(position, convertView, parent);

                        ((TextView) v).setTextSize(18);
                        return v;

                    }

                    public View getDropDownView(int position, View convertView,ViewGroup parent) {

                        View v = super.getDropDownView(position, convertView,parent);

                        ((TextView) v).setGravity(Gravity.CENTER);
                        return v;

                    }
                };
                spinnerBrand.setAdapter(NoCoreAdapter);
                spinnerBrand.setSelection(filterSpinnerId);
                spinnerBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                        //new GetAreaLists().execute(spinnerIds[position]);
                        search = new ArrayList<SubService>();
                       /* if(position==0)
                        {
                            for (int i = 0; i < serviceModel.Details.size(); i++) {
                                search.add(serviceModel.Details.get(i));
                            }

                        }
                        else*/
                            {
                            for (int i = 0; i < serviceModel.Details.size(); i++) {
                                if (serviceModel.Details.get(i).CategoryId.equals(spinnerIds[position])) {
                                    search.add(serviceModel.Details.get(i));
                                }
                            }
                        }

                        if(search==null || search.size()==0)
                            gv.setAdapter(null);
                        else {
                            edtInput.setText("");
                            LastKalaArrayFiltered = "search";
                            //search= Sort(search);
                            adapter = new CategoryAdapter(a, Sort(search), index, numCounts);
                            gv.setAdapter(adapter);

                        }
                        filterSpinnerId=position;

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {

                    }

                });

                Button button3=(Button)dialog.findViewById(R.id.btn1);
                //edtInput.setTypeface(face);
                //newpass.setTypeface(face);
                final AlertDialog alertDialog = dialogBuilder.create();
                button3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //new ChangePassword().execute(ShopId,oldpass.getText().toString(),newpass.getText().toString());


                        if(spinnerBrand.getCount()!=0) {
                         /*   if (spinnerBrand.getSelectedItemPosition() == 0) {
                                search = new ArrayList<SubService>();
                                for (int i = 0; i < serviceModel.Details.size(); i++) {
                                    if (serviceModel.Details.get(i).Name.contains(edtInput.getText().toString())) {
                                        search.add(serviceModel.Details.get(i));
                                    }
                                }
                                LastKalaArrayFiltered="search";
                                //search= Sort(search);
                                if(search!=null && search.size()>0 ) {
                                    adapter = new CategoryAdapter(a, Sort(search), index, numCounts);
                                    gv.setAdapter(adapter);
                                }
                                else
                                    gv.setAdapter(null);

                            } else */
                                {
                                 temp = new ArrayList<SubService>();
                                for (int i = 0; i < search.size(); i++) {
                                    if (search.get(i).Name.contains(edtInput.getText().toString())) {
                                        temp.add(search.get(i));
                                    }
                                }
                                LastKalaArrayFiltered="temp";
                                //temp= Sort(temp);
                                if(temp!=null && temp.size()!=0) {
                                    adapter = new CategoryAdapter(a, Sort(temp), index, numCounts);
                                    gv.setAdapter(adapter);
                                }
                                else
                                    gv.setAdapter(null);



                            }
                        }
                        alertDialog.dismiss();
                        a.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                });
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                alertDialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                alertDialog.show();
            }
        });


        relativeSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(c);
                LayoutInflater inflater = a.getLayoutInflater();
                final View dialog = inflater.inflate(R.layout.sort_kala_custom_dialog, null);
                dialogBuilder.setView(dialog);
                RadioButton name=(RadioButton)dialog.findViewById(R.id.name_sort);
                RadioButton price_up_sort=(RadioButton)dialog.findViewById(R.id.price_up_sort);
                RadioButton price_down_sort=(RadioButton)dialog.findViewById(R.id.price_down_sort);

                if(SortType.equals("name"))
                    name.setChecked(true);
                else if(SortType.equals("up"))
                    price_up_sort.setChecked(true);
                else
                    price_down_sort.setChecked(true);
                final AlertDialog alertDialog = dialogBuilder.create();
                name.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked) {
                            SortType = "name";
                            price2.setText("حروف الفبا");
                            if(LastKalaArrayFiltered.equals("main"))
                            {
                                serviceModel.Details=Sort(serviceModel.Details);
                                adapter= new CategoryAdapter(a, serviceModel.Details,index,numCounts);
                            }
                            else if(LastKalaArrayFiltered.equals("search"))
                            {
                                search=Sort(search);
                                adapter= new CategoryAdapter(a, search,index,numCounts);
                            }
                            else
                            {
                                temp=Sort(temp);
                                adapter= new CategoryAdapter(a, temp,index,numCounts);
                            }
                            gv.setAdapter(adapter);

                            alertDialog.dismiss();
                        }
                    }
                });
                price_up_sort.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked) {
                            SortType = "up";
                            price2.setText("زیاد به کم");
                            if(LastKalaArrayFiltered.equals("main"))
                            {
                                //arr=Sort(arr);
                                adapter= new CategoryAdapter(a, Sort(serviceModel.Details),index,numCounts);
                            }
                            else if(LastKalaArrayFiltered.equals("search"))
                            {
                                //search=Sort(search);
                                adapter= new CategoryAdapter(a, Sort(search),index,numCounts);
                            }
                            else
                            {
                                //temp=Sort(temp);
                                adapter= new CategoryAdapter(a, Sort(temp),index,numCounts);
                            }
                            if(adapter==null || adapter.getCount()==0)
                                gv.setAdapter(null);
                            else
                                gv.setAdapter(adapter);
                            alertDialog.dismiss();
                        }
                    }
                });
                price_down_sort.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked) {
                            SortType = "down";
                            price2.setText("کم به زیاد");
                            if(LastKalaArrayFiltered.equals("main"))
                            {
                                //arr=Sort(arr);
                                adapter= new CategoryAdapter(a, Sort(serviceModel.Details),index,numCounts);
                            }
                            else if(LastKalaArrayFiltered.equals("search"))
                            {
                               // search=Sort(search);
                                adapter= new CategoryAdapter(a, Sort(search),index,numCounts);
                            }
                            else
                            {
                                //temp=Sort(temp);
                                adapter= new CategoryAdapter(a, Sort(temp),index,numCounts);
                            }
                            if(adapter==null || adapter.getCount()==0)
                                gv.setAdapter(null);
                            else
                                gv.setAdapter(adapter);
                            alertDialog.dismiss();
                        }
                    }
                });

                //edtInput=(EditText)dialog.findViewById(R.id.etName);
                //edtInput.setTypeface(face);
                //newpass.setTypeface(face);

                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                alertDialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                alertDialog.show();
            }
        });
        search=new ArrayList<>();
        temp=new ArrayList<>();

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClickButton();
            }
        });

        //txt=(TextView) view.findViewById(R.id.categorytest);
      /* final RelativeLayout buttonLayout = (RelativeLayout) view.findViewById(R.id.button);
       final ExpandableRelativeLayout expandableLayout1 = (ExpandableRelativeLayout) view.findViewById(R.id.expandableLayout);

        expandableLayout1.setInterpolator(Utils.createInterpolator(Utils.BOUNCE_INTERPOLATOR));
        expandableLayout1.setListener(new ExpandableLayoutListenerAdapter() {
            @Override
            public void onPreOpen() {
                createRotateAnimator(buttonLayout, 0f, 180f).start();
                //expandState.put(position, true);
                state=true;
            }

            @Override
            public void onPreClose() {
                createRotateAnimator(buttonLayout, 180f, 0f).start();
               // expandState.put(position, false);
                state=false;
            }
        });

        buttonLayout.setRotation(state ? 180f : 0f);
        buttonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onClickButton(expandableLayout1);
            }
        });


        masir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickButton(expandableLayout1);
            }
        });

        edtInput.addTextChangedListener(new TextWatcher() {
             public void afterTextChanged(Editable s) {
                 if(spinnerBrand.getCount()!=0) {
                     if (spinnerBrand.getSelectedItemPosition() == 0) {
                         search = new ArrayList<Kala>();
                         for (int i = 0; i < arr.size(); i++) {
                             if (arr.get(i).Name.contains(edtInput.getText().toString())) {
                                 search.add(arr.get(i));
                             }
                         }
                         adapter = new CategoryAdapter(getActivity(), search,0);
                         gv.setAdapter(adapter);
                     } else {
                         ArrayList<Kala> temp = new ArrayList<Kala>();
                         for (int i = 0; i < search.size(); i++) {
                             if (search.get(i).Name.contains(edtInput.getText().toString())) {
                                 temp.add(search.get(i));
                             }
                         }
                         adapter = new CategoryAdapter(getActivity(), temp,0);
                         gv.setAdapter(adapter);

                     }
                 }
               }

              public void beforeTextChanged(CharSequence s, int start, int count, int after) {
              }

               public void onTextChanged(CharSequence s, int start, int before, int count) {

               }
             }
            );
*/
        mablagh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(last==0) {
                    Toast.makeText(a,"سفارشی ثبت نشده است",Toast.LENGTH_SHORT).show();
                    return;
                }
                final AlertDialog.Builder mydialogBuilder = new AlertDialog.Builder(a);
                //dialogBuilder.setCancelable(false);

                LayoutInflater inflater = a.getLayoutInflater();
                View mydialog = inflater.inflate(R.layout.service_custom_dialog, null);
                mydialogBuilder.setView(mydialog);
                Button mybutton3 = (Button) mydialog.findViewById(R.id.button3);
                TextView title = (TextView) mydialog.findViewById(R.id.title);
                ListView list = (ListView) mydialog.findViewById(R.id.list_service);
                mablagh2= (AutofitTextView) mydialog.findViewById(R.id.mablagh2);
                //Typeface type = Typeface.createFromAsset(a.getAssets(),"Regular.ttf");
               // mablagh2.setTypeface(type);
                if(arr2==null || arr2.size()==0)
                    list.setAdapter(null);
                else {
                    SubServicesForDialogAdapter adapter = new SubServicesForDialogAdapter(a, arr2,index,arr_count);
                    list.setAdapter(adapter);

                }



                final AlertDialog myalertDialog = mydialogBuilder.create();

                DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                //symbols.setGroupingSeparator(',');
                symbols.setDecimalSeparator(',');
                symbols.setGroupingSeparator(',');
                DecimalFormat df = new DecimalFormat();
                df.setDecimalFormatSymbols(symbols);
                df.setGroupingSize(3);
                df.setGroupingUsed(true);
                mablagh2.setText(df.format(last)+" ریال");
                mybutton3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if(last!=0) {
                            String requests="";
                            for(int i=0;i<arr2.size();i++)
                            {
                                if(arr_count.get(i)!=0) {
                                    if (i != arr2.size() - 1)
                                        requests += arr2.get(i).ID + "~" + arr_count.get(i).toString() + "_";
                                    else
                                        requests += arr2.get(i).ID + "~" + arr_count.get(i).toString();
                                }
                            }

                            Intent i=new Intent(v.getContext(),GetOrderTimeAndAddressActivity.class);

                            i.putExtra("serviceID",serviceModel.ID);
                            i.putExtra("requests",requests);
                            i.putExtra("price",getvalue());
                            i.putExtra("extratext",serviceModel.ExtraText);
                            i.putExtra("extravalue",serviceModel.ExtraValue);
                            i.putExtra("extravalue2",serviceModel.ExtraValue2);
                            i.putExtra("ExtraMoneyAfterXPrice",serviceModel.ExtraMoneyAfterXPrice);
                            i.putExtra("FirstTime",serviceModel.FirstTime);
                            i.putExtra("SecondTime",serviceModel.SecondTime);
                            i.putExtra("FirstTimeText",serviceModel.FirstTimeText);
                            i.putExtra("FirstTimeText2",serviceModel.FirstTimeText2);
                            i.putExtra("SecondTimeText",serviceModel.SecondTimeText);
                            i.putExtra("SecondTimeText2",serviceModel.SecondTimeText2);

                            startActivity(i);
                           // new SendRequest().execute(requests);
                        }

                    }
                });

                myalertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        if(adapter!=null && adapter.getCount()!=0) {
                            gv.setAdapter(adapter);
                        }
                    }
                });
                myalertDialog.show();
            }
        });




        if(serviceModel.Description!=null && !serviceModel.Description.equals("") &&  !serviceModel.Description.equals("null")) {
            dialogFrag = MySampleFabFragment.newInstance(serviceModel.Description);
            dialogFrag.setParentFab(fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogFrag.show(getSupportFragmentManager(), dialogFrag.getTag());
                }
            });
        }
        else
            fab.setVisibility(View.GONE);



    }



    public static  void  ChangeCatgory(int pos)
    {

       search = new ArrayList<SubService>();
        /*if(pos==0)
        {
            for (int i = 0; i < serviceModel.Details.size(); i++) {
                search.add(serviceModel.Details.get(i));
            }
            expandableLayout.setVisibility(View.GONE);
        }
        else*/
            {
            for (int i = 0; i < serviceModel.Details.size(); i++) {
                if (serviceModel.Details.get(i).CategoryId.equals(spinnerIds[pos])) {
                    search.add(serviceModel.Details.get(i));
                }
            }


            if(spinnerDescriptions[pos]!=null && !spinnerDescriptions[pos].equals("") && !spinnerDescriptions[pos].equals("null")) {
                txtCategoryDesc.setText(spinnerDescriptions[pos]);

                if(spinnerAddresses[pos]!=null && !spinnerAddresses[pos].equals("") && !spinnerAddresses[pos].equals("null"))
                    addressCategory.setText(spinnerAddresses[pos]);

                linearExpandable.setVisibility(View.VISIBLE);
                expandableLayout.setVisibility(View.VISIBLE);
            }
            else
                linearExpandable.setVisibility(View.GONE);
        }

        LastKalaArrayFiltered="search";
        //search= Sort(search);

        adapter = new CategoryAdapter(a, Sort(search),index,numCounts);
        if(search.size()!=0)
           gv.setAdapter(adapter);
        else
            gv.setAdapter(null);

        filterSpinnerId=pos;
    }
    public static int numCounts=2;
    private static String SortType="name";
    private static List<SubService> Sort(List<SubService> mylist)
    {
        List<SubService> mytemp=new ArrayList<>();
        List<SubService> mytemp2=new ArrayList<>();
        for(int i=0;i<mylist.size();i++)
        {
            mytemp.add(mylist.get(i));
        }

        if(SortType.equals("name"))
        {
            Collections.sort(mytemp);

        }
        else if(SortType.equals("up"))
        {
            for (int i=0;i<mytemp.size();i++)
               for (int j=0;j<mytemp.size();j++)
                {
                    if(mytemp.get(i).Price>mytemp.get(j).Price)
                    {
                        SubService tmp=mytemp.get(i);
                        mytemp.set(i,mytemp.get(j));
                        mytemp.set(j,tmp);

                    }
                }
        }
        else
        {
            for (int i=0;i<mytemp.size();i++)
                for (int j=0;j<mytemp.size();j++)
                {
                    if(mytemp.get(i).Price<mytemp.get(j).Price)
                    {
                        SubService tmp=mytemp.get(i);
                        mytemp.set(i,mytemp.get(j));
                        mytemp.set(j,tmp);

                    }
                }
        }

        for(int i=0;i<mytemp.size();i++)
        {
            Boolean has=false;
            for(int j=0;j<mytemp2.size();j++) {
                if (mytemp.get(i).Name.equals(mytemp2.get(j).Name) &&
                        mytemp.get(i).ImgUrl.equals(mytemp2.get(j).ImgUrl) &&
                        mytemp.get(i).Unit.equals(mytemp2.get(j).Unit) &&
                        mytemp.get(i).Description.equals(mytemp2.get(j).Description) &&
                        mytemp.get(i).Price == mytemp2.get(j).Price) {
                    has = true;
                    // break;
                }
            }
            if(!has)
                mytemp2.add(mytemp.get(i));
        }

        return mytemp2;
    }
    private void onClickButton() {
       // expandableLayout.toggle();
        Animation anim = AnimationUtils.loadAnimation(c,R.anim.fade_out);

        // By default all grid items will animate together and will look like the gridview is
        // animating as a whole. So, experiment with incremental delays as below to get a
        // wave effect.
        anim.setDuration(500);


        gv.setAnimation(anim);




        if(gv.getNumColumns()==1) {
            numCounts=3;
            if(LastKalaArrayFiltered.equals("main"))
            {
                adapter= new CategoryAdapter(a, Sort(serviceModel.Details),index,numCounts);
            }
            else if(LastKalaArrayFiltered.equals("search"))
            {
                adapter= new CategoryAdapter(a, Sort(search),index,numCounts);
            }
            else
            {
                adapter= new CategoryAdapter(a, Sort(temp),index,numCounts);
            }
            if(adapter==null ||adapter.getCount()==0)
                gv.setAdapter(null);
            else
                gv.setAdapter(adapter);

            gv.setNumColumns(numCounts);
            img2.setImageResource(R.drawable.six);

        }
        else if(gv.getNumColumns()==2) {
            numCounts=1;
            if(LastKalaArrayFiltered.equals("main"))
            {
                adapter= new CategoryAdapter(a, Sort(serviceModel.Details),index,numCounts);
            }
            else if(LastKalaArrayFiltered.equals("search"))
            {
                adapter= new CategoryAdapter(a, Sort(search),index,numCounts);
            }
            else
            {
                adapter= new CategoryAdapter(a, Sort(temp),index,numCounts);
            }
            if(adapter==null || adapter.getCount()==0)
                gv.setAdapter(null);
            else
                gv.setAdapter(adapter);

            gv.setNumColumns(numCounts);
            img2.setImageResource(R.drawable.two);
        }
        else
        {
            numCounts=2;
            if(LastKalaArrayFiltered.equals("main"))
            {
                adapter= new CategoryAdapter(a, Sort(serviceModel.Details),index,numCounts);
            }
            else if(LastKalaArrayFiltered.equals("search"))
            {
                adapter= new CategoryAdapter(a, Sort(search),index,numCounts);
            }
            else
            {
                adapter= new CategoryAdapter(a, Sort(temp),index,numCounts);
            }
            if(adapter==null|| adapter.getCount()==0)
                gv.setAdapter(null);
            else {
                gv.setAdapter(adapter);
            }
            gv.setNumColumns(numCounts);
            img2.setImageResource(R.drawable.four);
        }

    }

    @Override
    public void onResume()
    {

        if(adapter!=null && adapter.getCount()!=0) {
          gv.setAdapter(adapter);
        }

        super.onResume();
    }

    public ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return animator;
    }


    @Override
    public void onBackPressed()
    {

        arr2=new ArrayList<>();
        arr_count=new ArrayList<>();
        last=0;
        search=new ArrayList<>();
        temp=new ArrayList<>();
        adapter=null;
        spinnerStates=null;
        txtCategoryDesc=null;
        spinnerIds=null;
        index=-1;
        filterSpinnerId=0;
        last=0;
        LastKalaArrayFiltered="main";
        serviceModel=null;
        layoutManager=null;
        recyclerviewCategory=null;
        gv=null;
        super.onBackPressed();  // optional depending on your needs

    }

String Tozihat="";
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10) {
            if (resultCode == RESULT_OK) //&& data.getExtras().getString("res").equals("1")
            {
                String [] latlng = data.getExtras().getString("res").split("~");
                Tozihat = data.getExtras().getString("desc");
                String Name = data.getExtras().getString("name");

                String[] timestr = new String[2];
                timestr[0] = Name + " , " + Tozihat;
                timestr[1] = "-- تغییر آدرس --";

                ArrayAdapter NoCoreAdapter = new ArrayAdapter(a,
                        R.layout.my_spinner_textview3, timestr) {

                    public View getView(int position, View convertView, ViewGroup parent) {

                        View v = super.getView(position, convertView, parent);

                        ((TextView) v).setTextSize(16);
                        return v;

                    }

                    public View getDropDownView(int position, View convertView, ViewGroup parent) {

                        View v = super.getDropDownView(position, convertView, parent);

                        ((TextView) v).setGravity(Gravity.CENTER);
                        return v;

                    }
                };
                addressSpinner.setAdapter(NoCoreAdapter);
                addressSpinner.setSelection(0);
                notFirst = false;

                addressSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 1) {
                            Intent i = new Intent(view.getContext(), SelectedLocationActivity.class);
                            i.putExtra("state", "maghsad");
                            notFirst = true;
                            startActivityForResult(i, 10);

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


            } else {
                notFirst = false;
            }
        }
        else if(resultCode==1)
        {

        }
    }
    private class GetBrandsLists extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            // progress.show();
        }
        @Override
        protected String doInBackground(String... arg0) {


            try
            {
                URL url = new URL(Server_MainUrl+"/services/GetCategories?serviceid="+categoryID+"&cityid="+Server_CityId);
                // Read all the text returned by the server
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

                String builder ="";
                for (String line = null; (line = in.readLine()) != null;) {
                    builder+=(line+"\n");
                }
                in.close();


                return builder;


            } catch (Exception e) {

            }



            return null;
        }

        @Override
        protected void onPostExecute(String result) {




                if(result==null)
                {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(c);
                    dialog.setMessage("مشکل در اتصال به شبکه");
                    dialog.setPositiveButton("سعی مجدد", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            // TODO Auto-generated method stub
                            new GetBrandsLists().execute();
                        }
                    });
                    dialog.setNegativeButton("انصراف", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            // TODO Auto-generated method stub

                        }
                    });
                    dialog.show();
                    return;
                }


            //Log.v("test",result);
            try {

                JSONArray json;

                result=result.replace("&quot;","\"");
                json = new JSONArray(result);

                spinnerNames=new String[json.length()];
                spinnerStates=new Boolean[json.length()];
                spinnerIds=new String[json.length()];
                spinnerDescriptions=new String[json.length()];
                spinnerAddresses=new String[json.length()];
                for(int i=0;i<json.length();i++) {
                    spinnerNames[i]=json.getJSONObject(i).getString("Name");
                    spinnerIds[i]=json.getJSONObject(i).getString("Id");
                    spinnerAddresses[i]=json.getJSONObject(i).getString("Address");
                    spinnerDescriptions[i]=json.getJSONObject(i).getString("Description");
                    spinnerStates[i]=false;
                }


                if(spinnerNames!=null && spinnerNames.length>0) {
                    ChangeCatgory(0);
                    spinnerStates[0]=true;


                    TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
                    for(int i=0;i<spinnerNames.length;i++)
                        tabLayout.addTab(tabLayout.newTab().setText(spinnerNames[i]));

                    tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                        @Override
                        public void onTabSelected(TabLayout.Tab tab) {

                        }

                        @Override
                        public void onTabUnselected(TabLayout.Tab tab) {

                        }

                        @Override
                        public void onTabReselected(TabLayout.Tab tab) {

                        }
                    });
                }
                CategoryAdapter2 adapter2 = new CategoryAdapter2(spinnerNames,spinnerStates,a);
                recyclerviewCategory.setAdapter(adapter2);


            }
            catch(Exception ec){}

            // progress.dismiss();
        }

    }

    /*
    private void changeTabsFont() {
        Typeface type = Typeface.createFromAsset(getContext().getAssets(),"BYekan.ttf");

        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(type);

                }
            }
        }
    }*/
    /*
    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
*/
/*
    private void setupViewPager() {
        adapter = new Adapter(getActivity().getSupportFragmentManager());
        new GetLists().execute();
    }
*/
    /*
    private class GetLists extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            // progress.show();
        }
        @Override
        protected String doInBackground(String... arg0) {

            try
            {
                URL url = new URL("http://whitedesign.ir/design/api/category");
                // Read all the text returned by the server
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

                String builder ="";
                for (String line = null; (line = in.readLine()) != null;) {
                    builder+=(line+"\n");
                }
                in.close();


                return  builder;


            } catch (Exception e) {

            }



            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            try {
                JSONArray json;

                result = result.replace("&quot;", "\"");
                json = new JSONArray(result);

                for (int i = 0; i < json.length(); i++) {
                    adapter.addFragment(CheeseListFragment.newInstance(json.getJSONObject(i).getInt("id")),json.getJSONObject(i).getString("name"));
                }

                viewPager.setAdapter(adapter);

                tabLayout.setTabTextColors(getResources().getColorStateList(R.color.white));

                tabLayout.setupWithViewPager(viewPager);
                changeTabsFont();
            }
            catch(Exception ex){}
            // progress.dismiss();
        }

    }*/


    public static void AddToBasket(int index,int Id,int count)
    {

        boolean has=false;
        for(int i=0;i<arr2.size();i++)
        {
            if(arr2.get(i).ID== Id) {
                arr_count.set(i, count);
                has=true;
            }
        }
        if(!has)
        {
            for(int i=0;i<serviceModel.Details.size();i++)
            {
                if(serviceModel.Details.get(i).ID== Id) {
                    arr2.add(serviceModel.Details.get(i));
                    arr_count.add(count);
                }
            }
        }

        if(count==0)
            DeleteBasket(index,Id);
        showvalue();
    }

    public  static void DeleteBasket(int index,int Id)
    {
        for(int i=0;i<arr2.size();i++)
        {
            if(arr2.get(i).ID== Id) {
                arr2.remove(i);
                arr_count.remove(i);
            }
        }


        showvalue();
    }

    int getvalue()
    {
        int val=0;
        for(int i=0;i<arr2.size();i++)
        {
            val+=arr2.get(i).Price*arr_count.get(i);
        }
        return val;
    }
    static void showvalue()
    {
        int val=0;
        for(int i=0;i<arr2.size();i++)
        {
            val+=arr2.get(i).Price*arr_count.get(i);
        }

        startCountAnimation(val);
    }
    public static void startCountAnimation(int newval) {
        ValueAnimator animator = new ValueAnimator();
        int local_last=last;
        last=newval;
        animator.setObjectValues(local_last, newval);
        animator.setDuration(1000);

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        //symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator(',');
        final DecimalFormat df = new DecimalFormat();
        df.setDecimalFormatSymbols(symbols);
        df.setGroupingSize(3);
        df.setGroupingUsed(true);



        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                mablagh.setText(df.format((int) animation.getAnimatedValue()) + "  ریال " +" (تایید و ادامه)");

                if(mablagh2!=null)
                    mablagh2.setText(df.format((int) animation.getAnimatedValue()) + "  ریال ");
            }
        });
        animator.start();
    }


    private class Orderbook extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            // progress.show();
        }
        @Override
        protected String doInBackground(String... arg0) {


            try
            {
                Log.v("url",Server_MainUrl+"/services/orderbook?phone="+arg0[0]
                        +"&bookname="+arg0[1]+"&nasher="+arg0[2]+"&nevisande="+arg0[3]+"&isbn="+arg0[4]+"&count="+arg0[5]
                        +"&address="+arg0[6]);
                URL url = new URL(Server_MainUrl+"/services/orderbook?phone="+arg0[0]
                +"&bookname="+arg0[1]+"&nasher="+arg0[2]+"&nevisande="+arg0[3]+"&isbn="+arg0[4]+"&count="+arg0[5]
                        +"&address="+arg0[6]);
                // Read all the text returned by the server


                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

                String builder ="";
                for (String line = null; (line = in.readLine()) != null;) {
                    builder+=(line+"\n");
                }
                in.close();


                return builder;


            } catch (Exception e) {

            }



            return null;
        }

        @Override
        protected void onPostExecute(String result) {




            if(result==null)
            {
                Toast.makeText(c,"مشکل در اتصال به شبکه",Toast.LENGTH_LONG).show();
              /*  AlertDialog.Builder dialog = new AlertDialog.Builder(c);
                dialog.setMessage("مشکل در اتصال به شبکه");
                dialog.setPositiveButton("سعی مجدد", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        // TODO Auto-generated method stub
                        new GetBrandsLists().execute();
                    }
                });
                dialog.setNegativeButton("انصراف", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        // TODO Auto-generated method stub

                    }
                });
                dialog.show();
                */
                return;
            }

            try {

                if(result.contains("true"))
                {
                    Toast.makeText(c,"تبریک !سفارش شما با موفقیت ثبت شد کارشناسان ما پس از بررسی ، جهت تایید سفارش با شما تماس خواهند گرفت",Toast.LENGTH_LONG).show();
                    finish();
                }
                else
                {
                    Toast.makeText(c,"مشکل در ثبت سفارش",Toast.LENGTH_LONG).show();
                }


            }
            catch(Exception ec){}

            // progress.dismiss();
        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


}