package com.example.hisland.Adapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hisland.AppActivity.ChatwithFriend;
import com.example.hisland.Bean.Friendlist;
import com.example.hisland.Bean.Messageinfo;
import com.example.hisland.R;
import com.example.hisland.Service.FriendinfoService;
import com.example.hisland.Service.GetMessageService;
import com.example.hisland.Service.MessageinfoService;

import java.util.List;

public class FriendlistAdapter extends RecyclerView.Adapter<FriendlistAdapter.ViewHolder> {
    private List<Friendlist> mFriendList;
    private MessageinfoService messageinfoService = MessageinfoService.getMessageinfoService();
    private GetMessageService getMessageService = GetMessageService.getMessageService();
    private FriendinfoService friendinfoService=FriendinfoService.getFriendinfoService();
    //List<GetMessage> list_message = new ArrayList<>();

    static class ViewHolder extends RecyclerView.ViewHolder {
        View friendView;
        ImageView friendimage;
        TextView friendname, com_username;

        public ViewHolder(View view) {
            super(view);
            friendView = view;
            friendimage = view.findViewById(R.id.friend_image);
            friendname = view.findViewById(R.id.friend_username);
            com_username = view.findViewById(R.id.com_username);
        }
    }

    public FriendlistAdapter(List<Friendlist> friendlist) {
        mFriendList = friendlist;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_list, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Friendlist friendlist = mFriendList.get(position);
        holder.friendimage.setImageResource(friendlist.getImageid());
        holder.friendname.setText(friendlist.getFriendname());
        holder.com_username.setText(friendlist.getComusername());
        holder.friendView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Friendlist friendlist = mFriendList.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("选择操作！");
                //builder.setMessage("您确定要退出登录吗？");
                builder.setCancelable(true);
                builder.setPositiveButton("发消息", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(v.getContext(), ChatwithFriend.class);
                                intent.putExtra("username", friendlist.getComusername());
                                intent.putExtra("name", friendlist.getFriendname());
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Messageinfo messageinfo=new Messageinfo();
                                        messageinfo.setUsername(friendlist.getComusername());
                                        messageinfo.setFriendname(friendlist.getFriendname());
                                        messageinfo.setText(" ");
                                        boolean result=messageinfoService.checkmessage(friendlist.getComusername(),friendlist.getFriendname());
                                        if(!result) {
                                            messageinfoService.InsertMessageinfo(messageinfo);
                                        }
                                    }
                                }).start();
                                v.getContext().startActivity(intent);
                    }
                });
                builder.setNegativeButton("删除好友", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setTitle("警告");
                        builder.setMessage("您确定要删除"+friendlist.getFriendname()+"吗?");
                        builder.setCancelable(false);
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        friendinfoService.deleteFriend(friendlist.getComusername(),friendlist.getFriendname());
                                        dialog.dismiss();
                                    }
                                }).start();
                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();

                    }
                });
                builder.show();

//                Intent intent = new Intent(v.getContext(), ChatwithFriend.class);
////                new Thread(new Runnable() {
////                    @Override
////                    public void run() {
////                        list_message = getMessageService.getMessagelistData(friendlist.getComusername(), friendlist.getFriendname());
////                        Messageinfo messageinfo = new Messageinfo();
////                        messageinfo.setUsername(friendlist.getComusername());
////                        messageinfo.setFriendname(friendlist.getFriendname());
////                        messageinfo.setText(" ");
////                        boolean result = messageinfoService.checkmessage(friendlist.getComusername(), friendlist.getFriendname());
////                        if (!result) {
////                            messageinfoService.InsertMessageinfo(messageinfo);
////                        }
////                    }
////                }).start();
////                Bundle bundle = new Bundle();
////                bundle.putSerializable("list_message", (Serializable) list_message);
////                intent.putExtras(bundle);
//                intent.putExtra("username", friendlist.getComusername());
//                intent.putExtra("name", friendlist.getFriendname());
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Messageinfo messageinfo=new Messageinfo();
//                        messageinfo.setUsername(friendlist.getComusername());
//                        messageinfo.setFriendname(friendlist.getFriendname());
//                        messageinfo.setText(" ");
//                        boolean result=messageinfoService.checkmessage(friendlist.getComusername(),friendlist.getFriendname());
//                        if(!result) {
//                            messageinfoService.InsertMessageinfo(messageinfo);
//                        }
//                    }
//                }).start();
//                v.getContext().startActivity(intent);
            }


        });
    }

    @Override
    public int getItemCount() {
        return mFriendList.size();
    }


}
