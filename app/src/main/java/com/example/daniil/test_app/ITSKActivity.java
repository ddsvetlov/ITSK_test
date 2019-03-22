package com.example.daniil.test_app;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class ITSKActivity extends Activity implements SensorEventListener, StepListener {
     TextView tv_steps;
    private StepDetector stepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private Sensor linAccel;
    private int numSteps;
    float[] valuesLinAccel= new float[3];
    float[] valuesAccel= new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itsk);
        tv_steps= findViewById(R.id.tv_steps);
        //Закрпеляем ориентацию
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        linAccel =sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        stepDetector = new StepDetector();
        stepDetector.registerListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        numSteps = 0;

        tv_steps.setText(String.valueOf(numSteps));
        //Проверка наличия датчика акселерометра
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (countSensor!=null) {
            sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_FASTEST);
            sensorManager.registerListener(this, linAccel, SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            Toast.makeText(this, "Accelerometer Sensor Not Found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()){
            case Sensor.TYPE_ACCELEROMETER:
                for(int i=0; i<3;i++) {
                    valuesAccel[i] = event.values[i];
                }
                break;
            case Sensor.TYPE_LINEAR_ACCELERATION:
                for(int i=0; i<3;i++) {
                    valuesLinAccel[i] = event.values[i];
                }
                break;
        }
        stepDetector.updateAccel( valuesAccel, valuesLinAccel);
    }

    @Override
    public void step() {
        numSteps++;
        tv_steps.setText(String.valueOf(numSteps));
    }
}