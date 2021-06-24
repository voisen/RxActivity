package com.voisen.rxactivity;

import android.app.ActivityOptions;
import android.content.Intent;

import com.voisen.rxactivity.anno.ExtraValue;
import com.voisen.rxactivity.anno.IntentOptions;
import com.voisen.rxactivity.anno.IntentValue;
import com.voisen.rxactivity.anno.TrickActivity;

import io.reactivex.rxjava3.core.Observable;

public interface MyActivity {

    @TrickActivity(clazz = AboutActivity.class)
    Observable<Intent> goAbout(@ExtraValue("value") String value);

    @IntentOptions(action = "rxactivity_test")
    Observable<Intent> goImplicit(@IntentValue(IntentType.PackageName) String pkgName);

    @TrickActivity(clazz = FullscreenActivity.class)
    void goFullscreen();

    @TrickActivity(clazz = ImageActivity.class)
    void showImage(ActivityOptions options);

}
