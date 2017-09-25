package com.senior.g40.drivesafe;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.senior.g40.drivesafe.engines.CrashingSensorEngines;
import com.senior.g40.drivesafe.models.Accident;
import com.senior.g40.drivesafe.models.extras.AccidentBrief;
import com.senior.g40.drivesafe.utils.LocationUtils;
import com.senior.g40.drivesafe.weeworh.WWTo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

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
    @BindView(R.id.img_ww_ico)
    ImageView imgWwIco;
    @BindView(R.id.linrout_request_sent_message)
    LinearLayout linroutRequestSentMessage;
    private Vibrator vibrator;

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        ButterKnife.bind(this);
        Realm.init(this);
        realm = Realm.getDefaultInstance();

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
                txtAlertMessageLine1.setText(getResources().getString(R.string.crashsrvc_alert_message1)+" "+(millisUntilFinished / 1000) +" "+getResources().getString(R.string.crashsrvc_alert_message2) + getResources().getString(R.string.crashsrvc_alert_message3));
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
                this.finish();
                break;
            case R.id.btn_rescue_dismiss:
                counter.cancel();
                vibrator.cancel();
                this.finish();
                break;
            case R.id.btn_set_false:
                dismissLatestRescueRequest();
                break;
        }
    }

    private void requestRescue() {
        Accident.setInstance(WWTo.crashRescueRequest(AlertActivity.this, LocationUtils.lat, LocationUtils.lng, Math.round(CrashingSensorEngines.gs), LocationUtils.speed));
        if (!realm.isInTransaction()) {
            realm.beginTransaction();
        }
        realm.insert(new AccidentBrief(Accident.getInstance()));
        realm.commitTransaction();
    }

    private AccidentBrief latestAccidentBrief;

    private void dismissLatestRescueRequest() { //Or Set 'False'
        realm.beginTransaction();
        latestAccidentBrief = realm.where(AccidentBrief.class).findFirst();
        if (WWTo.setUserFalseAccidentId(this, latestAccidentBrief.getAccidentId(), latestAccidentBrief.getUserId())) {
            Toast.makeText(this, getResources().getString(R.string.crashsrvc_cancel_request_success), Toast.LENGTH_LONG).show();
            realm.delete(AccidentBrief.class);
            vibrator.cancel();
            finish();
        } else {
            Toast.makeText(this, getResources().getString(R.string.un_success), Toast.LENGTH_LONG).show();
        }
        realm.commitTransaction();
    }

}
