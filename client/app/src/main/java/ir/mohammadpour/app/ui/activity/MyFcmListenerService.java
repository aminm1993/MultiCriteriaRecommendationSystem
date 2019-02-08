package ir.mohammadpour.app.ui.activity;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import ir.mohammadpour.app.R;
import ir.mohammadpour.app.model.Message;
import ir.mohammadpour.app.ui.util.LocationService;
import ir.mohammadpour.app.ui.util.ShamsiCalleder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Amin on 2016-12-28.
 */
public class MyFcmListenerService extends FirebaseMessagingService {
    SharedPreferences prefs;
    @Override
    public void onMessageReceived(RemoteMessage message){
       // String from = message.getFrom();
       // Map data = message.getData();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

           //Log.v("fcm_msg",message.getData().toString());

            String value=  message.getData().get("value");
            if(value !=null) {
                Configuration.Builder configurationBuilder = new Configuration.Builder(this);
                configurationBuilder.addModelClass(Message.class);
                ActiveAndroid.initialize(configurationBuilder.create());
                Calendar c1 = Calendar.getInstance();

                int day = c1.get(Calendar.DATE);
                int mounth = c1.get(Calendar.MONTH)+1;
                int year = c1.get(Calendar.YEAR);

                String d1= ShamsiCalleder.getCurrentShamsidate(year+"/"+mounth+"/"+day);
                Message n = new Message();
                n.s_name = value;
                n.s_time=d1 +" ساعت "+c1.get(Calendar.HOUR_OF_DAY)+":"+c1.get(Calendar.MINUTE);
                n.save();
                createNotification(value);
            }
             else if(message.getData().get("code")!=null && !message.getData().get("code").equals("")) {
                try {
                stopService(new Intent(this, TransferService.class));
                //createNotification(message.getNotification().getBody());
                String code = message.getData().get("code");

                Intent intent = new Intent(this, CommentActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if( prefs.getString("driver_avatar_path", null)!=null&&  !prefs.getString("driver_avatar_path", null).equals(""))
                     intent.putExtra("driver_name", prefs.getString("driver_name", null));
                intent.putExtra("driver_car_name", prefs.getString("driver_car_name", null));
                intent.putExtra("driver_car_model", prefs.getString("driver_car_model", null));
                intent.putExtra("driver_pelak_number", prefs.getString("driver_pelak_number", null));
                intent.putExtra("driver_phone", prefs.getString("driver_phone", null));
                intent.putExtra("transfer_id", prefs.getString("transfer_id", null));

                if( prefs.getString("driver_avatar_path", null)!=null&&  !prefs.getString("driver_avatar_path", null).equals(""))
                     Log.v("fcm_msg_1", prefs.getString("driver_avatar_path", null));
                Log.v("fcm_msg_2",prefs.getString("driver_name", null));
                Log.v("fcm_msg_3",prefs.getString("driver_car_name", null));
                Log.v("fcm_msg_4",prefs.getString("driver_car_model", null));
                Log.v("fcm_msg_5",prefs.getString("driver_pelak_number", null));
                Log.v("fcm_msg_6",prefs.getString("driver_phone", null));
                Log.v("fcm_msg_7",prefs.getString("transfer_id", null));

                startActivity(intent);

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

                }
                catch (NullPointerException ex){}
            }


                 else {
                    try {
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("transfer_state", "true");

                        editor.putString("driver_avatar_path", message.getData().get("AvatarPath"));
                        editor.putString("driver_name", message.getData().get("FullName"));
                        editor.putString("driver_car_name", message.getData().get("CarName"));
                        editor.putString("driver_car_model", message.getData().get("CarModel"));
                        editor.putString("driver_pelak_number", message.getData().get("PelakNumber"));
                        editor.putString("driver_phone", message.getData().get("Phone"));

                        editor.apply();
                        editor.commit();

                        startService(new Intent(this, TransferService.class));


                    } catch (Exception ex) {
                    }
                }




    }

int GetRandom()
{
    Random rn = new Random();
    int n = 650000 - 1 + 1;
    int i = rn.nextInt() % n;
    return   (1 + i);

}
    private void createNotification( String messageBody) {
        Intent intent = new Intent( this , MessagesActivity.class );
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
}
