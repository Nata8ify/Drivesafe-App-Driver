package com.senior.g40.drivesafe;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.senior.g40.drivesafe.engines.CrashingSensorEngines;
import com.senior.g40.drivesafe.fragments.ActivateFragment;
import com.senior.g40.drivesafe.fragments.ReportFragment;
import com.senior.g40.drivesafe.models.Accident;
import com.senior.g40.drivesafe.services.CrashDetectionService;
import com.senior.g40.drivesafe.utils.LocationUtils;
import com.senior.g40.drivesafe.utils.SettingVerify;
import com.senior.g40.drivesafe.weeworh.WWTo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private static LocationUtils accLocationUtils;
    private ViewPager mViewPager;

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

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        ButterKnife.bind(this);
        this.context = this;
        validatePermission();
        crashingSensorEngines = CrashingSensorEngines.getInstance(this);
        crashingSensorEngines.setTxtviewOut(txtGs);
        startService(new Intent(this, MainActivity.class));
        accLocationUtils = LocationUtils.getInstance(context);
        accLocationUtils.startLocationUpdate();
    }

    @Override
    protected void onDestroy() {
        accLocationUtils.stopLocationUpdate();
        super.onDestroy();
    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }


        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_report, container, false);
            return rootView;
        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0 : return new ReportFragment();
                case 1 : return new ActivateFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
            }
            return null;
        }
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

    /*private int activateState;

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
    }*/


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
            Log.v(">>>>", Accident.getInstance().toString());
            if(WWTo.setUserFalseAccident(this, Accident.getInstance())){
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
    @BindView(R.id.btn_brawl_opt)
    Button btnBrawlOpt;

    @OnClick({R.id.btn_crash_opt, R.id.btn_fire_opt, R.id.btn_patient_opt,R.id.btn_brawl_opt,R.id.btn_animal_opt, R.id.btn_other_opt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_crash_opt:
                Accident crashAcc = WWTo.crashRescueRequest(context, LocationUtils.getInstance(context).getLat(), LocationUtils.getInstance(context).getLng());
                Accident.setInstance(crashAcc);
                toast(crashAcc.toString());
                break;
            case R.id.btn_fire_opt:
                Accident fireAcc = WWTo.fireRescueRequest(context, LocationUtils.getInstance(context).getLat(), LocationUtils.getInstance(context).getLng());
                Accident.setInstance(fireAcc);
                toast(fireAcc.toString());
                break;
            case R.id.btn_brawl_opt:
                Accident brawlAcc = WWTo.brawlRescueRequest(context, LocationUtils.getInstance(context).getLat(), LocationUtils.getInstance(context).getLng());
                Accident.setInstance(brawlAcc);
                toast(brawlAcc.toString());
                break;
            case R.id.btn_patient_opt:
                Accident patientAcc = WWTo.patientRescueRequest(context, LocationUtils.getInstance(context).getLat(), LocationUtils.getInstance(context).getLng());
                Accident.setInstance(patientAcc);
                toast(patientAcc.toString());
                break;
            case R.id.btn_animal_opt:
                Accident animalAcc = WWTo.animalRescueRequest(context, LocationUtils.getInstance(context).getLat(), LocationUtils.getInstance(context).getLng());
                Accident.setInstance(animalAcc);
                toast(animalAcc.toString());
                break;
            case R.id.btn_other_opt:
                Accident otherAcc = WWTo.otherRescueRequest(context, LocationUtils.getInstance(context).getLat(), LocationUtils.getInstance(context).getLng());
                Accident.setInstance(otherAcc);
                toast(otherAcc.toString());
                break;
        }
    }

}
