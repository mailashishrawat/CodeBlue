package com.indiainclusionsummit.indiainclusionsummit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by I055845 on 10/9/2015.
 */
public class NavigationFragment extends Fragment implements SensorEventListener {

    RelativeLayout parentLinearLayout;
    public int PointerX_target = 0;
    public int PointerY_target =0;
    public int destination = 0;
    public int current_location = 0;
    public int distance_to_travel = 0;
    public int distance = 0;
    public int Direct_change = 0;
    public static int navigation_diversion;
    public View V_TEMP;
    public TextView tvHeading;
    public String direction_guide_me = null;
    public int top, right, bottom, left;
    private Rect rectangle1,rectangle2, rectangle3 ;
    private static String directionMe = null;
    private static String distanceMe = null;
    private boolean isInternet, isBluetooth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();


        /////////////////////////////VIEW TO BE CREATED/////////////////////////////////
        View view = inflater.inflate(R.layout.navigation, container, false);
        RelativeLayout ll = (RelativeLayout) view.findViewById(R.id.map_layout);
        parentLinearLayout = new RelativeLayout(getContext());
        parentLinearLayout.addView(new MyView(getContext()));
        ll.getLayoutParams().height = 1000;
        ll.getLayoutParams().width = 600;
        ll.addView(parentLinearLayout);
        /////////////////////////////////////////////////////////////////////////////////

        if (!isBluetooth || !isInternet)
            return view;

        V_TEMP = view;

        tvHeading = (TextView) V_TEMP.findViewById(R.id.tvHeading);


        final Button button1 = (Button) view.findViewById(R.id.registerbutton);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                directionMe = null;
                distanceMe = null;

