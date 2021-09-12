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
                        Intent intent = new Intent(LoadingActivity.this, Floor1Activity.class);
                        stopService(new Intent(LoadingActivity.this, SoundFire.class));
                        startActivity(intent);
                    }
                });
            }
        }.start();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sound_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.soundOn: {
                startService(new Intent(LoadingActivity.this, SoundFire.class));
            }
            return true;
            case R.id.soundOff: {
                stopService(new Intent(LoadingActivity.this, SoundFire.class));
            }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}