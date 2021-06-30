package com.voisen.rxactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ActivityOptions;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.et_value);
    }

    public void toAbout(View view) {
        RxNavigation.shared().create(ActivityService.class).goAbout(editText.getText().toString())
                .map(data-> data.getStringExtra("date"))
                .then(new RxListener<String>() {
                    @Override
                    public void onResult(String data) {
                        editText.setText(data);
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast("出错:" + e.getLocalizedMessage());
                    }
                });
    }

    public void toSetting(View view) {
        RxNavigation.shared().create(ActivityService.class).goImplicit(this.getPackageName()).then((intent) -> {
            showToast(intent.getStringExtra("value"));
        });
    }

    public void toFull(View view) {
        RxNavigation.shared().create(ActivityService.class).goFullscreen();
    }

    Toast toast = null;
    public void showToast(String msg){
        if (toast != null){
            toast.cancel();
        }
        toast = Toast.makeText(this, "", Toast.LENGTH_LONG);
        toast.setText(msg);
        toast.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void showImg(View view) {
        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(this, findViewById(R.id.iv_img), "iv_img");
        RxNavigation.shared().create(ActivityService.class).showImage(activityOptions);
    }

    public void toLogin(View view) {
        RxNavigation.shared().create(ActivityService.class).goLogin();
    }

    public void showFragment(View view) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag("FRAGMENT");
        if (fragment == null) {
            fragment = RxNavigation.shared().create(ActivityService.class)
                    .messageFragment();
            transaction.add(R.id.content, fragment, "FRAGMENT");
        }
        transaction.show(fragment);
        transaction.commitNowAllowingStateLoss();
    }
}