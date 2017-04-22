package com.senior.g40.drivesafe.engines;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.ion.Ion;
import com.senior.g40.drivesafe.models.Profile;
import com.senior.g40.drivesafe.models.User;
import com.senior.g40.drivesafe.utils.Drivesafe;

import java.util.concurrent.ExecutionException;

/**
 * Created by PNattawut on 16-Mar-17.
 */

public class UserEngines {
    private static UserEngines userEngines;
    private static Context appContext;

    public static UserEngines getInstance(Context context) {
        if (userEngines == null) {
            userEngines = new UserEngines();
        }
        appContext = context;
        return userEngines;
    }

    public boolean login(String username, String password) {
        String callback = null;
        try {
            callback = Ion.with(appContext)
                    .load(Drivesafe.URI.LOGIN)
                    .setBodyParameter(Drivesafe.PARAM.USRN, username)
                    .setBodyParameter(Drivesafe.PARAM.PSWD, password)
                    .asString()
                    .get();
            Gson gson = new Gson();
            Profile profile = gson.fromJson(callback, Profile.class);
            if (profile != null) {
                Profile.createInsatance(profile);
                return true;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
