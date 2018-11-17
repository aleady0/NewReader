package com.example.dima.newreader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ItemActivity extends AppCompatActivity {
    WebView webView;
    int position;
    String note_txt;
    private static final String NOTE_POSITION = "note_position";
    private static final String NOTE_TEXT = "note_text";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        Intent intent = getIntent();
        Bundle bundle =  intent.getExtras();
        if (bundle != null) {
            position = intent.getIntExtra(NOTE_POSITION,-1);
            note_txt = intent.getStringExtra(NOTE_TEXT);
        }

        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(note_txt);
    }
}
