package com.senior.g40.drivesafe;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.senior.g40.drivesafe.engines.CrashingSensorEngines;
import com.senior.g40.drivesafe.models.Accident;
import com.senior.g40.drivesafe.utils.LocationUtils;
import com.senior.g40.drivesafe.weeworh.WWTo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AlertActivity extends AppCompatActivity {

    @BindView(R.id.txt_alert_msg)
    TextView txtAlertMessage;
    @BindView(R.id.btn_rescue_request)
    Button btnRescueRequest;
    @BindView(R.id.btn_rescue_dismiss)
    Button btnRescueDismiss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        ButterKnife.bind(this);
        txtAlertMessage.setText(Accident.getInstance().toString());
        ((Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(5000l);
    }

    CountDownTimer counter;

    @Override
    protected void onStart() {
        super.onStart();
        counter = new CountDownTimer(30000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                txtAlertMessage.setText("You have " + "00:" + (millisUntilFinished / 1000) + " second to respond before the application auto send the accident location to the rescuer team." +
                        "If you want to call for help please tap 'Yes' if you don't want any help please tap 'No'");
            }

            @Override
            public void onFinish() {
                requestRescue();
                txtAlertMessage.setText(Accident.getInstance().toString());
                Toast.makeText(AlertActivity.this, "Accident Location send", Toast.LENGTH_LONG).show();
            }
        };
        counter.start();
    }

    @OnClick({R.id.btn_rescue_request, R.id.btn_rescue_dismiss})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_rescue_request:
                counter.cancel();
                counter.onFinish();
                break;
            case R.id.btn_rescue_dismiss:
                counter.cancel();
                this.finish();
                break;
        }
    }

    private void requestRescue(){
        Accident.setInstance(WWTo.crashRescueRequest(AlertActivity.this, LocationUtils.lat, LocationUtils.lng, Math.round(CrashingSensorEngines.gs), LocationUtils.speed));
    }
}
