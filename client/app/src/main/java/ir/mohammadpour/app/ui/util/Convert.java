package ir.mohammadpour.app.ui.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Amin on 2017-06-07.
 */
public class Convert {
    public static JSONArray StringToJsonArray(String input)
    {
        JSONArray json=null;
        try {
             json = new JSONArray(input);
        }
        catch (JSONException ex){}

        return json;
    }

    public static JSONObject StringToJsonObject(String input)
    {
        JSONObject json=null;
        try {
            json = new JSONObject(input);
        }
        catch (JSONException ex){}

        return json;
    }
}
