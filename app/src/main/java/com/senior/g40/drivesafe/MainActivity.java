package com.senior.g40.drivesafe;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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

    private Handler mainHandler;
    private Runnable connectivityRunnable;


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


}
