package com.example.burningbuilding;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

public class GameSound extends Service{

    MediaPlayer mp;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public void onCreate(Context context, int Soundid, boolean loop)
    {
        mp = MediaPlayer.create(context, Soundid);
        mp.setLooping(loop);
    }
    public void onDestroy()
    {
        mp.stop();
    }
    public void onStart(Intent intent,int startid){
        mp.start();
    }
}