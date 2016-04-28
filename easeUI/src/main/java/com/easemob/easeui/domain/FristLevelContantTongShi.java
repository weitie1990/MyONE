package com.easemob.easeui.domain;

import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.NotNull;

import java.io.Serializable;

/**
 * Created by Thinkpad on 2016/4/23.
 */
public class FristLevelContantTongShi implements Serializable {
    @NotNull
    @NoAutoIncrement
    private int id;
    private String fullname;
    private int pid;
    private String name;

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
