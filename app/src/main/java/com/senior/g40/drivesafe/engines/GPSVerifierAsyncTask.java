package com.senior.g40.drivesafe.engines;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.senior.g40.drivesafe.R;
import com.senior.g40.drivesafe.utils.LocationUtils;

/**
 * Created by PNattawut on 27-Aug-17.
 */

public class GPSVerifierAsyncTask extends AsyncTask {

    private ProgressDialog progressDialog;
    private Context context;

    public GPSVerifierAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        LocationUtils.getInstance(context);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getString(R.string.inprogress));
        progressDialog.setCancelable(false);
        progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Object[] params) {
        while(LocationUtils.lat == 0 && LocationUtils.lng == 0){
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        progressDialog.cancel();
        super.onPostExecute(o);
    }

}
