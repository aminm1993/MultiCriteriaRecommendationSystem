package ir.mohammadpour.app.ui.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.mohammadpour.app.R;
import ir.mohammadpour.app.model.BackUpMessageModel;
import ir.mohammadpour.app.model.ChatModel;
import ir.mohammadpour.app.ui.activity.ChatActivity;

public class BackUpAdapter extends BaseAdapter {

    private Activity activity;

    List<BackUpMessageModel> chats;

    private static LayoutInflater inflater = null;

    public BackUpAdapter(Activity a, List<BackUpMessageModel> _chats) {

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
        TextView txtMessage;
        TextView txtInfo;
        LinearLayout contentWithBackground;
    }
    public View getView(final int position, final View convertView, ViewGroup parent) {
        final Holder holder = new Holder();
         View rowView = null;
//         holder.contentWithBackground=(LinearLayout)rowView.findViewById(R.id.contentWithBackground);

        if (chats.get(position).who==false)//khodam
        {
            rowView=inflater.inflate(R.layout.list_item_chat_message, null);
        }
        else {
            rowView=inflater.inflate(R.layout.list_item_chat_message_from, null);
        }
        holder.txtMessage=(TextView)rowView.findViewById(R.id.txtMessage);
        holder.txtInfo=(TextView)rowView.findViewById(R.id.txtInfo);

        holder.txtMessage.setText(chats.get(position).Message);
        holder.txtInfo.setText(chats.get(position).date);

        return rowView;
    }

    ProgressDialog dialog;

}
