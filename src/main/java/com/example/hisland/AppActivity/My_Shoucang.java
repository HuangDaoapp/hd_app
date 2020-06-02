package com.example.hisland.AppActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.hisland.Adapter.ChatAdapter;
import com.example.hisland.Bean.Comunity;
import com.example.hisland.R;
import com.example.hisland.Service.CommunityService;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class My_Shoucang extends AppCompatActivity {
    private List<Comunity> ccomunityList = new ArrayList<>();
    CommunityService comunityService = CommunityService.getCommunityService();
    private SwipeMenuRecyclerView cmyrv;
    private SwipeRefreshLayout csrlayout;
    private ChatAdapter cchatAdapter;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__shoucang);
        Intent getintent = getIntent();
        name=getintent.getStringExtra("name");
        initView();
        initcomunity();
        cmyrv.addItemDecoration(new DefaultItemDecoration(Color.GRAY));
        cmyrv.setSwipeMenuCreator(swipeMenuCreator);
        cmyrv.setSwipeMenuItemClickListener(swipeMenuItemClickListener);
        cchatAdapter = new ChatAdapter(this,ccomunityList);
        cmyrv.setLayoutManager(new LinearLayoutManager(this));
        cmyrv.setAdapter(cchatAdapter);
        csrlayout.setColorSchemeResources(android.R.color.holo_green_light,android.R.color.holo_red_light,android.R.color.holo_blue_light);
        csrlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(700);
                            csrlayout.setRefreshing(false);

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
        ccomunityList = new ArrayList<>();
        initcomunity();
        cchatAdapter = new ChatAdapter(this,ccomunityList);
        cmyrv.setLayoutManager(new LinearLayoutManager(this));
        cmyrv.setAdapter(cchatAdapter);
    }

    private void initcomunity() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Comunity> clist_comunity = comunityService.getCom2();
                Handler mainhandler=new Handler(Looper.getMainLooper());
                mainhandler.post(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = clist_comunity.size()-1; i>=0; i--) {
                            Comunity com = clist_comunity.get(i);
                            com.setUse(name);
                            ccomunityList.add(com);
                            cchatAdapter.notifyItemInserted(ccomunityList.size()-1);
                        }
                    }
                });
            }
        }).start();
    }
    private void initView() {
        cmyrv =findViewById(R.id.myrv_myshoucang);
        csrlayout = findViewById(R.id.myswipe_rv_myshoucang);
    }

    // 设置菜单监听器。
    SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        // 创建菜单：
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            int width = getResources().getDimensionPixelSize(R.dimen.dp_70);
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            SwipeMenuItem deleteItem = new SwipeMenuItem(My_Shoucang.this)
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
                String ss=ccomunityList.get(adapterPosition).getName();
                String sss=ccomunityList.get(adapterPosition).getUser();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        comunityService.deleteComunity1(sss,ss);
                    }
                }).start();
                ccomunityList.remove(adapterPosition);//删除item
                cchatAdapter.notifyDataSetChanged();
//                String ss=String.valueOf(adapterPosition);
//                Toast.makeText(My_Community.this, ss, Toast.LENGTH_SHORT).show();

            }
        }
    };
}

