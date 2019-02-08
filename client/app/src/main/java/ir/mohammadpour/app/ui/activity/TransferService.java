package ir.mohammadpour.app.ui.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.support.v4.app.NotificationCompat;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ir.mohammadpour.app.R;
import ir.mohammadpour.app.model.ChatModel;

public class TransferService extends Service{

    SharedPreferences prefs;
    MediaPlayer myMediaPlayer = null;
    Handler h = new Handler();
    public TransferService(Context context) {
        this.mContext = context;
        h = new Handler();
    }
    public TransferService()
    {}
    @Override
    public void onCreate()
    {
        mContext=this;
        myMediaPlayer = MediaPlayer.create(this, R.raw.cancel);
        myMediaPlayer.setLooping(false);
        h = new Handler();
         //Toast.makeText(mContext,"test ",Toast.LENGTH_LONG).show();


         final int delay = 5000; //milliseconds


        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String transfer_id = prefs.getString("transfer_id", null);
        final String myphone = prefs.getString("customer_phone", null);
        final String Server_MainUrl = prefs.getString("Server_MainUrl", null);
        h.postDelayed(new Runnable(){
            public void run(){
                try {

                        String url=Server_MainUrl+"/home/CheckStatus?phone="+myphone;
                        new AsyncCallWS().execute(url);
                }
                catch(Exception ex){
                    Log.v("transfer service:",ex.getMessage().toString());
                }
                h.postDelayed(this, delay);
            }
        }, delay);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        return START_STICKY;
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
                Log.v("result",result);
                try {
                    Log.v("myresult0","0");
                    String [] myresult=result.trim().split("~");
                    Log.v("myresult0","01");

                    if(myresult[5].trim().equals("true"))
                    {
                        if(myresult[5].trim().equals("true"))
                        {
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
                            createNotification("سفر شما لغو شد");

                            myMediaPlayer.start();
                            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager
                                    .getInstance(mContext);
                            Intent intent=new Intent("com.durga.action.close");
                            localBroadcastManager.sendBroadcast(intent);
                            h.removeCallbacksAndMessages(null);
                            stopSelf();
                            return;
                        }
                    }
                    else if(myresult.length==7)
                    {
                        if(myresult[6].trim().equals("true"))
                        {
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
                            createNotification("سفر شما لغو شد");

                            myMediaPlayer.start();
                            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager
                                    .getInstance(mContext);
                            Intent intent=new Intent("com.durga.action.close");
                            localBroadcastManager.sendBroadcast(intent);
                            h.removeCallbacksAndMessages(null);
                            stopSelf();
                            return;
                        }
                    }

                    if(myresult[0].equals("true"))
                    {

                        Log.v("myresult0","1");

                        Intent intent = new Intent(mContext, CommentActivity.class);
                        Log.v("myresult0","2");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        Log.v("myresult0","3");
                        if( prefs.getString("driver_avatar_path", null)!=null&&  !prefs.getString("driver_avatar_path", null).equals(""))
                            intent.putExtra("driver_avatar_path", prefs.getString("driver_avatar_path", null));
                        Log.v("myresult0","4");
                        intent.putExtra("driver_name", prefs.getString("driver_name", null));
                        Log.v("myresult0","5");
                        intent.putExtra("driver_car_name", prefs.getString("driver_car_name", null));
                        Log.v("myresult0","6");
                        intent.putExtra("driver_car_model", prefs.getString("driver_car_model", null));
                        Log.v("myresult0","7");
                        intent.putExtra("driver_pelak_number", prefs.getString("driver_pelak_number", null));
                        Log.v("myresult0","8");
                        intent.putExtra("driver_phone", prefs.getString("driver_phone", null));
                        Log.v("myresult0","9");
                        intent.putExtra("transfer_id", prefs.getString("transfer_id", null));
                        Log.v("myresult0","10");

                        if( prefs.getString("driver_avatar_path", null)!=null&&  !prefs.getString("driver_avatar_path", null).equals(""))
                            Log.v("fcm_msg_1", prefs.getString("driver_avatar_path", null));
                        Log.v("fcm_msg_2",prefs.getString("driver_name", null));
                        Log.v("fcm_msg_3",prefs.getString("driver_car_name", null));
                        Log.v("fcm_msg_4",prefs.getString("driver_car_model", null));
                        Log.v("fcm_msg_5",prefs.getString("driver_pelak_number", null));
                        Log.v("fcm_msg_6",prefs.getString("driver_phone", null));
                        Log.v("fcm_msg_7",prefs.getString("transfer_id", null));

                        getApplicationContext().startActivity(intent);

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
                        h.removeCallbacksAndMessages(null);
                        stopSelf();
                    }

                    else
                    {
                        try
                        {
                            JSONArray arr=new JSONArray(myresult[5]);
                            for(int i=0;i<arr.length();i++)
                            {

                                int last= prefs.getInt("last_chat_id",0);

                                if(!ChatActivity.chechChatExist(arr.getJSONObject(i).getInt("Id")+"") && last<arr.getJSONObject(i).getInt("Id")) {
                                    ChatModel model = new ChatModel();
                                    model.ID = arr.getJSONObject(i).getInt("Id") + "";
                                    model.msg = arr.getJSONObject(i).getString("Message");
                                    model.date = arr.getJSONObject(i).getString("date");
                                    model.type = arr.getJSONObject(i).getBoolean("type");
                                    model.who_added = arr.getJSONObject(i).getBoolean("who");



                                    //ChatActivity.chats.add(model);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putInt("last_chat_id",arr.getJSONObject(i).getInt("Id"));

                                    editor.apply();
                                    editor.commit();
                                    LocalBroadcastManager localBroadcastManager = LocalBroadcastManager
                                            .getInstance(mContext);
                                    Intent new_intent = new Intent("new_chat");
                                    new_intent.putExtra("Id", arr.getJSONObject(i).getInt("Id") + "");
                                    new_intent.putExtra("Message", arr.getJSONObject(i).getString("Message") + "");
                                    new_intent.putExtra("date", arr.getJSONObject(i).getString("date"));
                                    new_intent.putExtra("type", arr.getJSONObject(i).getBoolean("type"));
                                    new_intent.putExtra("who", arr.getJSONObject(i).getBoolean("who"));
                                    localBroadcastManager.sendBroadcast(new_intent);

                                    if (model.who_added) {
                                    } else {
                                        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                        Intent intent = new Intent(mContext, ChatActivity.class);
                                        int requestID = (int) System.currentTimeMillis();
                                        PendingIntent pIntent = PendingIntent.getActivity(mContext, requestID, intent, PendingIntent.FLAG_CANCEL_CURRENT);

                                        // this is it, we'll build the notification!
                                        // in the addAction method, if you don't want any icon, just set the first param to 0
                                        Notification mNotification = new Notification.Builder(mContext)
                                                .setContentTitle("پیام جدید")
                                                .setSmallIcon(R.drawable.appicon)
                                                .setContentIntent(pIntent)
                                                .setSound(soundUri)

                                                .build();
                                        mNotification.flags |= Notification.FLAG_AUTO_CANCEL;
                                        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);



                                        // If you want to hide the notification after it was selected, do the code below
                                        // myNotification.flags |= Notification.FLAG_AUTO_CANCEL;

                                        notificationManager.notify(arr.getJSONObject(i).getInt("Id"), mNotification);
                                    }
                                }


                            }


                        }
                        catch (JSONException ex){}


                        }

                }
                catch (ArrayIndexOutOfBoundsException ex){

                    Log.v("myresult0",ex.getMessage().toString());
                }
                catch (java.lang.NullPointerException ex){
                    Log.v("myresult0",ex.getMessage().toString());
                }
                //catch (ArrayIndexOutOfBoundsException ex){}
            }

        }

    }
    private Context mContext;
    int GetRandom()
    {
        Random rn = new Random();
        int n = 650000 - 1 + 1;
        int i = rn.nextInt() % n;
        return   (1 + i);

    }
    private void createNotification( String messageBody) {
        Intent intent = new Intent( this , SplashActivity.class );
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent resultIntent = PendingIntent.getActivity( this , 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder( this)
                .setSmallIcon(R.drawable.appicon)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setContentTitle("پیام جدید از اسگاد")
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.appicon))
                .setContentText(messageBody)
                .setAutoCancel( true )
                .setSound(notificationSoundURI)
                .setContentIntent(resultIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(GetRandom(), mNotificationBuilder.build());
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }


}