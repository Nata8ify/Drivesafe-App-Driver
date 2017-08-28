package com.senior.g40.drivesafe.engines;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.senior.g40.drivesafe.R;
import com.senior.g40.drivesafe.models.Accident;
import com.senior.g40.drivesafe.utils.LocationUtils;
import com.senior.g40.drivesafe.weeworh.WWTo;

/**
 * Created by PNattawut on 16-Mar-17.
 */

public class CrashingSensorEngines implements SensorEventListener {
    private static Sensor accSensor;
    private static SensorManager sensorManager;

    private static Context context;
    private static MediaPlayer accMediaPlayer;

    private static CrashingSensorEngines crashingSensorEngines;


    public static CrashingSensorEngines getInstance(Context context) {
        if (crashingSensorEngines == null) {
            crashingSensorEngines = new CrashingSensorEngines();
            CrashingSensorEngines.context = context;
            accMediaPlayer = MediaPlayer.create(context, R.raw.accident);
        }
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

        return crashingSensorEngines;

    }

    public static boolean isRunning;
    public void start() {
        sensorManager.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_UI);
        isRunning = true;
    }

    public void stop() {
        isRunning = false;
        sensorManager.unregisterListener(this);
    }

    public static float accX;
    public static float accY;
    public static float accZ;
    public static double accLinear;
    public static double gs;
    public static double fixedGs;

    private boolean reqState;
    public String verbose;


    @Override
    public void onSensorChanged(SensorEvent event){
        accX = event.values[0];
        accY = event.values[1];
        accZ = event.values[2];
        accLinear = Math.sqrt(((accX * accX) + (accY * accY) + (accZ * accZ)));
        gs = accLinear / 9.8;
        this.txtOut.setText(" G's : " + String.valueOf(gs) + "\n Lat: " + LocationUtils.lat + "| Lng: " + LocationUtils.lng);
            if (gs >= Accident.GS_DEBUG && !reqState) {
                fixedGs = gs;
                accMediaPlayer.start();

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //---------- Outta View
    private TextView txtOut;

    public void setTxtviewOut(TextView txtOut) {
        this.txtOut = txtOut;
    }

    private Runnable falseWatcherRunnable;
    private Handler falseWatcherHandler;

    private void falseWatcher() {
        falseWatcherHandler = new Handler();
        falseWatcherRunnable = new Runnable() {
            @Override
            public void run() {
                if (LocationUtils.speed > 0) {
                    Toast.makeText(context, "False Accident Detected", Toast.LENGTH_LONG).show();
                    boolean isSuccess = WWTo.setSystemFalseAccident(context, Accident.getInstance());
                    Log.v("isSuccess: ", isSuccess + "");
                    try {
                        Thread.sleep(10000L);
                        falseWatcherHandler.removeCallbacks(this);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    falseWatcherHandler.post(this);
                }
            }
        };
        falseWatcherHandler.post(falseWatcherRunnable);
    }
}

