package com.voisen.rxactivity;

import android.app.ActivityOptions;
import android.content.Intent;

import com.voisen.rxactivity.anno.ExtraValue;
import com.voisen.rxactivity.anno.IntentOptions;
import com.voisen.rxactivity.anno.IntentValue;
import com.voisen.rxactivity.anno.TrickActivity;
import com.voisen.rxactivity.full.FullscreenActivity;
import com.voisen.rxactivity.image.ImageActivity;

public interface ActivityService {

    /**
     * 指定跳转的Activity为`AboutActivity`
     * @param value 传递的参数
     */
    @TrickActivity(className = "com.voisen.rxactivity.about.AboutActivity")
    RxObserve<Intent> goAbout(@ExtraValue("value") String value);

    /**
     * 隐式跳转到指定的Action, @IntentOptions 优先级 要小于 @IntentValue 优先级
     * @param pkgName 传入包名, 相当与调用 intent.setPackageName(pkgName)
     */
    @IntentOptions(action = "rxactivity_test")
    RxObserve<Intent> goImplicit(@IntentValue(IntentType.PackageName) String pkgName);

    /**
     * 指定跳转Activity, 且无回传参数
     */
    @TrickActivity(clazz = FullscreenActivity.class)
    void goFullscreen();

    /**
     * 指定跳转Activity, 并指定跳转的`ActivityOptions`
     */
    @TrickActivity(clazz = ImageActivity.class)
    void showImage(ActivityOptions options);

}
