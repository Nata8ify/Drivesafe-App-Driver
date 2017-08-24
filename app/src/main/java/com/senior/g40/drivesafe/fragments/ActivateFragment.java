package com.senior.g40.drivesafe.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.senior.g40.drivesafe.R;
import com.senior.g40.drivesafe.engines.CrashingSensorEngines;


public class ActivateFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_activate,container,false);
    }

    @Override
    public void onStart() {

        super.onStart();
/*
        private int activateState;
        crashingSensorEngines = CrashingSensorEngines.getInstance(getContext());
        crashingSensorEngines.setTxtviewOut(txtGs);

        Button btnActiveDrivesafe = (Button) getView().findViewById(R.id.btn_activeDrivesafe);
        public void onClick(View view) {
            activateState = 1 - activateState;
            if (activateState == 1) {
                btnActiveDrivesafe.setText(getResources().getString(R.string.main_drvdeactive));
                crashingSensorEngines.start();
            } else {
                btnActiveDrivesafe.setText(getResources().getString(R.string.main_drvactive));
                crashingSensorEngines.stop();
                txtGs.setText(" G's : CRASHING SENSOR STOP");
            }
        }*/



    }
}
