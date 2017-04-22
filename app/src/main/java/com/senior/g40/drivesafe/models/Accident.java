/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.senior.g40.drivesafe.models;

import java.sql.Date;

/**
 *
 * @author PNattawut
 */
public class Accident {

    private long accId;
    private long userId;
    private Date date;
    private String time;
    private float latitude;
    private float longtitude;
    private float forceDetect;
    private float speedDetect;
    //-- accCode is have very importance role.
    private char accCode;
    
    public static char ACC_CODE_A = 'A';
    public static char ACC_CODE_G = 'G';
    public static char ACC_CODE_R = 'R';
    public static char ACC_CODE_C = 'C';
    public static char ACC_CODE_ERRU = '1';
    public static char ACC_CODE_ERRS = '2';
    //A[Accident]: Pending for rescue, 
    //G[Going]: Rescuer is on the way, 
    //R[Resecue]: Rescuer is rescuing, 
    //C[Clear]: Rescue received, marking will be cleared next time.  
//1[False on User]

    public static final float GS_SERIOUS = 60 ; // G's that cause the airbag deployed. [60]
    public static final float GS_DEBUG = 3 ;
    private static Accident accident;

    public Accident() {
    }

    public Accident(long userId, Date date, String time, float latitude, float longtitude, float forceDetect, float speedDetect, char accCode) {
        this.userId = userId;
        this.date = date;
        this.time = time;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.forceDetect = forceDetect;
        this.speedDetect = speedDetect;
        this.accCode = accCode;
    }

    public static Accident getInsatance() {
        if (accident == null) {
            accident = new Accident();
        }
        return accident;
    }

    public static Accident setInstance(Accident accident) {
        Accident.accident = accident;
        return accident;
    }


    public long getAccId() {
        return accId;
    }

    public void setAccId(long accId) {
        this.accId = accId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(float longtitude) {
        this.longtitude = longtitude;
    }

    public float getForceDetect() {
        return forceDetect;
    }

    public void setForceDetect(float forceDetect) {
        this.forceDetect = forceDetect;
    }

    public float getSpeedDetect() {
        return speedDetect;
    }

    public void setSpeedDetect(float speedDetect) {
        this.speedDetect = speedDetect;
    }

    public char getAccCode() {
        return accCode;
    }

    public void setAccCode(char accCode) {
        this.accCode = accCode;
    }

    @Override
    public String toString() {
        return "Accident{" + "userId=" + userId + ", date=" + date + ", time=" + time + ", latitude=" + latitude + ", longtitude=" + longtitude + ", forceDetect=" + forceDetect + ", speedDetect=" + speedDetect + ", accCode=" + accCode + '}';
    }

}
