package com.indiainclusionsummit.indiainclusionsummit;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
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


public class FeedbackActivity extends ActionBarActivity implements View.OnClickListener {

    private Button btnSubmitFeedback;
    private String userId;
    private String userName;
    private String userEmail;
    private String userMobile;
    private Context context;
    private  SharedPreferences sp;
    private SharedPreferences.Editor editor;
    public final String PREFS_NAME = "IIS_PREFS";
    public final String PREFS_FEED_EVENT   = "IIS_PREFS_EV_FEEDF";
    public final String PREFS_FEED_APP     = "IIS_PREFS_AP_FEEDF";
    public final String PREFS_FEED_REMARKS = "IIS_PREFS_RE_FEED";
    private String feedbackResult = "-1";
    float valeventFeed,valappFeed;
    String remarks;
    RatingBar rbEventFeed , rbAppFeed;
    EditText etRemarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        init();

    }

    private void init() {
        //Intent callerIntent = getIntent();
        Patient userObj = SharedWrapperStatic.getUser(getApplicationContext());

       /* userId     = new String(callerIntent.getStringExtra("key_id"));
        userName   = new String(callerIntent.getStringExtra("key_name"));
        userEmail  = new String(callerIntent.getStringExtra("key_email"));
        userMobile = new String(callerIntent.getStringExtra("key_mobile"));*/
        userId     = userObj.getUserId();
        userEmail  = userObj.getUserEmail();
        userName   = userObj.getUserName();
       // userMobile = userObj.getUserMob();

        btnSubmitFeedback = (Button) findViewById(R.id.btnSubmitFeedback);
        // check for feedback already provided in shared preferences.

        context = this.getApplicationContext();
        sp = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
        float feedEvent = (sp.getFloat(PREFS_FEED_EVENT, 0));
        float feedApp     = (sp.getFloat(PREFS_FEED_APP, 0));
        String feedRemarks = sp.getString(PREFS_FEED_REMARKS,null);

        if (feedEvent !=0 || feedApp != 0 || feedRemarks != null ) {
            //already given feedback so show it now in the screen what the user has given.
            Log.v("Avis","Read from preferences feedback with event as " + feedEvent + " App as " + feedApp + " remarks as " +feedRemarks);
            RatingBar rbEventFeed = (RatingBar) findViewById(R.id.ratingEventFeedback);
            RatingBar rbAppFeed   = (RatingBar) findViewById(R.id.ratingAppFeedback);
            EditText etRemarks    = (EditText) findViewById(R.id.etRemarksFeed);

            rbEventFeed.setRating(feedEvent);
            rbAppFeed.setRating(feedApp);
            etRemarks.setText(feedRemarks);
            etRemarks.setEnabled(false);
            //   rbAppFeed.setEnabled(false);
            //   rbEventFeed.setEnabled(false);
            //  rbAppFeed.setClickable(false);

            btnSubmitFeedback.setVisibility(View.INVISIBLE);
            setTitle("Thanks for your Feedback!");
        }
        else {
            btnSubmitFeedback.setOnClickListener(this);
        }
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnSubmitFeedback:
                Log.v("Avis", "Submitting feedback");
                String feedbackUrl = AppConstants.baseUrl + "?action=submitEventFeedback";
                // String feedbackUrl = "https://iisdemo1i064893trial.hanatrial.ondemand.com/iisdemo1/?action=submitEventFeedback";
                String emailApp  ="&email=";
                String mobApp    = "&phone=";
                String idApp     = "&userId=";
                String appFeed   = "&feedApp=";
                String eventFeed = "&feedEvent=";
                String remarkApp = "&remarks=";

                emailApp = emailApp + userEmail;
                mobApp   = mobApp   + userMobile;
                idApp    = idApp    + userId;

                rbEventFeed = (RatingBar) findViewById(R.id.ratingEventFeedback);
                rbAppFeed   = (RatingBar) findViewById(R.id.ratingAppFeedback);
                etRemarks   = (EditText) findViewById(R.id.etRemarksFeed);
                remarks = etRemarks.getText().toString();
                Log.v("Avis","Remarks : " + remarks);
                remarks = remarks.replaceAll(" ", "%20");
                Log.v("Avis", "Remarks : " + remarks);
                remarks = remarks.replaceAll("\n","%20");
                valeventFeed = rbEventFeed.getRating();
                valappFeed   = rbAppFeed.getRating();

                if (remarks == null && valeventFeed < 1 && valappFeed < 1)
                {
                    // no value entered.
                    Toast.makeText(getApplicationContext(),"No feedback provided", Toast.LENGTH_LONG).show();
                    break;
                }
                appFeed = appFeed + valappFeed;
                eventFeed = eventFeed + valeventFeed;
                remarkApp = remarkApp + remarks;
                feedbackUrl = feedbackUrl + emailApp + mobApp + idApp + appFeed + eventFeed + remarkApp;
                Log.v("Avis",feedbackUrl);
                new DownloadTask().execute(feedbackUrl);


                break;
        }
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
                Log.v("Avis",urls[0]);
                return loadFromNetwork(urls[0]);
            } catch (IOException e) {
                return "";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Log.v("Avis","Response recieved for feedback");
            Log.v("Avis", "result is " + result);
            parsejsonToObject(result);
            if(feedbackResult.equals("1")) {
                Toast.makeText(getApplicationContext(),"Thanks for rating us !", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getApplicationContext(),"Some issue in sharing the feedback, please try again", Toast.LENGTH_LONG).show();
            }

            //store the values in shared preferences
            if (feedbackResult.equals("1")) {
                editor.putFloat(PREFS_FEED_EVENT, valeventFeed);
                editor.putFloat(PREFS_FEED_APP, valappFeed);
                remarks = remarks.replaceAll("%20", " ");
                remarks = remarks.replaceAll("%20", " ");
                editor.putString(PREFS_FEED_REMARKS, remarks);
                Log.v("Avis", "Storing preferences feedback with event as " + valeventFeed + " App as " + valappFeed + " remarks as " + remarks);
                editor.commit();
                btnSubmitFeedback.setVisibility(View.INVISIBLE);
                setTitle("Thanks for your Feedback!");
                etRemarks.setEnabled(false);
                //            rbAppFeed.setEnabled(false);
                //            rbEventFeed.setEnabled(false);
            }
        }

    }

    private void parsejsonToObject(String l_json) {
        JSONTokener jTokener = new JSONTokener(l_json);
        int i = 0;
        try{
            JSONObject root = (JSONObject) jTokener.nextValue();
            JSONArray dbUpdArr = root.getJSONArray("DBUpdate"); //root
            for (;i<dbUpdArr.length();i++)
            {
                Log.v("Avis", "Parsing object number " + i);
                JSONObject c = (JSONObject)dbUpdArr.get(i);
                parseDBUpdate(c);
            }

        }
        catch(Exception e)
        {
            Log.v("Avis","parse exception" + e);
            e.printStackTrace();
        }
    }

    private void parseDBUpdate(JSONObject c) {
        try
        {
            feedbackResult = c.getString("resultCode");
        }
        catch(Exception e){
            Log.v("Avis","Parse individual error");
            e.printStackTrace();
        }

    }
}
