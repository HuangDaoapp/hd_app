package com.example.hisland.AppActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hisland.R;
import com.example.hisland.Service.UserinfoService;

public class ChangeSignature extends AppCompatActivity {
    EditText signature;
    Button change;
    UserinfoService userinfoService=UserinfoService.getUserinfoService();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_signature);
        signature=findViewById(R.id.signature);
        change=findViewById(R.id.change);
        Intent getintent = getIntent();
        String name = getintent.getStringExtra("name");
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        userinfoService.change_signature(name,signature.getText().toString());
                        Intent intent=new Intent();
                        intent.putExtra("user_quanju",name);
                        intent.putExtra("signature",signature.getText().toString());
                        setResult(RESULT_OK,intent);
                        finish();
                        if(Looper.myLooper()==null){
                            Looper.prepare();
                        }
                        Toast.makeText(ChangeSignature.this, "修改成功", Toast.LENGTH_SHORT).show();
                        Looper.loop();

                    }
                }).start();
            }
        });
    }
}
