package com.realestate_userapp.Models;

public class CategoryModel {

    String id, imageUrl, type;

    public CategoryModel(String id, String imageurl, String type) {
        this.id = id;
        this.imageUrl = imageurl;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
