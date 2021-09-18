package com.example.burningbuilding;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Floor6Activity extends AppCompatActivity {

    CountDownTimer gameTimer;
    long milisecondsOfGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startService(new Intent(com.example.burningbuilding.Floor6Activity.this, SoundServiceElevator.class));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor6);

        EditText code_et = findViewById(R.id.code_floor6);
        Button done_btn = findViewById(R.id.done_btn);

        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = code_et.getText().toString();
                if (code.equals("5410")) {
                    startService(new Intent(com.example.burningbuilding.Floor6Activity.this, SoundServiceVictory.class));
                    stopService(new Intent(com.example.burningbuilding.Floor6Activity.this, SoundServiceElevator.class));
                    new CountDownTimer(2000, 1000) {

                        public void onTick(long millisUntilFinished) {
                        }

                        public void onFinish() {
                            Intent intent = new Intent(com.example.burningbuilding.Floor6Activity.this, Floor5Activity.class);
                            startActivity(intent);
                        }
                    }.start();
                }
                else
                    Toast.makeText(Floor6Activity.this, "Wrong answer-Please try again!", Toast.LENGTH_SHORT).show();
                    startService(new Intent(com.example.burningbuilding.Floor6Activity.this, SoundWrongAnswer.class));
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
                startService(new Intent(Floor6Activity.this, SoundServiceElevator.class));
            }
            return true;
            case R.id.soundOff: {
                stopService(new Intent(Floor6Activity.this, SoundServiceElevator.class));
            }
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}