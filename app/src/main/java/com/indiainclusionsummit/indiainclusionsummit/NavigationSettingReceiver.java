package com.indiainclusionsummit.indiainclusionsummit;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by I065310 on 10/28/2015.
 */
public class NavigationSettingReceiver {
    private Context context;
    ///////////////////////////////////HCP SERVICES////////////////////////////////////
    //private Context ctx;
    //private String categoryUrl = AppConstants.baseUrl + "?action=getCustomizingArea";
    private String categoryUrl = "https://iisdemo1a7eac59ae.hana.ondemand.com/iisdemo1/?action=getCustomizingArea";
    //private String categoryUrl = "https://iisdemo1i064893trial.hanatrial.ondemand.com/iisdemo1/?action=getCustomizingArea";
    public static ArrayList<NavigationSettingData> NavigationSettingList = new ArrayList<NavigationSettingData>();
    public List<String> listSettingId = new ArrayList<String>();
    public List<String> listSettingName = new ArrayList<String>();
    public List<String> listSettingValue = new ArrayList<String>();
    public static List<String> NAVIGATION_SETTING_VALUE_HCP = new ArrayList<String>();


    /////////////////////////////////////////////////////////////////////////////////

    public NavigationSettingReceiver(Context c) {
        this.context = c;
    }

    public void load_categories(){
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
            Log.v("Avis", "Response received for NSR");
            Log.v("Avis", "result is " + result);
            parsejsonToObject(result);

            for (int i = 0; i < NavigationSettingList.size(); i++) {

                String categDesc_SettingId = NavigationSettingList.get(i).getSettingId();
                listSettingId.add(categDesc_SettingId);

                String categDesc_SettingName = NavigationSettingList.get(i).getSettingName();
                listSettingName.add(categDesc_SettingName);

                String categDesc_SettingValue = NavigationSettingList.get(i).getSettingValue();
                listSettingValue.add(categDesc_SettingValue);
                NAVIGATION_SETTING_VALUE_HCP.add(categDesc_SettingValue);
            }
        }

    }

    private void parsejsonToObject(String l_json) {
        JSONTokener jTokener = new JSONTokener(l_json);
        int i = 0;
        try{
            JSONObject root = (JSONObject) jTokener.nextValue();
            JSONArray categories = root.getJSONArray("NavigationCustomizing"); //root
            for (;i<categories.length();i++)
            {
                JSONObject c = (JSONObject)categories.get(i);
                parseCategory(c);
            }

        }
        catch(Exception e)
        {

            e.printStackTrace();
        }
    }

    private void parseCategory(JSONObject c) {
        try
        {
            NavigationSettingData categObj = new NavigationSettingData(
                    c.getString("id"),
                    c.getString("settingId"),
                    c.getString("settingName"),
                    c.getString("settingValue"),
                    c.getString("extra1"),
                    c.getString("extra2"),
                    c.getString("extra3"),
                    c.getString("extra4"));

            NavigationSettingList.add(categObj);
        }
        catch(Exception e){
            Log.v("Avis","Parse individual error NSR");
            e.printStackTrace();
        }

    }

}
