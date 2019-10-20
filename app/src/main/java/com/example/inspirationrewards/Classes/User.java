package com.example.inspirationrewards.Classes;

import android.util.Base64;

public class User {

    private Base64 sImage;
    private String sUsername;
    private String sPassword;
    private String sFirstName;
    private String sLastName;
    private String sDepartment;
    private String sPosition;
    private String sAboutUser;
    private boolean bIsAdmin;

    public Base64 getImage(){ return sImage; }

    public void setImage(Base64 newImage){ this.sImage = newImage; }

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

    public String getAboutUser(){ return sAboutUser; }

    public void setAboutUser(String newAbout){ this.sAboutUser = newAbout; }

    public boolean getAdmin(){ return bIsAdmin; }

    public void setAdmin(boolean newAdmin){ this.bIsAdmin = newAdmin; }
}