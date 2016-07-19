package com.indiainclusionsummit.indiainclusionsummit;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
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


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public Button loginBtn;

    public Context context;
    public EditText emailAddress;
    private EditText mobNumber;
    private Spinner spinner;
    private String consent_string;
    public String text;
    public Button scanBtn;
    public String contents;
    public String mPhoneNumber;
    private String emailAdd;
    private String mobNumb;
    private InputStream is;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;


    //GCM related declarations.
    RequestParams params = new RequestParams();
    GoogleCloudMessaging gcmObj;
    Context applicationContext;
    String regId = "";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    AsyncTask<Void, Void, String> createRegIdTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().hide();
        HandlePermissions();
        //code to obtain ph no
        TelephonyManager tMgr = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        mPhoneNumber = tMgr.getLine1Number();
        //for login
        SharedPreferences settings;
        context = this.getApplicationContext();
        applicationContext=context;
        settings = context.getSharedPreferences(AppConstants.PREFS_NAME, Context.MODE_PRIVATE); //1
        String textId     = settings.getString(AppConstants.PREFS_KEY_ID,null);
        String textMail   = settings.getString(AppConstants.PREFS_KEY_MAIL,null);
        String textMobile = settings.getString(AppConstants.PREFS_KEY_MOBILE,null);
        String textName   = settings.getString(AppConstants.PREFS_KEY_NAME,null);
        Log.v("Avis","Reading shared preference now with " + textMail + textId + textMobile + textName);
        if(textId != null && textMail != null && textMobile != null && textName != null)
        {
            try {
                //Class name = Class.forName("com.indiainclusionsummit.indiainclusionsummit.HomeActivity");
                Class name = Class.forName("com.indiainclusionsummit.indiainclusionsummit.DetailsActivity");
                Intent myint= new Intent(this, name);
                myint.putExtra("key_id",textId);
                myint.putExtra("key_email",textMail);
                myint.putExtra("key_mobile",textMobile);
                myint.putExtra("key_name", textName);
                Log.v("Avis","Read from shared preferences" + textId + textMail + textMobile + textName);
                startActivity(myint);
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        Drawable d = getResources().getDrawable(R.drawable.iis_background_image);
        getSupportActionBar().setBackgroundDrawable(d);

        setContentView(R.layout.activity_main);
        Log.v("Avis", "Proceeding for user action and then save");
        initialize();
    }

    public void initialize() {
        // TODO Auto-generated method stub

        loginBtn = (Button)findViewById(R.id.loginBtn);
        scanBtn = (Button)findViewById(R.id.scanBtn);
        emailAddress = (EditText)findViewById(R.id.emailAddress);
        mobNumber    = (EditText)findViewById(R.id.mobileNumber);
        loginBtn.setOnClickListener(this);
        scanBtn.setOnClickListener(this);
        // emailAddress.setText(mPhoneNumber != null ? mPhoneNumber : "no value");

        spinner = (Spinner) findViewById(R.id.consent_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.consent_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        //added on 18-nov for accessibility for dropdown
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                spinner.setFocusableInTouchMode(true);
                spinner.requestFocus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                spinner.setFocusableInTouchMode(true);
                spinner.requestFocus();
            }
        });


    }

    private void HandlePermissions()
    {
        if(this.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_REQUEST_COARSE_LOCATION);

        }

        if(this.checkSelfPermission(Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_SMS},
                    PERMISSION_REQUEST_COARSE_LOCATION);

        }

        if(this.checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    PERMISSION_REQUEST_COARSE_LOCATION);

        }


        if(this.checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET},
                    PERMISSION_REQUEST_COARSE_LOCATION);

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("App", "coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            finish();
                        }

                    });
                    builder.show();
                }
                return;
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