                for( int i=0 ;i<BeaconUtility.Major_HCP.size();i++) {

                    String BEACON_HCP = BeaconUtility.Major_HCP.get(i);
                    Log.v("NetZ","Beacon_hcp :" + BEACON_HCP);


                    String BEACON_X_CORD = BeaconUtility.X_HCP.get(i);
                    String BEACON_Y_CORD = BeaconUtility.Y_HCP.get(i);
                    String BEACON_DISTANCE =BeaconUtility.DISTANCE_HCP.get(i);

                    String NAVIGATION_HCP =NavigationSettingReceiver.NAVIGATION_SETTING_VALUE_HCP.get(1);
                    int B_found = Integer.parseInt(BEACON_HCP.toString());
                    int B_target = Integer.parseInt(NAVIGATION_HCP.toString());
                    if(B_found == B_target){
                        destination = Integer.parseInt(BEACON_DISTANCE.toString());
                        PointerX_target = Integer.parseInt(BEACON_X_CORD.toString());
                        PointerY_target = Integer.parseInt(BEACON_Y_CORD.toString());
                    }

                }

            }
        });
        // added on 18Nov for accessibility.
        button1.setFocusableInTouchMode(true);
        button1.requestFocus();

        final Button button2 = (Button) view.findViewById(R.id.interactionbutton);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                directionMe = null;
                distanceMe = null;

                Log.v("Avis","Clicked on INteraction");
                for( int i=0 ;i<BeaconUtility.Major_HCP.size();i++) {

                    String BEACON_HCP = BeaconUtility.Major_HCP.get(i);
                    String BEACON_X_CORD = BeaconUtility.X_HCP.get(i);
                    String BEACON_Y_CORD = BeaconUtility.Y_HCP.get(i);
                    String BEACON_DISTANCE =BeaconUtility.DISTANCE_HCP.get(i);
                    String NAVIGATION_HCP =NavigationSettingReceiver.NAVIGATION_SETTING_VALUE_HCP.get(2);
                    int B_found = Integer.parseInt(BEACON_HCP.toString());
                    int B_target = Integer.parseInt(NAVIGATION_HCP.toString());
                    if(B_found == B_target){
                        destination = Integer.parseInt(BEACON_DISTANCE.toString());
                        PointerX_target = Integer.parseInt(BEACON_X_CORD.toString());
                        PointerY_target = Integer.parseInt(BEACON_Y_CORD.toString());
                    }

                }

            }
        });

        final Button button3 = (Button) view.findViewById(R.id.btnToiletMale);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                directionMe = null;
                distanceMe = null;
                for( int i=0 ;i<BeaconUtility.Major_HCP.size();i++) {

                    String BEACON_HCP = BeaconUtility.Major_HCP.get(i);
                    String BEACON_X_CORD = BeaconUtility.X_HCP.get(i);
                    String BEACON_Y_CORD = BeaconUtility.Y_HCP.get(i);
                    String BEACON_DISTANCE =BeaconUtility.DISTANCE_HCP.get(i);
                    String NAVIGATION_HCP =NavigationSettingReceiver.NAVIGATION_SETTING_VALUE_HCP.get(3);
                    int B_found = Integer.parseInt(BEACON_HCP.toString());
                    int B_target = Integer.parseInt(NAVIGATION_HCP.toString());
                    if(B_found == B_target){
                        destination = Integer.parseInt(BEACON_DISTANCE.toString());
                        PointerX_target = Integer.parseInt(BEACON_X_CORD.toString());
                        PointerY_target = Integer.parseInt(BEACON_Y_CORD.toString());
                    }

                }

            }
        });

        final Button button4 = (Button) view.findViewById(R.id.btnFemaleToilet);
        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                directionMe = null;
                distanceMe = null;
                for( int i=0 ;i<BeaconUtility.Major_HCP.size();i++) {

                    String BEACON_HCP = BeaconUtility.Major_HCP.get(i);
                    String BEACON_X_CORD = BeaconUtility.X_HCP.get(i);
                    String BEACON_Y_CORD = BeaconUtility.Y_HCP.get(i);
                    String BEACON_DISTANCE =BeaconUtility.DISTANCE_HCP.get(i);
                    String NAVIGATION_HCP =NavigationSettingReceiver.NAVIGATION_SETTING_VALUE_HCP.get(6);
                    int B_found = Integer.parseInt(BEACON_HCP.toString());
                    int B_target = Integer.parseInt(NAVIGATION_HCP.toString());
                    if(B_found == B_target){
                        destination = Integer.parseInt(BEACON_DISTANCE.toString());
                        PointerX_target = Integer.parseInt(BEACON_X_CORD.toString());
                        PointerY_target = Integer.parseInt(BEACON_Y_CORD.toString());
                    }

                }

            }
        });

        final Button button5 = (Button) view.findViewById(R.id.hallbutton);
        button5.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                directionMe = null;
                distanceMe = null;
                for( int i=0 ;i<BeaconUtility.Major_HCP.size();i++) {

                    String BEACON_HCP = BeaconUtility.Major_HCP.get(i);
                    String BEACON_X_CORD = BeaconUtility.X_HCP.get(i);
                    String BEACON_Y_CORD = BeaconUtility.Y_HCP.get(i);
                    String BEACON_DISTANCE =BeaconUtility.DISTANCE_HCP.get(i);
                    String NAVIGATION_HCP =NavigationSettingReceiver.NAVIGATION_SETTING_VALUE_HCP.get(4);
                    int B_found = Integer.parseInt(BEACON_HCP.toString());
                    int B_target = Integer.parseInt(NAVIGATION_HCP.toString());
                    if(B_found == B_target){
                        destination = Integer.parseInt(BEACON_DISTANCE.toString());
                        PointerX_target = Integer.parseInt(BEACON_X_CORD.toString());
                        PointerY_target = Integer.parseInt(BEACON_Y_CORD.toString());
                    }

                }

            }
        });


        return view;

    }


    private void init() {
        isInternet  = Utils.checkForInternetConnection(getContext());
        if (!isInternet)
            Toast.makeText(getContext(),"Issue in connecting to Internet. Kindly check your connection settings", Toast.LENGTH_LONG).show();
        isBluetooth = Utils.checkForBlueToothSupport();
        if (isBluetooth)
            isBluetooth = Utils.checkCurrentBlueToothStatus();
        else
        {
            Toast.makeText(getContext(),"BlueTooth Support Required for Navigation, your device does not support.", Toast.LENGTH_LONG).show();
            return;
        }

        if (!isBluetooth)  // bluetooth is off
        {
            showDialog(getActivity(),"Switch On BlueTooth","App requires Bluetooth to be On to support all features, Do you like to turn it on");
        }
    }

    private void showDialog(Activity activity, String title, CharSequence message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        if (title != null) builder.setTitle(title);

        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                isBluetooth = Utils.turnOnBlueTooth();
                if(!isBluetooth)
                    Toast.makeText(getContext(),"Issue in turning on BlueTooth, kindly turn it on manually",Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("Cancel",  new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(getContext(),"With bluetooth off, app's functionality is limited. Turn Bluetooth on manually when possible.",Toast.LENGTH_LONG).show();
                isBluetooth = false;
            }
        });
        builder.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        // for the system's orientation sensor registered listeners
        BeaconUtility.mSensorManager.registerListener(this, BeaconUtility.mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onPause() {
        super.onPause();
        // to stop the listener and save battery
        BeaconUtility.mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // get the angle around the z-axis rotated
        float degree = Math.round(event.values[0]);
        String direction_status = null;

/***************************************************************************************************************************************START OF LOGIC*****/
        Angular_Adjustment AA = new Angular_Adjustment();
        // for iis we have 82 degree deviation
        float testVar = degree;
        if (testVar > 82)
            testVar = testVar - 82;
        else if (testVar >= 0 && testVar <= 82)
            testVar = testVar - 82 + 360;

        Log.v("NetZ", "AA DEGREE: " + String.valueOf(degree));
        Log.v("NetZ", "AA UP(-1)_DOWN(+1): " + String.valueOf(Direct_change));
        Log.v("NetZ", "AA DIVERT: " + String.valueOf(navigation_diversion));
        AA.direction_finder(degree, Direct_change, navigation_diversion);
        Log.v("Siva", "Diversion as " + navigation_diversion);
        direction_status=Angular_Adjustment.direction_status;
        direction_guide_me=Angular_Adjustment.direction_guide_me;
        Log.v("Siva","Diversion as " + direction_guide_me);
/***************************************************************************************************************************************END OF LOGIC******/
        if(tvHeading != null)
        tvHeading.setText("\r\n" + "\r\n" + "\r\n HEADING: " + Float.toString(testVar) + " degrees" + "\r\n DIRECTION :" + direction_status);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public class MyView extends View {
        public MyView(Context context) {
            super(context);


            // TODO Auto-generated constructor stub
        }
        @Override
        protected void onDraw(Canvas canvas) {
try {
    // TODO Auto-generated method stub
    super.onDraw(canvas);
    int radius = 20;
    Paint paint = new Paint();
    TextView Nav_direction = null, distance_value = null;

    if (V_TEMP != null) {
        Nav_direction = (TextView) V_TEMP.findViewById(R.id.NAV_VALUE);
        distance_value = (TextView) V_TEMP.findViewById(R.id.DISTANCE_VAL);
    }


    current_location = BeaconUtility.GlobalValueFetchBeaconcurrent();

    if (destination != 0 && Nav_direction != null && distance_value != null) {


                /*Nav_direction.setText(direction_guide_me.toString());
                if ( directionMe != direction_guide_me.toString()) {
                Nav_direction.setFocusableInTouchMode(true); // added on 18 Nov
                Nav_direction.requestFocus(); // added on 18 nov
                    directionMe = direction_guide_me.toString();
                 }
                    else {
                Nav_direction.clearFocus();
                }*/
//18 Nov insert
        Nav_direction.setText(direction_guide_me.toString());
        Nav_direction.setFocusableInTouchMode(true);
        distance_value.setFocusableInTouchMode(true);
        if (directionMe != direction_guide_me.toString()) {
            distance_value.requestFocus();
            directionMe = direction_guide_me.toString();
        } else {
            Nav_direction.requestFocus();
        }


        distance_value.setText(String.valueOf(distance * 10) + " METERS");
        distance_to_travel = destination - current_location;

        if (distance_to_travel > 0) {
            distance = distance_to_travel;
            Direct_change = 1;
        }
        if (distance_to_travel == 0) {
            distance = 0;
            Direct_change = 0;
            destination = 0; // added by Sivaram on 13Nov.. this will stop the navigation direction and distance getting update.
            PointerX_target = 0;  // to clear the red dot
            PointerY_target = 0;  // to clear the red dot
            Nav_direction.setText("");
            distance_value.setText("");
            Toast.makeText(getContext(), "Destination Arrived", Toast.LENGTH_LONG).show();
            Log.v("Avis", "Destination Arrived. Resetting navigation");
        }
        if (distance_to_travel < 0) {
            distance = distance_to_travel * (-1);
            Direct_change = -1;
        }

        //added on 18 Nov
        if (distanceMe != String.valueOf(distance)) {
            if (distance != 0) {
                Toast.makeText(getContext(), "You are " + distance * 10 + " meters away from your destination", Toast.LENGTH_LONG).show();
            }
            distanceMe = String.valueOf(distance);
        }


    }

    int shifter = 50;
    if (true) {

        paint.setColor(Color.parseColor("#3f3f3f"));
        // LEFT TOP RIGHT BOTTOM
        top = 300 - shifter;
        left = 300 - shifter; // basically (X1, Y1)
        right = left + 100; // width (distance from X1 to X2)
        bottom = top + 600; // height (distance from Y1 to Y2)
        rectangle1 = new Rect(left, top, right, bottom);
        canvas.drawRect(rectangle1, paint);


        paint.setColor(Color.parseColor("#3f3f3f"));
        // LEFT TOP RIGHT BOTTOM
        top = 300 - shifter;
        left = 100 - shifter; // basically (X1, Y1)
        right = left + 300; // width (distance from X1 to X2)
        bottom = top + 100; // height (distance from Y1 to Y2)
        rectangle2 = new Rect(left, top, right, bottom);
        canvas.drawRect(rectangle2, paint);


        paint.setColor(Color.parseColor("#3f3f3f"));
        // LEFT TOP RIGHT BOTTOM
        top = 100 - shifter;
        left = 100 - shifter; // basically (X1, Y1)
        right = left + 100; // width (distance from X1 to X2)
        bottom = top + 200; // height (distance from Y1 to Y2)
        rectangle3 = new Rect(left, top, right, bottom);
        canvas.drawRect(rectangle3, paint);
    }

    if (true) {
        //REGISTRATION
        paint.setColor(Color.parseColor("#FFFFFF"));
        canvas.drawCircle(110 - shifter, 230 - shifter, radius, paint);
        //INTERACTION
        paint.setColor(Color.parseColor("#FFFFFF"));
        canvas.drawCircle(390 - shifter, 580 - shifter, radius, paint);
        //WASHROOM
        paint.setColor(Color.parseColor("#FFFFFF"));
        canvas.drawCircle(315 - shifter, 720 - shifter, radius, paint);
        //MAIN HALL
        paint.setColor(Color.parseColor("#FFFFFF"));
        canvas.drawCircle(315 - shifter, 860 - shifter, radius, paint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(30);

        canvas.save();
        canvas.rotate(90, 60 - shifter, 120 - shifter);
        canvas.drawText("REGISTRATION", 60 - shifter, 120 - shifter, paint);
        canvas.restore();

        canvas.save();
        canvas.rotate(90, 250 - shifter, 610 - shifter);
        canvas.drawText("WASHROOM", 250 - shifter, 610 - shifter, paint);
        canvas.restore();

        canvas.save();
        canvas.rotate(90, 250 - shifter, 820 - shifter);
        canvas.drawText("HALL", 250 - shifter, 820 - shifter, paint);
        canvas.restore();

        canvas.save();
        canvas.rotate(90, 420 - shifter, 460 - shifter);
        canvas.drawText("INTERACTION", 420 - shifter, 460 - shifter, paint);
        canvas.restore();

    }

    //RED
    paint.setColor(Color.parseColor("#FF0000"));
    canvas.drawCircle(PointerX_target, PointerY_target, radius, paint);


    //GREEN
    paint.setColor(Color.parseColor("#00FF00"));
    canvas.drawCircle(BeaconUtility.PointerX, BeaconUtility.PointerY, radius, paint);
    Log.v("Beacon onDraw  X:", String.valueOf(BeaconUtility.GlobalValueFetchX()));
    Log.v("Beacon onDraw  Y:", String.valueOf(BeaconUtility.GlobalValueFetchY()));

    invalidate();
}catch (Exception ex)
{
    ex.printStackTrace();
}
        }

    }

}
