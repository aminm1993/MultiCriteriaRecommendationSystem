package ir.mohammadpour.app.ui.widget;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.CircleProgress;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import ir.mohammadpour.app.R;

/**
 * Created by Amin on 2017-02-19.
 */
public class GetBar {
    Context con;
    String phone;
    SharedPreferences prefs;
    public  String GetBar(Context c,String _phone)
    {
        con=c;
        phone=_phone;
        prefs=PreferenceManager.getDefaultSharedPreferences(c);
        String Server_MainUrl = prefs.getString("Server_MainUrl", null);
        try {
            String result = new Get().execute(Server_MainUrl,phone).get();
            return result.trim();
        }
        catch (ExecutionException ex)
        {
            return "false";
        }
        catch (InterruptedException ex)
        {
            return "false";
        }

    }


    private class Get extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            // progress.show();
        }
        @Override
        protected String doInBackground(String... arg0) {


            try
            {
                //Log.v("url","http://taxnet.ir/checkupdate.php?versioncode="+arg0[0]+"&type=0");
                URL url = new URL(arg0[0]+"/home/getbar?phone="+arg0[1]);
                // Read all the text returned by the server
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

                String builder ="";
                for (String line = null; (line = in.readLine()) != null;) {
                    builder+=(line+"\n");
                }
                in.close();

                builder=builder.replace("&quot;","\"");
                return builder;


            } catch (Exception e) {

            }



            return null;
        }

        @Override
        protected void onPostExecute(String result) {


        }

    }


}
