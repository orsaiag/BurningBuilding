package com.example.burningbuilding;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    EditText nameInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ImageButton volumeOn = findViewById(R.id.musicOn_btn);
        ImageButton volumeOff = findViewById(R.id.musicOff_btn);
        ImageButton score = findViewById(R.id.score_btn);
        ImageButton play_btn = findViewById(R.id.startGame_btn);
        ImageButton info = findViewById(R.id.info_btn);


        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, Floor8Activity.class);
                startActivity(intent);
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = getLayoutInflater().inflate(R.layout.activity_loading,null,false);
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setView(dialogView).setPositiveButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
            }
        });

        volumeOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volumeOff.setVisibility(View.VISIBLE);
                volumeOn.setVisibility(View.INVISIBLE);
                stopService(new Intent(SettingsActivity.this, SoundServiceElevator.class));
            }
        });

        volumeOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volumeOff.setVisibility(View.INVISIBLE);
                volumeOn.setVisibility(View.VISIBLE);
                startService(new Intent(SettingsActivity.this, SoundServiceElevator.class));
            }
        });

        score.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongViewCast")
            @Override
            public void onClick(View v) {
                View dialogView = getLayoutInflater().inflate(R.layout.activity_records,null,false);
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                dialogView.findViewById(R.id.nameInput).setVisibility(View.INVISIBLE);
                builder.setView(dialogView).setPositiveButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
            }
        });
    }
}