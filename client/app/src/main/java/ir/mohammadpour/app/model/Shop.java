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
public class Shop {

    private String Name;
    private String ImgUrl;
    private String Address;
    private String Phone;
    public void setName(String name) {
        this.Name = name;
    }

    public String getName() {
        return Name;
    }

    public void setImgUrl(String imgUrl) {
        this.ImgUrl = imgUrl;
    }

    public String getImgUrl() {
        return ImgUrl;
    }

    public void setAddress(String _Address) {
        this.Address = _Address;
    }

    public String getAddress() {
        return Address;
    }


    public void setPhone(String _Phone) {
        this.Phone = Phone;
    }

    public String getPhone() {
        return Phone;
    }

}
