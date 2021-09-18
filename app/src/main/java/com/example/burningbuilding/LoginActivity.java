package com.example.burningbuilding;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startService(new Intent(LoginActivity.this, SoundServiceElevator.class));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button continur_btn =findViewById(R.id.continur_btn);
        EditText name_Et = findViewById(R.id.nameInput);

        continur_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name= name_Et.getText().toString();
                if(name.length() >= 1) {
                    Intent intent = new Intent(LoginActivity.this, SettingsActivity.class);
                    intent.putExtra("name", name);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Empty name is not valid!", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                startService(new Intent(LoginActivity.this, SoundServiceElevator.class));
            }
            return true;
            case R.id.soundOff: {
                stopService(new Intent(LoginActivity.this, SoundServiceElevator.class));
            }
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}