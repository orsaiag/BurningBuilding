package com.example.burningbuilding;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Queue;

public class Floor1Activity extends AppCompatActivity {

    CountDownTimer gameTimer;
    long milisecondsOfGame;
    TextView letterNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor1);

        letterNumber = findViewById(R.id.number_of_letter);
        ImageButton playButton = findViewById(R.id.play_button_morse);
        ImageButton openDoorButton = findViewById(R.id.open_door_IB);
        EditText passcoceTextField = findViewById(R.id.pass_code_tf);

        ImageView dotIV=findViewById(R.id.dot_button);
        ImageView lineIV=findViewById(R.id.line_button);

        ObjectAnimator letterO = ObjectAnimator.ofFloat(lineIV, "alpha", 1f, 0.1f).setDuration(1000);
        ObjectAnimator dotReturnAnim = ObjectAnimator.ofFloat(dotIV, "alpha", 1f);
        ObjectAnimator lineReturnAnim = ObjectAnimator.ofFloat(lineIV, "alpha", 1f);

        letterO.setRepeatCount(2);
        ObjectAnimator letterS = ObjectAnimator.ofFloat(dotIV, "alpha", 1f, 0.1f).setDuration(1000);
        letterS.setRepeatCount(2);
        letterO.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onAnimationEnd(Animator animation) {

                    letterNumber.setText(Integer.parseInt(letterNumber.getText().toString()) + 1 + "");
                lineReturnAnim.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        letterS.addListener(new Animator.AnimatorListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (letterNumber.getText().toString().equals("3")){
                    letterNumber.setVisibility(View.INVISIBLE);
                }
                else{
                    letterNumber.setText(Integer.parseInt(letterNumber.getText().toString()) + 1 + "");
                }
                dotReturnAnim.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        AnimatorSet code = new AnimatorSet();
        code.play(letterS).after(1000);
        AnimatorSet code2 = new AnimatorSet();
        code2.play(letterO).after(5000);
        AnimatorSet code3 = new AnimatorSet();
        code3.play(letterS).after(9000);

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
                letterNumber.setText("0");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        letterNumber.setText("1");
                        letterNumber.setVisibility(View.VISIBLE);


                    }
                }, 1000);


                code.start();
                code2.start();
                code3.start();

            }
        });
        openDoorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passcoceTextField.getText().toString().toUpperCase().equals("SOS")) {
                    Toast.makeText(Floor1Activity.this, "Right passcode door will open",
                            Toast.LENGTH_SHORT).show();
                    startService(new Intent(Floor1Activity.this, SoundServiceVictory.class));
                    stopService(new Intent(Floor1Activity.this, SoundServiceElevator.class));
                    Intent intent = new Intent(Floor1Activity.this, EndGameActivity.class);
                    intent.putExtra("timer", milisecondsOfGame);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(Floor1Activity.this, "Wrong answer-Please try again!",
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