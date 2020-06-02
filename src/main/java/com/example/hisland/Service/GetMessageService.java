package com.example.hisland.Service;

import com.example.hisland.Bean.GetMessage;
import com.example.hisland.Util.MysqlUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetMessageService {
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    public GetMessageService() {

    }

    private static GetMessageService getMessageService = null;

    public static GetMessageService getMessageService() {
        if (getMessageService == null) {
            getMessageService = new GetMessageService();
        }
        return getMessageService;
    }

    public void InsertGetMessage(GetMessage getMessage) { //插入userinfo or 注册
        String sql = "insert into messagelist(username,friendname,text,type)" +
                "values(?,?,?,?)";
        try {
            conn = MysqlUtil.getCoon();
            if (conn != null) {
                ps = conn.prepareStatement(sql);
                ps.setString(1, getMessage.getUsername());
                ps.setString(2, getMessage.getFriendname());
                ps.setString(3, getMessage.getText());
                ps.setInt(4, getMessage.getType());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        MysqlUtil.closeAll(conn, ps, rs);
    }

    public List<GetMessage> getMessagelistData(String user_name, String friend_name) {
        List<GetMessage> list = new ArrayList<>();
        String sql = "select * from messagelist where username=? and friendname=?";
        try {
            conn = MysqlUtil.getCoon();
            if (conn != null) {
                ps = conn.prepareStatement(sql);
                ps.setString(1, user_name);
                ps.setString(2, friend_name);
                if (ps != null) {
                    rs = ps.executeQuery();
                    if (rs != null) {
                        while (rs.next()) {
                            GetMessage getMessage = new GetMessage();
                            getMessage.setUsername(rs.getString("username"));
                            getMessage.setFriendname(rs.getString("friendname"));
                            getMessage.setText(rs.getString("text"));
                            getMessage.setType(rs.getInt("type"));
                            list.add(getMessage);
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
}
