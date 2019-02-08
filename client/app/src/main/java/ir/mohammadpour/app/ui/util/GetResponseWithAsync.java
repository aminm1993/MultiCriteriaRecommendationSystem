package ir.mohammadpour.app.ui.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.speech.tts.Voice;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by Amin on 2017-06-07.
 */
public class GetResponseWithAsync {

    static ProgressDialog progress;
    static Context x;
    public static String GetRequest(String url, Context c)
    {
         progress= new ProgressDialog(c);
         progress.setCancelable(false);
         progress.setMessage("در حال دریافت اطلاعات");
        x=c;
        try {
            return new AsyncGetResponse().execute(url).get();
        }
        catch (ExecutionException ex){return "false";}
        catch (InterruptedException ex){return "false";}
    }

    static class AsyncGetResponse extends AsyncTask<String,String,String>
    {
        @Override
        protected  void onPreExecute()
        {
                  progress.show();
            // progress= new ProgressDialog(context);
            // progress.setMessage("Loading");
            //progress.show();
        }
        @Override
        protected String doInBackground(String... params) {
            try
            {
                String myUrl=params[0];


                URL url2 = new URL(myUrl);
                BufferedReader in2 = new BufferedReader(new InputStreamReader(url2.openStream(), "UTF-8"));

                String builder ="";
                for (String line = null; (line = in2.readLine()) != null;) {
                    builder+=(line+"\n");
                }

                in2.close();
                builder=builder.replace("&quot;","\"");
                return builder;



            } catch (Exception e) {
                return null;
            }

        }

        @Override
        protected void onPostExecute(String result) {
            progress.cancel();
        }
    }
}
