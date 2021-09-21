package com.example.burningbuilding;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.os.Vibrator;
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
    boolean soundEnabled;
    long miliSecondsOfGame = 600000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor8);


        Intent intent = getIntent();
        soundEnabled = intent.getBooleanExtra("sound",true);

        if(soundEnabled)
            startService(new Intent(Floor8Activity.this, SoundServiceElevator.class));

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
                if(seconds <10)
                    limeLeft = "Time left: 00:0" + minutes + ":0" + seconds;
                else
                    limeLeft = "Time left: 00:0" + minutes + ":" + seconds;


                miliSecondsOfGame = millisUntilFinished;
                setTitle(limeLeft);
            }

            public void onFinish() { // game over - timer has finished before finishing the floors
                Intent intent = new Intent(Floor8Activity.this,GameOver.class);
                intent.putExtra("sound",soundEnabled);
                intent.putExtra("floor",8);
                stopService(new Intent(Floor8Activity.this, SoundServiceElevator.class));
                startActivity(intent);
            }
        }.start();

        greenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(soundEnabled) {
                    startService(new Intent(Floor8Activity.this, SoundServiceVictory.class));
                    stopService(new Intent(Floor8Activity.this, SoundServiceElevator.class));
                }
                else {
                    Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vib.vibrate(500);
                }
                new CountDownTimer(1000, 1000) {

                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        Intent intent = new Intent(Floor8Activity.this,Floor7Activity.class);
                        intent.putExtra("timer", miliSecondsOfGame);
                        intent.putExtra("sound",soundEnabled);
                        startActivity(intent);
                        finish();
                    }
                }.start();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sound_menu, menu);
        if(soundEnabled)
        {
            menu.findItem(R.id.soundMenu).setTitle("Volume off");
            menu.findItem(R.id.soundMenu).setIcon(R.drawable.ic_baseline_volume_off_24);
        }
        else
        {
            menu.findItem(R.id.soundMenu).setTitle("Volume on");
            menu.findItem(R.id.soundMenu).setIcon(R.drawable.ic_baseline_volume_up_24);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.soundMenu: {
                if(soundEnabled) {
                    soundEnabled = false;
                    stopService(new Intent(Floor8Activity.this, SoundServiceElevator.class));
                    item.setTitle("Volume on");
                    item.setIcon(R.drawable.ic_baseline_volume_up_24);
                }
                else
                {
                    soundEnabled = true;
                    startService(new Intent(Floor8Activity.this, SoundServiceElevator.class));
                    item.setTitle("Volume off");
                    item.setIcon(R.drawable.ic_baseline_volume_off_24);
                }
            }
            return true;
            case R.id.info_menu: {
                stopService(new Intent(Floor8Activity.this, SoundServiceElevator.class));
            }
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}