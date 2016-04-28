package com.easemob.easeui;

/**
 * Created by Thinkpad on 2016/1/22.
 */
public class Conversion {
    private String nickName;
    private String headUrl;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    private static Conversion conversion=new Conversion();
    public static Conversion getInstance(){
        return conversion;
    }

}
