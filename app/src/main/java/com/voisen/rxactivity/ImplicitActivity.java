package com.voisen.rxactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

public class ImplicitActivity extends AppCompatActivity {

    WebView webView;
    View mContentView;


    private void initWebView() {
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.evaluateJavascript("androidCall('"+getPackageName()+"')", null);
            }
        });
        webView.loadData("<!DOCTYPE html><html>\t<head>\t\t<meta charset=\"utf-8\">\t\t<title>测试</title>\t</head>\t<body>\t\t<h1 id=\"h1\">等待中</h1>\t</body>\t<script type=\"text/javascript\">\t\tfunction androidCall(val){\t\t\tdocument.getElementById(\"h1\").innerText = val;\t\t}\t</script></html>",
                "text/html",
                "utf-8");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContentView = LayoutInflater.from(this).inflate(R.layout.settings_activity, null);
        setContentView(mContentView);
        webView = findViewById(R.id.webview);
        initWebView();
    }


    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        data.putExtra("value", "ImplicitActivity Back");
        setResult(RESULT_OK, data);
        finish();
    }
}