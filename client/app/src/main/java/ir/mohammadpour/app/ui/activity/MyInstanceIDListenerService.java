package ir.mohammadpour.app.ui.activity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by Amin on 2016-12-28.
 */
public class MyInstanceIDListenerService extends FirebaseInstanceIdService {



    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is also called
     * when the InstanceID token is initially generated, so this is where
     * you retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("device id:", refreshedToken);
        // TODO: Implement this method to send any registration to your app's servers.
        saveTokenToPrefs(refreshedToken);
        //Toast.makeText(this,refreshedToken,Toast.LENGTH_SHORT).show();
      //sendRegistrationToServer(refreshedToken);
    }
    private void saveTokenToPrefs(String _token)
    {
        // Access Shared Preferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        // Save to SharedPreferences
        editor.putString("registration_id", _token);
        editor.apply();

        String phone= preferences.getString("customer_phone", null);
        String pass= preferences.getString("customer_password", null);
        String server_address= preferences.getString("Server_MainUrl", null);
        if(phone!=null)
        {
            new SendTokenToServer().execute(phone,pass,_token,server_address);
        }
    }
    private class SendTokenToServer extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

            try
            {
                Log.v("service url:",params[3]+"/home/SetToken?phone="+params[0]+"&password="+params[1]+"&token="+params[2]);
                URL url = new URL(params[3]+"/home/SetToken?phone="+params[0]+"&password="+params[1]+"&token="+params[2]);
                // Read all the text returned by the server
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

                String builder ="";
                for (String line = null; (line = in.readLine()) != null;) {
                    builder+=(line+"\n");
                }
                in.close();

                // Toast.makeText(mContext,builder,Toast.LENGTH_LONG).show();

                return builder;
            } catch (IOException e) {


            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {


            }

    }
}