package com.example.burningbuilding;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class Floor8Activity extends AppCompatActivity {

    CountDownTimer gameTimer;
    long miliSecondsOfGame = 600000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor8);

        ImageButton greenBtn = findViewById(R.id.green_btn);

        Animation animation = AnimationUtils.loadAnimation(Floor8Activity.this, R.anim.flashing_elevator_btn);
        animation.setStartTime(3000);
        greenBtn.startAnimation(animation);

        gameTimer = new CountDownTimer(miliSecondsOfGame,1000) {

            public void onTick(long millisUntilFinished) {
                long minutes;
                int seconds;
                String limeLeft;
                minutes = millisUntilFinished / 60000;
                seconds = (int)(millisUntilFinished % 60000 / 1000);
                limeLeft = "Time left: 00:0" + minutes + ":" + seconds;

                miliSecondsOfGame = millisUntilFinished;
                setTitle(limeLeft);
            }

            public void onFinish() { // game over - timer has finished before finishing the floors
                Intent intent = new Intent(Floor8Activity.this,RecordsActivity.class);

                startActivity(intent);
            }
        }.start();

        greenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(Floor8Activity.this, SoundServiceVictory.class));
                stopService(new Intent(Floor8Activity.this, SoundServiceElevator.class));
                new CountDownTimer(2000, 1000) {

                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        Intent intent = new Intent(Floor8Activity.this,Floor7Activity.class);
                        intent.putExtra("timer", miliSecondsOfGame);
                        startActivity(intent);
                    }
                }.start();
            }
        });
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
                startService(new Intent(Floor8Activity.this, SoundServiceElevator.class));
            }
            return true;
            case R.id.soundOff: {
                stopService(new Intent(Floor8Activity.this, SoundServiceElevator.class));
            }
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}