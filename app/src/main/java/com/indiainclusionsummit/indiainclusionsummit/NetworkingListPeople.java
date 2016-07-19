package com.indiainclusionsummit.indiainclusionsummit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Created by I055845 on 10/14/2015.
 */
public class NetworkingListPeople extends ActionBarActivity implements
        MyCustomAdapter_NetPeople.customButtonListener {
    private String userId;
    private String userName;
    private String userMob;
    private String userMail;
    private String categId;
    private String beaconId;
    private String categDesc;

    private Context ctx;
    private String peopleUrl ="";
    private String app1Catg = "&categoryId=";
    private String app2User = "&userId=";
    private String app3Loc  = "&beaconId=";

    ArrayList<NetworkingUser> ulist = new ArrayList<NetworkingUser>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.networking_list_people);
        ctx = getApplicationContext();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Drawable d = getResources().getDrawable(R.drawable.iis_background_image_top);
        getSupportActionBar().setBackgroundDrawable(d);

        init();
        // set the title with category name
        TextView tvHeader = (TextView) findViewById(R.id.tv_intCatg_header_netList);
        String curText = (String) tvHeader.getText();
        curText = curText + "  " + categDesc;
        tvHeader.setText(curText);
        get_people();
    }

    private void get_people() {
        String finalUrl = "";
        app1Catg = app1Catg + categId;
        app2User = app2User + userId;
        app3Loc  = app3Loc  + beaconId;
        peopleUrl = AppConstants.baseUrl + "?action=getActivePeopleForCategory";
        finalUrl = peopleUrl + app1Catg + app2User + app3Loc;
        Log.v("Avis", "URL is :" + finalUrl);
        new DownloadTask(ctx).execute(finalUrl);
    }

    private void init() {
        ctx = this;
        Intent callerIntent = getIntent();
        Log.v("Avis", "Loading varaibles");
        Patient userObj = SharedWrapperStatic.getUser(ctx);
        userId    = userObj.getUserId();
        userName  = userObj.getUserName();
        userMob   = userObj.getUserMob();
        userMail  = userObj.getUserEmail();

        categId   = new String(callerIntent.getStringExtra("key_categId"));
        categDesc = new String(callerIntent.getStringExtra("key_categDesc"));
        beaconId = BeaconUtility.getCurrentBeaconId();
    }

    @Override
    public void onButtonClickListener(int position, String valueMobile ,String valueEmail,String custName , int actionCode) {
        Log.v("Avis", "Call with " + valueMobile + " and email " + valueEmail + " for " + custName);
        /*Toast.makeText(NetworkingListPeople.this, "Button click " + custName,
                Toast.LENGTH_SHORT).show();*/

        if (actionCode == 1)
        {
            Log.v("Avis","Call to " + valueMobile);
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + valueMobile));
            startActivity(callIntent);
        }

        else if (actionCode == 2)
        {
            Log.v("Avis", "Email to " + valueEmail);
            Intent gmail = new Intent(Intent.ACTION_VIEW);
            gmail.setClassName("com.google.android.gm","com.google.android.gm.ComposeActivityGmail");
            gmail.putExtra(Intent.EXTRA_EMAIL, new String[] { valueEmail });
            gmail.setData(Uri.parse(valueEmail));
            gmail.putExtra(Intent.EXTRA_SUBJECT,"Hi " + custName + " ! I am reaching you via India Inclusion Summit");
            gmail.setType("plain/text");
            gmail.putExtra(Intent.EXTRA_TEXT, " Hi " + custName );
            startActivity(gmail);
        }
        else if ( actionCode == 3) {
            Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
            intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
            intent.putExtra(ContactsContract.Intents.Insert.NAME, custName);
            intent.putExtra(ContactsContract.Intents.Insert.EMAIL, valueEmail);
            intent.putExtra(ContactsContract.Intents.Insert.PHONE, valueMobile);
            intent.putExtra(ContactsContract.Intents.Insert.NOTES,
                    "Met in India Inclusion Summit");
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_networking_people, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return true;
    }

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
            Log.v("Avis","Read after downloading url");
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
        private Context context;
        private ProgressDialog progressDialog;

        public DownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Finding People...");
            progressDialog.setMessage("Filtering on interest Category " + categDesc + "...Please wait.");
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... urls) {

            try {
                Log.v("Avis","do in background");
                return loadFromNetwork(urls[0]);
            } catch (IOException e) {
                Log.v("Avis", "error recieved");
                return "";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Log.v("Avis","People list recieved");
            final ListView peopleListView = (ListView) findViewById(R.id.lvPeopleList);
            Log.v("Avis","Result is " + result);
            parsejsonToObject(result);

            List<NetworkingUser> listNetUsers = new ArrayList<NetworkingUser>();
            for (int i=0; i<ulist.size(); i++){
                //prepare networking user object data
                NetworkingUser user = new NetworkingUser(ulist.get(i).getUserId(),ulist.get(i).getUserName(), ulist.get(i).getUserMob(), ulist.get(i).getUserMail());
                listNetUsers.add(user);
            }


            MyCustomAdapter_NetPeople adapter;
            adapter = new MyCustomAdapter_NetPeople(ctx,listNetUsers);
            peopleListView.setAdapter(adapter);
            adapter.setCustomButtonListener(NetworkingListPeople.this);

            // give  a message if no user found for that category now.
            if (listNetUsers == null || listNetUsers.size() == 0){
                TextView tvNoMembers = new TextView(ctx);
                View relLayout =  findViewById(R.id.rel_Lay_NetPeopleList);
                tvNoMembers.setText("No members found for the current selection, Please try some other criteria");
                RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                p.addRule(RelativeLayout.BELOW, R.id.tv_intCatg_header_netList);
                tvNoMembers.setLayoutParams(p);
                tvNoMembers.setTextColor(Color.rgb(242, 112,32));
                ((RelativeLayout) relLayout).addView(tvNoMembers);
            }
            progressDialog.dismiss();
        }
    }

    private void parsejsonToObject(String l_json) {
        JSONTokener jTokener = new JSONTokener(l_json);
        int i = 0;
        try{
            JSONObject root = (JSONObject) jTokener.nextValue();
            JSONArray users = root.getJSONArray("UserMaster"); //root
            for (;i<users.length();i++)
            {
                Log.v("Avis", "Parsing object number " + i);
                JSONObject c = (JSONObject)users.get(i);
                parseNetUser(c);
            }

        }
        catch(Exception e)
        {
            //only first tiem user master, after that it will be return code for friend requests. root node name will be DBUpdate
            Log.v("Avis","parse exception");
            e.printStackTrace();
        }
    }

    private void parseNetUser(JSONObject c) {
        try
        {
            NetworkingUser netUserObj = new NetworkingUser(c.getString("id"),c.getString("userName"),c.getString("userMob"),c.getString("userEmail"));
            ulist.add(netUserObj);
        }
        catch(Exception e){
            Log.v("Avis","Parse individual error");
            e.printStackTrace();
        }

    }

}
