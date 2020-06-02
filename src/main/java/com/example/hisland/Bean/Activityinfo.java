package com.example.hisland.Bean;

import java.sql.Date;
public class Activityinfo {
    private String activity_id;
    private String user_id;
    private Date activity_time;
    private String activity_text;
    private String activity_url;
    private int activity_count;

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Date getActivity_time() {
        return activity_time;
    }

    public void setActivity_time(Date activity_time) {
        this.activity_time = activity_time;
    }

    public String getActivity_text() {
        return activity_text;
    }

    public void setActivity_text(String activity_text) {
        this.activity_text = activity_text;
    }

    public String getActivity_url() {
        return activity_url;
    }

    public void setActivity_url(String activity_url) {
        this.activity_url = activity_url;
    }

    public int getActivity_count() {
        return activity_count;
    }

    public void setActivity_count(int activity_count) {
        this.activity_count = activity_count;
    }


}
