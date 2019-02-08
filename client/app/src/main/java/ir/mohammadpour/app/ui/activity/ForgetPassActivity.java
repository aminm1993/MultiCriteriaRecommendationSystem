package ir.mohammadpour.app.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import ir.mohammadpour.app.R;
import ir.mohammadpour.app.ui.util.Convert;
import ir.mohammadpour.app.ui.util.GetResponseWithAsync;

public class ForgetPassActivity extends AppCompatActivity {

    Button btnActive;
    EditText phone;
    Context c;
    Activity ac;
    SharedPreferences prefs;
    String [] spinnerNames;
    String [] spinnerIds;
    String [] Lat;
    String [] Lng;
    String [] MainUrl;
    String [] SmsUrl;
    String [] ContactUs;
    Spinner spinnercity;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        setContentView(R.layout.activity_forget_pass);
        c=this;
        ac=this;
        spinnercity=(Spinner)findViewById(R.id.citySpinner);
        prefs = PreferenceManager.getDefaultSharedPreferences(this );
        btnActive=(Button)findViewById(R.id.btnactive);
        phone=(EditText)findViewById(R.id.txtcode);
        String cityresponse= GetResponseWithAsync.GetRequest("http://scad.ir/getcity.php",c);
        ShowCity(cityresponse);
        //final String Server_URL = G.preferences.getString("Server_MainUrl", null);

        String Server_MainUrl = prefs.getString("Server_MainUrl", null);
         progressDialog = new ProgressDialog(c);
        progressDialog.setMessage("در حال دریافت اطلاعات");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        btnActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String response= GetResponseWithAsync.GetRequest(MainUrl[spinnercity.getSelectedItemPosition()]+"/passenger/ForgetPassword?phone="+phone.getText().toString(),c);
                progressDialog.cancel();
                if(response.trim().equals("true"))
                {
                    final Typeface face= Typeface.createFromAsset(ac.getAssets(),
                            "Regular.ttf");
                    final Typeface face2= Typeface.createFromAsset(ac.getAssets(),
                            "SOGAND.ttf");
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(c);
                    LayoutInflater inflater = ac.getLayoutInflater();
                    final View dialog = inflater.inflate(R.layout.forget_password_custom_dialog, null);

                    dialogBuilder.setView(dialog);



                    final EditText code =(EditText) dialog.findViewById(R.id.editCode);
                    Button button3=(Button)dialog.findViewById(R.id.button3);
                    TextView title=(TextView) dialog.findViewById(R.id.title);

                    title.setTypeface(face2);
                    button3.setTypeface(face);
                    button3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //String Server_MainUrl = prefs.getString("Server_MainUrl", null);
                            //new ChangePassword().execute(ShopId,oldpass.getText().toString(),newpass.getText().toString());

                            String myresponse= GetResponseWithAsync.GetRequest(MainUrl[spinnercity.getSelectedItemPosition()]+"/passenger/SetForgetPassword?phone="+phone.getText().toString()+"&code="+code.getText().toString(),c);

                            try{
                                if(myresponse.contains("true"))
                                {

                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putString("customer_phone", phone.getText().toString());
                                    editor.putString("customer_password", code.getText().toString());
                                    editor.putString("Server_MainUrl", MainUrl[spinnercity.getSelectedItemPosition()]);
                                    editor.putString("Server_Name", spinnerNames[spinnercity.getSelectedItemPosition()]);
                                    editor.putString("Server_Lat", Lat[spinnercity.getSelectedItemPosition()]);
                                    editor.putString("Server_Lng", Lng[spinnercity.getSelectedItemPosition()]);
                                    editor.putString("Server_ContactUs", ContactUs[spinnercity.getSelectedItemPosition()]);
                                    editor.putString("Server_CityId", spinnerIds[spinnercity.getSelectedItemPosition()]);
                                    editor.putString("active", "true");

                                    editor.apply();
                                    editor.commit();

                                    Intent i=new Intent(c,SearchPlaceOnMapActivity.class);
                                    startActivity(i);

                                    finish();

                                }
                                else
                                    Toast.makeText(c,"رمز وارد شده صحیح نمی باشد",Toast.LENGTH_LONG).show();
                            }
                            catch(Exception ex){
                                Snackbar.make( btnActive,ex.getMessage(), Snackbar.LENGTH_SHORT).show();
                                //Snackbar.make( btnlogin,result, Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });
                    AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();
                }
                else if(response.trim().equals("false"))
                    Toast.makeText(c,"مشکل در درخواست",Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(c,"مشکل در درخواست ، اینترنت خود را بررسی کنید",Toast.LENGTH_LONG).show();
            }
        });
    }


    void ShowCity(String result)
    {
        try {

           JSONArray json = Convert.StringToJsonArray(result);

            spinnerNames=new String[json.length()];
            Lat=new String[json.length()];
            Lng=new String[json.length()];
            MainUrl=new String[json.length()];
            SmsUrl=new String[json.length()];
            ContactUs=new String[json.length()];
            spinnerIds=new String[json.length()];
            for(int i=0;i<json.length();i++) {
                spinnerNames[i]=json.getJSONObject(i).getString("Name");
                spinnerIds[i]=json.getJSONObject(i).getString("Id");
                Lat[i]=json.getJSONObject(i).getString("Lat");
                Lng[i]=json.getJSONObject(i).getString("Lng");
                MainUrl[i]=json.getJSONObject(i).getString("MainUrl");
                SmsUrl[i]=json.getJSONObject(i).getString("SmsUrl");
                ContactUs[i]=json.getJSONObject(i).getString("ContactUs");
            }


            ArrayAdapter NoCoreAdapter = new ArrayAdapter(c,
                    R.layout.my_spinner_textview, spinnerNames){

                public View getView(int position, View convertView,ViewGroup parent) {

                    View v = super.getView(position, convertView, parent);

                    ((TextView) v).setTextSize(16);
                    ((TextView) v).setTextColor(Color.parseColor("#ffffff"));
                    Typeface font = Typeface.createFromAsset(getAssets(), "IRANSans.ttf");
                    ((TextView) v).setTypeface(font);
                    return v;

                }

                public View getDropDownView(int position, View convertView,ViewGroup parent) {

                    View v = super.getDropDownView(position, convertView,parent);

                    ((TextView) v).setGravity(Gravity.CENTER);
                    //((TextView) v).setTextColor(Color.parseColor("#ffffff"));
                    Typeface font = Typeface.createFromAsset(getAssets(), "IRANSans.ttf");
                    ((TextView) v).setTypeface(font);
                    return v;

                }
            };
            spinnercity.setAdapter(NoCoreAdapter);







        }
        catch(Exception ec){}
    }
}
