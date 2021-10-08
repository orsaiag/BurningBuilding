package com.example.burningbuilding;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

public class Floor3Activity extends AppCompatActivity implements SensorEventListener {

    private ImageView ball,hole;
    private FrameLayout ball_Frame;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private long lastUpdate;
    private boolean soundEnabled;
    Vibrator vib;

    CountDownTimer gameTimer;
    long milisecondsOfGame;

    public static int x = 0;
    public static int y = 0;
    public static int xStart, xEnd, yStart, yEnd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor3);

        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        ball = findViewById(R.id.metal_ball);
        hole = findViewById(R.id.hole_button);

        ball_Frame = findViewById(R.id.ball_Frame_To_Move);
        ball.setX(ball_Frame.getX());
        ball.setY(ball_Frame.getY());

        hole.setX((ball_Frame.getX() + ball_Frame.getWidth())/2);
        hole.setY((ball_Frame.getY() + ball_Frame.getHeight())/2);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        lastUpdate = System.currentTimeMillis();

        Intent intent = getIntent();
        soundEnabled = intent.getBooleanExtra("sound",true);
        milisecondsOfGame = intent.getLongExtra("timer",600000);

        if(soundEnabled)
            startService(new Intent(Floor3Activity.this, SoundServiceElevator.class));

        gameTimer = new CountDownTimer(milisecondsOfGame,1000) {

            public void onTick(long millisUntilFinished) {
                long minutes;
                int seconds;
                String limeLeft;
                minutes = millisUntilFinished / 60000;
                seconds = (int)(millisUntilFinished % 60000 / 1000);
                if(Locale.getDefault().getDisplayLanguage()=="iw") {
                    if (seconds < 10)
                        limeLeft = String.format("הזמן שנותר: 00:0%d:0%d",minutes,seconds);
                    else
                        limeLeft = String.format("הזמן שנותר: 00:0%d:%d",minutes,seconds);
                }
                else
                {
                    if (seconds < 10)
                        limeLeft = String.format("Time left: 00:0%d:0%d",minutes,seconds);
                    else
                        limeLeft = String.format("Time left: 00:0%d:%d",minutes,seconds);
                }

                milisecondsOfGame = millisUntilFinished;
                setTitle(limeLeft);
            }

            public void onFinish() { // game over - timer has finished before finishing the floors
                Intent intent = new Intent(Floor3Activity.this,GameOver.class);
                intent.putExtra("sound",soundEnabled);
                intent.putExtra("floor",3);
                stopService(new Intent(Floor3Activity.this, SoundServiceElevator.class));
                startActivity(intent);
            }
        }.start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        if(soundEnabled)
        {
            menu.findItem(R.id.soundMenu).setTitle(R.string.volume_off);
            menu.findItem(R.id.soundMenu).setIcon(R.drawable.ic_baseline_volume_off_24);
        }
        else
        {
            menu.findItem(R.id.soundMenu).setTitle(R.string.volume_on);
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
                    stopService(new Intent(Floor3Activity.this, SoundServiceElevator.class));
                    item.setTitle(R.string.volume_on);
                    item.setIcon(R.drawable.ic_baseline_volume_up_24);
                }
                else
                {
                    soundEnabled = true;
                    startService(new Intent(Floor3Activity.this, SoundServiceElevator.class));
                    item.setTitle(R.string.volume_off);
                    item.setIcon(R.drawable.ic_baseline_volume_off_24);
                }
            }
            return true;
            case R.id.info_menu: {
                View dialogView = getLayoutInflater().inflate(R.layout.information_of_floors,null,false);
                AlertDialog.Builder builder = new AlertDialog.Builder(Floor3Activity.this);
                ((TextView)dialogView.findViewById(R.id.floor_info)).setText(R.string.info_floor_3);
                builder.setView(dialogView).setPositiveButton(R.string.info_back_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
            }
            return true;
            case R.id.gameRestart:{
                Intent intentRestart = new Intent(Floor3Activity.this,SettingsActivity.class);
                intentRestart.putExtra("sound",soundEnabled);
                startActivity(intentRestart);
                finish();
            }
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        xStart = (int) ball_Frame.getX();
        yStart = (int) ball_Frame.getY();
        xEnd = xStart + ball_Frame.getWidth() - ball.getWidth();
        yEnd = yStart + ball_Frame.getHeight() - ball.getHeight();

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            x -= (int) event.values[0];
            y += (int) event.values[1];

            if(x >= xStart && x <= xEnd)
            {
                ball.setX(x);
            }
            if(y >= yStart && y <= yEnd)
            {
                ball.setY(y);
            }

            checkButtonPressed();
        }
    }

    public void checkButtonPressed()
    {
        boolean intersects = false;
        if((ball.getX() > hole.getX() && ball.getX() <= hole.getX()+hole.getWidth()) && (ball.getY() > hole.getY() && ball.getY() <= hole.getY() + hole.getHeight()))
            intersects = true;
        if((ball.getX()+ball.getWidth() > hole.getX() && ball.getX() <= hole.getX()+hole.getWidth()) && (ball.getY()+ball.getHeight() > hole.getY() && ball.getY() <= hole.getY() + hole.getHeight()))
            intersects = true;
        if((ball.getX() +ball.getWidth() > hole.getX() && ball.getX() <= hole.getX()+hole.getWidth()) && (ball.getY() > hole.getY() && ball.getY() <= hole.getY() + hole.getHeight()))
            intersects = true;
        if((ball.getX()+ball.getWidth() > hole.getX() && ball.getX() <= hole.getX()+hole.getWidth()) && (ball.getY() > hole.getY() && ball.getY() <= hole.getY() + hole.getHeight()))
            intersects = true;

        if(intersects)
        {
            //Finish level
            hole.setImageResource(R.drawable.button_pressed1);
            if(soundEnabled)
                startService(new Intent(Floor3Activity.this, SoundServiceVictory.class));
            else
                vib.vibrate(500);
            stopService(new Intent(Floor3Activity.this, SoundServiceElevator.class));
            new CountDownTimer(1000, 1000) {

                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    Intent intent= new Intent(Floor3Activity.this, Floor2Activity.class);
                    intent.putExtra("timer", milisecondsOfGame);
                    intent.putExtra("sound",soundEnabled);
                    startActivity(intent);
                    finish();
                }
            }.start();
        }
        else
        {
            hole.setImageResource(R.drawable.greenbutton);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer,
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}