package ir.mohammadpour.app.ui.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by Amin on 2017-02-19.
 */
public class GetTavaghofPrice {
    Context con;
    SharedPreferences prefs;
    String id;
    String phone;
    public  String GetPrice(Context c,String _id,String _phone)
    {
        prefs= PreferenceManager.getDefaultSharedPreferences(c);
        id=_id;
        phone=_phone;
        con=c;
        String Server_MainUrl = prefs.getString("Server_MainUrl", null);
        try {
            String result = new Get().execute(Server_MainUrl).get();
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
                URL url = new URL(arg0[0]+"/home/get_tavagof_price?id="+id+"&phone="+phone);
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
