package com.indiainclusionsummit.indiainclusionsummit;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by I055845 on 10/9/2015.
 */
public class DetailsActivity extends ActionBarActivity implements android.support.v7.app.ActionBar.TabListener{

    private ViewPager detailspager;
    private Tabsadapter mTabsAdapter;
    private BeaconUtility bu;
    private NavigationSettingReceiver nsr;
    TabHost tabHost;


    //SIVARAM 28 OCT
    private String userId;
    private String userName;
    private String userEmail;
    private String userMob;

    // internet and bluetooth connection variables.
    private boolean isConnected = false, isBlueTooth = false;

    //04-Nov code
    private SensorManager mSensorManager;
    private ShakeEventManager mSensorListener;
    private MySpeaker speaker;
    private final int CHECK_CODE = 0x1;
    private Context ctx;


    private void checkTTS(){
        Intent check = new Intent();
        check.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(check, CHECK_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CHECK_CODE){
            if(resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS){
                Log.v("Speech","Speech engine okay");
                speaker = new MySpeaker(this);
            }else {
                Log.v("Speech","No speech Engine");
                Intent install = new Intent();
                install.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(install);
            }
        }
    }

    /* end of4-nov */

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_main);
        init();

/* 4-Nov Begin for Sensor Shake   */
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeEventManager();

        mSensorListener.setOnShakeListener(  new ShakeEventManager.OnShakeListener()
        {
            public void onShake()
            {
                 if (speaker != null) {
                        speaker.allow(true);
                        String location = BeaconUtility.getLocationVoice();
                        if (location != "" && location != " ")
                            speaker.speak("You are near " + location);
                        else
                            speaker.speak("No nearby landmark found. Please move a bit and check again");
                 }
            }
        });



/* End of 4-Nov for Sensor Shake */

/*
Begin for Beacon related activities.
*******************************************
*/

        bu  = new BeaconUtility(getApplicationContext());
        nsr = new NavigationSettingReceiver(getApplicationContext());

        // load the beacon settings ( AreaMap Beacon Master Data) and configurations from the server.
        bu.load_categories();
        nsr.load_categories();

        // initialize your android device COMPASS sensor capabilities
        bu.callCompassServices_IIS();

        // initialize your android device BLE capabilities and start scanning for bluetooth
        bu.callBluetoothServices_IIS();
