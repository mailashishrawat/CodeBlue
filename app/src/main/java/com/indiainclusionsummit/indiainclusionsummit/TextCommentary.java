package com.indiainclusionsummit.indiainclusionsummit;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.w3c.dom.Text;

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


public class TextCommentary extends ActionBarActivity {

    private String textCommentUrl = AppConstants.baseUrl + "?action=getTextStream";
    private Context context;
    private ArrayList<TextComment> commentlist = new ArrayList<TextComment>();
    private TimerTask doAsynchronousTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_commentary);
        context = getApplicationContext();
        loadTextComments();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_text_commentary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void loadTextComments(){
       callAsynchronousTask();
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
                            new DownloadTask(context).execute(textCommentUrl);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 25000); //execute in every 15 seconds
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
            Log.v("Avis", "Read after downloading url");
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
           /* progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Text Commentary Loading...");
            progressDialog.setMessage("Finding out ");
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.show();*/
            Log.v("Avis","Loading Text Comments");
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
            Log.v("Avis", "Text Comment recieved");
            final ListView textCommentView = (ListView) findViewById(R.id.lv_textComments);
            Log.v("Avis","Result is " + result);
            parsejsonToObject(result);

            MyCustomAdapter_TextCommentry adapter;
            adapter = new MyCustomAdapter_TextCommentry(context,commentlist);
            textCommentView.setAdapter(adapter);
        }
    }

    private void parsejsonToObject(String l_json) {
        JSONTokener jTokener = new JSONTokener(l_json);
        int i = 0;
        try{
            JSONObject root = (JSONObject) jTokener.nextValue();
            commentlist.clear();
            JSONArray comments = root.getJSONArray("TextCommentary"); //root
            for (;i<comments.length();i++)
            {
                Log.v("Avis", "Parsing object number " + i);
                JSONObject c = (JSONObject)comments.get(i);
                parseComments(c);
            }

        }
        catch(Exception e)
        {
            //only first tiem user master, after that it will be return code for friend requests. root node name will be DBUpdate
            Log.v("Avis","parse exception");
            e.printStackTrace();
        }
    }

    private void parseComments(JSONObject c) {
        try
        {
            TextComment commentObj = new TextComment(c.getString("id"),c.getString("textComment"),c.getString("feedDate"),c.getString("feedTime"));
            commentlist.add(commentObj);
        }
        catch(Exception e){
            Log.v("Avis","Parse individual error");
            e.printStackTrace();
        }

    }


    @Override
    public void onBackPressed() {
        Log.v("Avis", "onBackPressed Called");
        doAsynchronousTask.cancel();
        super.onBackPressed();
    }

}
