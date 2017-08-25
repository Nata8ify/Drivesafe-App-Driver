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

    @Override
    public void onCreate() {
        super.onCreate();
        sensorHandler = new Handler();
        sensorRunnable = new Runnable() {
            @Override
            public void run() {
                CrashingSensorEngines.getInstance(CrashDetectionService.this).start();
                Log.d("G's", CrashingSensorEngines.gs+"");
                sensorHandler.post(this);
                isSensorActived = true;
                if(CrashingSensorEngines.gs >= Accident.GS_DEBUG){
                    Intent main = new Intent(CrashDetectionService.this, AlertActivity.class);
                    main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(main);

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
    private boolean isSensorActived;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!isSensorActived) {
            registerReceiver(serviceBroadcastReceiver, new IntentFilter("stopSelf"));
            PendingIntent pdIntent = PendingIntent.getBroadcast(this, 0, new Intent("stopSelf"), PendingIntent.FLAG_CANCEL_CURRENT);
            Notification notification = new Notification.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pdIntent)
                    .setContentTitle("Service Running")
                    .setContentText("Tap to Stop...").build();
            NotificationManager mng = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            startForeground(1, notification);
            sensorHandler.post(sensorRunnable);
        }return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(serviceBroadcastReceiver);
        CrashingSensorEngines.getInstance(CrashDetectionService.this).stop();
        sensorHandler.removeCallbacks(sensorRunnable);
        isSensorActived = false;
        Log.v("onDestroy: ", true + "");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }


}
