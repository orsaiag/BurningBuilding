package com.example.burningbuilding;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Queue;

public class Floor1Activity extends AppCompatActivity {
    List<Animator> animatorList = new ArrayList<>();
    Boolean isRunning = false;

    CountDownTimer gameTimer;
    long milisecondsOfGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor1);

        ImageButton playButton = findViewById(R.id.play_button_morse);
        ImageButton openDoorButton = findViewById(R.id.open_door_IB);
        EditText passcoceTextField = findViewById(R.id.pass_code_tf);
        ImageView dotIV=findViewById(R.id.dot_button);
        ImageView lineIV=findViewById(R.id.line_button);

        ObjectAnimator letterO = ObjectAnimator.ofFloat(lineIV, "alpha", 0.1f, 1f).setDuration(1000);
        letterO.setRepeatCount(2);
        ObjectAnimator letterS = ObjectAnimator.ofFloat(dotIV, "alpha", 0.1f, 1f).setDuration(1000);
        letterS.setRepeatCount(2);

        AnimatorSet code = new AnimatorSet();
        code.play(letterS).before(letterO);
        AnimatorSet code2 = new AnimatorSet();
        code2.play(letterS).after(6000);

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

            public void onFinish() { // game over - timer has finished before finishing the floor
            }
        }.start();

        LinkedList<Integer> animQue = new LinkedList<>();
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                code.start();
                code2.start();
            }
        });
        openDoorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passcoceTextField.getText().toString().toUpperCase().equals("SOS")) {
                    //TODO: add next floor intent
                    Toast.makeText(Floor1Activity.this, "Right passcode door will open",
                            Toast.LENGTH_SHORT).show();
                    startService(new Intent(Floor1Activity.this, SoundServiceVictory.class));
                    stopService(new Intent(Floor1Activity.this, SoundServiceElevator.class));
                    Intent intent = new Intent(Floor1Activity.this, EndGameActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(Floor1Activity.this, "Wrong passcode :( try again",
                            Toast.LENGTH_SHORT).show();
                }

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
                startService(new Intent(Floor1Activity.this, SoundServiceElevator.class));
            }
            return true;
            case R.id.soundOff: {
                stopService(new Intent(Floor1Activity.this, SoundServiceElevator.class));
            }
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}