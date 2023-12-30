package com.softkour.qrsta_server.config;

public class MyUtils {

    public static String getUserPhone(String logoutAndPhone){
        return  logoutAndPhone.substring(logoutAndPhone.indexOf('+')+1);
    }
}
