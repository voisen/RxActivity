package com.voisen.rxactivity;

import android.app.Application;

public class App extends Application {

    public static ActivityService activitys;

    @Override
    public void onCreate() {
        super.onCreate();
        RxActivity.init(this);
        activitys = new RxActivity.Builder()
                .create(ActivityService.class);
    }
}
