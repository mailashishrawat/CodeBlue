package com.indiainclusionsummit.indiainclusionsummit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * Created by I055845 on 10/21/2015.
 */
public class HomeWebFragment extends Fragment {

    private WebView webViewHomeWeb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.homeweb, container, false);

        Toast.makeText(getContext(), "Loading Content from internet...", Toast.LENGTH_LONG).show();

        webViewHomeWeb = (WebView)view.findViewById(R.id.webViewHomeWeb);
        webViewHomeWeb.setWebViewClient(new MyBrowser());
        String iis_url = "http://www.indiainclusionsummit.com/";
        //String iis_url = R.string.iisUrlString;
        webViewHomeWeb.getSettings().setLoadsImagesAutomatically(true);
        webViewHomeWeb.getSettings().setJavaScriptEnabled(true);
        webViewHomeWeb.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webViewHomeWeb.loadUrl(iis_url);

        return view;

    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
