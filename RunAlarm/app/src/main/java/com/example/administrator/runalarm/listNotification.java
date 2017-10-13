package com.example.administrator.runalarm;

/**
 * Created by Administrator on 01/10/2017.
 */

public class listNotification {
    private String mDate;
    private String mTitle;
    private String mContent;
    private String mTable;
    private String mID;

    public listNotification(String mDate, String mTitle, String mContent, String mTable,String mID) {
        this.mDate = mDate;
        this.mTitle = mTitle;
        this.mContent = mContent;
        this.mTable = mTable;
        this.mID = mID;
    }

    public String getDate() {
        return mDate;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getContent() {
        return mContent;
    }

    public String getTable() {
        return mTable;
    }

    public String getID() {
        return mID;
    }

    public void setDate(String date) {
        this.mDate = date;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public void setContent(String content) {
        this.mContent = content;
    }

    public void setTable(String table) {
        this.mTable = table;
    }

    public void setID(String id) {
        this.mID = id;
    }
}
