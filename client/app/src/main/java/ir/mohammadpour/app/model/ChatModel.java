package ir.mohammadpour.app.model;

import android.content.Context;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import ir.mohammadpour.app.constants.NetworkConstants;
import ir.mohammadpour.app.controller.BaseInterface;
import ir.mohammadpour.app.network.BaseNetwork;

/**
 * Created by skytreasure on 15/4/16.
 */
public class ChatModel{

public String ID;
    public String TransferID;
    public boolean who_added;
    public boolean type;
    public String msg;
    public String date;
}
