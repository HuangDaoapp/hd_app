package com.example.hisland.Bean;

public class Messagelist {
    private String friendname;
    private int imageid;
    private String friendmessage;
    private String username;

    public String getUsername() {
        return username;
    }

    public Messagelist(String friendname, int imageid, String friendmessage,String username) {
        this.friendname = friendname;
        this.imageid = imageid;
        this.friendmessage = friendmessage;
        this.username=username;
    }

    public String getFriendname() {
        return friendname;
    }

    public int getImageid() {
        return imageid;
    }

    public String getFriendmessage() {
        return friendmessage;
    }
}
