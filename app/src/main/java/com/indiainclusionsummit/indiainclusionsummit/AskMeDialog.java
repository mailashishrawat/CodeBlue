package com.indiainclusionsummit.indiainclusionsummit;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by I055845 on 10/9/2015.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class AskMeDialog extends DialogFragment implements View.OnClickListener {

    Button queryBtn;
    EditText etQueryText;
    ImageView ibQueryRecord;

    private String userName;
    private String userId;
    private String userMail;
    private String userMobile;
    private DialogFragment df;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ask_me_popup,null);
        init();
        queryBtn = (Button)view.findViewById(R.id.queryBtn);
        etQueryText = (EditText)view.findViewById(R.id.queryText);
        queryBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                fireRequest();
            }
        });
        ibQueryRecord = (ImageView)view.findViewById(R.id.queryRecord);
        ibQueryRecord.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Log.v("Avis","Click for record");
                promptSpeechInput();
            }
        });

        return view;
    }

    private void init(){
        Patient userObj = SharedWrapperStatic.getUser(getActivity().getApplicationContext());
        userId     = userObj.getUserId();
        userMail   = userObj.getUserEmail();

        userName   = userObj.getUserName();

        df = this;
        getDialog().setTitle("Send us your Query");
    }
    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, 100);
        } catch (ActivityNotFoundException a) {
            //Toast.makeText(getContext(),
            //        getString(R.string.speech_not_supported),
            //        Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v("Avis", "Speech Input Result recieved with result code " + resultCode);

        ArrayList<String> result = data
                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        etQueryText.setText(result.get(0));
        Log.v("Avis", "Result : " + result.get(0));
    }


    private void fireRequest() {
        // get the current location from beacon Utility thread. Sivaram 02-Nov
        String currentBeacId = BeaconUtility.getCurrentBeaconId();

        //check whether speech to text is invoked, if not check for user query

        String textConverted = etQueryText.getText().toString();
        textConverted=textConverted.replaceAll(" ", "%20");
        String mobApp  = "&userMobile=" + userMobile;
        userName = userName.replaceAll(" ","%20");
        String nameApp = "&userName="   + userName;
        textConverted = textConverted.replaceAll(" ","%20");
        String querApp = "&userQuery="  + textConverted;
        String locApp  = "&beaconId="   + currentBeacId;
        //String url = AppConstants.baseUrl + "?action=newQuery&userMobile=7829780080&userName=Mobile&userQuery=" + textConverted +"&beaconId=beacon2";
        String url = AppConstants.baseUrl + "?action=newQuery" + mobApp + nameApp + querApp + locApp;
        Log.v("Avis", "Firing request");
        Log.v("Avis", url);
        new DownloadTask().execute(url);
        textConverted = null;
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            try {
                Log.v("Avis","doInBackground:" + urls[0]);
                return loadFromNetwork(urls[0]);
            } catch (IOException e) {
                //return getString(R.string.connection_error);
                return "";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // Show Alert
            Log.v("Avis","onPostExecute");
            df.dismiss();
        }
    }

    /** Initiates the fetch operation. */
    private String loadFromNetwork(String urlString) throws IOException {
        InputStream stream = null;
        Log.v("Avis","LoadFromNetwork :" +urlString);
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
    }

    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        Log.v("Avis","dowloadUrl : " + urlString);
        // Start the query
        conn.connect();
        InputStream stream = conn.getInputStream();
        return stream;
    }

    private String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    @Override
    public void onClick(View v) {

    }
}
