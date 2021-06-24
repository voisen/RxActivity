package com.voisen.rxactivity;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

public class ActivityInterceptor implements RxActivityInterceptor{
    private static final String TAG = "ActivityInterceptor";

    @Override
    public boolean shouldInterceptorIntent(Activity fromActivity, Intent intent) {
        Log.i(TAG, "shouldInterceptorIntent: 是否需要拦截: "+ intent.getComponent().getClassName());
        return false;
    }

    @Override
    public Object overrideResultData(Activity fromActivity, Intent data) {
        Log.i(TAG, "overrideResultData: 结果值: "+ data);
        return data;
    }
}
