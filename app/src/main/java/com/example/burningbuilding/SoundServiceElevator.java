package com.example.burningbuilding;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

    public class SoundServiceElevator extends GameSound  {
        public void onCreate()
        {
            super.onCreate(this,R.raw.elevator_music,true);
        }
    }