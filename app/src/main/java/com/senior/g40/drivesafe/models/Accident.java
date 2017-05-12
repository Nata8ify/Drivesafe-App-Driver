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

    private long accidentId;
    private long userId;
    private Date date;
    private String time;
    private float latitude;
    private float longitude;
    private float forceDetect;
    private float speedDetect;
    //-- accCode & accType is have very importance role.
    private byte accType;
    private char accCode;

    public static final byte ACC_TYPE_TRAFFIC = 1;
    public static final byte ACC_TYPE_FIRE = 2;
    public static final byte ACC_TYPE_BRAWL = 3;
    public static final byte ACC_TYPE_ANIMAL = 4;
    public static final byte ACC_TYPE_OTHER = 5;

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

    public Accident(long accId, long userId, Date date, String time, float latitude, float longtitude, float forceDetect, float speedDetect, byte accType, char accCode) {
        this.accidentId = accId;
        this.userId = userId;
        this.date = date;
        this.time = time;
        this.latitude = latitude;
        this.longitude = longtitude;
        this.forceDetect = forceDetect;
        this.speedDetect = speedDetect;
        this.accType = accType;
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

    public long getAccidentId() {
        return accidentId;
    }

    public void setAccidentId(long accidentId) {
        this.accidentId = accidentId;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongtitude(float longitude) {
        this.longitude = longitude;
    }

    public double getForceDetect() {
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

    public byte getAccType() {
        return accType;
    }

    public void setAccType(byte accType) {
        this.accType = accType;
    }

    @Override
    public String toString() {
        return "Accident{" + "accidentId=" + accidentId + ", userId=" + userId + ", date=" + date + ", time=" + time + ", latitude=" + latitude + ", longitude=" + longitude + ", forceDetect=" + forceDetect + ", speedDetect=" + speedDetect + ", accType=" + accType + ", accCode=" + accCode + '}';
    }
}
