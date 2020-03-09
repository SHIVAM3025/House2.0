package com.dtu.house.Model;

public class Upload_promo {

    private String mtitle;
    private String mImage;
    private String mdesc;
    private String mkey;
    private String publisherId;
    private int going;
    private String mname;
    private String ImageUrl;


    public Upload_promo() {
    }

    public Upload_promo(String mtitle, String mImage, String mdesc, String mkey, String publisherId, int going, String mname, String ImageUrl) {
        this.mtitle = mtitle;
        this.mImage = mImage;
        this.mname = mname;
        this.ImageUrl = ImageUrl;
        this.mdesc = mdesc;
        this.mkey = mkey;
        this.publisherId = publisherId;
        this.going = going;

    }

    //getter


    public String getMname() {
        return mname;
    }

    public String getImageUrl() {
        return ImageUrl;
    }


    public String getMtitle() {
        return mtitle;
    }

    public String getmImage() {
        return mImage;
    }

    public String getMdesc() {
        return mdesc;
    }


    public String getMkey() {
        return mkey;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public int getGoing() {
        return going;
    }

    //setter


    public void setMname(String mname) {
        this.mname = mname;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }


    public void setMtitle(String mtitle) {
        this.mtitle = mtitle;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }

    public void setMdesc(String mdesc) {
        this.mdesc = mdesc;
    }

    public void setMkey(String mkey) {
        this.mkey = mkey;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public void setGoing(int going) {
        this.going = going;
    }
}
