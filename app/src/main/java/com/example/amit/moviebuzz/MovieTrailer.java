package com.example.amit.moviebuzz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MovieTrailer extends AppCompatActivity {
    WebView mWebview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_trailer);
        String urlKey=getIntent().getStringExtra("imdbKeyTrailer");
        mWebview=findViewById(R.id.m_webview);
        mWebview.setWebViewClient(new WebViewClient());
        WebSettings webSettings=mWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        String url="https://m.imdb.com/title/"+urlKey+"/";
        mWebview.loadUrl(url);
    }
}
