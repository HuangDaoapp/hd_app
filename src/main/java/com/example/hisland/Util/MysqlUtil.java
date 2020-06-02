package com.example.hisland.Util;


import java.sql.Connection;
import java.sql.PreparedStatement;


import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MysqlUtil {
    private static final String driver = "com.mysql.jdbc.Driver";//mysql驱动
    private static final String url = "jdbc:mysql:/xxxxx?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true";
    private static final String username = "root";
    private static final String password = "xxxx";
    public static Connection getCoon(){
        Connection  con= null;
                try {
                    Class.forName("com.mysql.jdbc.Driver");//获取mysql驱动

                }catch (ClassNotFoundException e){
                    e.printStackTrace();
                }try {
                    con = DriverManager.getConnection(url,username,password);
                }
                catch (SQLException e){
                    e.printStackTrace();
                }


        return con;
    }
    public static void closeAll(Connection conn, PreparedStatement ps){
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
    public static void closeAll(Connection conn, PreparedStatement ps, ResultSet rs){
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
