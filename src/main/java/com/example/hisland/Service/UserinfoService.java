package com.example.hisland.Service;

import android.graphics.Bitmap;
import android.net.Uri;

import com.example.hisland.Util.*;


import com.example.hisland.Bean.Userinfo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;


public class UserinfoService {
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    public static UserinfoService userinfoService = null;

    public UserinfoService() {

    }

    /**
     * 获取MySQL数据库单例类对象
     */
    public static UserinfoService getUserinfoService() {
        if (userinfoService == null) {
            userinfoService = new UserinfoService();
        }
        return userinfoService;
    }

    public List<Userinfo> getUserData() {
        //结果存放集合
        List<Userinfo> list = new ArrayList<>();
        //MySQL 语句
        String sql = "select * from userinfo";
        //获取链接数据库对象

        try {
            conn = MysqlUtil.getCoon();
            if (conn != null) {
                ps = conn.prepareStatement(sql);
                if (ps != null) {
                    rs = ps.executeQuery();
                    if (rs != null) {
                        while (rs.next()) {
                            Userinfo u = new Userinfo();
                            u.setUser_headpic(rs.getString("user_headpic"));
                            u.setUser_id(rs.getString("user_id"));
                            u.setUser_nickname(rs.getString("user_nickname"));
                            u.setUser_password(rs.getString("user_password"));
                            u.setUser_gender(rs.getString("user_gender"));
                            u.setUser_age(rs.getInt("user_age"));
                            u.setUser_signature(rs.getString("user_signature"));
                            u.setUser_phonenumber(rs.getString("user_phonenumber"));
                            u.setUser_card(rs.getString("user_card"));
                            u.setUser_occupation(rs.getString("user_occupation"));
                            u.setUser_location(rs.getString("user_location"));
                            list.add(u);
                        }
                    }/** private String user_headpic; //user 头像 url；
                     private String user_id; //user id 唯一
                     private String user_nickname; //user 昵称
                     private String user_password; //密码
                     private String user_gender; //sex
                     private int user_age;
                     private String user_signature; //个性签名
                     private String user_phonenumber;//电话
                     private String user_card; //交友卡片
                     private String user_occupation; //职业
                     private String user_location; //地区*/
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        MysqlUtil.closeAll(conn, ps, rs);//关闭相关操作
        return list;
    }

    public void delUserData(String user_nickname) {  //根据id删除
        if (user_nickname != null) {
            //获取链接数据库对象
            //MySQL 语句
            String sql = "delete from userinfo where user_nickname=?";
            try {
                conn = MysqlUtil.getCoon();

                if ((conn != null)) {
                    ps = conn.prepareStatement(sql);
                    ps.setString(1, user_nickname);
                    ps.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        MysqlUtil.closeAll(conn, ps);//关闭相关操作
    }

    public int UpdateData(Userinfo userinfo) { //修改资料
        int flag = 1; //判断是否update成功
        String sql = "update userinfo set user_headpic=?,user_nickname=?,user_gender=?,user_age=?," +
                "user_signature=?,user_phonenumber=?,user_card=?,user_occupation=?,user_location=?  where user_id=?";
        if (userinfo != null) {
            try {
                conn = MysqlUtil.getCoon();
                if (conn != null) {
                    ps = conn.prepareStatement(sql);
                    ps.setString(1, userinfo.getUser_headpic());
                    ps.setString(2, userinfo.getUser_nickname());
                    ps.setString(3, userinfo.getUser_gender());
                    ps.setInt(4, userinfo.getUser_age());
                    ps.setString(5, userinfo.getUser_signature());
                    ps.setString(6, userinfo.getUser_phonenumber());
                    ps.setString(7, userinfo.getUser_card());
                    ps.setString(8, userinfo.getUser_occupation());
                    ps.setString(9, userinfo.getUser_location());
                    ps.setString(10, userinfo.getUser_id());
                    ps.executeUpdate();
                }
            } catch (SQLException e) {
                flag = -1;
                e.printStackTrace();
            }
            MysqlUtil.closeAll(conn, ps, rs);
        }
        return flag;
    }

    public void UpdateMessage(Userinfo userinfo) { //修改资料
        String sql = "update userinfo set user_age=?,user_phonenumber=?,user_card=?,user_occupation=?,user_location=?  where user_nickname=?";
        if (userinfo != null) {
            try {
                conn = MysqlUtil.getCoon();
                if (conn != null) {
                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, userinfo.getUser_age());
                    ps.setString(2, userinfo.getUser_phonenumber());
                    ps.setString(3, userinfo.getUser_card());
                    ps.setString(4, userinfo.getUser_occupation());
                    ps.setString(5, userinfo.getUser_location());
                    ps.setString(6, userinfo.getUser_nickname());
                    ps.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            MysqlUtil.closeAll(conn, ps, rs);
        }
    }

    public void InsertUserinfo(Userinfo userinfo) { //插入userinfo or 注册
        String sql = "insert into userinfo(user_nickname,user_password,user_gender)" +
                "values(?,?,?)";
        try {
            conn = MysqlUtil.getCoon();
            if (conn != null) {
                ps = conn.prepareStatement(sql);
                //ps.setString(1, userinfo.getUser_headpic());
                ps.setString(1, userinfo.getUser_nickname());
                ps.setString(2, userinfo.getUser_password());
                ps.setString(3, userinfo.getUser_gender());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        MysqlUtil.closeAll(conn, ps, rs);
    }

    public Userinfo getUserbyId(String name) {
        Userinfo u = new Userinfo();
        try {
            conn = MysqlUtil.getCoon();
            String sql = "select * from userinfo where user_nickname=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            rs = ps.executeQuery();
            while (rs.next()) {
                u.setUser_age(rs.getInt("user_age"));
                u.setUser_phonenumber(rs.getString("user_phonenumber"));
                u.setUser_card(rs.getString("user_card"));
                u.setUser_occupation(rs.getString("user_occupation"));
                u.setUser_location(rs.getString("user_location"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MysqlUtil.closeAll(conn, ps, rs);//关闭相关操作
        }
        return u;
    }

    public boolean login(String user_nickname, String user_password) {
        String login = "select * from  userinfo where user_nickname=? and user_password=?";
        int flag = -1;
        try {
            conn = MysqlUtil.getCoon();
            if (conn != null) {
                ps = conn.prepareStatement(login);
                ps.setString(1, user_nickname);
                ps.setString(2, user_password);
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

    public boolean check(String user_nickname) {
        String check = "select * from  userinfo where user_nickname=?";
        int flag = -1;
        try {
            conn = MysqlUtil.getCoon();
            if (conn != null) {
                ps = conn.prepareStatement(check);
                ps.setString(1, user_nickname);
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

    public String getpassword(String user_nickname) {
        Userinfo userinfo = new Userinfo();
        String check = "select user_password from  userinfo where user_nickname=?";
        try {
            conn = MysqlUtil.getCoon();
            if (conn != null) {
                ps = conn.prepareStatement(check);
                ps.setString(1, user_nickname);
                if (ps != null) {
                    rs = ps.executeQuery();
                    if (rs != null) {
                        while (rs.next()) {
                            userinfo.setUser_password(rs.getString("user_password"));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        MysqlUtil.closeAll(conn, ps, rs);//关闭相关操作
        return userinfo.getUser_password();
    }

    public void change_signature(String user_nickname, String user_signature) { //修改资料
        String sql = "update userinfo set user_signature=?where user_nickname=?";
        try {
            conn = MysqlUtil.getCoon();
            if (conn != null) {
                ps = conn.prepareStatement(sql);
                ps.setString(1, user_signature);
                ps.setString(2, user_nickname);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        MysqlUtil.closeAll(conn, ps, rs);
    }

    public String select_signature(String user_nickname) {
        Userinfo userinfo = new Userinfo();
        String check = "select user_signature from  userinfo where user_nickname=?";
        try {
            conn = MysqlUtil.getCoon();
            if (conn != null) {
                ps = conn.prepareStatement(check);
                ps.setString(1, user_nickname);
                if (ps != null) {
                    rs = ps.executeQuery();
                    if (rs != null) {
                        while (rs.next()) {
                            userinfo.setUser_signature(rs.getString("user_signature"));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        MysqlUtil.closeAll(conn, ps, rs);//关闭相关操作
        return userinfo.getUser_signature();
    }

    public int updateHeadpic(String username, InputStream in) {
        Userinfo userinfo = new Userinfo();
        int flag = 1;
        String sql = "update userinfo set user_headpic = ? where user_nickname = ?";
        try {
            conn = MysqlUtil.getCoon();
            if (conn != null) {
                ps = conn.prepareStatement(sql);
                ps.setString(2, username);
                ps.setBlob(1, in);
                ps.executeUpdate();

            }
        } catch (SQLException e) {
            flag = -1;
            e.printStackTrace();
        }
        MysqlUtil.closeAll(conn, ps, rs);
        return flag;
    }
    public Blob getBlob(String username){
        Userinfo userinfo = new Userinfo();
        String sql = "select user_headpic from userinfo where user_nickname = ?";
        Blob picture = null;
        try{
            conn = MysqlUtil.getCoon();
            if (conn != null) {
                ps=conn.prepareStatement(sql);
                ps.setString(1,username);
                rs = ps.executeQuery();
                if (rs.next()){
                    picture = rs.getBlob("user_headpic");

                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return picture;
    }

}
