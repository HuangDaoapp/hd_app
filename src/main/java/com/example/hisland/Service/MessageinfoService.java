package com.example.hisland.Service;

import com.example.hisland.Bean.Messageinfo;
import com.example.hisland.Util.MysqlUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessageinfoService {
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    public MessageinfoService() {

    }

    private static MessageinfoService messageinfoService = null;

    public static MessageinfoService getMessageinfoService() {
        if (messageinfoService == null) {
            messageinfoService = new MessageinfoService();
        }
        return messageinfoService;
    }
    public void UpdateMessage(Messageinfo messageinfo) { //修改资料
        String sql = "update messageinfo set chat_text=?where user_name=? and friend_name=?";
        if (messageinfo != null) {
            try {
                conn = MysqlUtil.getCoon();
                if (conn != null) {
                    ps = conn.prepareStatement(sql);
                    ps.setString(1, messageinfo.getText());
                    ps.setString(2, messageinfo.getUsername());
                    ps.setString(3, messageinfo.getFriendname());
                    ps.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            MysqlUtil.closeAll(conn, ps, rs);
        }
    }
    public void InsertMessageinfo(Messageinfo messageinfo) { //插入userinfo or 注册
        String sql = "insert into messageinfo(user_name,friend_name,chat_text)" +
                "values(?,?,?)";
        try {
            conn = MysqlUtil.getCoon();
            if (conn != null) {
                ps = conn.prepareStatement(sql);
                ps.setString(1, messageinfo.getUsername());
                ps.setString(2, messageinfo.getFriendname());
                ps.setString(3, messageinfo.getText());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        MysqlUtil.closeAll(conn, ps, rs);
    }
    public List<Messageinfo> getMessageData(String user_name) {
        List<Messageinfo> list = new ArrayList<>();
        String sql = "select * from messageinfo where user_name=?";
        try {
            conn = MysqlUtil.getCoon();
            if (conn != null) {
                ps = conn.prepareStatement(sql);
                ps.setString(1,user_name);
                if (ps != null) {
                    rs = ps.executeQuery();
                    if (rs != null) {
                        while (rs.next()) {
                            Messageinfo messageinfo = new Messageinfo();
                            messageinfo.setUsername(rs.getString("user_name"));
                            messageinfo.setFriendname(rs.getString("friend_name"));
                            messageinfo.setText(rs.getString("chat_text"));
                            list.add(messageinfo);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        MysqlUtil.closeAll(conn, ps, rs);//关闭相关操作
        return list;
    }
    public boolean checkmessage(String user_name, String friend_name) {
        String check = "select * from  messageinfo where friend_name=? and user_name=?";
        int flag = -1;
        try {
            conn = MysqlUtil.getCoon();
            if (conn != null) {
                ps = conn.prepareStatement(check);
                ps.setString(1, friend_name);
                ps.setString(2, user_name);
                if (ps != null) {
                    rs = ps.executeQuery();
                    if (rs != null) {
                        while (rs.next()) {
                            flag = 1;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        MysqlUtil.closeAll(conn, ps, rs);//关闭相关操作
        if (flag == 1) {
            return true;
        } else {
            return false;
        }
    }
}
