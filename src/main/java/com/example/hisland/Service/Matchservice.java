package com.example.hisland.Service;


import com.example.hisland.Util.MysqlUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;

public class Matchservice {
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    public String getMatchuser() {
        String sql = "select * from match";
        String username="";
        try {
            conn = MysqlUtil.getCoon();
            if (conn != null) {
                ps = conn.prepareStatement(sql);
                if (ps != null) {
                    rs = ps.executeQuery();
                    if (rs != null) {
                        while (rs.next()) {
                            username =rs.getString("username");
                        }
                    }
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        MysqlUtil.closeAll(conn, ps, rs);
        return username;
    }

    public void deleteMatch() {
        String sql = "delete * from match";
        try {
            conn = MysqlUtil.getCoon();
            if (conn != null) {
                ps = conn.prepareStatement(sql);
                ps.executeQuery();

            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        MysqlUtil.closeAll(conn, ps);

    }
    public int insertMatch(String username) {
        int flag = 1;
        String sql = "insert into match(username) values(?) ";
        try {
            conn = MysqlUtil.getCoon();
            if (conn != null) {
                ps = conn.prepareStatement(sql);
                ps.setString(1, username);
                ps.executeUpdate();}
        }catch(SQLException e){
            e.printStackTrace();
            flag = -1;
        }
        MysqlUtil.closeAll(conn, ps);
        return flag;
    }

}


