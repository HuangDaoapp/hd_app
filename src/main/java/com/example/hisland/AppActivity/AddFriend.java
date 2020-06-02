package com.example.hisland.AppActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hisland.Bean.Addfriendinfo;
import com.example.hisland.Bean.Friendinfo;
import com.example.hisland.R;
import com.example.hisland.Service.AddFriendinfoService;
import com.example.hisland.Service.FriendinfoService;
import com.example.hisland.Service.UserinfoService;
import com.hyphenate.EMContactListener;

public class AddFriend extends AppCompatActivity implements EMContactListener {
    EditText friend_name,check_message;
    Button add_friend;
    private FriendinfoService friendinfoService = FriendinfoService.getFriendinfoService();
    private UserinfoService userinfoService = UserinfoService.getUserinfoService();
    private AddFriendinfoService addFriendinfoService=AddFriendinfoService.getAddFriendinfoService();
    Friendinfo friendinfo = new Friendinfo();
    Friendinfo friendinfo1 = new Friendinfo();
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        friend_name = findViewById(R.id.friend_name);
        check_message=findViewById(R.id.check_message);
        add_friend = findViewById(R.id.add);
        Intent getintent = getIntent();
        name = getintent.getStringExtra("name");

        add_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        friendinfo.setUser_nickname(name);
                        friendinfo.setFriend_nickname(friend_name.getText().toString());

                        friendinfo1.setUser_nickname(friend_name.getText().toString());
                        friendinfo1.setFriend_nickname(name);
                        boolean result = userinfoService.check(friend_name.getText().toString());
                        boolean result1 = friendinfoService.check(friend_name.getText().toString(), name);
                        if (!name.equals(friend_name.getText().toString())) {
                            if (result) {
                                if (result1) {
                                    if (Looper.myLooper() == null) {
                                        Looper.prepare();
                                    }
                                    Toast.makeText(AddFriend.this, "用户已添加！", Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                } else {
                                    Addfriendinfo addfriendinfo=new Addfriendinfo();
                                    addfriendinfo.setAddusername(name);
                                    addfriendinfo.setAddriendname(friend_name.getText().toString());
                                    addfriendinfo.setAddtext(check_message.getText().toString());
                                    addFriendinfoService.addmyfriend(addfriendinfo);
                                    //直接在数据库中插入两条数据
//                                    friendinfoService.addFriend(friendinfo);
//                                    friendinfoService.addFriend(friendinfo1);


                                    //参数为要添加的好友的username和添加理由
//                                    try {
//                                        EMClient.getInstance().contactManager().addContact(friend_name.getText().toString(), check_message.getText().toString());
//                                    } catch (HyphenateException e) {
//                                        e.printStackTrace();
//                                    }
                                }
                            } else {
                                if (Looper.myLooper() == null) {
                                    Looper.prepare();
                                }
                                Toast.makeText(AddFriend.this, "用户不存在！", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                        }else{
                            if (Looper.myLooper() == null) {
                                Looper.prepare();
                            }
                            Toast.makeText(AddFriend.this, "用户不可添加本人！", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                        finish();
//                        if (Looper.myLooper() == null) {
//                            Looper.prepare();
//                        }
//                        Toast.makeText(AddFriend.this, "添加成功！", Toast.LENGTH_SHORT).show();
//                        Looper.loop();
                    }
                }).start();
            }
        });
    }

    @Override
    public void onContactAdded(String s) {
//增加了联系人时回调此方法
    }

    @Override
    public void onContactDeleted(String s) {
//被删除时回调此方法
    }

    @Override
    public void onContactInvited(String s, String s1) {
        //收到好友邀请
//        try {
//            EMClient.getInstance().contactManager().acceptInvitation(name);
//        } catch (HyphenateException e) {
//            e.printStackTrace();
//        }

    }

    @Override
    public void onFriendRequestAccepted(String s) {
        //好友请求被同意
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                friendinfoService.addFriend(friendinfo);
//                friendinfoService.addFriend(friendinfo1);
//            }
//        }).start();
    }

    @Override
    public void onFriendRequestDeclined(String s) {
        //好友请求被拒绝
    }
}
