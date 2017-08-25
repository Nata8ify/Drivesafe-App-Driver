package com.senior.g40.drivesafe.engines;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.senior.g40.drivesafe.MainActivity;
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
    private static LocationUtils accLocationUtils;


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

        accLocationUtils = LocationUtils.getInstance(context);
        return crashingSensorEngines;

    }

    public void start() {
        sensorManager.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_UI);
    }

    public void stop() {
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

    /*@Override
    public void onSensorChanged(SensorEvent event){
        accX = event.values[0];
        accY = event.values[1];
        accZ = event.values[2];
        accLinear = Math.sqrt(((accX * accX) + (accY * accY) + (accZ * accZ)));
        gs = accLinear / 9.8;
        if (accLocationUtils.getLng() != 0.0f & accLocationUtils.getLng() != 0.0f) { //<-- Please use something waiting or somethings.
            this.txtOut.setText(" G's : " + String.valueOf(gs) + "\n Lat: " + accLocationUtils.getLat() + "| Lng: " + accLocationUtils.getLng());
            if (gs >= Accident.GS_DEBUG && !reqState) {
                fixedGs = gs;
                accMediaPlayer.start();
                ((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(5000l);
                Toast.makeText(context, "Detected! at : " + "" + gs + "\n Lat: " + accLocationUtils.getLat() + "\n Lng: " + accLocationUtils.getLng(), Toast.LENGTH_LONG).show();
                reqState = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Accident.setInstance(WWTo.crashRescueRequest(context, accLocationUtils.getLat(), accLocationUtils.getLng(), Math.round(fixedGs), accLocationUtils.getSpeed()));
                        Toast.makeText(context, "Accident Information Saved." + Accident.getInstance().toString(), Toast.LENGTH_LONG).show();
                        Log.v("Accident.getInsatance()", Accident.getInstance().toString());
                        reqState = false;
                        falseWatcher();
                    }
                }, 5000l);
            }
        } else {
            this.txtOut.setText("Service is Starting...");
        }
    }*/

    public AlertDialog alertDialog;
    @Override
    public void onSensorChanged(SensorEvent event){
        accX = event.values[0];
        accY = event.values[1];
        accZ = event.values[2];
        accLinear = Math.sqrt(((accX * accX) + (accY * accY) + (accZ * accZ)));
        gs = accLinear / 9.8;
        if (accLocationUtils.getLng() != 0.0f & accLocationUtils.getLng() != 0.0f) { //<-- Please use something waiting or somethings.
            this.txtOut.setText(" G's : " + String.valueOf(gs) + "\n Lat: " + accLocationUtils.getLat() + "| Lng: " + accLocationUtils.getLng());
            if (gs >= Accident.GS_DEBUG && !reqState) {
                fixedGs = gs;
                accMediaPlayer.start();

                if(alertDialog!=null && alertDialog.isShowing()) return;
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Accident Detected");
                builder.setMessage("You have 00:30 second to respond before the application auto send the accident location to the rescuer team." +
                        "If you want to call for help please tap 'Yes' if you don't want any help please tap 'No'");
                builder.setCancelable(true);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reqState = false;
                        Accident.setInstance(WWTo.crashRescueRequest(context, accLocationUtils.getLat(), accLocationUtils.getLng(), Math.round(fixedGs), accLocationUtils.getSpeed()));
                        dialog.dismiss();
                        Toast.makeText(context,"Accident Location send",Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        falseWatcher();
                        dialog.cancel();
                    }
                });
                alertDialog = builder.create();
               /* try {
                    alertDialog.show();
                } catch(Exception exp){
                }*/

            }
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
                if (accLocationUtils.getSpeed() > 0) {
                    Toast.makeText(context, "False Accident Detected" + accLocationUtils.getLng(), Toast.LENGTH_LONG).show();
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

