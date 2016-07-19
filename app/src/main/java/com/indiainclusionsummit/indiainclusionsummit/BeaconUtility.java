package com.indiainclusionsummit.indiainclusionsummit;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by I065310 on 10/20/2015.
 */
public  class BeaconUtility  {


    private Context context;
    private BluetoothManager btManager;
    public BluetoothAdapter btAdapter;
    public Handler scanHandler = new Handler();
    private int scan_interval_ms = 500;
    private boolean isScanning = false;
    public static int PointerX = 0;
    public static int PointerY = 0;
    public static int Beacon_current_location = 0;
    public static String beaconIdext = null;
    public static String beaconIdprev = null;    // Insert on 3-Nov
    public static SensorManager mSensorManager;
   // private String categoryUrl = AppConstants.baseUrl + "?action=getMap";
   // private String categoryUrl = "https://iisdemo1i064893trial.hanatrial.ondemand.com/iisdemo1/?action=getMap";
    private String categoryUrl = "https://iisdemo1a7eac59ae.hana.ondemand.com/iisdemo1/?action=getMap  ";
    private static boolean isInNetworkingZone = false;
    ArrayList<BeaconData> clist = new ArrayList<BeaconData>();
    public List<String> listBeaconMajor = new ArrayList<String>();
    public List<String> listBeaconXcord = new ArrayList<String>();
    public List<String> listBeaconYcord = new ArrayList<String>();
    public List<String> listBeaconContext = new ArrayList<String>();
    public List<String> listBeaconDistance = new ArrayList<String>();
    public List<String> listDiversion = new ArrayList<String>();
    public static List<String> Major_HCP = new ArrayList<String>();
    public static List<String> X_HCP = new ArrayList<String>();
    public static List<String> Y_HCP = new ArrayList<String>();
    public static List<String> DISTANCE_HCP = new ArrayList<String>();
    public static String locationVoice = "";



    ////////////////////////////////CONSTRUCTOR TO GET CONTEXT/////////////////////////
    public BeaconUtility(Context c) {
        this.context = c;
    }
    ///////////////////////////////////////////////////////////////////////////////////

    public void load_categories(){
        // load the beacon master data list here.
        new DownloadTask().execute(categoryUrl);
    }



