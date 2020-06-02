package com.example.hisland.Adapter;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hisland.Bean.Addfriendlist;
import com.example.hisland.Bean.Friendinfo;
import com.example.hisland.R;
import com.example.hisland.Service.AddFriendinfoService;
import com.example.hisland.Service.FriendinfoService;

import java.util.List;

public class AddFriendlistAdapter extends RecyclerView.Adapter<AddFriendlistAdapter.ViewHolder>{
    private List<Addfriendlist> mAddfriendList;
    private AddFriendinfoService addFriendinfoService=AddFriendinfoService.getAddFriendinfoService();
    private FriendinfoService friendinfoService=FriendinfoService.getFriendinfoService();
    static class ViewHolder extends RecyclerView.ViewHolder{
        View messageView;
        TextView addusername;
        TextView addfriendname;
        TextView addtext;
        public ViewHolder(View view) {
            super(view);
            messageView = view;
            addusername = view.findViewById(R.id.add_username);
            addfriendname=view.findViewById(R.id.add_friendname);
            addtext=view.findViewById(R.id.add_text);
        }
    }
    public AddFriendlistAdapter(List<Addfriendlist> addfriendlist) {
        mAddfriendList = addfriendlist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_addfriend, parent, false);
        AddFriendlistAdapter.ViewHolder holder = new AddFriendlistAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Addfriendlist addfriendlist=mAddfriendList.get(position);
        holder.addusername.setText(addfriendlist.getAddusername());
        holder.addfriendname.setText(addfriendlist.getAddfriendname());
        holder.addtext.setText(addfriendlist.getAddtext());
        holder.messageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Addfriendlist addfriendlist=mAddfriendList.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("选择操作！");
                builder.setMessage("您确定要添加"+addfriendlist.getAddfriendname()+"吗?");
                builder.setCancelable(true);
                builder.setPositiveButton("同意", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Friendinfo friendinfo=new Friendinfo();
                                Friendinfo friendinfo1=new Friendinfo();
                                friendinfo.setUser_nickname(addfriendlist.getAddusername());
                                friendinfo.setFriend_nickname(addfriendlist.getAddfriendname());
                                friendinfo1.setUser_nickname(addfriendlist.getAddfriendname());
                                friendinfo1.setFriend_nickname(addfriendlist.getAddusername());
                                addFriendinfoService.deletemyFriend(addfriendlist.getAddfriendname(),addfriendlist.getAddusername());
                                friendinfoService.addFriend(friendinfo);
                                friendinfoService.addFriend(friendinfo1);
                            }
                        }).start();

                    }
                });
                builder.setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                addFriendinfoService.deletemyFriend(addfriendlist.getAddfriendname(),addfriendlist.getAddusername());
                            }
                        }).start();

                    }
                });
                builder.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return mAddfriendList.size();
    }





}
