package com.example.burningbuilding;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class SoundWrongAnswer extends GameSound {
    public void onCreate() {
        super.onCreate(this, R.raw.wrong_answer, false);
    }
}