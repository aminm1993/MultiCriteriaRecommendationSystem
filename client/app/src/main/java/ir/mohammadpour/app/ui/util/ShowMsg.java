package ir.mohammadpour.app.ui.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Trace;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import java.util.ArrayList;

import ir.mohammadpour.app.R;
import ir.mohammadpour.app.ui.activity.Service2Activity;
import ir.mohammadpour.app.ui.activity.ServicesActivity;

/**
 * Created by Amin on 2017-02-19.
 */
public class ShowMsg {



     public static void Show(final Activity a, final String _title, String _description, final Boolean redirect,final Boolean finishing)
    {
        final Typeface titr= Typeface.createFromAsset(a.getAssets(),
                "titr.ttf");
        final Typeface face= Typeface.createFromAsset(a.getAssets(),
                "BYekan.ttf");
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(a,R.style.DialogSlideAnim);
        LayoutInflater inflater = a.getLayoutInflater();
        final View dialog = inflater.inflate(R.layout.show_msg_custom_dialog, null);

        dialogBuilder.setView(dialog);

        Button button3=(Button)dialog.findViewById(R.id.button3);
        TextView title=(TextView)dialog.findViewById(R.id.title);
        TextView msg=(TextView)dialog.findViewById(R.id.msg);
        title.setTypeface(titr);
        button3.setTypeface(titr);
        msg.setTypeface(face);
        //txtDescription.setText(Html.fromHtml(Description));
        title.setText(_title);
        msg.setText(_description);
        final AlertDialog alertDialog = dialogBuilder.create();
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
                a.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                if(redirect)
                {


                    Consts.arr2=new ArrayList<>();
                    Consts.arr_count=new ArrayList<>();
                    Consts.last=0;
                    Intent i=new Intent(a,ServicesActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    a.startActivity(i);

                    a.finish();


                    //Intent intent=new Intent(a,ServicesActivity.class);
                    //a.startActivity(intent);
                }
                if(finishing) {
                    a.finish();
                }
            }
        });

        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if(redirect)
                {
                    Intent i=new Intent(a,ServicesActivity.class);
                    a.startActivity(i);
                }
                if(finishing)
                    a.finish();
            }
        });
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        alertDialog.show();
    }

}
