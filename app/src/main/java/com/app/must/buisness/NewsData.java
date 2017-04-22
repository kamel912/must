package com.app.must.buisness;

public class NewsData {
    private int news_id;
    private String path;
    private String title;

    public NewsData(int id, String tit, String Patho) {
        this.news_id = id;
        this.title = tit;
        this.path = Patho;
    }

    public int getID() {
        return this.news_id;
    }

    public void setID(int id) {
        this.news_id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
