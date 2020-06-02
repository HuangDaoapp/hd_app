package com.example.hisland.Bean;

import java.sql.Date;
public class Commentinfo {

    private String user;
    private String dtuser;
    private String info,title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDtuser() {
        return dtuser;
    }

    public void setDtuser(String dtuser) {
        this.dtuser = dtuser;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
