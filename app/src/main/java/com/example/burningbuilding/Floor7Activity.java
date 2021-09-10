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
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.material.chip.Chip;

public class Floor7Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startService(new Intent(Floor7Activity.this, SoundServiceElevator.class));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor7);

        RadioButton radio1=findViewById(R.id.radio1);
        RadioButton radio2=findViewById(R.id.radio2);
        RadioButton radio3=findViewById(R.id.radio3);
        RadioButton radio4=findViewById(R.id.radio4);
        RadioButton radio5=findViewById(R.id.radio5);
        RadioButton radio6=findViewById(R.id.radio6);

        Button done_btn= findViewById(R.id.done1_btn);
        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radio2.isChecked() && radio4.isChecked() && radio5.isChecked()) {
                    startService(new Intent(com.example.burningbuilding.Floor7Activity.this, SoundServiceVictory.class));
                    stopService(new Intent(com.example.burningbuilding.Floor7Activity.this, SoundServiceElevator.class));
                    new CountDownTimer(2000, 1000) {

                        public void onTick(long millisUntilFinished) {
                        }

                        public void onFinish() {
                            Intent intent = new Intent(com.example.burningbuilding.Floor7Activity.this, Floor6Activity.class);
                            startActivity(intent);
                        }
                    }.start();
                }
                else
                    Toast.makeText(Floor7Activity.this, "Wrong answer-Please try again!", Toast.LENGTH_SHORT).show();
                startService(new Intent(com.example.burningbuilding.Floor7Activity.this, SoundWrongAnswer.class));
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
                startService(new Intent(Floor7Activity.this, SoundServiceElevator.class));
            }
            return true;
            case R.id.soundOff: {
                stopService(new Intent(Floor7Activity.this, SoundServiceElevator.class));
            }
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}