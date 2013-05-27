package org.verletjs.Handlers;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import org.verletjs.VerletCore.Vec2;
import org.verletjs.VerletCore.Verlet;

/**
 * Created with IntelliJ IDEA.
 * User: George
 * Date: 01.05.13
 * Time: 13:24
 * To change this template use File | Settings | File Templates.
 */
public class AccelerometerVerletHandler implements SensorEventListener {
    private static final String TAG = AccelerometerVerletHandler.class.getName().toString();

    float accelX;
    float accelY;
    float accelZ;
    private Verlet verlet;
    private long lastUpdate;
    private Vec2 vec = new Vec2();
    public AccelerometerVerletHandler(Context context, Verlet verlet) {
        this.verlet = verlet;

        SensorManager manager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        if(manager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() == 0) {
            Log.v(TAG, "No accelerometer installed");
        } else{
            Sensor accelerometer = manager.getSensorList(
                    Sensor.TYPE_ACCELEROMETER).get(0);
            if(!manager.registerListener(this, accelerometer,
                    SensorManager.SENSOR_DELAY_GAME)) {
                Log.v(TAG,"Couldn't register sensor listener");
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        accelX = event.values[0];
        accelY = event.values[1];
        accelZ = event.values[2];

        long actualTime = System.currentTimeMillis();

        if (actualTime - lastUpdate < 200) {
            //Если с момента начала тряски прошло меньше 200
            // миллисекунд - выходим из обработчика
            return;
        }
        lastUpdate = actualTime;
        vec.x = - accelX * verlet.getStandartGravity() / SensorManager.GRAVITY_EARTH;
        vec.y = accelY * verlet.getStandartGravity() / SensorManager.GRAVITY_EARTH;
        verlet.setGravity(vec);
    }
    public float getAccelX() {
        return accelX;
    }
    public float getAccelY() {
        return accelY;
    }
    public float getAccelZ() {
        return accelZ;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
