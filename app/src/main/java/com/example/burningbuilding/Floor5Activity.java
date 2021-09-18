package com.example.burningbuilding;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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

public class Floor5Activity extends AppCompatActivity {
    int counterBlack = 0, counterYellow=0, counterOrange=0,counterBlue=0,counterGreen=0;
    final int BLACK = 4, BLUE = 2, ORANGE=5 ,YELLOW=3 ,GREEN= 1;

    CountDownTimer gameTimer;
    long milisecondsOfGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor5);
        startService(new Intent(Floor5Activity.this, SoundServiceElevator.class));

        Button yellow_btn=findViewById(R.id.yellow_btn);
        Button black_btn=findViewById(R.id.black_btn);
        Button blue_btn=findViewById(R.id.blue_btn);
        Button green_btn=findViewById(R.id.green_btn);
        Button orange_btn=findViewById(R.id.orange_btn);
        TextView press_here_tv= findViewById(R.id.press_tv);

        orange_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counterOrange++;
            }
        });

        yellow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counterYellow++;
            }
        });
        black_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counterBlack++;
            }
        });
        blue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counterBlue++;
            }
        });
        green_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counterGreen++;
            }
        });

        ImageButton elevator_btn=findViewById(R.id.elevator_btn);

        Animation animation = AnimationUtils.loadAnimation(Floor5Activity.this, R.anim.flashing_elevator_btn);
        animation.setStartTime(3000);
        elevator_btn.startAnimation(animation);
        press_here_tv.startAnimation(animation);

        elevator_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckCounters();
            }
        });

        Intent intent = getIntent();
        milisecondsOfGame = intent.getLongExtra("timer",600000);

        gameTimer = new CountDownTimer(milisecondsOfGame,1000) {

            public void onTick(long millisUntilFinished) {
                long minutes;
                int seconds;
                String limeLeft;
                minutes = millisUntilFinished / 60000;
                seconds = (int)(millisUntilFinished % 60000 / 1000);
                limeLeft = "Time left: 00:0" + minutes + ":" + seconds;

                milisecondsOfGame = millisUntilFinished;
                setTitle(limeLeft);
            }

            public void onFinish() { // game over - timer has finished before finishing the floors

            }
        }.start();
    }


    public void CheckCounters(){
        if(counterBlack == BLACK && counterBlue == BLUE && counterYellow == YELLOW && counterGreen == GREEN && counterOrange == ORANGE)
        {
            startService(new Intent(Floor5Activity.this, SoundServiceVictory.class));
            stopService(new Intent(Floor5Activity.this, SoundServiceElevator.class));
            new CountDownTimer(2000, 1000) {

                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    Intent intent = new Intent(Floor5Activity.this, Floor4Activity.class);
                    startActivity(intent);
                }
            }.start();
        }
        else
        {
            Toast.makeText(this, "Wrong answer-Please try again!", Toast.LENGTH_SHORT).show();
            counterOrange=0;
            counterGreen=0;
            counterYellow=0;
            counterBlue=0;
            counterBlack=0;
            startService(new Intent(Floor5Activity.this, SoundWrongAnswer.class));
            stopService(new Intent(Floor5Activity.this, SoundServiceElevator.class));
        }
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
                startService(new Intent(Floor5Activity.this, SoundServiceElevator.class));
            }
            return true;
            case R.id.soundOff: {
                stopService(new Intent(Floor5Activity.this, SoundServiceElevator.class));
            }
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}