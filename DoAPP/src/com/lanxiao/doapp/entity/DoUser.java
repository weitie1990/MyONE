package com.lanxiao.doapp.entity;

import java.io.Serializable;

/**
 * Created by Thinkpad on 2016/1/20.
 */
public class DoUser implements Serializable {
    private String id;
    private String nickName;
    private String aver;
    private String unionid;

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAver() {
        return aver;
    }

    public void setAver(String aver) {
        this.aver = aver;
    }
}