/*        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch(v.getId()){
            case R.id.loginBtn:
                //check for internet connectivity first
                boolean isConnected = Utils.checkForInternetConnection(getApplicationContext());
                if (!isConnected) {
                    Toast.makeText(getApplicationContext(),"There is no internet Connection, Check your internet settings",Toast.LENGTH_LONG).show();
                    return;
                }
                Log.v("Avis","Login with email and phone");
                String categoryUrl = AppConstants.baseUrl;
               /*   categoryUrl = categoryUrl + "?action=getUserDetail";
              String emailApp ="&email=";
                String mobApp = "&phone=";
                String consentApp = "&consent=";
                emailApp = emailApp + emailAddress.getText().toString();
                mobApp   = mobApp + mobNumber.getText().toString();
                int consent_spinner = spinner.getSelectedItemPosition();
                Log.v("Avis","Consent Spinner selected item position is " + consent_spinner);
                switch(consent_spinner){
                    case 0 :
                        consent_string = "all";
                        break;
                    case 1:
                        consent_string = "mail";
                        break;
                    case 2:
                        consent_string = "phone";
                        break;
                    case 3:
                        consent_string = "none";
                        break;
                }

                consentApp = consentApp + consent_string;*/
                EditText txtEmail=(EditText)findViewById(R.id.emailAddress);
              //  categoryUrl = categoryUrl +"/"+AppConstants.PATIENT+"?filter=emailid eq '"+txtEmail.getText()+ "'&"+AppConstants.JSON_FORMAT;

                    categoryUrl = categoryUrl +"/"+AppConstants.PATIENT+"('"+txtEmail.getText()+"')?"+AppConstants.JSON_FORMAT;
         //       categoryUrl = categoryUrl +"/"+"Customers"+"?"+AppConstants.JSON_FORMAT;

                 new DownloadTask(context).execute(categoryUrl);

                break;


            case R.id.scanBtn:
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE", "QR_CODE_MODE");
                Log.v("Avis","Scan is fine");
                startActivityForResult(intent, 0);
        }

    }



    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
