package com.qushihan.po;

import java.io.Serializable;
import java.util.List;

public class PageBean implements Serializable {
    private int curPage; // 当前是第几页
    private int maxPage; // 总页数
    private long maxRowCount; // 总行数
    private int rowsPerpage; // 每页多少行
    private List<Article> data; // 每页的数据

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    public long getMaxRowCount() {
        return maxRowCount;
    }

    public void setMaxRowCount(long maxRowCount) {
        this.maxRowCount = maxRowCount;
    }

    public int getRowsPerpage() {
        return rowsPerpage;
    }

    public void setRowsPerpage(int rowsPerpage) {
        this.rowsPerpage = rowsPerpage;
    }

    public List<Article> getData() {
        return data;
    }

    public void setData(List<Article> data) {
        this.data = data;
    }
}
