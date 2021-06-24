package com.voisen.rxactivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.voisen.rxactivity.anno.IntentOptions;
import com.voisen.rxactivity.anno.TrickActivity;
import com.voisen.rxactivity.utils.ActivityUtils;
import com.voisen.rxactivity.anno.ExtraValue;
import com.voisen.rxactivity.lifecycle.ActivityLifecycle;
import com.voisen.rxactivity.utils.BundleUtils;
import com.voisen.rxactivity.anno.IntentValue;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public final class RxActivity implements InvocationHandler {

    private static final ActivityLifecycle mActivityLifecycle = new ActivityLifecycle();

    private final List<RxActivityInterceptor> interceptors = new ArrayList<>();

    private static  Application mApplication;

    public static void init(Application application){
        mApplication = application;
        application.unregisterActivityLifecycleCallbacks(mActivityLifecycle);
        application.registerActivityLifecycleCallbacks(mActivityLifecycle);
    }

    @SuppressWarnings("unchecked")
    private <T> T create(Class<T> service){
        return (T) Proxy.newProxyInstance(service.getClassLoader(),
                new Class[]{service},
                this
        );
    }


    private void onErrorOccurred(Activity activity, Intent intent, Throwable throwable){
        if (interceptors.size() == 0){
            throwable.printStackTrace();
            return;
        }
        for (RxActivityInterceptor interceptor : interceptors) {
            interceptor.onErrorOccurred(activity, intent, throwable);
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable{
        FragmentActivity topActivity = null;
        Intent intent = null;
        if (mApplication == null){
            throw new RuntimeException("Please call the initialization method first. (RxActivity.init(Application))");
        }
        try{
            boolean observable = !void.class.isAssignableFrom(method.getReturnType());
            int enterAnim = -1;
            int exitAnim = -1;
            TrickActivity trickActivity = method.getAnnotation(TrickActivity.class);
            IntentOptions intentOptions = method.getAnnotation(IntentOptions.class);
            topActivity = (FragmentActivity) mActivityLifecycle.topActivity();
            intent = new Intent();
            if (trickActivity != null){
                if (!trickActivity.clazz().equals(FragmentActivity.class)){
                    intent.setClass(topActivity, trickActivity.clazz());
                }else if (trickActivity.className().length() > 0){
                    intent.setClassName(topActivity, trickActivity.className());
                }
                enterAnim = trickActivity.enterAnim();
                exitAnim = trickActivity.exitAnim();
            }
            if (intentOptions != null){
                int flags = intentOptions.flags();
                if (flags >= 0){
                    intent.setFlags(flags);
                }
                String action = intentOptions.action();
                if (action.length() > 0){
                    intent.setAction(action);
                }
                String identifier = intentOptions.identifier();
                if (identifier.length() > 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                    intent.setIdentifier(identifier);
                }
                String packageName = intentOptions.packageName();
                if (packageName.length() > 0){
                    intent.setPackage(packageName);
                }
                String type = intentOptions.type();
                if (type.length() > 0){
                    intent.setType(type);
                }
            }
            ActivityOptions activityOptions = null;
            Bundle bundle = new Bundle();
            Annotation[][] annotations = method.getParameterAnnotations();
            for (int i = 0; i < annotations.length; i++) {
                Annotation[] annos = annotations[i];
                Object arg = args[i];
                for (Annotation anno : annos) {
                    if (anno instanceof ExtraValue) {
                        String key = ((ExtraValue) anno).key();
                        if (key.length() == 0) {
                            key = ((ExtraValue) anno).value();
                        }
                        if (BuildConfig.DEBUG && key.length() <= 0) {
                            throw new AssertionError("ExtraValue key must be not null !");
                        }
                        BundleUtils.putObject(bundle, key, arg);
                    }else if (anno instanceof IntentValue){
                        boolean ignoreNull = ((IntentValue) anno).ignoreNull();
                        if (arg != null || !ignoreNull) {
                            IntentType type = ((IntentValue) anno).value();
                            if (type == IntentType.None){
                                type = ((IntentValue) anno).type();
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
            for (RxActivityInterceptor interceptor : interceptors) {
                boolean b = interceptor.shouldInterceptorIntent(topActivity, intent);
                if (b){
                    onErrorOccurred(topActivity, intent, new InterruptedException("Intent is to intercept !"));
                    return Observable.empty();
                }
                intent = interceptor.overrideIntent(topActivity, intent);
            }

            if (intent == null){
                onErrorOccurred(topActivity, intent, new NullPointerException("Intent cannot be null !"));
                return Observable.empty();
            }

            FragmentActivity finalTopActivity = topActivity;
            return ActivityUtils.startActivity(topActivity, intent, enterAnim, exitAnim, activityOptions, observable).map(intent1 -> {
                Object object = intent1;
                for (RxActivityInterceptor interceptor : interceptors) {
                    object = interceptor.overrideResultData(finalTopActivity, intent1);
                }
                return object;
            });
        } catch (Exception e){
            if (topActivity == null) {
                e.printStackTrace();
            }else {
                onErrorOccurred(topActivity, intent, e);
            }
            return Observable.empty();
        }
    }

    public static final class Builder{

        private final List<RxActivityInterceptor> interceptors = new ArrayList<>();

        public Builder addInterceptor(RxActivityInterceptor interceptor){
            interceptors.add(interceptor);
            return this;
        }

        public<T> T create(Class<T> service){
            RxActivity activity = new RxActivity();
            activity.interceptors.addAll(this.interceptors);
            return activity.create(service);
        }
    }
}
