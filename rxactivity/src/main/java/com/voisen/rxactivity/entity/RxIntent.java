package com.voisen.rxactivity.entity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.voisen.rxactivity.BuildConfig;
import com.voisen.rxactivity.RxIntentType;
import com.voisen.rxactivity.RxNull;
import com.voisen.rxactivity.annotations.RxActivity;
import com.voisen.rxactivity.annotations.RxExtraValue;
import com.voisen.rxactivity.annotations.RxIntentOptions;
import com.voisen.rxactivity.annotations.RxIntentValue;
import com.voisen.rxactivity.interfaces.IRxNavigation;
import com.voisen.rxactivity.utils.RxBundleUtils;
import com.voisen.rxactivity.utils.RxPackageGenUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class RxIntent {
    private static final String TAG = "RxActivity";
    private static final Map<String, String> pathMap = new HashMap<>();
    private Activity activity;
    private RxActivity rxActivity;
    private RxIntentOptions options;
    private Method method;
    private Object[] args;

    private boolean observable = false;
    private Intent intent = new Intent();

    public boolean isObservable() {
        return observable;
    }

    public Intent getIntent() {
        return intent;
    }

    public int getEnterAnim() {
        return enterAnim;
    }

    public int getExitAnim() {
        return exitAnim;
    }

    public ActivityOptions getActivityOptions() {
        return activityOptions;
    }

    public Bundle getBundle() {
        return bundle;
    }


    private int enterAnim = -1;
    private int exitAnim = -1;
    private ActivityOptions activityOptions = null;
    private Bundle bundle = new Bundle();



    public RxIntent(Activity activity, RxActivity rxActivity, RxIntentOptions options, Method method, Object[] args) {
        this.activity = activity;
        this.rxActivity = rxActivity;
        this.options = options;
        this.method = method;
        this.args = args;
    }



    public void initialization() throws Throwable{
        if (activity == null){
            throw new NullPointerException("不能够处理该请求 ! 获取到的 activity 为空 !");
        }
        observable = !void.class.isAssignableFrom(method.getReturnType());
        loadTrickActivityInfo();
        loadIntentOptionsInfo();
        loadArgsInfo();
    }

    private void loadArgsInfo() {
        Annotation[][] annotations = method.getParameterAnnotations();
        for (int i = 0; i < annotations.length; i++) {
            Annotation[] annos = annotations[i];
            Object arg = args[i];
            for (Annotation anno : annos) {
                if (anno instanceof RxExtraValue) {
                    String key = ((RxExtraValue) anno).key();
                    if (key.length() == 0) {
                        key = ((RxExtraValue) anno).value();
                    }
                    if (BuildConfig.DEBUG && key.length() <= 0) {
                        throw new AssertionError("ExtraValue key must be not null !");
                    }
                    RxBundleUtils.putObject(bundle, key, arg);
                }else if (anno instanceof RxIntentValue){
                    boolean ignoreNull = ((RxIntentValue) anno).ignoreNull();
                    if (arg != null || !ignoreNull) {
                        RxIntentType type = ((RxIntentValue) anno).value();
                        if (type == RxIntentType.None){
                            type = ((RxIntentValue) anno).type();
                        }
                        switch (type){
                            case Data:
                                intent.setData((Uri) arg);
                                break;
                            case Type:
                                intent.setType((String) arg);
                                break;
                            case Flags:
                                intent.setFlags((int) arg);
                                break;
                            case Action:
                                intent.setAction((String) arg);
                                break;
                            case Component:
                                intent.setComponent((ComponentName) arg);
                                break;
                            case Identifier:
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                    intent.setIdentifier((String) arg);
                                }
                                break;
                            case PackageName:
                                intent.setPackage((String) arg);
                                break;
                            case None:
                                throw new IllegalArgumentException("Please set the intent type");
                        }
                    }
                }
            }
            intent.putExtras(bundle);
            if (arg instanceof ActivityOptions){
                activityOptions = (ActivityOptions) arg;
            }
        }
    }

    private void loadIntentOptionsInfo() {
        if (options != null){
            int flags = options.flags();
            if (flags >= 0){
                intent.setFlags(flags);
            }
            String action = options.action();
            if (action.length() > 0){
                intent.setAction(action);
            }
            String identifier = options.identifier();
            if (identifier.length() > 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                intent.setIdentifier(identifier);
            }
            String packageName = options.packageName();
            if (packageName.length() > 0){
                intent.setPackage(packageName);
            }
            String type = options.type();
            if (type.length() > 0){
                intent.setType(type);
            }
        }
    }

    private void loadTrickActivityInfo() {
        if (rxActivity != null){
            if (!rxActivity.clazz().equals(RxNull.class)){
                intent.setClass(activity, rxActivity.clazz());
            }else if (rxActivity.className().length() > 0){
                intent.setClassName(activity, rxActivity.className());
            }else if (rxActivity.path().length() > 0){
                String path = rxActivity.path();
                if (pathMap.containsKey(path)){
                    intent.setClassName(activity, pathMap.get(path));
                }else {
                    String className = IRxNavigation.PACKAGE_NAME + "." + RxPackageGenUtils.getClassName(path);
                    try {
                        IRxNavigation activityPath = (IRxNavigation) Class.forName(className).newInstance();
                        String activityClass = activityPath.getRealClass();
                        pathMap.put(path, activityClass);
                        intent.setClassName(activity, activityClass);
                    } catch (Exception e) {
                        Log.e(TAG, "loadTrickActivityInfo: 获取ActivityClass 失败: ", e);
                    }
                }
            }
            enterAnim = rxActivity.enterAnim();
            exitAnim = rxActivity.exitAnim();
        }
    }
}
