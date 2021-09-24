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
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class Floor7Activity extends AppCompatActivity {

    CountDownTimer gameTimer;
    long milisecondsOfGame;
    boolean soundEnabled;
    Vibrator vib;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor7);

        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        Intent intent = getIntent();
        soundEnabled = intent.getBooleanExtra("sound",true);
        if(soundEnabled)
            startService(new Intent(Floor7Activity.this, SoundServiceElevator.class));

        RadioButton radio1=findViewById(R.id.radio1);
        RadioButton radio2=findViewById(R.id.radio2);
        RadioButton radio3=findViewById(R.id.radio3);
        RadioButton radio4=findViewById(R.id.radio4);
        RadioButton radio5=findViewById(R.id.radio5);
        RadioButton radio6=findViewById(R.id.radio6);

        milisecondsOfGame = intent.getLongExtra("timer",600000);

        gameTimer = new CountDownTimer(milisecondsOfGame,1000) {

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

                milisecondsOfGame = millisUntilFinished;
                setTitle(limeLeft);
            }

            public void onFinish() { // game over - timer has finished before finishing the floors
                Intent intent = new Intent(Floor7Activity.this,GameOver.class);
                intent.putExtra("sound",soundEnabled);
                intent.putExtra("floor",7);
                stopService(new Intent(Floor7Activity.this, SoundServiceElevator.class));
                startActivity(intent);
            }
        }.start();

        Button done_btn= findViewById(R.id.done1_btn);
        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radio2.isChecked() && radio4.isChecked() && radio5.isChecked()) {
                    if(soundEnabled)
                        startService(new Intent(com.example.burningbuilding.Floor7Activity.this, SoundServiceVictory.class));
                    else
                        vib.vibrate(500);

                    stopService(new Intent(com.example.burningbuilding.Floor7Activity.this, SoundServiceElevator.class));
                    new CountDownTimer(1000, 1000) {

                        public void onTick(long millisUntilFinished) {
                        }

                        public void onFinish() {
                            Intent intent = new Intent(com.example.burningbuilding.Floor7Activity.this, Floor6Activity.class);
                            intent.putExtra("timer", milisecondsOfGame);
                            intent.putExtra("sound",soundEnabled);
                            startActivity(intent);
                            finish();
                        }
                    }.start();
                }
                else {
                    Toast.makeText(Floor7Activity.this, R.string.wrong_answer, Toast.LENGTH_SHORT).show();
                    if (soundEnabled)
                        startService(new Intent(com.example.burningbuilding.Floor7Activity.this, SoundWrongAnswer.class));
                    else
                        vib.vibrate(500);
                }
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
                    stopService(new Intent(Floor7Activity.this, SoundServiceElevator.class));
                    item.setTitle(R.string.volume_on);
                    item.setIcon(R.drawable.ic_baseline_volume_up_24);
                }
                else
                {
                    soundEnabled = true;
                    startService(new Intent(Floor7Activity.this, SoundServiceElevator.class));
                    item.setTitle(R.string.volume_off);
                    item.setIcon(R.drawable.ic_baseline_volume_off_24);
                }
            }
            return true;
            case R.id.info_menu: {
                View dialogView = getLayoutInflater().inflate(R.layout.information_of_floors,null,false);
                AlertDialog.Builder builder = new AlertDialog.Builder(Floor7Activity.this);
                ((TextView)dialogView.findViewById(R.id.floor_info)).setText(R.string.info_floor_7);
                builder.setView(dialogView).setPositiveButton(R.string.info_back_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
            }
            return true;
            case R.id.gameRestart:{
                Intent intentRestart = new Intent(Floor7Activity.this,SettingsActivity.class);
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