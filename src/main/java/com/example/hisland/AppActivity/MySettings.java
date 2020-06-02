package com.example.hisland.AppActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hisland.R;
import com.example.hisland.Service.UserinfoService;

public class MySettings extends AppCompatActivity {
    private Button logout, zhuxiao;
    UserinfoService userinfoService = UserinfoService.getUserinfoService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_settings);
        logout = findViewById(R.id.logout);
        zhuxiao = findViewById(R.id.zhuxiao);
        Intent getintent = getIntent();
        String name = getintent.getStringExtra("name");
        zhuxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MySettings.this);
                builder.setTitle("危险操作警告");
                builder.setMessage("您确定要注销此账户吗？");
                builder.setCancelable(false);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                userinfoService.delUserData(name);
                                startActivity(new Intent(MySettings.this, Login.class));
                            }
                        }).start();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.show();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MySettings.this);
                builder.setTitle("提示！");
                builder.setMessage("您确定要退出登录吗？");
                builder.setCancelable(false);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(MySettings.this, Login.class));
                            }
                        }).start();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.show();
            }
        });
    }
}
