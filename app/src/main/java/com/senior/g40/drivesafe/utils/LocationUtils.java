package com.senior.g40.drivesafe.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

/**
 * Created by PNattawut on 26-Mar-17.
 */

public class LocationUtils {

    private LocationManager accLocationManager;
    private LocationListener accLocationListener;
    public static double lat;
    public static double lng;
    public static  float speed;
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
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        accLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, accLocationListener);
    }

    public void stopLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
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
