package com.voisen.rxactivity.image;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.voisen.rxactivity.R;
import com.voisen.rxactivity.annotations.RxPath;

@RxPath("222")
public class ImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
    }
}