package com.example.burningbuilding;

        import androidx.appcompat.app.AppCompatActivity;

        import android.animation.Animator;
        import android.animation.AnimatorSet;
        import android.animation.ObjectAnimator;
        import android.animation.ValueAnimator;
        import android.annotation.SuppressLint;
        import android.content.Context;
        import android.content.Intent;
        import android.graphics.drawable.Drawable;
        import android.os.Bundle;
        import android.os.CountDownTimer;
        import android.os.Handler;
        import android.os.Vibrator;
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
    TextView letterNumber,wordNumber;
    boolean soundEnabled;
    Vibrator vib;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor1);

        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        letterNumber = findViewById(R.id.number_of_letter);
        wordNumber = findViewById(R.id.word_number_TV);
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
                    wordNumber.setVisibility(View.INVISIBLE);
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
        code2.play(letterO).after(7000);
        AnimatorSet code3 = new AnimatorSet();
        code3.play(letterS).after(12000);

        Intent intent = getIntent();
        soundEnabled = intent.getBooleanExtra("sound",true);
        milisecondsOfGame = intent.getLongExtra("timer",600000);

        if(soundEnabled)
            startService(new Intent(Floor1Activity.this, SoundServiceElevator.class));

        gameTimer = new CountDownTimer(milisecondsOfGame,1000) {

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

                milisecondsOfGame = millisUntilFinished;
                setTitle(limeLeft);
            }

            public void onFinish() { // game over - timer has finished before finishing the floor
                Intent intent = new Intent(Floor1Activity.this,GameOver.class);
                intent.putExtra("sound",soundEnabled);
                intent.putExtra("floor",1);
                stopService(new Intent(Floor1Activity.this, SoundServiceElevator.class));
                startActivity(intent);
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
                        wordNumber.setVisibility(View.VISIBLE);
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
                    if(soundEnabled)
                        startService(new Intent(Floor1Activity.this, SoundServiceVictory.class));
                    else
                        vib.vibrate(500);
                    stopService(new Intent(Floor1Activity.this, SoundServiceElevator.class));
                    new CountDownTimer(1000, 1000) {
                        public void onTick(long millisUntilFinished) {
                        }

                        public void onFinish() {
                            Intent intent = new Intent(Floor1Activity.this, EndGameActivity.class);
                            intent.putExtra("timer", milisecondsOfGame);
                            intent.putExtra("sound",soundEnabled);
                            startActivity(intent);
                            finish();
                        }
                    }.start();
                }
                else{
                    Toast.makeText(Floor1Activity.this, "Wrong answer-Please try again!",
                            Toast.LENGTH_SHORT).show();
                    if (soundEnabled)
                        startService(new Intent(com.example.burningbuilding.Floor1Activity.this, SoundWrongAnswer.class));
                    else
                        vib.vibrate(500);
                }
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
                    stopService(new Intent(Floor1Activity.this, SoundServiceElevator.class));
                    item.setTitle("Volume on");
                    item.setIcon(R.drawable.ic_baseline_volume_up_24);
                }
                else
                {
                    soundEnabled = true;
                    startService(new Intent(Floor1Activity.this, SoundServiceElevator.class));
                    item.setTitle("Volume off");
                    item.setIcon(R.drawable.ic_baseline_volume_off_24);
                }
            }
            return true;
            case R.id.info_menu: {
                stopService(new Intent(Floor1Activity.this, SoundServiceElevator.class));
            }
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}