package com.voisen.rxactivity;

import android.app.Activity;
import android.content.Intent;

public interface RxActivityInterceptor {

    boolean shouldInterceptorIntent(Activity fromActivity, Intent intent);

    default Intent overrideIntent(Activity fromActivity, Intent intent){
        return intent;
    }

    default Object overrideResultData(Activity fromActivity, Intent data){
        return data;
    }

    default void onErrorOccurred(Activity fromActivity , Intent intent, Throwable e){
        e.printStackTrace();
    }
}
