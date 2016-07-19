package com.indiainclusionsummit.indiainclusionsummit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by I055845 on 10/9/2015.
 */
public class NetworkingFragment extends Fragment{

    private String userId;
    private String userMob;
    private String userName;
    private String userMail;
    private Context ctx;
    private String categoryUrl;
    ArrayList<NetworkCategories> clist = new ArrayList<NetworkCategories>();
    private ListView categoryListView;
    public  static boolean isInternet = false;
    public  static boolean isBluetooth = false;
    private boolean excOccur = false;
    private View myView;
    private TimerTask doAsynchronousTask;
    private int noMemId = -99;
    private TextView tvIntCatgHeader;

    @Override
    public void onResume() {
        Log.v("Avis","On reumse of netowrking");
        super.onResume();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      /*  FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();*/
        View view = inflater.inflate(R.layout.networking, container, false);
        categoryListView = (ListView)view.findViewById(R.id.lvCategList);
        myView = view;
        ctx = getContext();
        init();
        load_categories();
       /* if(isInternet && isBluetooth)
            load_categories();
        else {
            // add a text view that internet connection has to be on for app to work.
            TextView tvNoMembers = new TextView(ctx);
            View relLayout =  (RelativeLayout) view.findViewById(R.id.relLay_NetHome);
            tvNoMembers.setText("Turn on Bluetooth and Internet to use this feature");
            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            p.addRule(RelativeLayout.BELOW, R.id.tvIntCatgHeader);
            tvNoMembers.setLayoutParams(p);
            tvNoMembers.setTextColor(Color.rgb(242, 112, 32));
            ((RelativeLayout) relLayout).addView(tvNoMembers);
        }*/

        //added on 18 Nov for accessibility changes.
        tvIntCatgHeader = (TextView) view.findViewById(R.id.tvIntCatgHeader);
        tvIntCatgHeader.setFocusableInTouchMode(true);
        tvIntCatgHeader.requestFocus();

        if(!isInternet)
            callAsynchronousTask();

        return view;
    }

    private void callAsynchronousTask() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {

                            Log.v("Avis","Checking for internet again");
                            if (!isInternet) {
                                isInternet = Utils.checkForInternetConnection(ctx);
                            }

                            if (isInternet) {
                                categoryUrl = AppConstants.baseUrl + "?action=ShowCategoriesForEvent";
                                new DownloadTask().execute(categoryUrl);
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 15000); //execute in every 15 seconds
    }

