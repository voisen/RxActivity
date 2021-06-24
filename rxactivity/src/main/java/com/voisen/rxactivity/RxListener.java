package com.voisen.rxactivity;

import android.util.Log;

public interface RxListener<T> {
    String TAG = "RxActivity";
    void onResult(T data);
    default void onError(Throwable e){
    }
    default void onCanceled(){
        Log.i(TAG, "onCanceled: 用户取消");
    }
}
