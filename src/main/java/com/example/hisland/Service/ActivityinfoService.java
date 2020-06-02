package com.example.hisland.Service;

import com.example.hisland.Bean.Activityinfo;
import com.example.hisland.Bean.Userinfo;
import com.example.hisland.Util.MysqlUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
public class ActivityinfoService {
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    public static ActivityinfoService activityinfoService = null;
    public ActivityinfoService(){

    }
    /**
     * 获取MySQL数据库单例类对象
     * */
    public static ActivityinfoService getActivityinfoService(){
        if (activityinfoService==null){
            activityinfoService=new ActivityinfoService();
        }return  activityinfoService;
    }
    public List<Activityinfo> getActivityData() {
        List<Activityinfo> list=new ArrayList<>();
        //MySQL 语句
        String sql="select * from activityinfo";
        try{
            conn= MysqlUtil.getCoon();
            if (conn!=null){
                ps=conn.prepareStatement(sql);
                if (ps!=null){
                    rs=ps.executeQuery();
                    if (rs!=null){
                        while (rs.next()){
                            Activityinfo activity = new Activityinfo();
                            activity.setActivity_id(rs.getNString("activity_id"));
                            activity.setUser_id(rs.getString("user_id"));
                            activity.setActivity_time(rs.getDate("activity_time"));
                            activity.setActivity_text(rs.getString("activity_text"));
                            activity.setActivity_url(rs.getString("activity_url"));
                            activity.setActivity_count(rs.getInt("activity_count"));
                            list.add(activity);

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
    public int delActivityByid(String id){//根据动态id删除动态
        int flag=1;
        String sql = "delete from activityinfo where activity_id=?";
        if (id!=null){
            try{
                conn=MysqlUtil.getCoon();
                if (conn!=null){
                    ps=conn.prepareStatement(sql);
                    ps.setString(1,id);
                    ps.executeUpdate();
                }
            } catch (SQLException e) {
                flag = -1;
                e.printStackTrace();
            }
        }
        MysqlUtil.closeAll(conn,ps);//关闭相关操作
        return flag;
    }
    public int publishActivity(Activityinfo activityinfo){
        int flag = 1;
        String sql = "insert into activityinfo(activity_id,user_id,activity_time,activity_text"+
                ",activity_url,activity_count) values(?,?,?,?,?,?)";
        try{
            conn=MysqlUtil.getCoon();
            if (conn!=null){
                ps=conn.prepareStatement(sql);
                ps.setString(1,activityinfo.getActivity_id());
                ps.setString(2,activityinfo.getUser_id());
                ps.setDate(3,activityinfo.getActivity_time());
                ps.setString(4,activityinfo.getActivity_text());
                ps.setString(5,activityinfo.getActivity_url());
                ps.setInt(6,activityinfo.getActivity_count());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            flag = -1;
            e.printStackTrace();
        }
        MysqlUtil.closeAll(conn,ps);//关闭相关操作
        return flag;
    }
}
