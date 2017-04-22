package com.senior.g40.drivesafe.engines;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.github.pires.obd.commands.protocol.EchoOffCommand;
import com.senior.g40.drivesafe.R;
import com.senior.g40.drivesafe.models.Accident;
import com.senior.g40.drivesafe.utils.Drivesafe;
import com.senior.g40.drivesafe.utils.LocationUtils;

import java.io.IOException;
import java.util.UUID;

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
        accLocationUtils.startLocationUpdate();
    }

    public void stop() {
        sensorManager.unregisterListener(this);
        accLocationUtils.stopLocationUpdate();
    }

    public static float accX;
    public static float accY;
    public static float accZ;
    public static double accLinear;
    public static double gs;


    private boolean reqState;

    @Override
    public void onSensorChanged(SensorEvent event) {
        accX = event.values[0];
        accY = event.values[1];
        accZ = event.values[2];
        accLinear = Math.sqrt(((accX * accX) + (accY * accY) + (accZ * accZ)));
        gs = accLinear / 9.8;
        if(accLocationUtils.getLng() != 0.0f & accLocationUtils.getLng() != 0.0f) { //<-- Please use something waiting or somethings.
            this.txtOut.setText(" G's : " + String.valueOf(gs) + "\n Latitude: " + accLocationUtils.getLat() + "\n Longitude: " + accLocationUtils.getLng());
            if (gs >= Accident.GS_DEBUG && !reqState) {
                //TODO
                accMediaPlayer.start();
                ((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(5000l);
                Toast.makeText(context, "Detected! at : " + "" + gs + "\n Lat: " + accLocationUtils.getLat() + "\n Lng: " + accLocationUtils.getLng(), Toast.LENGTH_LONG).show();
                reqState = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Accident.setInstance(Drivesafe.rescuseRequest(context, accLocationUtils.getLat(), accLocationUtils.getLng(), Math.round(gs), accLocationUtils.getSpeed()));
                        Toast.makeText(context, "Accident Information Saved." + accLocationUtils.getLng(), Toast.LENGTH_LONG).show();
                        reqState = false;
                        falseWatcher();
                    }
                }, 5000l);
            }
        } else {
            this.txtOut.setText("Service is Starting...");
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
    private void falseWatcher(){
        falseWatcherHandler = new Handler();
        falseWatcherRunnable = new Runnable() {
            @Override
            public void run() {
                    if(accLocationUtils.getSpeed() > 0){
                        Toast.makeText(context, "False Accident Detected" + accLocationUtils.getLng(), Toast.LENGTH_LONG).show();
                        boolean isSuccess = Drivesafe.setSystemFalseAccident(context, Accident.getInsatance());
                        Log.v("isSuccess: ",isSuccess+"");
                        try {
                            Thread.sleep(10000L);
                            falseWatcherHandler.removeCallbacks(this);
                        } catch (InterruptedException e) {e.printStackTrace();}
                    } else {
                        falseWatcherHandler.post(this);
                    }
            }
        };
        falseWatcherHandler.post(falseWatcherRunnable);
    }

    private class TryODB2 {
        BluetoothDevice btDevice = null;
        BluetoothSocket btSocket = null;
        BluetoothManager btManager = null;
        UUID obd2UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        public TryODB2() {
            btManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            btDevice = btManager.getAdapter().getRemoteDevice("");
            try {

                btSocket = btDevice.createRfcommSocketToServiceRecord(obd2UUID);
                btSocket.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                new EchoOffCommand().run(btSocket.getInputStream(), btSocket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
