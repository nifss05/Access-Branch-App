package com.example.accessapp;

public class NoticeData {
    String title,img,date,time,key;

    public NoticeData() {
    }

    public NoticeData(String title, String img, String date, String time, String key) {
        this.title = title;
        this.img = img;
        this.date = date;
        this.time = time;
        this.key = key;
    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
