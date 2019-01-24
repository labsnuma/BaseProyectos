package com.cardmax.base.Muro.Fragmentos;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cardmax.base.Muro.MuroMainActivity;
import com.cardmax.base.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavegadorFragment extends Fragment {
    private WebView mWebView;


    public NavegadorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.muro_fragment_navegador, container, false);
        mWebView = (WebView)v.findViewById(R.id.web_activity);
        String url = ((MuroMainActivity)this.getActivity()).alert.toString();

        mWebView.loadUrl(url);

        // Enable Javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Force links and redirects to open in the WebView instead of in a browser
        mWebView.setWebViewClient(new WebViewClient());

        return v;
    }

}
