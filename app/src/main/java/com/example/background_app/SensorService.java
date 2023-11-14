package com.example.background_app;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SensorService extends Service implements SensorEventListener {

    public List<Pair<String, JSONObject>> cache = new ArrayList<>();
    private float luxValue = 0;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(() -> {

            // Initialize SensorManager
            SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

            Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            if (lightSensor != null) {
                sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
            }

            Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (accelerometerSensor != null) {
                sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        return START_STICKY;
    }

    private void setupAndRegisterSensors() {
        Log.i("Service", "Service is running...");
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        JSONObject jsonObject = new JSONObject();
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            // Lux value is in event.values[0]
            float luxValue = event.values[0];
            Log.i("service", String.format("lux: %f", luxValue));
            this.luxValue = luxValue;
            jsonObject = new JSONObject();
            try {
                jsonObject.put("lux", this.luxValue);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String jsonString = jsonObject.toString();
            new PostData().execute(jsonString);

        } else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // Accelerometer values are in event.values[0], event.values[1], event.values[2]
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            //Log.i("service", String.format("acceleration: %f, %f, %f", x, y, z));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // Do nothing for now
    }
}
