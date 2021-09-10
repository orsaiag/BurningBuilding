package com.example.burningbuilding;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class Floor4Activity extends AppCompatActivity implements View.OnClickListener {

    int[] colorsArray = new int[]{R.color.black,R.color.blue,R.color.green,R.color.red,R.color.orange};
    int btn1Counter = 0, btn2Counter = 0, btn3Counter = 0;
    Button Button1,Button2,Button3;
    ImageView flowers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor4);
        Button1 = findViewById(R.id.btn_1);
        Button2 = findViewById(R.id.btn_2);
        Button3 = findViewById(R.id.btn_3);

        //Button1.setOnClickListener((View.OnClickListener) Floor4Activity.this);
        Button1.setOnClickListener(this);
        Button2.setOnClickListener(this);
        Button3.setOnClickListener(this);

        flowers= findViewById(R.id.flowers);
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
                startService(new Intent(Floor4Activity.this, SoundServiceElevator.class));
            }
            return true;
            case R.id.soundOff: {
                stopService(new Intent(Floor4Activity.this, SoundServiceElevator.class));
            }
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_1) {
            btn1Counter++;
        }
        else if(v.getId() == R.id.btn_2) {
            btn2Counter++;
        }
        else if(v.getId() == R.id.btn_3){
            btn3Counter++;
        }
        UpdateColors();
        CheckMatches();
    }

    public void UpdateColors()
    {
        int currentBtnColor;
        //btn1
        currentBtnColor = btn1Counter%5;
        Button1.setBackgroundColor(colorsArray[currentBtnColor]);
        //btn2
        currentBtnColor = btn2Counter%5;
        Button2.setBackgroundColor(colorsArray[currentBtnColor]);
        //btn3
        currentBtnColor = btn3Counter%5;
        Button3.setBackgroundColor(colorsArray[currentBtnColor]);
    }

    public void CheckMatches() // if all matches, we move the picture
    {
        if (btn3Counter%5 == 4 && btn2Counter%5 == 1 && btn1Counter%5 == 2)
        {
            ImageButton elevator_btn=findViewById(R.id.elevator_btn);
            Animation animation = AnimationUtils.loadAnimation(Floor4Activity.this, R.anim.flowers_move_anim);
            animation.setStartTime(4000);
            elevator_btn.setVisibility(View.VISIBLE);
            flowers.startAnimation(animation);
        }
    }
}