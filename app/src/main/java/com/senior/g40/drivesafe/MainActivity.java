package com.senior.g40.drivesafe;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.senior.g40.drivesafe.engines.CrashingSensorEngines;
import com.senior.g40.drivesafe.engines.GPSVerifierAsyncTask;
import com.senior.g40.drivesafe.fragments.ActivateFragment;
import com.senior.g40.drivesafe.fragments.ReportFragment;
import com.senior.g40.drivesafe.models.Accident;
import com.senior.g40.drivesafe.models.extras.AccidentBrief;
import com.senior.g40.drivesafe.utils.LocationUtils;
import com.senior.g40.drivesafe.utils.SettingVerify;
import com.senior.g40.drivesafe.weeworh.WWTo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

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

    private Handler mainHandler;
    private Runnable connectivityRunnable;
    private Runnable currentRescReqRunnable;

    private CrashingSensorEngines crashingSensorEngines;

    /*Activity Attributes*/
    private Context context;

    private Realm realm;
    private AccidentBrief latestAccidentBrief;

    private AlertDialog connectivityDialog;
    private AlertDialog currentRescReqDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        ButterKnife.bind(this);
        Realm.init(this);
        realm = Realm.getDefaultInstance();
        latestAccidentBrief = realm.where(AccidentBrief.class).findFirst();

        this.context = this;
        validatePermission();
        crashingSensorEngines = CrashingSensorEngines.getInstance(this);
        crashingSensorEngines.setTxtviewOut(txtGs);
        accLocationUtils = LocationUtils.getInstance(context);
        accLocationUtils.startLocationUpdate();


        mainHandler = new Handler();
        new GPSVerifierAsyncTask(this).execute();
        connectivityDialog = new AlertDialog.Builder(MainActivity.this)
                .setMessage(getResources().getString(R.string.warn_no_network_and_please))
                .setPositiveButton(getResources().getString(R.string.goto_setting), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.setClassName("com.android.phone", "com.android.phone.NetworkSetting");
                        startActivity(intent);
                    }
                })
                .setCancelable(false)
                .create();
        connectivityRunnable = new Runnable() {
            @Override
            public void run() {
                if (!SettingVerify.isNetworkConnected(MainActivity.this)) {
                    connectivityDialog.show();
                } else {
                    connectivityDialog.dismiss();
                }
                mainHandler.postDelayed(this, 1000);
            }
        };

        currentRescReqDialog = new AlertDialog.Builder(this)
                .setView(R.layout.view_dismiss_alert)
                .setCancelable(false)
                .create();
        currentRescReqRunnable = new Runnable() {
            @Override
            public void run() {
                latestAccidentBrief = realm.where(AccidentBrief.class).findFirst();
                if (latestAccidentBrief != null) {
                    if (SettingVerify.isNetworkConnected(MainActivity.this)) {updateReportedIncidentInfo();clearReportedincidentIfClosed();}
                    if(!isFinishing()){
                    currentRescReqDialog.show();}
                } else {
                    currentRescReqDialog.cancel();
                }
                mainHandler.postDelayed(this, 1000);
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mainHandler.post(connectivityRunnable);
        mainHandler.post(currentRescReqRunnable);
    }

    @Override
    protected void onResume() {
        LocationUtils.getInstance(this).startLocationUpdate();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        accLocationUtils.stopLocationUpdate();
        super.onDestroy();
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ReportFragment();
                case 1:
                    return new ActivateFragment();
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
                    return getString(R.string.page_title_report_incident);
                case 1:
                    return getString(R.string.page_title_drivemode);
            }
            return null;
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
            toast(getString(R.string.warn_permission));
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
            if (WWTo.setUserFalseAccident(this, Accident.getInstance())) {
                toast("Success!");
            } else {
                toast(":(");
            }
        }
    }

    protected final int CANCEL_TAP_TIMES = 10;
    protected int currentCancelTapTimes = 0;

    public void setFalseOrCancel(View view) {
        if (currentCancelTapTimes < CANCEL_TAP_TIMES) {
            ++currentCancelTapTimes;
        } else {
            dismissLatestRescueRequest();
            currentCancelTapTimes = 0;
            currentRescReqDialog.cancel();
        }
        ((Button) view).setText(getResources().getString(R.string.crashsrvc_cancel_request).concat(" (").concat(String.valueOf(CANCEL_TAP_TIMES - currentCancelTapTimes)).concat(")"));
    }


    private void dismissLatestRescueRequest(){ //Or Set 'False'
        if(!realm.isInTransaction()){realm.beginTransaction();}
        latestAccidentBrief = realm.where(AccidentBrief.class).findFirst();
        if(latestAccidentBrief == null) {
            Toast.makeText(this, getResources().getString(R.string.crashsrvc_no_incident_reported), Toast.LENGTH_LONG).show();
            return;
        }
        if (WWTo.setUserFalseAccidentId(this, latestAccidentBrief.getAccidentId(), latestAccidentBrief.getUserId())) {
            Toast.makeText(this, getResources().getString(R.string.crashsrvc_cancel_request), Toast.LENGTH_LONG).show();
            latestAccidentBrief.deleteFromRealm();
        }
        realm.commitTransaction();
    }

    public void updateReportedIncidentInfo(){
        if(latestAccidentBrief == null){return;}
        Accident.setInstance(WWTo.updateCurrentReportedIncident(this, latestAccidentBrief.getAccidentId()));
    }

    private void clearReportedincidentIfClosed(){
        Log.d("CLOESED?", Accident.getInstance().toString());
        Log.d("latestAccidentBrief?", latestAccidentBrief.toString());
        if(Accident.getInstance().getAccCode() == Accident.ACC_CODE_C){
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Log.d("we going", "del");
                    realm.where(AccidentBrief.class).findAll().deleteAllFromRealm();
                }
            });
        }
    }

}
