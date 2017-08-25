package com.senior.g40.drivesafe;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.senior.g40.drivesafe.models.Accident;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlertActivity extends AppCompatActivity {

    @BindView(R.id.txt_instaance_acc)
    TextView txtInstaanceAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        ButterKnife.bind(this);
        txtInstaanceAcc.setText(Accident.getInstance().toString());
        ((Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(5000l);
    }
}
