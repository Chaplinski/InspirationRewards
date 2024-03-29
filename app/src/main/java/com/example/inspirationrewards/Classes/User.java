package com.example.inspirationrewards.Classes;

import android.util.Base64;

import org.json.JSONArray;

import java.io.Serializable;

public class User implements Serializable {

    //private Base64 sImage;
    private String sUsername;
    private String sPassword;
    private String sFirstName;
    private String sLastName;
    private String sDepartment;
    private String sPosition;
    private String sStory;
    private String sLocation;
    private String sImage;
    private int iPointsAwarded;
    private int iPointsToAward;
    private String rewardRecord;
    private boolean bIsAdmin;
    //possibly be adding a variable for number of points a user has been awarded so far
    // and a String for location


//    public Base64 getImage(){ return sImage; }
//
//    public void setImage(Base64 newImage){ this.sImage = newImage; }

    public String getUsername(){ return sUsername; }

    public void setUserName(String newUserName){ this.sUsername = newUserName; }

    public String getPassword(){ return sPassword; }

    public void setPassword(String newPassword){ this.sPassword = newPassword; }

    public String getFirstName(){ return sFirstName; }

    public void setFirstName(String newFirstName){ this.sFirstName = newFirstName; }

    public String getLastName(){ return sLastName; }

    public void setLastName(String newLastName){ this.sLastName = newLastName; }

    public String getDepartment(){ return sDepartment; }

    public void setDepartment(String newDepartment){ this.sDepartment = newDepartment; }

    public String getPosition(){ return sPosition; }

    public void setPosition(String newPosition){ this.sPosition = newPosition; }

    public String getStory(){ return sStory; }

    public void setStory(String newStory){ this.sStory = newStory; }

    public String getLocation(){ return sLocation; }

    public void setLocation(String newLocation){ this.sLocation = newLocation; }

    public String getImage(){ return sImage; }

    public void setImage(String newImage){ sImage = newImage; }

    public int getPointsAwarded(){
        return iPointsAwarded;
    }

    public void setPointsAwarded(int rewards){ iPointsAwarded = rewards; }

    public int getPointsToAward(){ return iPointsToAward; }

    public void setPointsToAward(int points){ iPointsToAward = points; }

    public String getRewardRecord(){
        return rewardRecord;
    }

    public void setRewardRecord(String rewardRecord){
        this.rewardRecord = rewardRecord;
    }

    public boolean getAdmin(){ return bIsAdmin; }

    public void setAdmin(boolean newAdmin){ this.bIsAdmin = newAdmin; }
}
