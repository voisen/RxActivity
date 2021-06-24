package com.voisen.rxactivity.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.voisen.rxactivity.utils.ActivityUtils;

import java.util.ArrayList;
import java.util.List;

public final class ActivityLifecycle implements Application.ActivityLifecycleCallbacks {

    private final List<Activity> activities = new ArrayList<>();

    public Activity topActivity(){
        int size = activities.size();
        if (size == 0){
            return null;
        }
        return activities.get(size - 1);
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        activities.add(activity);
        try {
            ActivityUtils.injectIntentBundleValues(activity.getClass(), activity.getIntent().getExtras(), activity);
            ActivityUtils.restoreSavedInstanceState(activity.getClass(), savedInstanceState, activity);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
        ActivityUtils.savedInstanceState(activity.getClass(), outState, activity);
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        activities.remove(activity);
    }
}
