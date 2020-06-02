package com.example.hisland.Service;

import com.example.hisland.Bean.Commentinfo;
import com.example.hisland.Bean.Comunity;
import com.example.hisland.Util.MysqlUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommentinfoService {
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    public  CommentinfoService(){

    }
    public static CommentinfoService commentinfoService = null;
    public static CommentinfoService commentinfoService(){
        if (commentinfoService==null){
            commentinfoService = new CommentinfoService();
        }
        return commentinfoService;
    }
    public int publishComment(Commentinfo commentinfo){
        int flag = 1;
        String sql="insert into commentinfo(dtuser,user,info,title"+
                ") values (?,?,?,?)";
        try{
            conn = MysqlUtil.getCoon();
            if (conn!=null){
                ps = conn.prepareStatement(sql);
                ps.setString(1,commentinfo.getDtuser());
                ps.setString(2,commentinfo.getUser());
                ps.setString(3,commentinfo.getInfo());
                ps.setString(4,commentinfo.getTitle());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            flag = -1;
            e.printStackTrace();
        }
        return flag;
    }
    public void deleteComment(){
//        int flag = 1;
//        String sql = "delete from commentinfo where comment_id =?";
//        try{
//            conn = MysqlUtil.getCoon();
//            if (conn!=null){
//                ps=conn.prepareStatement(sql);
//                ps.setString(1,id);
//                ps.executeUpdate();
//            }
//        } catch (SQLException e) {
//            flag = -1;
//            e.printStackTrace();
//        }
//        return flag;
      }
    public List<Commentinfo> getComment(String dtuser,String title) {
        List<Commentinfo> list=new ArrayList<>();
        //MySQL 语句
        String sql="select * from commentinfo where dtuser=? and title=?";
        try{
            conn= MysqlUtil.getCoon();
            if (conn!=null){
                ps=conn.prepareStatement(sql);
                ps.setString(1,dtuser);
                ps.setString(2,title);
                if (ps!=null){
                    rs=ps.executeQuery();
                    if (rs!=null){
                        while (rs.next()){
                            Commentinfo com = new Commentinfo();
                            com.setInfo(rs.getString("info"));
                            com.setDtuser(rs.getString("dtuser"));
                            com.setUser(rs.getString("user"));
                            com.setTitle(rs.getString("title"));
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
}
