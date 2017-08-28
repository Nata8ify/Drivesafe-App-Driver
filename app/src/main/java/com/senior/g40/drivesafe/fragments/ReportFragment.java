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

import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;
import com.senior.g40.drivesafe.AlertActivity;
import com.senior.g40.drivesafe.R;
import com.senior.g40.drivesafe.engines.CrashingSensorEngines;
import com.senior.g40.drivesafe.models.Accident;
import com.senior.g40.drivesafe.models.extras.AccidentBrief;
import com.senior.g40.drivesafe.utils.LocationUtils;
import com.senior.g40.drivesafe.utils.SettingVerify;
import com.senior.g40.drivesafe.weeworh.WWTo;

import io.realm.Realm;


public class ReportFragment extends Fragment {

    private Realm realm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Realm.init(getContext());
        realm = Realm.getDefaultInstance();
        return inflater.inflate(R.layout.fragment_report, container, false);

    }

    @Override
    public void onStart() {
        super.onStart();

        SwipeButton btnSwipe = (SwipeButton) getView().findViewById(R.id.btn_swipe);
        btnSwipe.setOnStateChangeListener (new OnStateChangeListener() {
            @Override
            public void onStateChange(boolean active) {
                Accident crashAcc = WWTo.crashRescueRequest(getContext(), LocationUtils.getInstance(getContext()).getLat(), LocationUtils.getInstance(getContext()).getLng());
                Accident.setInstance(crashAcc);
                save();
            }
        });


        Button btnReportCrash = (Button) getView().findViewById(R.id.btn_crash_opt);
        btnReportCrash.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Accident crashAcc = WWTo.crashRescueRequest(getContext(), LocationUtils.getInstance(getContext()).getLat(), LocationUtils.getInstance(getContext()).getLng());
                Accident.setInstance(crashAcc);
                save();
                return false;
            }


        });

        Button btnReportFire = (Button) getView().findViewById(R.id.btn_fire_opt);
        btnReportFire.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Accident fireAcc = WWTo.fireRescueRequest(getContext(), LocationUtils.getInstance(getContext()).getLat(), LocationUtils.getInstance(getContext()).getLng());
                Accident.setInstance(fireAcc);
                save();
                return false;
            }

        });

        Button btnReportBrawl = (Button) getView().findViewById(R.id.btn_brawl_opt);
        btnReportBrawl.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Accident brawlAcc = WWTo.brawlRescueRequest(getContext(), LocationUtils.getInstance(getContext()).getLat(), LocationUtils.getInstance(getContext()).getLng());
                Accident.setInstance(brawlAcc);
                save();
                return false;
            }

        });

        Button btnReportPatient = (Button) getView().findViewById(R.id.btn_patient_opt);
        btnReportPatient.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Accident patientAcc = WWTo.patientRescueRequest(getContext(), LocationUtils.getInstance(getContext()).getLat(), LocationUtils.getInstance(getContext()).getLng());
                Accident.setInstance(patientAcc);
                save();
                return false;
            }

        });

        Button btnReportAnimal = (Button) getView().findViewById(R.id.btn_animal_opt);
        btnReportAnimal.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Accident animalAcc = WWTo.animalRescueRequest(getContext(), LocationUtils.getInstance(getContext()).getLat(), LocationUtils.getInstance(getContext()).getLng());
                Accident.setInstance(animalAcc);
                save();
                return false;
            }

        });

        Button btnReportOther = (Button) getView().findViewById(R.id.btn_other_opt);
        btnReportOther.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Accident otherAcc = WWTo.otherRescueRequest(getContext(), LocationUtils.getInstance(getContext()).getLat(), LocationUtils.getInstance(getContext()).getLng());
                Accident.setInstance(otherAcc);
                save();
                return false;
            }

        });

        final Button btnUserfalse = (Button) getView().findViewById(R.id.btn_userfalse);
        btnUserfalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingVerify.isNetworkConnected(getContext())) {
                    dismissLatestRescueRequest();
                }


            }

        });


    }

    private void save() {
        if(!realm.isInTransaction()){realm.beginTransaction();}
        realm.insert(new AccidentBrief(Accident.getInstance()));
        realm.commitTransaction();
    }

    private AccidentBrief latestAccidentBrief;
    private void dismissLatestRescueRequest(){ //Or Set 'False'
        if(!realm.isInTransaction()){realm.beginTransaction();}
        latestAccidentBrief = realm.where(AccidentBrief.class).findFirst();
        if(latestAccidentBrief == null) {
            Toast.makeText(getContext(), "Unsuccessful, You have no Incident which is Reported.", Toast.LENGTH_LONG).show();
            return;
        }
        if (WWTo.setUserFalseAccidentId(getContext(), latestAccidentBrief.getAccidentId(), latestAccidentBrief.getUserId())) {
            Toast.makeText(getContext(), getResources().getString(R.string.crashsrvc_cancel_request), Toast.LENGTH_LONG).show();
            realm.delete(AccidentBrief.class);
        }
        realm.commitTransaction();
    }
}



