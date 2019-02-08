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

import ir.mohammadpour.app.R;

/**
 * Created by Amin on 2017-02-19.
 */
public class UpdateGooglePlay {
     Activity ac;
    ProgressDialog progressDialog;
    Context con;
    SharedPreferences prefs;
    CircleProgress circle_progress;
    public  void Check(Activity a,Context c)
    {
        ac=a;
        con=c;
        //String Server_MainUrl = prefs.getString("Server_MainUrl", null);
        new ChechUpdate().execute("http://scad.ir/update_google_play.php");


    }

     void Show(final String link, String Description)
    {
        final Typeface face= Typeface.createFromAsset(ac.getAssets(),
                "Regular.ttf");
        final Typeface face2= Typeface.createFromAsset(ac.getAssets(),
                "SOGAND.ttf");
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ac);
        LayoutInflater inflater = ac.getLayoutInflater();
        final View dialog = inflater.inflate(R.layout.update_play_service__custom_dialog, null);

        dialogBuilder.setView(dialog);
        progressDialog = new ProgressDialog(ac);
        progressDialog.setMessage("در حال دریافت اطلاعات");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        circle_progress=(CircleProgress)dialog.findViewById(R.id.circle_progress);
        final TextView txtDescription =(TextView) dialog.findViewById(R.id.txtDescription);
        Button button3=(Button)dialog.findViewById(R.id.button3);
        TextView title=(TextView)dialog.findViewById(R.id.title);
        title.setTypeface(face2);
        button3.setTypeface(face);
        txtDescription.setTypeface(face);
        txtDescription.setText(Description);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String Server_MainUrl = prefs.getString("Server_MainUrl", null);
                //new ChangePassword().execute(ShopId,oldpass.getText().toString(),newpass.getText().toString());
                circle_progress.setVisibility(View.VISIBLE);
                circle_progress.setMax(100);
                UpdateApp  atualizaApp = new UpdateApp();
                atualizaApp.setContext(con);
                atualizaApp.execute("http://scad.ir"+link);
            }
        });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }




    private class ChechUpdate extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            // progress.show();
        }
        @Override
        protected String doInBackground(String... arg0) {


            try
            {
                //Log.v("url","http://taxnet.ir/checkupdate.php?versioncode="+arg0[0]+"&type=0");
                URL url = new URL(arg0[0]);
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


            if(result==null) {

                return;
            }

            Show(result.trim(), "لطفا google play خود را آپدیت کتید");



        }

    }





    public class UpdateApp extends AsyncTask<String,String,String>{
        private Context context;
        public void setContext(Context contextf){
            context = contextf;
        }
        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... arg0) {
            try {
                URL url = new URL(arg0[0]);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
                c.setRequestProperty("Accept","*/*");
                c.setDoOutput(false);
                c.connect();
                int lenghtOfFile = c.getContentLength();
                String PATH = "/mnt/sdcard/Download/";
                File file = new File(PATH);
                file.mkdirs();
                File outputFile = new File(file, arg0[0].hashCode()+".apk");
                if(outputFile.exists()){
                    outputFile.delete();
                }
                FileOutputStream fos = new FileOutputStream(outputFile);

                InputStream is = c.getInputStream();

                byte[] buffer = new byte[1024];
                int len1 = 0;
                long total = 0;
                while ((len1 = is.read(buffer)) != -1) {
                    total += len1;
                    int progress_temp = (int) total * 100 / lenghtOfFile;
                    publishProgress(""+ progress_temp);
                    fos.write(buffer, 0, len1);
                }
                fos.close();
                is.close();

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File("/mnt/sdcard/Download/"+arg0[0].hashCode()+".apk")), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // without this flag android returned a intent error!
                context.startActivity(intent);

                return "true";

            } catch (Exception e) {
               return  null;
            }

        }

        @Override
        protected void onProgressUpdate(String... progress) {
            Log.d("ANDRO_ASYNC",progress[0]);
            circle_progress.setProgress(Integer.parseInt(progress[0]));
            //mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }
        @Override
        protected void onPostExecute(String result) {
            progressDialog.cancel();
            if(result==null)
                Toast.makeText(context,"مشکل در دانلود",Toast.LENGTH_LONG).show();
            else
                Toast.makeText(context,"فایل با موفقیت دانلود شد",Toast.LENGTH_LONG).show();
        }

        }
}
