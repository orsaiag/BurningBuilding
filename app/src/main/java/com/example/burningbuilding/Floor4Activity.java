package com.example.burningbuilding;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

public class Floor4Activity extends AppCompatActivity implements View.OnClickListener {

    int[] colorsArray = new int[]{R.color.black, R.color.blue, R.color.green, R.color.red, R.color.orange};
    int btn1Counter = 0, btn2Counter = 0, btn3Counter = 0;
    Button Button1, Button2, Button3;
    ImageView flowers;
    boolean soundEnabled;

    CountDownTimer gameTimer;
    long milisecondsOfGame;
    Vibrator vib;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor4);

        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        Button1 = findViewById(R.id.btn_1);
        Button2 = findViewById(R.id.btn_2);
        Button3 = findViewById(R.id.btn_3);

        Button1.setOnClickListener(this);
        Button2.setOnClickListener(this);
        Button3.setOnClickListener(this);

        flowers = findViewById(R.id.flowers);

        Intent intent = getIntent();
        soundEnabled = intent.getBooleanExtra("sound",true);
        milisecondsOfGame = intent.getLongExtra("timer",600000);

        if(soundEnabled)
            startService(new Intent(Floor4Activity.this, SoundServiceElevator.class));

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
                Intent intent = new Intent(Floor4Activity.this,GameOver.class);
                intent.putExtra("sound",soundEnabled);
                intent.putExtra("floor",4);
                stopService(new Intent(Floor4Activity.this, SoundServiceElevator.class));
                startActivity(intent);
            }
        }.start();
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
                    stopService(new Intent(Floor4Activity.this, SoundServiceElevator.class));
                    item.setTitle(R.string.volume_on);
                    item.setIcon(R.drawable.ic_baseline_volume_up_24);
                }
                else
                {
                    soundEnabled = true;
                    startService(new Intent(Floor4Activity.this, SoundServiceElevator.class));
                    item.setTitle(R.string.volume_off);
                    item.setIcon(R.drawable.ic_baseline_volume_off_24);
                }
            }
            return true;
            case R.id.info_menu: {
                View dialogView = getLayoutInflater().inflate(R.layout.information_of_floors,null,false);
                AlertDialog.Builder builder = new AlertDialog.Builder(Floor4Activity.this);
                ((TextView)dialogView.findViewById(R.id.floor_info)).setText(R.string.info_floor_4);
                builder.setView(dialogView).setPositiveButton(R.string.info_back_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
            }
            return true;
            case R.id.gameRestart:{
                Intent intentRestart = new Intent(Floor4Activity.this,SettingsActivity.class);
                intentRestart.putExtra("sound",soundEnabled);
                startActivity(intentRestart);
                finish();
            }
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_1) {
            btn1Counter++;
        } else if (v.getId() == R.id.btn_2) {
            btn2Counter++;
        } else if (v.getId() == R.id.btn_3) {
            btn3Counter++;
        }
        UpdateColors();
        CheckMatches();
    }

    @SuppressLint("ResourceAsColor")
    public void UpdateColors() {
        int currentBtnColor;
        //btn1
        currentBtnColor = btn1Counter % 5;
        Button1.setBackgroundColor(getResources().getColor(colorsArray[currentBtnColor]));
        //btn2
        currentBtnColor = btn2Counter % 5;
        Button2.setBackgroundColor(getResources().getColor(colorsArray[currentBtnColor]));
        //btn3
        currentBtnColor = btn3Counter % 5;
        Button3.setBackgroundColor(getResources().getColor(colorsArray[currentBtnColor]));
    }

    public void CheckMatches() // if all matches, we move the picture
    {
        if (btn3Counter % 5 == 4 && btn2Counter % 5 == 1 && btn1Counter % 5 == 2) {
            ImageButton elevator_btn = findViewById(R.id.elevator_btn);
            ObjectAnimator anim = ObjectAnimator.ofFloat(flowers, "translationX", 100).setDuration(2000);
            anim.start();
            elevator_btn.setVisibility(View.VISIBLE);
            elevator_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(soundEnabled)
                        startService(new Intent(Floor4Activity.this, SoundServiceVictory.class));
                    else
                        vib.vibrate(500);

                    stopService(new Intent(Floor4Activity.this, SoundServiceElevator.class));
                    new CountDownTimer(1000, 1000) {

                        public void onTick(long millisUntilFinished) {
                        }

                        public void onFinish() {
                            Intent intent = new Intent(Floor4Activity.this, Floor3Activity.class);
                            intent.putExtra("timer", milisecondsOfGame);
                            intent.putExtra("sound",soundEnabled);
                            startActivity(intent);
                            finish();
                        }
                    }.start();
                }
            });
        }
    }
}