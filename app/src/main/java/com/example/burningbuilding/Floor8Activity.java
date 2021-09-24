package com.example.burningbuilding;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Locale;

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
                if(Locale.getDefault().getDisplayLanguage()=="en") {
                    if (seconds < 10)
                        limeLeft = String.format("Time left: 00:0%d:0%d",minutes,seconds);
                    else
                        limeLeft = String.format("Time left: 00:0%d:%d",minutes,seconds);
                }
                else
                {
                    if (seconds < 10)
                        limeLeft = String.format("הזמן שנותר: 00:0%d:0%d",minutes,seconds);
                    else
                        limeLeft = String.format("הזמן שנותר: 00:0%d:%d",minutes,seconds);
                }
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
        inflater.inflate(R.menu.menu, menu);
        if(soundEnabled)
        {
            menu.findItem(R.id.soundMenu).setTitle(R.string.volume_off);
            menu.findItem(R.id.soundMenu).setIcon(R.drawable.ic_baseline_volume_off_24);
        }
        else
        {
            menu.findItem(R.id.soundMenu).setTitle(R.string.volume_on);
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
                    item.setTitle(R.string.volume_on);
                    item.setIcon(R.drawable.ic_baseline_volume_up_24);
                }
                else
                {
                    soundEnabled = true;
                    startService(new Intent(Floor8Activity.this, SoundServiceElevator.class));
                    item.setTitle(R.string.volume_off);
                    item.setIcon(R.drawable.ic_baseline_volume_off_24);
                }
            }
            return true;
            case R.id.info_menu: {
                View dialogView = getLayoutInflater().inflate(R.layout.information_of_floors,null,false);
                AlertDialog.Builder builder = new AlertDialog.Builder(Floor8Activity.this);
                ((TextView)dialogView.findViewById(R.id.floor_info)).setText(R.string.info_floor_8);
                builder.setView(dialogView).setPositiveButton(R.string.info_back_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
            }
            return true;
            case R.id.gameRestart:{
                Intent intentRestart = new Intent(Floor8Activity.this,SettingsActivity.class);
                intentRestart.putExtra("sound",soundEnabled);
                startActivity(intentRestart);
                finish();
            }
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}