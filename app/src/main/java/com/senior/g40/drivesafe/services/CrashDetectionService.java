package com.senior.g40.drivesafe.services;

import android.app.IntentService;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Handler;
import android.util.Log;

import com.senior.g40.drivesafe.engines.CrashingSensorEngines;

/**
 * Created by PNattawut on 22-Apr-17.
 */

public class CrashDetectionService extends IntentService implements SensorEventListener {

    private CrashingSensorEngines crashingSensorEngines;
    private Intent intentServiceCDS;

    public CrashDetectionService() {
        super("Rescue Request Service");
    }

    public CrashDetectionService(String name) {
        super(name);
    }


    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

    }

    private Runnable sensorRunnable;
    private Handler sensorHandler;
    private boolean isSensorActived;
    int i;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        intentServiceCDS = intent;
        sensorRunnable = new Runnable() {
            @Override
            public void run() {
                //TODO.
            }
        };
        sensorHandler = new Handler();
        isSensorActived = true;
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isSensorActived = false;
        Log.v("onDestroy: ", true + "");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        sensorHandler.post(sensorRunnable);
    }

    public static float accX;
    public static float accY;
    public static float accZ;
    public static double accLinear;
    public static double gs;
    @Override
    public void onSensorChanged(SensorEvent event) {
        accX = event.values[0];
        accY = event.values[1];
        accZ = event.values[2];
        accLinear = Math.sqrt(((accX * accX) + (accY * accY) + (accZ * accZ)));
        gs = accLinear / 9.8;
        if(gs >= 3 & isSensorActived){
        Log.v("gs", "DEBUG G's.");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
