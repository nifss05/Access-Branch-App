package com.example.accessapp;

public class GalleryFirebase {
    String uri,key;
    public GalleryFirebase(String uri, String key) {
        this.uri = uri;
        this.key = key;
        //nifal
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


}
