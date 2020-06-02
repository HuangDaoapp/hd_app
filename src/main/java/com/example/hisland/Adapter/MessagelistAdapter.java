package com.example.hisland.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hisland.AppActivity.ChatwithFriend;
import com.example.hisland.Bean.Messageinfo;
import com.example.hisland.Bean.Messagelist;
import com.example.hisland.R;
import com.example.hisland.Service.MessageinfoService;

import java.util.List;

public class MessagelistAdapter extends RecyclerView.Adapter<MessagelistAdapter.ViewHolder>{
    private List<Messagelist> mMessageList;
    private MessageinfoService messageinfoService=MessageinfoService.getMessageinfoService();
    static class ViewHolder extends RecyclerView.ViewHolder{
        View messageView;
        ImageView messageimage;
        TextView messagename;
        TextView messagetext;
        TextView user_name;
        public ViewHolder(View view) {
            super(view);
            messageView = view;
            messageimage = view.findViewById(R.id.message_image);
            messagename = view.findViewById(R.id.message_username);
            messagetext=view.findViewById(R.id.message_content);
            user_name=view.findViewById(R.id.user_name);
        }
    }
    public MessagelistAdapter(List<Messagelist> messagelist) {
        mMessageList = messagelist;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_list, parent, false);
        MessagelistAdapter.ViewHolder holder = new MessagelistAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Messagelist messagelist=mMessageList.get(position);
        holder.messageimage.setImageResource(messagelist.getImageid());
        holder.messagename.setText(messagelist.getFriendname());
        holder.messagetext.setText(messagelist.getFriendmessage());
        holder.messageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Messagelist messagelist=mMessageList.get(position);
                Intent intent=new Intent(v.getContext(), ChatwithFriend.class);
                intent.putExtra("username",messagelist.getUsername());
                intent.putExtra("name",messagelist.getFriendname());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Messageinfo messageinfo=new Messageinfo();
                        messageinfo.setUsername(messagelist.getUsername());
                        messageinfo.setFriendname(messagelist.getFriendname());
                        messageinfo.setText(" ");
                        boolean result=messageinfoService.checkmessage(messagelist.getUsername(),messagelist.getFriendname());
                        if(!result) {
                            messageinfoService.InsertMessageinfo(messageinfo);
                        }
                    }
                }).start();
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }
}
