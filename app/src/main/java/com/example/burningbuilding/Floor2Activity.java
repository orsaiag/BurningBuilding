package com.example.burningbuilding;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class Floor2Activity extends AppCompatActivity{

    EditText ETLeft2,ETRight2,ETLeft3,ETRight3;

    CountDownTimer gameTimer;
    long milisecondsOfGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor2);
        startService(new Intent(Floor2Activity.this, SoundServiceElevator.class));

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
                CheckIfRightAnswer();
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
                //Intent intent = new Intent(Floor8Activity.this,RecordsActivity.class);

                //startActivity(intent);
            }
        }.start();
    }

    public void CheckIfRightAnswer()
    {
        if(ETLeft3.getText().toString().equals("5") && ETLeft2.getText().toString().equals("7") &&
                ETRight2.getText().toString().equals("4") && ETRight3.getText().toString().equals("10"))
        {
            startService(new Intent(Floor2Activity.this, SoundServiceVictory.class));
            stopService(new Intent(Floor2Activity.this, SoundServiceElevator.class));
            new CountDownTimer(2000, 1000) {

                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    Intent intent = new Intent(Floor2Activity.this, Floor1Activity.class);
                    intent.putExtra("timer", milisecondsOfGame);
                    startActivity(intent);
                    finish();
                }
            }.start();
        }
        else
        {
            Toast.makeText(this, "Wrong answer-Please try again!", Toast.LENGTH_SHORT).show();
            startService(new Intent(Floor2Activity.this, SoundWrongAnswer.class));
            stopService(new Intent(Floor2Activity.this, SoundServiceElevator.class));
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
                startService(new Intent(Floor2Activity.this, SoundServiceElevator.class));
            }
            return true;
            case R.id.soundOff: {
                stopService(new Intent(Floor2Activity.this, SoundServiceElevator.class));
            }
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}