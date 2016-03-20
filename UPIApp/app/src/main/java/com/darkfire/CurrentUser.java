package com.darkfire;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;

import java.util.HashMap;

/**
 * Created by Siddharth on 2/24/2016.
 */
public class CurrentUser {

    private String mobile, password;
    private String adharNumber;
    private String vpa = "testVPA";
    private String mca = "9072013";
    private boolean active = false;

    public static final boolean offline = true;

    private static CurrentUser INSTANCE;

    private CurrentUser(){

    }

    public static CurrentUser getInstance(){
        if(INSTANCE==null){
            INSTANCE = new CurrentUser();
        }
        return INSTANCE;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setPassword(String password){
        this.password = password;
    }
    public String getPassword(){
        return password;
    }

    public String getAdharNumber() {
        return adharNumber;
    }

    public void setAdharNumber(String adharNumbe) {
        this.adharNumber = adharNumbe;
    }

    public boolean isActive(){
        return active;
    }

    public String getMca(){
        return mca;
    }
    public String getVpa(){
        return vpa;
    }

    public boolean verify(){
        //TODO: make an http request and veriy if credentials
        //if yes turn active = true;
        if(offline) {
            adharNumber = "1234-4567-90";
            active = true;
            return true;
        }
        String url = CurrentServer.getInstance().getBaseAddress()+"login";
        HashMap<String, String> map = new HashMap<>();
        map.put("username", getMobile());
        map.put("password", getPassword());
        String json = HttpUtility.openPostConnection(url, map);
        if(json.equalsIgnoreCase("Unauthorized")){
            return false;
        }
        try {
            JSONObject jsonObject = (JSONObject)(new JSONTokener(json).nextValue());
            if(jsonObject.getString("status").equals("ok")){
                this.adharNumber = jsonObject.getString("aadharid");
                this.active = true;
                return true;
            }else{

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
}
