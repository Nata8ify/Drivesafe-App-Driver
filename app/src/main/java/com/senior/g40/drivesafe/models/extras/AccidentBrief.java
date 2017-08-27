package com.senior.g40.drivesafe.models.extras;

import com.senior.g40.drivesafe.models.Accident;

import java.sql.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by PNattawut on 27-Aug-17.
 */

public class AccidentBrief extends RealmObject{
    @PrimaryKey
    private long accidentId;

    private long userId;
    private double latitude;
    private double longitude;

    public AccidentBrief() {
    }

    public AccidentBrief(Accident accident) {
        this.accidentId = accident.getAccidentId();
        this.userId = accident.getUserId();
        this.latitude = accident.getLatitude();
        this.longitude = accident.getLongitude();
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

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "AccidentBrief{" +
                "accidentId=" + accidentId +
                ", userId=" + userId +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
