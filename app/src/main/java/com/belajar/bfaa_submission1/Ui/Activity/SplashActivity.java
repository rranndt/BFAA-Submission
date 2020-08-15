package com.belajar.bfaa_submission1.Ui.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.belajar.bfaa_submission1.R;

public class SplashActivity extends AppCompatActivity {

    private String appVersion;
    private TextView tvAppVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initViews();
        setSplash();
    }

    private void initViews() {
        tvAppVersion = findViewById(R.id.app_version);
    }

    @SuppressLint("SetTextI18n")
    private void setSplash() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            appVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        tvAppVersion.setText(getResources().getString(R.string.app_version) + " " + appVersion);

        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        },1000);
    }
}