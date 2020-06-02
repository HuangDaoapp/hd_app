package com.example.hisland.Bean;

public class Friendlist  {
    private String friendname;
    private int imageid;
    private String comusername;

    public String getComusername() {
        return comusername;
    }

    public Friendlist(String friendname, int imageid, String comusername) {
        this.friendname = friendname;
        this.imageid = imageid;
        this.comusername = comusername;
    }

    public String getFriendname() {
        return friendname;
    }

    public int getImageid() {
        return imageid;
    }
}
