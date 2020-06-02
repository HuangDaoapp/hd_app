package com.example.hisland.AppActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hisland.Bean.Userinfo;
import com.example.hisland.R;
import com.example.hisland.Service.UserinfoService;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.exceptions.HyphenateException;

public class Register extends AppCompatActivity {
    private EditText username, userpassword;
    private RadioButton gender_man, gender_girl;
    private RadioGroup gender_group;
    private Button register;
    private UserinfoService userinfoService = UserinfoService.getUserinfoService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //定义全屏参数
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //设置当前窗体为全屏显示
        getWindow().setFlags(flag, flag);
        setContentView(R.layout.activity_register);
        //初始化
        EMOptions options = new EMOptions();
        options.setAcceptInvitationAlways(false);
        EMClient.getInstance().init(this, options);
        EMClient.getInstance().setDebugMode(true);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        username = findViewById(R.id.username);
        userpassword = findViewById(R.id.userpassword);
        register = findViewById(R.id.register);
        gender_group = findViewById(R.id.gender_group);
        gender_man = findViewById(R.id.man_user);
        gender_girl = findViewById(R.id.girl_user);
        Userinfo userinfo = new Userinfo();
        gender_man.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                userinfo.setUser_gender(gender_man.getText().toString().trim());
            }
        });
        gender_girl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                userinfo.setUser_gender(gender_girl.getText().toString().trim());
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userinfo.setUser_nickname(username.getText().toString().trim());
                userinfo.setUser_password(userpassword.getText().toString().trim());

                if (username.getText().toString().equals("") || userpassword.getText().toString().equals("")) {
                    Toast.makeText(Register.this, "用户名及密码不能为空", Toast.LENGTH_SHORT).show();
                } else if(!gender_man.isChecked()&&!gender_girl.isChecked()){
                    Toast.makeText(Register.this, "请选择您的性别", Toast.LENGTH_SHORT).show();
                }else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            boolean result = userinfoService.check(username.getText().toString());
                            if (result) {
                                if (Looper.myLooper() == null) {
                                    Looper.prepare();
                                }
                                Toast.makeText(Register.this, "用户已存在", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            } else {
                                userinfoService.InsertUserinfo(userinfo);
                                try {
                                    EMClient.getInstance().createAccount(username.getText().toString(), userpassword.getText().toString());//同步方法
                                } catch (HyphenateException e) {
                                    e.printStackTrace();
                                }
                                startActivity(new Intent(Register.this, Login.class));
                                if (Looper.myLooper() == null) {
                                    Looper.prepare();
                                }
                                Toast.makeText(Register.this, "注册成功", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                        }
                    }).start();

                }
            }
        });
    }



}
