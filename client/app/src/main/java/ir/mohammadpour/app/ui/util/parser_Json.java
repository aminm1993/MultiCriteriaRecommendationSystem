package ir.mohammadpour.app.ui.util;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import ir.mohammadpour.app.ui.activity.WaitingActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by Amin on 2017-01-02.
 */
public class parser_Json {

   static JSONObject output;
    private class GetJsonByUrl extends AsyncTask<String, String, String> {

        @Override
        protected  void onPreExecute()
        {
        }
        @Override
        protected String doInBackground(String... params) {
            try
            {

                URL url2 = new URL(params[0]);
                BufferedReader in2 = new BufferedReader(new InputStreamReader(url2.openStream(), "UTF-8"));

                String builder ="";
                for (String line = null; (line = in2.readLine()) != null;) {
                    builder+=(line+"\n");
                }

                in2.close();

                return builder;



            } catch (Exception e) {
                return e.getMessage();
            }

        }

        @Override
        protected void onPostExecute(String result) {
            //Here you are done with the task
        }
    }
    public  JSONObject getJSONfromURL(String url) {


        try {
            String x = new GetJsonByUrl().execute(url).get();
             return new JSONObject(x.trim());
        }
        catch(Exception ex){
            return  null;
        }


    }

}