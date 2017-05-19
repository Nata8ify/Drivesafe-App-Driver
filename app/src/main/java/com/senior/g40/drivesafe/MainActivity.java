package com.senior.g40.drivesafe;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.senior.g40.drivesafe.engines.CrashingSensorEngines;
import com.senior.g40.drivesafe.models.Accident;
import com.senior.g40.drivesafe.services.CrashDetectionService;
import com.senior.g40.drivesafe.utils.LocationUtils;
import com.senior.g40.drivesafe.utils.SettingVerify;
import com.senior.g40.drivesafe.weeworh.WWTo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.txt_gs)
    TextView txtGs;
    @BindView(R.id.btn_activeDrivesafe)
    Button btnActiveDrivesafe;
    @BindView(R.id.btn_activeDrivesafeService)
    Button btnActiveDrivesafeService;
    @BindView(R.id.activity_main)
    LinearLayout activityMain;


    private CrashingSensorEngines crashingSensorEngines;

    /*Activity Attributes*/
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        this.context = this;
        validatePermission();
        crashingSensorEngines = CrashingSensorEngines.getInstance(this);
        crashingSensorEngines.setTxtviewOut(txtGs);
    }


    private int activateServiceState;

    @Override
    protected void onResume() {
        super.onResume();
        txtGs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(LocationUtils.getInstance(MainActivity.this).getMapUri())));
            }
        });
        btnActiveDrivesafeService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (1 - activateServiceState == 1) {
                    activateServiceState = 1;
                    startService(new Intent(MainActivity.this, CrashDetectionService.class));
                    btnActiveDrivesafeService.setText(R.string.main_drvpauseserv);
                } else {
                    btnActiveDrivesafeService.setText(R.string.main_drvactiveserv);
                    activateServiceState = 0;
                    stopService(new Intent(MainActivity.this, CrashDetectionService.class));
                }
            }
        });
    }

    private int activateState;

    @OnClick(R.id.btn_activeDrivesafe)
    public void onClick() {
        activateState = 1 - activateState;
        if (activateState == 1) {
            btnActiveDrivesafe.setText(getResources().getString(R.string.main_drvdeactive));
            crashingSensorEngines.start();
        } else {
            btnActiveDrivesafe.setText(getResources().getString(R.string.main_drvactive));
            crashingSensorEngines.stop();
            txtGs.setText(" G's : CRASHING SENSOR STOP");
        }
    }


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
    }

    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.btn_userfalse)
    public void onViewClicked() {
        if (SettingVerify.isNetworkConnected(this)) {
            Log.v(">>>>", Accident.getInsatance().toString());
            if(WWTo.setUserFalseAccident(this, Accident.getInsatance())){
                toast("Success!");
            } else {
                toast(":(");
            }
        }
    }


    @BindView(R.id.btn_userfalse)
    Button btnUserfalse;
    @BindView(R.id.btn_crash_opt)
    Button btnCrashOpt;
    @BindView(R.id.btn_fire_opt)
    Button btnFireOpt;
    @BindView(R.id.btn_patient_opt)
    Button btnPatientOpt;
    @BindView(R.id.btn_animal_opt)
    Button btnAnimalOpt;
    @BindView(R.id.btn_other_opt)
    Button btnOtherOpt;
    @OnClick({R.id.btn_crash_opt, R.id.btn_fire_opt, R.id.btn_patient_opt, R.id.btn_animal_opt, R.id.btn_other_opt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_crash_opt:
                Accident crashAcc = WWTo.crashRescueRequest(context, LocationUtils.getInstance(context).getLat(), LocationUtils.getInstance(context).getLng());
                Accident.setInstance(crashAcc);
                toast(crashAcc.toString());
                break;
            case R.id.btn_fire_opt:
                toast(""+R.id.btn_fire_opt);
                break;
            case R.id.btn_patient_opt:
                toast(""+R.id.btn_patient_opt);
                break;
            case R.id.btn_animal_opt:
                toast(""+R.id.btn_animal_opt);
                break;
            case R.id.btn_other_opt:
                toast(""+R.id.btn_other_opt);
                break;
        }
    }
}
