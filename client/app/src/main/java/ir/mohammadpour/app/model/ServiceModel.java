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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ir.mohammadpour.app.constants.NetworkConstants;
import ir.mohammadpour.app.controller.BaseInterface;
import ir.mohammadpour.app.network.BaseNetwork;

/**
 * Created by skytreasure on 15/4/16.
 */
public class ServiceModel {

    public  ServiceModel()
    {
        Details=new ArrayList<>();
    }
    public int ID;
    public String Name;
    public String Description;
    public String IconUrl;
    public String DeActiveServiceText;
    public boolean has_services;
    public boolean type;
    public List<SubService> Details;

    public String ExtraText;
    public int ExtraValue;
    public int ExtraValue2;
    public int ExtraMoneyAfterXPrice;
    public boolean FirstTime;
    public boolean SecondTime;
    public String FirstTimeText ;
    public String SecondTimeText ;

    public String FirstTimeText2;
    public String SecondTimeText2 ;

}
