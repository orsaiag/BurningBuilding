package com.example.burningbuilding;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class Floor2Activity extends AppCompatActivity{

    EditText ETLeft2,ETRight2,ETLeft3,ETRight3;

    CountDownTimer gameTimer;
    long milisecondsOfGame;
    boolean soundEnabled;
    Vibrator vib;
    boolean L1=false, L2=false, R1=false, R2=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor2);

        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        ETLeft2=findViewById(R.id.editTextLeft_2);
        ETRight2=findViewById(R.id.editTextRight_2);
        ETLeft3=findViewById(R.id.editTextLeft_3);
        ETRight3=findViewById(R.id.editTextRight_3);
        ETRight2.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                R1 = true;
                CheckIfRightAnswer();
            }
        });
        ETRight3.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                R2 = true;
                CheckIfRightAnswer();
            }
        });
        ETLeft2.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                L1 = true;
                CheckIfRightAnswer();
            }
        });
        ETLeft3.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                L2 = true;
                CheckIfRightAnswer();
            }
        });

        Intent intent = getIntent();
        soundEnabled = intent.getBooleanExtra("sound",true);
        milisecondsOfGame = intent.getLongExtra("timer",600000);

        if(soundEnabled)
            startService(new Intent(Floor2Activity.this, SoundServiceElevator.class));

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
                Intent intent = new Intent(Floor2Activity.this,GameOver.class);
                intent.putExtra("sound",soundEnabled);
                intent.putExtra("floor",2);
                stopService(new Intent(Floor2Activity.this, SoundServiceElevator.class));
                startActivity(intent);
            }
        }.start();
    }

    public void CheckIfRightAnswer()
    {
        if(ETLeft3.getText().toString().equals("5") && ETLeft2.getText().toString().equals("7") &&
                ETRight2.getText().toString().equals("4") && ETRight3.getText().toString().equals("10"))
        {
            if(soundEnabled)
                startService(new Intent(Floor2Activity.this, SoundServiceVictory.class));
            else
                vib.vibrate(500);
            stopService(new Intent(Floor2Activity.this, SoundServiceElevator.class));
            new CountDownTimer(1000, 1000) {

                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    Intent intent = new Intent(Floor2Activity.this, Floor1Activity.class);
                    intent.putExtra("timer", milisecondsOfGame);
                    intent.putExtra("sound",soundEnabled);
                    startActivity(intent);
                    finish();
                }
            }.start();
        }
        else
        {
            CheckAllEditBoxes();
        }
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
                    stopService(new Intent(Floor2Activity.this, SoundServiceElevator.class));
                    item.setTitle(R.string.volume_on);
                    item.setIcon(R.drawable.ic_baseline_volume_up_24);
                }
                else
                {
                    soundEnabled = true;
                    startService(new Intent(Floor2Activity.this, SoundServiceElevator.class));
                    item.setTitle(R.string.volume_off);
                    item.setIcon(R.drawable.ic_baseline_volume_off_24);
                }
            }
            return true;
            case R.id.info_menu: {
                View dialogView = getLayoutInflater().inflate(R.layout.information_of_floors,null,false);
                AlertDialog.Builder builder = new AlertDialog.Builder(Floor2Activity.this);
                ((TextView)dialogView.findViewById(R.id.floor_info)).setText(R.string.info_floor_2);
                builder.setView(dialogView).setPositiveButton(R.string.info_back_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
            }
            return true;
            case R.id.gameRestart:{
                Intent intentRestart = new Intent(Floor2Activity.this,SettingsActivity.class);
                intentRestart.putExtra("sound",soundEnabled);
                startActivity(intentRestart);
                finish();
            }
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void CheckAllEditBoxes()
    {
        if(R1 && R2 && L1 && L2)
        {
            Toast.makeText(this, R.string.wrong_answer, Toast.LENGTH_SHORT).show();
            if (soundEnabled)
                startService(new Intent(com.example.burningbuilding.Floor2Activity.this, SoundWrongAnswer.class));
            else
                vib.vibrate(500);
        }
    }
}