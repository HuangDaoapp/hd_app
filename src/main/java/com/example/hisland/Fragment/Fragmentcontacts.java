package com.example.hisland.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.hisland.Adapter.FriendlistAdapter;
import com.example.hisland.AppActivity.HelpWd;
import com.example.hisland.Bean.Friendinfo;
import com.example.hisland.Bean.Friendlist;
import com.example.hisland.R;
import com.example.hisland.Service.FriendinfoService;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fragmentcontacts extends Fragment {
    private List<Friendlist> friendlistList = new ArrayList<>();
    FriendinfoService friendinfoService = FriendinfoService.getFriendinfoService();
    TextView titleuser;
    RecyclerView recyclerView;
    FriendlistAdapter adapter;
    CircleImageView cr;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_contacts, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        titleuser = getActivity().findViewById(R.id.title_text);
        recyclerView = getActivity().findViewById(R.id.recycle_view);
        cr=getActivity().findViewById(R.id.circleimg);
        //cr.setImageBitmap("");
        //进行逻辑的处理
        SwipeRefreshLayout swipeRefreshLayout = getActivity().findViewById(R.id.swipe);
        initFriends();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FriendlistAdapter(friendlistList);
        recyclerView.setAdapter(adapter);

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
                Refresh();
            }
        });
    }

    private void Refresh() {
        friendlistList = new ArrayList<>();
        initFriends();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FriendlistAdapter(friendlistList);
        recyclerView.setAdapter(adapter);
    }

    private void initFriends() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Friendinfo> list_friend = friendinfoService.getFriendData(titleuser.getText().toString());
                Handler mainhandler=new Handler(Looper.getMainLooper());
                mainhandler.post(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < list_friend.size(); i++) {
                            Friendlist friend = new Friendlist(list_friend.get(i).getFriend_nickname(), R.drawable.touxiang,titleuser.getText().toString());
                            friendlistList.add(friend);
                            adapter.notifyItemInserted(friendlistList.size()-1);
                        }
                    }
                });

            }
        }).start();
    }
}
