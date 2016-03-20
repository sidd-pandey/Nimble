package com.darkfire;

/**
 * Created by Siddharth on 2/25/2016.
 */
public class CurrentServer {
    private String protocol="http";
    private String server = "10.42.0.1";
    private String port = "3000";
    private String servlet = "";

    private static CurrentServer INSTANCE;

    private CurrentServer(){

    }
    public static CurrentServer getInstance(){
        if(INSTANCE==null)
            INSTANCE = new CurrentServer();
        return INSTANCE;

    }
    public String getBaseAddress(){
        return protocol + "://" + server + ":" + port + "/" + servlet;
    }

}
