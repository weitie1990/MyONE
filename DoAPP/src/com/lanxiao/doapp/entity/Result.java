package com.lanxiao.doapp.entity;

/**
 * Created by Thinkpad on 2015/12/23.
 */
public class Result {
    public String result;
    public String message;
    public String bodyvalue;

    public Result(String result, String message, String bodyvalue) {
        this.result = result;
        this.message = message;
        this.bodyvalue = bodyvalue;
    }
    public Result() {

    }

    public String getBodyvalue() {
        return bodyvalue;
    }

    public void setBodyvalue(String bodyvalue) {
        this.bodyvalue = bodyvalue;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
