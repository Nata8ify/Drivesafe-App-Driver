package com.senior.g40.drivesafe;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.senior.g40.drivesafe.engines.UserEngines;
import com.senior.g40.drivesafe.models.Profile;
import com.senior.g40.drivesafe.utils.LocationUtils;
import com.senior.g40.drivesafe.utils.SettingVerify;
import com.senior.g40.drivesafe.weeworh.WWProp;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.edtxt_loginUsername)
    EditText edtxtLoginUsername;
    @BindView(R.id.edtxt_loginPassword)
    EditText edtxtLoginPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.activity_login)
    LinearLayout activityLogin;
    @BindView(R.id.txt_headMessage)
    TextView txtHeadMessage;
    @BindView(R.id.chkbox_remember)
    CheckBox chkboxRemember;

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        LocationUtils.getInstance(this);

    }

    ProgressDialog progressDialog;

    @Override
    protected void onStart() {
        super.onStart();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        Realm.init(this);
        realm = Realm.getDefaultInstance();
        isRememberSet();
    }

    @OnClick({R.id.btn_login, R.id.btn_register})
    public void onClick(View view) {
        if (SettingVerify.isNetworkConnected(this)) {
            switch (view.getId()) {
                case R.id.btn_login:
                    new LoginAsyncTask(LoginActivity.this).execute();
                    break;
                case R.id.btn_register:
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(WWProp.WEEWORH_HOST)));
                    break;
            }
        } else {
            if (view.getId() == R.id.btn_login || view.getId() == R.id.btn_register) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setMessage("No internet connection. Please turn on the internet wifi or 3G/4G to use this application.");
                builder.setTitle("No internet connection");
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        }
        return;
    }

    private void isRememberSet() {
        progressDialog.show();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (realm.where(Profile.class).findFirst() != null) {
                    Profile.createInsatance(realm.where(Profile.class).findFirst());
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    LoginActivity.this.finish();
                }
            }
        });
        progressDialog.cancel();
    }

    class LoginAsyncTask extends AsyncTask {

        private Context context;
        private ProgressDialog progressDialog;
        private AlertDialog alertDialog;
        private boolean isSuccess;
        private String username;
        private String password;

        public LoginAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(getString(R.string.loading));
            progressDialog.setCancelable(false);
            progressDialog.show();
            alertDialog = new AlertDialog.Builder(context)
                    .setTitle(getString(R.string.warn_login_failed))
                    .setMessage(getString(R.string.warn_login_failed_causes))
                    .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.cancel();
                        }
                    })
                    .setCancelable(false)
                    .create();

            username = edtxtLoginUsername.getText().toString();
            password = edtxtLoginPassword.getText().toString();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            isSuccess = UserEngines.getInstance(this.context)
                    .login(username, password);

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (isSuccess) {
                if (chkboxRemember.isChecked()) {
                    realm.beginTransaction();
                    realm.insert(Profile.getInsatance());
                    realm.commitTransaction();
                }
                startActivity(new Intent(this.context, MainActivity.class));
                finish();
            } else {
                alertDialog.show();
            }
            progressDialog.cancel();
        }

    }
}