        private String downloadUrl(String urlString) throws IOException, URISyntaxException {
            //  urlString="https://sliblra7eac59ae.hana.ondemand.com/I315050/sliblr/MyPackage/services.xsodata/Patient?filter="+'"'+"emailid eq 'patient1@gmail.com'"+'"'+"&$format=json";
            URL urlToEncode = new URL(urlString);
            URI uri = new URI(urlToEncode.getProtocol(), urlToEncode.getUserInfo(), urlToEncode.getHost(), urlToEncode.getPort(), urlToEncode.getPath(), urlToEncode.getQuery(), urlToEncode.getRef());

            HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
            conn.setReadTimeout(60000 /* milliseconds */);
            conn.setConnectTimeout(60000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("accept", "application/json");
            conn.setDoInput(true);
            // Start the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("APP", "The response is: " + response);
            InputStream stream = conn.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    stream));
            String inputLine,fullstring="";

            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
                fullstring = fullstring + inputLine;
            }
            in.close();
            return  fullstring;
        };


  /*  private String loadFromNetwork(String urlString) throws IOException {
        InputStream stream = null;
        String str ="";
        try {
            stream = downloadUrl(urlString);
            str = readIt(stream, 5000000);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        return str;
    };
*/
    private String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    };



    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "";
            } catch (URISyntaxException e) {
                e.printStackTrace();
                return "";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Log.v("Avis", "Response recieved for beacon master data AreaMap");
            Log.v("Avis", "result is " + result);
            parsejsonToObject(result);

            for (int i = 0; i < clist.size(); i++) {

                String categDesc_Major = clist.get(i).getbeaconId();
                listBeaconMajor.add(categDesc_Major);
                Major_HCP.add(categDesc_Major);

                String categDesc_Xcord = clist.get(i).getxcord();
                listBeaconXcord.add(categDesc_Xcord);
                X_HCP.add(categDesc_Xcord);

                String categDesc_Ycord = clist.get(i).getycord();
                listBeaconYcord.add(categDesc_Ycord);
                Y_HCP.add(categDesc_Ycord);

                String categDesc_Context = clist.get(i).getlocName();
                listBeaconContext.add(categDesc_Context);

                String categDesc_Distance = clist.get(i).getextra1();
                listBeaconDistance.add(categDesc_Distance);
                DISTANCE_HCP.add(categDesc_Distance);

                String categDesc_Diversion = clist.get(i).getextra2();
                listDiversion.add(categDesc_Diversion);
                int testing = Integer.parseInt(categDesc_Diversion);
                NavigationFragment.navigation_diversion = testing;
//                Log.v("Avis","FINAL DIVERT : " + String.valueOf(testing));

            }
        }

    }

    private void parsejsonToObject(String l_json) {
        JSONTokener jTokener = new JSONTokener(l_json);
        int i = 0;
        try{
            JSONObject root = (JSONObject) jTokener.nextValue();
            JSONArray categories = root.getJSONArray("AreaMap"); //root
            for (;i<categories.length();i++)
            {
                Log.v("AreaMap", "Parsing object number " + i);
                JSONObject c = (JSONObject)categories.get(i);
                Log.v("AreaMap", "Parsing object Data " + categories.get(i).toString());
                parseCategory(c);
            }

        }
        catch(Exception e)
        {
            Log.v("AreaMap","parse exception");
            e.printStackTrace();
        }
    }

    private void parseCategory(JSONObject c) {
        try
        {
            BeaconData categObj = new BeaconData(   c.getString("id"),
                    c.getString("beaconId"),
                    c.getString("locName"),
                    c.getString("xcord"),
                    c.getString("ycord"),
                    c.getString("extra1"),
                    c.getString("extra2"),
                    c.getString("extra3"),
                    c.getString("extra4"));
            clist.add(categObj);
        }
        catch(Exception e){
            Log.v("Avis"," AreaMap Parse individual error");
            e.printStackTrace();
        }
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public  void callBluetoothServices_IIS(){
        btManager = (BluetoothManager)this.context.getSystemService(Context.BLUETOOTH_SERVICE);
        btAdapter = btManager.getAdapter();
        scanHandler.post(scanRunnable);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void callCompassServices_IIS(){
        mSensorManager = (SensorManager) this.context.getSystemService(Context.SENSOR_SERVICE);
    }

    /*
        This is the callback in the runnable which is called in callBluetoothServices_IIS.
        This is called in a background thread continuously and it scans for the beacon and sets in the variable major and minor.
        After this, we calculate the distance between that beacon to mobile and then check if it is in permissible limits ( configured in NSR setting ID 6 )
        If the distance is less than or equal to the permissible range , then we are near to that beacon and then we set the variables PointerX, PointerY,BeaconCurrentLocation &
        Navigation Diversion attribute in Navigation Fragment.

        Catch in the logic as of now :
        Assume permissible configured limit was 10mtrs and scan detects 2 beacons B1 and B2.
        In the scanner we calculate distance now from B1 to mobile say 5m.
                           -> Ok for permissible distance, set the location to B1.
                           -> We break the loop and hence never calculate distance of B2 to mobile, it may be 2 mtrs.
                              In this case we are near to B2 than B1, but app will show as B1 only.
         Possible Fix : Do not exit out of the loop immediately, among all the beacons detected find out the least distance and use it.

     */
    protected BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback()
    {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord)
        {
            int startByte = 2;
            boolean patternFound = false;
            while (startByte <= 5)
            {
                if (    ((int) scanRecord[startByte + 2] & 0xff) == 0x02 && //Identifies an iBeacon
                        ((int) scanRecord[startByte + 3] & 0xff) == 0x15)
                { //Identifies correct data length
                    patternFound = true;
                    break;
                }
                startByte++;
            }

            if (patternFound)
            {
                //Convert to hex String
                byte[] uuidBytes = new byte[16];

                System.arraycopy(scanRecord, startByte + 4, uuidBytes, 0, 16);
                String hexString = bytesToHex(uuidBytes);
                //UUID detection
                String uuid =  hexString.substring(0,8) + "-" +
                        hexString.substring(8,12) + "-" +
                        hexString.substring(12,16) + "-" +
                        hexString.substring(16,20) + "-" +
                        hexString.substring(20,32);

                // major
                final int major = (scanRecord[startByte + 20] & 0xff) * 0x100 + (scanRecord[startByte + 21] & 0xff);
                final int minor = (scanRecord[startByte + 22] & 0xff) * 0x100 + (scanRecord[startByte + 23] & 0xff);
                double distance = 0;
                if(Integer.toString(major) != null){
                    byte txpower = (scanRecord[29]);
                    distance = calculateDistance(txpower, rssi);
                }

                //////////////////////////////////////////////////LOGIC////////////////////////////////////////////////

                int Beacon_range_Setting = 1;
                int dupRangeSetting = 1;
      //          Log.v("DUMP","Navigation Setting list size is " + NavigationSettingReceiver.NavigationSettingList.size());

                if ( NavigationSettingReceiver.NavigationSettingList != null && NavigationSettingReceiver.NavigationSettingList.size() > 0)
                    for ( int i = 0; i < NavigationSettingReceiver.NavigationSettingList.size(); i++)
                    {
              //          Log.v("DUMP","Navigation Setting list parsting ID number  " + NavigationSettingReceiver.NavigationSettingList.get(i).getSettingId());
                        if ( NavigationSettingReceiver.NavigationSettingList.get(i).getSettingId()=="6" || NavigationSettingReceiver.NavigationSettingList.get(i).getSettingId().equalsIgnoreCase("6")) {
                            // setting Id 6 should be for Beacon_range_Scanner should always be in id 6.
              //              Log.v("DUMP","Navigation Setting list ID 6, Distance value is " + NavigationSettingReceiver.NavigationSettingList.get(i).getSettingValue());
                            Beacon_range_Setting = Integer.parseInt(NavigationSettingReceiver.NavigationSettingList.get(i).getSettingValue());
              //              Log.v("DistanceAvis","Setting value for first time : " + Beacon_range_Setting);
                            dupRangeSetting = Beacon_range_Setting;
                        }
                    }
                //   Integer.parseInt(NavigationSettingReceiver.NAVIGATION_SETTING_VALUE_HCP.get(5));
                // Beacon_range_Setting variable will give the permissible value for distance from a beacon.
            //    Log.v("DistanceAvis","Beacon Rnage Setting : "  + Beacon_range_Setting + " Distance found : " + distance );
                // variable Major has the detected beacon's major, and so does minor. Distance is distance from that beacon.
                for( int i=0 ;i<listBeaconMajor.size();i++)
                {
                    String BEACON_HCP = listBeaconMajor.get(i);
                    String BEACON_X_CORD = listBeaconXcord.get(i);
                    String BEACON_Y_CORD = listBeaconYcord.get(i);
                    String BEACON_DIVERSION = listDiversion.get(i);
                    String BEACON_CUMULATIVE_DISTANCE = listBeaconDistance.get(i);
                    Log.v("Siva","BeaconHCP = " + listBeaconMajor.get(i) + " diver is " + listDiversion.get(i));
                    if (major ==Integer.parseInt(BEACON_HCP.toString()))
                    {
                        Beacon_range_Setting = dupRangeSetting;

          //              Log.v("DistanceAvis","Comparing Beacon Rnage Setting : "  + Beacon_range_Setting + " Distance found : " + distance );
                        if (distance <= Beacon_range_Setting)
                        {
                            PointerX = Integer.parseInt(BEACON_X_CORD.toString());
                            PointerY = Integer.parseInt(BEACON_Y_CORD.toString());
                            Beacon_current_location = Integer.parseInt(BEACON_CUMULATIVE_DISTANCE);
                            beaconIdext = BEACON_HCP; // added by sivaram on 2-nov
                     //       Log.v("ZAvis","Context is " + listBeaconContext.get(i));
                            locationVoice = listBeaconContext.get(i);
                            if(listBeaconContext.get(i).contains("NETWORK") || listBeaconContext.get(i).contains("INTERACT")) {
                       //         Log.v("ZAvis","Setting true interaction");
                                isInNetworkingZone = true; }
                            else {
                                isInNetworkingZone = false;
                                Log.v("ZAvis","Setting false interaction");
                            }
                            NavigationFragment.navigation_diversion = Integer.parseInt(BEACON_DIVERSION);
                                Log.v("Siva","BeaconId ext = " + beaconIdext + " diver is " + BEACON_DIVERSION);
                            if(Integer.parseInt(BEACON_DIVERSION) != 0)
                            {
                                Log.v("Siva", "AA_DIVERSION_FOUND : " + BEACON_DIVERSION);
                            }


                            // fire URL to update user location with beaconId with beaconIdExt
                            //String updateLocUrl = AppConstants.baseUrl + "?action=";

                            // add code to send his location to the server 03_Nov
// String updateLocUrl   = AppConstants.baseUrl + "?action=updateUserLocation";
                            if (beaconIdprev != beaconIdext) {
                                String updateLocUrl = AppConstants.baseUrl + "?action=updateUserLocation";
                                String updateLocApp1  = "&beaconId=" + beaconIdext;
                                String updateUserApp2 = "&userId=" + SharedWrapperStatic.getUser(context).getUserId();

                                updateLocUrl = updateLocUrl + updateLocApp1 + updateUserApp2;
                                Log.v("Avis","Update location for " + updateLocUrl);
                                new UploadTask().execute(updateLocUrl);
                                beaconIdprev = beaconIdext;
                            }

                            break;
                        }
                    }

                }
                ////////////////////////////////////////////LOGIC END////////////////////////////////////////////////////
            }
        }
    };

    private double calculateDistance(int txPower, double rssi) {
        if (rssi == 0) {
            return -1.0; // if we cannot determine accuracy, return -1.
        }

        double ratio = rssi*1.0/txPower;
        if (ratio < 1.0) {
            return Math.pow(ratio,10);
        }
        else {
            double accuracy =  (0.89976)*Math.pow(ratio,7.7095) + 0.111;
            return accuracy;
        }
    };

    final char[] hexArray = "0123456789ABCDEF".toCharArray();
    private String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }



    private Runnable scanRunnable = new Runnable()
    {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void run() {
            if (isScanning)
            {
                if (btAdapter != null)
                {
                    btAdapter.stopLeScan(leScanCallback);
                }
            }
            else
            {
                if (btAdapter != null)
                {
                    btAdapter.startLeScan(leScanCallback);
                }
            }
            isScanning = !isScanning;
            scanHandler.postDelayed(this, scan_interval_ms);
        }
    };


