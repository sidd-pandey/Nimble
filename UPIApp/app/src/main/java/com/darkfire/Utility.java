package com.darkfire;

/**
 * Created by Siddharth on 3/7/2016.
 */
public class Utility {

    public static final String billElementSeparator = "@";
    public static final String elementSeparator = "#";
    public static final String billValueSeparator=",";


    public static String getTransactionId(){
        String id = "";
        for(int i = 0; i < 10; i++){
            id = id + (int)(Math.random()*9 + 1);
        }
        return id;
    }
    public static String lineSeparator(){
        return "####";
    }
}
