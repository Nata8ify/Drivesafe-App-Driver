package com.senior.g40.drivesafe.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.senior.g40.drivesafe.AlertActivity;
import com.senior.g40.drivesafe.MainActivity;
import com.senior.g40.drivesafe.R;
import com.senior.g40.drivesafe.engines.CrashingSensorEngines;
import com.senior.g40.drivesafe.fragments.ActivateFragment;
import com.senior.g40.drivesafe.models.Accident;

/**
 * Created by PNattawut on 22-Apr-17.
 */

public class CrashDetectionService extends IntentService {

    public CrashDetectionService() {
        super("Rescue Request Service");
    }

    public CrashDetectionService(String name) {
        super(name);
    }

    private BroadcastReceiver serviceBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            stopService(new Intent(CrashDetectionService.this,CrashDetectionService.class));
            Log.d("on","Do or Not");
        }
    };

    private boolean isRequestDialogPrompted;
    @Override
    public void onCreate() {
        super.onCreate();
        sensorHandler = new Handler();
        sensorRunnable = new Runnable() {
            @Override
            public void run() {
                CrashingSensorEngines.getInstance(CrashDetectionService.this).start();
                sensorHandler.post(this);
                isSensorActived = true;
                if(CrashingSensorEngines.gs >= Accident.GS_DEBUG){
                    sensorHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isRequestDialogPrompted = false;
                        }
                    }, 3000);
                    if(!isRequestDialogPrompted && !AlertActivity.isAlertActivityPrompted || true) {
                        Intent main = new Intent(CrashDetectionService.this, AlertActivity.class);
                        main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(main);
                        isRequestDialogPrompted = true;
                    }
                }
            }
        };
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

    }

    private Runnable sensorRunnable;
    private Handler sensorHandler;
    public static boolean isSensorActived;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!isSensorActived) {
            registerReceiver(serviceBroadcastReceiver, new IntentFilter("stopSelf"));
            PendingIntent pdIntent = PendingIntent.getBroadcast(this, 0, new Intent("stopSelf"), PendingIntent.FLAG_CANCEL_CURRENT);
            Notification notification = new Notification.Builder(this)
                    .setSmallIcon(R.mipmap.icon)
                    .setContentIntent(pdIntent)
                    .setContentTitle("Crash Detection Service is Running")
                    .setContentText("Tap this Notification to Stop...").build();
            startForeground(1, notification);
            sensorHandler.post(sensorRunnable);
            isSensorActived = true;
        }return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(serviceBroadcastReceiver);
        CrashingSensorEngines.getInstance(CrashDetectionService.this).stop();
        sensorHandler.removeCallbacks(sensorRunnable);
        isSensorActived = false;
        ActivateFragment.activateState[0] = 1;
        Log.v("onDestroy: ", true + "");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }


}