/*
    THESE FUNCTIONS HELP TO GET :
    1. GlobalValueFetchBeaconcurrent : To get the current Beacon user is near to
    2.GlobalValueFetchX              : To get the X co-ordinate of Beacon identified where user is standing
    3.GlobalValueFetchY              : To get the Y co-ordinate of Beacon identified where user is standing
     */

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static int GlobalValueFetchBeaconcurrent(){
        return Beacon_current_location;
    }

    public static int GlobalValueFetchX(){
        return PointerX;
    }

    public static int GlobalValueFetchY(){
        return PointerY;
    }

    public static String getCurrentBeaconId() {
        return beaconIdext;
    }

    public static boolean checkInNetworkingZone() {
     //   Log.v("Avis","Check in zone" + isInNetworkingZone);
        return isInNetworkingZone;
    }

    //03-Nov insert for uploading location to server.
    private class UploadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "";
            } catch (URISyntaxException e) {
                e.printStackTrace();
                return "";
            }
        } // do in background end

        @Override
        protected void onPostExecute(String result) {
            Log.v("Avis", "Response recieved for Area Map Update");
            Log.v("Avis", "result is " + result);
        } // on Post execute end
    } // uploadtask end


 //04-nov insert
    public static String getLocationVoice() {
        return locationVoice;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////


}

