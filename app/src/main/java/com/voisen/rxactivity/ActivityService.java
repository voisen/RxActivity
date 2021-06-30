package com.voisen.rxactivity;

import android.app.ActivityOptions;
import android.content.Intent;

import com.voisen.rxactivity.anno.RxExtraValue;
import com.voisen.rxactivity.anno.RxIntentOptions;
import com.voisen.rxactivity.anno.RxIntentValue;
import com.voisen.rxactivity.anno.RxActivity;
import com.voisen.rxactivity.full.FullscreenActivity;
import com.voisen.rxactivity.image.ImageActivity;

public interface ActivityService {

    /**
     * 指定跳转的Activity为`AboutActivity`
     * @param value 传递的参数
     */
    @RxActivity(path = "app/about")
    RxActivityObserve<Intent> goAbout(@RxExtraValue("value") String value);

    /**
     * 隐式跳转到指定的Action, @IntentOptions 优先级 要小于 @IntentValue 优先级
     * @param pkgName 传入包名, 相当与调用 intent.setPackageName(pkgName)
     */
    @RxIntentOptions(action = "rxactivity_test")
    RxActivityObserve<Intent> goImplicit(@RxIntentValue(RxIntentType.PackageName) String pkgName);

    /**
     * 指定跳转Activity, 且无回传参数
     */
    @RxActivity(clazz = FullscreenActivity.class)
    void goFullscreen();

    /**
     * 指定跳转Activity, 并指定跳转的`ActivityOptions`
     */
    @RxActivity(clazz = ImageActivity.class)
    void showImage(ActivityOptions options);


    @RxActivity(path = "app2lib/login")
    void goLogin();

}
