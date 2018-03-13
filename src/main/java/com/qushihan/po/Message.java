package com.qushihan.po;

import java.util.Date;

public class Message {
    private Integer id;// 发主贴的用户ID
    private Integer rid;// 发回贴的用户id
    private String txt; // 把主贴title当成内容发送
    private Date sendTime;
    private String title;// 从贴的title

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
