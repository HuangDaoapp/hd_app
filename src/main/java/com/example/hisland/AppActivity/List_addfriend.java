package com.example.hisland.AppActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.hisland.Adapter.AddFriendlistAdapter;
import com.example.hisland.Bean.Addfriendinfo;
import com.example.hisland.Bean.Addfriendlist;
import com.example.hisland.R;
import com.example.hisland.Service.AddFriendinfoService;

import java.util.ArrayList;
import java.util.List;

public class List_addfriend extends AppCompatActivity {
    private List<Addfriendlist> addfriendlistList = new ArrayList<>();
    private AddFriendinfoService addFriendinfoService = AddFriendinfoService.getAddFriendinfoService();
    RecyclerView recyclerView;
    String username;
    AddFriendlistAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_addfriend);
        recyclerView = findViewById(R.id.add_recycle_view);
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.add_swipe);
        initaddFriendlist();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AddFriendlistAdapter(addfriendlistList);
        recyclerView.setAdapter(adapter);
        Intent getintent = getIntent();
        username = getintent.getStringExtra("username");
        //刷新
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_red_light, android.R.color.holo_blue_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(700);
                            swipeRefreshLayout.setRefreshing(false);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                Refreshaddfriendlist();
            }
        });
    }

    private void Refreshaddfriendlist() {
        addfriendlistList = new ArrayList<>();
        initaddFriendlist();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AddFriendlistAdapter(addfriendlistList);
        recyclerView.setAdapter(adapter);
    }

    private void initaddFriendlist() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Addfriendinfo> list_add_friend = addFriendinfoService.getaddFriendData(username);
                Handler mainhandler=new Handler(Looper.getMainLooper());
                mainhandler.post(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < list_add_friend.size(); i++) {
                            Addfriendlist addfriend = new Addfriendlist(list_add_friend.get(i).getAddriendname(), list_add_friend.get(i).getAddusername(), list_add_friend.get(i).getAddtext());
                            addfriendlistList.add(addfriend);
                            adapter.notifyItemInserted(addfriendlistList.size()-1);
                        }
                    }
                });
            }
        }).start();
    }
}
