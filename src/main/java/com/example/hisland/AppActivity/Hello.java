package com.example.hisland.AppActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.hisland.R;

import java.util.Timer;
import java.util.TimerTask;

public class Hello extends AppCompatActivity implements View.OnClickListener {
    private int reclen = 5;
    private TextView textView;
    Timer timer = new Timer();
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //定义全屏参数
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //设置当前窗体为全屏显示
        getWindow().setFlags(flag, flag);
        setContentView(R.layout.activity_hello);
        initView();
        timer.schedule(task, 1000, 1000);
        //正常情况下不点击跳过
        handler = new Handler();
        handler.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                //从欢迎界面跳转到首界面
                Intent intent = new Intent(Hello.this, Login.class);
                startActivity(intent);
                finish();
            }
        }, 5000);//延迟三秒后发送handle消息
    }

    private void initView() {
        textView = findViewById(R.id.jumpover);
        textView.setOnClickListener(this);
    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    reclen--;
                    textView.setText("跳过" + reclen);
                    if (reclen < 0) {
                        timer.cancel();
                        textView.setVisibility(View.GONE);
                    }
                }
            });
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.jumpover:
                //从欢迎界面登录到主界面
                Intent intent = new Intent(Hello.this, Login.class);
                startActivity(intent);
                finish();
                if (runnable != null) {
                    handler.removeCallbacks(runnable);
                }
                break;
            default:
                break;
        }
    }
}