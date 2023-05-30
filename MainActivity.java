package com.example.speed1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private TextView speedTextView;
    private Button resumeButton;
    private Button pauseButton;
    private boolean isPaused = false;
    private long lastUpdateTime = 0;
    private float lastX=0.0F,lastY=0.0F,lastZ= 0.0F;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        speedTextView = findViewById(R.id.speedometer);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        resumeButton = findViewById(R.id.startButton);
        pauseButton = findViewById(R.id.stopButton);
        resumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPaused = false;
            }
        });
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPaused = true;
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        // Register the sensor listener
        sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the sensor listener
        sensorManager.unregisterListener(sensorEventListener);
    }
    private final SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (!isPaused) {
                long currentTime = System.currentTimeMillis();
                long timeDifference = currentTime - lastUpdateTime;
                lastUpdateTime = currentTime;
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                float deltaX = x - lastX;
                float deltaY = y - lastY;
                float deltaZ = z - lastZ;
                double speed = ((Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) / timeDifference * 10000)-1)/1000;
                lastX = x;
                lastY = y;
                lastZ = z;

                if(speed<0)
                {
                    speedTextView.setText("0.00 Km/hr");
                }
                else {
                    speedTextView.setText(String.format("%.2f Km/hr", speed));
                }
            }
            else
            {
                speedTextView.setText("Stopped");
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
}