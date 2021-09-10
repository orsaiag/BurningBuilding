package com.example.burningbuilding;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class Floor1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor1);
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
                startService(new Intent(Floor1Activity.this, SoundServiceElevator.class));
            }
            return true;
            case R.id.soundOff: {
                stopService(new Intent(Floor1Activity.this, SoundServiceElevator.class));
            }
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}