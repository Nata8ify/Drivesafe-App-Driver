package com.senior.g40.drivesafe.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.senior.g40.drivesafe.engines.CrashingSensorEngines;

/**
 * Created by PNattawut on 22-Apr-17.
 */

public class CrashDetectionService extends IntentService {

    private CrashingSensorEngines crashingSensorEngines;


    public CrashDetectionService() {
        super("Rescue Request Service");
    }
    public CrashDetectionService(String name) {
        super(name);
    }


    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        crashingSensorEngines = CrashingSensorEngines.getInstance(this);
        crashingSensorEngines.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        crashingSensorEngines.stop();
        Log.v("onDestroy: ", true+"");
    }

    int i;
    @Override
    protected void onHandleIntent(Intent intent) {
        while (true){
            ++i;
            try {
                Thread.sleep(3000);
                Log.v("I: ", i+"");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class RunningCDSAsyncTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

    }
}
