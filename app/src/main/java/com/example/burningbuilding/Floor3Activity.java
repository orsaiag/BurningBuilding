package com.example.burningbuilding;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

public class Floor3Activity extends AppCompatActivity implements SensorEventListener {

    private ImageView ball,hole;
    private FrameLayout ball_Frame;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private long lastUpdate;

    CountDownTimer gameTimer;
    long milisecondsOfGame;

    public static int x = 0;
    public static int y = 0;
    public static int xStart, xEnd, yStart, yEnd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor3);

        ball = findViewById(R.id.metal_ball);
        hole = findViewById(R.id.hole_button);

        ball_Frame = findViewById(R.id.ball_Frame_To_Move);


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        lastUpdate = System.currentTimeMillis();

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
                startService(new Intent(Floor3Activity.this, SoundServiceElevator.class));
            }
            return true;
            case R.id.soundOff: {
                stopService(new Intent(Floor3Activity.this, SoundServiceElevator.class));
            }
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        xStart = (int) ball_Frame.getTranslationX();
        yStart = (int) ball_Frame.getTranslationY();
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
        if((ball.getX() >= hole.getX() && ball.getX() <= hole.getX()+hole.getWidth()) && (ball.getY() >= ball.getY() && ball.getY() <= hole.getY() + hole.getHeight()))
        {
            //Finish level
            hole.setImageResource(R.drawable.button_pressed1);
            startService(new Intent(Floor3Activity.this, SoundServiceVictory.class));
            stopService(new Intent(Floor3Activity.this, SoundServiceElevator.class));
            new CountDownTimer(1000, 1000) {

                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    Intent intent= new Intent(Floor3Activity.this, Floor2Activity.class);
                    intent.putExtra("timer", milisecondsOfGame);
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