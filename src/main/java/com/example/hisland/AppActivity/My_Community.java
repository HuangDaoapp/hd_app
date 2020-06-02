package com.example.hisland.AppActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hisland.Adapter.ChatAdapter;
import com.example.hisland.Bean.Commentinfo;
import com.example.hisland.Bean.Comunity;
import com.example.hisland.R;
import com.example.hisland.Service.CommunityService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class My_Community extends AppCompatActivity {
    private List<Comunity> comunityList = new ArrayList<>();
    CommunityService comunityService = CommunityService.getCommunityService();
    private SwipeMenuRecyclerView myrv;
    private SwipeRefreshLayout srlayout;
    private ChatAdapter chatAdapter;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_community);
        Intent getintent = getIntent();
        name=getintent.getStringExtra("name");
        initView();
        initcomunity();
        myrv.addItemDecoration(new DefaultItemDecoration(Color.GRAY));
        myrv.setSwipeMenuCreator(swipeMenuCreator);
        myrv.setSwipeMenuItemClickListener(swipeMenuItemClickListener);
        chatAdapter = new ChatAdapter(this,comunityList);
        myrv.setLayoutManager(new LinearLayoutManager(this));
        myrv.setAdapter(chatAdapter);
        srlayout.setColorSchemeResources(android.R.color.holo_green_light,android.R.color.holo_red_light,android.R.color.holo_blue_light);
        srlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(700);
                            srlayout.setRefreshing(false);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                Refresh();
            }
        });
    }
    private void Refresh() {
        comunityList = new ArrayList<>();
        initcomunity();
        chatAdapter = new ChatAdapter(this,comunityList);
        myrv.setLayoutManager(new LinearLayoutManager(this));
        myrv.setAdapter(chatAdapter);
    }

    private void initcomunity() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Comunity> list_comunity = comunityService.getCom(name);
                Handler mainhandler=new Handler(Looper.getMainLooper());
                mainhandler.post(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < list_comunity.size(); i++) {
                            Comunity com = list_comunity.get(i);
                            com.setUse(name);
                            comunityList.add(com);
                            chatAdapter.notifyItemInserted(comunityList.size()-1);
                        }
                    }
                });
            }
        }).start();

    }
    private void initView() {
        myrv =findViewById(R.id.myrv);
        srlayout = findViewById(R.id.myswipe_rv);
    }

    // 设置菜单监听器。
    SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        // 创建菜单：
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            int width = getResources().getDimensionPixelSize(R.dimen.dp_70);
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            SwipeMenuItem deleteItem = new SwipeMenuItem(My_Community.this)
                    .setTextColor(Color.WHITE)
                    .setBackgroundColor(Color.RED)
                    .setText("删除")
                    .setWidth(width)
                    .setHeight(height);
            swipeRightMenu.addMenuItem(deleteItem);
        }
    };

    // 菜单点击监听。
    SwipeMenuItemClickListener swipeMenuItemClickListener = new SwipeMenuItemClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge) {
            // 任何操作必须先关闭菜单，否则可能出现Item菜单打开状态错乱。
            menuBridge.closeMenu();

            int direction = menuBridge.getDirection();//左边还是右边菜单
            final int adapterPosition = menuBridge.getAdapterPosition();//    recyclerView的Item的position。
            int position = menuBridge.getPosition();// 菜单在RecyclerView的Item中的Position。

            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
                String ss=comunityList.get(adapterPosition).getName();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        Toast.makeText(My_Community.this, ss, Toast.LENGTH_SHORT).show();
                        comunityService.deleteComunity(name,ss);
                    }
                }).start();
                comunityList.remove(adapterPosition);//删除item
                chatAdapter.notifyDataSetChanged();
//                String ss=String.valueOf(adapterPosition);
//                Toast.makeText(My_Community.this, ss, Toast.LENGTH_SHORT).show();

            }
        }
    };
}
