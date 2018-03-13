package com.qushihan.po;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Bbsuser {
    private int userid;
    private String username;
    private String password;
    private byte[] pic;
    private Integer pagenum;
    private String picPath;
    private Set<Article> articles = new HashSet<>();

    public Bbsuser() {
    }

    public Bbsuser(int userid) {
        this.userid = userid;
    }

    public Bbsuser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Id
    @Column(name = "userid", nullable = false)
    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    @Basic
    @Column(name = "username", nullable = false, length = 20)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "password", nullable = false, length = 20)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "pic", nullable = true)
    public byte[] getPic() {
        return pic;
    }

    public void setPic(byte[] pic) {
        this.pic = pic;
    }

    @Basic
    @Column(name = "PAGENUM", nullable = true)
    public Integer getPagenum() {
        return pagenum;
    }

    public void setPagenum(Integer pagenum) {
        this.pagenum = pagenum;
    }

    @Basic
    @Column(name = "pic_path", nullable = true, length = 255)
    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bbsuser bbsuser = (Bbsuser) o;
        return userid == bbsuser.userid &&
                Objects.equals(username, bbsuser.username) &&
                Objects.equals(password, bbsuser.password) &&
                Arrays.equals(pic, bbsuser.pic) &&
                Objects.equals(pagenum, bbsuser.pagenum) &&
                Objects.equals(picPath, bbsuser.picPath);
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(userid, username, password, pagenum, picPath);
        result = 31 * result + Arrays.hashCode(pic);
        return result;
    }

    @OneToMany(mappedBy = "user")
    public Set<Article> getArticles() {
        return articles;
    }

    public void setArticles(Set<Article> articles) {
        this.articles = articles;
    }
}
