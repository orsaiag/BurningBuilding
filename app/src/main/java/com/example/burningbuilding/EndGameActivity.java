package com.example.burningbuilding;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.CountDownTimer;
import android.view.View;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class EndGameActivity extends AppCompatActivity {
    ImageButton replay;
    TextView finished_game;
    ListView recordsLV;
    long timeLeft;
    int score;
    String name = "";
    EditText name_ET;
    ArrayList<scoreListItem> scoreList = new ArrayList<>();
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);

        //Extract score items
        SharedPreferences sp= getSharedPreferences("records", MODE_PRIVATE);
        Map<String, ?> items = sp.getAll();
        scoreList.add(new scoreListItem("Name",0,"Date"));
        items.forEach((k, v) -> scoreList.add(new scoreListItem(v.toString())));
        //End

        Intent intent = getIntent();
        timeLeft = intent.getLongExtra("timer",600000);
        score = ((int) timeLeft / 1000) + 8 * 100;

        new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                View dialogView = getLayoutInflater().inflate(R.layout.activity_records, null, false);
                AlertDialog.Builder builder = new AlertDialog.Builder(EndGameActivity.this);
                TextView your_score = findViewById(R.id.your_score);
                String score_tv = ((TextView)dialogView.findViewById(R.id.your_score)).getText().toString();

                recordsLV = (ListView)dialogView.findViewById(R.id.score_list);
                ArrayAdapter<scoreListItem> adapter = new ArrayAdapter<>(EndGameActivity.this, android.R.layout.simple_list_item_1,scoreList);
                recordsLV.setAdapter(adapter);

               ((TextView)dialogView.findViewById(R.id.your_score)).setText(score_tv + " " + score);
                builder.setView(dialogView).setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        name = ((TextView)dialogView.findViewById(R.id.nameInput)).getText().toString();
                        scoreListItem newItem = new scoreListItem(name, score, Calendar.getInstance().getTime().toString());
                        SharedPreferences sp= getSharedPreferences("records", MODE_PRIVATE);
                        sp.edit().putString("item",newItem.toString()).commit();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
            }
        }.start();

        finished_game = findViewById(R.id.finished_game_tv);
        finished_game.setVisibility(View.GONE);

        replay= findViewById(R.id.replay_btn);
        replay.setVisibility(View.VISIBLE);
        replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EndGameActivity.this,SettingsActivity.class);
                startActivity(intent);
            }
        });
    }
}

