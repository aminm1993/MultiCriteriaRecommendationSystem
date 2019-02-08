package ir.mohammadpour.app.ui.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.mohammadpour.app.R;
import ir.mohammadpour.app.model.ChatModel;
import ir.mohammadpour.app.ui.activity.ChatActivity;
import ir.mohammadpour.app.ui.activity.Sabeghe;

public class ChatAdapter extends BaseAdapter {

    private Activity activity;

    List<ChatModel> chats;

    private static LayoutInflater inflater = null;

    public ChatAdapter(Activity a, List<ChatModel> _chats) {

        activity = a;

        chats = _chats;

        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    public int getCount() {
        return chats.size();
    }

    public Object getItem(int position) {
        return chats.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
    class Holder
    {
        TextView text;
        ImageView img;
        CircleImageView user_profile_photo;
    }


    SharedPreferences prefs;
    String voiceStoragePath="";
    public View getView(final int position, final View convertView, ViewGroup parent) {


        //if(convertView!=null)
        //  return convertView;
        prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        final Holder holder = new Holder();
         View rowView=null;
        if (chats.get(position).who_added==false)//khodam
        {
            rowView = inflater.inflate(R.layout.message_from_me, null);
            holder.user_profile_photo=(CircleImageView)rowView.findViewById(R.id.user_profile_photo);
        }
        else {
            rowView = inflater.inflate(R.layout.message_to_me, null);
            String driver_avatar_path = prefs.getString("driver_avatar_path", null);
            holder.user_profile_photo=(CircleImageView)rowView.findViewById(R.id.user_profile_photo);
            final ImageLoader imageLoader = ImageLoader.getInstance();
            String url=  prefs.getString("Server_MainUrl", null);
            imageLoader.displayImage(url + "/profiles/" + driver_avatar_path, holder.user_profile_photo, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted() {

                        }

                        @Override
                        public void onLoadingFailed(FailReason failReason) {
                            // arrayKala.get(position).imgUrl="http://taxnet.ir/images/shopdefault.png";
                            //imageLoader.displayImage("http://taxnet.ir/images/shopdefault.png",relativeLayout);
                        }

                        @Override
                        public void onLoadingComplete(Bitmap bitmap) {
                            //Drawable dr = new BitmapDrawable(bitmap);

                            // relativeLayout.setBackground(dr);
                            //Blurry.with(c).radius(25).sampling(2).onto(relativeLayout);

                        }

                        @Override
                        public void onLoadingCancelled() {

                        }
                    }
            );
        }




            holder.text=(TextView)rowView.findViewById(R.id.text) ;
            holder.img=(ImageView) rowView.findViewById(R.id.img) ;



            if(chats.get(position).type==false)//record
            {
                holder.img.setVisibility(View.VISIBLE);
                holder.text.setVisibility(View.GONE);

                 voiceStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath();
                voiceStoragePath = voiceStoragePath + File.separator + "voices/";
                File file = new File(voiceStoragePath);
                file.mkdirs();

                File outputFile = new File(file, chats.get(position).msg.replace("/voices/","").trim());
                if (outputFile.exists()) {
                    //outputFile.delete();

                    holder.img.setImageResource(R.drawable.on);
                    holder.img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            playSound(voiceStoragePath+chats.get(position).msg.replace("/voices/","").trim());
                        }
                    });
                }
                else
                {

                    holder.img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String url=  prefs.getString("Server_MainUrl", null);
                            new DownloadTask(v.getContext()).execute(url+chats.get(position).msg,chats.get(position).msg.replace("/voices/","").trim());
                        }
                    });
                }

            }
            else
            {
                holder.img.setVisibility(View.GONE);
                holder.text.setVisibility(View.VISIBLE);
                holder.text.setText(chats.get(position).msg);
            }






        return rowView;
    }

    ProgressDialog dialog;
    private void playSound(String path) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(path);
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

    class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;
        //private PowerManager.WakeLock mWakeLock;

        public DownloadTask(Context context) {
            this.context = context;
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
                String PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
                PATH = PATH + File.separator + "voices/";
                File file = new File(PATH);
                file.mkdirs();
                File outputFile = new File(file, arg0[1]);
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
                    publishProgress(progress_temp);
                    fos.write(buffer, 0, len1);
                }
                fos.close();
                is.close();


                return "true";

            } catch (Exception e) {
                return  null;
            }
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(activity);
            dialog.setMessage("در حال دریافت فایل");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {

        }

        @Override
        protected void onPostExecute(String result) {
            // mWakeLock.release();

            dialog.cancel();
            if (result != null) {
               // Toast.makeText(activity,"مشکل در دانلود",Toast.LENGTH_LONG).show();
            }
            else if (result.contains("true")){
               Toast.makeText(activity,"فایل با موفقیت دانلود شد",Toast.LENGTH_LONG).show();
                ChatActivity ac=(ChatActivity)activity;
                ac.refresh();
            }

        }
    }

}
