package com.senior.g40.drivesafe.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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
    private ReportAsynctTask reportAsynctTask;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Realm.init(getContext());
        realm = Realm.getDefaultInstance();
        reportAsynctTask = new ReportAsynctTask(getContext());
        return inflater.inflate(R.layout.fragment_report, container, false);

    }

    @Override
    public void onStart() {
        super.onStart();

        ImageButton btnReportCrash = (ImageButton) getView().findViewById(R.id.btn_crash_opt);
        btnReportCrash.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Accident crashAcc = WWTo.crashRescueRequest(getContext(), LocationUtils.getInstance(getContext()).getLat(), LocationUtils.getInstance(getContext()).getLng());
                if (crashAcc == null) {
                    alertDuplicatedReport();
                    return false;
                }
                Accident.setInstance(crashAcc);
                save();
                return false;
            }

        });

        ImageButton btnReportFire = (ImageButton) getView().findViewById(R.id.btn_fire_opt);
        btnReportFire.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Accident fireAcc = WWTo.fireRescueRequest(getContext(), LocationUtils.getInstance(getContext()).getLat(), LocationUtils.getInstance(getContext()).getLng());
                if (fireAcc == null) {
                    alertDuplicatedReport();
                    return false;
                }
                Accident.setInstance(fireAcc);
                save();
                return false;
            }

        });

        ImageButton btnReportBrawl = (ImageButton) getView().findViewById(R.id.btn_brawl_opt);
        btnReportBrawl.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Accident brawlAcc = WWTo.brawlRescueRequest(getContext(), LocationUtils.getInstance(getContext()).getLat(), LocationUtils.getInstance(getContext()).getLng());
                if (brawlAcc == null) {
                    alertDuplicatedReport();
                    return false;
                }
                Accident.setInstance(brawlAcc);
                save();
                return false;
            }

        });

        ImageButton btnReportPatient = (ImageButton) getView().findViewById(R.id.btn_patient_opt);
        btnReportPatient.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Accident patientAcc = WWTo.patientRescueRequest(getContext(), LocationUtils.getInstance(getContext()).getLat(), LocationUtils.getInstance(getContext()).getLng());
                if (patientAcc == null) {
                    alertDuplicatedReport();
                    return false;
                }
                Accident.setInstance(patientAcc);
                save();
                return false;
            }

        });

        ImageButton btnReportAnimal = (ImageButton) getView().findViewById(R.id.btn_animal_opt);
        btnReportAnimal.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Accident animalAcc = WWTo.animalRescueRequest(getContext(), LocationUtils.getInstance(getContext()).getLat(), LocationUtils.getInstance(getContext()).getLng());
                if (animalAcc == null) {
                    alertDuplicatedReport();
                    return false;
                }
                Accident.setInstance(animalAcc);
                save();
                return false;
            }

        });

        ImageButton btnReportOther = (ImageButton) getView().findViewById(R.id.btn_other_opt);
        btnReportOther.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Accident otherAcc = WWTo.otherRescueRequest(getContext(), LocationUtils.getInstance(getContext()).getLat(), LocationUtils.getInstance(getContext()).getLng());
                if (otherAcc == null) {
                    alertDuplicatedReport();
                    return false;
                }
                Accident.setInstance(otherAcc);
                save();
                //reportAsynctTask.execute(LocationUtils.getInstance(getContext()).getLat(), LocationUtils.getInstance(getContext()).getLng());
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
        if (!realm.isInTransaction()) {
            realm.beginTransaction();
        }
        realm.insert(new AccidentBrief(Accident.getInstance()));
        realm.commitTransaction();
    }

    private AccidentBrief latestAccidentBrief;

    private void dismissLatestRescueRequest() { //Or Set 'False'
        if (!realm.isInTransaction()) {
            realm.beginTransaction();
        }
        latestAccidentBrief = realm.where(AccidentBrief.class).findFirst();
        if (latestAccidentBrief == null) {
            Toast.makeText(getContext(), getResources().getString(R.string.crashsrvc_no_incident_reported), Toast.LENGTH_LONG).show();
            return;
        }
        if (WWTo.setUserFalseAccidentId(getContext(), latestAccidentBrief.getAccidentId(), latestAccidentBrief.getUserId())) {
            Toast.makeText(getContext(), getResources().getString(R.string.crashsrvc_cancel_request), Toast.LENGTH_LONG).show();
            realm.delete(AccidentBrief.class);
        }
        realm.commitTransaction();
    }

    private AlertDialog duplicatedReportDialog;

    private void alertDuplicatedReport() {
        if (duplicatedReportDialog == null) {
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


    class ReportAsynctTask extends AsyncTask {

        private Context context;
        private ProgressDialog progressDialog;
        private AlertDialog alertDialog;
        private Object[] params;
        private Accident accident;

        public ReportAsynctTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getString(R.string.sending_rescue_req));
            progressDialog.show();

            alertDialog = new AlertDialog.Builder(context)
                    .setMessage(getString(R.string.warn_report_failed))
                    .setNegativeButton(getString(R.string.close), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.cancel();
                        }
                    })/*
                    .setPositiveButton(getString(R.string.try_again), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //TODO
                        }
                    })*/
                    .create();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                this.params = params;
                this.accident = WWTo.rescueRequest(context, (double) params[0], (double) params[1], (byte) params[2]);
            } catch (Exception exp) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        alertDialog.show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            progressDialog.cancel();
            if (this.accident == null) {
                alertDuplicatedReport();
            } else {
                Accident.setInstance(this.accident);
                save();
            }
        }
    }

}



