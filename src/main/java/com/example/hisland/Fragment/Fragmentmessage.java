package com.example.hisland.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.hisland.Adapter.MessagelistAdapter;
import com.example.hisland.Bean.Messageinfo;
import com.example.hisland.Bean.Messagelist;
import com.example.hisland.R;
import com.example.hisland.Service.MessageinfoService;

import java.util.ArrayList;
import java.util.List;

public class Fragmentmessage extends Fragment {
    private List<Messagelist> messagelistList = new ArrayList<>();
    TextView titleuser;
    RecyclerView recyclerView;
    private MessageinfoService messageinfoService = MessageinfoService.getMessageinfoService();
    List<Messageinfo> list_message = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;
    MessagelistAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_message, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        titleuser = getActivity().findViewById(R.id.title_text);
        recyclerView = getActivity().findViewById(R.id.recycle_view_msg);
        //进行逻辑的处理
        swipeRefreshLayout = getActivity().findViewById(R.id.swipe_msg);
        initMessages();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MessagelistAdapter(messagelistList);
        recyclerView.setAdapter(adapter);
        //adapter.notifyItemInserted(messagelistList.size() - 1);
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
        messagelistList = new ArrayList<>();
        initMessages();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MessagelistAdapter(messagelistList);
        recyclerView.setAdapter(adapter);
    }

    private void initMessages() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                list_message = messageinfoService.getMessageData(titleuser.getText().toString());
                Handler mainhandler=new Handler(Looper.getMainLooper());
                mainhandler.post(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < list_message.size(); i++) {
                            Messagelist message = new Messagelist(list_message.get(i).getFriendname(), R.drawable.touxiang, list_message.get(i).getText(), list_message.get(i).getUsername());
                            messagelistList.add(message);
                            adapter.notifyItemInserted(messagelistList.size()-1);
                        }
                    }
                });

            }
        }).start();
    }
}
