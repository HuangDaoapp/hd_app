package com.example.hisland.Service;

import com.example.hisland.Bean.Comunity;
import com.example.hisland.Util.MysqlUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommunityService {
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    public CommunityService() {

    }

    private static CommunityService communityService = null;

    public static CommunityService getCommunityService() {
        if (communityService == null) {
            communityService = new CommunityService();
        }
        return communityService;
    }
    public List<Comunity> getCom(String user_nickname) {
        List<Comunity> list=new ArrayList<>();
        //MySQL 语句
        String sql="select * from Comunity where user=?";
        try{
            conn= MysqlUtil.getCoon();
            if (conn!=null){
                ps=conn.prepareStatement(sql);
                ps.setString(1,user_nickname);
                if (ps!=null){
                    rs=ps.executeQuery();
                    if (rs!=null){
                        while (rs.next()){
                            Comunity com = new Comunity();
                            com.setInfo(rs.getString("info"));
                            com.setName(rs.getString("name"));
                            com.setUser(rs.getString("user"));
                            list.add(com);

                        }/** private String activity_id;
                         private String user_id;
                         private Date activity_time;
                         private String activity_text;
                         private String activity_url;
                         private int activity_count;*/
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        MysqlUtil.closeAll(conn,ps,rs);//关闭相关操作kk
        return list;
    }

    public List<Comunity> getCom1(String user_nickname) {
        List<Comunity> list=new ArrayList<>();
        //MySQL 语句
        String sql="select * from Comunity where user in (select friend_nickname from friendinfo where user_nickname=? )or user=?";
        try{
            conn= MysqlUtil.getCoon();
            if (conn!=null){
                ps=conn.prepareStatement(sql);
                ps.setString(1,user_nickname);
                ps.setString(2,user_nickname);
                if (ps!=null){
                    rs=ps.executeQuery();
                    if (rs!=null){
                        while (rs.next()){
                            Comunity com = new Comunity();
                            com.setInfo(rs.getString("info"));
                            com.setName(rs.getString("name"));
                            com.setUser(rs.getString("user"));
                            list.add(com);

                        }/** private String activity_id;
                         private String user_id;
                         private Date activity_time;
                         private String activity_text;
                         private String activity_url;
                         private int activity_count;*/
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        MysqlUtil.closeAll(conn,ps,rs);//关闭相关操作kk
        return list;
    }
    public List<Comunity> getCom2() {
        List<Comunity> list=new ArrayList<>();
        //MySQL 语句
        String sql="select * from shoucang";
        try{
            conn= MysqlUtil.getCoon();
            if (conn!=null){
                ps=conn.prepareStatement(sql);
                if (ps!=null){
                    rs=ps.executeQuery();
                    if (rs!=null){
                        while (rs.next()){
                            Comunity com = new Comunity();
                            com.setInfo(rs.getString("info"));
                            com.setName(rs.getString("name"));
                            com.setUser(rs.getString("user"));
                            list.add(com);

                        }/** private String activity_id;
                         private String user_id;
                         private Date activity_time;
                         private String activity_text;
                         private String activity_url;
                         private int activity_count;*/
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        MysqlUtil.closeAll(conn,ps,rs);//关闭相关操作kk
        return list;
    }

    public void addComunity(Comunity comunity) {
        String sql = "insert into Comunity(name,info,user) values(?,?,?)";
        try {
            conn = MysqlUtil.getCoon();
            if (conn != null) {
                ps = conn.prepareStatement(sql);
                ps.setString(1, comunity.getName());
                ps.setString(2, comunity.getInfo());
                ps.setString(3, comunity.getUser());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void addComunity1(Comunity comunity) {
        String sql = "insert into shoucang(name,info,user,type) values(?,?,?,?)";
        try {
            conn = MysqlUtil.getCoon();
            if (conn != null) {
                ps = conn.prepareStatement(sql);
                ps.setString(1, comunity.getName());
                ps.setString(2, comunity.getInfo());
                ps.setString(3, comunity.getUser());
                ps.setString(4, comunity.getType());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteComunity(String user, String name) {
        String sql = "delete from  Comunity where user=? and name=?";
        try {
            conn = MysqlUtil.getCoon();
            if (conn != null) {
                ps = conn.prepareStatement(sql);
                ps.setString(1,user);
                ps.setString(2,name);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteComunity1(String user, String name) {
        String sql = "delete from  shoucang where user=? and name=?";
        try {
            conn = MysqlUtil.getCoon();
            if (conn != null) {
                ps = conn.prepareStatement(sql);
                ps.setString(1,user);
                ps.setString(2,name);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public String selecttype(String user,String name) {
        Comunity comunity = new Comunity();
        String check = "select type from  shoucang where user= ? and name=?";
        try {
            conn = MysqlUtil.getCoon();
            if (conn != null) {
                ps = conn.prepareStatement(check);
                ps.setString(1,user);
                ps.setString(2,name);
                if (ps != null) {
                    rs = ps.executeQuery();
                    if (rs != null) {
                        while (rs.next()) {
                            comunity.setType(rs.getString("type"));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        MysqlUtil.closeAll(conn, ps, rs);//关闭相关操作
        return comunity.getType();
    }

}
