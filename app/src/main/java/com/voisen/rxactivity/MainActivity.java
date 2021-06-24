package com.voisen.rxactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

    EditText editText;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.et_value);
    }

    public void toAbout(View view) {
        Disposable disposable = App.activitys.goAbout(editText.getText().toString()).subscribe((intent) -> {
            editText.setText(intent.getStringExtra("date"));
        });
        compositeDisposable.add(disposable);
    }

    public void toSetting(View view) {
        Disposable disposable = App.activitys.goImplicit(this.getPackageName()).subscribe((intent) -> {
            showToast(intent.getStringExtra("value"));
        });
        compositeDisposable.add(disposable);
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
        compositeDisposable.clear();
        super.onDestroy();
    }

    public void showImg(View view) {
        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(this, findViewById(R.id.iv_img), "iv_img");
        App.activitys.showImage(activityOptions);
    }
}