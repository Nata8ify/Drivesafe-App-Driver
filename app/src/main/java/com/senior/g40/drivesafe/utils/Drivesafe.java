package com.senior.g40.drivesafe.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koushikdutta.ion.Ion;
import com.senior.g40.drivesafe.models.Accident;
import com.senior.g40.drivesafe.models.Profile;

import java.util.concurrent.ExecutionException;

/**
 * Created by PNattawut on 16-Mar-17.
 */

public class Drivesafe {
    public static final String HOST = "http://103.253.146.87:8080";
    //    public static final String DRIVESAFE_HOST = "http://103.253.146.87:8080/Drivesafe-1.0-SNAPSHOT/";
    public static final String DRIVESAFE_HOST = "http://103.253.146.87:8080/Drivesafe/";

    public class URI {
        public static final String LOGIN = DRIVESAFE_HOST + "DriverIn?opt=login&utyp=M";
        public static final String CRASH_HIT = DRIVESAFE_HOST + "DriverIn?opt=acchit";
        public static final String SYS_FALSE_CRASH = DRIVESAFE_HOST + "DriverIn?opt=sys_accfalse";
    }
    public class PARAM {
        /* Login Parameters */
        public static final String USRN = "usrn";
        public static final String PSWD = "pswd";
        public static final String UTYP = "utyp";

        /* Request for Rescue Parameters */
        public static final String LAT = "lat"; // Latitude
        public static final String LNG = "lng"; // Longitude
        public static final String FDT = "fdt"; // Force Detect
        public static final String SDT = "sdt"; // Speed Detect
        public static final String USRID = "usrid"; // UserID
        public static final String ACCC = "accc"; // Accident Code : Crash Code

    }

    // --* Prototype Method. -> Not Finalized
    public static Accident rescuseRequest(Context context, double latitude, double longitude, double forceDetect, float speedDetect) {
        String response = null;
        try {
            response = Ion.with(context)
                    .load(URI.CRASH_HIT)
                    .setBodyParameter(PARAM.USRID, String.valueOf(Profile.getInsatance().getUserId()))
                    .setBodyParameter(PARAM.LAT, String.valueOf(latitude))
                    .setBodyParameter(PARAM.LNG, String.valueOf(longitude))
                    .setBodyParameter(PARAM.FDT, String.valueOf(forceDetect))
                    .setBodyParameter(PARAM.SDT, String.valueOf(speedDetect))
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

    public static boolean setSystemFalseAccident(Context context, Accident accident) {
        try {
            return Boolean.parseBoolean(Ion.with(context)
            .load(URI.SYS_FALSE_CRASH)
            .setBodyParameter("accid", String.valueOf(accident.getAccId()))
            .asString()
            .get().trim());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }
}
