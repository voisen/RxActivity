package com.voisen.rxactivity.fragment;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.voisen.rxactivity.RxActivityObserve;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public final class RxCallbackFragment extends Fragment {

    private final AtomicInteger mRequestCode = new AtomicInteger(0);
    private final HashMap<Integer, RxActivityObserve<Intent>> mCallbackMap = new HashMap<>();

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        RxActivityObserve<Intent> subject = mCallbackMap.get(requestCode);
        if (subject == null){
            return;
        }
        if (resultCode == Activity.RESULT_CANCELED){
            subject.canceled();
        }else{
            subject.onResult(data);
        }
        mCallbackMap.remove(requestCode);
    }

    public RxActivityObserve<Intent> startActivity(Intent intent,
                                                   int enterAnim,
                                                   int exitAnim,
                                                   ActivityOptions activityOptions,
                                                   boolean observable) {
        FragmentActivity activity = getActivity();
        RxActivityObserve<Intent> subject = null;
        Bundle optionsBundle = activityOptions == null ? null : activityOptions.toBundle();
        if (observable) {
            int requestCode = mRequestCode.getAndAdd(1);
            subject = RxActivityObserve.create();
            startActivityForResult(intent, requestCode, optionsBundle);
            mCallbackMap.put(requestCode, subject);
        }else{
            startActivity(intent, optionsBundle);
        }
        if(enterAnim >= 0 || exitAnim >= 0) {
            if (activity != null) {
                activity.overridePendingTransition(Math.max(enterAnim, 0), Math.max(exitAnim, 0));
            }
        }
        return subject;
    }

}
