package com.senior.g40.drivesafe.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.senior.g40.drivesafe.R;
import com.senior.g40.drivesafe.engines.CrashingSensorEngines;
import com.senior.g40.drivesafe.services.CrashDetectionService;


public class ActivateFragment extends Fragment {

    private Handler activateFragmentHandler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activateFragmentHandler = new Handler();
        return inflater.inflate(R.layout.fragment_activate, container, false);
    }

    public static int[] activateState = new int[1];
    @Override
    public void onStart() {
        super.onStart();

        final TextView txtGs = (TextView) getView().findViewById(R.id.txt_gs);
        final Button btnActiveDrivesafe = (Button) getView().findViewById(R.id.btn_activeDrivesafe);
        final Button btnActiveDrivesafeService = (Button) getView().findViewById(R.id.btn_activeDrivesafeService);
        final CrashingSensorEngines crashingSensorEngines;
        crashingSensorEngines = CrashingSensorEngines.getInstance(getContext());
        crashingSensorEngines.setTxtviewOut(txtGs);
        if(CrashingSensorEngines.isRunning){
            btnActiveDrivesafe.setText(getResources().getString(R.string.main_drvdeactive));
        }


        btnActiveDrivesafe.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent service =  new Intent(getContext(), CrashDetectionService.class);
                activateState[0] = 1 - activateState[0];
                if (activateState[0] == 1) {
                    btnActiveDrivesafe.setText(getResources().getString(R.string.main_drvdeactive));
                    getActivity().startService(service);
                } else {
                    btnActiveDrivesafe.setText(getResources().getString(R.string.main_drvactive));
                    activateFragmentHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            txtGs.setText("CRASHING SENSOR STOP");
                        }
                    }, 100);
                    getActivity().stopService(service);

                }
            }
        });
    }


    }







