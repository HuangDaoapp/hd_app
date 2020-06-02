package com.example.hisland.Service;

import com.example.hisland.Bean.Addfriendinfo;
import com.example.hisland.Util.MysqlUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddFriendinfoService {
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    public AddFriendinfoService() {

    }

    private static AddFriendinfoService addFriendinfoService = null;

    public static AddFriendinfoService getAddFriendinfoService() {
        if (addFriendinfoService == null) {
            addFriendinfoService = new AddFriendinfoService();
        }
        return addFriendinfoService;
    }
    public List<Addfriendinfo> getaddFriendData(String addusername) {
        List<Addfriendinfo> list = new ArrayList<>();
        String sql = "select * from addfriendinfo where addfriendname=?";
        try {
            conn = MysqlUtil.getCoon();
            if (conn != null) {
                ps = conn.prepareStatement(sql);
                ps.setString(1, addusername);
                if (ps != null) {
                    rs = ps.executeQuery();
                    if (rs != null) {
                        while (rs.next()) {
                            Addfriendinfo addfriendinfo = new Addfriendinfo();
                            addfriendinfo.setAddusername(rs.getString("addusername"));
                            addfriendinfo.setAddriendname(rs.getString("addfriendname"));
                            addfriendinfo.setAddtext(rs.getString("addtext"));
                            list.add(addfriendinfo);
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

    public void addmyfriend(Addfriendinfo addfriendinfo) {
        String sql = "insert into addfriendinfo(addusername,addfriendname,addtext) values(?,?,?)";
        try {
            conn = MysqlUtil.getCoon();
            if (conn != null) {
                ps = conn.prepareStatement(sql);
                ps.setString(1, addfriendinfo.getAddusername());
                ps.setString(2, addfriendinfo.getAddriendname());
                ps.setString(3, addfriendinfo.getAddtext());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletemyFriend(String addusername, String addfriendname) {
        String sql = "delete from addfriendinfo where addusername=? and addfriendname=?";
        try {
            conn = MysqlUtil.getCoon();
            if (conn != null) {
                ps = conn.prepareStatement(sql);
                ps.setString(1, addusername);
                ps.setString(2,addfriendname);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
