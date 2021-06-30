package com.voisen.rxactivity;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.voisen.rxactivity.anno.RxGreenChannel;
import com.voisen.rxactivity.anno.RxIntentOptions;
import com.voisen.rxactivity.anno.RxActivity;
import com.voisen.rxactivity.anno.RxFragment;
import com.voisen.rxactivity.entity.RxIntent;
import com.voisen.rxactivity.utils.RxActivityUtils;
import com.voisen.rxactivity.lifecycle.RxActivityLifecycle;
import com.voisen.rxactivity.utils.RxNull;
import com.voisen.rxprocessor.interfaces.IRxNavigation;
import com.voisen.rxprocessor.utils.RxPackageGenUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public final class RxNavigation implements InvocationHandler {

    private static final String TAG = "RxActivity";

    private static final RxActivityLifecycle mActivityLifecycle = new RxActivityLifecycle();

    private RxActivityInterceptor interceptor;

    private static  Application mApplication;
    private static final RxNavigation sharedInstance = new RxNavigation();

    public static RxNavigation shared(){
        return sharedInstance;
    }

    public RxNavigation setInterceptor(RxActivityInterceptor interceptor) {
        this.interceptor = interceptor;
        return this;
    }

    public static synchronized void init(Application application){
        mApplication = application;
        application.unregisterActivityLifecycleCallbacks(mActivityLifecycle);
        application.registerActivityLifecycleCallbacks(mActivityLifecycle);
    }

    @SuppressWarnings("unchecked")
    public  <T> T create(Class<T> service){
        return (T) Proxy.newProxyInstance(service.getClassLoader(),
                new Class[]{service},
                this
        );
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable{
        if (mApplication == null){
            throw new RuntimeException("请先调用初始化方法. (RxActivity.init(Application))");
        }
        RxFragment rxFragment = method.getAnnotation(RxFragment.class);
        RxActivity rxActivity = method.getAnnotation(RxActivity.class);
        RxIntentOptions rxIntentOptions = method.getAnnotation(RxIntentOptions.class);
        RxGreenChannel greenChannel = method.getAnnotation(RxGreenChannel.class);
        if (rxFragment != null){
            return handlerFragment(method, args, rxFragment);
        }
        FragmentActivity activity = (FragmentActivity) mActivityLifecycle.topActivity();
        RxIntent rxIntent = new RxIntent(activity, rxActivity, rxIntentOptions, method, args);
        try{
            rxIntent.initialization();

            if (greenChannel != null && interceptor != null && interceptor.shouldInterceptorIntent(activity, rxIntent.getIntent())){
                return RxActivityObserve.returnError(new Throwable("跳转已被拦截器拦截: " + interceptor.getClass()));
            }
            if (method.getReturnType().equals(Intent.class)){
                return rxIntent.getIntent();
            }

            RxActivityObserve<Intent> observe = RxActivityUtils.startActivity(activity,
                    rxIntent.getIntent(),
                    rxIntent.getEnterAnim(),
                    rxIntent.getExitAnim(),
                    rxIntent.getActivityOptions(),
                    rxIntent.isObservable());
            if (observe == null){
                return null;
            }
            return observe;
        } catch (Exception e){
            Log.e(TAG, "Error: ", e);
            return RxActivityObserve.returnError(e);
        }
    }

    private Object handlerFragment(Method method, Object[] args, RxFragment rxFragment) {
        try {
            Class<?> fClass = null;
            if (rxFragment.className().length() > 0){
                fClass = Class.forName(rxFragment.className());
            }else if(!rxFragment.clazz().equals(RxNull.class)){
                fClass = rxFragment.clazz();
            }else if (rxFragment.path().length() > 0){
                String s = IRxNavigation.PACKAGE_NAME + "." + RxPackageGenUtils.getClassName(rxFragment.path());
                fClass = Class.forName(s);
            }
            if (fClass != null){
                return fClass.newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
