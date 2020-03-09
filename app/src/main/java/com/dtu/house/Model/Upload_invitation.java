package com.dtu.house.Model;

public class Upload_invitation {

    private String mdate;
    private String mtime;
    private String mvenue;
    private String mhost;
    private String mheading;
    private String mpre;
    private String mdesc;
    private String mbenefits;
    private String publisherId;
    private String mimage;
    private String mkey;
    private String mname;
    private String ImageUrl;
    private int going;
    private  long time_inmillis;
    private boolean verification;


    public Upload_invitation() {


    }



    public Upload_invitation(String mdate, String mtime, String mvenue, String mhost, String mheading, String mpre, String mdesc, String mbenefits, String publisherId, String mimage, String mkey, int going, String mname, String ImageUrl , long time_inmillis ,boolean verification) {
        this.mdate = mdate;
        this.mname = mname;
        this.ImageUrl = ImageUrl;
        this.mtime = mtime;
        this.mvenue = mvenue;
        this.mhost = mhost;
        this.mheading = mheading;
        this.mpre = mpre;
        this.mdesc = mdesc;
        this.mbenefits = mbenefits;
        this.publisherId = publisherId;
        this.mimage = mimage;
        this.mkey = mkey;
        this.going = going;
        this.time_inmillis = time_inmillis;
        this.verification = verification;

    }

    //getter


    public boolean isVerification() {
        return verification;
    }

    public String getMname() {
        return mname;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public long getTime_inmillis() {
        return time_inmillis;
    }

    public String getMdate() {
        return mdate;
    }

    public String getMtime() {
        return mtime;
    }

    public String getMvenue() {
        return mvenue;
    }

    public String getMhost() {
        return mhost;
    }

    public String getMheading() {
        return mheading;
    }

    public String getMpre() {
        return mpre;
    }

    public String getMdesc() {
        return mdesc;
    }

    public String getMbenefits() {
        return mbenefits;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public String getMimage() {
        return mimage;
    }

    public String getMkey() {
        return mkey;
    }

    public int getGoing() {
        return going;
    }

    //setter


    public void setVerification(boolean verification) {
        this.verification = verification;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public void setTime_inmillis(long time_inmillis) {
        this.time_inmillis = time_inmillis;
    }

    public void setMdate(String mdate) {
        this.mdate = mdate;
    }

    public void setMtime(String mtime) {
        this.mtime = mtime;
    }

    public void setMvenue(String mvenue) {
        this.mvenue = mvenue;
    }

    public void setMhost(String mhost) {
        this.mhost = mhost;
    }

    public void setMheading(String mheading) {
        this.mheading = mheading;
    }

    public void setMpre(String mpre) {
        this.mpre = mpre;
    }

    public void setMdesc(String mdesc) {
        this.mdesc = mdesc;
    }

    public void setMbenefits(String mbenefits) {
        this.mbenefits = mbenefits;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public void setMimage(String mimage) {
        this.mimage = mimage;
    }

    public void setMkey(String mkey) {
        this.mkey = mkey;
    }

    public void setGoing(int going) {
        this.going = going;
    }
}
