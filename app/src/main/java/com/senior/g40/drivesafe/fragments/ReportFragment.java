package com.senior.g40.drivesafe.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.senior.g40.drivesafe.R;
import com.senior.g40.drivesafe.models.Accident;
import com.senior.g40.drivesafe.utils.LocationUtils;
import com.senior.g40.drivesafe.utils.SettingVerify;
import com.senior.g40.drivesafe.weeworh.WWTo;


public class ReportFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_report, container, false);

    }

    @Override
    public void onStart() {
        super.onStart();

        Button btnReportCrash = (Button) getView().findViewById(R.id.btn_crash_opt);
        btnReportCrash.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Accident crashAcc = WWTo.crashRescueRequest(getContext(), LocationUtils.getInstance(getContext()).getLat(), LocationUtils.getInstance(getContext()).getLng());
                Accident.setInstance(crashAcc);
                Toast.makeText(getContext(), crashAcc.toString(), Toast.LENGTH_LONG).show();
                return false;
            }


        });

        Button btnReportFire = (Button) getView().findViewById(R.id.btn_fire_opt);
        btnReportFire.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Accident fireAcc = WWTo.fireRescueRequest(getContext(), LocationUtils.getInstance(getContext()).getLat(), LocationUtils.getInstance(getContext()).getLng());
                Accident.setInstance(fireAcc);
                Toast.makeText(getContext(), fireAcc.toString(), Toast.LENGTH_LONG).show();
                return false;
            }

        });

        Button btnReportBrawl = (Button) getView().findViewById(R.id.btn_brawl_opt);
        btnReportBrawl.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Accident brawlAcc = WWTo.brawlRescueRequest(getContext(), LocationUtils.getInstance(getContext()).getLat(), LocationUtils.getInstance(getContext()).getLng());
                Accident.setInstance(brawlAcc);
                Toast.makeText(getContext(), brawlAcc.toString(), Toast.LENGTH_LONG).show();
                return false;
            }

        });

        Button btnReportPatient = (Button) getView().findViewById(R.id.btn_patient_opt);
        btnReportPatient.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Accident patientAcc = WWTo.patientRescueRequest(getContext(), LocationUtils.getInstance(getContext()).getLat(), LocationUtils.getInstance(getContext()).getLng());
                Accident.setInstance(patientAcc);
                Toast.makeText(getContext(), patientAcc.toString(), Toast.LENGTH_LONG).show();
                return false;
            }

        });

        Button btnReportAnimal = (Button) getView().findViewById(R.id.btn_animal_opt);
        btnReportAnimal.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Accident animalAcc = WWTo.animalRescueRequest(getContext(), LocationUtils.getInstance(getContext()).getLat(), LocationUtils.getInstance(getContext()).getLng());
                Accident.setInstance(animalAcc);
                Toast.makeText(getContext(), animalAcc.toString(), Toast.LENGTH_LONG).show();
                return false;
            }

        });

        Button btnReportOther = (Button) getView().findViewById(R.id.btn_other_opt);
        btnReportOther.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Accident otherAcc = WWTo.otherRescueRequest(getContext(), LocationUtils.getInstance(getContext()).getLat(), LocationUtils.getInstance(getContext()).getLng());
                Accident.setInstance(otherAcc);
                Toast.makeText(getContext(), otherAcc.toString(), Toast.LENGTH_LONG).show();
                return false;
            }

        });

        final Button btnUserfalse = (Button) getView().findViewById(R.id.btn_userfalse);
        btnUserfalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingVerify.isNetworkConnected(getContext())) {
                    Log.v(">>>>", Accident.getInstance().toString());
                    if (WWTo.setUserFalseAccident(getContext(), Accident.getInstance())) {
                        Toast.makeText(getContext(), "Success", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), " : ( ", Toast.LENGTH_LONG).show();
                    }
                }



            }

        });


    }
    }



