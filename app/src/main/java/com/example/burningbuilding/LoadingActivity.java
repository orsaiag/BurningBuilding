package com.example.burningbuilding;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LoadingActivity extends AppCompatActivity {
    LinearLayout linear_Layout;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startService(new Intent(LoadingActivity.this, SoundFire.class));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        TextView welcome = findViewById(R.id.building_pic);
        welcome.setVisibility(View.VISIBLE);

        TextView continue_Tv = findViewById(R.id.continue_textView);
        linear_Layout = findViewById(R.id.linearLayout);
        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                continue_Tv.setVisibility(View.VISIBLE);
                linear_Layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(LoadingActivity.this, SettingsActivity.class);//change!!
                        stopService(new Intent(LoadingActivity.this, SoundFire.class));
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }.start();
    }
}