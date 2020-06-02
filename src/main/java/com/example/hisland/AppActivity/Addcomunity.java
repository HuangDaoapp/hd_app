package com.example.hisland.AppActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hisland.Bean.Comunity;
import com.example.hisland.R;
import com.example.hisland.Service.CommunityService;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Addcomunity extends AppCompatActivity {
    TextView biaoti,neirong;
    Button tijiao;
    private CommunityService communityService=CommunityService.getCommunityService();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcomunity);
        biaoti=findViewById(R.id.biaoti);
        neirong=findViewById(R.id.neirong);
        tijiao=findViewById(R.id.tijiao);
        Intent getintent=getIntent();
        String name=getintent.getStringExtra("name");
        tijiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String time=df.format(new Date());
                        Comunity comunity=new Comunity();
                        comunity.setName(biaoti.getText().toString()+"\n"+time);
                        comunity.setInfo(neirong.getText().toString());
                        comunity.setUser(name);
                        communityService.addComunity(comunity);
                    }
                }).start();
                finish();
            }
        });
    }
}
