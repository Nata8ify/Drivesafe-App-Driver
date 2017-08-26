package com.senior.g40.drivesafe;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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

    @BindView(R.id.txt_alert_msg_line1)
    TextView txtAlertMessageLine1;
    @BindView(R.id.btn_rescue_request)
    Button btnRescueRequest;
    @BindView(R.id.btn_rescue_dismiss)
    Button btnRescueDismiss;
    @BindView(R.id.txt_alert_msg_line2_timer)
    TextView txtAlertMsgLine2Timer;

    public static boolean isAlertActivityPrompted;
    @BindView(R.id.linrout_request_actions)
    LinearLayout linroutRequestActions;
    @BindView(R.id.btn_set_false)
    Button btnSetFalse;
    @BindView(R.id.linrout_request_cancel)
    LinearLayout linroutRequestCancel;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        ButterKnife.bind(this);
        isAlertActivityPrompted = true;
        txtAlertMessageLine1.setText(Accident.getInstance().toString());
        vibrator = ((Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE));
        vibrator.vibrate(20000l);
    }

    CountDownTimer counter;

    @Override
    protected void onStart() {
        super.onStart();
        counter = new CountDownTimer(30000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                txtAlertMessageLine1.setText("You have " + (millisUntilFinished / 1000) + " second to respond before the application auto send the accident location to the rescuer team." +
                        "If you want to call for help please tap 'SEND A RESCUE REQUEST' if you don't want any help please tap 'NO, THIS IS NOT ACCIDENT'");
                txtAlertMsgLine2Timer.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                requestRescue();
                txtAlertMessageLine1.setText(getResources().getString(R.string.crashsrvc_request_sent));
                Toast.makeText(AlertActivity.this, getResources().getString(R.string.crashsrvc_request_sent), Toast.LENGTH_LONG).show();
            }
        };
        counter.start();
    }

    @OnClick({R.id.btn_rescue_request, R.id.btn_rescue_dismiss, R.id.btn_set_false})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_rescue_request:
                counter.cancel();
                counter.onFinish();
                linroutRequestActions.setVisibility(View.GONE);
                linroutRequestCancel.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_rescue_dismiss:
                counter.cancel();
                vibrator.cancel();
                this.finish();
                break;
            case R.id.btn_set_false:
                if (WWTo.setUserFalseAccident(this, Accident.getInstance())) {
                    Toast.makeText(this, getResources().getString(R.string.crashsrvc_cancel_request), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Unsuccessful", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void requestRescue() {
        Accident.setInstance(WWTo.crashRescueRequest(AlertActivity.this, LocationUtils.lat, LocationUtils.lng, Math.round(CrashingSensorEngines.gs), LocationUtils.speed));
    }

}
