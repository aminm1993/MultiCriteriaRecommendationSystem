package ir.mohammadpour.app.ui.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.Image;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.RingtoneManager;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListPopupWindow;
import android.text.Editable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ir.mohammadpour.app.R;
import ir.mohammadpour.app.model.ChatModel;
import ir.mohammadpour.app.ui.adapter.ChatAdapter;

public class ChatActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    private MediaRecorder mediaRecorder;

    EditText editMsg;
    Button record;
    Boolean state = false;
    ListView lv;
    String voiceStoragePath;
    static final String AB = "abcdefghijklmnopqrstuvwxyz";
    static Random rnd = new Random();
    String Server_MainUrl = "";
    SharedPreferences prefs;
    String transfer_id="";
    String myphone="";
    ImageView send;

    Activity ac;
    public static List<ChatModel> chats=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        setContentView(R.layout.activity_chat);
        //chats=new ArrayList<>();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Server_MainUrl = prefs.getString("Server_MainUrl", null);
        transfer_id=prefs.getString("transfer_id", null);
        myphone = prefs.getString("customer_phone", null);
        dialog = new ProgressDialog(this);
        dialog.setMessage("در حال ارسال پیام");
        dialog.setCancelable(false);
        hasSDCard();
        send=(ImageView)findViewById(R.id.send);

        ac=this;
        lv=(ListView)findViewById(R.id.lv);
        voiceStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File audioVoice = new File(voiceStoragePath + File.separator + "voices");
        if (!audioVoice.exists()) {
            audioVoice.mkdir();
        }
        voiceStoragePath = voiceStoragePath + File.separator + "voices/" + generateVoiceFilename(6) + ".mp3";
        System.out.println("Audio path : " + voiceStoragePath);
        record = (Button) findViewById(R.id.record);
        editMsg = (EditText) findViewById(R.id.editMsg);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editMsg.getText().toString().equals("") || editMsg.getText().toString().trim().equals(""))
                {
                    Toast.makeText(ac,"لطفا پیغام خود را بنویسید",Toast.LENGTH_LONG).show();
                }
                else {
                    new PostTextMessage().execute(editMsg.getText().toString());
                }
            }
        });
        /*record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!state) {
                    state = true;
                    record.setImageResource(R.drawable.on);
                    //start record
                    if (mediaRecorder == null) {
                        initializeMediaRecord();
                    }
                    startAudioRecording();
                    Toast.makeText(ChatActivity.this,"شروع ضبط صدا",Toast.LENGTH_SHORT).show();
                } else {
                    state = false;
                    record.setImageResource(R.drawable.off);
                    //end record and send to server
                    stopAudioRecording();

                    dialog = new ProgressDialog(ChatActivity.this);
                    dialog.setMessage("در حال آپلود فایل");
                    dialog.setCancelable(false);

                    new UploadFileAsync().execute(voiceStoragePath);
                }
            }
        });
*/

        record.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    record.setBackgroundResource(R.drawable.on);
                    if (mediaRecorder == null) {
                        initializeMediaRecord();
                    }
                    startAudioRecording();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    record.setBackgroundResource(R.drawable.off);
                    //end record and send to server
                    stopAudioRecording();

                    dialog = new ProgressDialog(ChatActivity.this);
                    dialog.setMessage("در حال آپلود فایل");
                    dialog.setCancelable(false);

                    new UploadFileAsync().execute(voiceStoragePath);
                }
                return true;
            }
        });
        String url=Server_MainUrl+"/home/CheckStatus?phone="+myphone;
        new AsyncCallWS().execute(url);





        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("new_chat");
        mLocalBroadcastManager.registerReceiver(mBroadcastReceiver, mIntentFilter);
    }


    @Override
    public void onBackPressed()
    {

        if(state)
        {
            state = false;
            //record.setImageResource(R.drawable.off);
            //end record and send to server
            stopAudioRecording();

            dialog = new ProgressDialog(ChatActivity.this);
            dialog.setMessage("در حال آپلود فایل");
            dialog.setCancelable(false);

            new UploadFileAsync().execute(voiceStoragePath);

            //super.onBackPressed();
        }
        else
        {
            super.onBackPressed();
        }
    }
    private String generateVoiceFilename(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    private void hasSDCard() {
        Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if (isSDPresent) {
            System.out.println("There is SDCard");
        } else {
            System.out.println("There is no SDCard");
        }
    }

    private void startAudioRecording() {
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //recordingButton.setEnabled(false);
        //stopButton.setEnabled(true);
    }

    private void stopAudioRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }
        //stopButton.setEnabled(false);
        //playButton.setEnabled(true);
    }

    private void playLastStoredAudioMusic() {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(voiceStoragePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
        //recordingButton.setEnabled(true);
        // playButton.setEnabled(false);

    }

    private void stopAudioPlay() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void mediaPlayerPlaying() {
        if (!mediaPlayer.isPlaying()) {
            stopAudioPlay();
        }
    }

    private void initializeMediaRecord() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(voiceStoragePath);
    }


    int serverResponseCode = 0;
    ProgressDialog dialog = null;

    String upLoadServerUri = null;

    private class UploadFileAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

                    try
                    {
                        HttpClient client = new DefaultHttpClient();
                        HttpPost post = new HttpPost(Server_MainUrl+"/request/AddChatMsg");

                        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
                        entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

                        entityBuilder.addTextBody("transferid", transfer_id);
                        entityBuilder.addTextBody("phone", myphone);
                        entityBuilder.addTextBody("who", "0");
                        String sourceFileUri = params[0];
                        File file = new File(sourceFileUri);
                        //if(file != null)
                        //{
                        entityBuilder.addPart("file", new FileBody(file));
                        //}

                        HttpEntity entity = entityBuilder.build();
                        post.setEntity(entity);
                        HttpResponse response = client.execute(post);
                        HttpEntity httpEntity = response.getEntity();
                        String result = EntityUtils.toString(httpEntity);
                       return result;
                    }
                    catch(Exception e)
                    {
                       return null;
                    }





        }

        @Override
        protected void onPostExecute(String result) {

            if(result!=null)
            {
                if(result.trim().contains("true"))
                {
                    Toast.makeText(ChatActivity.this,"فایل صوتی ارسال شد",Toast.LENGTH_SHORT).show();
                    refresh();
                }
                Log.v("voice",result);
            }
            dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            dialog.show();
        }

    }





    private class PostTextMessage extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... data) {

                try {
                    HttpClient client = new DefaultHttpClient();
                    HttpPost post = new HttpPost(Server_MainUrl + "/request/AddChatMsg");

                    MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
                    entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

                    entityBuilder.addTextBody("transferid", transfer_id);
                    entityBuilder.addTextBody("phone", myphone);
                    entityBuilder.addTextBody("who", "0");
                    entityBuilder.addTextBody("message", data[0], ContentType.TEXT_PLAIN.withCharset("UTF-8"));
                    //}

                    HttpEntity entity = entityBuilder.build();
                    post.setEntity(entity);
                    HttpResponse response = client.execute(post);
                    HttpEntity httpEntity = response.getEntity();
                    String result = EntityUtils.toString(httpEntity);
                    return result;
                } catch (Exception e) {
                    return null;
                }

        }

        @Override
        protected void onPostExecute(String result) {

            if(result.equals("true"))
            {
                refresh();

                editMsg.setText("");
            }
            dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            dialog.show();
        }
    }


    public static boolean chechChatExist(String Id)
    {
        for(int i=0;i<chats.size();i++)
        {
            if(chats.get(i).ID.equals(Id))
                return true;
        }

        return false;
    }



    private class AsyncCallWS extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

            try
            {
                Log.v("service_url",params[0]);
                URL url = new URL(params[0]);
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

            if(result==null)
                return;
            else
            {
                chats=new ArrayList<>();
                //Log.v("result",result);
                try {
                    String [] myresult=result.trim().split("~");
                    if(myresult[0].equals("true"))
                    {
                    }

                    else
                    {
                        try
                        {
                            JSONArray arr=new JSONArray(myresult[5]);
                            for(int i=0;i<arr.length();i++)
                            {


                                    ChatModel model = new ChatModel();
                                    model.ID = arr.getJSONObject(i).getInt("Id") + "";
                                    model.msg = arr.getJSONObject(i).getString("Message");
                                    model.date = arr.getJSONObject(i).getString("date");
                                    model.type = arr.getJSONObject(i).getBoolean("type");
                                    model.who_added = arr.getJSONObject(i).getBoolean("who");

                                chats.add(model);
                            }
                            if(chats==null || chats.size()==0)
                                lv.setAdapter(null);
                            else {
                                ChatAdapter adapter = new ChatAdapter(ac, chats);
                                lv.setAdapter(adapter);
                            }

                        }
                        catch (JSONException ex){}
                    }
                }
                catch (ArrayIndexOutOfBoundsException ex){}
                catch (java.lang.NullPointerException ex){}
                //catch (ArrayIndexOutOfBoundsException ex){}
            }

        }

    }
    LocalBroadcastManager mLocalBroadcastManager;
    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("new_chat")) {
                {


                    String Id = intent.getExtras().getString("Id", "");
                    String Message = intent.getExtras().getString("Message", "");
                    String date = intent.getExtras().getString("date", "");
                    boolean type = intent.getExtras().getBoolean("type", false);
                    boolean who = intent.getExtras().getBoolean("who", false);


                    if (chats == null)
                        chats = new ArrayList<>();
                    ChatModel model = new ChatModel();
                    model.ID = Id;
                    model.msg = Message;
                    model.date = date;
                    model.type = type;
                    model.who_added = who;

                    chats.add(model);

                    ChatAdapter adapter = new ChatAdapter(ac, chats);
                    adapter.notifyDataSetChanged();
                    lv.setAdapter(adapter);



                }

            }
        }

    };
    public void refresh()
    {
        String url=Server_MainUrl+"/home/CheckStatus?phone="+myphone;
        new AsyncCallWS().execute(url);
    }
}
