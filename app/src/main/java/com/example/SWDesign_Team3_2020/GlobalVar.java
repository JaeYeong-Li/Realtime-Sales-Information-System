package com.example.SWDesign_Team3_2020;

import android.app.Application;
public class GlobalVar extends Application{
    private static String userID;
    public static String userPW;
    public static String userNAME;

    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
    }
    public String getuserID(){
        return userID;
    }
    public void setUserID(String id){
        userID = id;
    }
    public String getuserPW(){
        return userPW;
    }
    public void setUserPW(String pw){
        userPW = pw;
    }
    public String getuserNAME() {
        return userNAME;
    }
    public void setUserNAME(String name){
        userNAME = name;
    }
}
