package com.example.hisland.Service;

import com.example.hisland.Bean.Friendinfo;
import com.example.hisland.Util.MysqlUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FriendinfoService {
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    public FriendinfoService() {

    }

    private static FriendinfoService friendinfoService = null;

    public static FriendinfoService getFriendinfoService() {
        if (friendinfoService == null) {
            friendinfoService = new FriendinfoService();
        }
        return friendinfoService;
    }

    public List<Friendinfo> getFriendData(String user_nickname) {
        List<Friendinfo> list = new ArrayList<>();
        String sql = "select * from friendinfo where user_nickname=?";
        try {
            conn = MysqlUtil.getCoon();
            if (conn != null) {
                ps = conn.prepareStatement(sql);
                ps.setString(1, user_nickname);
                if (ps != null) {
                    rs = ps.executeQuery();
                    if (rs != null) {
                        while (rs.next()) {
                            Friendinfo friendinfo = new Friendinfo();
                            friendinfo.setFriend_nickname(rs.getString("friend_nickname"));
                            list.add(friendinfo);
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

    public void addFriend(Friendinfo friendinfo) {
        String sql = "insert into friendinfo(user_nickname,friend_nickname) values(?,?)";
        try {
            conn = MysqlUtil.getCoon();
            if (conn != null) {
                ps = conn.prepareStatement(sql);
                ps.setString(1, friendinfo.getUser_nickname());
                ps.setString(2, friendinfo.getFriend_nickname());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteFriend(String username, String friendname) {
        String sql = "delete from friendinfo where user_nickname=? and friend_nickname=?";
        try {
            conn = MysqlUtil.getCoon();
            if (conn != null) {
                ps = conn.prepareStatement(sql);
                ps.setString(1, username);
                ps.setString(2,friendname);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean check(String friend_nickname, String user_nickname) {
        String check = "select * from  friendinfo where friend_nickname=? and user_nickname=?";
        int flag = -1;
        try {
            conn = MysqlUtil.getCoon();
            if (conn != null) {
                ps = conn.prepareStatement(check);
                ps.setString(1, friend_nickname);
                ps.setString(2, user_nickname);
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
