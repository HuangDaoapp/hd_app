package com.example.hisland.AppActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.hisland.R;

public class HelpWd extends AppCompatActivity {
    WebView wv;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpwd);
        wv = (WebView) findViewById(R.id.webv1);
        tv = (TextView) findViewById(R.id.tv);
        tv.setText("荒岛帮助文档");
        Intent str = getIntent();
        String url = str.getStringExtra("url").toString();
        wv.loadUrl(url);
    }
}
