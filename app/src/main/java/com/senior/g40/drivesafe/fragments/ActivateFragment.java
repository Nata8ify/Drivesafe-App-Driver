package com.senior.g40.drivesafe.fragments;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.senior.g40.drivesafe.MainActivity;
import com.senior.g40.drivesafe.R;
import com.senior.g40.drivesafe.engines.CrashingSensorEngines;
import com.senior.g40.drivesafe.services.CrashDetectionService;
import com.senior.g40.drivesafe.utils.LocationUtils;


public class ActivateFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_activate, container, false);
    }

    @Override
    public void onStart() {

        super.onStart();

        final TextView txtGs = (TextView) getView().findViewById(R.id.txt_gs);
        final Button btnActiveDrivesafe = (Button) getView().findViewById(R.id.btn_activeDrivesafe);
        final Button btnActiveDrivesafeService = (Button) getView().findViewById(R.id.btn_activeDrivesafeService);
        final CrashingSensorEngines crashingSensorEngines;
        crashingSensorEngines = CrashingSensorEngines.getInstance(getContext());
        crashingSensorEngines.setTxtviewOut(txtGs);

        final int activateServiceState;
/*
        protected void onResume(){
            super.onResume();
            txtGs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(LocationUtils.getInstance(getContext()).getMapUri())));
                }
            });

            btnActiveDrivesafeService.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (1 - activateServiceState == 1) {
                        activateServiceState = 1;
                        startService(new Intent(getContext(), CrashDetectionService.class));
                        btnActiveDrivesafeService.setText(R.string.main_drvpauseserv);
                    } else {
                        btnActiveDrivesafeService.setText(R.string.main_drvactiveserv);
                        activateServiceState = 0;
                        stopService(new Intent(getContext(), CrashDetectionService.class));
                    }
                }


            });
        }
*/

        final int[] activateState = new int[1];
        btnActiveDrivesafe.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                activateState[0] = 1 - activateState[0];
                if (activateState[0] == 1) {
                    btnActiveDrivesafe.setText(getResources().getString(R.string.main_drvdeactive));
                    crashingSensorEngines.start();
                } else {
                    btnActiveDrivesafe.setText(getResources().getString(R.string.main_drvactive));
                    crashingSensorEngines.stop();
                    txtGs.setText(" G's : CRASHING SENSOR STOP");
                }
            }
        });

/*

    public void validatePermission() {
        // Performs Permission Checking.
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, GRANT_FINE_LOCATION);
    }

    private final int GRANT_FINE_LOCATION = 1;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults[0] == RESULT_CANCELED) {
            switch (requestCode) {
                case GRANT_FINE_LOCATION:
                    break;
            }
        } else {
            toast("Permission is required, without this permission, application will be terminated.");
            finish();
        }
    }*/


    }
}
