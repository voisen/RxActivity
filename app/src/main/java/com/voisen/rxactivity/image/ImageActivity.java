package com.voisen.rxactivity.image;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.voisen.rxactivity.R;
import com.voisen.rxprocessor.RxPath;

@RxPath("222")
public class ImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
    }
}