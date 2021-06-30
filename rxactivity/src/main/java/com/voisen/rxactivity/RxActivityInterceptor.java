package com.voisen.rxactivity;

import android.app.Activity;
import android.content.Intent;

public interface RxActivityInterceptor {

    /**
     * 拦截跳转
     * @param fromActivity activity
     * @param intent intent
     * @return true or false 返回true 拦截
     */
    boolean shouldInterceptorIntent(Activity fromActivity, Intent intent);
}
