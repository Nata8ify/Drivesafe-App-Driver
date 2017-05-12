package com.senior.g40.drivesafe.utils;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

/**
 * Created by PNattawut on 26-Mar-17.
 */

public class LocationUtils {

    private LocationManager accLocationManager;
    private LocationListener accLocationListener;
    private double lat;
    private double lng;
    private float speed;
    private String mapUri;

    private static LocationUtils locationUtils;
    private static Context context;

    public static LocationUtils getInstance(Context context) {
        if (locationUtils == null) {
            LocationUtils.context = context;
            LocationUtils.locationUtils = new LocationUtils();
        }
        return locationUtils;
    }

    public LocationUtils() {

        // Initiate Location Modules.
        accLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        accLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lng = location.getLongitude();
                lat = location.getLatitude();
                speed = location.getSpeed();
                mapUri = "http://maps.google.com/maps?adaddr=" + lat + "," + lng;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        };
    }

    public void startLocationUpdate() {
        accLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, accLocationListener);
    }

    public void stopLocationUpdate() {
        accLocationManager.removeUpdates(accLocationListener);
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public float getSpeed() {
        return speed;
    }

    public String getMapUri() {
        return mapUri;
    }

    private boolean locationEnable;

    public boolean isLocationEnable() {
        return locationEnable;
    }
}
