package com.example.chata6.model;

public class User {

    private String id;
    private String name;
    private String search;
    private String imageURL;

    public User(String id, String name, String search, String imageURL) {
        this.id = id;
        this.name = name;
        this.search = search;
        this.imageURL = imageURL;
    }

    public User() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }


    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
