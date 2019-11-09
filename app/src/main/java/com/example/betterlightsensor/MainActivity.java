package com.example.betterlightsensor;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView textLIGHT_available, textLIGHT_reading;
    private int xTickValues = 0;
    LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
    Timer timer_sensor = new Timer();
    private double sensor_value = 0;
    Button stop, start;
    private SensorManager mySensorManager;
    private Sensor lightSensor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.addSeries(series);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxY(50);
        graph.getViewport().setMaxX(50);
        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);
        start = (Button) findViewById(R.id.start);
        start.setOnClickListener(this);
        stop = (Button) findViewById(R.id.stop);
        stop.setOnClickListener(this);

        textLIGHT_available
                = (TextView)findViewById(R.id.lightAvailable);
        textLIGHT_reading
                = (TextView)findViewById(R.id.lightReading);

        mySensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

        lightSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if(lightSensor != null){
            textLIGHT_available.setText("Sensor.TYPE_LIGHT Available");
            mySensorManager.registerListener(
                    lightSensorListener,
                    lightSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);

        } else {
            textLIGHT_available.setText("Sensor.TYPE_LIGHT NOT Available");
        }

    }

    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()){
            case R.id.start:
                xTickValues = 0;
                series.resetData(new DataPoint[]{
                        new DataPoint(xTickValues, sensor_value)
                });
                mySensorManager.registerListener(lightSensorListener,lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
                break;
            case R.id.stop:
                mySensorManager.unregisterListener(lightSensorListener);
                break;
        }

    }

    private final SensorEventListener lightSensorListener
            = new SensorEventListener(){

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if(event.sensor.getType() == Sensor.TYPE_LIGHT){
                sensor_value = event.values[0];
                textLIGHT_reading.setText("LIGHT: " + sensor_value);
                series.appendData(new DataPoint(xTickValues, sensor_value),true,50);
                xTickValues += 1;
            }
        }

    };

}
