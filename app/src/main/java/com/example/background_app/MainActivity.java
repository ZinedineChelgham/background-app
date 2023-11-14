package com.example.background_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Intent intent = new Intent(this, SensorService.class);
        Intent intent = new Intent(this, SensorForegroundService.class);
        startService(intent);

        Button startButton = findViewById(R.id.b_start);
        startButton.setOnClickListener(v -> {
            Intent intentt = new Intent(this, SensorForegroundService.class);
            startService(intent);
        });

        Button stopButton = findViewById(R.id.b_stop);
        stopButton.setOnClickListener(v -> {
            Intent intentt = new Intent(this, SensorForegroundService.class);
            intent.setAction("stop");
            stopService(intent);
        });
    }

    @Override
    public void onDestroy() {
        Intent intent = new Intent(this, SensorForegroundService.class);
        intent.setAction("stop");
        stopService(intent);
        super.onDestroy();

    }
}