//		IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (requestCode == 0) {
            if(resultCode == RESULT_OK)
            {
                //we have a result
//			registerId.setText("beforescan");
//			String scanContent = scanningResult.getContents();
//			String scanFormat = scanningResult.getFormatName();
                contents = intent.getStringExtra("SCAN_RESULT");
                String email = null, mobile = null;
                String[] parts = contents.split("#");
                email = parts[0];
                mobile = parts[1];
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

                emailAddress.setText(email != null?email:"no value");
                mobNumber.setText(mobile != null?mobile:"no value");

            }
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }



    private String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    };


    public void callDetailActivity(String textId,String textEmail, String textMob,String textName)
    {
        Intent in = new Intent(context, com.indiainclusionsummit.indiainclusionsummit.DetailsActivity.class);
        in.putExtra("key_id",textId);
        in.putExtra("key_email",textEmail);
        in.putExtra("key_mobile",textMob);
        in.putExtra("key_name", textName);
        in.addCategory(Intent.ACTION_DEFAULT);
        startActivity(in);

    }
    public void callDetailActivity(Patient patient)
    {
        Intent in = new Intent(context, com.indiainclusionsummit.indiainclusionsummit.DetailsActivity.class);
        in.putExtra("patient",patient);

        in.addCategory(Intent.ACTION_DEFAULT);
        startActivity(in);

    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        private ProgressDialog progressDialog;
        private Context context;
        private int response;

        public DownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute(){
            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle(" Logging In...");
            progressDialog.setMessage("Validating Credentials " + "...Please wait.");
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
//            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            // TODO Auto-generated method stub
            try{
               // return loadFromNetwork(urls[0]);
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return e.toString();
            } catch (URISyntaxException e) {
                e.printStackTrace();
                return e.toString();
            }
        }


        protected void onPostExecute(String result) {
            JSONObject json=null;
            Patient patient= null;

            if ((response!=AppConstants.RESPONSE_SUCESS))
            {  Toast.makeText(context," no response from server",Toast.LENGTH_LONG).show();
            return;
            }
            try {
                 json = new JSONObject(result);
                patient = new Patient((JSONObject) json.get("d"),getApplicationContext());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.v("Avis", "Response recieved for netwrking catgs");
            Log.v("Avis", "result is " + result);
          //  parsejsonToObject(result);
            if ( json==null) {
                Toast.makeText(context,"Invalid patient id or email address ",Toast.LENGTH_LONG).show();

                }
            //error out here.
            else
            {
                Log.v("Avis","User Login Sucess id is " );


//                progressDialog.dismiss();
                callDetailActivity( patient);
            }
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
              response = conn.getResponseCode();
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
        private void parsejsonToObject(String l_json) {
            JSONTokener jTokener = new JSONTokener(l_json);
            int i = 0;
            try{
                JSONObject root = (JSONObject) jTokener.nextValue();
                JSONArray userMaster = root.getJSONArray("UserMaster"); //root
                for (;i<userMaster.length();i++) {
                    Log.v("Avis", "Parsing object number " + i);
                    JSONObject c = (JSONObject)userMaster.get(i);

                }

            }
            catch(Exception e)
            {
                Log.v("Avis","parse exception");
                e.printStackTrace();
            }
        }


    }

    private void fireToGCM(String email, String phone, String userId, String userName) {
        if (checkPlayServices()) {

            // Register Device in GCM Server
            registerInBackground(email, phone , userId, userName);
        }
    }

    private void registerInBackground(final String email, final String phone , final String userId, final String userName) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcmObj == null) {
                        gcmObj = GoogleCloudMessaging
                                .getInstance(getApplicationContext());
                    }
                    regId = gcmObj
                            .register(Config_GCM.GOOGLE_SENDER_ID);
                    msg = "Registration ID :" + regId;
                    Log.v("Avis","Success in GCM instance with regId as " + regId);

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    Log.v("Avis","Exception recieved in gcm instance ..." + msg);
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                //      showProgress(false);
                if (!TextUtils.isEmpty(regId)) {
                    // Store RegId created by GCM Server in SharedPref
                    storeRegIdinSharedPref(getApplicationContext(), regId, email, phone , userId, userName);
          /*          Toast.makeText(
                            getApplicationContext(),
                            "Registered with GCM Server successfully.\n\n"
                                    + msg, Toast.LENGTH_SHORT).show();*/
                } else {
                  /*  Toast.makeText(
                            getApplicationContext(),
                            "Reg ID Creation Failed.\n\nEither you haven't enabled Internet or GCM server is busy right now. Make sure you enabled Internet and try registering again after some time."
                                    + msg, Toast.LENGTH_LONG).show();*/
                }
            }
        }.execute(null, null, null);
    }

    private void storeRegIdinSharedPref(Context applicationContext, String regId, String email, String phone , String userId, String userName ) {
        // code for shared preferences here.

        Log.v("Avis", "Store in preferences values are" + email + " and " + phone + "--" + regId);
        storeRegIdInServer(email, phone , regId , userId, userName);
    }

    private void storeRegIdInServer(final String email,final String phone ,final String pGcmId , final String userId, final String userName) {
        params.put("regId", regId);
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        String url = AppConstants.baseUrl;
        url = url + "?action=addGCMUser";
        String conExtra = "&gcmId=" + pGcmId + "&phone=" + phone + "&email=" + email;
        url = url + conExtra;
        Log.v("Avis", "Store in server values are" + email + " and " + phone + " and -- " + regId);
        Log.v("Avis","url is : " + url);
        client.post(url, params,
                new AsyncHttpResponseHandler() {
                    // When the response returned by REST has Http
                    // response code '200'
                    @Override
                    public void onSuccess(String response) {

                        /*Toast.makeText(applicationContext,
                                "Reg Id shared successfully with Web App ",
                                Toast.LENGTH_LONG).show();*/
                        //Intent i = new Intent(applicationContext, HomeActivity.class);
                        Intent i = new Intent(applicationContext, DetailsActivity.class);
                        i.putExtra("key_id",userId);
                        i.putExtra("key_email",email);
                        i.putExtra("key_mobile",phone);
                        i.putExtra("key_name", userName);
                        i.addCategory(Intent.ACTION_DEFAULT);
                        i.putExtra("key_regId", regId);

                        startActivity(i);
                        finish();
                    }

                    // When the response returned by REST has Http
                    // response code other than '200' such as '404',
                    // '500' or '403' etc
                    @Override
                    public void onFailure(int statusCode, Throwable error,
                                          String content) {

                        Log.v("Avis", "Failure in registering with GCM with status code" + statusCode);
                        // When Http response code is '404'
                        if (statusCode == 404) {
                            Toast.makeText(getApplicationContext(),
                                    "Requested resource not found",
                                    Toast.LENGTH_LONG).show();
                        }
                        // When Http response code is '500'
                        else if (statusCode == 500) {
                            Toast.makeText(getApplicationContext(),
                                    "Something went wrong at server end",
                                    Toast.LENGTH_LONG).show();
                        }
                        // When Http response code other than 404, 500
                        else {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Unexpected Error occcured! [Most common Error: Device might "
                                            + "not be connected to Internet or remote server is not up and running], check for other errors as well",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });



    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        // When Play services not found in device
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                // Show Error dialog to install Play services
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(
                        getApplicationContext(),
                        "This device doesn't support Play services, App will not work normally",
                        Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        } else {
            /*Toast.makeText(
                    getApplicationContext(),
                    "This device supports Play services, App will work normally",
                    Toast.LENGTH_LONG).show();*/
        }
        return true;
    }
}
