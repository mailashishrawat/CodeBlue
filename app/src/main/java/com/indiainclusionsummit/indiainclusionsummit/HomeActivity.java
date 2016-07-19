package com.indiainclusionsummit.indiainclusionsummit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * Created by I055845 on 10/9/2015.
 */
public class HomeActivity extends AppCompatActivity {

    private WebView webViewHome;
    private String name, email, mobile , id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
        Toast.makeText(this,"Loading Content from internet...", Toast.LENGTH_LONG).show();

        webViewHome = (WebView)findViewById(R.id.webViewHome);
        webViewHome.setWebViewClient(new MyBrowser());
        String iis_url = "http://www.indiainclusionsummit.com/";
        //String iis_url = R.string.iisUrlString;
        webViewHome.getSettings().setLoadsImagesAutomatically(true);
        webViewHome.getSettings().setJavaScriptEnabled(true);
        webViewHome.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webViewHome.loadUrl(iis_url);

    }

    private void init() {
        Intent callerIntent = getIntent();
        id     = new String(callerIntent.getStringExtra("key_id"));
        name   = new String(callerIntent.getStringExtra("key_name"));
        email  = new String(callerIntent.getStringExtra("key_email"));
        mobile = new String(callerIntent.getStringExtra("key_mobile"));
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu_home) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu_home);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent in = new Intent(this, DetailsActivity.class);
        in.putExtra("key_id",id);
        in.putExtra("key_email",email);
        in.putExtra("key_mobile",mobile);
        in.putExtra("key_name", name);
        startActivity(in);
        int iid = item.getItemId();
/*        if (id == R.id.action_settings) {
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    public void detailsActivity(MenuItem item) {
        Intent in = new Intent(this, DetailsActivity.class);
        in.putExtra("key_id",id);
        in.putExtra("key_email",email);
        in.putExtra("key_mobile",mobile);
        in.putExtra("key_name", name);
        startActivity(in);
    }

    public void AboutUsActivity(MenuItem item) {
        Intent in = new Intent(this, com.indiainclusionsummit.indiainclusionsummit.AboutUsActivity.class);
        in.putExtra("key_id",id);
        in.putExtra("key_email",email);
        in.putExtra("key_mobile",mobile);
        in.putExtra("key_name", name);
        in.addCategory(Intent.ACTION_DEFAULT);
        startActivity(in);
    }

    public void FeedbackActivity(MenuItem item) {
        Intent in = new Intent(this, com.indiainclusionsummit.indiainclusionsummit.FeedbackActivity.class);
        in.putExtra("key_id",id);
        in.putExtra("key_email",email);
        in.putExtra("key_mobile",mobile);
        in.putExtra("key_name", name);
        in.addCategory(Intent.ACTION_DEFAULT);
        startActivity(in);
    }


}
