package com.example.pvz_controller;

import java.util.Arrays;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.widget.TextView;

public class StartActivity extends Activity {

	//	private float[] rotVecVal = new float[4];
	//	private TextView txtRotVec;
	private final int UP_THRESHOLD = 5;
	private final int DOWN_THRESHOLD = 55;
	private final int RIGHT_THRESHOLD = 25;
	private final int LEFT_THRESHOLD = 25;
	private float[] orientationVal = new float[3];
	private TextView txtOrient;
	private TextView txtDirection;
	private Direction direction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);       
		SensorManager sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);

//      txtRotVec = (TextView) findViewById(R.id.rotVec);
//		txtOrient = (TextView) findViewById(R.id.orientation);
		txtDirection = (TextView) findViewById(R.id.direction);
		direction = Direction.STOPPED;

		final SensorEventListener mEventListener = new SensorEventListener() {
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
			}

			@SuppressWarnings("deprecation")
			public void onSensorChanged(SensorEvent event) {
				switch (event.sensor.getType()) {
				case Sensor.TYPE_ORIENTATION:
					System.arraycopy(event.values, 0, orientationVal, 0, 3);
					break;
//              case Sensor.TYPE_ROTATION_VECTOR:
//                  System.arraycopy(event.values, 0, rotVecVal, 0, 4);
//                  break;
				}
				txtOrient.setText("Orientation: " + Arrays.toString(orientationVal));
				determineDirection();
				txtDirection.setText("Direction: " + direction.name());
//              txtRotVec.setText("RotVec: " + Arrays.toString(rotVecVal));

			};
		};
		setListners(sensorManager, mEventListener);
	}
	
	public void determineDirection(){
		Direction newDir = direction;
		if (orientationVal[1] > -130 && orientationVal[1] < 130){
			if(orientationVal[1] > LEFT_THRESHOLD){
				newDir = Direction.LEFT;
			} else if (orientationVal[1] < -RIGHT_THRESHOLD){
				newDir = Direction.RIGHT;
			}
		} 
		if (orientationVal[2] > DOWN_THRESHOLD){
			newDir = Direction.DOWN;
		} else if (orientationVal[2] < -UP_THRESHOLD){
			newDir = Direction.UP;
		}
		direction = newDir;
	}

	@SuppressWarnings("deprecation")
	public void setListners(SensorManager sensorManager, SensorEventListener mEventListener)
	{
		sensorManager.registerListener(mEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), 
				SensorManager.SENSOR_DELAY_GAME);
		//        sensorManager.registerListener(mEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR), 
		//                SensorManager.SENSOR_DELAY_NORMAL);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start, menu);
		return true;
	}
}