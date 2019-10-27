package com.example.inspirationrewards.Classes;

import java.io.Serializable;

public class Reward implements Serializable {

    private String sTargetUserName;
    private String sTargetName;
    private String sDate;
    private String sNotes;
    private int iRewardPoints;

    private String sSourceName;
    private String sSourcePassword;

    public String getTargetUserName(){ return sTargetUserName; }

    public void setTargetUserName(String incomingUserName){ sTargetUserName = incomingUserName; }

    public String getTargetName(){ return sTargetName; }

    public void setTargetName(String incomingName){ sTargetName = incomingName; }

    public String getDate(){ return sDate; }

    public void setDate(String date){ sDate = date; }

    public String getNotes(){ return sNotes; }

    public void setNotes(String notes){ sNotes = notes; }

    public int getRewardPoints(){ return iRewardPoints; }

    public void setRewardPoints(int points){ iRewardPoints = points; }

    public String getSourceName(){ return sSourceName; }

    public void setSourceName(String incomingName){ sSourceName = incomingName; }

    public String getSourcePassword(){ return sSourcePassword; }

    public void setSourcePassword(String incomingPassword){ sSourcePassword = incomingPassword; }

}
