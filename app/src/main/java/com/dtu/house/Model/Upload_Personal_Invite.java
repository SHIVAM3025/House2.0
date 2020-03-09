package com.dtu.house.Model;

public class Upload_Personal_Invite {

    private String mImageUrl;
    private String mkey;
    private int going;
    private String mheading;



    public Upload_Personal_Invite(String mImageUrl, int going, String mheading) {
        this.mImageUrl = mImageUrl;
        this.going = going;
        this.mheading = mheading;
    }

    public Upload_Personal_Invite() {
    }

    //getter


    public String getmImageUrl() {
        return mImageUrl;
    }

    public String getMkey() {
        return mkey;
    }

    public int getGoing() {
        return going;
    }

    public String getMheading() {
        return mheading;
    }

    //setter


    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public void setMkey(String mkey) {
        this.mkey = mkey;
    }

    public void setMheading(String mheading) {
        this.mheading = mheading;
    }

    public void setGoing(int going) {
        this.going = going;
    }
}