    private void init() {
        //initialize variables which was sent from previous intent here.
        Log.v("Avis", "Networking home invoked");

        Patient userObj = SharedWrapperStatic.getUser(ctx);
        userId   = userObj.getUserId();
       // userMob  = userObj.getUserMob();
        userName = userObj.getUserName();
        userMail = userObj.getUserEmail();

        //check for internet connection
        isInternet = Utils.checkForInternetConnection(getContext());
        if (!isInternet) {
            Log.v("Avis", "Adding text for no internet");
            Toast.makeText(getContext(),"Issue in connecting to Internet. Kindly check your connection settings", Toast.LENGTH_LONG).show();
            TextView tvNoMembers = new TextView(ctx);
            tvNoMembers.setId(R.id.reservedNetId);
            View relLayout =  (RelativeLayout) myView.findViewById(R.id.relLay_NetHome);
            tvNoMembers.setText("Turn on Bluetooth and Internet to use this feature and reopen the application.");
            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            p.addRule(RelativeLayout.BELOW, R.id.tvIntCatgHeader);
            tvNoMembers.setLayoutParams(p);
            tvNoMembers.setTextColor(Color.rgb(242, 112, 32));
            ((RelativeLayout) relLayout).addView(tvNoMembers);
        }

        // check for bluetooth
        isBluetooth = Utils.checkForBlueToothSupport();
        if (!isBluetooth) {
            Toast.makeText(getContext(),"Bluetooth support required for Networking, not supported in this device", Toast.LENGTH_LONG).show();
            return; }

        isBluetooth = Utils.checkCurrentBlueToothStatus();
        if (!isBluetooth) {
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


    private void load_categories(){
        // load the category list here.
        categoryUrl = AppConstants.baseUrl + "?action=ShowCategoriesForEvent";
        Log.v("Avis","Going to load network Categories with" + categoryUrl);
        new DownloadTask().execute(categoryUrl);
    }

    // for fetching from server
    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Start the query
        conn.connect();
        InputStream stream = conn.getInputStream();
        return stream;
    };

    /** Initiates the fetch operation. */
    private String loadFromNetwork(String urlString) throws IOException {
        InputStream stream = null;
        String str ="";
        try {
            stream = downloadUrl(urlString);
            str = readIt(stream, 50000);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        return str;
    };

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
                return loadFromNetwork(urls[0]);
            } catch (IOException e) {
                return "";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Log.v("Avis","Response recieved for netwrking catgs");
            Log.v("Avis","result is " + result);
            parsejsonToObject(result);

            List<String> listCategoryName = new ArrayList<String>();
            for (int i=0; i<clist.size(); i++){
                String categDesc = clist.get(i).getCategoryDesc();
                listCategoryName.add(categDesc);
                if (i==0 && doAsynchronousTask != null) {
                    Log.v("Avis","Cancelling thread");
                    doAsynchronousTask.cancel();
                    TextView tvNoMembers =  (TextView) myView.findViewById(R.id.reservedNetId);
                    if (tvNoMembers != null) {
                        tvNoMembers.setVisibility(View.INVISIBLE);
                    tvNoMembers.setVisibility(View.GONE); }
                }
            }

            String[] categArray = listCategoryName.toArray(new String[listCategoryName.size()]);
            ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_list_item_1, android.R.id.text1, categArray );
            categoryListView.setAdapter(courseAdapter);
            categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // ListView Clicked item index
                    int itemPosition = position;
                    // ListView Clicked item value
                    String itemValue = (String) categoryListView.getItemAtPosition(position);

/*                    // Show Alert
                    Toast.makeText(getContext(),
                            "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                            .show();*/
                    String categoryId = clist.get(position).getCategoryId();
                    String categoryDesc = clist.get(position).getCategoryDesc();

                    // check for internet and bluetooth here
                    isInternet  = Utils.checkForInternetConnection(ctx);
                    isBluetooth = Utils.checkCurrentBlueToothStatus();
                    if (!isInternet || !isBluetooth) {
                        Toast.makeText(ctx,"Turn on Bluetooth and Internet to use this feature",Toast.LENGTH_LONG).show();
                        return;
                    }

                    // check for his location whether in networking zone.
                    boolean isInNetZone = BeaconUtility.checkInNetworkingZone();
                    Log.v("ZAvis","Check for zone resulted in " + isInNetZone);
                    if (!isInNetZone) {
                        Toast.makeText(ctx,"You are not in Networking Zone, kindly go to Interaction Zone and use this", Toast.LENGTH_LONG).show();
                        return;
                    }

                    //fire intent to next activity.
                    Intent in = new Intent(ctx, com.indiainclusionsummit.indiainclusionsummit.NetworkingListPeople.class);
                    in.addCategory(Intent.ACTION_DEFAULT);
                    in.putExtra("key_categId", categoryId);
                    in.putExtra("key_categDesc", categoryDesc);
                    Log.v("Avis", "Firing intent to List People Activity for id " + categoryId + " desc : " + categoryDesc);
                    startActivity(in);
                }

            });

        }

    }

    private void parsejsonToObject(String l_json) {
        JSONTokener jTokener = new JSONTokener(l_json);
        excOccur = false;
        int i = 0;
        try{
            JSONObject root = (JSONObject) jTokener.nextValue();
            JSONArray categories = root.getJSONArray("CategoriesMaster"); //root
            for (;i<categories.length();i++)
            {
                Log.v("Avis", "Parsing object number " + i);
                JSONObject c = (JSONObject)categories.get(i);
                parseCategory(c);
            }

        }
        catch(Exception e)
        {
            Log.v("Avis","parse exception");
            excOccur = true;
            e.printStackTrace();
        }
    }

    private void parseCategory(JSONObject c) {
        try
        {
            NetworkCategories categObj = new NetworkCategories(c.getString("categoryId"), c.getString("categoryDesc"));
            clist.add(categObj);
        }
        catch(Exception e){
            Log.v("Avis","Parse individual error");
            excOccur = true;
            e.printStackTrace();
        }

    }

}
