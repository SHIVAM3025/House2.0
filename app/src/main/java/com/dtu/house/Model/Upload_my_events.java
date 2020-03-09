package com.dtu.house.Model;

public class Upload_my_events {

    private String mAuth;
    private String heading;
    private String mImageurl;
    private String mvenue;
    private String mdate;
    private String mtime;
    private String mdesc;
    private String key;
    private long time_in_millis;

    public Upload_my_events() {
    }

    public Upload_my_events(String mAuth, String heading, String mImageurl, String mvenue, String mdate, String mtime, String mdesc , long time_in_millis) {
        this.mAuth = mAuth;
        this.heading = heading;
        this.mImageurl = mImageurl;
        this.mvenue = mvenue;
        this.mdate = mdate;
        this.mtime = mtime;
        this.mdesc = mdesc;
        this.time_in_millis = time_in_millis;
    }



    //getter


    public long getTime_in_millis() {
        return time_in_millis;
    }

    public String getmAuth() {
        return mAuth;
    }

    public String getHeading() {
        return heading;
    }

    public String getmImageurl() {
        return mImageurl;
    }

    public String getMvenue() {
        return mvenue;
    }

    public String getMdate() {
        return mdate;
    }

    public String getMtime() {
        return mtime;
    }

    public String getMdesc() {
        return mdesc;
    }

    public String getKey() {
        return key;
    }

    //setter


    public void setTime_in_millis(long time_in_millis) {
        this.time_in_millis = time_in_millis;
    }

    public void setmAuth(String mAuth) {
        this.mAuth = mAuth;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public void setmImageurl(String mImageurl) {
        this.mImageurl = mImageurl;
    }

    public void setMvenue(String mvenue) {
        this.mvenue = mvenue;
    }

    public void setMdate(String mdate) {
        this.mdate = mdate;
    }

    public void setMtime(String mtime) {
        this.mtime = mtime;
    }

    public void setMdesc(String mdesc) {
        this.mdesc = mdesc;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
