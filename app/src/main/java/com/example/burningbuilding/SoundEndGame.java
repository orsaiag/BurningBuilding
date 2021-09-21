package com.example.burningbuilding;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

public class SoundEndGame extends GameSound  {
    public void onCreate()
    {
        super.onCreate(this,R.raw.wining_song,true);
    }
}