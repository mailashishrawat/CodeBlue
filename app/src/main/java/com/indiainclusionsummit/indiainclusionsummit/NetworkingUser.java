package com.indiainclusionsummit.indiainclusionsummit;

import android.util.Log;

/**
 * Created by I055845 on 10/14/2015.
 */
public class NetworkingUser {
    private String userName;
    private  String userId;
    private  String userMob;
    private  String userMail;
    private boolean isSelected = false;

    NetworkingUser(String pId,String pName,String pMob, String pMail) {
        userName = pName;
        userId   = pId;
        userMob  = pMob;
        userMail = pMail;
    }

    NetworkingUser(String pId,String pName) {
        userName = pName;
        userId   = pId;
    }

    public void setMail(String pMail){
        userMail = pMail;
    }
    public void setMob(String pMob) {
        userMob = pMob;
    }

    public String getUserName() { return userName;}
    public String getUserId() { return userId;}
    public String getUserMob() { return userMob;}
    public String getUserMail() { return userMail;}


    public void setSelected(boolean selected) {
        if (selected == true)
            Log.v("Avis", "setting selected" + userId);
        else
            Log.v("Avis", "Clearing selected" + userId);
        this.isSelected = selected;
    }

    public boolean getSelected()
    {
        return this.isSelected;
    }

}
