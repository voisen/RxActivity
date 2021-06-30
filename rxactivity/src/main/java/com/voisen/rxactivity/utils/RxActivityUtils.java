package com.voisen.rxactivity.utils;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.voisen.rxactivity.RxActivityObserve;
import com.voisen.rxactivity.annotations.RxAutowired;
import com.voisen.rxactivity.annotations.RxSaveState;
import com.voisen.rxactivity.fragment.RxCallbackFragment;

import java.lang.reflect.Field;

public class RxActivityUtils {
    private final static String TAG = "__ACTIVITY_CALLBACK_FRAGMENT";

    public static RxActivityObserve<Intent> startActivity(@NonNull FragmentActivity activity,
                                                          Class<? extends FragmentActivity> clazz,
                                                          Bundle bundle,
                                                          boolean observable){
        return RxActivityUtils.startActivity(activity, clazz, -1, -1, bundle, null, observable);
    }

    public static RxActivityObserve<Intent> startActivity(@NonNull FragmentActivity activity,
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


    public static RxActivityObserve<Intent> startActivity(@NonNull FragmentActivity activity,
                                                          Intent intent,
                                                          int enterAnim,
                                                          int exitAnim,
                                                          ActivityOptions activityOptions,
                                                          boolean observable){

        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        RxCallbackFragment fragment = (RxCallbackFragment) fragmentManager.findFragmentByTag(TAG);
        if (fragment == null){
            fragment = new RxCallbackFragment();
            fragmentTransaction.add(fragment, TAG);
            fragmentTransaction.commitNowAllowingStateLoss();
        }
        return fragment.startActivity(intent, enterAnim, exitAnim, activityOptions, observable);
    }

    public static void injectValues(Activity activity){
        Bundle bundle = activity.getIntent().getExtras();
        injectIntentBundleValues(activity.getClass(), bundle, activity);
    }

    public static void injectIntentBundleValues(Class<?> clazz, Bundle bundle, Object obj){
        if (clazz == null ||
                bundle == null ||
                !Activity.class.isAssignableFrom(clazz) ||
                clazz.equals(AppCompatActivity.class) ||
                clazz.equals(FragmentActivity.class) ||
                clazz.equals(Activity.class)){
            return;
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            RxAutowired rxAutowired = field.getAnnotation(RxAutowired.class);
            if (rxAutowired != null){
                boolean ignoreNull = rxAutowired.ignoreNull();
                String key = field.getName();
                if (key.length() == 0){
                    key = rxAutowired.value();
                }
                if (key.length() == 0){
                    key = rxAutowired.key();
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

    public static void restoreSavedInstanceState(Activity activity, Bundle saveInstanceState){
        restoreSavedInstanceState(activity.getClass(), saveInstanceState, activity);
    }

    public static void restoreSavedInstanceState(Class<?> clazz, Bundle saveInstanceState, Object obj) {
        if (clazz == null ||
                saveInstanceState == null ||
                !Activity.class.isAssignableFrom(clazz) ||
                clazz.equals(AppCompatActivity.class) ||
                clazz.equals(FragmentActivity.class) ||
                clazz.equals(Activity.class)){
            return;
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            RxSaveState injectField = field.getAnnotation(RxSaveState.class);
            if (injectField != null){
                boolean ignoreNull = injectField.ignoreNull();
                String key = field.getName();
                if (saveInstanceState.containsKey(key)){
                    Object o = saveInstanceState.get(key);
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
        restoreSavedInstanceState(clazz.getSuperclass(), saveInstanceState, obj);
    }

    public static void savedInstanceState(Activity activity, Bundle outBundle){
        savedInstanceState(activity.getClass(), outBundle, activity);
    }

    public static void savedInstanceState(Class<?> clazz, Bundle outBundle, Object obj){
        if (clazz == null ||
                outBundle == null ||
                !Activity.class.isAssignableFrom(clazz) ||
                clazz.equals(AppCompatActivity.class) ||
                clazz.equals(FragmentActivity.class) ||
                clazz.equals(Activity.class)){
            return;
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            RxSaveState injectField = field.getAnnotation(RxSaveState.class);
            if (injectField == null){
                continue;
            }
            field.setAccessible(true);
            try {
                Object o = field.get(obj);
                if (injectField.ignoreNull() && o == null){
                    continue;
                }
                RxBundleUtils.putObject(outBundle, field.getName(), o);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        savedInstanceState(clazz.getSuperclass(), outBundle, obj);
    }
}
