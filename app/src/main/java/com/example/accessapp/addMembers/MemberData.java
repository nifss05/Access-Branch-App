package com.example.accessapp.addMembers;

public class MemberData {
    String name,email,post,img,key;

    public MemberData() {
    }

    public MemberData(String name, String email, String post,String img, String key) {
        this.name = name;
        this.email = email;
        this.post = post;
        this.img=img;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
