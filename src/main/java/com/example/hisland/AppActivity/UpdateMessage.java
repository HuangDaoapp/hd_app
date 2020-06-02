package com.example.hisland.AppActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hisland.Bean.Userinfo;
import com.example.hisland.R;
import com.example.hisland.Service.UserinfoService;

public class UpdateMessage extends AppCompatActivity {
    private EditText age, number, occupation, location, friend_card;
    private Button subbmit;
    private int user_age;
    UserinfoService userinfoService = UserinfoService.getUserinfoService();
    Userinfo userinfo1 = new Userinfo();
    String a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_message);

        age = findViewById(R.id.age);
        number = findViewById(R.id.number);
        occupation = findViewById(R.id.occupation);
        location = findViewById(R.id.location);
        friend_card = findViewById(R.id.friend_card);
        subbmit = findViewById(R.id.subbmit);

        Intent getintent = getIntent();
        String name = getintent.getStringExtra("name");

        new Thread(new Runnable() {
            @Override
            public void run() {
                userinfo1 = userinfoService.getUserbyId(name);
                Handler main=new Handler(Looper.getMainLooper());
                main.post(new Runnable() {
                    @Override
                    public void run() {
                        a = String.valueOf(userinfo1.getUser_age());
                        age.setText(a);
                        number.setText(userinfo1.getUser_phonenumber());
                        occupation.setText(userinfo1.getUser_occupation());
                        location.setText(userinfo1.getUser_location());
                        friend_card.setText(userinfo1.getUser_card());
                    }
                });
            }
        }).start();

        subbmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Userinfo userinfo = new Userinfo();
                userinfo.setUser_nickname(name);
                user_age = Integer.parseInt(age.getText().toString());
                userinfo.setUser_age(user_age);
                userinfo.setUser_phonenumber(number.getText().toString());
                userinfo.setUser_occupation(occupation.getText().toString());
                userinfo.setUser_location(location.getText().toString());
                userinfo.setUser_card(friend_card.getText().toString());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        userinfoService.UpdateMessage(userinfo);
                        finish();
                        if (Looper.myLooper() == null) {
                            Looper.prepare();
                        }
                        Toast.makeText(UpdateMessage.this, "修改成功！", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                }).start();
            }
        });
    }
}
