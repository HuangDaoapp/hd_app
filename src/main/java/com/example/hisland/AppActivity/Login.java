package com.example.hisland.AppActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hisland.R;
import com.example.hisland.Service.UserinfoService;
import com.example.hisland.UsingClass.WordReplacement;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;

public class Login extends AppCompatActivity {
    boolean cansee = false;
    EditText name, password;
    Button login;
    TextView register,forget_password;
    ImageView imageView;
    CheckBox checkBox;
    private UserinfoService userinfoService = UserinfoService.getUserinfoService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //定义全屏参数
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //设置当前窗体为全屏显示
        getWindow().setFlags(flag, flag);
        setContentView(R.layout.activity_login);
//初始化
        EMOptions options = new EMOptions();
        options.setAcceptInvitationAlways(false);//默认不同意好友请求
        EMClient.getInstance().init(this, options);
        EMClient.getInstance().setDebugMode(true);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }


        name = findViewById(R.id.username);
        password = findViewById(R.id.userpassword);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        imageView=findViewById(R.id.show_password);
        checkBox=findViewById(R.id.remember_password);
        forget_password=findViewById(R.id.forget_password);

        //密码可见不可见
        imageView.setOnClickListener(new View.OnClickListener() {//监听按钮
            @Override
            public void onClick(View v) {
                if (cansee == false) {
                    //如果是不能看到密码的情况下
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.kejian));
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    password.setSelection(password.getText().toString().length());
                    cansee = true;
                } else {
                    //如果是能看到密码的状态下
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.bukejian));
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    password.setSelection(password.getText().toString().length());
                    cansee = false;
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox.isChecked()){
                    String usr=name.getText().toString();
                    String pwd=password.getText().toString();
                    memInfo(usr,pwd);
                    password.setTransformationMethod(new WordReplacement());//调用隐藏函数
                }else{
                    SharedPreferences.Editor et=getSharedPreferences("data",0).edit();
                    et.clear();
                    et.commit();
                }
                //连接数据库
                if (name.getText().toString().equals("") || password.getText().toString().equals("")) {
                    Toast.makeText(Login.this, "登录异常,账号或密码不能为空！", Toast.LENGTH_SHORT).show();
                } else {
                    //开启一个线程
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            boolean result = userinfoService.login(name.getText().toString(), password.getText().toString());
                            String signaturefirst=userinfoService.select_signature(name.getText().toString());
                            if (result) {
                                EMClient.getInstance().login(name.getText().toString(),password.getText().toString(),new EMCallBack() {//回调
                                    @Override
                                    public void onSuccess() {
                                        EMClient.getInstance().groupManager().loadAllGroups();
                                        EMClient.getInstance().chatManager().loadAllConversations();
                                        Log.d("main", "登录聊天服务器成功！");
                                    }

                                    @Override
                                    public void onProgress(int progress, String status) {

                                    }

                                    @Override
                                    public void onError(int code, String message) {
                                        Log.d("main", "登录聊天服务器失败！");
                                    }
                                });
                                Intent intent = new Intent(Login.this, Main.class);
                                intent.putExtra("name", name.getText().toString());
                                intent.putExtra("signaturefirst", signaturefirst);
                                startActivity(intent);
                                finish();
                            } else {
                                if(Looper.myLooper()==null){
                                    Looper.prepare();
                                }
                                Toast.makeText(Login.this, "密码或账号错误", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                        }
                    }).start();
                }
            }
        });
        restoreInfo();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });
        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String pass=userinfoService.getpassword(name.getText().toString());
                        if(Looper.myLooper()==null){
                            Looper.prepare();
                        }
                        Toast.makeText(Login.this, pass, Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                }).start();
            }
        });
    }
    private void memInfo(String usr,String pwd){
        SharedPreferences.Editor editor=getSharedPreferences("data",0).edit();
        editor.putString("name",usr);
        editor.putString("password",pwd);
        editor.commit();
    }
    private void restoreInfo(){
        SharedPreferences sharedPreferences=getSharedPreferences("data",0);
        name.setText(sharedPreferences.getString("name",""));
        password.setText(sharedPreferences.getString("password",""));
        password.setTransformationMethod(new WordReplacement());
    }
}
