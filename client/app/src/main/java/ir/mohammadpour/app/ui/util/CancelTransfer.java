package ir.mohammadpour.app.ui.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by Amin on 2017-06-01.
 */
public class CancelTransfer {
    static SharedPreferences prefs;
    static Context c;
    static String Server_MainUrl="";
    static Activity a;
    static ProgressDialog simpleLoading;
    public static void CancelTransfer(Activity ac,Context co)
    {
        a=ac;
        c=co;
        simpleLoading = new ProgressDialog(ac);
        simpleLoading.setMessage("در حال ارسال اطلاعات");
        simpleLoading.setCancelable(false);
        prefs = PreferenceManager.getDefaultSharedPreferences(ac);
        String url = prefs.getString("Server_MainUrl", null);
        Server_MainUrl=url;
        new Cancel().execute(prefs.getString("transfer_id", null));
    }
    private static class Cancel extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            simpleLoading.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                Log.v("url", Server_MainUrl+"/request/CancelTransfer?uniquid=" + params[0]);

                URL url2 = new URL(Server_MainUrl+"/request/CancelTransfer?uniquid=" + params[0]);

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


            if(result==null) {
                simpleLoading.cancel();
                /*AlertDialog.Builder dialog = new AlertDialog.Builder(c);
                dialog.setMessage("مشکل در برقراری ارتباط");
                dialog.setPositiveButton("سعی مجدد", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        // TODO Auto-generated method stub
                        new Cancel().execute(prefs.getString("transfer_id", null));
                    }
                });
                dialog.setNegativeButton("انصراف", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        // TODO Auto-generated method stub
                        a.finish();
                    }
                });
                dialog.show();
                */

               // if(result.contains("true")) {
                    Toast.makeText(c, "مشکل در برقراری ارتباط", Toast.LENGTH_LONG).show();
                    a.finish();
                    return;
               // }

            }
                        //Here you are done with the task
            try {

                if(result.contains("true")) {

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

                    Toast.makeText(c, "سفر شما لغو شد", Toast.LENGTH_LONG).show();

                    a.finish();
                    //if(Integer.parseInt(animator.getAnimatedValue().toString())<Integer.parseInt(result.trim()))
                    //   animator.setObjectValues(Integer.parseInt(animator.getAnimatedValue().toString()),Integer.parseInt(result.trim()));
                    // else {

                }
                else
                {
                    Toast.makeText(c, "سفر شما توسط راننده شروع شده و امکان لغو سفر در این لحظه وجود ندارد", Toast.LENGTH_LONG).show();
                }

            } catch (Exception ex) {
            }


            simpleLoading.cancel();
        }
    }
}
