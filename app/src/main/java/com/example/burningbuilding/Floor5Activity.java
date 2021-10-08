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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class Floor5Activity extends AppCompatActivity {
    int counterBlack = 0, counterYellow=0, counterOrange=0,counterBlue=0,counterGreen=0;
    final int BLACK = 4, BLUE = 2, ORANGE=5 ,YELLOW=3 ,GREEN= 1;

    CountDownTimer gameTimer;
    long milisecondsOfGame;
    boolean soundEnabled;
    TextView press_here_tv;
    Animation animation;

    Button yellow_btn;
    Button black_btn;
    Button blue_btn;
    Button green_btn;
    Button orange_btn;
    Vibrator vib;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor5);

        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        yellow_btn=findViewById(R.id.yellow_btn);
        black_btn=findViewById(R.id.black_btn);
        blue_btn=findViewById(R.id.blue_btn);
        green_btn=findViewById(R.id.green_btn);
        orange_btn=findViewById(R.id.orange_btn);
        press_here_tv= findViewById(R.id.press_tv);


        press_here_tv.setVisibility(View.INVISIBLE);

        orange_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counterOrange++;
                orange_btn.setText(counterOrange+"");
                checkColorsCounter();
            }
        });

        yellow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counterYellow++;
                yellow_btn.setText(counterYellow+"");
                checkColorsCounter();
            }
        });
        black_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counterBlack++;
                black_btn.setText(counterBlack+"");
                checkColorsCounter();
            }
        });
        blue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counterBlue++;
                blue_btn.setText(counterBlue+"");
                checkColorsCounter();
            }
        });
        green_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counterGreen++;
                green_btn.setText(counterGreen+"");
                checkColorsCounter();
            }
        });

        ImageButton elevator_btn=findViewById(R.id.elevator_btn);
        ImageButton replay=findViewById(R.id.reset_counter_btn);

        animation = AnimationUtils.loadAnimation(Floor5Activity.this, R.anim.flashing_elevator_btn);
        animation.setStartTime(3000);
        elevator_btn.startAnimation(animation);

        elevator_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckCounters();
            }
        });

        replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counterOrange=0;
                counterGreen=0;
                counterYellow=0;
                counterBlue=0;
                counterBlack=0;
                green_btn.setText(counterGreen+"");
                black_btn.setText(counterBlack+"");
                yellow_btn.setText(counterYellow+"");
                orange_btn.setText(counterOrange+"");
                blue_btn.setText(counterBlue+"");
            }
        });

        Intent intent = getIntent();
        soundEnabled = intent.getBooleanExtra("sound",true);
        milisecondsOfGame = intent.getLongExtra("timer",600000);

        if(soundEnabled)
            startService(new Intent(Floor5Activity.this, SoundServiceElevator.class));

        gameTimer = new CountDownTimer(milisecondsOfGame,1000) {

            public void onTick(long millisUntilFinished) {
                long minutes;
                int seconds;
                String limeLeft;
                minutes = millisUntilFinished / 60000;
                seconds = (int)(millisUntilFinished % 60000 / 1000);
                if(Locale.getDefault().getDisplayLanguage()=="iw") {
                    if (seconds < 10)
                        limeLeft = String.format("הזמן שנותר: 00:0%d:0%d",minutes,seconds);
                    else
                        limeLeft = String.format("הזמן שנותר: 00:0%d:%d",minutes,seconds);
                }
                else
                {
                    if (seconds < 10)
                        limeLeft = String.format("Time left: 00:0%d:0%d",minutes,seconds);
                    else
                        limeLeft = String.format("Time left: 00:0%d:%d",minutes,seconds);
                }

                milisecondsOfGame = millisUntilFinished;
                setTitle(limeLeft);
            }

            public void onFinish() { // game over - timer has finished before finishing the floors
                Intent intent = new Intent(Floor5Activity.this,GameOver.class);
                intent.putExtra("sound",soundEnabled);
                intent.putExtra("floor",5);
                stopService(new Intent(Floor5Activity.this, SoundServiceElevator.class));
                startActivity(intent);
            }
        }.start();
    }


    public void CheckCounters(){
        if(counterBlack == BLACK && counterBlue == BLUE && counterYellow == YELLOW && counterGreen == GREEN && counterOrange == ORANGE)
        {
            press_here_tv.setVisibility(View.VISIBLE);
            press_here_tv.startAnimation(animation);
            if(soundEnabled)
                startService(new Intent(Floor5Activity.this, SoundServiceVictory.class));
            else {
                vib.vibrate(500);
            }
            stopService(new Intent(Floor5Activity.this, SoundServiceElevator.class));
            new CountDownTimer(1000, 1000) {

                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    Intent intent = new Intent(Floor5Activity.this, Floor4Activity.class);
                    intent.putExtra("timer", milisecondsOfGame);
                    intent.putExtra("sound",soundEnabled);
                    startActivity(intent);
                    finish();
                }
            }.start();
        }
        else
        {
            Toast.makeText(this, R.string.wrong_answer, Toast.LENGTH_SHORT).show();
            if (soundEnabled)
                startService(new Intent(com.example.burningbuilding.Floor5Activity.this, SoundWrongAnswer.class));
            else
                vib.vibrate(500);
            counterOrange=0;
            counterGreen=0;
            counterYellow=0;
            counterBlue=0;
            counterBlack=0;
            green_btn.setText(counterGreen+"");
            black_btn.setText(counterBlack+"");
            yellow_btn.setText(counterYellow+"");
            orange_btn.setText(counterOrange+"");
            blue_btn.setText(counterBlue+"");
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
                    stopService(new Intent(Floor5Activity.this, SoundServiceElevator.class));
                    item.setTitle(R.string.volume_on);
                    item.setIcon(R.drawable.ic_baseline_volume_up_24);
                }
                else
                {
                    soundEnabled = true;
                    startService(new Intent(Floor5Activity.this, SoundServiceElevator.class));
                    item.setTitle(R.string.volume_off);
                    item.setIcon(R.drawable.ic_baseline_volume_off_24);
                }
            }
            return true;
            case R.id.info_menu: {
                View dialogView = getLayoutInflater().inflate(R.layout.information_of_floors,null,false);
                AlertDialog.Builder builder = new AlertDialog.Builder(Floor5Activity.this);
                ((TextView)dialogView.findViewById(R.id.floor_info)).setText(R.string.info_floor_5);
                builder.setView(dialogView).setPositiveButton(R.string.info_back_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
            }
            return true;
            case R.id.gameRestart:{
                Intent intentRestart = new Intent(Floor5Activity.this,SettingsActivity.class);
                intentRestart.putExtra("sound",soundEnabled);
                startActivity(intentRestart);
                finish();
            }
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void checkColorsCounter()
    {
        if(counterBlack == BLACK && counterBlue == BLUE && counterYellow == YELLOW && counterGreen == GREEN && counterOrange == ORANGE) {
            press_here_tv.setVisibility(View.VISIBLE);
            press_here_tv.startAnimation(animation);
        }
    }
}