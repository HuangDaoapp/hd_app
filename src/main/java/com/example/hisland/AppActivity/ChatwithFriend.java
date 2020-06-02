package com.example.hisland.AppActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hisland.Adapter.MsgAdapter;
import com.example.hisland.Bean.GetMessage;
import com.example.hisland.Bean.Messageinfo;
import com.example.hisland.Bean.Msg;
import com.example.hisland.R;
import com.example.hisland.Service.GetMessageService;
import com.example.hisland.Service.MessageinfoService;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import java.util.ArrayList;
import java.util.List;

public class ChatwithFriend extends AppCompatActivity implements EMMessageListener {
    private int Count = 0;
    private List<Msg> msgList = new ArrayList<>();
    private EditText inputText;
    private Button send;
    private RecyclerView msgRecyclerView;
    private TextView friendname_who;
    private LinearLayout yincang_bottom;
    private ImageButton jiahao;
    private MessageinfoService messageinfoService = MessageinfoService.getMessageinfoService();
    private GetMessageService getMessageService = GetMessageService.getMessageService();
    private String com_username, com_friendname;
    List<GetMessage> list_message = new ArrayList<>();
    MsgAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatwith_friend);
        inputText = (EditText) findViewById(R.id.input_text);
        send = (Button) findViewById(R.id.send);
        msgRecyclerView = (RecyclerView) findViewById(R.id.msg_recycle_view);
        friendname_who = findViewById(R.id.who);
        yincang_bottom = findViewById(R.id.dibu_xianshi);
        jiahao = findViewById(R.id.jiahao);
        initMsgs();
        //获取键盘状态
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();//返回键盘是否已经打开
        //获取从前一个活动传过来的name
        Intent getintent = getIntent();
//        list_message = (List<GetMessage>) getintent.getSerializableExtra("list_message");
        String username = getintent.getStringExtra("username");
        String name = getintent.getStringExtra("name");
        com_username = username;
        com_friendname = name;
        friendname_who.setText(name);
        //适配器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);
        inputText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yincang_bottom.setVisibility(View.GONE);
            }
        });
        //监听接收消息
        EMClient.getInstance().chatManager().addMessageListener(this);
        //发送聊天内容
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = inputText.getText().toString();
                if (!"".equals(content)) {
                    Msg msg = new Msg(content, Msg.TYPE_SENT);
                    EMMessage message = EMMessage.createTxtSendMessage(content, friendname_who.getText().toString());
                    EMClient.getInstance().chatManager().sendMessage(message);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            GetMessage getMessage = new GetMessage();
                            GetMessage getMessage1=new GetMessage();

                            getMessage.setUsername(com_username);
                            getMessage.setFriendname(com_friendname);
                            getMessage.setText(content);
                            getMessage.setType(Msg.TYPE_SENT);

                            getMessage1.setUsername(com_friendname);
                            getMessage1.setFriendname(com_username);
                            getMessage1.setText(content);
                            getMessage1.setType(Msg.TYPE_RECEIVED);
                            getMessageService.InsertGetMessage(getMessage);
                            getMessageService.InsertGetMessage(getMessage1);
                        }
                    }).start();
                    msgList.add(msg);
                    adapter.notifyItemInserted(msgList.size() - 1);//当有新消息时刷新ListView中的显示
                    msgRecyclerView.scrollToPosition(msgList.size() - 1);//将ListView定位到最后一行
                    inputText.setText("");//清空输入框中的内容
                }
            }
        });
        //底部隐藏菜单
        jiahao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Count = Count + 1;
                if (Count % 2 == 1) {
                    if (isOpen) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                    yincang_bottom.setVisibility(View.VISIBLE);
                } else {
                    if (isOpen) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                    yincang_bottom.setVisibility(View.GONE);
                }

            }
        });
    }

    private void initMsgs() {
        Msg msg1 = new Msg("现在可以和我开始聊天了！", Msg.TYPE_RECEIVED);
        msgList.add(msg1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<GetMessage> list_message = getMessageService.getMessagelistData(com_username, com_friendname);
                Handler mainhandler=new Handler(Looper.getMainLooper());
                mainhandler.post(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < list_message.size(); i++) {
                            if (list_message.get(i).getType() == 1) {
                                Msg msg = new Msg(list_message.get(i).getText(), Msg.TYPE_SENT);
                                msgList.add(msg);
                                adapter.notifyItemInserted(msgList.size()-1);
                                msgRecyclerView.scrollToPosition(msgList.size() - 1);
                            } else {
                                Msg msg = new Msg(list_message.get(i).getText(), Msg.TYPE_RECEIVED);
                                msgList.add(msg);
                                adapter.notifyItemInserted(msgList.size()-1);
                                msgRecyclerView.scrollToPosition(msgList.size() - 1);
                            }
                        }

                    }
                });

            }
        }).start();


        //聊天记录的获取//环信方式
//        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(com_username);
//        //获取此会话的所有消息
//        List<EMMessage> messages = conversation.getAllMessages();
//        //List<EMMessage> messages = conversation.loadMoreMsgFromDB("现在可以和我开始聊天了！",20);
//        if (messages != null) {
//            EMMessage have_emMessage = messages.get(messages.size() - 1);
//            EMTextMessageBody emTextMessageBody = (EMTextMessageBody) have_emMessage.getBody();
//            String msg = emTextMessageBody.getMessage();
//            Msg msg_have = new Msg(msg, Msg.TYPE_RECEIVED);
//            msgList.add(msg_have);
//        }

    }


    @Override
    public void onMessageReceived(List<EMMessage> list) {
        EMMessage emMessage = list.get(list.size() - 1);
        EMTextMessageBody emTextMessageBody = (EMTextMessageBody) emMessage.getBody();
        String msg = emTextMessageBody.getMessage();
        Msg msg1 = new Msg(msg, Msg.TYPE_RECEIVED);
        msgList.add(msg1);
        adapter.notifyItemInserted(msgList.size()-1);
        msgRecyclerView.scrollToPosition(msgList.size() - 1);
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageRead(List<EMMessage> list) {

    }

    @Override
    public void onMessageDelivered(List<EMMessage> list) {

    }

    @Override
    public void onMessageRecalled(List<EMMessage> list) {

    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object o) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Msg msg = msgList.get(msgList.size() - 1);
        String chat = msg.getContent();
        Messageinfo messageinfo = new Messageinfo();
        messageinfo.setUsername(com_username);
        messageinfo.setFriendname(com_friendname);
        messageinfo.setText(chat);
        if(msgList.size()>1) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (msgList != null) {
                        messageinfoService.UpdateMessage(messageinfo);
                    }
                }
            }).start();
        }

        EMClient.getInstance().chatManager().removeMessageListener(this);
    }


}