/*
END
*******************************************
*/

        detailspager = (ViewPager) findViewById(R.id.detailspager);
        mTabsAdapter = new Tabsadapter(getSupportFragmentManager());

        detailspager.setAdapter(mTabsAdapter);

        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

       // ActionBar.Tab homeWebTab = getSupportActionBar().newTab().setText("Home").setTabListener(this);
       // ActionBar.Tab commentaryTab = getSupportActionBar().newTab().setText("Commentary").setTabListener(this);
     //   ActionBar.Tab publicprofiletab = getSupportActionBar().newTab().setText("My Stats").setTabListener(this);
        ActionBar.Tab patienttab=getSupportActionBar().newTab().setText("Patient").setTabListener(this);
        ActionBar.Tab communitytab = getSupportActionBar().newTab().setText("Navigation").setTabListener(this);

     //   getSupportActionBar().addTab(homeWebTab);
      //  getSupportActionBar().addTab(commentaryTab);
     //   getSupportActionBar().addTab(publicprofiletab);
        getSupportActionBar().addTab(patienttab);
        getSupportActionBar().addTab(communitytab);

        /*Drawable d = getResources().getDrawable(R.drawable.iis_background_image);
        getSupportActionBar().setBackgroundDrawable(d);*/

        //This helps in providing swiping effect for v7 compat library
        detailspager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                getSupportActionBar().setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    @Override
    public void onTabReselected(ActionBar.Tab arg0, FragmentTransaction arg1) {
        // TODO Auto-generated method stub
        String abc = "Hello";
    }

    @Override
    public void onTabSelected(ActionBar.Tab selectedtab, FragmentTransaction arg1) {
        // TODO Auto-generated method stub
        getSupportActionBar().setSelectedNavigationItem(selectedtab.getPosition());
        String abc = "Hello";
        detailspager.setCurrentItem(selectedtab.getPosition()); //update tab position on tap
        if (selectedtab.getPosition() == 1) {
            Log.v("Avis","Tab Selected Networking with " + selectedtab.getTag());
            if (Utils.checkCurrentBlueToothStatus() && Utils.checkForInternetConnection(ctx)) {
                NetworkingFragment.isInternet = true;
                NetworkingFragment.isBluetooth = true;
            }

        }

    }

    @Override
    public void onTabUnselected(ActionBar.Tab arg0, FragmentTransaction arg1) {
        // TODO Auto-generated method stub
        String abc = "Hello";

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void AskMeActivity(MenuItem item) {
        FragmentManager manager=getFragmentManager();
        AskMeDialog myDialog = new AskMeDialog();
        myDialog.show(manager, "MyDialog");

        if(item.getItemId()==R.id.queryBtn)
        {
            dismissDialog(0);
        }
    }

    public void AboutUsActivity(MenuItem item) {
        Intent in = new Intent(this, com.indiainclusionsummit.indiainclusionsummit.AboutUsActivity.class);
        in.addCategory(Intent.ACTION_DEFAULT);
        startActivity(in);
    }

    public void onBackPressed(){

    }

    private void init()
    {
        ctx = getApplicationContext();
        Patient loginObj = SharedWrapperStatic.getUser(getApplicationContext());

        userId   = loginObj.getUserId();
        userName = loginObj.getUserName();
        userEmail = loginObj.getUserEmail();

        Log.v("Avis","Read from preference with static method " + userId + "-" + userName + "-" + userEmail + "-" + userMob);

        // check for internet Connection here as well.
        isConnected = Utils.checkForInternetConnection(getApplicationContext());
        if(!isConnected)
            Toast.makeText(getApplicationContext(),"Issue in internet connection, App may not work properly",Toast.LENGTH_LONG).show();

        // check for bluetooth on the day of event alone
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Log.v("Avis","Current date is " + date);
        // event date hardcoded as of now

        Log.v("Avis","Checking for bluetooth");
        isBlueTooth = Utils.checkForBlueToothSupport();
        if (!isBlueTooth) {
            Toast.makeText(getApplicationContext(),"Device does not support bluetooth,app may not work fully", Toast.LENGTH_LONG).show();
            return;}

        // device supports bluetooth for sure
        isBlueTooth = Utils.checkCurrentBlueToothStatus();
        if(!isBlueTooth){
            //bluetooth is off, ask the user to switch on
            showDialog(this,"Switch on BlueTooth","App requires Bluetooth to be On to support all features, Do you like to turn it on");

        }

        // check for speech engine
        checkTTS();
    }

    public void showDialog(Activity activity, String title, CharSequence message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        if (title != null) builder.setTitle(title);

        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                isBlueTooth = Utils.turnOnBlueTooth();
                if(!isBlueTooth)
                    Toast.makeText(getApplicationContext(),"Issue in turning on BlueTooth, kindly turn it on manually",Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("Cancel",  new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(getApplicationContext(),"With bluetooth off, app's functionality is limited. Turn Bluetooth on manually when possible.",Toast.LENGTH_LONG).show();
                isBlueTooth = false;
            }
        });
        builder.show();
    }


    public void FeedbackActivity(MenuItem item) {
        Intent in = new Intent(this, com.indiainclusionsummit.indiainclusionsummit.FeedbackActivity.class);
        in.addCategory(Intent.ACTION_DEFAULT);
        in.putExtra("key_id",userId);
        in.putExtra("key_email",userEmail);
        in.putExtra("key_mobile",userMob);
        in.putExtra("key_name", userName);
        startActivity(in);
    }


}
