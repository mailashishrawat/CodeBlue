package com.indiainclusionsummit.indiainclusionsummit;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Created by I064893 on 11/5/2015.
 */
public class ShakeEventManager implements SensorEventListener {
    /** Minimum movement force to consider. */
    private static final int MIN_FORCE = 15;

    /**
     * Minimum times in a shake gesture that the direction of movement needs to
     * change.
     */
    private static final int MIN_DIRECTION_CHANGE = 10;

    /** Maximum pause between movements. */
    private static final int MAX_PAUSE_BETHWEEN_DIRECTION_CHANGE = 100;

    /** Maximum allowed time for shake gesture. */
    private static final int MAX_TOTAL_DURATION_OF_SHAKE = 400;

    /** Time when the gesture started. */
    private long mFirstDirectionChangeTime = 0;

    /** Time when the last movement started. */
    private long mLastDirectionChangeTime;

    /** How many movements are considered so far. */
    private int mDirectionChangeCount = 0;

    /** The last x position. */
    private float lastX = 0;

    /** The last y position. */
    private float lastY = 0;

    /** The last z position. */
    private float lastZ = 0;

    /** OnShakeListener that is called when shake is detected. */
    private OnShakeListener mShakeListener;

    /**
     * Interface for shake gesture.
     */
    public interface OnShakeListener {

        /**
         * Called when shake gesture is detected.
         */
        void onShake();
    }

    public void setOnShakeListener(OnShakeListener listener) {
        mShakeListener = listener;
    }

    @Override
    public void onSensorChanged(SensorEvent se) {
        // get sensor data
        float x = se.values[SensorManager.DATA_X];
        float y = se.values[SensorManager.DATA_Y];
        float z = se.values[SensorManager.DATA_Z];

        // calculate movement
        float totalMovement = Math.abs(x + y + z - lastX - lastY - lastZ);

        Log.v("Shake", "Movement : " + totalMovement + " + min Force :" + MIN_FORCE);
        if (totalMovement > MIN_FORCE) {

            // get time
            long now = System.currentTimeMillis();

            // store first movement time
            if (mFirstDirectionChangeTime == 0) {
                mFirstDirectionChangeTime = now;
                mLastDirectionChangeTime = now;
            }

            // check if the last movement was not long ago
            long lastChangeWasAgo = now - mLastDirectionChangeTime;
            Log.v("Shake","LastChangeWasAgo : " + lastChangeWasAgo + " Max pause : " + MAX_PAUSE_BETHWEEN_DIRECTION_CHANGE);
            if (lastChangeWasAgo < MAX_PAUSE_BETHWEEN_DIRECTION_CHANGE) {

                // store movement data
                mLastDirectionChangeTime = now;
                mDirectionChangeCount++;

                // store last sensor data
                lastX = x;
                lastY = y;
                lastZ = z;

                // check how many movements are so far
                Log.v("Shake","Direction Change Count : " + mDirectionChangeCount + " Min Direction Change : " + MIN_DIRECTION_CHANGE);
                if (mDirectionChangeCount >= MIN_DIRECTION_CHANGE) {

                    // check total duration
                    long totalDuration = now - mFirstDirectionChangeTime;
                    Log.v("Shake"," totalDuration : " + totalDuration + " Max Duration Shake : " + MAX_TOTAL_DURATION_OF_SHAKE);
                    if (totalDuration < MAX_TOTAL_DURATION_OF_SHAKE) {
                        Log.v("Speech","Calling on shake to speech");
                        mShakeListener.onShake();
                        resetShakeParameters();
                    }
                }

            } else {
                resetShakeParameters();
            }
        }
    }

    /**
     * Resets the shake parameters to their default values.
     */
    private void resetShakeParameters() {
        mFirstDirectionChangeTime = 0;
        mDirectionChangeCount = 0;
        mLastDirectionChangeTime = 0;
        lastX = 0;
        lastY = 0;
        lastZ = 0;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

}
