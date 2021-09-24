package com.example.burningbuilding;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ListActivity;
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
import java.util.TimeZone;


public class EndGameActivity extends AppCompatActivity {
    ImageButton replay;
    TextView finished_game;
    ListView listView;
    long timeLeft;
    int score;
    String name = "";
    EditText name_ET;
    boolean soundEnabled;
    ArrayList<scoreListItem> scoreList = new ArrayList<>();
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);
        finished_game = findViewById(R.id.finished_game_tv);
        scoreList = getScoresFromSP();

        Intent intent = getIntent();
        soundEnabled = intent.getBooleanExtra("sound",true);
        timeLeft = intent.getLongExtra("timer",600000);
        score = ((int) timeLeft / 1000) + 8 * 100;

        if(soundEnabled)
            startService(new Intent(EndGameActivity.this, SoundEndGame.class));


        new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                View dialogView = getLayoutInflater().inflate(R.layout.activity_records, null, false);
                AlertDialog.Builder builder = new AlertDialog.Builder(EndGameActivity.this);
                TextView your_score = findViewById(R.id.your_score);
                String score_tv = ((TextView)dialogView.findViewById(R.id.your_score)).getText().toString();

                listView = (ListView)dialogView.findViewById(R.id.list);
                scoreAdapter score_Adapter = new scoreAdapter(scoreList,EndGameActivity.this);
                listView.setAdapter(score_Adapter);

               ((TextView)dialogView.findViewById(R.id.your_score)).setText(score_tv + " " + score);
                builder.setView(dialogView).setPositiveButton(R.string.save_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        name = ((TextView)dialogView.findViewById(R.id.nameInput)).getText().toString();
                        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

                        String date = calendar.get(Calendar.DAY_OF_MONTH) + "/" +
                                (calendar.get(Calendar.MONTH)+1) + "/" +
                                calendar.get(Calendar.YEAR);
                        scoreListItem newItem = new scoreListItem(name, score, date);
                        scoreList.add((newItem));
                        SharedPreferences sp= getSharedPreferences("records", MODE_PRIVATE);
                        sp.edit().putString("item_"+scoreList.size()+1,newItem.toString()).apply();
                        //Sort table by score
                    }
                }).setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();

                finished_game.setVisibility(View.GONE);

                replay= findViewById(R.id.replay_btn);
                replay.setVisibility(View.VISIBLE);
                replay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(EndGameActivity.this,SettingsActivity.class);
                        stopService(new Intent(EndGameActivity.this, SoundEndGame.class));
                        startActivity(intent);
                    }
                });
            }
        }.start();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<scoreListItem> getScoresFromSP()
    {
        ArrayList<scoreListItem> sortedList = new ArrayList<>();
        SharedPreferences sp= getSharedPreferences("records", MODE_PRIVATE);
        Map<String, ?> items = sp.getAll();
        //scoreList.add(new scoreListItem("Name",0,"Date"));
        items.forEach((k, v) -> sortedList.add(new scoreListItem(v.toString())));
        int size = sortedList.size();
        for(int i=0;i<size;i++)
        {
            //scoreListItem currentItem = scoreList.get(i);
            int currentMin=i;
            for(int j=0;j<size;j++)
            {
                if(sortedList.get(j).getScore() < sortedList.get(currentMin).getScore())
                {
                    currentMin=j;
                    //swap places of i with j
                    scoreListItem temp_Item_A = sortedList.get(i);
                    scoreListItem temp_Item_B = sortedList.get(currentMin);
                    sortedList.set(currentMin,temp_Item_A);
                    sortedList.set(i,temp_Item_B);
                }
            }
        }

        return sortedList;
    }
}

