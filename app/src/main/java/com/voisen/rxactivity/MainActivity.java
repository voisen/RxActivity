package com.voisen.rxactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.os.Bundle;
import android.view.View;
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
        App.activitys.goAbout(editText.getText().toString())
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
        App.activitys.goImplicit(this.getPackageName()).then((intent) -> {
            showToast(intent.getStringExtra("value"));
        });
    }

    public void toFull(View view) {
        App.activitys.goFullscreen();
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
        App.activitys.showImage(activityOptions);
    }
}