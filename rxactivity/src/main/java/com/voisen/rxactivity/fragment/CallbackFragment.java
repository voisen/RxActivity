package com.voisen.rxactivity.fragment;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;

public final class CallbackFragment extends Fragment {

    private final AtomicInteger mRequestCode = new AtomicInteger(0);
    private final HashMap<Integer, PublishSubject<Intent>> mCallbackMap = new HashMap<>();

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PublishSubject<Intent> subject = mCallbackMap.get(requestCode);
        if (subject == null){
            return;
        }
        if (resultCode == Activity.RESULT_CANCELED){
//            subject.onError(new UserCanceledException("User cancelled !"));
        }else{
            subject.onNext(data);
            subject.onComplete();
        }
        mCallbackMap.remove(requestCode);
    }

    public Observable<Intent> startActivity(Intent intent,
                                            int enterAnim,
                                            int exitAnim,
                                            ActivityOptions activityOptions,
                                            boolean observable) {
        PublishSubject<Intent> subject = null;
        Bundle optionsBundle = activityOptions == null ? null : activityOptions.toBundle();
        if (observable) {
            int requestCode = mRequestCode.getAndAdd(1);
            subject = PublishSubject.create();
            startActivityForResult(intent, requestCode, optionsBundle);
            mCallbackMap.put(requestCode, subject);
        }else{
            startActivity(intent, optionsBundle);
        }
        if(enterAnim >= 0 || exitAnim >= 0) {
            FragmentActivity activity = getActivity();
            if (activity != null) {
                activity.overridePendingTransition(Math.max(enterAnim, 0), Math.max(exitAnim, 0));
            }
        }
        return subject;
    }
}
