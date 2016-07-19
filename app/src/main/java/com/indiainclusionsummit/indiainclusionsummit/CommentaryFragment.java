package com.indiainclusionsummit.indiainclusionsummit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.MediaController;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
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
 * Created by I055845 on 10/9/2015.
 */
public class CommentaryFragment extends Fragment implements View.OnClickListener{

    public static MediaPlayer mPlayer;
    ImageView buttonPlay;
    TextView play_message;
    ArrayList<EventStream> clist = new ArrayList<EventStream>();
    String streamUrl;
    String streamAudioUrl = null;
    //String url = "http://users.skynet.be/fa046054/home/P22/track06.mp3";
    // String url = "http://10.52.236.85:88/broadwave.m3u?src=1&rate=1";
    protected Activity mActivity;
    private Spinner spinLang;
    private ProgressDialog pd ;
    public static boolean AudioPlaying = false;
    private Context context;
    private TextView tvTuneIn;
    private TextView tvEng;
    private TextView tvKan;

    @Override
    public void onAttach(Activity act)
    {
        super.onAttach(act);
        mActivity = act;
    }

    @Override
    public void onCreate(Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle saveInstanceState)
    {
        super.onActivityCreated(saveInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getContext();

        View view = inflater.inflate(R.layout.live_commentory, container, false);
        spinLang = (Spinner) view.findViewById(R.id.spinn_Language);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.live_lang_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinLang.setAdapter(adapter);
        tvTuneIn = (TextView) view.findViewById(R.id.tvRadioFreq);
        tvEng    = (TextView) view.findViewById(R.id.tvRadio1);
        tvKan    = (TextView) view.findViewById(R.id.tvRadio2);
        getStreamUrl();

        //added on 18 nov
        tvTuneIn.setFocusableInTouchMode(true);
    //    tvTuneIn.requestFocus();

        // new code n 04-nov

        Button btnTextStream = (Button) view.findViewById(R.id.btnTextStreaming);
        btnTextStream.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent in = new Intent(context, com.indiainclusionsummit.indiainclusionsummit.TextCommentary.class);
                in.addCategory(Intent.ACTION_DEFAULT);
                startActivity(in);
            }
                     });

        // end of new code on 04-nov

        buttonPlay = (ImageView)view.findViewById(R.id.image_button_play);
        buttonPlay.requestFocus(0);
        play_message = (TextView)view.findViewById(R.id.play_message);
        buttonPlay.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // check for internet and then start streaming
                Log.v("Avis","Streaming button clicked now with Audio Playing as " + CommentaryFragment.AudioPlaying);
                boolean isInternet = Utils.checkForInternetConnection(getContext());;
                if (CommentaryFragment.AudioPlaying == true && mPlayer!= null)
                {
                    mPlayer.stop();
                    CommentaryFragment.AudioPlaying = false;
                    AudioPlaying  = false;
                    play_message.setText(R.string.play_message_text);
                    buttonPlay.setImageResource(R.drawable.button_orange_play);
                    buttonPlay.setContentDescription("Start listening to live audio commentary.");

                    Log.v("Avis", "Audio playing true,player stopped now");
                } else if( CommentaryFragment.AudioPlaying == true ) {
                    Log.v("Avis","Audio playing true, but player is null");
                }
                else if (CommentaryFragment.AudioPlaying == false){
                    new StreamTask().execute(null,null,null);
                }
                if (!isInternet)
                    Toast.makeText(getContext(),"Issue in connecting to Internet. Check your internet settings",Toast.LENGTH_LONG).show();

 /*               // start with the player work
                boolean boolException = false;
                if (mPlayer != null && mPlayer.isPlaying()) {
                    Log.v("Avis", "Going to stop stream from" + streamAudioUrl);
                    mPlayer.stop();
                    play_message.setText(R.string.play_message_text);
                    buttonPlay.setImageResource(R.drawable.button_orange_play); // .setText("Play");
                    buttonPlay.setContentDescription("Start listening to live audio commentary.");
                } else if (streamAudioUrl != null ) {
                    Log.v("Avis", "Going to stream from" + streamAudioUrl);
                    mPlayer = new MediaPlayer();
                    mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                 try {
                        mPlayer.setDataSource(streamAudioUrl);
                        Log.v("Avis", "data source okay");
                    } catch (IllegalArgumentException e) {
                        Toast.makeText(getContext(), "You might not set the URL correctly!", Toast.LENGTH_LONG).show();
                        boolException = true;
                    } catch (SecurityException e) {
                        Toast.makeText(getContext(), "You might not set the URI correctly!",
                                Toast.LENGTH_LONG).show();
                        boolException = true;
                    } catch (IllegalStateException e) {
                        Toast.makeText(getContext(), "You might not set the URI correctly!",
                                Toast.LENGTH_LONG).show();
                        boolException = true;
                    } catch (IOException e) {
                        boolException = true;
                        e.printStackTrace();
                    }

                    try {
                        mPlayer.prepare();
                        Log.v("Avis", "player prepared");
                    } catch (IllegalStateException e) {
                        Toast.makeText(getContext(), "You might not set the URI correctly!",
                                Toast.LENGTH_LONG).show();
                        boolException = true;
                    } catch (IOException e) {
                        Toast.makeText(getContext(), "You might not set the URI correctly!",
                                Toast.LENGTH_LONG).show();
                        boolException = true;
                    }
                    if (!boolException) {
                        Log.v("Avis","going to start");
                    mPlayer.start();
                    play_message.setText(R.string.pause_message_text);
                    buttonPlay.setImageResource(R.drawable.button_orange_pause); //setText("Stop");
                    buttonPlay.setContentDescription("Stop listening to live audio commentary."); }
                    else {
                        Toast.makeText(getContext(),"Some issue in connecting to Internet", Toast.LENGTH_LONG).show();
                    }
                    //buttonPlay.setContentDescription(R.string.live_commentory_pause_message);
                }
                else {
                    Toast.makeText(getContext(),"Some issue in connecting to Internet", Toast.LENGTH_LONG).show();
                }
*/
                // end the progress here.

            }
        });


        return view;
    }

    private void init() {

    }
    private void getStreamUrl() {
        String streamUrl;
        streamUrl = AppConstants.baseUrl + "?action=getLiveStreamUrl";
       /* int lang_spinner = spinLang.getSelectedItemPosition();
        String langApp = "&lang=";
        if (lang_spinner == 0)
            langApp = langApp + "EN";
        else if (lang_spinner == 1)
            langApp = langApp + "KAN";*/

     /*   streamUrl = streamUrl + langApp;*/
        Log.v("Avis",streamUrl);

        //check for internet connectivity.
        boolean isConnected = Utils.checkForInternetConnection(getContext());
        if (!isConnected)
            Toast.makeText(getContext(),"Issue in connecting to Internet. Kindly check your connection settings", Toast.LENGTH_LONG).show();
        else
            new DownloadTask().execute(streamUrl);
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
            Log.v("Avis", "Response recieved for Event Stream");
            Log.v("Avis","result is " + result);
            parsejsonToObject(result);
            if (clist.size() > 0)
                streamAudioUrl = clist.get(0).getEventDesc();
            if (streamAudioUrl != null)
            {
                String textTuneIn = "For Audio Commentary, with your Radio Application please tune into:";

                textTuneIn = textTuneIn;
                tvTuneIn.setText(textTuneIn);

                //second
                String splits[];
                splits = streamAudioUrl.split("and", 2);
                tvEng.setText(splits[0]);
          //      tvEng.setFocusableInTouchMode(true);
                if (splits.length > 1) {
                    tvKan.setText(splits[1]);
                //    tvKan.setFocusableInTouchMode(true);
                }


                // added on 18 Nov for accessibility cahnges

                tvTuneIn.setFocusableInTouchMode(true);
                tvTuneIn.requestFocus();

            }
        }

    }

    private class StreamTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {

            Log.v("Avis","StreamTask : Pre Execute");
            if(AudioPlaying == false)
            {
                mPlayer = new MediaPlayer();
                mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                Log.v("Avis", "New Media Player instantiated");
            }
            else {
                Log.v("Avis", "Media player going to stop");
                mPlayer.stop();
                play_message.setText(R.string.play_message_text);
                buttonPlay.setImageResource(R.drawable.button_orange_play); // .setText("Play");
                buttonPlay.setContentDescription("Start listening to live audio commentary.");
            }

            pd = new ProgressDialog(getContext());
            pd.setTitle("Live Streaming...");
            pd.setMessage("Connecting..Please wait.");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
            Log.v("Avis", "Progress dialog instantiated");
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                //Do something...
                //Thread.sleep(5000);
                Log.v("Avis", "Stream : Background");
                try
                {
                    Log.v("Avis", "Stream : Background : Going to set DataSource");
                    Log.v("Avis","StreamAudio Url is " + streamAudioUrl);
                    mPlayer.setDataSource(streamAudioUrl);
                    Log.v("Avis", "Stream : Background : Going to Prepare");
                    mPlayer.prepare();
                    Log.v("Avis", "Stream : Background : Going to Start");
                    mPlayer.start();
                    AudioPlaying=true;
                    Log.v("Avis","Setting audio player to true");
                    Log.v("Avis", "Stream : Background : Started");
                    play_message.setText(R.string.pause_message_text);
                    buttonPlay.setImageResource(R.drawable.button_orange_pause); //setText("Stop");
                    buttonPlay.setContentDescription("Stop listening to live audio commentary.");
                }
                catch (IOException e) {
                    Log.v("Avis", "Error in playing : Background Stream");
                    Log.e("AudioFileError", "Could not open file " + streamAudioUrl + " for playback.", e);
                    Toast.makeText(getContext(),"Error in streaming, Please try again", Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (pd!=null) {
                Log.v("Avis", "Stream : PostExecute : Executed");
                pd.dismiss();
                //b.setEnabled(true);
            }
        }

    };

    private void parsejsonToObject(String l_json) {
        JSONTokener jTokener = new JSONTokener(l_json);
        int i = 0;
        try{
            JSONObject root = (JSONObject) jTokener.nextValue();
            JSONArray categories = root.getJSONArray("EventsStream"); //root
            for (;i<categories.length();i++)
            {
                Log.v("Avis", "Parsing object number " + i);
                JSONObject c = (JSONObject)categories.get(i);
                parseEvent(c);
            }

        }
        catch(Exception e)
        {
            Log.v("Avis","parse exception");
            e.printStackTrace();
        }
    }

    private void parseEvent(JSONObject c) {
        try
        {
            EventStream eventObj = new EventStream(c.getString("autoEventId"), c.getString("eventTitle"),c.getString("eventDesc"));
            clist.add(eventObj);
        }
        catch(Exception e){
            Log.v("Avis","Parse individual error");
            e.printStackTrace();
        }

    }


    @Override
    public void onClick(View v) {

    }
}
