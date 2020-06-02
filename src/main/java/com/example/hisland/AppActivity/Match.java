package com.example.hisland.AppActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hisland.Adapter.MyCountTimer;
import com.example.hisland.R;
import com.example.hisland.Service.Matchservice;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Timer;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Match extends AppCompatActivity  {
    private  static String match_user = "";
    private static String name="";
    private TextView text;
    private Timer timer;
    private int flag = 0;
    private static Matchservice ms = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        text = findViewById(R.id.match_status);
        Intent getintent = getIntent();
        Button btnCountTimer= (Button) findViewById(R.id.btnCountTimer);
        //倒计时总时间为10S，时间防止从9s开始显示
        MyCountTimer myCountTimer = new MyCountTimer(16000, 1000, btnCountTimer, "重新倒计时");
        myCountTimer.start();

        text.setText("匹配中...");
        name = getintent.getStringExtra("name");
        post();
       }


    private  int post(){
        new Thread() {
            @Override
            public void run() {
                //Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                JSONObject json = new JSONObject();
                try {
                    json.put("username", name);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //申明给服务端传递一个json串
                //创建一个OkHttpClient对象
                OkHttpClient okHttpClient = new OkHttpClient();
                //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
                //json为String类型的json数据
                RequestBody requestBody = RequestBody.create(JSON,String.valueOf(json));
                Request request = new Request.Builder()
                        .url("xxxx")
                        .post(requestBody)
                        .build();

                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        //DialogUtils.showPopMsgInHandleThread(Release_Fragment.this.getContext(), mHandler, "数据获取失败，请重新尝试！");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String string = response.body().string();//获得response的内容并转为string
                        try {
                            JSONObject json = new JSONObject(string);//将获得的内容转为json格式

                            Match.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    try {
                                        if (json.getInt("code")==200&&!name.equals(json.getString("username"))){   //状态码200，并且连接池里不是自己就匹配成功
                                            text.setText("匹配成功");
                                            flag = 1;
                                            match_user = json.getString("username");
                                            Intent match = new Intent(Match.this,ChatwithFriend.class);
                                            match.putExtra("name",match_user);
                                            startActivity(match);
                                            finish();
                                        }else{
                                            flag = -1;
                                            text.setText("匹配中...");
//                                            String u = ms.getMatchuser();
//                                            if(!"".equals(u)){
//                                                text.setText("匹配成功");
//                                                Intent match = new Intent(Match.this,ChatwithFriend.class);
//                                                match.putExtra("name",u);
//                                                startActivity(match);
//                                                ms.deleteMatch();
//                                            }else {
//                                                flag = -1;
//                                                text.setText("匹配失败");
//                                            }

                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }.start();
    return flag;
    }
}
