package com.lanxiao.doapp.entity;

import com.lidroid.xutils.db.annotation.Id;

/**
 * Created by Thinkpad on 2016/2/28.
 */
public class PersonSignInLogItem {
    @Id
    private int id;
    private String userName;
    private String userpic;

    public String getUserpic() {
        return userpic;
    }

    public void setUserpic(String userpic) {
        this.userpic = userpic;
    }

    private String singintime;
    private String signaddress;
    private String signbeiju;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSingintime() {
        return singintime;
    }

    public void setSingintime(String singintime) {
        this.singintime = singintime;
    }

    public String getSignaddress() {
        return signaddress;
    }

    public void setSignaddress(String signaddress) {
        this.signaddress = signaddress;
    }

    public String getSignbeiju() {
        return signbeiju;
    }

    public void setSignbeiju(String signbeiju) {
        this.signbeiju = signbeiju;
    }

    public String getSignpic() {
        return signpic;
    }

    public void setSignpic(String signpic) {
        this.signpic = signpic;
    }

    private String signpic;


}
