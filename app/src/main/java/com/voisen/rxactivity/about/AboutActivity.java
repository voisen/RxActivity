package com.voisen.rxactivity.about;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.voisen.rxactivity.R;
import com.voisen.rxactivity.annotations.RxAutowired;
import com.voisen.rxactivity.annotations.RxPath;
import com.voisen.rxactivity.annotations.RxSaveState;

import java.text.SimpleDateFormat;
import java.util.Date;

@RxPath("app/about")
public class AboutActivity extends AppCompatActivity {

    @RxAutowired
    String value;

    @RxSaveState
    String time;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private TextView textView;
    private TextView timeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTitle("About");
        timeView = findViewById(R.id.tv_time);
        textView = findViewById(R.id.tv_name);
        initView();
    }

    private void initView() {
        textView.setText(value);
        timeView.setText(time);
    }

    public void takeTime(View view) {
        time = dateFormat.format(new Date());
        timeView.setText(time);
        showToast("设置成功 界面旋转后状态会自动保存");
    }

    public void goBack(View view) {
        showToast("时间信息已回传");
        Intent data = new Intent();
        data.putExtra("date", time);
        setResult(RESULT_OK, data);
        finish();
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

}