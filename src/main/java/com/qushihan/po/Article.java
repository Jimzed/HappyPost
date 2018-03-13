package com.qushihan.po;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
public class Article {
    private int id;
    private Integer rootid;
    private String title;
    private String content;
    private Date datetime;
    private Bbsuser user;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "rootid", nullable = true)
    public Integer getRootid() {
        return rootid;
    }

    public void setRootid(Integer rootid) {
        this.rootid = rootid;
    }

    @Basic
    @Column(name = "title", nullable = true, length = 255)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "content", nullable = true, length = -1)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Basic
    @Column(name = "datetime", nullable = true)
    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return id == article.id &&
                Objects.equals(rootid, article.rootid) &&
                Objects.equals(title, article.title) &&
                Objects.equals(content, article.content) &&
                Objects.equals(datetime, article.datetime);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, rootid, title, content, datetime);
    }

    @ManyToOne
    @JoinColumn(name = "userid", referencedColumnName = "userid")
    public Bbsuser getUser() {
        return user;
    }

    public void setUser(Bbsuser user) {
        this.user = user;
    }
}
