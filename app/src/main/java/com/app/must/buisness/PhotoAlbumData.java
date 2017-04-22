package com.app.must.buisness;

public class PhotoAlbumData {
    private int id;
    private int image_id;
    private String subject;

    public PhotoAlbumData(int tid, int imgid, String sub) {
        this.id = tid;
        this.image_id = imgid;
        this.subject = sub;
    }

    public int getID() {
        return this.id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public int getImageID() {
        return this.image_id;
    }

    public void setImageID(int id) {
        this.image_id = id;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String title) {
        this.subject = title;
    }
}
