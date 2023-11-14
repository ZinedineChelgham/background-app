package com.example.background_app;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONObject;

import java.util.Objects;

public class SensorForegroundService extends Service implements SensorEventListener {

    private float luxValue = 0;
    private final String TAG = "SensorForegroundService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /*if (Objects.equals(intent.getAction(), "stop")) {
            Log.i("issou", "Received Stop Foreground Intent");
            //your end servce code
            stopForeground(true);
            stopSelfResult(startId);
        }*/
        new Thread(()-> {
            SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

            Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            if (lightSensor != null) {
                sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
            }

            Sensor mGyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            if (mGyroscope != null) {
                sensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
            }
        }).start();

        return START_STICKY;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        JSONObject jsonObject = new JSONObject();
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            // Lux value is in event.values[0]
            float luxValue = event.values[0];
            try {
                jsonObject.put("lux", luxValue);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Log.i("service", String.format("acceleration: %f, %f, %f", x, y, z));
        } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            try {
                jsonObject.put("gyro_x", x);
                jsonObject.put("gyro_y", y);
                jsonObject.put("gyro_z", z);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String jsonString = jsonObject.toString();
      //  new PostData().execute(jsonString);
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // Do nothing for now
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
       // stopForeground(true);

    }
}
