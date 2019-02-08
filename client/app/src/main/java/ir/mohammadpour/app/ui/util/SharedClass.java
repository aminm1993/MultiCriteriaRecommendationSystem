package ir.mohammadpour.app.ui.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by amin on 3/13/17.
 */

public class SharedClass {
    private SharedPreferences basketPref;
    public SharedClass(Context context) {
            basketPref = context.getSharedPreferences("user", 0);
    }


    public void setSeeMarkerAdd(){//String id, String price, String sumPrice, String count, String name, String imageName, String title){
        SharedPreferences.Editor editor = basketPref.edit();
        editor.putBoolean("SeeMarkerAdd", true);
        editor.commit();
    }

    public void setSeeRoute(){//String id, String price, String sumPrice, String count, String name, String imageName, String title){
        SharedPreferences.Editor editor = basketPref.edit();
        editor.putBoolean("SeeRoute", true);
        editor.commit();
    }

    public boolean isSeeMarkerAdd(){
        boolean json = basketPref.getBoolean("SeeMarkerAdd", false);
        return json;
    }

    public boolean getSeeRoute(){
        boolean json = basketPref.getBoolean("SeeRoute", false);
        return json;
    }
}
