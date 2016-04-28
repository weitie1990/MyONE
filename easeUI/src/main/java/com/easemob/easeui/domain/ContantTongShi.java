package com.easemob.easeui.domain;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Foreign;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.NotNull;

import java.io.Serializable;

/**
 * Created by Thinkpad on 2016/4/23.
 */
public class ContantTongShi {
    @NotNull
    @NoAutoIncrement
    private int id;
    private String fullname;
    private int pid;
    private String name;

    /*public FristLevelContantTongShi getParent() {
        return parent;
    }

    public void setParent(FristLevelContantTongShi parent) {
        this.parent = parent;
    }*/

    public int getTongshiId() {
        return tongshiId;
    }

    public void setTongshiId(int tongshiId) {
        this.tongshiId = tongshiId;
    }

  /*  @Foreign(column = "tongshi_id", foreign = "id")//前面id为存在本表里面外键的字段名(company_id)，后面id为对应父表主键字段名
    public FristLevelContantTongShi parent;*/
    @Column(column = "tongshi_id")
    private int tongshiId;
    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
