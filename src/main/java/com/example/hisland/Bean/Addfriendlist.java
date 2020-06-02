package com.example.hisland.Bean;

public class Addfriendlist {
    private String addusername;
    private String addfriendname;
    private String addtext;

    public Addfriendlist(String addusername, String addfriendname, String addtext) {
        this.addusername = addusername;
        this.addfriendname = addfriendname;
        this.addtext = addtext;
    }
    public String getAddusername() {
        return addusername;
    }
    public String getAddfriendname() {
        return addfriendname;
    }
    public String getAddtext() {
        return addtext;
    }
}
