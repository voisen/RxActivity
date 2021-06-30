package com.voisen.rxactivity;

import android.app.Application;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RxNavigation.init(this);
        RxNavigation.shared()
                .setInterceptor(new ActivityInterceptor());
    }
}
