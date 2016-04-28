package com.lanxiao.doapp.entity;

/**
 * Created by Thinkpad on 2016/4/20.
 */
public class Work {
    private int id;
    private String name;
    private String type;
    private String target   ;
    private String logo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
