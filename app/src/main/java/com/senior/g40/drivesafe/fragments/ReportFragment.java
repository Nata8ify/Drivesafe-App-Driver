package com.senior.g40.drivesafe.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;
import com.senior.g40.drivesafe.R;
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

        Button btnReportCrash = (Button) getView().findViewById(R.id.btn_crash_opt);
        btnReportCrash.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                    Accident crashAcc = WWTo.crashRescueRequest(getContext(), LocationUtils.getInstance(getContext()).getLat(), LocationUtils.getInstance(getContext()).getLng());
                    if(crashAcc == null){alertDuplicatedReport(); return false;}
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
                if(fireAcc == null){alertDuplicatedReport(); return false;}
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
                if(brawlAcc == null){alertDuplicatedReport(); return false;}
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
                if(patientAcc == null){alertDuplicatedReport(); return false;}
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
                if(animalAcc == null){alertDuplicatedReport(); return false;}
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
                if(otherAcc == null){alertDuplicatedReport(); return false;}
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

    private AlertDialog duplicatedReportDialog;
    private void alertDuplicatedReport(){
        if(duplicatedReportDialog == null){
            duplicatedReportDialog = new AlertDialog.Builder(getContext())
                    .setTitle(getString(R.string.main_report_we_copy))
                    .setMessage(getString(R.string.main_report_alert_reported))
                    .setPositiveButton(getString(R.string.close), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .create();
        }
        duplicatedReportDialog.show();
    }
}



