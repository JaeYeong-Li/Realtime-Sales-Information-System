package com.example.SWDesign_Team3_2020;

import android.app.Application;
public class GlobalVar extends Application{
    private static String userID;
    private static String userPW;
    private static String userNAME;
    public static int searchMode=1;
    public static String selectedMenu;
    public static String selectedOpenTime;

    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
    }
    public Boolean isIDnull() {
        if(this.userID == null || this.userID.isEmpty() ==true)
            return true;
        else
            return false;
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
    public void setSearchMode(int mode) {searchMode = mode;}
    public int getSearchMode() { return searchMode;}
    public String getselectedMenu() {
        return selectedMenu;
    }
    public void setselectedMenu(String menu){
        selectedMenu = menu;
    }
    public String getselectedOpenTime() {
        return selectedOpenTime;
    }
    public void setselectedOpenTime(String ot){
        selectedOpenTime = ot;
    }
}
