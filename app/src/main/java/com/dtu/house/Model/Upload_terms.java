package com.dtu.house.Model;

public class Upload_terms {

    private String Heading;
    private String Desc;

    public Upload_terms() {
    }

    public Upload_terms(String heading, String desc) {
        Heading = heading;
        Desc = desc;
    }

    //getter


    public String getHeading() {
        return Heading;
    }

    public String getDesc() {
        return Desc;
    }

    //setter


    public void setHeading(String heading) {
        Heading = heading;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }
}
