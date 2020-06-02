package com.example.hisland.Fragment;

import android.content.Intent;
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

import com.example.hisland.Adapter.ChatAdapter;
import com.example.hisland.AppActivity.Addcomunity;
import com.example.hisland.Bean.Comunity;
import com.example.hisland.R;
import com.example.hisland.Service.CommunityService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class Fragmentisland extends Fragment {
    private List<Comunity> comunityList = new ArrayList<>();
    CommunityService comunityService = CommunityService.getCommunityService();
    private RecyclerView rv;
    private SwipeRefreshLayout srlayout;
    TextView titleuser;

    private ChatAdapter chatAdapter;
    FloatingActionButton add_dongtai;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_island, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //进行逻辑的处理
        add_dongtai=getActivity().findViewById(R.id.add_dongtai);
        titleuser = getActivity().findViewById(R.id.title_text);
        initView();

        Refresh();
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

        add_dongtai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(), Addcomunity.class);
                intent.putExtra("name",titleuser.getText().toString());
                v.getContext().startActivity(intent);
            }
        });
    }

    private void Refresh() {
        comunityList = new ArrayList<>();
        initcomunity();
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(layoutManager);

        chatAdapter = new ChatAdapter(getActivity(),comunityList);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(chatAdapter);
    }

    private void initcomunity() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Comunity> list_comunity = comunityService.getCom1(titleuser.getText().toString());
                Handler mainhandler=new Handler(Looper.getMainLooper());
                mainhandler.post(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = list_comunity.size()-1; i >=0; i--) {
                            Comunity com = list_comunity.get(i);
                            com.setUse(titleuser.getText().toString());
                            comunityList.add(com);
                            chatAdapter.notifyItemInserted(comunityList.size()-1);
                        }
                    }
                });


            }
        }).start();

    }

    private void initView() {
        rv = getActivity().findViewById(R.id.rv);
        srlayout = getActivity().findViewById(R.id.swipe_rv);
//        add = getActivity().findViewById(R.id.add);
//        rvlayout = getActivity().findViewById(R.id.rvlayout);
//        addcontent =view.findViewById(R.id.addcontent);
//        addcomunity =view.findViewById(R.id.addcomunity);
    }
}
