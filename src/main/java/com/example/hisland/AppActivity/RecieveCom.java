package com.example.hisland.AppActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hisland.Adapter.commentAdapter;
import com.example.hisland.Bean.Commentinfo;
import com.example.hisland.Bean.Comunity;
import com.example.hisland.R;
import com.example.hisland.Service.CommentinfoService;
import com.example.hisland.Service.CommunityService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecieveCom extends AppCompatActivity {
    boolean cansee=false;
    EditText pinglun;
    Commentinfo commentinfo =new Commentinfo();
    Button fasong;
    ImageView dianzan;
    private TextView rec_cname,rec_cinfo,rec_cuser,useee;
    TextView titleuser;
    private List<Commentinfo> commetList = new ArrayList<>();
    CommentinfoService commentinfoService = CommentinfoService.commentinfoService();
    CommunityService communityService=CommunityService.getCommunityService();
    private RecyclerView rv;
    private commentAdapter CommentAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recievecom);
        rv = findViewById(R.id.pinglun);
        pinglun=findViewById(R.id.input_pinglun);
        fasong=findViewById(R.id.fasong);
        dianzan=findViewById(R.id.dianzan);
        initView();
        getinfo();
        inittype();
        dianzan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cansee == false) {
                    dianzan.setImageDrawable(getResources().getDrawable(R.drawable.dianzan1));
                    cansee = true;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Comunity comunity=new Comunity();
                            comunity.setName(rec_cname.getText().toString());
                            comunity.setInfo(rec_cinfo.getText().toString());
                            comunity.setUser(rec_cuser.getText().toString());
                            comunity.setType("y");
                            communityService.addComunity1(comunity);
                            //communityService.changetype(1,rec_cuser.getText().toString(),rec_cinfo.getText().toString());
                        }
                    }).start();
                } else {
                    dianzan.setImageDrawable(getResources().getDrawable(R.drawable.dianzan));
                    cansee = false;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            communityService.deleteComunity1(rec_cuser.getText().toString(),rec_cname.getText().toString());
                        }
                    }).start();
                }
            }
        });
        fasong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String time=df.format(new Date());
                        commentinfo.setTitle(rec_cname.getText().toString());
                        commentinfo.setDtuser(rec_cuser.getText().toString());
                        commentinfo.setUser(useee.getText().toString()+' '+time);
                        commentinfo.setInfo(pinglun.getText().toString());
                        commentinfoService.publishComment(commentinfo);
                    }
                }).start();
                commetList.add(commentinfo);
                CommentAdapter.notifyItemInserted(commetList.size() - 1);//当有新消息时刷新ListView中的显示
                rv.scrollToPosition(commetList.size() - 1);//将ListView定位到最后一行
                pinglun.setText("");//清空输入框中的内容

            }
        });
            }

    private void inittype() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String i=communityService.selecttype(rec_cuser.getText().toString(),rec_cname.getText().toString());
                Handler main=new Handler(Looper.getMainLooper());
                main.post(new Runnable() {
                    @Override
                    public void run() {
                        if(i!=null){
                            if(i.equals("y")){
                                dianzan.setImageDrawable(getResources().getDrawable(R.drawable.dianzan1));
                            }else{
                                rec_cuser.setText(rec_cuser.getText().toString());
                            }
                        }

                    }
                });
            }
        }).start();
    }

    private void getinfo() {
        Intent intent = getIntent();
        String name = intent.getStringExtra("username");
        String info = intent.getStringExtra("info");
        String user_own = intent.getStringExtra("onw");
        String use=intent.getStringExtra("use");
        rec_cname.setText(name);
        rec_cinfo.setText(info);
        rec_cuser.setText(user_own);
        useee.setText(use);
        initView();

        Refresh();
    }

    private void initView() {
        rec_cname = findViewById(R.id.rec_cname);
        rec_cinfo = findViewById(R.id.rec_cinfo);
        rec_cuser = findViewById(R.id.rec_cuser);
        useee     =findViewById(R.id.useee);
//        reccom_collect = findViewById(R.id.reccom_collect);
//        com_back = findViewById(R.id.com_back);
    }
    private void Refresh() {
        commetList = new ArrayList<>();
        initcomment();
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(layoutManager);

        CommentAdapter = new commentAdapter(this,commetList);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(CommentAdapter);
    }

    private void initcomment() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Commentinfo> list_comment = commentinfoService.getComment(rec_cuser.getText().toString(),rec_cname.getText().toString());
                Handler mainhandler=new Handler(Looper.getMainLooper());
                mainhandler.post(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < list_comment.size(); i++) {
                            Commentinfo com = list_comment.get(i);
                            commetList.add(com);
                            CommentAdapter.notifyItemInserted(commetList.size()-1);
                        }
                    }
                });


            }
        }).start();

    }

}
