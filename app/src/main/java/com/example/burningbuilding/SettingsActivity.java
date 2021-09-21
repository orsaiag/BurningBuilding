package com.example.burningbuilding;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

import com.example.burningbuilding.EndGameActivity;
public class SettingsActivity extends AppCompatActivity {
    EditText nameInput;
    boolean soundEnabled = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        startService(new Intent(SettingsActivity.this, SoundServiceElevator.class));

        ImageButton volumeOn = findViewById(R.id.musicOn_btn);
        ImageButton volumeOff = findViewById(R.id.musicOff_btn);
        ImageButton score = findViewById(R.id.score_btn);
        ImageButton play_btn = findViewById(R.id.startGame_btn);
        ImageButton info = findViewById(R.id.info_btn);


        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, Floor8Activity.class);
                intent.putExtra("sound",soundEnabled);
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
                soundEnabled = false;
            }
        });

        volumeOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volumeOff.setVisibility(View.INVISIBLE);
                volumeOn.setVisibility(View.VISIBLE);
                startService(new Intent(SettingsActivity.this, SoundServiceElevator.class));
                soundEnabled = true;
            }
        });

        score.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @SuppressLint("WrongViewCast")
            @Override
            public void onClick(View v) {
                View dialogView = getLayoutInflater().inflate(R.layout.activity_records,null,false);
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                dialogView.findViewById(R.id.nameInput).setVisibility(View.INVISIBLE);

                ArrayList<scoreListItem> scoreList = getScoresFromSP();
                ListView listView = (ListView)dialogView.findViewById(R.id.list);
                scoreAdapter score_Adapter = new scoreAdapter(scoreList,SettingsActivity.this);
                listView.setAdapter(score_Adapter);

                builder.setView(dialogView).setPositiveButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
            }
        });
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