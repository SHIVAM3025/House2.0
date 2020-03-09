package com.dtu.house.Model;

public class Upload_Personal_promote {

    private String mImageUrl;
    private int like;

    private String mkey;

    public Upload_Personal_promote(String mImageUrl,  int like) {
        this.mImageUrl = mImageUrl;

        this.like = like;

    }

    public Upload_Personal_promote() {
    }

    //getter


    public int getLike() {
        return like;
    }

    public String getMkey() {
        return mkey;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }



    //setter


    public void setLike(int like) {
        this.like = like;
    }

    public void setMkey(String mkey) {
        this.mkey = mkey;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }


}
