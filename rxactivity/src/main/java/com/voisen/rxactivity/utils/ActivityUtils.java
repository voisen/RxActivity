package com.voisen.rxactivity.utils;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.voisen.rxactivity.RxObserve;
import com.voisen.rxactivity.fragment.CallbackFragment;
import com.voisen.rxactivity.anno.Autowired;
import com.voisen.rxactivity.anno.SaveState;

import java.lang.reflect.Field;

public class ActivityUtils {
    private final static String TAG = "__ACTIVITY_CALLBACK_FRAGMENT";

    public static RxObserve<Intent> startActivity(@NonNull FragmentActivity activity,
                                                   Class<? extends FragmentActivity> clazz,
                                                   Bundle bundle,
                                                   boolean observable){
        return ActivityUtils.startActivity(activity, clazz, -1, -1, bundle, null, observable);
    }

    public static RxObserve<Intent> startActivity(@NonNull FragmentActivity activity,
                                                   Class<? extends FragmentActivity> clazz,
                                                   int enterAnim,
                                                   int exitAnim,
                                                   Bundle bundle,
                                                   ActivityOptions activityOptions,
                                                   boolean observable){
        Intent intent = new Intent(activity, clazz);
        if (bundle != null){
            intent.putExtras(bundle);
        }
        return startActivity(activity, intent, enterAnim, exitAnim, activityOptions, observable);
    }


    public static RxObserve<Intent> startActivity(@NonNull FragmentActivity activity,
                                                  Intent intent,
                                                  int enterAnim,
                                                  int exitAnim,
                                                  ActivityOptions activityOptions,
                                                  boolean observable){

        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        CallbackFragment fragment = (CallbackFragment) fragmentManager.findFragmentByTag(TAG);
        if (fragment == null){
            fragment = new CallbackFragment();
            fragmentTransaction.add(fragment, TAG);
            fragmentTransaction.commitNowAllowingStateLoss();
        }
        return fragment.startActivity(intent, enterAnim, exitAnim, activityOptions, observable);
    }


    public static void injectIntentBundleValues(Class<?> clazz, Bundle bundle, Object obj){
        if (clazz == null || !FragmentActivity.class.isAssignableFrom(clazz) || bundle == null){
            return;
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Autowired autowired = field.getAnnotation(Autowired.class);
            if (autowired != null){
                boolean ignoreNull = autowired.ignoreNull();
                String key = field.getName();
                if (key.length() == 0){
                    key = autowired.value();
                }
                if (key.length() == 0){
                    key = autowired.key();
                }
                if (bundle.containsKey(key)){
                    Object o = bundle.get(key);
                    if (ignoreNull && o == null){
                        continue;
                    }else{
                        field.setAccessible(true);
                        try {
                            field.set(obj, o);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        injectIntentBundleValues(clazz.getSuperclass(), bundle, obj);
    }

    public static void restoreSavedInstanceState(Class<?> clazz, Bundle bundle, Object obj) {
        if (clazz == null || !FragmentActivity.class.isAssignableFrom(clazz) || bundle == null){
            return;
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            SaveState injectField = field.getAnnotation(SaveState.class);
            if (injectField != null){
                boolean ignoreNull = injectField.ignoreNull();
                String key = field.getName();
                if (bundle.containsKey(key)){
                    Object o = bundle.get(key);
                    if (ignoreNull && o == null){
                        continue;
                    }else{
                        field.setAccessible(true);
                        try {
                            field.set(obj, o);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        restoreSavedInstanceState(clazz.getSuperclass(), bundle, obj);
    }


    public static void savedInstanceState(Class<?> clazz, Bundle outBundle, Object obj){
        if (clazz == null || !FragmentActivity.class.isAssignableFrom(clazz) || outBundle == null){
            return;
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            SaveState injectField = field.getAnnotation(SaveState.class);
            if (injectField == null){
                continue;
            }
            field.setAccessible(true);
            try {
                Object o = field.get(obj);
                if (injectField.ignoreNull() && o == null){
                    continue;
                }
                BundleUtils.putObject(outBundle, field.getName(), o);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        savedInstanceState(clazz.getSuperclass(), outBundle, obj);
    }
}
