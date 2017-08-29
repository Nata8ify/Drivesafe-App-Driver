package com.senior.g40.drivesafe.weeworh;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.koushikdutta.ion.Ion;
import com.senior.g40.drivesafe.models.Accident;
import com.senior.g40.drivesafe.models.Profile;

import java.util.concurrent.ExecutionException;

/**
 * Created by PNattawut on 14-May-17.
 */

public class WWTo {


    // --* Prototype Method. -> Not Finalized

    /**
     * Report Crash Incident (Along with build-in Sensor Detection Information)
     *
     * @param context     Sending Activity's Context.
     * @param latitude    Incident Location's Latitude.
     * @param longitude   Incident Location's Longitude.
     * @param forceDetect Value of Force (As G Unit) can Detected After Crash was Occurred
     * @param speedDetect Value of Speed can Detected After Crash was Occurred
     * @return The Incident Information after it was stored.
     */
    public static Accident crashRescueRequest(@NonNull Context context, @NonNull double latitude, @NonNull double longitude, @NonNull double forceDetect, @NonNull float speedDetect) {
        String response = null;
        try {
            response = Ion.with(context)
                    .load(WWProp.URI.CRASH_HIT)
                    .setBodyParameter(WWProp.PARAM.USRID, String.valueOf(Profile.getInsatance().getUserId()))
                    .setBodyParameter(WWProp.PARAM.LAT, String.valueOf(latitude))
                    .setBodyParameter(WWProp.PARAM.LNG, String.valueOf(longitude))
                    .setBodyParameter(WWProp.PARAM.FDT, String.valueOf(forceDetect))
                    .setBodyParameter(WWProp.PARAM.SDT, String.valueOf(speedDetect))
                    .asString()
                    .get();
            Log.v("response", response);
            return new GsonBuilder().setDateFormat("yyyy-MM-dd").create().fromJson(response, Accident.class);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Report Crash Incident (Non build-in Sensor Detection Information)
     *
     * @param context   Sending Activity's Context.
     * @param latitude  Incident Location's Latitude.
     * @param longitude Incident Location's Longitude.
     * @return The Incident Information after it was stored.
     */
    public static Accident crashRescueRequest(@NonNull Context context, @NonNull double latitude, @NonNull double longitude) {
        String response = null;
        Log.v("latlng", latitude + "::::" + longitude + "::" + String.valueOf(Accident.ACC_TYPE_TRAFFIC));
        try {
            response = Ion.with(context)
                    .load(WWProp.URI.INCIDENT_REPORT)
                    .setBodyParameter(WWProp.PARAM.USRID, String.valueOf(Profile.getInsatance().getUserId()))
                    .setBodyParameter(WWProp.PARAM.LAT, String.valueOf(latitude))
                    .setBodyParameter(WWProp.PARAM.LNG, String.valueOf(longitude))
                    .setBodyParameter(WWProp.PARAM.ACTYP, String.valueOf(Accident.ACC_TYPE_TRAFFIC))
                    .asString()
                    .get();
            Log.v("res", response);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.v("res 1", response);
        } catch (ExecutionException e) {
            e.printStackTrace();
            Log.v("res 2", response);
        }
        return new GsonBuilder().setDateFormat("yyyy-MM-dd").create().fromJson(response, Accident.class);
    }

    /**
     * Report Fire Incident
     *
     * @param context   Sending Activity's Context.
     * @param latitude  Incident Location's Latitude.
     * @param longitude Incident Location's Longitude.
     * @return The Incident Information after it was stored.
     */
    public static Accident fireRescueRequest(@NonNull Context context, @NonNull double latitude, @NonNull double longitude) {
        String response = null;
        Log.v("latlng", latitude + "::::" + longitude + "::" + String.valueOf(Accident.ACC_TYPE_FIRE));
        try {
            response = Ion.with(context)
                    .load(WWProp.URI.INCIDENT_REPORT)
                    .setBodyParameter(WWProp.PARAM.USRID, String.valueOf(Profile.getInsatance().getUserId()))
                    .setBodyParameter(WWProp.PARAM.LAT, String.valueOf(latitude))
                    .setBodyParameter(WWProp.PARAM.LNG, String.valueOf(longitude))
                    .setBodyParameter(WWProp.PARAM.ACTYP, String.valueOf(Accident.ACC_TYPE_FIRE))
                    .asString()
                    .get();
            Log.v("res", response);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.v("res 1", response);
        } catch (ExecutionException e) {
            e.printStackTrace();
            Log.v("res 2", response);
        }
        return new GsonBuilder().setDateFormat("yyyy-MM-dd").create().fromJson(response, Accident.class);
    }

    /**
     * Report Brawl Incident
     *
     * @param context   Sending Activity's Context.
     * @param latitude  Incident Location's Latitude.
     * @param longitude Incident Location's Longitude.
     * @return The Incident Information after it was stored.
     */
    public static Accident brawlRescueRequest(@NonNull Context context, @NonNull double latitude, @NonNull double longitude) {
        String response = null;
        Log.v("latlng", latitude + "::::" + longitude + "::" + String.valueOf(Accident.ACC_TYPE_BRAWL));
        try {
            response = Ion.with(context)
                    .load(WWProp.URI.INCIDENT_REPORT)
                    .setBodyParameter(WWProp.PARAM.USRID, String.valueOf(Profile.getInsatance().getUserId()))
                    .setBodyParameter(WWProp.PARAM.LAT, String.valueOf(latitude))
                    .setBodyParameter(WWProp.PARAM.LNG, String.valueOf(longitude))
                    .setBodyParameter(WWProp.PARAM.ACTYP, String.valueOf(Accident.ACC_TYPE_BRAWL))
                    .asString()
                    .get();
            Log.v("res", response);
            return new GsonBuilder().setDateFormat("yyyy-MM-dd").create().fromJson(response, Accident.class);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.v("res 1", response);
        } catch (ExecutionException e) {
            e.printStackTrace();
            Log.v("res 2", response);
        }
        return null;
    }

    /**
     * Report Animal Incident
     *
     * @param context   Sending Activity's Context.
     * @param latitude  Incident Location's Latitude.
     * @param longitude Incident Location's Longitude.
     * @return The Incident Information after it was stored.
     */
    public static Accident animalRescueRequest(@NonNull Context context, @NonNull double latitude, @NonNull double longitude) {
        String response = null;
        Log.v("latlng", latitude + "::::" + longitude + "::" + String.valueOf(Accident.ACC_TYPE_ANIMAL));
        try {
            response = Ion.with(context)
                    .load(WWProp.URI.INCIDENT_REPORT)
                    .setBodyParameter(WWProp.PARAM.USRID, String.valueOf(Profile.getInsatance().getUserId()))
                    .setBodyParameter(WWProp.PARAM.LAT, String.valueOf(latitude))
                    .setBodyParameter(WWProp.PARAM.LNG, String.valueOf(longitude))
                    .setBodyParameter(WWProp.PARAM.ACTYP, String.valueOf(Accident.ACC_TYPE_ANIMAL))
                    .asString()
                    .get();
            Log.v("res", response);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.v("res 1", response);
        } catch (ExecutionException e) {
            e.printStackTrace();
            Log.v("res 2", response);
        }
        return new GsonBuilder().setDateFormat("yyyy-MM-dd").create().fromJson(response, Accident.class);
    }

    /**
     * Report Patient Incident
     *
     * @param context   Sending Activity's Context.
     * @param latitude  Incident Location's Latitude.
     * @param longitude Incident Location's Longitude.
     * @return The Incident Information after it was stored.
     */
    public static Accident patientRescueRequest(@NonNull Context context, @NonNull double latitude, @NonNull double longitude) {
        String response = null;
        Log.v("latlng", latitude + "::::" + longitude + "::" + String.valueOf(Accident.ACC_TYPE_PATIENT));
        try {
            response = Ion.with(context)
                    .load(WWProp.URI.INCIDENT_REPORT)
                    .setBodyParameter(WWProp.PARAM.USRID, String.valueOf(Profile.getInsatance().getUserId()))
                    .setBodyParameter(WWProp.PARAM.LAT, String.valueOf(latitude))
                    .setBodyParameter(WWProp.PARAM.LNG, String.valueOf(longitude))
                    .setBodyParameter(WWProp.PARAM.ACTYP, String.valueOf(Accident.ACC_TYPE_PATIENT))
                    .asString()
                    .get();
            Log.v("res", response);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.v("res 1", response);
        } catch (ExecutionException e) {
            e.printStackTrace();
            Log.v("res 2", response);
        }
        return new GsonBuilder().setDateFormat("yyyy-MM-dd").create().fromJson(response, Accident.class);
    }

    /**
     * Report Other Incident
     *
     * @param context   Sending Activity's Context.
     * @param latitude  Incident Location's Latitude.
     * @param longitude Incident Location's Longitude.
     * @return The Incident Information after it was stored.
     */
    public static Accident otherRescueRequest(@NonNull Context context, @NonNull double latitude, @NonNull double longitude) {
        String response = null;
        Log.v("latlng", latitude + "::::" + longitude + "::" + String.valueOf(Accident.ACC_TYPE_OTHER));
        try {
            response = Ion.with(context)
                    .load(WWProp.URI.INCIDENT_REPORT)
                    .setBodyParameter(WWProp.PARAM.USRID, String.valueOf(Profile.getInsatance().getUserId()))
                    .setBodyParameter(WWProp.PARAM.LAT, String.valueOf(latitude))
                    .setBodyParameter(WWProp.PARAM.LNG, String.valueOf(longitude))
                    .setBodyParameter(WWProp.PARAM.ACTYP, String.valueOf(Accident.ACC_TYPE_OTHER))
                    .asString()
                    .get();
            Log.v("res", response);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.v("res 1", response);
        } catch (ExecutionException e) {
            e.printStackTrace();
            Log.v("res 2", response);
        }
        return new GsonBuilder().setDateFormat("yyyy-MM-dd").create().fromJson(response, Accident.class);
    }

    public static Accident updateCurrentReportedIncident(@NonNull Context context, long accidentId) {
        try {
            return  Ion.with(context)
                    .load(WWProp.URI.GET_UPDATE_REPORTED_INCIDENT)
                    .setBodyParameter(WWProp.PARAM.ACCID, String.valueOf(accidentId))
                    .as(Accident.class)
                    .get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean setSystemFalseAccident(Context context, Accident accident) {
        try {
            boolean isSuccess = Boolean.parseBoolean(Ion.with(context)
                    .load(WWProp.URI.SYS_FALSE_CRASH)
                    .setBodyParameter("accid", String.valueOf(accident.getAccidentId()))
                    .asString()
                    .get().trim());

            return isSuccess;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean setUserFalseAccident(Context context, Accident accident) {
        String response = null;
        try {
            response = Ion.with(context)
                    .load(WWProp.URI.USR_FALSE_CRASH)
                    .setBodyParameter(WWProp.PARAM.ACCID, String.valueOf(accident.getAccidentId()))
                    .setBodyParameter(WWProp.PARAM.USRID, String.valueOf(accident.getUserId()))
                    .asString()
                    .get().trim();
            Log.v("boolean res", response);
            return Boolean.parseBoolean(response);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean setUserFalseAccidentId(Context context, long accidentId, long userId) {
        String response = null;
        try {
            response = Ion.with(context)
                    .load(WWProp.URI.USR_FALSE_CRASH)
                    .setBodyParameter(WWProp.PARAM.ACCID, String.valueOf(accidentId))
                    .setBodyParameter(WWProp.PARAM.USERID, String.valueOf(userId))
                    .asString()
                    .get().trim();
            Log.v("boolean res", response);
            return Boolean.parseBoolean(response);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }
}
